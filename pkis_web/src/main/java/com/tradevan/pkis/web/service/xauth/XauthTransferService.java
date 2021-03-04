package com.tradevan.pkis.web.service.xauth;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.ReadContext;
import com.tradevan.mapper.contract.dao.ContractMapper;
import com.tradevan.mapper.pkis.dao.ContracttransferMapper;
import com.tradevan.mapper.pkis.model.Contracttransfer;
import com.tradevan.mapper.xauth.dao.XauthMapper;
import com.tradevan.mapper.xauth.dao.XauthRoleUserMapper;
import com.tradevan.mapper.xauth.dao.XauthTransferMapper;
import com.tradevan.mapper.xauth.model.XauthRoleUser;
import com.tradevan.mapper.xauth.model.XauthUsers;
import com.tradevan.pkis.web.service.common.CommonService;
import com.tradevan.pkis.web.service.contract.ContractService;
import com.tradevan.pkis.web.util.ElasticSearchUtil;
import com.tradevan.pkis.web.util.JsonUtil;
import com.tradevan.xauthframework.common.bean.ProcessResult;
import com.tradevan.xauthframework.common.utils.MapUtils;
import com.tradevan.xauthframework.core.common.LocaleMessage;
import com.tradevan.xauthframework.core.enums.MSG_KEY;
import com.tradevan.xauthframework.dao.bean.GridResult;
import com.tradevan.xauthframework.web.service.DefaultService;

@Service("XauthTransferService")
@Transactional(rollbackFor=Exception.class)
public class XauthTransferService extends DefaultService {
	
	@Autowired
	ContracttransferMapper contracttransferMapper;
	
	@Autowired
	XauthMapper xauthMapper;
	
	@Autowired
	ElasticSearchUtil elasticSearchUtil;
	
	@Autowired
	XauthRoleUserMapper xauthRoleUserMapper;
	
	@Autowired
	ContractMapper contractMapper;
	
	@Autowired
	ContractService contractService;
	
	@Autowired
	XauthTransferMapper xauthTransferMapper;
	
	@Autowired
	CommonService commonService;
	
	/**
	 * 轉移清單合約查詢
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public GridResult selectTransferList(Map<String, Object> params) throws Exception {
		GridResult gridResult = null;
		Map<String, Object> contractidmap = new HashMap<String, Object>();
		Map<String, Integer> contractcount = new HashMap<String, Integer>();
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();

		String idenid = "";
		String roleid = "";
		String userid = MapUtils.getString(params, "userId");
		String feidenid = MapUtils.getString(params, "idenId");
		String feroleid = MapUtils.getString(params, "roleId");
		String years = MapUtils.getString(params, "years");
		String fsection = MapUtils.getString(params, "section");
		String suppliergui = MapUtils.getString(params, "suppliergui");
		String suppliercode = MapUtils.getString(params, "suppliercode");
		String contractmodel = MapUtils.getString(params, "module");
		// 前端無參數填入設定
		if (StringUtils.isBlank(years)) {
			years = "X";
		}

		Map<String, Object> compParams = new HashMap<String, Object>();
		compParams.put("userid", userid);

		QueryWrapper<XauthRoleUser> queryWrapper = new QueryWrapper<XauthRoleUser>();
		queryWrapper.eq("USER_ID", userid);
		// 兩者皆為空值的情況
		if (StringUtils.isBlank(feidenid)) {
			List<XauthRoleUser> list = xauthRoleUserMapper.selectList(queryWrapper);
			if (list.size() > 0) {
				idenid = list.get(0).getIdenId();
				roleid = list.get(0).getRoleId();
				compParams.put("idenid", idenid);
				compParams.put("roleid", roleid);
			}
		} else {
			idenid = feidenid;
			if (StringUtils.isBlank(feroleid)) {
				queryWrapper.eq("IDEN_ID", idenid);
				List<XauthRoleUser> list = xauthRoleUserMapper.selectList(queryWrapper);
				if (list.size() > 0) {
					roleid = list.get(0).getRoleId();
					compParams.put("roleid", roleid);
				}
			} else {
				roleid = feroleid;
				compParams.put("roleid", roleid);
			}
			compParams.put("idenid", idenid);
		}

		if (roleid != null && !roleid.equals("")) {
			compParams.put("roleid", roleid);
		}
		if (contractmodel != null && !contractmodel.equals("")) {
			compParams.put("contractmodel", contractmodel);
		}
		if (suppliergui != null && !suppliergui.equals("")) {
			compParams.put("suppliergui", suppliergui);
		}
		if (suppliercode != null && !suppliercode.equals("")) {
			compParams.put("suppliercode", suppliercode);
		}
		List<Map<String, Object>> contractList = xauthTransferMapper.selectallContract(compParams);
		Set<String> checkDataid = new HashSet<String>();
		if (contractList.size() > 0) {
			for (Map<String, Object> action : contractList) {
				String dataId = MapUtils.getString(action, "DATAID");
				String nowUserId = MapUtils.getString(action, "NOWUSERID");
				String nowRoles = MapUtils.getString(action, "NOWROLES");
				// 皆有值的情況下為移轉單據 且前端傳的部門為空值
				if (StringUtils.isNotBlank(nowUserId) && StringUtils.isNotBlank(nowRoles)
						&& StringUtils.isNotBlank(feidenid) && StringUtils.isNotBlank(feroleid)) {
					if ((nowRoles.indexOf(roleid) > -1) && (nowUserId.indexOf(userid) > -1)) {
					} else {
						continue;
					}
				}
				// 當前端未填角色ID
				if (StringUtils.isNotBlank(nowRoles) && StringUtils.isNotBlank(feroleid)) {
					if (!(nowRoles.indexOf(roleid) > -1)) {
						continue;
					}
				}

				if (StringUtils.isNotBlank(nowUserId)) {
					if (!(nowUserId.indexOf(userid) > -1)) {
						continue;
					}
				}
				String flowstatus = MapUtils.getString(action, "FLOWSTATUS");
				String indexName = MapUtils.getString(action, "INDEXNAME");

				// 判斷合約是否為草稿狀態
				if (StringUtils.equals(flowstatus, "DRAFT")) {
					// EL查詢todo
					String esJson = elasticSearchUtil.serachById(dataId, indexName);
					esJson = JsonUtil.jsonSkipToString(esJson);
					// 關閉連線
					elasticSearchUtil.closeESClient();

					JsonObject jsonObj = new JsonParser().parse(esJson).getAsJsonObject();
					String todo = jsonObj.get("todo").getAsString();
					if (StringUtils.equals(todo, "作廢")) {
						continue;
					}
				}

				StringBuffer contractkey = new StringBuffer();

				// 合約年度
				String year = MapUtils.getString(action, "YEAR");
				if (!(checkYear(year, years))) {
					continue;
				}
				String section = MapUtils.getString(action, "DEPTCODE");
				if (section == null) {
					section = "X";
				}
				if (fsection != null && !(fsection.indexOf(section) > -1)) {
					continue;
				}
				// 合約模式
				contractmodel = MapUtils.getString(action, "CONTRACTMODELNAME");
				// 合約範本
				String disp = MapUtils.getString(action, "FLOWNAME");
				// 取得課別
				String deptname = MapUtils.getString(action, "DEPTNAME");
				if (deptname.equals("-") || deptname == null) {
					deptname = "";
				}
				// 供應商統編
				suppliergui = MapUtils.getString(action, "SUPPLIERGUI");

				// 供應商廠編
				suppliercode = MapUtils.getString(action, "SUPPLIERCODE");

				// 供應商名稱
				String suppliercname = MapUtils.getString(action, "SUPPLIERCNAME");
				// 角色資訊
				String contactPerson = MapUtils.getString(action, "DISPORD").equals("1") ? "Y" : "N";

				String dispprd = MapUtils.getString(action, "DISPORD");

				contractkey.append(year).append(",")
										.append(contractmodel).append(",")
										.append(disp).append(",")
										.append(deptname).append(",")
										.append(suppliergui).append(",")
										.append(suppliercode).append(",")
										.append(suppliercname).append(",");
				/**放置統計件數與 合約資訊
				  *存放格式dataId& | | | 分隔號#
				  */
				if(contractidmap.get(contractkey.toString()) == null) {
					checkDataid.add(dataId);
					contractcount.put(contractkey.toString(), 1);
					contractidmap.put(contractkey.toString(), dataId + "&" + userid + "|" + idenid + "|" + roleid + "|" + contactPerson + "|" + indexName + "|" + dispprd);

				}
				else {
					if(!checkDataid.contains(dataId)) {
						checkDataid.add(dataId);
						contractcount.put(contractkey.toString(), MapUtils.getInteger(contractcount, contractkey.toString())+1);	
					}
					contractidmap.put(contractkey.toString(), MapUtils.getString(contractidmap, contractkey.toString())+"#"+dataId + "&" + userid + "|" + idenid+"|" + roleid +"|"+ contactPerson+"|" + indexName+"|"+dispprd);
				}
			}
			//放置至前端grid
			String gridarr [] = {
					LocaleMessage.getMsg("contract.field.year"),
					LocaleMessage.getMsg("contractReview.field.contractmodel"),
					LocaleMessage.getMsg("contract.field.dispname"),
					LocaleMessage.getMsg("xauth.field.transfer.section"),
					LocaleMessage.getMsg("suppliter.field.suppliergui"),
					LocaleMessage.getMsg("xauth.field.transfer.suppliercode"),
					LocaleMessage.getMsg("xauth.field.transfer.suppliercname"),
			};
			for (String key : contractcount.keySet()) {
				String setvalue[] = key.split(",");
				Map<String, Object> gridMap = new HashMap<String, Object>();

				for (int i = 0; i < gridarr.length; i++) {
					gridMap.put(gridarr[i], setvalue[i]);
				}
				gridMap.put("Count", contractcount.get(key));
				gridMap.put("Contracts", contractidmap.get(key));
				// 放入合約資訊
				resultList.add(gridMap);
			}
		}	
		// 排序及分頁
		gridResult = this.grid(params);
		ElasticSearchUtil.sortPagination(gridResult, resultList, params);
		
		return gridResult;
	}
	
	/**
	 * 插入轉移人表格
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public ProcessResult setUndertaker(Map<String, Object> params) throws Exception {
		ProcessResult result = new ProcessResult();

		String datas = MapUtils.getString(params, "dataids");
		String transferUserid = MapUtils.getString(params, "transferUserid");
		String transferRoleid = MapUtils.getString(params, "transferRoleid");
		String transferidenId = MapUtils.getString(params, "transferidenId");
		JsonPath jsonPathByUndertakeuser;
		JsonPath jsonPathByDept;

		if (transferUserid == null || transferRoleid == null || transferidenId == null) {
			result.setStatus(ProcessResult.NG);
			result.addMessage("請選擇移轉人資料");
			return result;
		}

		// 格式 dataid&""|""|""#
		if (params != null && params.size() > 0) {
			if (datas != null) {
				String datalist[] = datas.split("\\#");
				for (String data : datalist) {
					String dataid = data.split("&amp;")[0];
					String oriUserId = data.split("&amp;")[1].split("\\|")[0];
					String oriIdenId = data.split("&amp;")[1].split("\\|")[1];
					String oriRoleId = data.split("&amp;")[1].split("\\|")[2];
					String dispord = data.split("&amp;")[1].split("\\|")[5];

					QueryWrapper<Contracttransfer> queryWrapper = new QueryWrapper<Contracttransfer>();
					queryWrapper.eq("DATAID", dataid);
					queryWrapper.eq("UNDERTAKEUSERID", oriUserId);
					queryWrapper.eq("DISPORD", dispord);

					List<Contracttransfer> transferlist = contracttransferMapper.selectList(queryWrapper);
					// 大於0代表有移轉紀錄採用原始的
					if (transferlist.size() > 0) {
						oriUserId = transferlist.get(0).getUserid() == null ? "X" : transferlist.get(0).getUserid();
						oriIdenId = transferlist.get(0).getDeptid() == null ? "X" : transferlist.get(0).getDeptid();
						oriRoleId = transferlist.get(0).getRoleid() == null ? "X" : transferlist.get(0).getRoleid();
					}

					String contactPerson = data.split("&amp;")[1].split("\\|")[3];
					String indexName = data.split("&amp;")[1].split("\\|")[4];

					Contracttransfer contracttransfer = new Contracttransfer();
					contracttransfer.setDataid(dataid);
					if (!oriUserId.equals("X")) {
						contracttransfer.setUserid(oriUserId);
						contracttransfer.setUndertakeuserid(MapUtils.getString(params, "transferUserid"));
					}
					if (!oriRoleId.equals("X")) {
						contracttransfer.setRoleid(oriRoleId);
						contracttransfer.setUndertakeroleid(MapUtils.getString(params, "transferRoleid"));

					}
					contracttransfer.setDeptid(oriIdenId);
					// 設定移轉人資料
					contracttransfer.setUndertakedeptid(MapUtils.getString(params, "transferidenId"));
					contracttransfer.setCrateuser(getCreOrUpdUser(null));
					contracttransfer.setCratedate(new Timestamp(new Date().getTime()));
					contracttransfer.setDispord(dispord);
					contracttransferMapper.insert(contracttransfer);

					// 判斷是否為承辦人打入EL
					if (contactPerson.equals("Y")) {
						// 組Elasticsearch條件
						String esJson = elasticSearchUtil.serachById(dataid, indexName);
						esJson = JsonUtil.jsonSkipToString(esJson);
						logger.info("esJson ===" + esJson);
						// 關閉連線
						elasticSearchUtil.closeESClient();
						esJson = esJson.replaceAll("\\\\", "");

						ReadContext json = JsonPath.parse(esJson);

						// 非制式客製化-EN
						List<Map<String, Object>> resultdataList = json.read(
								"$.data[?(@.datatype == '" + LocaleMessage.getMsg("contractEn.filed.Basic_Information")
										+ "')].docdetail[?(@.resultdata)].resultdata");
						jsonPathByUndertakeuser = JsonPath.compile(
								"$.data[?(@.datatype == '" + LocaleMessage.getMsg("contractEn.filed.Basic_Information")
										+ "')].docdetail[?(@.resultdata)].resultdata.承接人");
						jsonPathByDept = JsonPath.compile(
								"$.data[?(@.datatype == '" + LocaleMessage.getMsg("contractEn.filed.Basic_Information")
										+ "')].docdetail[?(@.resultdata)].resultdata.承接部門");
						// 採原設計方案
						if (resultdataList.size() < 1) {
							jsonPathByUndertakeuser = JsonPath.compile(
									"$.data[?(@.datatype == '基本資料')].docdetail[?(@.resultdata)].resultdata.承接人");
							jsonPathByDept = JsonPath.compile(
									"$.data[?(@.datatype == '基本資料')].docdetail[?(@.resultdata)].resultdata.承接部門");
						}

						DocumentContext documentContext = JsonPath.parse(esJson);
						XauthUsers xauthUsers = new XauthUsers();
						xauthUsers.setAppId(APP_ID);
						xauthUsers.setUserId(transferUserid);
						List<Map<String, Object>> userList = xauthMapper.selectUserRole(xauthUsers);
						// 承接人寫入
						documentContext.set(jsonPathByUndertakeuser, userList.get(0).get("USER_CNAME"));
						// 承接部門寫入
						documentContext.set(jsonPathByDept, userList.get(0).get("IDEN_ID"));

						esJson = documentContext.jsonString();
						ElasticSearchUtil.updateById(dataid, indexName, esJson);
						// 關閉連線
						elasticSearchUtil.closeESClient();
					}
				}
			}
			result.setStatus(ProcessResult.OK);
			result.addMessage(MSG_KEY.INSERT_OK.getMessage());
		}
		else {
			result.setStatus(ProcessResult.NG);
			result.addMessage(MSG_KEY.PARAMS_EMPTY.getMessage());
		}
		return result ;
	}
	
	/**
	 * 判斷查詢年度
	 * String year 
	 * String years
	 * @return
	 */
	public boolean checkYear(String year,String years) {
		boolean result = false;
		if (years.indexOf(year) > -1 || years.equals("X")) {
			result = true;
		}
		return result;

	}
	
	/**
	 * 取得被移轉人(原USERID)
	 * @param underTakeUserId
	 * @param dataid
	 * @return
	 */
	public Map<String , String> getTranferOriUserids(String underTakeUserId,String dataid) {
		Map<String, String> result = new HashMap<String, String>();
		String isAgent = "N";
		String flowuserid = underTakeUserId;
		Map<String, Object> compParams = new HashMap<String, Object>();
		compParams.put("dataid", dataid);
		compParams.put("undertakeuserid", flowuserid);
		List<Map<String, Object>> list = xauthTransferMapper.selectlastTransfer(compParams);
		// 已被轉移就採用原userid
		if (list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				flowuserid = MapUtils.getString(list.get(i), "USERID");
				String agerntuserid = MapUtils.getString(list.get(i), "AGENT_USER_ID");
				if (StringUtils.isNotBlank(agerntuserid)) {
					if (StringUtils.equals(agerntuserid, underTakeUserId)) {
						isAgent = "Y";
					} else {
						isAgent = "N";
					}
				}
				result.put(flowuserid, isAgent);

			}
		}
		return result;
	}
	
	/**
	   * 當前合約持有人判斷
	 * @param dataid
	 * @param nowuser
	 * @return
	 * @throws Exception
	 */
	public boolean isNowContractTaskUser(String dataid, String nowuser) throws Exception {

		boolean result = false;
		Set<String> useridlist = new HashSet<String>();

		Map<String, Object> compParams = new HashMap<String, Object>();
		compParams.put("dataid", dataid);

		List<Map<String, Object>> list = contractMapper.selectContractMaster(compParams);

		String nowtakeUser = MapUtils.getString(list.get(0), "NOWUSERIDS");
		if (nowtakeUser == null) {
			nowtakeUser = "";
		}
		String nowtakeRole = MapUtils.getString(list.get(0), "NOWROLEIDS");
		if (nowtakeRole == null) {
			nowtakeRole = "";
		}

		String nowtakeDept = MapUtils.getString(list.get(0), "NOWDEPTID");
		String nowtakeAgentUserid = MapUtils.getString(list.get(0), "AGENTUSERID");
		// 判斷無USERID時用ROLEID查找資料庫資料
		if (StringUtils.isNotBlank(nowtakeRole) && StringUtils.isBlank(nowtakeUser)) {
			String[] roles = nowtakeRole.split(",");
			for (int i = 0; i < roles.length; i++) {
				List<String> roleUserList = commonService.getUserDatas(roles[i], nowtakeDept);
				if (roleUserList.size() > 0) {
					for (String action : roleUserList) {
						useridlist.add(action);
						useridlist.add(contractService.getAgentUserId(action));
					}
				}
			}
		}

		useridlist.add(contractService.getAgentUserId(nowtakeUser));
		useridlist.add(nowtakeUser);
		useridlist.add(nowtakeAgentUserid);
		for (String userid : useridlist) {
			if (StringUtils.isNotBlank(userid)) {
				if (userid.indexOf(nowuser) > -1) {
					result = true;
				}
			}

		}
		return result;
	}
	
	/**
	 * 取得合約轉移人ID EMAIL送出時的關卡人
	 * @param userid
	 * @param oriuserid
	 * @param dataid
	 * @return
	 * @throws Exception
	 */
	public String getUndertakeId(String userid,String oriuserid,String dataid) {
		String flowuserid = oriuserid;
		Map<String, Object> compParams = new HashMap<String, Object>();
		compParams.put("dataid", dataid);
		compParams.put("userid", flowuserid);
		List<Map<String, Object>> list = xauthTransferMapper.selectlastTransfer(compParams);
		// 已被轉移取得移轉後ID
		if (list.size() > 0) {
			flowuserid = MapUtils.getString(list.get(0), "UNDERTAKEUSERID");
		}
		return flowuserid;
	}
	
	/**
	 * 取得合約userid (roleid查找)
	 * @param userid
	 * @param orirole
	 * @param dataid
	 * @return
	 * @throws Exception
	 */
	public String getUserIdByRole(String userid,String orirole,String dataid) {
		String flowuserid = userid;

		Map<String, Object> compParams = new HashMap<String, Object>();
		compParams.put("dataid", dataid);
		compParams.put("undertakeuserid", userid);
		compParams.put("roleid", orirole);
		List<Map<String, Object>> list = xauthTransferMapper.selectlastTransfer(compParams);
		// 已被轉移就採用原userid
		if (list.size() > 0) {
			flowuserid = MapUtils.getString(list.get(0), "USERID");
		}
		return flowuserid;
	}
	
	/**
	 * 取得合約userid (原始userid查找)
	 * 
	 * @param userid
	 * @param oriuserid
	 * @param dataid
	 * @return
	 * @throws Exception
	 */
	public String getUserIdByUserid(String userid,String oriuserid,String dataid) {
		String flowuserid = userid;

		Map<String, Object> compParams = new HashMap<String, Object>();
		compParams.put("dataid", dataid);
		compParams.put("undertakeuserid", flowuserid);
		compParams.put("userid", oriuserid);
		List<Map<String, Object>> list = xauthTransferMapper.selectlastTransfer(compParams);
		// 已被轉移就採用原userid
		if (list.size() > 0) {
			flowuserid = MapUtils.getString(list.get(0), "USERID");
		}
		return flowuserid;
	}
	/**
	 * 取得合約所有部門
	 * 
	 * @param userid
	 * @param oriuserid
	 * @param dataid
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> getAllSection() {
		List<Map<String, Object>> dataList = null;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("appId", "APPKIS");
		params.put("gp", "DEPT_CODE");
		params.put("sortColumnName", "ORDER_SEQ");
		params.put("sortOrder", "ASC");
		dataList = contractMapper.selectSysCode(params);
		ProcessResult result = new ProcessResult();
		result.addResult("dataList", dataList);
		return dataList;
	}
}

