package com.tradevan.pkis.web.service.contract;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.imageio.ImageIO;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.ReadContext;
import com.tradevan.handyflow.bean.DefaultFlowAction;
import com.tradevan.handyflow.bean.DocStateBean;
import com.tradevan.handyflow.bean.FlowBean;
import com.tradevan.handyflow.service.FlowQueryService;
import com.tradevan.handyflow.service.FlowService;
import com.tradevan.mapper.contract.dao.ContractMapper;
import com.tradevan.mapper.pkis.dao.CodelistMapper;
import com.tradevan.mapper.pkis.dao.ContractfileMapper;
import com.tradevan.mapper.pkis.dao.ContractmasterMapper;
import com.tradevan.mapper.pkis.dao.ContractreviewMapper;
import com.tradevan.mapper.pkis.dao.ContracttransferMapper;
import com.tradevan.mapper.pkis.dao.DesignermgrMapper;
import com.tradevan.mapper.pkis.dao.DocstateMapper;
import com.tradevan.mapper.pkis.dao.DocstatelogMapper;
import com.tradevan.mapper.pkis.dao.EmailqueueMapper;
import com.tradevan.mapper.pkis.dao.FlowstepMapper;
import com.tradevan.mapper.pkis.dao.NegoentryMapper;
import com.tradevan.mapper.pkis.dao.PercontracttemplateMapper;
import com.tradevan.mapper.pkis.dao.ReviewconfMapper;
import com.tradevan.mapper.pkis.dao.ShortagepenaltyMapper;
import com.tradevan.mapper.pkis.dao.SuppliermasterMapper;
import com.tradevan.mapper.pkis.dao.SystemparamMapper;
import com.tradevan.mapper.pkis.model.Codelist;
import com.tradevan.mapper.pkis.model.Contractfile;
import com.tradevan.mapper.pkis.model.Contractmaster;
import com.tradevan.mapper.pkis.model.Contractreview;
import com.tradevan.mapper.pkis.model.Contracttransfer;
import com.tradevan.mapper.pkis.model.Designermgr;
import com.tradevan.mapper.pkis.model.Docstate;
import com.tradevan.mapper.pkis.model.Docstatelog;
import com.tradevan.mapper.pkis.model.Emailqueue;
import com.tradevan.mapper.pkis.model.Flowstep;
import com.tradevan.mapper.pkis.model.Negoentry;
import com.tradevan.mapper.pkis.model.Percontracttemplate;
import com.tradevan.mapper.pkis.model.Reviewconf;
import com.tradevan.mapper.pkis.model.Shortagepenalty;
import com.tradevan.mapper.pkis.model.Suppliermaster;
import com.tradevan.mapper.pkis.model.Systemparam;
import com.tradevan.mapper.xauth.dao.XauthDeptMapper;
import com.tradevan.mapper.xauth.dao.XauthRoleAgentUserMapper;
import com.tradevan.mapper.xauth.dao.XauthRoleMapper;
import com.tradevan.mapper.xauth.dao.XauthRoleUserMapper;
import com.tradevan.mapper.xauth.dao.XauthSysCodeMapper;
import com.tradevan.mapper.xauth.dao.XauthTransferMapper;
import com.tradevan.mapper.xauth.dao.XauthUsersMapper;
import com.tradevan.mapper.xauth.model.XauthDept;
import com.tradevan.mapper.xauth.model.XauthRole;
import com.tradevan.mapper.xauth.model.XauthRoleAgentUser;
import com.tradevan.mapper.xauth.model.XauthRoleUser;
import com.tradevan.mapper.xauth.model.XauthSysCode;
import com.tradevan.mapper.xauth.model.XauthUsers;
import com.tradevan.pkis.web.service.common.CommonService;
import com.tradevan.pkis.web.service.flow.FlowSetService;
import com.tradevan.pkis.web.service.xauth.XauthService;
import com.tradevan.pkis.web.service.xauth.XauthTransferService;
import com.tradevan.pkis.web.util.ElasticSearchUtil;
import com.tradevan.pkis.web.util.JsonUtil;
import com.tradevan.xauthframework.common.bean.ProcessResult;
import com.tradevan.xauthframework.common.utils.ConvertUtils;
import com.tradevan.xauthframework.common.utils.JsonUtils;
import com.tradevan.xauthframework.core.common.LocaleMessage;
import com.tradevan.xauthframework.core.enums.MSG_KEY;
import com.tradevan.xauthframework.core.enums.USER_TYPE;
import com.tradevan.xauthframework.core.security.UserInfo;
import com.tradevan.xauthframework.core.utils.XauthPropUtils;
import com.tradevan.xauthframework.dao.bean.GridResult;
import com.tradevan.xauthframework.web.service.DefaultService;

@Service("ContractService")
@Transactional(rollbackFor = Exception.class)
public class ContractService extends DefaultService {
	static boolean usermatch;
	
	@Autowired
	ContracttransferMapper contracttransferMapper;
	
	@Autowired
	XauthRoleAgentUserMapper xauthRoleAgentUserMapper;
	
	@Autowired
	ShortagepenaltyMapper shortagepenaltyMapper;
	
	@Autowired
	ContractMapper contractMapper;
	
	@Autowired
	XauthSysCodeMapper xauthSysCodeMapper;
	
	@Autowired
	XauthDeptMapper xauthDeptMapper;
	
	@Autowired
	XauthUsersMapper xauthUsersMapper;
	
	@Autowired
	XauthRoleUserMapper xauthRoleUserMapper;
	
	@Autowired
	XauthRoleMapper xauthRoleMapper;
	
	@Autowired
	SuppliermasterMapper suppliermasterMapper;
	
	@Autowired
	ReviewconfMapper reviewconfMapper;
	
	@Autowired
	CodelistMapper codelistMapper;
	
	@Autowired
	ContractmasterMapper contractmasterMapper;
	
	@Autowired
	ContractfileMapper contractfileMapper;
	
	@Autowired
	ContractreviewMapper contractreviewMapper;
	
	@Autowired
	DocstatelogMapper docstatelogMapper;
	
	@Autowired
	DesignermgrMapper designermgrMapper;
	
	@Autowired
	FlowstepMapper flowstepMapper;
	
	@Autowired
	DocstateMapper docstateMapper;
	
	@Autowired	
	NegoentryMapper negoentryMapper;
	
	@Autowired
	EmailqueueMapper emailqueueMapper;
	
	@Autowired
	SystemparamMapper systemparamMapper;
	
	@Autowired
	PercontracttemplateMapper percontracttemplateMapper;
	
	@Autowired
	ElasticSearchUtil elasticSearchUtil;
	
	@Autowired
	XauthService xauthService;
	
	@Autowired
	FlowSetService flowSetService;
	
	@Autowired
	ContractReportService contractReportService;
	
	@Autowired
	private FlowService flowService;
	
	@Autowired
	ContractService contractService;
	
	@Autowired
	private FlowQueryService flowQueryService;
	
	@Autowired  
	private Environment env;  
	
	@Autowired
	XauthTransferService xauthTransferService;
	
	@Autowired
	XauthTransferMapper xauthTransferMapper;
	
	@Autowired
	CommonService commonService;
		
	private static String FILE_URL = XauthPropUtils.getKey("upload.files.url");
	
	private static String SUBJECT = XauthPropUtils.getKey("email.subject");
	
	private static String ADMIN_EMAIL = XauthPropUtils.getKey("admin.email");
	
	private static String DOWMLOAD_PDF_URL = XauthPropUtils.getKey("download.files.pdfurl");
	
	private static String RESULT_JSON_URL  = XauthPropUtils.getKey("resultJson.file.url");

//	private static int FILE_MAX_SIZE = 1024*1024*10;

	/**
	 * 合約查詢
	 * 
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public GridResult selectContractList(Map<String, Object> params) throws Exception {
		GridResult gridResult = null;
		UserInfo userInfo = userContext.getCurrentUser();
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		StringBuffer esSql = null;
		String beginColumnName = "data.docdetail.resultdata";
		String module = MapUtils.getString(params, "module");
		String suppliercode = MapUtils.getString(params, "suppliercode"); // 供應商廠編
		String suppliername = MapUtils.getString(params, "suppliername");
		String suppliergui = MapUtils.getString(params, "suppliergui");
		String deptno = MapUtils.getString(params, "deptno");
		String contractBgnDate = MapUtils.getString(params, "contractBgnDate");
		String contractEndDate = MapUtils.getString(params, "contractEndDate");
		String indexName = "";
		String todo = "";
		String contractNo = ""; 
		String flowid = ""; 
		String users = "";
		String roles = "";
		String deptId = "";
		String flowStatus = "";
		String applicantUserid = "";
		List<XauthSysCode> deptCodeList = getSysCode("DEPT_CODE"); // 課別資料
		Map<String, Object> resultMap = null;
		Map<String, Object> dataidMap = new HashMap<String, Object>(); // 組dataid的map (key : indexname, value : dataid)
		Map<String, Object> agentUserIdMap = new HashMap<String, Object>();
		boolean isContractUser;
		boolean isSendContractCancel = true; // 判斷作廢的合約是否有送出
		
		Map<String, Object> compParams = new HashMap<String, Object>();
		compParams.put("contractmodel", module);
		compParams.put("contractBgnDate", contractBgnDate);
		compParams.put("contractEndDate", contractEndDate);
		List<Map<String, Object>> contractList = contractMapper.selectContracts(compParams);
		List<String> roleUserList = null;
		String	agentUserId = "";
		// 將role轉換成user，若非當合約簽審人員，不得看到此合約，排除admin
		for(Map<String, Object> contractDataMap : contractList) {
			isContractUser = false;
			String userids = "";
			users = MapUtils.getString(contractDataMap, "USERIDS");
			roles = MapUtils.getString(contractDataMap, "ROLEIDS");
			deptId = MapUtils.getString(contractDataMap, "NOWDEPTID");
			if(StringUtils.isBlank(users) && StringUtils.isNotBlank(roles) && StringUtils.isNotBlank(deptId)) {
				// 取出roles
				for(String roleId : roles.split(",")) {
					roleUserList = commonService.getUserDatas(roleId, deptId);
					// 重組userids
					userids = commonService.getDataByComma(roleUserList);
					contractDataMap.put("USERIDS", userids);
				}
			}
			//若非當合約簽審人員，不得看到此合約，排除admin
			if(!StringUtils.equals(userContext.getCurrentUser().getUserType(), USER_TYPE.SYS_ADMIN.getCode())) {
				if(StringUtils.isNotBlank(MapUtils.getString(contractDataMap, "USERIDS"))) {
					for(String userId : MapUtils.getString(contractDataMap, "USERIDS").split(",")) {
						//加入代理人
						agentUserId = getAgentUserId(userId);
						if(StringUtils.equals(userId, userInfo.getUserId()) || StringUtils.equals(agentUserId, userInfo.getUserId())) {
							isContractUser = true;
							break;
						}
					}
				}
			} else {
				isContractUser = true;
			}
			if(!isContractUser) {
				continue;
			}
			
			// 組dataid
			String dataid = MapUtils.getString(contractDataMap, "DATAID");
			indexName = MapUtils.getString(contractDataMap, "INDEXNAME");
			// 組index資料和代理人資訊
			dataidMap.put(dataid, indexName);
			agentUserIdMap.put(dataid, agentUserId);
			
		}
		for(String dataidKey : dataidMap.keySet()) {
			indexName = MapUtils.getString(dataidMap, dataidKey);
			agentUserId = MapUtils.getString(agentUserIdMap, dataidKey);
			esSql = new StringBuffer();
			// 組Elasticsearch條件
			esSql.append(" where " + "_id = '" + dataidKey + "'");
			if(StringUtils.isNotBlank(suppliercode)) {
				esSql.append("and " + beginColumnName + "." + LocaleMessage.getMsg("suppliter.field.suppliercode") + "=" + "'" + suppliercode + "'");
			}
			if(StringUtils.isNotBlank(suppliername)) {
				esSql.append("and " + beginColumnName + "." + LocaleMessage.getMsg("suppliter.field.suppliercname") + "=" + "'" + suppliername + "'");
			}
			if(StringUtils.isNotBlank(suppliergui)) {
				esSql.append("and " + beginColumnName + "." + LocaleMessage.getMsg("suppliter.field.suppliergui") + "=" + "'" + suppliergui + "'");
			}
			if(StringUtils.isNotBlank(deptno)) {
				esSql.append("and " + beginColumnName + "." + LocaleMessage.getMsg("contract.field.section") + "=" + "'" + deptno + "'");
			}
			List<String> resultJson = ElasticSearchUtil.searchForES(indexName, "todo," + beginColumnName, esSql.toString());
			if(resultJson != null && resultJson.size() > 0) {
				for(String json : resultJson) {
//					 將查詢後的json放入grid中
					ReadContext readContext = JsonPath.parse(json);
					todo = readContext.read("$.todo");
					List<Map<String, Object>> resultdataList = readContext.read("$..resultdata");
					if(resultdataList.size() > 0) {
						resultMap = resultdataList.get(0);
						contractNo = MapUtils.getString(resultMap, LocaleMessage.getMsg("contract.field.no"));
						resultMap.put("todo", todo);
						resultMap.put("isNowUser", "N");
						resultMap.put("indexName", indexName);
						resultMap.put("agentUserId", agentUserId);
						
						//查詢承辦人ID 並加入代理人ID
						compParams.put("dataid", contractNo);
						List<Map<String, Object>> docStateList = contractMapper.selectDocstate(compParams);
						if(docStateList.size()>0) {
							String userid =	MapUtils.getString(docStateList.get(0), "NOWAPPLICANTID");
							resultMap.put("被代理申請人",getAgentUserId(userid));
						}

						// 取得課別資料中文名稱
						for(XauthSysCode deptCode : deptCodeList) {
							if(StringUtils.equals(deptCode.getCode(), MapUtils.getString(resultMap, LocaleMessage.getMsg("contract.field.section")))) {
								resultMap.put("deptCName", deptCode.getCname());
								resultMap.put("deptNo", deptCode.getCode());
							}
						}
						
						// 從資料庫取得取得flow 狀態
						for(Map<String, Object> contractData : contractList) {
							if(StringUtils.equals(contractNo, MapUtils.getString(contractData, "DATAID"))) {
								isSendContractCancel = true;
								flowid = MapUtils.getString(contractData, "FLOWID");
								flowStatus = MapUtils.getString(contractData, "FLOWSTATUS");
								if(StringUtils.equals(flowStatus, "PROCESS")) {
									if(StringUtils.equals(todo, "駁回")) {
										if(MapUtils.getString(contractData, "TASKNAME").equals("申請人")) {
											flowStatus = "編輯中";
										}else {
											flowStatus = "退回重審";
										}
									}else {
										flowStatus = "審核中";
									}
								}else if(StringUtils.equals(flowStatus, "END")) {
									flowStatus = "歸檔";
								}else if(StringUtils.equals(flowStatus, "CANCEL")) {
									flowStatus = "作廢";
								}else {
									if(StringUtils.equals(todo, "作廢")) {
										flowStatus = "作廢";
										isSendContractCancel = false;
									}
								}
								resultMap.put(LocaleMessage.getMsg("contract.field.docstatus"), flowStatus);
								resultMap.put("nowTaskCName", MapUtils.getString(contractData, "TASKNAME"));
								resultMap.put("flowId", flowid);
							
								//判斷使用者是否為合約當前簽審人員
								String taskuserids = MapUtils.getString(contractData, "TASKUSERIDS");
								String taskroleids = MapUtils.getString(contractData, "TASKROLEIDS");
								deptId = MapUtils.getString(contractData, "DEPTID");
								if(StringUtils.isBlank(taskuserids) && StringUtils.isNotBlank(taskroleids) && StringUtils.isNotBlank(deptId)) {
									for(String taskrole : taskroleids.split(",")) {
										roleUserList = commonService.getUserDatas(taskrole, deptId);
										for(int i = 0 ; i < roleUserList.size() ; i ++) {
											//移轉USERID置換(原持有人)
											String userId = userInfo.getUserId();
											userId = xauthTransferService.getUserIdByRole(userId,taskrole,contractNo);
											//判斷是否為代理人
											if(StringUtils.equals(roleUserList.get(i), userId)
												||StringUtils.equals(getAgentUserId(roleUserList.get(i)),userId)){
												resultMap.put("isNowUser", "Y");
												break;
											}
										}
									}
								} else if(StringUtils.isNotBlank(taskuserids)) {
									for(String taskuser : taskuserids.split(",")) {
										//移轉USERID置換(原持有人)
										String isuserid = userInfo.getUserId();
										isuserid = xauthTransferService.getUserIdByUserid(isuserid,taskuser,contractNo);
										//判斷是否為代理人
										if(StringUtils.equals(taskuser, isuserid) || getAgentUserId(taskuser).equals(isuserid)){
											resultMap.put("isNowUser", "Y");
											break;
										}
									}
								}
								// 承辦人(包含移轉後)
								if(MapUtils.getInteger(contractData, "DISPORD") == 1) {
									applicantUserid = MapUtils.getString(contractData, "USERIDS");
								}
								// 轉換送審日期格式
								SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
								DateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
								String sendDate = MapUtils.getString(contractData, "SENDDATE");
								resultMap.put(LocaleMessage.getMsg("contract.field.send.date"), sdf.format(sdfDate.parse(sendDate)));
							}
						}
						
						// 甲方非申請人:申請人未送出之合約無法查詢(排除TODO: 暫存、新建、作廢)，admin皆能看到
						if(StringUtils.equals(userContext.getCurrentUser().getUserType(), USER_TYPE.SYS_ADMIN.getCode())|| StringUtils.equals(userInfo.getUserId(), applicantUserid)) {
							resultList.add(resultMap);
						} else if(StringUtils.equals(userInfo.getUserId(), agentUserId) && isSendContractCancel) { // 代理人若為作廢可查到此筆合約
							if(!StringUtils.equals(MapUtils.getString(resultMap, "todo"), "暫存") 
									&& !StringUtils.equals(MapUtils.getString(resultMap, "todo"), "新建")) {
								resultList.add(resultMap);
							}
						} else {
							if(!StringUtils.equals(MapUtils.getString(resultMap, "todo"), "暫存") 
									&& !StringUtils.equals(MapUtils.getString(resultMap, "todo"), "新建") 
									&& !StringUtils.equals(MapUtils.getString(resultMap, "todo"), "作廢")) {
								resultList.add(resultMap);
							}
						}
					}
				}
			}
		}
		// 排序及分頁
		gridResult = this.grid(params);
		ElasticSearchUtil.sortPagination(gridResult, resultList, params);
		
		return gridResult;
	}
	/**
	 * 待辦清單Grid
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public GridResult selectToDoList(Map<String, Object> params) throws Exception {
		
		String beginColumnName = "data.docdetail.resultdata";
		List<DocStateBean> docStateBeanList = null;
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		List<String> agentusers = null;
		Set<String>useridlist = new HashSet<String>();

		Map<String, Object> resultMap = null;
		Map<String, Object> dataMap = new HashMap<String, Object>();
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		StringBuffer esSql = new StringBuffer();
		//取使用者資料
		UserInfo userInfo = userContext.getCurrentUser();
		GridResult gridResult = this.grid(params);

		//原始單據id加入
		useridlist.add(userInfo.getUserId());
		//原始單據代理人id加入
		agentusers = getPrincipalUserId(userInfo.getUserId());
		if(agentusers.size() > 0) {
			for(String userid : agentusers) {
				useridlist.add(userid);		
			}
		}
		//移轉清單id加入(含代理人)
		Map<String, Object> compParams = new HashMap<String, Object>();
		compParams.put("userid", userInfo.getUserId());
		List<Map<String, Object>> searchlist = xauthTransferMapper.searchtransferUserId(compParams);
		if(searchlist.size()>0) {
			for(Map<String, Object> map : searchlist) {
				String searchid = MapUtils.getString(map, "USERID");
				useridlist.add(searchid);
			}
		}
		//查詢userid並合併list
		for(String userid : useridlist) {
			docStateBeanList = flowQueryService.fetchToDoListBy(userid, APP_ID, true);
			if (docStateBeanList != null && docStateBeanList.size() > 0) {
				List<Map<String, Object>> templist = null;
				templist = ConvertUtils.Object2MapList(docStateBeanList);	
				if(list != null && list.size() > 0) {
					list = Stream.of(list, templist).flatMap(Collection::stream).distinct().collect(Collectors.toList());
				}
				else {
					list = templist;
				}
			}
		}

		for(Map<String, Object> data : list) {
			String applyNo = MapUtils.getString(data, "applyNo");
			String flowStatus = MapUtils.getString(data, "flowStatus");
			String taskName = MapUtils.getString(data, "taskName");

			//判斷是否為合約持有人
			if(!xauthTransferService.isNowContractTaskUser(applyNo,userInfo.getUserId())) {
				continue;
			}

			Map<String, Object> contractmaster = new HashMap<String, Object>();
			contractmaster.put("dataid", applyNo);
			//取得selectContractMaster資訊 NOWUSERID 為合約建立人 為判斷是否為承辦人或承辦代理人
			List<Map<String, Object>> contractmasterListSql = contractMapper.selectContractMasterByTask1(contractmaster);
			if(contractmasterListSql.size() > 0) {
				String indexname = MapUtils.getString(contractmasterListSql.get(0), "INDEXNAME") ;
				String model = MapUtils.getString(contractmasterListSql.get(0), "CONTRACTMODEL") ;
				String nowuserid = MapUtils.getString(contractmasterListSql.get(0), "NOWUSERID") ;
				esSql = new StringBuffer();
				XauthSysCode xauthSysCode = commonService.getSysCodeData("CONTRACT_MODE_CODE", model);
				dataMap.put("contractmodel", xauthSysCode.getCname());
				
				esSql.append(" where " + beginColumnName + "." + LocaleMessage.getMsg("contract.field.no") + "='" + applyNo+"'");
				List<String> resultJson = ElasticSearchUtil.searchForES(indexname, "todo," + beginColumnName, esSql.toString());
				if(resultJson != null && resultJson.size() > 0) {
					for(String json : resultJson) {
						ReadContext readContext = JsonPath.parse(json);
						String todo = readContext.read("$.todo");
						List<Map<String, Object>> resultdataList = readContext.read("$..resultdata");
						if(resultdataList.size() > 0) {
							resultMap = resultdataList.get(0);
							String startDay = MapUtils.getString(resultMap, LocaleMessage.getMsg("contract.field.updtime"));
							String deptNo = MapUtils.getString(resultMap, LocaleMessage.getMsg("contract.field.section")) == null ? "" : MapUtils.getString(resultMap, LocaleMessage.getMsg("contract.field.section"));
							String contractor = MapUtils.getString(resultMap, LocaleMessage.getMsg("contract.field.contractor"));
							String undertakeuser = MapUtils.getString(resultMap, LocaleMessage.getMsg("contract.field.undertakeuserid"));
							if(StringUtils.isNotBlank(undertakeuser)) {
								contractor = undertakeuser;
							}
							String suppliergui = MapUtils.getString(resultMap, LocaleMessage.getMsg("suppliter.field.suppliergui"));
							String supplierCname= MapUtils.getString(resultMap, LocaleMessage.getMsg("suppliter.field.suppliercname"));
							String checkStatic = MapUtils.getString(resultMap, LocaleMessage.getMsg("contract.field.check_static"));
							String checkResult = MapUtils.getString(resultMap, LocaleMessage.getMsg("contract.field.check_result"));
							dataMap.put("contracttype", MapUtils.getString(resultMap, LocaleMessage.getMsg("contract.field.name")));
							dataMap.put("startDay", sdf.format(sdf.parse(startDay)));
							dataMap.put("deptNo", deptNo);
							dataMap.put("contractor", contractor);
							dataMap.put("suppliergui", suppliergui);
							dataMap.put("supplierCname", supplierCname);
							dataMap.put("checkStatic", checkStatic);
							dataMap.put("checkResult", checkResult);
//							dataMap.put("applyNo", applyNo);
							dataMap.put("indexname", indexname);
							
							dataMap.put("supplierCname", supplierCname);
							dataMap.put("checkStatic", checkStatic);
							dataMap.put("checkResult", checkResult);
							//代理人處理 前段改判斷方式 只需判斷flowStatus 
							dataMap.put("currentUserid", userContext.getCurrentUser().getUserId());
							dataMap.put("contractorAgentUserId", contractService.getAgentUserId(nowuserid));
							
							if(StringUtils.equals(flowStatus, "PROCESS")) {
								if(StringUtils.equals(todo, "駁回")) {
									if(taskName.equals("申請人")) {
										flowStatus = "編輯中";
									}else {
										flowStatus = "退回重審";
									}
								}else {
									flowStatus = "審核中";
								}
							}else if(StringUtils.equals(flowStatus, "END")) {
								flowStatus = "歸檔";
							}else if(StringUtils.equals(flowStatus, "CANCEL")) {
								flowStatus = "作廢";
							}else {
								if(StringUtils.equals(todo, "作廢")) {
									flowStatus = "作廢";
								}
							}
							//用於判斷前端狀態
							dataMap.put("flowStatus", flowStatus);
							resultMap = new HashMap<String, Object>();
							resultMap.putAll(data);
							resultMap.putAll(dataMap);
							result.add(resultMap);
						}
					}
				}
			}
		}

		// 排序及分頁
		ElasticSearchUtil.sortPagination(gridResult, result, params);

		return gridResult;
	}
	
	/**
	 * 編輯頁面-下拉式選單
	 * @param contractmodel
	 * @return
	 */
	public List<Reviewconf> getFlowList(String contractmodel){
		List<Reviewconf> result = null;
		QueryWrapper<Reviewconf> queryWrapper = new QueryWrapper<Reviewconf>();
		queryWrapper.eq("CONTRACTMODEL", contractmodel);
		queryWrapper.eq("STATUS", "N"); 
		result = reviewconfMapper.selectList(queryWrapper);
	
		return result;
	}
	
	/**
	 * 編輯頁面-下拉式選單
	 * @param section
	 * @return
	 * @throws Exception 
	 */
	public List<Map<String, Object>> getSuppliermaster(String section, String module) throws Exception{
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		Map<String, Object> resultMap = null;
		List<Suppliermaster> suppliermasterList = commonService.getSupplierDatasByDeptno(module, section);
		for(Suppliermaster suppliermaster : suppliermasterList) {
			resultMap = new HashMap<String, Object>();
			resultMap.put("suppliercode", suppliermaster.getSuppliercode());
			resultMap.put("suppliergui", suppliermaster.getSuppliergui());
			resultMap.put("suppliercname", suppliermaster.getSuppliercname());
			resultMap.put("supplierid", suppliermaster.getSupplierid());
			result.add(resultMap);
		}
		return result;
	}
	
	/**
	 * 組從合約Grid到合約編輯所需的資料準備
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getFlowValue(Map<String, Object> params) throws Exception{
//		List<Systemparam> systemparamList = new ArrayList<Systemparam>();
		Map<String, Object> result = new HashMap<String, Object>();
		String jsonData = MapUtils.getString(params, "keyData");
		if (StringUtils.isNotBlank(jsonData)) {
			Map<String, Object> keyData = new Gson().fromJson(jsonData, HashMap.class);
			List<DocStateBean> list = new ArrayList<DocStateBean>();
			UserInfo userInfo = userContext.getCurrentUser();
			String nowUserId = userInfo.getUserId();
			String indexName = MapUtils.getString(keyData, "indexName").toLowerCase();
			String modal = MapUtils.getString(params, "module");
			String btnType = MapUtils.getString(params, "btnType");
			String applyNo = MapUtils.getString(keyData, "合約編碼");
			String flowId = MapUtils.getString(keyData, "flowId");
			String taskName = "";
			String takeUserId = "";
			
			//合約承辦人ID
			String contractorAgentUserId="";
			Map<String, Object> compParams = new HashMap<String, Object>();
			compParams.put("dataid", applyNo);
			List<Map<String, Object>> docStateList = contractMapper.selectDocstate(compParams);
			if(docStateList.size()>0) {
				contractorAgentUserId=getAgentUserId(MapUtils.getString(docStateList.get(0), "NOWAPPLICANTID"));
				taskName = MapUtils.getString(docStateList.get(0), "TASKNAME");
				takeUserId = MapUtils.getString(docStateList.get(0), "TASKUSERIDS");
			}
			int needSignature = 0;
			
			result.put("currentUserCname", userContext.getCurrentUser().getUserCname());
			result.put("currentUserid", userContext.getCurrentUser().getUserId());
			result.put("idenId", userContext.getCurrentUser().getIdenId());
			result.put("indexName", indexName);
			result.put("btnType", btnType);
			result.put("module", modal);
			result.put("dataId", applyNo);
			
			String datype=LocaleMessage.getMsg("contractEn.filed.Basic_Information");
			if(modal.equals("SC")) {
				datype="基本資料";
			}
			result.put("dataType", datype);
			//admin 法務簽章 處理
			if(StringUtils.equals(userContext.getCurrentUser().getUserType(), USER_TYPE.SYS_ADMIN.getCode())) {
				if(StringUtils.isNotBlank(takeUserId)) {
					nowUserId = takeUserId;
				}
			}
			//1.查詢合約轉移人
			Map<String , String> transferuserids =  xauthTransferService.getTranferOriUserids(nowUserId ,applyNo);
			for(String userid : transferuserids.keySet()) {
				list = flowQueryService.fetchToDoListBy(userid, flowId, APP_ID, true, false);
				if(list.size() > 0) {
					break;
				}
			}
			//2.查找原始單據
			if(list.size() == 0) {
				list = flowQueryService.fetchToDoListBy(nowUserId, flowId, APP_ID, true, false);
			}
			//3.改查原始單據代理人
			if(list.size() == 0){
				List<String> agentlist = getPrincipalUserId(nowUserId);
				if(agentlist.size() > 0) {
					for(String userid : agentlist) {
						list = flowQueryService.fetchToDoListBy(userid, flowId, APP_ID, true, false);
						if(list.size() > 0) {
							break ;
						}
					}
				}
				
			}
			DocStateBean docState = list.size() > 0 ? list.get(0) : null ;
					
			//供應商
			if(StringUtils.equals("999999999", userInfo.getIdenId())) {
				needSignature = 2;
			}
			//末關卡 法務CS
			if(docState != null) {
				if(StringUtils.equals("SC", modal) && StringUtils.equals(docState.getTaskDesc(), "法務審核")) {
					needSignature = 1;
				}
				//法務NSC
				if(StringUtils.equals("NSC", modal) && StringUtils.equals(docState.getTaskDesc(), "法務簽章")) {
					needSignature = 1;
				}
			}
			result.put("contractorAgentUserId", contractorAgentUserId);
			result.put("signature", needSignature);
			result.put("taskName", taskName);

		}
		return result;
	}
	
	/**
	 * 部門階層
	 * 
	 * @return
	 */
	public List<String> getDept(){
		UserInfo userInfo = userContext.getCurrentUser();
		
		return getAllDept(null, userInfo.getIdenId(), userInfo.getIdenId());
	}
	
	/**
	 * 取SysCode資料
	 * @param gp
	 * @return
	 * @throws Exception
	 */
	public List<XauthSysCode> getSysCode(String gp) throws Exception{
		return commonService.getSysCodeDatasByGp(gp);
	}

	/**
	 * 合約編輯下拉式選單
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> getSysCode(Map<String, Object> params) throws Exception{
		Gson gson = new Gson();
		List<Map<String, Object>> jsonList = new ArrayList<Map<String, Object>>();
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> jsonData = new HashMap<String, Object>();
		
		String gp = MapUtils.getString(params, "gp");
		String model = MapUtils.getString(params, "model");
		
//		QueryWrapper<XauthSysCode> queryWrapper = new QueryWrapper<XauthSysCode>();
//		queryWrapper.eq("APP_ID", "APPKIS");
//		queryWrapper.eq("GP", gp);
//		List<XauthSysCode> xauthSysCodeList = xauthSysCodeMapper.selectList(queryWrapper);
		
		List<XauthSysCode> xauthSysCodeList = getSysCode(gp);
		logger.info("xauthSysCodeList hk4gf4==="+xauthSysCodeList);
		for(XauthSysCode xauthSysCode : xauthSysCodeList) {
			Map<String, Object> json = new HashMap<String, Object>();
			//20200921-制式合約過濾 合約主檔
			if(StringUtils.equals(model, "SC")) {
				if(!StringUtils.equals(xauthSysCode.getCname(), "合約主檔")) {
					json.put("#FILE_TYPESELECT", "");
					json.put("FLIE_TYPE", xauthSysCode.getCname());
					json.put("code", xauthSysCode.getCode());
					json.put("memo", xauthSysCode.getMemo());
					jsonList.add(json);
				}
			}else {
				json.put("#FILE_TYPESELECT", "");
				json.put("FLIE_TYPE", xauthSysCode.getCname());
				json.put("code", xauthSysCode.getCode());
				json.put("memo", xauthSysCode.getMemo());
				jsonList.add(json);
			}
		}
		
		String json = gson.toJson(jsonList);
		
		jsonData.put("data", json);
		jsonData.put("token", "8d9693a4-af5d-4f13-b640-30a8f861a6a1");
		result.put("rtnCode", 0);
		result.put("message", "Success");
		result.put("jsonData", jsonData);
		
		return result;
	}

	/**
	 * 新增畫面 新增按鈕呼叫方法
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public ProcessResult insertContractData(Map<String, Object> params) throws Exception {
		Reviewconf resultReviewconf = null;
		ProcessResult result = new ProcessResult();
		String templateYear = "";
		String year = MapUtils.getString(params, "year");
		String oldFlowid = MapUtils.getString(params, "disp");
		String flowname = MapUtils.getString(params, "dispName");
		String module = MapUtils.getString(params, "module");
		String contractNo = MapUtils.getString(params, "contractNo");
		String section = MapUtils.getString(params, "section");
		String supplierid = MapUtils.getString(params, "supplierid");
		String templateType = MapUtils.getString(params, "templateType");
		String perTemplateDataid = MapUtils.getString(params, "perTemplateDataid");
		String isUseLastCon = MapUtils.getString(params, "isUseLastCon");
		String contractBgnDate = MapUtils.getString(params, "contractBgnDate");
		String flowversion = "";
		Suppliermaster suppliermaster = null;
		String resultJson = null;
		String json = null;
		String version = null;
		String newFlowid = null;
		Designermgr designermgr = null;
		UserInfo userInfo = userContext.getCurrentUser();

		if (params != null && params.size() > 0) {
			QueryWrapper<Suppliermaster> supplierQueryWrapper = new QueryWrapper<Suppliermaster>();
			supplierQueryWrapper.eq("SUPPLIERID", supplierid);
			if(StringUtils.isNotBlank(section)) {
				supplierQueryWrapper.eq("DEPTNO", section);
			} else {
				supplierQueryWrapper.isNull("DEPTNO");
			}
			List<Suppliermaster> suppliermasterList = suppliermasterMapper.selectList(supplierQueryWrapper);
			if(suppliermasterList.size() == 0) {
				result.setStatus(ProcessResult.NG);
				result.addMessage("查無供應商資料");
				return result;
			}
			suppliermaster = suppliermasterList.get(0);
			ProcessResult checkRs = checkContractInsertData(params, suppliermaster);
			if (checkRs.getStatus() == ProcessResult.NG) {
				result.setStatus(checkRs.getStatus());
				result.setMessages(checkRs.getMessages());
				return result;
			}
			if(StringUtils.isBlank(oldFlowid) || StringUtils.isBlank(flowname)) {
				result.setStatus(ProcessResult.NG);
				result.addMessage("合約範本不得為空");
				return result;
			}
			QueryWrapper<Reviewconf> queryWrapper = new QueryWrapper<Reviewconf>();
			queryWrapper.eq("FLOWID", oldFlowid);
			queryWrapper.eq("FLOWNAME", flowname);
			queryWrapper.eq("CONTRACTMODEL", module);
			List<Reviewconf> reviewconfList = reviewconfMapper.selectList(queryWrapper);
			int size = reviewconfList.size();
			if(size > 0) {
				int resultVersion = 1;
				for(Reviewconf reviewconf : reviewconfList) {
					int fversion = Integer.valueOf(reviewconf.getFlowversion());
					if(fversion >= resultVersion) {
						resultVersion = fversion;
						resultReviewconf = reviewconf;
					}
				}
			}else {
				result.setStatus(ProcessResult.NG);
				result.addMessage("查無合約範本資料");
				return result;
			}
			flowversion = resultReviewconf.getFlowversion();
			
			Percontracttemplate perConTempData = null;
			String perConTempDocver = "";
			if(StringUtils.equals("PT", templateType)) {
				perConTempData = getPerConTemplateData(perTemplateDataid);
				json = perConTempData.getJson();
				perConTempDocver = perConTempData.getDocver();
			}
			
			List<Designermgr> designermgrList = getDesignermgr(flowname, module, templateType, perConTempDocver);
			if(designermgrList != null && designermgrList.size() > 0) {
				designermgr = designermgrList.get(0);
				if(!StringUtils.equals("PT", templateType)) {
					// 若選法務範本及選擇使用前次合約
					if(StringUtils.equals("LT", templateType) && StringUtils.equals("Y", isUseLastCon)) {
						json = getBeforeConJson(year, module, flowname, section, supplierid);
					} else {
						json = designermgr.getJson();
					}
				}
				version = designermgr.getVersion();
				templateYear = designermgr.getYear();
				if(StringUtils.isBlank(json)) {
					logger.info("查無json欄位");
					result.setStatus(ProcessResult.NG);
					result.addMessage("新增資料發生錯誤");
					return result;
				}
				boolean isLogr = false;
				if(StringUtils.equals("SC", module)) {
					isLogr = true;
				}
				
				//合約編號
				if(StringUtils.isBlank(contractNo)) {
					contractNo = getContractNo(isLogr, year, section, suppliermaster.getSuppliercode());
					params.put("contractNo", contractNo);
				}
				newFlowid = MapUtils.getString(params, "disp") + MapUtils.getString(params, "contractNo");
				
				//流程編號
				if(StringUtils.isNotBlank(newFlowid)) {
					params.put("flowid", newFlowid);
				}
				if(StringUtils.isNotBlank(flowversion)) {
					params.put("flowversion", flowversion);
				}
				if(StringUtils.isNotBlank(version)) {
					params.put("version", version);
				}
				resultJson = insertToJson(json, params, suppliermaster, templateYear, templateType, isUseLastCon);
				resultJson = JsonUtil.jsonSetValueByKey(resultJson, "todo", "新建");
				// 2020/11/25 供應商資料改成塞resultdata
//				resultJson = setSupplierMasterToJson(resultJson, supplierid, section);
				// 10.3合約起日寫入json
				resultJson = JsonUtil.setdcFeesConDateValue(resultJson, contractBgnDate);
				resultJson = setDefaultValue(resultJson);
				
				// 將審核評估資料塞入json
//				resultJson = setNegoentryVal(resultJson, section, suppliermaster.getSuppliercode());
//				logger.info("resultJson == " + resultJson);
				
				params.put("applicantid", userInfo.getUserId());
				//制式 Flow
				if(StringUtils.equals("SC", module)) {
					result = flowSetService.setSCFlowStepAndLink(params, resultReviewconf, suppliermaster);
				}
				//非制式 Flow
				if(StringUtils.equals("NSC", module)) {
					result = flowSetService.setNSCFlowStepAndLink(params, resultReviewconf, suppliermaster);
				}
				//Flow 流程錯誤訊息
				if(StringUtils.equals(ProcessResult.NG, result.getStatus())) {
					return result;
				}
				// 寫入ElasticSearch
				String indexName = getIndexName(MapUtils.getString(params, "year"), designermgr.getSpec());
				ProcessResult insertEsResult = insertContractForES(params, resultJson,
						designermgr.getSpec(), MapUtils.getString(params, "contractNo"), indexName, designermgr.getJson());
				if (insertEsResult.getStatus().equals(ProcessResult.NG)) {
					result.setStatus(insertEsResult.getStatus());
					result.setMessages(insertEsResult.getMessages());
					return result;
				}
				
				// 寫入DB CONTRACTMASTER
				insertContractmaster(params, resultJson, indexName);
				
				//寫入備份區
				createJsonFile(resultJson, contractNo, "POST");
				
				result = new ProcessResult();
				result.addResult("dataId", contractNo);
				//非制式客製化-EN
				if(module.equals("NSC")) {
					result.addResult("dataType", LocaleMessage.getMsg("contractEn.filed.Basic_Information"));
				}
				else {
					result.addResult("dataType", "基本資料");
				}
				result.addResult("indexName", indexName);
				result.addResult("btntype", 2);
			} else {
				result.setStatus(ProcessResult.NG);
				result.addMessage("查無合約範本資料");
				return result;
			}
			result.setStatus(ProcessResult.OK);
			result.addMessage(MSG_KEY.INSERT_OK.getMessage());
		} else {
			result.setStatus(ProcessResult.NG);
			result.addMessage(MSG_KEY.PARAMS_EMPTY.getMessage());
		}

		return result;
	}

	/**
	 * 取得Designermgr資料
	 * @param flowname
	 * @param contractmodel
	 * @param templateType
	 * @param perConTemplateDocver
	 * @return
	 */
	private List<Designermgr> getDesignermgr(String flowname, String contractmodel, String templateType, String perConTemplateDocver) {
		List<Designermgr> result = null;
		
		QueryWrapper<Designermgr> designermgrWrapper = new QueryWrapper<Designermgr>();
		if(StringUtils.equals("PT", templateType)) {
			designermgrWrapper.eq("DOCVER", perConTemplateDocver);
			result = designermgrMapper.selectList(designermgrWrapper);
		} else {
			designermgrWrapper.eq("MODULE", contractmodel);
			if(StringUtils.equals("SC", contractmodel)) {
				designermgrWrapper.eq("DISP", flowname);
			}
			designermgrWrapper.isNull("DROPTIME");
			designermgrWrapper.isNotNull("PRDTIME");
			designermgrWrapper.orderByDesc("PRDTIME");
			result = designermgrMapper.selectList(designermgrWrapper);
		}
		
		return result;
	}
	
	private Percontracttemplate getPerConTemplateData(String perTemplateDataid) {
		Percontracttemplate perConTemplate = percontracttemplateMapper.selectById(perTemplateDataid);
		
		return perConTemplate;
	}
	
	/**
	 * 新增 -> 編輯 合約頁面帶出 ES Json 給端元件顯示出 編輯 Html 畫面 
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("static-access")
	public Map<String, Object> getJsonByEs(Map<String, Object> params) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		String indexName = MapUtils.getString(params, "indexName");
		String dataId = MapUtils.getString(params, "dataid");
		
		if(StringUtils.isBlank(indexName) || StringUtils.isBlank(dataId)) {
			jsonMap.put("data", "");
			resultMap.put("rtnCode", -1);
			resultMap.put("jsonData", "");
			resultMap.put("message", "查詢合約發生錯誤");
			return resultMap;
		}
		
		String esJson = elasticSearchUtil.serachById(dataId, indexName);
		esJson = JsonUtil.jsonSkipToString(esJson);
		
		// 關閉連線
		elasticSearchUtil.closeESClient();

		JsonObject jsonObj = new JsonParser().parse(esJson).getAsJsonObject();
		String todo = jsonObj.get("todo").getAsString();
		if(StringUtils.equals("作廢", todo)) {
			jsonMap.put("data", "");
			resultMap.put("rtnCode", -1);
			resultMap.put("jsonData", "");
			resultMap.put("message", "此合約已作廢");
		}else {
			jsonMap.put("data", esJson);
			resultMap.put("rtnCode", 0);
			resultMap.put("jsonData", jsonMap);
			resultMap.put("message", "建立合約成功");
		}
		
		return resultMap;
	}
	
	/**
	 * 編輯畫面 送出,駁回,審核 按鈕呼叫方法
	 * @param params
	 * @param type
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> flowDeal(Map<String, Object> params, String type, String contractNo, boolean isSupplier) throws Exception{
		Map<String, Object> result = new HashMap<String, Object>();
		ProcessResult processResult = null;
		StringBuffer errorMsg = null;
		UserInfo userInfo = userContext.getCurrentUser();
		String nowUserId = userInfo.getUserId();
		String nowTaskId = "";
		String nextTask = "";
		String indexName = "";
		String flowId = "";
		String dataId = "";
		String modelName = "";
		String deptNo = "";
		String dispName = "";
		String supplierId = "";
		String contractYear = "";
		String idenId = "";
		String jsonData = new Gson().toJson(params.get("data"), Map.class);
		ReadContext json = JsonPath.parse(jsonData);
		String todo = json.read("$.todo");
		String supplieremail = "";
		String applicantid = "";
		String contractname="";
		String nowContractUserId = "";
		String agentuser = "";
		String nowContractRoleId = "";
		String nowContractDeptId = "";
//		String contractor = "";
		String undertakeuser = "";
		String flowuserid = "";
		String undertakeidenId = "";
		String isAgentUserId = "N";

		Map<String, Object> rebuildFlowMap = new HashMap<String, Object>(); 
			
		StringBuffer toComeNames = new StringBuffer();
		toComeNames.append("至");
		//非制式客製化-EN 
		List<Map<String, Object>> basicDataList = json.read("$.data[?(@.datatype == '"+LocaleMessage.getMsg("contractEn.filed.Basic_Information")+"')].docdetail[?(@.resultdata)].resultdata");
		if(basicDataList.size()  <= 0) {
			//採原計畫書
			basicDataList = json.read("$.data[?(@.datatype == '基本資料')].docdetail[?(@.resultdata)].resultdata");
			// 2020/11/25 供應商資料來源改為resultdata
//			supplieremail = json.read("$..docdetail[?(@.displayname=='聯絡人電子郵件：')].value").toString();
		}else {
//			supplieremail = json.read("$..docdetail[?(@.displayname=='Contact Person Email聯絡人電子郵件：')].value").toString();
		}
		//取的resultdata資訊
		if(basicDataList.size() > 0) {
			modelName = (String) basicDataList.get(0).get("合約模式");
			deptNo = (String) basicDataList.get(0).get(LocaleMessage.getMsg("contract.field.section")); 
			dispName = (String) basicDataList.get(0).get(LocaleMessage.getMsg("contract.field.type")); 
			supplierId = (String) basicDataList.get(0).get(LocaleMessage.getMsg("suppliter.field.supplierid"));
			contractYear = (String) basicDataList.get(0).get(LocaleMessage.getMsg("contract.field.year"));
			idenId = (String) basicDataList.get(0).get(LocaleMessage.getMsg("xauth.field.iden"));
			supplieremail = (String) basicDataList.get(0).get(LocaleMessage.getMsg("suppliter.field.supplieremail"));
			contractname=(String) basicDataList.get(0).get(LocaleMessage.getMsg("contract.field.name"));
			undertakeuser = (String) basicDataList.get(0).get(LocaleMessage.getMsg("contract.field.undertakeuserid"));
//			contractor = (String) basicDataList.get(0).get(LocaleMessage.getMsg("contract.field.contractor"));
			undertakeidenId = (String) basicDataList.get(0).get(LocaleMessage.getMsg("contract.field.undertakedept"));
		}
		 
		Map<String, Object> compParams = new HashMap<String, Object>();
		compParams.put("dataid", contractNo);
		List<Map<String, Object>> contractmasterList = contractMapper.selectContractMaster(compParams);
		if(contractmasterList.size() == 0) {
			result.put("rtnCode", -1);
			result.put("message", "查無合約資料");
			return result;
		}else {
			dataId = MapUtils.getString(contractmasterList.get(0), "DATAID");
			flowId = MapUtils.getString(contractmasterList.get(0), "FLOWID");
			indexName = MapUtils.getString(contractmasterList.get(0), "INDEXNAME");
			//1222 flow流程改寫
			
			applicantid = MapUtils.getString(contractmasterList.get(0), "ORIUSERID");
			//判斷當前ID是否轉移
			nowContractUserId = MapUtils.getString(contractmasterList.get(0), "NOWUSERIDS");
			agentuser = MapUtils.getString(contractmasterList.get(0), "AGENTUSERID");
			
			if(StringUtils.isNotBlank(nowContractUserId) && ((nowContractUserId.equals(nowUserId) || StringUtils.equals(agentuser, nowUserId)))) {
				//判斷flowstep是否存在角色ID 並置換USERID
				
				 if(MapUtils.getString(contractmasterList.get(0), "ORIUSERID")!=null) {
					nowUserId = MapUtils.getString(contractmasterList.get(0), "ORIUSERID");
				}
				 
				 if(StringUtils.equals(agentuser, nowUserId)) {
						isAgentUserId = "Y";
				 }
			}
			
			//admin判斷 原始單據沒有USERID時狀況	  若非admin則用該登入userid
			else if(StringUtils.equals(userContext.getCurrentUser().getUserType(), USER_TYPE.SYS_ADMIN.getCode())){
				
				if(MapUtils.getString(contractmasterList.get(0), "ORIUSERID") != null) {
					nowUserId = MapUtils.getString(contractmasterList.get(0), "ORIUSERID");
				}
				else {
					nowContractRoleId = MapUtils.getString(contractmasterList.get(0), "ORIROLEIDS");
					nowContractDeptId = MapUtils.getString(contractmasterList.get(0), "ORIDEPTID");
					List<String> userlist = commonService.getUserDatas(nowContractRoleId, nowContractDeptId);
					nowUserId = commonService.getDataByComma(userlist);
				}
			}
		}
		// Act & Assert
		//針對admin查找 可能同部門同角色會有多個userid
		List<DocStateBean> list = new ArrayList<DocStateBean>();
		for(String userid : nowUserId.split(",")) {
			list = flowQueryService.fetchToDoListBy(userid, flowId, APP_ID, true, false);
			if(list.size() > 0 ) {
				nowUserId = userid;
				break;
			}
		}

		//改查代理人 
		if(list.size() < 1 ){
			List<String> agentlist = getPrincipalUserId(nowUserId);
			if(agentlist.size() > 0 ) {
				for(String userid : agentlist) {
					list = flowQueryService.fetchToDoListBy(userid, flowId, APP_ID, true, false);
					if(list.size() > 0) {
						isAgentUserId = "Y";
						nowUserId = userid;
						break;
					}
				}
			}
			
		}
		if((list.size() == 0) && !StringUtils.equals("送出", type)) {
			result.put("rtnCode", -1);
			result.put("message", "查無合約流程資料");
			return result;
		}
		if(StringUtils.equals("送出", type)) {
			
			// 將承辦人員填的供應商資料存入DB
			/**註解 不將合約供應商資訊寫入資料庫 2020 / 11 / 19  
			*/
			//updateSuppDataToDb(jsonData, supplierId);
			if(StringUtils.equals(modelName, "制式合約")) {
				String scFileName = contractNo + "_1.pdf";
				// 制式合約審核評估當年度檢核結果存入DB
				insertNegoentryToDb(jsonData, contractYear);
				contractReportService.createScPdf(FILE_URL + dataId, scFileName, jsonData, setPdfValue(jsonData, flowId, modelName), modelName);
				insertToContractfile(contractNo, FILE_URL + dataId, scFileName, modelName);
			} else {
				QueryWrapper <Contractfile> contractfileQw = new QueryWrapper<Contractfile>();
				boolean isUpload = false; // 是否上傳合約主檔
				contractfileQw.eq("DATAID", contractNo);
				contractfileQw.eq("TYPE", "01");
				contractfileQw.eq("FILETYPE", "合約主檔");
				List<Contractfile> contractfileList = contractfileMapper.selectList(contractfileQw);
				if(contractfileList.size() > 0) {
					Contractfile contractfile = contractfileList.get(0);
					String contractFilePath = contractfile.getFliepath();
					String contractFileName = contractfile.getFliename();
					String resultPdfFileName = StringUtils.split(contractFileName, ".")[0] + "_1.pdf";
					isUpload = true;
					// 組成pdf
					contractReportService.createPdf(setPdfValue(jsonData, flowId, modelName), contractFilePath, resultPdfFileName, contractFileName, modelName);
					// insert to oracle CONTRACTFILE
					insertToContractfile(contractNo, contractFilePath, resultPdfFileName, modelName);
				}
				
				if(!isUpload) {
					result.put("rtnCode", -1);
					result.put("message", "請於附件資料上傳合約主檔");
					return result;
				}
			}
			
			DocStateBean docState = null;
			if(StringUtils.equals(todo, "駁回")) {
				//判斷第一關人員是否與當前人員相同 更改applicantid
				if(StringUtils.equals(nowContractUserId, userInfo.getUserId()) || StringUtils.equals(nowContractUserId, getPrincipalUserId(userInfo.getUserId(),nowContractUserId))) {
					applicantid = nowContractUserId;
					if(StringUtils.isNotBlank(undertakeidenId)) {
						idenId = undertakeidenId;
					}
				}
				//判斷是否為新的承辦人 送出前先刪除DOC
				if(StringUtils.equals(undertakeuser, userInfo.getUserCname()) || StringUtils.equals(nowContractUserId, getPrincipalUserId(userInfo.getUserId(),nowContractUserId))) {
					if(list.size() > 0 ){
						logger.info("刪除流程DOC");
						QueryWrapper <Docstatelog> docstatelogQueryWrapper =new QueryWrapper<Docstatelog>();
						docstatelogQueryWrapper.eq("APPLYNO", dataId);
						docstatelogMapper.delete(docstatelogQueryWrapper);
						QueryWrapper <Docstate> docstateQueryWrapper =new QueryWrapper<Docstate>();
						docstateQueryWrapper.eq("APPLYNO", dataId);
						docstateMapper.delete(docstateQueryWrapper);
					}
				}
				
				docState = list.get(0);
				nowTaskId = docState.getNowTaskId();
				int nowTaskNum = Integer.valueOf(nowTaskId.replace("Task", ""));// Integer.valueOf(nowTaskId.substring(nowTaskId.length() - 1, nowTaskId.length()));
				nextTask = "Task" + (nowTaskNum + 1);
				rebuildFlowMap.put("module", StringUtils.equals(modelName, "制式合約") ? "SC" : "NSC");
				rebuildFlowMap.put("section", deptNo);
				rebuildFlowMap.put("dispName", dispName);
				rebuildFlowMap.put("flowid", flowId);
				rebuildFlowMap.put("idenId", idenId);
				
				rebuildFlowMap.put("applicantid", applicantid);
				QueryWrapper<Reviewconf> reviewconfWrapper = new QueryWrapper<Reviewconf>();
				reviewconfWrapper.eq("FLOWID", flowId.substring(0, (flowId.length() - dataId.length())));
				reviewconfWrapper.eq("FLOWNAME", dispName);
				reviewconfWrapper.eq("CONTRACTMODEL", MapUtils.getString(rebuildFlowMap, "module"));
				reviewconfWrapper.orderByDesc("FLOWVERSION");
				List<Reviewconf> reviewconfList = reviewconfMapper.selectList(reviewconfWrapper);
				Reviewconf resultReviewconf = reviewconfList.get(0);
				
				// 取得Suppliermaster資料
				Suppliermaster suppliermaster = commonService.getSupplierData(supplierId, deptNo);
				flowSetService.deleteFlow(flowId);
				
				if(StringUtils.equals(modelName, "制式合約")) {
					// 重建flow流程
					processResult = flowSetService.setSCFlowStepAndLink(rebuildFlowMap, resultReviewconf, suppliermaster);
				} else {
					// 重建flow流程
					processResult = flowSetService.setNSCFlowStepAndLink(rebuildFlowMap, resultReviewconf, suppliermaster);
				}
				//判斷是否需要重啟FLOW流程
				if(StringUtils.equals(undertakeuser, userInfo.getUserCname()) || StringUtils.equals(nowContractUserId, getPrincipalUserId(userInfo.getUserId(),nowContractUserId))) {
					logger.info("重啟FLOW");
					FlowBean flowBean = new FlowBean();
					flowBean.setSysId(APP_ID);
					flowBean.setFormId(flowId);
					/**1222 flow流程改寫
					  *合約草稿 USERID持有人 當有承接人時以承接人為主*/
					
					flowuserid = applicantid;
					flowBean.setApplyNo(contractNo);
					flowBean.setFlowId(flowId);
					flowBean.setFlowVersion("2");
					flowBean.setUserId(flowuserid);
					
					docState = flowService.startFlow(flowBean, null);
					nowTaskId = docState.getNowTaskId();
					nowTaskNum = Integer.valueOf(nowTaskId.replace("Task", "")); // Integer.valueOf(nowTaskId.substring(nowTaskId.length() - 1, nowTaskId.length()));
					nextTask = "Task" + (nowTaskNum + 1);
					docState = flowService.apply(docState, null);
					if(StringUtils.equals(nowContractUserId, getPrincipalUserId(userInfo.getUserId(),nowContractUserId))) {
						isAgentUserId = "Y";
					}
				
				}
				else {
					//關卡一 無更動承辦人
					if(StringUtils.equals(ProcessResult.NG, processResult.getStatus())) {
						result.put("rtnCode", -1);
						result.put("message", processResult.getMessages());
						return result;
					}
					QueryWrapper<Flowstep> flowStepWrapper = new QueryWrapper<Flowstep>();
					flowStepWrapper.eq("FLOWID", flowId);
					flowStepWrapper.eq("STEPID", nextTask);
					List<Flowstep> flowStepList = flowstepMapper.selectList(flowStepWrapper);
					if(flowStepList.size() > 0) {
						Flowstep flowStep = flowStepList.get(0);
						String nextUserId = StringUtils.isBlank(flowStep.getUserids()) ? flowStep.getRoleids() : flowStep.getUserids();
						docState = flowService.applyTo(nextUserId, docState.nowUser(docState.getNowUserId(), docState.getNowTaskId()), null);
					}
				}
			} else {
				//判斷是否為承接人流程 *草稿階段移轉
				if(StringUtils.equals(nowContractUserId, userInfo.getUserId()) && StringUtils.isNotBlank(undertakeuser)) {
					logger.info("FLOW流程更換");
					if(StringUtils.equals(nowContractUserId, userInfo.getUserId())) {
						applicantid = nowContractUserId;
						idenId = undertakeidenId;
					}
					
					rebuildFlowMap.put("module", StringUtils.equals(modelName, "制式合約") ? "SC" : "NSC");
					rebuildFlowMap.put("section", deptNo);
					rebuildFlowMap.put("dispName", dispName);
					rebuildFlowMap.put("flowid", flowId);
					rebuildFlowMap.put("idenId", idenId);
					rebuildFlowMap.put("applicantid", applicantid);
					
					QueryWrapper<Reviewconf> reviewconfWrapper = new QueryWrapper<Reviewconf>();
					reviewconfWrapper.eq("FLOWID", flowId.substring(0, (flowId.length() - dataId.length())));
					reviewconfWrapper.eq("FLOWNAME", dispName);
					reviewconfWrapper.eq("CONTRACTMODEL", MapUtils.getString(rebuildFlowMap, "module"));
					reviewconfWrapper.orderByDesc("FLOWVERSION");
					List<Reviewconf> reviewconfList = reviewconfMapper.selectList(reviewconfWrapper);
					Reviewconf resultReviewconf = reviewconfList.get(0);
					
					// 取得Suppliermaster資料
					Suppliermaster suppliermaster = commonService.getSupplierData(supplierId, deptNo);
					flowSetService.deleteFlow(flowId);
					if(StringUtils.equals(modelName, "制式合約")) {
						// 重建flow流程
						processResult = flowSetService.setSCFlowStepAndLink(rebuildFlowMap, resultReviewconf, suppliermaster);
					}else {
						processResult = flowSetService.setNSCFlowStepAndLink(rebuildFlowMap, resultReviewconf, suppliermaster);

					}
				}
				
				//合約草稿 新增
				FlowBean flowBean = new FlowBean();
				flowBean.setSysId(APP_ID);
				flowBean.setFormId(flowId);
				/**1222 flow流程改寫
				  *合約草稿 USERID持有人 當有承接人時以承接人為主*/
//				flowuserid = getcontractorId(userInfo.getUserId() , contractNo);
				flowuserid = applicantid;
				flowBean.setApplyNo(contractNo);
				flowBean.setFlowId(flowId);
				flowBean.setFlowVersion("2");
				flowBean.setUserId(flowuserid);
				
				docState = flowService.startFlow(flowBean, null);
				nowTaskId = docState.getNowTaskId();
				int nowTaskNum = Integer.valueOf(nowTaskId.replace("Task", "")); // Integer.valueOf(nowTaskId.substring(nowTaskId.length() - 1, nowTaskId.length()));
				nextTask = "Task" + (nowTaskNum + 1);
				
				docState = flowService.apply(docState, null);
				
				isAgentUserId="N";
			}
			/**判斷第一關合約當前的持有人 若承接人等於當前人員 
			 	*更改其承辦人 並清空承接人 並清除移轉紀錄
			 	*當承接人為空值無須清空
			*/
			if((StringUtils.equals(nowContractUserId, userInfo.getUserId()) || StringUtils.equals(nowContractUserId, getPrincipalUserId(userInfo.getUserId(),nowContractUserId)))&& StringUtils.isNotBlank(undertakeuser) ) {
				JsonPath jsonPathByUndertakeuser;
				JsonPath jsonPathByDept;
				JsonPath jsonPathByuser;
				JsonPath jsonPathByUndertakDept;

				//非制式客製化-EN
				List<Map<String, Object>> resultdataList = json.read("$.data[?(@.datatype == '"+LocaleMessage.getMsg("contractEn.filed.Basic_Information")+"')].docdetail[?(@.resultdata)].resultdata");
				jsonPathByUndertakeuser = JsonPath.compile("$.data[?(@.datatype == '"+LocaleMessage.getMsg("contractEn.filed.Basic_Information")+"')].docdetail[?(@.resultdata)].resultdata."+LocaleMessage.getMsg("contract.field.undertakeuserid"));
				jsonPathByUndertakDept = JsonPath.compile("$.data[?(@.datatype == '"+LocaleMessage.getMsg("contractEn.filed.Basic_Information")+"')].docdetail[?(@.resultdata)].resultdata."+LocaleMessage.getMsg("contract.field.undertakedept"));
				jsonPathByDept = JsonPath.compile("$.data[?(@.datatype == '"+LocaleMessage.getMsg("contractEn.filed.Basic_Information")+"')].docdetail[?(@.resultdata)].resultdata."+LocaleMessage.getMsg("xauth.field.iden"));
				jsonPathByuser = JsonPath.compile("$.data[?(@.datatype == '"+LocaleMessage.getMsg("contractEn.filed.Basic_Information")+"')].docdetail[?(@.resultdata)].resultdata."+LocaleMessage.getMsg("contract.field.contractor"));

				//採原設計方案
				if(resultdataList.size() < 1) {
					jsonPathByUndertakeuser = JsonPath.compile("$.data[?(@.datatype == '基本資料')].docdetail[?(@.resultdata)].resultdata."+LocaleMessage.getMsg("contract.field.undertakeuserid"));
					jsonPathByUndertakDept = JsonPath.compile("$.data[?(@.datatype == '基本資料')].docdetail[?(@.resultdata)].resultdata."+LocaleMessage.getMsg("contract.field.undertakedept"));
					jsonPathByDept = JsonPath.compile("$.data[?(@.datatype == '基本資料')].docdetail[?(@.resultdata)].resultdata."+LocaleMessage.getMsg("xauth.field.iden"));
					jsonPathByuser = JsonPath.compile("$.data[?(@.datatype == '基本資料')].docdetail[?(@.resultdata)].resultdata."+LocaleMessage.getMsg("contract.field.contractor"));

				}
				DocumentContext documentContext = JsonPath.parse(jsonData);
				
				//承接人,承接部門清空
				documentContext.set(jsonPathByUndertakeuser, "");
				documentContext.set(jsonPathByUndertakDept, "");
				
				//部門,承接人重新寫入
				documentContext.set(jsonPathByuser, undertakeuser);

				documentContext.set(jsonPathByDept, idenId);
				
				
				jsonData = documentContext.jsonString();
				ElasticSearchUtil.updateById(dataId, indexName, jsonData);
				//關閉連線
				elasticSearchUtil.closeESClient();
				//刪除移轉紀錄
				QueryWrapper <Contracttransfer> queryWrapper = new QueryWrapper<Contracttransfer>();
				queryWrapper.eq("DATAID", dataId);
				contracttransferMapper.delete(queryWrapper);
				
			}

			processResult = insertEmailQueue(docState, supplieremail, contractname);
			if(StringUtils.equals(processResult.getStatus(), ProcessResult.NG)) {
				errorMsg = new StringBuffer();
				for(String str : processResult.getMessages()) {
					errorMsg.append(str);
				}
				result.put("rtnCode", -1);
				result.put("message", errorMsg.toString());
				return result;
			}
			else if(StringUtils.equals(processResult.getStatus(), ProcessResult.OK)) {
				for(String str : processResult.getMessages()) {
					toComeNames.append(str);
				}
			}
			
			if(isSupplier) {
				result.put("model", "supplierContract");
			}else {
				result.put("model", "contract");
			}
			//寫入備份區
			createJsonFile(jsonData, contractNo, "PUT");
		}
		
		if(StringUtils.equals("審核", type) || StringUtils.equals("核准", type)) {
			DocStateBean docState = list.get(0);
			nowTaskId = docState.getNowTaskId();
			int nowTaskNum = Integer.valueOf(nowTaskId.replace("Task", "")); //Integer.valueOf(nowTaskId.substring(nowTaskId.length() - 1, nowTaskId.length()));
			nextTask = "Task" + (nowTaskNum + 1);
			docState = flowService.claim(docState.nowUser(nowUserId, docState.getNowTaskId()));
			if(StringUtils.equals("Task1", docState.getNowTaskId())) {
				docState = flowService.process(docState.nowUser(nowUserId, docState.getNowTaskId()), null);
			}else {
				docState = flowService.process(docState.nowUser(nowUserId, docState.getNowTaskId()), DefaultFlowAction.APPROVE);
			}
			processResult = insertEmailQueue(docState, supplieremail, contractname);
			if(StringUtils.equals(processResult.getStatus(), ProcessResult.NG)) {
				errorMsg = new StringBuffer();
				for(String str : processResult.getMessages()) {
					errorMsg.append(str);
				}
				result.put("rtnCode", -1);
				result.put("message", errorMsg.toString());
				return result;
			}
			else if(StringUtils.equals(processResult.getStatus(), ProcessResult.OK)) {
				for(String str : processResult.getMessages()) {
					toComeNames.append(str);
				}
			}
			
			// 末關卡完成後寄信給申請者與供應商
			if(StringUtils.equals("End", docState.getNowTaskId())) {
				processResult = insertEmailToApplicant(docState, contractname);
				if(StringUtils.equals(processResult.getStatus(), ProcessResult.NG)) {
					errorMsg = new StringBuffer();
					for(String str : processResult.getMessages()) {
						errorMsg.append(str);
					}
					result.put("rtnCode", -1);
					result.put("message", errorMsg.toString());
					return result;
				}
				processResult = insertEmailToSupplier(docState, supplieremail, contractname);
				if(StringUtils.equals(processResult.getStatus(), ProcessResult.NG)) {
					errorMsg = new StringBuffer();
					for(String str : processResult.getMessages()) {
						errorMsg.append(str);
					}
					result.put("rtnCode", -1);
					result.put("message", errorMsg.toString());
					return result;
				}
			}
			
			if(isSupplier) {
				result.put("model", "supplierPending");
			}else {
				result.put("model", "pending");
			}
		}
		
		if(StringUtils.equals("駁回", type)) {
			DocStateBean docState = list.get(0);
			//判斷是否刪檔
			nowTaskId = docState.getNowTaskId();
			int nowTaskNum = Integer.valueOf(nowTaskId.replace("Task", "")); //Integer.valueOf(nowTaskId.substring(nowTaskId.length() - 1, nowTaskId.length()));
			nextTask = "Task" + (nowTaskNum - 1);
			
			QueryWrapper<Flowstep> flowStepQueryWrapper = new QueryWrapper<Flowstep> ();
			flowStepQueryWrapper.eq("FLOWID", docState.getFlowId());
			flowStepQueryWrapper.eq("STEPID", nextTask);
			List<Flowstep> flowstepList = flowstepMapper.selectList(flowStepQueryWrapper);
			
			if(flowstepList.size() > 0) {
				String stepName = flowstepList.get(0).getStepname();
				
				if(StringUtils.equals("申請人", stepName)) {
					QueryWrapper<Contractfile> contractfileQueryWrapper = new QueryWrapper<Contractfile>();
					contractfileQueryWrapper.eq("DATAID", dataId);
					contractfileQueryWrapper.eq("TYPE", "02");
					List<Contractfile> contractfileList = contractfileMapper.selectList(contractfileQueryWrapper);
					Contractfile contractfile = getNewFile(contractfileList);
					if(!deleteFile(contractfile)) {
						result.put("rtnCode", -1);
						result.put("message", type + " 失敗 檔案刪除失敗!");
						return result;
					}
				}
				else if(StringUtils.equals("供應商", stepName)) {
					QueryWrapper<Contractfile> contractfileQueryWrapper = new QueryWrapper<Contractfile>();
					contractfileQueryWrapper.eq("DATAID", dataId);
					contractfileQueryWrapper.eq("TYPE", "02");
					contractfileQueryWrapper.like("FLIENOTE", stepName);
					List<Contractfile> contractfileList = contractfileMapper.selectList(contractfileQueryWrapper);
					Contractfile contractfile = getNewFile(contractfileList);
					if(!deleteFile(contractfile)) {
						result.put("rtnCode", -1);
						result.put("message", type + " 失敗 檔案刪除失敗!");
						return result;
					}else {
						contractfileQueryWrapper = new QueryWrapper<Contractfile>();
						contractfileQueryWrapper.eq("DATAID", dataId);
						contractfileQueryWrapper.eq("TYPE", "02");
						contractfileQueryWrapper.like("FLIENOTE", stepName);
						contractfileList = contractfileMapper.selectList(contractfileQueryWrapper);
						contractfile = getNewFile(contractfileList);
						if(!deleteFile(contractfile)) {
							result.put("rtnCode", -1);
							result.put("message", type + " 失敗 檔案刪除失敗!");
							return result;
						}
					}
				}
			}else {
				result.put("rtnCode", -1);
				result.put("message", type + " 失敗 查無合約流程步驟!");
				return result;
			}
			docState = flowService.claim(docState.nowUser(nowUserId, docState.getNowTaskId()));
			docState = flowService.process(docState.nowUser(nowUserId, docState.getNowTaskId()), DefaultFlowAction.RETURN);
			processResult = insertEmailQueue(docState, supplieremail, contractname);
			if(StringUtils.equals(processResult.getStatus(), ProcessResult.NG)) {
				errorMsg = new StringBuffer();
				for(String str : processResult.getMessages()) {
					errorMsg.append(str);
				}
				result.put("rtnCode", -1);
				result.put("message", errorMsg.toString());
				return result;
			}
			else if(StringUtils.equals(processResult.getStatus(), ProcessResult.OK)) {
				for(String str : processResult.getMessages()) {
					toComeNames.append(str);
				}
			}
			if(isSupplier) {
				result.put("model", "supplierPending");
			}else {
				result.put("model", "pending");
			}
		}
		
		if(StringUtils.isNoneBlank(type)) {
			jsonData = JsonUtil.jsonSetValueByKey(jsonData, "todo", type);
		}
		//判斷是否當前關卡人員
		if(list.size()>0) {
//			if(nowUserId.equals(userInfo.getUserId()) || nowContractUserId.equals(userInfo.getUserId()) ) 
			if(StringUtils.equals(nowUserId, userInfo.getUserId()) || StringUtils.equals(nowContractUserId, userInfo.getUserId()) ) 
				isAgentUserId="N";
			else 
				isAgentUserId="Y";
		}
		else {
			isAgentUserId="N";
		}
		if (StringUtils.equals(agentuser,userInfo.getUserId())) {
			isAgentUserId="Y";
		}
		
		//更新
		if(StringUtils.isNotBlank(jsonData) && StringUtils.isNoneBlank(contractNo) && StringUtils.isNoneBlank(indexName)) {
			// 取得原userId
			List<XauthRoleAgentUser> roleAgentUsers = commonService.getAgentDatas(userInfo.getIdenId(), userInfo.getUserId());
			List<String> userIds = new ArrayList<String>();
//			StringBuffer userIdSetBy = new StringBuffer();
			String userIdSetBy = "";

			for(XauthRoleAgentUser roleAgentUser : roleAgentUsers) {
				userIds.addAll(commonService.getAgentSetByUsers(flowId, nowTaskId, roleAgentUser));
			}
			userIdSetBy = commonService.getDataByComma(userIds);
			logger.info("userIdSetBy === "+userIdSetBy);
//			for(int i = 0 ; i < userIds.size() ; i ++) {
//				if(i == userIds.size() - 1) {
//					userIdSetBy.append(userIds.get(i));
//				} else {
//					userIdSetBy.append(userIds.get(i)).append(",");
//				}
//			}
			processResult = updataFlowValueToDB(indexName, contractNo, jsonData, type, list,isAgentUserId, userIdSetBy);
			if(StringUtils.equals(ProcessResult.NG, processResult.getStatus())) {
				result.put("rtnCode", -1);   
				result.put("message", type + " 資料變更失敗");
				return result;
			}
		}else {
			result.put("rtnCode", -1);
			result.put("message", type + " 資料不得為空!");
			return result;
		}
		result.put("rtnCode", 0);
	
		if(toComeNames.length()>2) {
			result.put("message", type+"成功"+toComeNames);
		}else result.put("message", type+"成功");
		
		return result;
	}
	
	/**
	 * 編輯畫面 暫存 ＆＆ 作廢 按鈕呼叫方法
	 * @param params
	 * @param type
	 * @param indexName
	 * @param contractNo
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> updataValue(Map<String, Object> params, String type, String indexName, String contractNo) throws Exception{
		Map<String, Object> result = new HashMap<String, Object>();
		List<DocStateBean> list = null;
		ProcessResult processResult = null;
		String jsonData = new Gson().toJson(params.get("data"), Map.class);
		UserInfo userInfo = userContext.getCurrentUser();
		String nowUserId = "";
		String flowId = "";
		String isAgentUserId = "X";
		String userId = "";
		
		
		Map<String , Object> comparm = new HashMap<String, Object>();
		comparm.put("dataid", contractNo);
		//取得selectContractMaster資訊 NOWUSERID 為合約建立人 為判斷是否為承辦人或承辦代理人
		List<Map <String ,Object>> userList = contractMapper.selectContractMasterByTask1(comparm);
		if(userList.size() > 0) {
			nowUserId = MapUtils.getString(userList.get(0), "ORIUSERIDS") ;
			//判斷是否為代理人
			String userid = MapUtils.getString(userList.get(0), "NOWUSERID") ;
			if(StringUtils.equals(userid, userInfo.getUserId())) {
				isAgentUserId = "N";
			}
			else if(StringUtils.equals(userid, getPrincipalUserId(userInfo.getUserId(),userid))) {
				isAgentUserId = "Y";

			}
			else if(StringUtils.equals(userContext.getCurrentUser().getUserType(), USER_TYPE.SYS_ADMIN.getCode())) {
				isAgentUserId = "Y";
			}
			else {
				result.put("rtnCode", -1);
				result.put("message", type + "此流程人員異常");
				return result;
			}
			
		}
		
		ReadContext readContext = JsonPath.parse(jsonData);
		String todo = readContext.read("$.todo");
		
		// 取得原userId
		Map<String, Object> contractNoMap = new HashMap<String, Object>();
		contractNoMap.put("dataid", contractNo);
		List<Map<String, Object>> task1ContractData = contractMapper.selectContractMaster(contractNoMap);
		if(task1ContractData != null && task1ContractData.size() > 0) {
			userId = MapUtils.getString(task1ContractData.get(0), "NOWUSERID");
		}
		
		if(StringUtils.isNoneBlank(type)) {
			//執行暫存且TODO為駁回，不變更TODO狀態
			if(StringUtils.equals(todo, "駁回") && StringUtils.equals(type, "暫存")) {
				jsonData = JsonUtil.jsonSetValueByKey(jsonData, "todo", todo);
			}else {
				jsonData = JsonUtil.jsonSetValueByKey(jsonData, "todo", type);
			}
		}
		
		if(StringUtils.equals("作廢", type)) {
			
			Contractmaster contractmaster = commonService.getContractmasterData(contractNo);
			
			if(contractmaster == null) {
				result.put("rtnCode", -1);
				result.put("message", "查無合約資料");
				return result;
			}else {
				flowId = contractmaster.getFlowid();
			}
			
			list = flowQueryService.fetchToDoListBy(nowUserId, flowId, APP_ID, true, false);
			//作廢 已建立流程，須取消流程。      20200911 by max
			if(list.size() > 0){
				DocStateBean docState = list.get(0);
				docState = flowService.claim(docState.nowUser(nowUserId, docState.getNowTaskId()));
				docState = flowService.process(docState.nowUser(nowUserId, docState.getNowTaskId()), DefaultFlowAction.CANCEL);
			}
			
			//作廢導頁
			if(StringUtils.equals(todo, "駁回")) {
				result.put("model", "pending");
			}else {
				result.put("model", "contract");
			}
		}
		
		//更新
		if(StringUtils.isNotBlank(jsonData) && StringUtils.isNoneBlank(contractNo) && StringUtils.isNoneBlank(indexName)) {
			processResult = updataFlowValueToDB(indexName, contractNo, jsonData, type, list,isAgentUserId, userId);
			if(StringUtils.equals(ProcessResult.NG, processResult.getStatus())) {
				result.put("rtnCode", -1);
				result.put("message", type + " 資料變更失敗");
				return result;
			}
		}else {
			result.put("rtnCode", -1);
			result.put("message", type + " 資料不得為空!");
			return result;
		}
		
		result.put("rtnCode", 0);
		result.put("message", type + " 成功!");
		return result;
	}
	
	/**
	 * 新增合約主檔  && 更新合約主檔 && 更新合約主檔ES
	 * @param indexName
	 * @param contractNo
	 * @param jsonData
	 * @param type
	 * @param list
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public ProcessResult updataFlowValueToDB(String indexName, String contractNo, String jsonData, String type, List<DocStateBean> list,String isAgentUserId, String userId) throws Exception {
		Date ts = new Timestamp(new Date().getTime());
		UserInfo userInfo = userContext.getCurrentUser();
		Contractreview contractreview = new Contractreview();
		ProcessResult result = new ProcessResult();
		DocStateBean docState = null;
		String nowTaskId = "";
		JsonPath jsonPath;
		
		if(list != null && list.size() > 0) {
			docState = list.get(0);
			nowTaskId = docState.getNowTaskId();
		}else {
			nowTaskId = "Task1";
		}
		
		if(StringUtils.isBlank(indexName) || StringUtils.isBlank(contractNo) || StringUtils.isBlank(jsonData)) {
			result.setStatus(ProcessResult.NG);
			result.addMessage("資料不得為空!");
			return result;
		}
		
		// 取得CONTRACTREVIEW STEPNAME欄位
		String stepname = "";
		ProcessResult getFlowstepNameResult = flowSetService.getFlowstepName(contractNo);
		Map<String, Object> flowstepNameMap = getFlowstepNameResult.getResult();
		List<Map<String, Object>> flowstepNameList = (List<Map<String, Object>>) flowstepNameMap.get("dataList");
		for(Map<String, Object> flowstepNameData : flowstepNameList) {
			if(StringUtils.equals(nowTaskId, MapUtils.getString(flowstepNameData, "stepid"))) {
				stepname = MapUtils.getString(flowstepNameData, "rolename");
				break;
			}
		}
		
		ReadContext json = JsonPath.parse(jsonData);
		
		//非制式客製化-EN
		List<String> vals = json.read("$.data[?(@.datatype == '"+LocaleMessage.getMsg("contractEn.filed.Approval_Comment")+"' )].docdetail[?(@.displayname == '"+LocaleMessage.getMsg("contractEn.filed.ApprovalComment")+"' && @.value != '' && @.uitype == 'label')].value");
		 jsonPath = JsonPath.compile("$.data[?(@.datatype == '"+LocaleMessage.getMsg("contractEn.filed.Approval_Comment")+"' )].docdetail[?(@.displayname == '"+LocaleMessage.getMsg("contractEn.filed.ApprovalComment")+"' && @.value != '' && @.uitype == 'label')].value");
		//採原設計方案
		if(vals.size() <= 0) {
		vals = json.read("$.data[?(@.datatype == '審核意見' )].docdetail[?(@.displayname == '審核意見' && @.value != '' && @.uitype == 'label')].value");
		jsonPath = JsonPath.compile("$.data[?(@.datatype == '審核意見' )].docdetail[?(@.displayname == '審核意見' && @.value != '' && @.uitype == 'label')].value");
		}
		
		//審核意見
		if(!StringUtils.equals(type, "暫存")) {
			contractreview.setDataid(contractNo);
			contractreview.setStepid(nowTaskId);
			contractreview.setReviewrlt(type);
			contractreview.setReviewuser(userInfo.getUserId() + "-" + userInfo.getUserCname());
			String note=vals.size() > 0 ? vals.get(0) : "";
			
			if(isAgentUserId.equals("Y")) {
				note = "(代)" + note;
				contractreview.setUserids(userId);
			}
			
			contractreview.setReviewnote(note);
			contractreview.setStepname(stepname);
			contractreview.setCreatedate(ts);
			contractreview.setCreateuser(getCreOrUpdUser(null));
			contractreviewMapper.insert(contractreview);	
		}
		
		//清空審核意見欄位
		DocumentContext documentContext = JsonPath.parse(jsonData);
		documentContext.set(jsonPath, "");
		jsonData = documentContext.jsonString();
		
		//合約主檔
		updateContractmaster(null, contractNo, jsonData, type);
		//合約主檔ES
		ElasticSearchUtil.updateById(contractNo, indexName, jsonData);
		//關閉連線
		elasticSearchUtil.closeESClient();
	
		return result;
	}
	
	/**
	 * 合約編輯Grid Query
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> fixQuery(Map<String, Object> params) throws Exception{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		List<Map<String, Object>> jsonList = new ArrayList<Map<String, Object>>();
		Map<String, Object> result = new HashMap<String, Object>();
		List<Contractfile> contravtfileList = null;
		Map<String, Object> jsonMap = null;
		
		String dataid = "";
		String isdownload = "";
		String datatype = MapUtils.getString(params, "func");
		String idenId = MapUtils.getString(params, "idenId");
		String jsonData = new Gson().toJson(params.get("data"), Map.class);
		ReadContext json = JsonPath.parse(jsonData);
		//非制式客製化-EN
		List<Map<String, Object>> resultdataList = json.read("$.data[?(@.datatype == '"+LocaleMessage.getMsg("contractEn.filed.Basic_Information")+"')].docdetail[?(@.resultdata)].resultdata");
		if(resultdataList.size()<1) {
			resultdataList = json.read("$.data[?(@.datatype == '基本資料')].docdetail[?(@.resultdata)].resultdata");
		}
		
		for(Map<String, Object> resultdata : resultdataList) {
			dataid = MapUtils.getString(resultdata, "合約編碼");
		}
		if(datatype.indexOf("附件資料") > -1) {
			List<String> fixblockList = json.read("$.data[?(@.datatype == '" + datatype + "')].docdetail[?(@.uitype == 'label')].fixblock");
			
			fixblockList = getSingle(fixblockList);
			QueryWrapper<Contractfile> queryWrapper = new QueryWrapper<Contractfile>();
			queryWrapper.orderByDesc("CREATEDATE");
			queryWrapper.eq("DATAID", dataid);
			queryWrapper.eq("TYPE", "02");
			contravtfileList = contractfileMapper.selectList(queryWrapper);
			if(contravtfileList.size() != 0) {
				for(Contractfile contractfile : contravtfileList) {
					jsonMap = new HashMap<String, Object>();
					jsonMap.put("id", contractfile.getSerno().toString());
					jsonMap.put("檔案類型", contractfile.getFiletype());//合約主檔
					jsonMap.put("檔案名稱", contractfile.getFliename());
					jsonMap.put("檔案說明", contractfile.getFlienote());
					jsonMap.put("#下載BTN", contractfile.getIsdownload());
					jsonMap.put("供應商可否下載", contractfile.getIsdownload());
					if(idenId.equals("999999999")) {
						if(contractfile.getIsdownload().equals("是")) {
							jsonList.add(jsonMap);
						}
					}
					else {
						jsonList.add(jsonMap);
					}
				}
			}
			
			if(fixblockList.size() == 0 && jsonList.size() == 0) {
				result.put("rtnCode", 99);
				result.put("message", "查無資料");
				return result;
			}
			for(String str : fixblockList) {
				jsonMap = new HashMap<String, Object>();
				String jsonPathUrl = "$.data[?(@.datatype == '" + datatype + "')].docdetail[?(@.uitype == 'label' && @.fixblock == '" + str + "')]";
				List<Map<String, Object>> vals = json.read(jsonPathUrl, List.class);
				for(Map<String, Object> val : vals) {
					String displayName =  MapUtils.getString(val, "displayname");
					jsonMap.put("id", str);
					if(displayName.indexOf("檔案類型") > -1) {
						jsonMap.put("檔案類型", MapUtils.getString(val, "value"));
					}
					if(displayName.indexOf("檔案上傳") > -1) {
						jsonMap.put("檔案名稱", MapUtils.getString(val, "value"));
					}
					if(displayName.indexOf("檔案說明") > -1) {
						jsonMap.put("檔案說明", MapUtils.getString(val, "value"));
					}
					if(displayName.indexOf("供應商可否下載") > -1) {
//						isdownload = MapUtils.getString(val, "value").replaceAll("[a-zA-Z]", "");
						isdownload = MapUtils.getString(val, "value");
						if(isdownload.indexOf("YES") > -1)
							isdownload = "是";
						else if(isdownload.indexOf("NO") > -1)
							isdownload = "否";
						jsonMap.put("#下載BTN", isdownload);
						jsonMap.put("供應商可否下載",isdownload);
					}
				}
				
				if(idenId.equals("999999999")) {
					if(isdownload.equals("是")) {
						jsonList.add(jsonMap);
					}
				}
				else {
					jsonList.add(jsonMap);
				}
			}
		}
		
		if(datatype.indexOf("審核意見")> -1) {
			QueryWrapper<Contractreview> contractreviewQueryWrapper = new QueryWrapper<Contractreview>();
			contractreviewQueryWrapper.orderBy(true, true, "CREATEDATE");
			contractreviewQueryWrapper.eq("DATAID", dataid);
			List<Contractreview> contractreviewList = contractreviewMapper.selectList(contractreviewQueryWrapper);
			
			for(Contractreview contractreview : contractreviewList) {
				int size = jsonList.size();
				
				jsonMap = new HashMap<String, Object>();
				jsonMap.put("流程關卡", contractreview.getStepid());
				jsonMap.put("審查結果", contractreview.getReviewrlt());
				jsonMap.put("審核人員", contractreview.getReviewuser());
				jsonMap.put("收到時間", size > 0 ? MapUtils.getString(jsonList.get(size - 1), "送出時間") : "");
				jsonMap.put("送出時間", sdf.format(contractreview.getCreatedate().getTime()));
				jsonMap.put("審核意見", contractreview.getReviewnote());
				jsonList.add(jsonMap);
			}
		}
		
		if(jsonList.size() != 0) {
			jsonMap = new HashMap<String, Object>();
			jsonMap.put("data", JsonUtils.obj2json(jsonList));
			result.put("jsonData",jsonMap);
			result.put("rtnCode", 0);
			result.put("message", "查詢成功");
		}else {
			result.put("rtnCode", 99);
			result.put("message", "查無資料");
		}
		return result;
	}
	
	/**
	 * 檔案上傳
	 * @param files
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public ProcessResult upload(MultipartFile files, Map<String, Object> params) throws Exception {
		ProcessResult result = new ProcessResult();
		
		String dataid = MapUtils.getString(params, "dataid");
		String fileUrl = FILE_URL + dataid;
		String fileName = files.getOriginalFilename();
		
		//寫入ElasticSearch & Oracle
		ProcessResult insertResult = fileInsert(files, params);
		if(StringUtils.equals(insertResult.getStatus(), ProcessResult.NG)) {
			result.setStatus(insertResult.getStatus());
			result.setMessages(insertResult.getMessages());
		} else {
			File file = new File(fileUrl);
			
			if(!file.exists()){
				file.mkdir();
			}
			
			file = new File(fileUrl + "/" + fileName);
			files.transferTo(file);
			
			result.setStatus(insertResult.getStatus());
			result.addMessage("檔案上傳:成功");
		}
		return result;
	}
	
	/**
	 * 檔案上傳塞記錄檔Table
	 * @param files
	 * @param params
	 * @return
	 */
	private ProcessResult fileInsert(MultipartFile files, Map<String, Object> params) throws Exception {
		List<Contractfile> contravtfileList = new ArrayList<Contractfile>();
		Contractfile contractfile = new Contractfile();
		ProcessResult result = new ProcessResult();
		
		String fileName = files.getOriginalFilename();
		String dataid = MapUtils.getString(params, "dataid");
		String fixid = MapUtils.getString(params, "fixid");
		String fileType = MapUtils.getString(params, "fileType");
		String isdownload = MapUtils.getString(params, "isDownload");
		if(isdownload.indexOf("YES") > -1)
			isdownload = "是";
		else if(isdownload.indexOf("NO") > -1)
			isdownload = "否";
//		isdownload=isdownload.replaceAll("[a-zA-Z]", "");
		
		try {
			
			contractfile.setFliename(fileName);
			contractfile.setFliepath(FILE_URL + dataid);
			contractfile.setDataid(dataid);
			contractfile.setFixid(fixid);
			contractfile.setType("01");
			contractfile.setFiletype(fileType);
			contractfile.setIsdownload(isdownload);
			
			QueryWrapper<Contractfile> queryWrapper = new QueryWrapper<Contractfile>();
			queryWrapper.eq("DATAID", dataid);
			queryWrapper.eq("FIXID", fixid);
			contravtfileList = contractfileMapper.selectList(queryWrapper);
			
			Date ts = new Timestamp(new Date().getTime());
			if(contravtfileList.size() == 0) {
				contractfile.setCreateuser(getCreOrUpdUser(null));
				contractfile.setCreatedate(ts);
				contractfileMapper.insert(contractfile);
			}else {
				contractfile.setUpdateuser(getCreOrUpdUser(null));
				contractfile.setUpdatedate(ts);
				
				UpdateWrapper<Contractfile> updateWrapper = new UpdateWrapper<Contractfile>();
				updateWrapper.eq("DATAID", dataid);
				updateWrapper.eq("FIXID", fixid);
				contractfileMapper.update(contractfile, updateWrapper);
			}
			
			result.setStatus(ProcessResult.OK);
			result.addMessage("檔案上傳:成功");
			
		}catch(Exception e){
			result.setStatus(ProcessResult.NG);
			result.addMessage("ES檔案上傳:失敗");
			logger.error("Exceptio 出現 : " + e, e);
		}
//		catch(Exception e){
//			result.setStatus(ProcessResult.NG);
//			result.addMessage("檔案上傳:失敗");
//		    e.printStackTrace();
//			System.out.println("insert Exception:" + e);
//		}finally{
//			
//		}
		
		return result;
	}
	
	/**
	 * 刪除檔案 實體檔 與 Table 資料
	 * @param contraction
	 * @return
	 */
	private boolean deleteFile(Contractfile contraction) {
		File file = null;
		String path = "";
		String fileName = "";
		
		if(contraction == null) {
			return false;
		}else {
			path = contraction.getFliepath();
			fileName = contraction.getFliename();
		}
		
		if(StringUtils.isBlank(path) || StringUtils.isBlank(fileName)) {
			logger.info(" Flie Delete Fail ");
			return false;
		}else {
			file = new File(path + "/" + fileName);
		}
		
		if(file.exists()){
			file.delete();
		}else {
			logger.info(" Flie Delete Fail Can Not Find The File ");
			return false;
		}
		
		UpdateWrapper<Contractfile> wrapper = new UpdateWrapper<Contractfile>();
		wrapper.eq("SERNO", contraction.getSerno());
		wrapper.eq("DATAID", contraction.getDataid());
		wrapper.eq("FLIENAME", contraction.getFliename());
		contractfileMapper.delete(wrapper);
		
		return true;
	}
	
	/**
	 * 取得Table 最新 File Name 編號
	 * @param contractfileList
	 * @return
	 */
	private static Contractfile getNewFile(List<Contractfile> contractfileList) {
		Contractfile newContractfile = null;
		int row = 0;
		for(Contractfile contractfile : contractfileList) {
			if(StringUtils.isNoneBlank(contractfile.getFliename())) {
				String str = contractfile.getFliename().replace(".pdf", "");
				int version = Integer.valueOf(str.substring(str.length()-1 , str.length()));
				if(row < version) {
					row = version;
					newContractfile = contractfile;
				}
			}
		}
		return newContractfile;
	}
	
	/**
	 * 去重複
	 * @param list
	 * @return
	 */
	public static List<String> getSingle(List<String> list) {
		List<String> tempList = new ArrayList<String>();          //1,建立新集合
		Iterator<String> it = list.iterator();              	  //2,根據傳入的集合(老集合)獲取迭代器
		while(it.hasNext()) {                					  //3,遍歷老集合
			String str = it.next();               				  //記錄住每一個元素
			if(!tempList.contains(str)) {           			  //如果新集合中不包含老集合中的元素
				tempList.add(str);                				  //將該元素新增
			}
		}  
		
		for(int i = 0 ; i < tempList.size() ; i ++) {
			if(tempList.get(i) != null && tempList.get(i).indexOf("fix0_0") > -1) {
				tempList.remove(i);
			}
		}
		
		return tempList;
	}

	/**
	 * 檢核新增合約頁面
	 * 
	 * @param params
	 * @return
	 */
	private ProcessResult checkContractInsertData(Map<String, Object> params, Suppliermaster suppliermaster) {
		ProcessResult result = new ProcessResult();
		result.setStatus(ProcessResult.NG);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		try {
			Integer contractAmount = MapUtils.getInteger(params, "contractAmount");
			String oldFlowid = MapUtils.getString(params, "disp");
			String contractBgn = MapUtils.getString(params, "contractBgnDate");
			String contractEnd = MapUtils.getString(params, "contractEndDate");
			String year = MapUtils.getString(params, "year");
			String disp = MapUtils.getString(params, "disp"); 
			String module = MapUtils.getString(params, "module"); 
			String templateType = MapUtils.getString(params, "templateType");
			String perTemplateDataid = MapUtils.getString(params, "perTemplateDataid");
			StringBuffer suppBlankMsg = new StringBuffer(); // 紀錄供應商資料不完整訊息
			QueryWrapper<Systemparam> systemparamWrapper = new QueryWrapper<Systemparam>(); 
			Systemparam systemparam = null; 
			UserInfo userInfo = userContext.getCurrentUser(); 
			boolean isIdenId = false; // 判斷是否為可進行新增合約的部門 

			if (StringUtils.isBlank(year)) { 
				result.addMessage("請輸入合約年度"); 
				return result; 
			} 

			if (StringUtils.isBlank(module)) { 
				result.addMessage("請選擇合約模式"); 
				return result; 
			} 
			
			if (StringUtils.isBlank(disp)) { 
				result.addMessage("請選擇合約範本"); 
				return result; 
			} 

			if (StringUtils.isNotBlank(contractBgn) && StringUtils.isNotBlank(contractEnd)) {
				Date contractBgnDate = sdf.parse(contractBgn);
				Date contractEndDate = sdf.parse(contractEnd);
				
				if(!StringUtils.equals(year, contractBgn.substring(0, 4))) {
					result.addMessage("合約期間起日需等於合約年度");
					return result;
				}
				
				if (contractEndDate.before(contractBgnDate)) {
					result.addMessage("合約期間訖日不可大於起日");
					return result;
				}
			}
			if(StringUtils.isBlank(templateType)) {
				result.addMessage("請選擇範本種類"); 
				return result; 
			} else if(StringUtils.equals("PT", templateType)) {
				if(StringUtils.isBlank(perTemplateDataid)) {
					result.addMessage("請選擇個人範本"); 
					return result; 
				}
			}
			
			// 檢核供應商資料是否完整
			if(StringUtils.isBlank(suppliermaster.getSuppliercname())) {
				suppBlankMsg.append(" " + LocaleMessage.getMsg("suppliter.field.suppliercname"));
			}
			if(StringUtils.isBlank(suppliermaster.getPicuser())) {
				suppBlankMsg.append(" " + LocaleMessage.getMsg("suppliter.field.picuser"));
			}
			if(StringUtils.isBlank(suppliermaster.getSuppliercaddr())) {
				suppBlankMsg.append(" " + LocaleMessage.getMsg("suppliter.field.suppliercaddr"));
			}
			if(StringUtils.isBlank(suppliermaster.getContacruser())) {
				suppBlankMsg.append(" " + LocaleMessage.getMsg("suppliter.field.contacruser"));
			}
			if(StringUtils.isBlank(suppliermaster.getSupplieremail())) {
				suppBlankMsg.append(" " + LocaleMessage.getMsg("suppliter.field.supplieremail"));
			}
			if(StringUtils.isNotBlank(suppBlankMsg.toString())) {
				result.addMessage("請供應商維護" + suppBlankMsg.toString());
				return result;
			}
			
			systemparamWrapper.eq("APPID", APP_ID); 
			systemparamWrapper.eq("CONTRACTMODEL", module); 
			systemparamWrapper.eq("ACTIONTYPE", "00"); 
			systemparamWrapper.eq("FLOWID", disp); 
//			if(StringUtils.equals(module, "SC")) { 
//				systemparamWrapper.eq("DEPTNO", deptNo); 
//			} else { 
//				systemparamWrapper.eq("FLOWID", disp); 
//			} 
			List<Systemparam> systemparamList =  systemparamMapper.selectList(systemparamWrapper); 
			if(systemparamList != null && systemparamList.size() > 0) { 
				systemparam = systemparamList.get(0); 
				for(String idenId : systemparam.getIdenids().split(",")) { 
					if(StringUtils.equals(idenId, userInfo.getIdenId())) { 
						isIdenId = true; 
						break; 
					} 
				} 
				if(!isIdenId) { 
					result.addMessage("您無權限新增此合約範本"); 
					return result; 
				} 
			} 
			
			
			//金額異常錯誤訊息
			//抓流程編碼(前5碼)
			String first5 = oldFlowid.substring(0, 5);
			//去除前5碼，取得金額
			String str = oldFlowid.substring(5);
			Integer listsize = 0;
			//判定是否為數值
			if (str.matches("^-?[0-9]+$")) {
				listsize = Integer.valueOf(str);
			}
			if (listsize > 0) {
				QueryWrapper<Reviewconf> reviewconfQueryWrapper = new QueryWrapper<Reviewconf>();
				reviewconfQueryWrapper.like("FLOWID", first5);
				//字串轉數字比對
				reviewconfQueryWrapper.orderByAsc("TO_NUMBER(CONDITION)");
				List<Reviewconf> rList = reviewconfMapper.selectList(reviewconfQueryWrapper);
				List<Integer> list = new LinkedList<Integer>();
				
				//金額換算排序
				rList.forEach(action -> {
					list.add(Integer.valueOf(action.getFlowid().substring(5)));
				});
				Collections.sort(list);
				
				//比對合約金是否符合金額限制
				if (list.size() == 1) {
					if ((list.get(0) % 2) == 0) {
						if (contractAmount > list.get(0)) {
							result.addMessage("金額大於異常");
							return result;
						}
					}
					else if ((list.get(0) % 2) != 0) {
						if (contractAmount < list.get(0)) {
							result.addMessage("金額小於");
							return result;
						}
					}
				} else if (list.size() > 1) {
					int nowpostion = list.indexOf(listsize);
					if((list.get(nowpostion) % 2) != 0) {
						if(nowpostion != 0) {
							for (int j =nowpostion+1; j <list.size(); j++) {
								if ((list.get(j) % 2) == 1 && (list.get(j) > list.get(nowpostion))) {
									if(list.get(nowpostion) <= contractAmount && contractAmount < list.get(j)) {
										break;
									}
									else {
										result.addMessage("金額上限異常");
										return result;
									}
								}
							}
						}
					}
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("checkContractInsertData exception", e, e);
			result.addMessage(MSG_KEY.PARAMS_EMPTY.getMessage());
			return result;
		}
		result.setStatus(ProcessResult.OK);
		return result;
	}
	
	/**
	 * 取得當前使用者 部門階層Cname
	 * @param deptCnameList
	 * @param nextIdenid
	 * @return
	 */
	public List<String> getAllDept(List<String> deptCnameList, String nextIdenid, String firstIdenid){
		
		if(deptCnameList == null) {
			deptCnameList = new ArrayList<String>();
		}
		
		QueryWrapper<XauthDept> queryWrapper = new QueryWrapper<XauthDept>();
		queryWrapper.eq("IDEN_ID", nextIdenid);
		List<XauthDept> xauthDeptList = xauthDeptMapper.selectList(queryWrapper);
		String parentId = xauthDeptList.get(0).getParentId();
		String cName = xauthDeptList.get(0).getCname();
		String idenId = xauthDeptList.get(0).getIdenId();

		if(!StringUtils.equals("#", parentId)) {
			if(!StringUtils.equals(firstIdenid, idenId)) {
				deptCnameList.add(cName);
			}
			return getAllDept(deptCnameList, parentId, firstIdenid);
		}else {
			return deptCnameList;
		}
	}

	/**
	 * 建立 ES indexName 
	 * @param year
	 * @param spec
	 * @return
	 */
	public static String getIndexName(String year, String spec) {
		String indexName = year + "_" + spec.toLowerCase() + "_qryrec_doc";
		return indexName;
	}
	
	/**
	 * 合約流水號
	 * @param years 系統年(yyyy)
	 * @return 1 (一碼)
	 * @throws Exception
	 */
	public String getContractNo(boolean isLogr, String years, String section, String suppliercode) throws Exception{
		Codelist codelist = new Codelist();
		QueryWrapper<Codelist> queryWrapper = new QueryWrapper<Codelist>();
		
		String resultString = "";
		
		if(isLogr) {
			resultString += "LOGR";
			resultString += years;
			resultString += section;
			
			codelist.setClasstype("LOGR");
			queryWrapper.eq("CLASSTYPE", "LOGR");
			queryWrapper.eq("DEPTNO", section);
		}else {
			resultString += "CUSM";
			resultString += years;
			
			codelist.setClasstype("CUSM");
			queryWrapper.eq("CLASSTYPE", "CUSM");
			queryWrapper.isNull("DEPTNO");
		}
		
		resultString += suppliercode;
		
		queryWrapper.eq("SYS", "SEQUENCE");
		queryWrapper.eq("YEARS", years);
		queryWrapper.eq("SUPPLIERCODE", suppliercode);
		
		List<Codelist> codelistList = codelistMapper.selectList(queryWrapper);
		
		if(codelistList.size() == 0) {
			resultString += "1";
			codelist.setSys("SEQUENCE");
			codelist.setYears(years);
			codelist.setAnumber(2);
			codelist.setNote("序號");
			if(isLogr) {
				codelist.setDeptno(section);
			}
			codelist.setSuppliercode(suppliercode);
			codelistMapper.insert(codelist);
		}else {
			codelist = codelistList.get(0);
			Integer anumber = codelist.getAnumber();
			resultString +=  String.valueOf(anumber);
			codelist.setAnumber(anumber + 1);
			codelistMapper.update(codelist, queryWrapper);
		}
		
		return resultString;
	}
	
	/**
	 * 將新增頁面資料塞入json
	 * 
	 * @param json
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public String insertToJson(String json, Map<String, Object> params, Suppliermaster suppliermaster, String templateYear, String templateType, String isUseLastCon) throws Exception {
		json = json.replaceAll("\\\\", "");
		Map<String, Object> paramsData = JsonUtils.json2Object(json, Map.class, false);
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		Map<String, Object> resultdataMap = new HashMap<String, Object>();
		String contractBgnDate = MapUtils.getString(params, "contractBgnDate");
		String contractEndDate = MapUtils.getString(params, "contractEndDate");
		List<Map<String, Object>> docdetailList = null;
		String suppliercname = suppliermaster.getSuppliercname();
		String suppliercode = suppliermaster.getSuppliercode();
		String deptno = MapUtils.getString(params, "section");
		String contractYear = MapUtils.getString(params, "year");
		String contractName = MapUtils.getString(params, "contractName");
		String datatype = "基本資料";
		
		// 撈缺貨罰款資料
		Map<String, Object> shortagepenaltyMap = new HashMap<String, Object>();
		shortagepenaltyMap.put("SUPPLIERCODE", suppliercode);
		shortagepenaltyMap.put("DEPTNO", deptno);
		List<String> shortagepenaltyList = null;
		Map<String, Object> shortagepenaltyResultMap = selectShortagepenalty(shortagepenaltyMap).getResult();
		logger.info("shortagepenaltyResultMap == " + shortagepenaltyResultMap);

		List<Map<String, Object>> dataList = (List<Map<String, Object>>) paramsData.get("data");
		for (Map<String, Object> dataMap : dataList) {
			if (MapUtils.getString(dataMap, "datatype").indexOf(datatype)>-1) {
				docdetailList = (List<Map<String, Object>>) dataMap.get("docdetail");
				if(StringUtils.equals("PT", templateType) || (StringUtils.equals("LT", templateType) && StringUtils.equals("Y", isUseLastCon))) {
					int docdetailSize = docdetailList.size();
					docdetailList.remove(docdetailSize - 1);
				}
			}
			
		}

		jsonMap.put("segmentation", "");
		jsonMap.put("data", "");
		// 組貨罰款資料至resultdata
		shortagepenaltyList = (List<String>) shortagepenaltyResultMap.get("dataList");
		if(shortagepenaltyList.size() > 0) {
			for(int i = 0 ; i < shortagepenaltyList.size() ; i ++) {
				if(i != shortagepenaltyList.size() - 1) {
					resultdataMap.put("Shortagepenalty_" + i, shortagepenaltyList.get(i));
				}
				else {
					//六階處理
					int j = i;
					for(int k = 0; k < 6 - i; k++){
						resultdataMap.put("Shortagepenalty_" + (i + k), "X|X|X");
						j = i + k + 1;
					}
					resultdataMap.put("Shortagepenalty_" + j, shortagepenaltyList.get(i));
				}
			}
		}else {
			for(int i = 0 ; i < 7 ; i ++) {
				if(i == 6) {
					resultdataMap.put("Shortagepenalty_" + i, "");
				}else {
					resultdataMap.put("Shortagepenalty_" + i, "||");
				}
			}
		}
		resultdataMap.put(LocaleMessage.getMsg("contract.field.no"), MapUtils.getString(params, "contractNo"));
		resultdataMap.put(LocaleMessage.getMsg("contract.field.name"), contractName);
		resultdataMap.put(LocaleMessage.getMsg("contract.field.year"), contractYear);
		resultdataMap.put(LocaleMessage.getMsg("contract.field.startdate"), contractBgnDate);
		resultdataMap.put(LocaleMessage.getMsg("contract.field.enddate"), contractEndDate);
		resultdataMap.put(LocaleMessage.getMsg("contract.field.module"), MapUtils.getString(params, "moduleName"));
		resultdataMap.put(LocaleMessage.getMsg("contract.field.dispname"), MapUtils.getString(params, "dispName"));
		//合約金額千分位
		resultdataMap.put(LocaleMessage.getMsg("contract.field.amount"), MapUtils.getString(params, "displatcontractAmount"));
		resultdataMap.put(LocaleMessage.getMsg("xauth.field.iden"), MapUtils.getString(params, "idenId"));//MapUtils.getString(params, "depName")
//		resultdataMap.put(LocaleMessage.getMsg("contract.field.contractor"), MapUtils.getString(params, "userCname"));
		resultdataMap.put(LocaleMessage.getMsg("contract.field.updtime"), MapUtils.getString(params, "creDateDesc"));
		resultdataMap.put(LocaleMessage.getMsg("contract.field.send.date"), "");
		resultdataMap.put(LocaleMessage.getMsg("contract.field.contractor"), MapUtils.getString(params, "userCname"));
		resultdataMap.put(LocaleMessage.getMsg("contract.field.section"), deptno);
		resultdataMap.put(LocaleMessage.getMsg("contract.field.flowversion"), MapUtils.getString(params, "flowversion"));
		resultdataMap.put(LocaleMessage.getMsg("contract.field.version"), MapUtils.getString(params, "version"));
		resultdataMap.put(LocaleMessage.getMsg("contract.field.type"), MapUtils.getString(params, "dispName"));
		resultdataMap.put(LocaleMessage.getMsg("contract.field.templateyear"), templateYear);
		resultdataMap.put(LocaleMessage.getMsg("contract.field.check_static"), "");
		resultdataMap.put(LocaleMessage.getMsg("contract.field.check_result"), "");
		resultdataMap.put(LocaleMessage.getMsg("contract.field.check_result_text"), "");
		resultdataMap.put("是否放行", "");
		// 供應商資料
		resultdataMap.put(LocaleMessage.getMsg("suppliter.field.supplierid"), suppliermaster.getSupplierid());  // 供應商代碼
		resultdataMap.put(LocaleMessage.getMsg("suppliter.field.suppliercode"), suppliercode);  // 供應商廠編
		resultdataMap.put(LocaleMessage.getMsg("suppliter.field.suppliergui"), suppliermaster.getSuppliergui());  // 供應商統編
		resultdataMap.put(LocaleMessage.getMsg("suppliter.field.suppliercname"), suppliercname);  // 供應商中文名稱
		resultdataMap.put(LocaleMessage.getMsg("suppliter.field.supplierename"), StringUtils.isBlank(suppliermaster.getSupplierename()) ? "" : suppliermaster.getSupplierename());  // 供應商英文名稱
		resultdataMap.put(LocaleMessage.getMsg("suppliter.field.picuser"), suppliermaster.getPicuser());  // 供應商法定代理人
		resultdataMap.put(LocaleMessage.getMsg("suppliter.field.suppliercaddr"), suppliermaster.getSuppliercaddr());  // 供應商中文地址
		resultdataMap.put(LocaleMessage.getMsg("suppliter.field.suppliereaddr"), StringUtils.isBlank(suppliermaster.getSuppliereaddr()) ? "" : suppliermaster.getSuppliereaddr());  // 供應商英文地址
		resultdataMap.put(LocaleMessage.getMsg("suppliter.field.supplieretel"), StringUtils.isBlank(suppliermaster.getSupplieretel()) ? "" : suppliermaster.getSupplieretel());  // 供應商電話
		resultdataMap.put(LocaleMessage.getMsg("suppliter.field.contacruser"), suppliermaster.getContacruser());  // 聯絡人
		resultdataMap.put(LocaleMessage.getMsg("suppliter.field.supplieremail"), suppliermaster.getSupplieremail());  // 聯絡人Email
//		resultdataMap.put(LocaleMessage.getMsg("suppliter.field.fax"), "");
		resultdataMap.put(LocaleMessage.getMsg("contract.field.undertakeuserid"), "");	//承接人
		resultdataMap.put(LocaleMessage.getMsg("contract.field.undertakedept"), "");	//承接部門

		jsonMap.put("resultdata", resultdataMap);

		// 將新增頁面json塞入資料庫取得的json
		docdetailList.add(jsonMap);
		String resultJson = JsonUtils.obj2json(paramsData);
		return resultJson;
	}
	
	public String setDefaultValue(String json) {
		json = JsonUtil.setDefaultValue(json);
		return json;
	}
	
	/**
	 * 將合約編號新增資料至CONTRACTMASTER table
	 * 
	 * @param params
	 */
	public void insertContractmaster(Map<String, Object> params, String json, String indexName) throws Exception {
		Date ts = new Timestamp(new Date().getTime());
		Contractmaster contractmaster = new Contractmaster();
		contractmaster.setContractmodel(MapUtils.getString(params, "module"));
		contractmaster.setDataid(MapUtils.getString(params, "contractNo"));
		contractmaster.setFlowid(MapUtils.getString(params, "flowid"));
		contractmaster.setCreateuser(getCreOrUpdUser(null));
		contractmaster.setCreatedate(ts);
		contractmaster.setJson(json);
		contractmaster.setIndexname(indexName);
		contractmasterMapper.insert(contractmaster);
	}
	
	/**
	 * 將合約編號更新資料至CONTRACTMASTER table
	 * 
	 * @param params
	 */
	public void updateContractmaster(Map<String, Object> params, String contractNo, String jsonData, String type) throws Exception {
		Contractmaster contractmaster = new Contractmaster();
		contractmaster.setJson(jsonData);
		contractmaster.setUpdatedate(new Timestamp(new Date().getTime()));
		contractmaster.setUpdateuser(getCreOrUpdUser(null));
		if(StringUtils.equals(type, "送出") || StringUtils.equals(type, "作廢")) {
			contractmaster.setSendDate(new Timestamp(new Date().getTime()));
		}

		QueryWrapper<Contractmaster> queryWrapper = new QueryWrapper<Contractmaster>();
		if(params != null) {
			queryWrapper.eq("DATAID", MapUtils.getString(params, "contractNo"));
		}else {
			queryWrapper.eq("DATAID", contractNo);
		}
		
		contractmasterMapper.update(contractmaster, queryWrapper);
	}

	/**
	 * 新增 ES 資料 公用方法
	 * @param params
	 * @param json
	 * @param spec
	 * @param contractNo
	 * @return
	 */
	public ProcessResult insertContractForES(Map<String, Object> params, String json, String spec, String contractNo, String indexName, String oldJson) {
		String ip = XauthPropUtils.getKey("es.ip");
		String port = XauthPropUtils.getKey("es.port");

		ProcessResult result = new ProcessResult();
		result.setStatus(ProcessResult.NG);

		try {
			// 檢查連線
			boolean isConnect = elasticSearchUtil.initESClinet(ip, port);
			if (isConnect) {
				// 判斷index是否存在
				if (!elasticSearchUtil.existsIndex(indexName)) {
					insertDesignermgrForES(params, ip, port, indexName, json, oldJson);
					logger.info("ES index不存在，已新增index");
					
				} else {
					// 寫入資料
					Map<String, Object> esValue = elasticSearchUtil.insertIndex(indexName, contractNo, json);
					result.addResult("id", MapUtils.getObject(esValue, "id"));
					result.addResult("type", MapUtils.getObject(esValue, "type"));
				}
			}

			// 關閉連線
			elasticSearchUtil.closeESClient();
		} catch (Exception e) {
			logger.error("insertContractmasterForES exception", e, e);
			result.addMessage(MSG_KEY.INSERT_FAIL.getMessage());
			return result;

		}
		result.setStatus(ProcessResult.OK);
		return result;
	}
	
	/**
	 * 寫入PDF參數
	 * @param jsonData
	 * @param flowId
	 * @param modelName
	 * @throws Exception
	 */
	public Map<String, Object> setPdfValue(String jsonData, String flowId, String modelName) throws Exception {
		Map<String, Object> pdfValueMap = new HashMap<String, Object>();
		ReadContext json = JsonPath.parse(jsonData);
		String chModuleNameJsonPath = "";
		String moduleYearJsonPath = "";
		String validBgnDateJsonPath = "";
		String validEndDateJsonPath = "";
		String suppCodeJsonPath = "";
		String taxNumJsonPath = "";
		String exeDateJsonPath="";
		String suppNameJsonPath = "";
		if(StringUtils.equals(modelName, "制式合約")) {
			 chModuleNameJsonPath = "$.data[?(@.datatype == '基本資料')].docdetail[?(@.resultdata)].resultdata.合約類型";
			 moduleYearJsonPath = "$.data[?(@.datatype == '基本資料')].docdetail[?(@.resultdata)].resultdata.合約年度";
			 validBgnDateJsonPath = "$.data[?(@.datatype == '基本資料')].docdetail[?(@.resultdata)].resultdata.合約期間起";
			 validEndDateJsonPath = "$.data[?(@.datatype == '基本資料')].docdetail[?(@.resultdata)].resultdata.合約期間迄";
			 suppCodeJsonPath = "$.data[?(@.datatype == '基本資料')].docdetail[?(@.resultdata)].resultdata.供應商廠編";
			 taxNumJsonPath = "$.data[?(@.datatype == '基本資料')].docdetail[?(@.resultdata)].resultdata.供應商統編";
			 exeDateJsonPath = "$.data[?(@.datatype == '基本資料')].docdetail[?(@.resultdata)].resultdata.建立日期";
			 suppNameJsonPath = "$.data[?(@.datatype == '基本資料')].docdetail[?(@.resultdata)].resultdata.供應商中文名稱";
		}
		//非制式客製化-EN
		else {
			 chModuleNameJsonPath = "$.data[?(@.datatype == '"+LocaleMessage.getMsg("contractEn.filed.Basic_Information")+"')].docdetail[?(@.resultdata)].resultdata.合約類型";
			 moduleYearJsonPath = "$.data[?(@.datatype == '"+LocaleMessage.getMsg("contractEn.filed.Basic_Information")+"')].docdetail[?(@.resultdata)].resultdata.合約年度";
			 validBgnDateJsonPath = "$.data[?(@.datatype == '"+LocaleMessage.getMsg("contractEn.filed.Basic_Information")+"')].docdetail[?(@.resultdata)].resultdata.合約期間起";
			 validEndDateJsonPath = "$.data[?(@.datatype == '"+LocaleMessage.getMsg("contractEn.filed.Basic_Information")+"')].docdetail[?(@.resultdata)].resultdata.合約期間迄";
			 suppCodeJsonPath = "$.data[?(@.datatype == '"+LocaleMessage.getMsg("contractEn.filed.Basic_Information")+"')].docdetail[?(@.resultdata)].resultdata.供應商廠編";
			 taxNumJsonPath = "$.data[?(@.datatype == '"+LocaleMessage.getMsg("contractEn.filed.Basic_Information")+"')].docdetail[?(@.resultdata)].resultdata.供應商統編";
			 exeDateJsonPath = "$.data[?(@.datatype == '"+LocaleMessage.getMsg("contractEn.filed.Basic_Information")+"')].docdetail[?(@.resultdata)].resultdata.建立日期";
			 suppNameJsonPath = "$.data[?(@.datatype == '"+LocaleMessage.getMsg("contractEn.filed.Basic_Information")+"')].docdetail[?(@.resultdata)].resultdata.供應商中文名稱";
		}
		
		String legalRep = "";
		String enAddress = "";
		String chAddress = "";
		String phoneNum = "";
		String faxNum = "";
		String exeYear = "";
		String exeMon = "";
		String exeDay = "";
		String deptNo = "";
		String enModuleName = "";
		String division = "";
				
		// 從XAUTH_SYS_CODE取得合約英文名稱
		QueryWrapper<XauthSysCode> sysCodeWrapper = new QueryWrapper<XauthSysCode>();
		if(StringUtils.equals(modelName, "制式合約")) {
			sysCodeWrapper.eq("GP", "SC_FLOW_TEMPLATE");
		} else {
			sysCodeWrapper.eq("GP", "NSC_FLOW_TEMPLATE");
		}
		sysCodeWrapper.eq("CODE", StringUtils.substring(flowId, 0, 5));
		List<XauthSysCode> xauthSysCodeList = xauthSysCodeMapper.selectList(sysCodeWrapper);
		if(xauthSysCodeList.size() > 0) {
			enModuleName = xauthSysCodeList.get(0).getEname();
		}
		pdfValueMap.put("EN_MODULE_NAME", enModuleName);
		
		pdfValueMap.put("CH_MODULE_NAME", JsonUtil.getJsonPathVal(jsonData, chModuleNameJsonPath));
		pdfValueMap.put("MODULE_YEAR", JsonUtil.getJsonPathVal(jsonData, moduleYearJsonPath));
		
		if(StringUtils.equals(modelName, "制式合約")) {
			List<Integer> legalRepRowList = JsonUtil.getJsonPathRowVal(jsonData, "$.data[?(@.datatype == '基本資料')].docdetail[?(@.displayname=='法定代理人：')].row");
			if(legalRepRowList.size() > 0) {
				legalRep = JsonUtil.getJsonPathVal(jsonData, "$.data[?(@.datatype == '基本資料')].docdetail[?(@.row==" + legalRepRowList.get(0) + "&&@.col==2)].displayname");
			}
			
			List<Integer> enAddressRowList = JsonUtil.getJsonPathRowVal(jsonData, "$.data[?(@.datatype == '基本資料')].docdetail[?(@.displayname=='公司地址(英)：')].row");
			if(enAddressRowList.size() > 0) {
				enAddress = JsonUtil.getJsonPathVal(jsonData, "$.data[?(@.datatype == '基本資料')].docdetail[?(@.row==" + enAddressRowList.get(0) +"&&@.col==2)].displayname");
			}
			
			List<Integer> chAddressRowList = JsonUtil.getJsonPathRowVal(jsonData, "$.data[?(@.datatype == '基本資料')].docdetail[?(@.displayname=='公司地址(中)：')].row");
			if(chAddressRowList.size() > 0) {
				chAddress = JsonUtil.getJsonPathVal(jsonData, "$.data[?(@.datatype == '基本資料')].docdetail[?(@.row==" + chAddressRowList.get(0) + "&&@.col==2)].displayname");
			}
			
			List<Integer> phoneNumRowList = JsonUtil.getJsonPathRowVal(jsonData, "$.data[?(@.datatype == '基本資料')].docdetail[?(@.displayname=='TEL：')].row");
			if(phoneNumRowList.size() > 0) {
				phoneNum = JsonUtil.getJsonPathVal(jsonData, "$.data[?(@.datatype == '基本資料')].docdetail[?(@.row==" + phoneNumRowList.get(0) + "&&@.col==2)].displayname");
			}
			
			List<Integer> faxNumRowList = JsonUtil.getJsonPathRowVal(jsonData, "$.data[?(@.datatype == '基本資料')].docdetail[?(@.displayname=='FAX：')].row");
			if(faxNumRowList.size() > 0) {
				faxNum = JsonUtil.getJsonPathVal(jsonData, "$.data[?(@.datatype == '基本資料')].docdetail[?(@.row==" + faxNumRowList.get(0) + "&&@.col==2)].displayname");
			}
			
			List<Map<String, Object>> basicDataList = json.read("$.data[?(@.datatype == '基本資料')].docdetail[?(@.resultdata)].resultdata");
			if(basicDataList.size() > 0) {
				deptNo = (String) basicDataList.get(0).get("課別");
			}
//			List<Integer> suppNameRowList = JsonUtil.getJsonPathRowVal(jsonData, "$.data[?(@.datatype == '基本資料')].docdetail[?(@.displayname=='公司名稱(中)：')].row");
//			if(suppNameRowList.size() > 0) {
//				suppName = JsonUtil.getJsonPathVal(jsonData, "$.data[?(@.datatype == '基本資料')].docdetail[?(@.displayname=='公司名稱(中)：'&&@.row==" + suppNameRowList.get(1) + ")].value");
//			}
		}
		//非制式客製化-EN pdfValueMap
		else {
			List<Integer> legalRepRowList = JsonUtil.getJsonPathRowVal(jsonData, "$.data[?(@.datatype == '"+LocaleMessage.getMsg("contractEn.filed.Basic_Information")+"')].docdetail[?(@.displayname=='"+LocaleMessage.getMsg("contractEn.filed.Representative")+"：')].row");
			if(legalRepRowList.size() > 0) {
				legalRep = JsonUtil.getJsonPathVal(jsonData, "$.data[?(@.datatype == '"+LocaleMessage.getMsg("contractEn.filed.Basic_Information")+"')].docdetail[?(@.row==" + legalRepRowList.get(0) + "&&@.col==2)].displayname");
			}
			
			List<Integer> enAddressRowList = JsonUtil.getJsonPathRowVal(jsonData, "$.data[?(@.datatype == '"+LocaleMessage.getMsg("contractEn.filed.Basic_Information")+"')].docdetail[?(@.displayname=='"+LocaleMessage.getMsg("contractEn.filed.CompanyAddressEn")+"：')].row");
			if(enAddressRowList.size() > 0) {
				enAddress = JsonUtil.getJsonPathVal(jsonData, "$.data[?(@.datatype == '"+LocaleMessage.getMsg("contractEn.filed.Basic_Information")+"')].docdetail[?(@.row==" + enAddressRowList.get(0) +"&&@.col==2)].displayname");
			}
			
			List<Integer> chAddressRowList = JsonUtil.getJsonPathRowVal(jsonData, "$.data[?(@.datatype == '"+LocaleMessage.getMsg("contractEn.filed.Basic_Information")+"')].docdetail[?(@.displayname=='"+LocaleMessage.getMsg("contractEn.filed.CompanyAddressCn")+"：')].row");
			if(chAddressRowList.size() > 0) {
				chAddress = JsonUtil.getJsonPathVal(jsonData, "$.data[?(@.datatype == '"+LocaleMessage.getMsg("contractEn.filed.Basic_Information")+"')].docdetail[?(@.row==" + chAddressRowList.get(0) + "&&@.col==2)].displayname");
			}
			
			List<Integer> phoneNumRowList = JsonUtil.getJsonPathRowVal(jsonData, "$.data[?(@.datatype == '"+LocaleMessage.getMsg("contractEn.filed.Basic_Information")+"')].docdetail[?(@.displayname=='TEL：')].row");
			if(phoneNumRowList.size() > 0) {
				phoneNum = JsonUtil.getJsonPathVal(jsonData, "$.data[?(@.datatype == '"+LocaleMessage.getMsg("contractEn.filed.Basic_Information")+"')].docdetail[?(@.row==" + phoneNumRowList.get(0) + "&&@.col==2)].displayname");
			}
			
			List<Integer> faxNumRowList = JsonUtil.getJsonPathRowVal(jsonData, "$.data[?(@.datatype == '"+LocaleMessage.getMsg("contractEn.filed.Basic_Information")+"')].docdetail[?(@.displayname=='FAX：')].row");
			if(faxNumRowList.size() > 0) {
				faxNum = JsonUtil.getJsonPathVal(jsonData, "$.data[?(@.datatype == '"+LocaleMessage.getMsg("contractEn.filed.Basic_Information")+"')].docdetail[?(@.row==" + faxNumRowList.get(0) + "&&@.col==2)].displayname");
			}
			
			List<Map<String, Object>> basicDataList = json.read("$.data[?(@.datatype == '"+LocaleMessage.getMsg("contractEn.filed.Basic_Information")+"')].docdetail[?(@.resultdata)].resultdata");
			if(basicDataList.size() > 0) {
				deptNo = (String) basicDataList.get(0).get("課別");
			}
//			List<Integer> suppNameRowList = JsonUtil.getJsonPathRowVal(jsonData, "$.data[?(@.datatype == '"+LocaleMessage.getMsg("contractEn.filed.Basic_Information")+"')].docdetail[?(@.displayname=='"+LocaleMessage.getMsg("contractEn.filed.CompanyNameCh")+"：')].row");
//			if(suppNameRowList.size() > 0) {
//				suppName = JsonUtil.getJsonPathVal(jsonData, "$.data[?(@.datatype == '"+LocaleMessage.getMsg("contractEn.filed.Basic_Information")+"')].docdetail[?(@.displayname=='"+LocaleMessage.getMsg("contractEn.filed.CompanyNameCh")+"：'&&@.row==" + suppNameRowList.get(1) + ")].value");
//			}
			
		}
		
		pdfValueMap.put("LEGAL_REP", legalRep);
		pdfValueMap.put("EN_ADDRESS", enAddress);
		pdfValueMap.put("CH_ADDRESS", chAddress);
		pdfValueMap.put("PHONE_NUM", phoneNum);
		pdfValueMap.put("PHONE_NUM", phoneNum);
		pdfValueMap.put("DEPT_NO", deptNo);
		pdfValueMap.put("FAX_NUM", faxNum);
		pdfValueMap.put("VALID_BGN_DATE", JsonUtil.getJsonPathVal(jsonData, validBgnDateJsonPath));
		pdfValueMap.put("VALID_END_DATE", JsonUtil.getJsonPathVal(jsonData, validEndDateJsonPath));
		
		// 取得處別名稱
		XauthSysCode xauthSysCode = commonService.getSysCodeData("DEPT_CODE", deptNo);
		
		if(xauthSysCode != null) {
			division = xauthSysCode.getMemo();

		}
		pdfValueMap.put("DIVISION", division);
		pdfValueMap.put("SUPP_NAME", JsonUtil.getJsonPathVal(jsonData, suppNameJsonPath));
		pdfValueMap.put("SUPP_CODE", JsonUtil.getJsonPathVal(jsonData, suppCodeJsonPath));
		pdfValueMap.put("TAX_NUM", JsonUtil.getJsonPathVal(jsonData, taxNumJsonPath));
		String exeDate = JsonUtil.getJsonPathVal(jsonData, exeDateJsonPath);   // "2020/08/11 09:42:25"
		if(StringUtils.isNotBlank(exeDate)) {
			exeYear = exeDate.substring(0, 4);
			exeMon = exeDate.substring(5, 7);
			exeDay = exeDate.substring(8, 10);
		}
		pdfValueMap.put("EXE_YEAR", exeYear);
		pdfValueMap.put("EXE_MON", exeMon);
		pdfValueMap.put("EXE_DAY", exeDay);
		
		return pdfValueMap;
	}
	
	/**
	 * 新增檔案上傳資料Table
	 * @param contractNo
	 * @param filePath
	 * @param fileName
	 * @param modelName
	 * @throws Exception
	 */
	public void insertToContractfile(String contractNo, String filePath, String fileName, String modelName) throws Exception {
		
		QueryWrapper<Contractfile> contractNewfileQw = new QueryWrapper<Contractfile>();
		contractNewfileQw.eq("DATAID", contractNo);
		contractNewfileQw.eq("TYPE", "02");
		contractNewfileQw.eq("FILETYPE", "合約主檔");
		List<Contractfile> contractNewfileList = contractfileMapper.selectList(contractNewfileQw);
		Contractfile newContractfile = null;
		Date ts = new Timestamp(new Date().getTime());
		
		if(contractNewfileList.size() > 0) {
			newContractfile = contractNewfileList.get(0);
			newContractfile.setDataid(contractNo);
			newContractfile.setFliepath(filePath);
			newContractfile.setFliename(fileName);
//			newContractfile.setIsdownload(contractfile.getIsdownload());
			newContractfile.setIsdownload("是");
			newContractfile.setFlienote("");
			newContractfile.setType("02");
			newContractfile.setFixid("");
			newContractfile.setFiletype("合約主檔");
			newContractfile.setUpdateuser(getCreOrUpdUser(null));
			newContractfile.setUpdatedate(ts);
			contractfileMapper.update(newContractfile, contractNewfileQw);
		} else {
			newContractfile = new Contractfile();
			newContractfile.setDataid(contractNo);
			newContractfile.setFliepath(filePath);
			newContractfile.setFliename(fileName);
//			newContractfile.setIsdownload(contractfile.getIsdownload());
			newContractfile.setIsdownload("是");
			newContractfile.setFlienote("");
			newContractfile.setType("02");
			newContractfile.setFixid("");
			newContractfile.setFiletype("合約主檔");
			newContractfile.setCreateuser(getCreOrUpdUser(null));
			newContractfile.setCreatedate(ts);
			newContractfile.setUpdateuser(getCreOrUpdUser(null));
			newContractfile.setUpdatedate(ts);
			
			contractfileMapper.insert(newContractfile);
		}
	}
	
	/**
	 * 從承辦人員填寫供應商資訊，更新至DB
	 * @param jsonData
	 * @throws Exception
	 */
public void updateSuppDataToDb(String jsonData, String supplierId) throws Exception {
		
		String deptno = "";             // 課別
		String suppliercode = "";		// 供應商廠編
		String suppliergui = "";		// 統一編號
		String suppliercname = "";		// 公司名稱(中)
		String supplierename = "";		// 公司名稱(英)
		String picuser = "";			// 法定代理人
		String suppliercaddr = "";		// 公司地址(中)
		String suppliereaddr = "";		// 公司地址(英)
		String suppliertel = "";		// TEL
		String supplieremail="";		//mail
		String contacruser="";			//聯絡人
		
		Suppliermaster suppliermaster = null;
		Date now = new Timestamp(new Date().getTime());
		
		ReadContext json = JsonPath.parse(jsonData);
		//非制式客製化-EN  採中英文搜尋若為0採取原始取得方式
		List<Map<String, Object>> basicDataList = json.read("$.data[?(@.datatype == '"+LocaleMessage.getMsg("contractEn.filed.Basic_Information")+"')].docdetail[?(@.resultdata)].resultdata");
		if(basicDataList.size()<1) {
			basicDataList = json.read("$.data[?(@.datatype == '基本資料')].docdetail[?(@.resultdata)].resultdata");
			suppliercode = JsonUtil.getJsonPathVal(jsonData, "$.data[?(@.datatype == '基本資料')].docdetail[?(@.resultdata)].resultdata.供應商廠編");
			suppliergui = JsonUtil.getJsonPathVal(jsonData, "$.data[?(@.datatype == '基本資料')].docdetail[?(@.displayname=='統一編號：')].value");
			
			List<Integer> suppliercnameList = JsonUtil.getJsonPathRowVal(jsonData, "$.data[?(@.datatype == '基本資料')].docdetail[?(@.displayname=='公司名稱(中)：')].row");
			if(suppliercnameList.size() > 0) {
				suppliercname = JsonUtil.getJsonPathVal(jsonData, "$.data[?(@.datatype == '基本資料')].docdetail[?(@.displayname=='公司名稱(中)：'&&@.row==" + suppliercnameList.get(1) + ")].value");
			}
			
			List<Integer> supplierenameList = JsonUtil.getJsonPathRowVal(jsonData, "$.data[?(@.datatype == '基本資料')].docdetail[?(@.displayname=='公司名稱(英)：')].row");
			if(suppliercnameList.size() > 0) {
				supplierename = JsonUtil.getJsonPathVal(jsonData, "$.data[?(@.datatype == '基本資料')].docdetail[?(@.displayname=='公司名稱(英)：'&&@.row==" + supplierenameList.get(1) + ")].value");
			}
			
			List<Integer> picuserList = JsonUtil.getJsonPathRowVal(jsonData, "$.data[?(@.datatype == '基本資料')].docdetail[?(@.displayname=='法定代理人：')].row");
			if(picuserList.size() > 0) {
				picuser = JsonUtil.getJsonPathVal(jsonData, "$.data[?(@.datatype == '基本資料')].docdetail[?(@.displayname=='法定代理人：'&&@.row==" + picuserList.get(1) + ")].value");
			}
			
			List<Integer> sippliercaddrList = JsonUtil.getJsonPathRowVal(jsonData, "$.data[?(@.datatype == '基本資料')].docdetail[?(@.displayname=='公司地址(中)：')].row");
			if(sippliercaddrList.size() > 0) {
				suppliercaddr = JsonUtil.getJsonPathVal(jsonData, "$.data[?(@.datatype == '基本資料')].docdetail[?(@.displayname=='公司地址(中)：'&&@.row==" + sippliercaddrList.get(1) + ")].value");
			}
			
			List<Integer> suppliereaddrList = JsonUtil.getJsonPathRowVal(jsonData, "$.data[?(@.datatype == '基本資料')].docdetail[?(@.displayname=='公司地址(英)：')].row");
			if(suppliereaddrList.size() > 0) {
				suppliereaddr = JsonUtil.getJsonPathVal(jsonData, "$.data[?(@.datatype == '基本資料')].docdetail[?(@.displayname=='公司地址(英)：'&&@.row==" + suppliereaddrList.get(1) + ")].value");
			}
			List<Integer> suppliertelList = JsonUtil.getJsonPathRowVal(jsonData, "$.data[?(@.datatype == '基本資料')].docdetail[?(@.displayname=='TEL：')].row");
			if(suppliertelList.size() > 0) {
				suppliertel = JsonUtil.getJsonPathVal(jsonData, "$.data[?(@.datatype == '基本資料')].docdetail[?(@.displayname=='TEL：'&&@.row==" + suppliertelList.get(0) + ")].value");
			}
			//mail
			List<Integer> suppliermailList = JsonUtil.getJsonPathRowVal(jsonData, "$.data[?(@.datatype == '基本資料')].docdetail[?(@.displayname=='聯絡人電子郵件：')].row");
			if(suppliermailList.size() > 0) {
				supplieremail = JsonUtil.getJsonPathVal(jsonData, "$.data[?(@.datatype == '基本資料')].docdetail[?(@.displayname=='聯絡人電子郵件：'&&@.row==" + suppliermailList.get(0) + ")].value");
			}
			//聯絡人
			List<Integer> supplierContacruserList = JsonUtil.getJsonPathRowVal(jsonData, "$.data[?(@.datatype == '基本資料')].docdetail[?(@.displayname=='聯絡人：')].row");
			if(supplierContacruserList.size() > 0) {
				contacruser = JsonUtil.getJsonPathVal(jsonData, "$.data[?(@.datatype == '基本資料')].docdetail[?(@.displayname=='聯絡人：'&&@.row==" + supplierContacruserList.get(0) + ")].value");
			}

		}
		//非制式客製化-EN  取值
		else  {
			suppliercode = JsonUtil.getJsonPathVal(jsonData, "$.data[?(@.datatype == '"+LocaleMessage.getMsg("contractEn.filed.Basic_Information")+"')].docdetail[?(@.resultdata)].resultdata.供應商廠編");
			suppliergui = JsonUtil.getJsonPathVal(jsonData, "$.data[?(@.datatype == '"+LocaleMessage.getMsg("contractEn.filed.Basic_Information")+"')].docdetail[?(@.resultdata)].resultdata.供應商統編");

			List<Integer> suppliercnameList = JsonUtil.getJsonPathRowVal(jsonData, "$.data[?(@.datatype == '"+LocaleMessage.getMsg("contractEn.filed.Basic_Information")+"')].docdetail[?(@.displayname=='"+LocaleMessage.getMsg("contractEn.filed.CompanyNameCh")+"：')].row");
			if(suppliercnameList.size() > 0) {
				suppliercname = JsonUtil.getJsonPathVal(jsonData, "$.data[?(@.datatype == '"+LocaleMessage.getMsg("contractEn.filed.Basic_Information")+"')].docdetail[?(@.displayname=='"+LocaleMessage.getMsg("contractEn.filed.CompanyNameCh")+"：'&&@.row==" + suppliercnameList.get(1) + ")].value");
			}
			
			List<Integer> supplierenameList = JsonUtil.getJsonPathRowVal(jsonData, "$.data[?(@.datatype == '"+LocaleMessage.getMsg("contractEn.filed.Basic_Information")+"')].docdetail[?(@.displayname=='"+LocaleMessage.getMsg("contractEn.filed.CompanyNameEn")+"：')].row");
			if(suppliercnameList.size() > 0) {
				supplierename = JsonUtil.getJsonPathVal(jsonData, "$.data[?(@.datatype == '"+LocaleMessage.getMsg("contractEn.filed.Basic_Information")+"')].docdetail[?(@.displayname=='"+LocaleMessage.getMsg("contractEn.filed.CompanyNameEn")+"：'&&@.row==" + supplierenameList.get(1) + ")].value");
			}
			
			List<Integer> picuserList = JsonUtil.getJsonPathRowVal(jsonData, "$.data[?(@.datatype == '"+LocaleMessage.getMsg("contractEn.filed.Basic_Information")+"')].docdetail[?(@.displayname=='"+LocaleMessage.getMsg("contractEn.filed.Representative")+"：')].row");
			if(picuserList.size() > 0) {
				picuser = JsonUtil.getJsonPathVal(jsonData, "$.data[?(@.datatype == '"+LocaleMessage.getMsg("contractEn.filed.Basic_Information")+"')].docdetail[?(@.displayname=='"+LocaleMessage.getMsg("contractEn.filed.Representative")+"：'&&@.row==" + picuserList.get(1) + ")].value");
			}
			
			List<Integer> sippliercaddrList = JsonUtil.getJsonPathRowVal(jsonData, "$.data[?(@.datatype == '"+LocaleMessage.getMsg("contractEn.filed.Basic_Information")+"')].docdetail[?(@.displayname=='"+LocaleMessage.getMsg("contractEn.filed.CompanyAddressCn")+"：')].row");
			if(sippliercaddrList.size() > 0) {
				suppliercaddr = JsonUtil.getJsonPathVal(jsonData, "$.data[?(@.datatype == '"+LocaleMessage.getMsg("contractEn.filed.Basic_Information")+"')].docdetail[?(@.displayname=='"+LocaleMessage.getMsg("contractEn.filed.CompanyAddressCn")+"：'&&@.row==" + sippliercaddrList.get(1) + ")].value");
			}
			
			List<Integer> suppliereaddrList = JsonUtil.getJsonPathRowVal(jsonData, "$.data[?(@.datatype == '"+LocaleMessage.getMsg("contractEn.filed.Basic_Information")+"')].docdetail[?(@.displayname=='"+LocaleMessage.getMsg("contractEn.filed.CompanyAddressEn")+"：')].row");
			if(suppliereaddrList.size() > 0) {
				suppliereaddr = JsonUtil.getJsonPathVal(jsonData, "$.data[?(@.datatype == '"+LocaleMessage.getMsg("contractEn.filed.Basic_Information")+"')].docdetail[?(@.displayname=='"+LocaleMessage.getMsg("contractEn.filed.CompanyAddressEn")+"：'&&@.row==" + suppliereaddrList.get(1) + ")].value");
			}	
			List<Integer> suppliertelList = JsonUtil.getJsonPathRowVal(jsonData, "$.data[?(@.datatype == '"+LocaleMessage.getMsg("contractEn.filed.Basic_Information")+"')].docdetail[?(@.displayname=='TEL：')].row");
			if(suppliertelList.size() > 0) {
				suppliertel = JsonUtil.getJsonPathVal(jsonData, "$.data[?(@.datatype == '"+LocaleMessage.getMsg("contractEn.filed.Basic_Information")+"')].docdetail[?(@.displayname=='TEL：'&&@.row==" + suppliertelList.get(0) + ")].value");
			}
			//mail
			List<Integer> suppliermailList = JsonUtil.getJsonPathRowVal(jsonData, "$.data[?(@.datatype == '"+LocaleMessage.getMsg("contractEn.filed.Basic_Information")+"')].docdetail[?(@.displayname=='"+LocaleMessage.getMsg("contractEn.filed.Email")+"：')].row");
			if(suppliermailList.size() > 0) {
				supplieremail = JsonUtil.getJsonPathVal(jsonData, "$.data[?(@.datatype == '"+LocaleMessage.getMsg("contractEn.filed.Basic_Information")+"')].docdetail[?(@.displayname=='"+LocaleMessage.getMsg("contractEn.filed.Email")+"：'&&@.row==" + suppliermailList.get(0) + ")].value");
			}
			//聯絡人
			List<Integer> supplierContacruserList = JsonUtil.getJsonPathRowVal(jsonData, "$.data[?(@.datatype == '"+LocaleMessage.getMsg("contractEn.filed.Basic_Information")+"')].docdetail[?(@.displayname=='"+LocaleMessage.getMsg("contractEn.filed.ContactPerson")+"：')].row");
			if(supplierContacruserList.size() > 0) {
				contacruser = JsonUtil.getJsonPathVal(jsonData, "$.data[?(@.datatype == '"+LocaleMessage.getMsg("contractEn.filed.Basic_Information")+"')].docdetail[?(@.displayname=='"+LocaleMessage.getMsg("contractEn.filed.ContactPerson")+"：'&&@.row==" + supplierContacruserList.get(0) + ")].value");
			}
		}
	
		deptno = (String) basicDataList.get(0).get("課別");
		// 查詢DB SUPPLIERMASTER
		QueryWrapper<Suppliermaster> queryWrapper = new QueryWrapper<Suppliermaster>();
		queryWrapper.eq("SUPPLIERID", supplierId);
		if(StringUtils.isNotBlank(deptno)) {
			queryWrapper.eq("DEPTNO", deptno);
		} else {
			queryWrapper.isNull("DEPTNO");
		}
		List<Suppliermaster> suppliermasterList = suppliermasterMapper.selectList(queryWrapper);
		// 若欄位是空的將值塞入DB
		if(suppliermasterList.size() > 0) {
			suppliermaster = suppliermasterList.get(0);
			if(StringUtils.isBlank(suppliermaster.getSuppliergui())) {
				suppliermaster.setSuppliergui(suppliergui);
			}
			if(StringUtils.isBlank(suppliermaster.getSuppliercname())) {
				suppliermaster.setSuppliercname(suppliercname);
			}
			if(StringUtils.isBlank(suppliermaster.getSupplierename())) {
				suppliermaster.setSupplierename(supplierename);
			}
			if(StringUtils.isBlank(suppliermaster.getPicuser())) {
				suppliermaster.setPicuser(picuser);
			}
			if(StringUtils.isBlank(suppliermaster.getSuppliercaddr())) {
				suppliermaster.setSuppliercaddr(suppliercaddr);
			}
			if(StringUtils.isBlank(suppliermaster.getSuppliereaddr())) {
				suppliermaster.setSuppliereaddr(suppliereaddr);
			}
			if(StringUtils.isBlank(suppliermaster.getSupplieretel())) {
				suppliermaster.setSupplieretel(suppliertel);
			}
			if(StringUtils.isBlank(suppliermaster.getSupplieremail())) {
				suppliermaster.setSupplieremail(supplieremail);
			}
			if(StringUtils.isBlank(suppliermaster.getContacruser())) {
				suppliermaster.setContacruser(contacruser);
			}
			suppliermaster.setUpdateuser(getCreOrUpdUser(null));
			suppliermaster.setUpdatedate(now);
			suppliermasterMapper.update(suppliermaster, queryWrapper);
		}
		
	}
	
	/**
	* 將審核評估毛利資料塞入json
	* @param jsonData
	* @param section
	* @param suppliercode
	*/
	public String setNegoentryVal(String jsonData, String section, String suppliercode)  throws Exception {
	
		Date now = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
		String nowYear = sdf.format(now);
		List<Double> yearList = rowListDouble(jsonData, "合約年度");
		List<Double> flowList = rowListDouble(jsonData, "Flow");
		List<Double> hypCostList = rowListDouble(jsonData, "HYP_Cost");
		List<Double> supCostList = rowListDouble(jsonData, "SUP_Cost");
		List<Double> kmCostList = rowListDouble(jsonData, "KM_Cost");
		List<Double> natCostList = rowListDouble(jsonData, "NAT_Cost");
		List<Double> hypMarginList = rowListDouble(jsonData, "HYP_Margin");
		List<Double> supMarginList = rowListDouble(jsonData, "SUP_Margin");
		List<Double> kmMarginList = rowListDouble(jsonData, "KM_Margin");
		List<Double> natMarginList = rowListDouble(jsonData, "NAT_Margin");
		List<Double> hypMarginActualList = rowListDouble(jsonData, "HYP_Margin_Actual");
		List<Double> supMarginActualList = rowListDouble(jsonData, "SUP_Margin_Actual");
		List<Double> kmMarginActualList = rowListDouble(jsonData, "KM_Margin_Actual");
		List<Double> natMarginActualList = rowListDouble(jsonData, "NAT_Margin_Actual");
		List<Double> extraList = rowListDouble(jsonData, "Extra白單");
		List<Double> extraPcList = rowListDouble(jsonData, "Extra白單%");
		List<Double> extraActualList = rowListDouble(jsonData, "Extra白單%Actual");
		String result = "";
		DocumentContext documentContext = JsonPath.parse(jsonData);
		
		// 查詢TABLE NEGOENTRY的資料內容
		QueryWrapper<Negoentry> queryWrapper = new QueryWrapper<Negoentry>();
		queryWrapper.eq("DEPTNO", section);
		queryWrapper.eq("SUPPLIERCODE", suppliercode);
		List<Negoentry> negoentryList = negoentryMapper.selectList(queryWrapper);
		if(negoentryList.size() > 0) {
			for(Negoentry negoentry : negoentryList) {
				// 前年資料塞入json
				if(StringUtils.equals(negoentry.getYears(), String.valueOf((Integer.valueOf(nowYear) - 2)))) {
					if(yearList.size() > 0) {
						documentContext.set(JsonPath.compile(jsonPath("合約年度",  yearList.get(0))), negoentry.getYears());
					}
					if(flowList.size() > 0) {
						documentContext.set(JsonPath.compile(jsonPath("Flow", flowList.get(0))), negoentry.getFlow() != null ?  negoentry.getFlow() : "");
					}
					if(hypCostList.size() > 0) {
						documentContext.set(JsonPath.compile(jsonPath("HYP_Cost", hypCostList.get(0))), negoentry.getHypcost() != null ?  negoentry.getHypcost() : "");
					}
					if(supCostList.size() > 0) {
						documentContext.set(JsonPath.compile(jsonPath("SUP_Cost", supCostList.get(0))), negoentry.getSupcost() != null ?  negoentry.getSupcost() : "");
					}
					if(kmCostList.size() > 0) {
						documentContext.set(JsonPath.compile(jsonPath("KM_Cost", kmCostList.get(0))), negoentry.getKmcost() != null ?  negoentry.getKmcost() : "");
					}
					if(natCostList.size() > 0) {
						documentContext.set(JsonPath.compile(jsonPath("NAT_Cost", natCostList.get(0))), negoentry.getNatcost() != null ?  negoentry.getNatcost() : "");
					}
					if(hypMarginList.size() > 0) {
						documentContext.set(JsonPath.compile(jsonPath("HYP_Margin", hypMarginList.get(0))), negoentry.getHypmargin() != null ?  negoentry.getHypmargin() : "");
					}
					if(supMarginList.size() > 0) {
						documentContext.set(JsonPath.compile(jsonPath("SUP_Margin", supMarginList.get(0))), negoentry.getSupmargin() != null ?  negoentry.getSupmargin() : "");
					}
					if(kmMarginList.size() > 0) {
						documentContext.set(JsonPath.compile(jsonPath("KM_Margin", kmMarginList.get(0))), negoentry.getKmmargin() != null ?  negoentry.getKmmargin() : "");
					}
					if(natMarginList.size() > 0) {
						documentContext.set(JsonPath.compile(jsonPath("NAT_Margin", natMarginList.get(0))), negoentry.getNatmargin() != null ?  negoentry.getNatmargin() : "");
					}
					if(hypMarginActualList.size() > 0) {
						documentContext.set(JsonPath.compile(jsonPath("HYP_Margin_Actual", hypMarginActualList.get(0))), negoentry.getHypmarginactual() != null ?  negoentry.getHypmarginactual() : "");
					}
					if(supMarginActualList.size() > 0) {
						documentContext.set(JsonPath.compile(jsonPath("SUP_Margin_Actual", supMarginActualList.get(0))), negoentry.getSupmarginactual() != null ?  negoentry.getSupmarginactual() : "");
					}
					if(kmMarginActualList.size() > 0) {
						documentContext.set(JsonPath.compile(jsonPath("KM_Margin_Actual", kmMarginActualList.get(0))), negoentry.getKmmarginactual() != null ?  negoentry.getKmmarginactual() : "");
					}
					if(natMarginActualList.size() > 0) {
						documentContext.set(JsonPath.compile(jsonPath("NAT_Margin_Actual", natMarginActualList.get(0))), negoentry.getNatmarginactual() != null ?  negoentry.getNatmarginactual() : "");
					}
					if(extraList.size() > 0) {
						documentContext.set(JsonPath.compile(jsonPath("Extra白單", extraList.get(0))), negoentry.getExtra() != null ?  negoentry.getExtra() : "");
					}
					if(extraPcList.size() > 0) {
						documentContext.set(JsonPath.compile(jsonPath("Extra白單%", extraPcList.get(0))), negoentry.getExtrapc() != null ?  negoentry.getExtrapc() : "");
					}
					if(extraActualList.size() > 0) {
						documentContext.set(JsonPath.compile(jsonPath("Extra白單%Actual", extraActualList.get(0))), negoentry.getExtraactual() != null ?  negoentry.getExtraactual() : "");
					}
				} else if(StringUtils.equals(negoentry.getYears(), String.valueOf((Integer.valueOf(nowYear) - 1)))) {
					// 歷年資料塞入json
					if(yearList.size() > 0) {
						documentContext.set(JsonPath.compile(jsonPath("合約年度", yearList.get(1))), negoentry.getYears());
					}
					if(flowList.size() > 0) {
						documentContext.set(JsonPath.compile(jsonPath("Flow", flowList.get(1))), negoentry.getFlow()!= null ?  negoentry.getFlow() : "");
					}
					if(hypCostList.size() > 0) {
						documentContext.set(JsonPath.compile(jsonPath("HYP_Cost", hypCostList.get(1))), negoentry.getHypcost()!= null ?  negoentry.getHypcost() : "");
					}
					if(supCostList.size() > 0) {
						documentContext.set(JsonPath.compile(jsonPath("SUP_Cost", supCostList.get(1))), negoentry.getSupcost()!= null ?  negoentry.getSupcost() : "");
					}
					if(kmCostList.size() > 0) {
						documentContext.set(JsonPath.compile(jsonPath("KM_Cost", kmCostList.get(1))), negoentry.getKmcost()!= null ?  negoentry.getKmcost() : "");
					}
					if(natCostList.size() > 0) {
						documentContext.set(JsonPath.compile(jsonPath("NAT_Cost", natCostList.get(1))), negoentry.getNatcost()!= null ?  negoentry.getNatcost() : "");
					}
					if(hypMarginList.size() > 0) {
						documentContext.set(JsonPath.compile(jsonPath("HYP_Margin", hypMarginList.get(1))), negoentry.getHypmargin()!= null ?  negoentry.getHypmargin() : "");
					}
					if(supMarginList.size() > 0) {
						documentContext.set(JsonPath.compile(jsonPath("SUP_Margin", supMarginList.get(1))), negoentry.getSupmargin()!= null ?  negoentry.getSupmargin() : "");
					}
					if(kmMarginList.size() > 0) {
						documentContext.set(JsonPath.compile(jsonPath("KM_Margin", kmMarginList.get(1))), negoentry.getKmmargin()!= null ?  negoentry.getKmmargin() : "");
					}
					if(natMarginList.size() > 0) {
						documentContext.set(JsonPath.compile(jsonPath("NAT_Margin", natMarginList.get(1))), negoentry.getNatmargin()!= null ?  negoentry.getNatmargin() : "");
					}
					if(hypMarginActualList.size() > 0) {
						documentContext.set(JsonPath.compile(jsonPath("HYP_Margin_Actual", hypMarginActualList.get(1))), negoentry.getHypmarginactual()!= null ?  negoentry.getHypmarginactual() : "");
					}
					if(supMarginActualList.size() > 0) {
						documentContext.set(JsonPath.compile(jsonPath("SUP_Margin_Actual", supMarginActualList.get(1))), negoentry.getSupmarginactual()!= null ?  negoentry.getSupmarginactual() : "");
					}
					if(kmMarginActualList.size() > 0) {
						documentContext.set(JsonPath.compile(jsonPath("KM_Margin_Actual", kmMarginActualList.get(1))), negoentry.getKmmarginactual()!= null ?  negoentry.getKmmarginactual() : "");
					}
					if(natMarginActualList.size() > 0) {
						documentContext.set(JsonPath.compile(jsonPath("NAT_Margin_Actual", natMarginActualList.get(1))), negoentry.getNatmarginactual()!= null ?  negoentry.getNatmarginactual() : "");
					}
					if(extraList.size() > 0) {
						documentContext.set(JsonPath.compile(jsonPath("Extra白單", extraList.get(1))), negoentry.getExtra()!= null ?  negoentry.getExtra() : "");
					}
					if(extraPcList.size() > 0) {
						documentContext.set(JsonPath.compile(jsonPath("Extra白單%", extraPcList.get(1))), negoentry.getExtrapc()!= null ?  negoentry.getExtrapc() : "");
					}
					if(extraActualList.size() > 0) {
						documentContext.set(JsonPath.compile(jsonPath("Extra白單%Actual", extraActualList.get(1))), negoentry.getExtraactual()!= null ?  negoentry.getExtraactual() : "");
					}
				}
			}
		}
		result = documentContext.jsonString();
		return result;
	}
	
	/**
	 * Flow 流程後新增 Email 資訊 以便日後排成處理
	 * @param docState
	 * @return
	 * @throws Exception
	 */
	public ProcessResult insertEmailQueue(DocStateBean docState, String supplieremail, String contractname) throws Exception{
		ProcessResult result = new ProcessResult();
		StringBuffer content = null;
		Emailqueue emailqueue = new Emailqueue();
		UserInfo userInfo = userContext.getCurrentUser();
		Flowstep flowstep = null;
		XauthUsers xauthUsers = null;
		String toUserCname = "";
		String applicantCname = "";
		String email = "";
		String apiUrl = env.getProperty("spring.domain");  
		String contectPath = servletContext.getContextPath(); 
		String url = apiUrl + contectPath;  
		String roleName = "";
		Map<String ,Object> xauthRoleMap=new HashMap<String ,Object>();
		String dataid = "";

		if(docState == null) {
			result.setStatus(ProcessResult.NG);
			result.addMessage("發生錯誤-流程不得為空");
			return result;
		} else {
			dataid = docState.getApplyNo();
			xauthUsers = getUser(docState.getApplicantId());
			if(xauthUsers != null) {
				//判斷是否有合約轉移 更換其角色名
				String userid = xauthTransferService.getUndertakeId(userInfo.getUserId(),xauthUsers.getUserId(),dataid);
				if(!StringUtils.equals(docState.getApplicantId(),userid)) {
					xauthUsers = getUser(userid);
				}				
				applicantCname = xauthUsers.getUserCname();

			}else {
				result.setStatus(ProcessResult.NG);
				result.addMessage("發生錯誤-查無申請人資料");
				return result;
			}
		}
		
		if(StringUtils.equals("End", docState.getNowTaskId())) {
			result.setStatus(ProcessResult.OK);
			return result;
		}
		
		QueryWrapper<Flowstep> flowStepQueryWrapper = new QueryWrapper<Flowstep> ();
		flowStepQueryWrapper.eq("FLOWID", docState.getFlowId());
		flowStepQueryWrapper.eq("STEPID", docState.getNowTaskId());
		List<Flowstep> flowstepList = flowstepMapper.selectList(flowStepQueryWrapper);
		
		if(flowstepList.size() == 0) {
			result.setStatus(ProcessResult.NG);
			result.addMessage("發生錯誤-查無流程步驟資料");
			return result;
		}else {
			flowstep = flowstepList.get(0);
		}
		StringBuffer bf = new StringBuffer();
		//User ID
		if(StringUtils.isNotBlank(flowstep.getUserids())){
			String[] users = flowstep.getUserids().split(",");
			for(String user : users) {
				xauthUsers = getUser(user);
				if(xauthUsers != null) {
					//判斷是否有合約轉移 更換其角色名
					String userid = xauthTransferService.getUndertakeId(userInfo.getUserId(),xauthUsers.getUserId(),dataid);
					if(!StringUtils.equals(user,userid)) {
						xauthUsers = getUser(userid);
					}
					toUserCname = xauthUsers.getUserCname();
					logger.info("送信ID==="+toUserCname);

					email = StringUtils.equals("999999999", xauthUsers.getIdenId()) ? supplieremail : xauthUsers.getEmail();
					xauthRoleMap.put("user_id", user);
					List<Map<String, Object>> list =contractMapper.selectRoleAndName(xauthRoleMap);
					for(Map<String, Object> dataMap :  list) {
						roleName= MapUtils.getString(dataMap, "ROLE_CNAME");
					}
					roleName = StringUtils.equals("999999999", xauthUsers.getIdenId()) ? flowstep.getStepdesc() : roleName + " 審核";
				}else {
					result.setStatus(ProcessResult.NG);
					result.addMessage("發生錯誤-查無寄件者資料-001");
					return result;
				}
				//編輯郵件內文
				bf.append(toUserCname+" ");

				content = new StringBuffer();
				content.append("您有一份合約已經送到您手上，請儘速處理。 <BR>"); 
//				content.append("關卡名稱：" + flowstep.getStepdesc() + "<BR>"); 
				content.append("關卡名稱：" + roleName + "<BR>"); 
				content.append("合約編號: " + docState.getApplyNo() + "<BR>"); 
				content.append("合約名稱: " + contractname + "<BR>"); 
				content.append("合約建立者：" + applicantCname + "<BR>"); 
				content.append("<a href='" + url + "' target='_blank'>開啟表單</a><BR>"); 

				content.append("----------------------------------------------------------------------<BR>");
				
				content.append("The contract has been assigned to you, Please process it as soon as possible.<BR>"); 
//				content.append("Process Name：" + flowstep.getStepdesc() + "<BR>"); 
				content.append("Process Name：" + roleName + "<BR>"); 
				content.append("Contract Number：" + docState.getApplyNo() + "<BR>"); 
				content.append("Contract Name： " + contractname + "<BR>"); 
				content.append("Creator：" + applicantCname + "<BR>"); 
				content.append("<a href='" + url + "' target='_blank'>Contract Link</a>"); 
				
				
				emailqueue.setSerno(null);
				emailqueue.setSysid(APP_ID);
				emailqueue.setCategory("FLOW");
				emailqueue.setMailto(StringUtils.isBlank(email) ? ADMIN_EMAIL : email);
//				emailqueue.setSubject(SUBJECT);
				emailqueue.setSubject("[Notification] E-Contract -到關通知信 Process Arrived");
				emailqueue.setContent(content.toString());
//				emailqueue.setAttachment("測試附件");
				emailqueue.setSysmemo(docState.getApplyNo());
				emailqueue.setStatus("N");
				emailqueue.setPriority("1");
				emailqueue.setCreatetime(new Timestamp(new Date().getTime()));
				emailqueue.setCreateuserid(getCreOrUpdUser(null));
				emailqueue.setUpdatetime(new Timestamp(new Date().getTime()));
				emailqueue.setUpdateuserid(getCreOrUpdUser(null));
				
				emailqueueMapper.insert(emailqueue);
			}
		}
		//Role ID
		if(StringUtils.isNoneBlank(flowstep.getRoleids())) {
			String[] roles = flowstep.getRoleids().split(",");
			String flowDeptid =  flowstep.getDeptid();
			for(String role : roles) {
				QueryWrapper<XauthRoleUser> queryWrapper = new QueryWrapper<XauthRoleUser>();
				queryWrapper.eq("IDEN_ID", flowDeptid);
				queryWrapper.eq("ROLE_ID", role);
				List<XauthRoleUser> xauthRoleUserList = xauthRoleUserMapper.selectList(queryWrapper);
				
				QueryWrapper<XauthRole> xauthRoleQueryWrapper = new QueryWrapper<XauthRole>();
				xauthRoleQueryWrapper.eq("IDEN_ID", flowDeptid);
				xauthRoleQueryWrapper.eq("ROLE_ID", role);
				List<XauthRole> xauthRoleList = xauthRoleMapper.selectList(xauthRoleQueryWrapper);
				roleName = xauthRoleList.get(0).getRoleCname() + " 審核";
				 
				for(XauthRoleUser xauthRoleUser : xauthRoleUserList) {
					if(StringUtils.isNoneBlank(xauthRoleUser.getUserId())) {
						xauthUsers = getUser(xauthRoleUser.getUserId());
						if(xauthUsers != null) {
							//判斷是否有合約轉移
							String userid = xauthTransferService.getUndertakeId(userInfo.getUserId(),xauthUsers.getUserId(),dataid);
							if(!StringUtils.equals(xauthUsers.getUserId(),userid)) {
								xauthUsers = getUser(userid);
							}
							toUserCname = xauthUsers.getUserCname();
							email = xauthUsers.getEmail();
						}else {
							result.setStatus(ProcessResult.NG);
							result.addMessage("發生錯誤-查無寄件者資料-002");
							return result;
						}
						//編輯郵件內文
						bf.append(toUserCname+" ");
						content = new StringBuffer();
						content.append("您有一份合約已經送到您手上，請儘速處理。 <BR>"); 
//						content.append("關卡名稱：" + flowstep.getStepdesc() + "<BR>"); 
						content.append("關卡名稱：" + roleName + "<BR>"); 
						content.append("合約編號: " + docState.getApplyNo() + "<BR>"); 
						content.append("合約名稱: " + contractname + "<BR>"); 
						content.append("合約建立者：" + applicantCname + "<BR>"); 
						content.append("<a href='" + url + "' target='_blank'>開啟表單</a><BR>"); 
						
						content.append("----------------------------------------------------------------------<BR>");
						
						content.append("The contract has been assigned to you, Please process it as soon as possible.<BR>"); 
//						content.append("Process Name：" + flowstep.getStepdesc() + "<BR>"); 
						content.append("Process Name：" + roleName + "<BR>"); 
						content.append("Contract Number：" + docState.getApplyNo() + "<BR>"); 
						content.append("Contract Name： " + contractname + "<BR>"); 
						content.append("Creator：" + applicantCname + "<BR>"); 
						content.append("<a href='" + url + "' target='_blank'>Contract Link</a>"); 
				
						emailqueue.setSerno(null);
						emailqueue.setSysid(APP_ID);
						emailqueue.setCategory("FLOW");
						emailqueue.setMailto(StringUtils.isBlank(email) ? ADMIN_EMAIL : email);
//						emailqueue.setSubject(SUBJECT);
						emailqueue.setSubject("[Notification] E-Contract -到關通知信 Process Arrived");
						emailqueue.setContent(content.toString());
//						emailqueue.setAttachment("測試附件");
						emailqueue.setSysmemo(docState.getApplyNo());
						emailqueue.setStatus("N");
						emailqueue.setPriority("1");
						emailqueue.setCreatetime(new Timestamp(new Date().getTime()));
						emailqueue.setCreateuserid(getCreOrUpdUser(null));
						emailqueue.setUpdatetime(new Timestamp(new Date().getTime()));
						emailqueue.setUpdateuserid(getCreOrUpdUser(null));
						
						emailqueueMapper.insert(emailqueue);
					}
				}
			}
		}
		result.addMessage(bf.toString());
		result.setStatus(ProcessResult.OK);
		return result;
	}
	
	/**
	 * 末關卡簽核完成後寄信給申請者
	 * @param docState
	 * @return
	 * @throws Exception
	 */
	public ProcessResult insertEmailToApplicant(DocStateBean docState,String contractname) throws Exception {
		ProcessResult result = new ProcessResult();
		StringBuffer content = null;
		Emailqueue emailqueue = new Emailqueue();
		XauthUsers xauthUsers = null;
//		String applicantCname = "";
		String email = "";
		String apiUrl = env.getProperty("spring.domain");  
		String contectPath = servletContext.getContextPath(); 
		String url = apiUrl + contectPath;  
		String applicantid = "";
		
		if(docState == null) {
			result.setStatus(ProcessResult.NG);
			result.addMessage("發生錯錯誤-流程不得為空");
			return result;
		} else {
			applicantid = docState.getApplicantId();
			xauthUsers = getUser(applicantid);
			if(xauthUsers != null) {
//				applicantCname = xauthUsers.getUserCname();
				email = xauthUsers.getEmail();
			}else {
				result.setStatus(ProcessResult.NG);
				result.addMessage("發生錯錯誤-查無申請人資料");
				return result;
			}
		}
		
		// 末關卡完成寄給申請者內容
		content = new StringBuffer();
		content.append(" 您有一份合約已經簽核完成。 <BR>"); 
//		content.append("關卡名稱：" + flowstep.getStepdesc() + "<BR>"); 
		content.append("合約編號: " + docState.getApplyNo() + "<BR>"); 
		content.append("合約名稱: " + contractname + "<BR>"); 
//		content.append("合約建立者：" + applicantCname + "<BR>"); 
		content.append("<a href='" + url + "' target='_blank'>開啟合約</a><BR>"); 
		content.append("----------------------------------------------------------------------<BR>");
		content.append("Your contract has been signed.   <BR>"); 
		content.append("Contract Number：" + docState.getApplyNo() + "<BR>"); 
		content.append("Contract Name：" + contractname + "<BR>"); 
		content.append("<a href='" + url + "' target='_blank'>Contract Link</a>"); 
		
		emailqueue.setSerno(null);
		emailqueue.setSysid(APP_ID);
		emailqueue.setCategory("FLOW");
		emailqueue.setMailto(StringUtils.isBlank(email) ? ADMIN_EMAIL : email);
//		emailqueue.setSubject(SUBJECT);
		emailqueue.setSubject("[Notification] E-Contract - 合約完成通知信 Signed Completed");
		emailqueue.setContent(content.toString());
//		emailqueue.setAttachment("測試附件");
		emailqueue.setSysmemo(docState.getApplyNo());
		emailqueue.setStatus("N");
		emailqueue.setPriority("1");
		emailqueue.setCreatetime(new Timestamp(new Date().getTime()));
		emailqueue.setCreateuserid(getCreOrUpdUser(null));
		emailqueue.setUpdatetime(new Timestamp(new Date().getTime()));
		emailqueue.setUpdateuserid(getCreOrUpdUser(null));
		
		emailqueueMapper.insert(emailqueue);
		result.setStatus(ProcessResult.OK);
		
		return result;
	}
	
	/**
	 * 末關卡簽核完成後寄信給供應商
	 * @param docState
	 * @return
	 * @throws Exception
	 */
	public ProcessResult insertEmailToSupplier(DocStateBean docState, String supplieremail,String contractname) throws Exception {
		ProcessResult result = new ProcessResult();
		StringBuffer content = null;
		Emailqueue emailqueue = new Emailqueue();
		Flowstep flowstep = null;
		XauthUsers xauthUsers = null;
//		String toUserCname = "";
//		String applicantCname = "";
		String email = supplieremail;
		String apiUrl = env.getProperty("spring.domain");  
		String contectPath = servletContext.getContextPath(); 
		String url = apiUrl + contectPath;  
		
		if(docState == null) {
			result.setStatus(ProcessResult.NG);
			result.addMessage("發生錯錯誤-流程不得為空");
			return result;
		}
		
		QueryWrapper<Flowstep> flowStepQueryWrapper = new QueryWrapper<Flowstep> ();
		flowStepQueryWrapper.eq("FLOWID", docState.getFlowId());
		flowStepQueryWrapper.eq("DEPTID", "999999999");
		List<Flowstep> flowstepList = flowstepMapper.selectList(flowStepQueryWrapper);
		
		if(flowstepList.size() == 0) {
			result.setStatus(ProcessResult.NG);
			result.addMessage("發生錯錯誤-查無流程步驟資料");
			return result;
		}else {
			flowstep = flowstepList.get(0);
		}
		if(StringUtils.isNotBlank(flowstep.getUserids())){
			String[] users = flowstep.getUserids().split(",");
			for(String user : users) {
				xauthUsers = getUser(user);
				if(xauthUsers != null) {
//					toUserCname = xauthUsers.getUserCname();
//					email = xauthUsers.getEmail();
				}else {
					result.setStatus(ProcessResult.NG);
					result.addMessage("發生錯錯誤-查無寄件者資料-001");
					return result;
				}
				// 末關卡完成寄給供應商內容
				content = new StringBuffer();
//				content.append("<H2>[Notification] E-Contract - 合約完成通知信 Signed Completed</H2><BR>"); 
//				content.append(toUserCname + " 您有一份合約已經簽核完成。 <BR>"); 
				content.append(" 您有一份合約已經簽核完成。 <BR>"); 

//				content.append("關卡名稱：" + flowstep.getStepdesc() + "<BR>"); 
				content.append("合約編號: " + docState.getApplyNo() + "<BR>"); 
				content.append("合約名稱: " + contractname + "<BR>"); 
//				content.append("合約建立者：" + applicantCname + "<BR>"); 
				content.append("<a href='" + url + "' target='_blank'>開啟合約</a>"+ "<BR>"); 
				content.append("----------------------------------------------------------------------<BR>");
				content.append("Your contract has been signed.   <BR>"); 
				content.append("Contract Number：" + docState.getApplyNo() + "<BR>"); 
				content.append("Contract Name：" + contractname + "<BR>"); 
				content.append("<a href='" + url + "' target='_blank'>Contract Link</a>"+ "<BR>"); 

				emailqueue.setSerno(null);
				emailqueue.setSysid(APP_ID);
				emailqueue.setCategory("FLOW");
				emailqueue.setMailto(StringUtils.isBlank(email) ? ADMIN_EMAIL : email);
//				emailqueue.setSubject(SUBJECT);
				emailqueue.setSubject("[Notification] E-Contract - 合約完成通知信 Signed Completed");
				emailqueue.setContent(content.toString());
//					emailqueue.setAttachment("測試附件");
				emailqueue.setSysmemo(docState.getApplyNo());
				emailqueue.setStatus("N");
				emailqueue.setPriority("1");
				emailqueue.setCreatetime(new Timestamp(new Date().getTime()));
				emailqueue.setCreateuserid(getCreOrUpdUser(null));
				emailqueue.setUpdatetime(new Timestamp(new Date().getTime()));
				emailqueue.setUpdateuserid(getCreOrUpdUser(null));
				
				emailqueueMapper.insert(emailqueue);
			}
		}
		result.setStatus(ProcessResult.OK);
		
		return result;
	}
	
	/**
	 * 取得單筆User資料
	 * @param userId
	 * @return
	 */
	public XauthUsers getUser(String userId) {
		QueryWrapper<XauthUsers> queryWrapper = new QueryWrapper<XauthUsers>();
		queryWrapper.eq("APP_ID", APP_ID);
		queryWrapper.eq("USER_ID", userId);
		List<XauthUsers> xauthUsersList = xauthUsersMapper.selectList(queryWrapper);
		
		if(xauthUsersList.size() != 0) {
			return xauthUsersList.get(0);
		}else {
			return null;
		}
	}
	
	public void insertNegoentryToDb1(String jsonData) throws Exception {
		
		Date now = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
		String nowYear = sdf.format(now);
		String deptNo = "";
		String supplierCode = "";
		String flow = "";
		double hypcost = 0;
		double supcost = 0;
		double kmcost = 0;
		double natcost = 0;
		double hypmargin = 0;
		double supmargin = 0;
		double kmmargin = 0;
		double natmargin = 0;
		double hypmarginactual = 0;
		double supmarginactual = 0;
		double kmmarginactual = 0;
		double natmarginactual = 0;
		double extra = 0;
		double extrapc = 0;
		double extraautual = 0;
		ReadContext json = JsonPath.parse(jsonData);
		QueryWrapper<Negoentry> queryWrapper = new QueryWrapper<Negoentry>();
		Negoentry negoentry = null;
		
		// 塞變數值
		List<Map<String, Object>> basicDataList = json.read("$.data[?(@.datatype == '基本資料')].docdetail[?(@.resultdata)].resultdata");
		if(basicDataList.size() > 0) {
			deptNo = (String) basicDataList.get(0).get("課別");
		}
		supplierCode = JsonUtil.getJsonPathVal(jsonData, "$.data[?(@.datatype == '基本資料')].docdetail[?(@.resultdata)].resultdata.供應商廠編");
		
		if(rowListInteger(jsonData, "Flow").size() > 0) {
			flow = JsonUtil.getJsonPathVal(jsonData, jsonPath("Flow", rowListInteger(jsonData, "Flow").get(2)));
		}
		if(rowListInteger(jsonData, "HYP_Cost").size() > 0) {
			hypcost = NumberUtils.toDouble(JsonUtil.getJsonPathVal(jsonData, jsonPath("HYP_Cost", rowListInteger(jsonData, "HYP_Cost").get(2))));
		}
		if(rowListInteger(jsonData, "SUP_Cost").size() > 0) {
			supcost = NumberUtils.toDouble(JsonUtil.getJsonPathVal(jsonData, jsonPath("SUP_Cost", rowListInteger(jsonData, "SUP_Cost").get(2))));
		}
		if(rowListInteger(jsonData, "KM_Cost").size() > 0) {
			kmcost = NumberUtils.toDouble(JsonUtil.getJsonPathVal(jsonData, jsonPath("KM_Cost", rowListInteger(jsonData, "KM_Cost").get(2))));
		}
		if(rowListInteger(jsonData, "NAT_Cost").size() > 0) {
			natcost = NumberUtils.toDouble(JsonUtil.getJsonPathVal(jsonData, jsonPath("NAT_Cost", rowListInteger(jsonData, "NAT_Cost").get(2))));
		}
		if(rowListInteger(jsonData, "HYP_Margin").size() > 0) {
			hypmargin = NumberUtils.toDouble(JsonUtil.getJsonPathVal(jsonData, jsonPath("HYP_Margin", rowListInteger(jsonData, "HYP_Margin").get(2))));
		}
		if(rowListInteger(jsonData, "SUP_Margin").size() > 0) {
			supmargin = NumberUtils.toDouble(JsonUtil.getJsonPathVal(jsonData, jsonPath("SUP_Margin", rowListInteger(jsonData, "SUP_Margin").get(2))));
		}
		if(rowListInteger(jsonData, "KM_Margin").size() > 0) {
			kmmargin = NumberUtils.toDouble(JsonUtil.getJsonPathVal(jsonData, jsonPath("KM_Margin", rowListInteger(jsonData, "KM_Margin").get(2))));
		}
		if(rowListInteger(jsonData, "NAT_Margin").size() > 0) {
			natmargin = NumberUtils.toDouble(JsonUtil.getJsonPathVal(jsonData, jsonPath("NAT_Margin", rowListInteger(jsonData, "NAT_Margin").get(2))));
		}
		if(rowListInteger(jsonData, "HYP_Margin_Actual").size() > 0) {
			hypmarginactual = NumberUtils.toDouble(JsonUtil.getJsonPathVal(jsonData, jsonPath("HYP_Margin_Actual", rowListInteger(jsonData, "HYP_Margin_Actual").get(2))));
		}
		if(rowListInteger(jsonData, "SUP_Margin_Actual").size() > 0) {
			supmarginactual = NumberUtils.toDouble(JsonUtil.getJsonPathVal(jsonData, jsonPath("SUP_Margin_Actual", rowListInteger(jsonData, "SUP_Margin_Actual").get(2))));
		}
		if(rowListInteger(jsonData, "KM_Margin_Actual").size() > 0) {
			kmmarginactual = NumberUtils.toDouble(JsonUtil.getJsonPathVal(jsonData, jsonPath("KM_Margin_Actual", rowListInteger(jsonData, "KM_Margin_Actual").get(2))));
		}
		if(rowListInteger(jsonData, "NAT_Margin_Actual").size() > 0) {
			natmarginactual = NumberUtils.toDouble(JsonUtil.getJsonPathVal(jsonData, jsonPath("NAT_Margin_Actual", rowListInteger(jsonData, "NAT_Margin_Actual").get(2))));
		}
		if(rowListInteger(jsonData, "Extra白單").size() > 0) {
			extra = NumberUtils.toDouble(JsonUtil.getJsonPathVal(jsonData, jsonPath("Extra白單", rowListInteger(jsonData, "Extra白單").get(2))));
		}
		if(rowListInteger(jsonData, "Extra白單%").size() > 0) {
			extrapc = NumberUtils.toDouble(JsonUtil.getJsonPathVal(jsonData, jsonPath("Extra白單%", rowListInteger(jsonData, "Extra白單%").get(2))));
		}
		if(rowListInteger(jsonData, "Extra白單%Actual").size() > 0) {
			extraautual = NumberUtils.toDouble(JsonUtil.getJsonPathVal(jsonData, jsonPath("Extra白單%Actual", rowListInteger(jsonData, "Extra白單%Actual").get(2))));
		}
		
		// 將變數值存入DB
		queryWrapper.eq("YEARS", nowYear);
		queryWrapper.eq("DEPTNO", deptNo);
		queryWrapper.eq("SUPPLIERCODE", supplierCode);
		List<Negoentry> negoentryList = negoentryMapper.selectList(queryWrapper);
		if(negoentryList.size() > 0) {
			negoentry = negoentryList.get(0);
			negoentry.setFlow(flow);
			negoentry.setHypcost(hypcost);
			negoentry.setSupcost(supcost);
			negoentry.setKmcost(kmcost);
			negoentry.setNatcost(natcost);
			negoentry.setHypmargin(hypmargin);
			negoentry.setSupmargin(supmargin);
			negoentry.setKmmargin(kmmargin);
			negoentry.setNatmargin(natmargin);
			negoentry.setHypmarginactual(hypmarginactual);
			negoentry.setSupmarginactual(supmarginactual);
			negoentry.setKmmarginactual(kmmarginactual);
			negoentry.setNatmarginactual(natmarginactual);
			negoentry.setExtra(extra);
			negoentry.setExtrapc(extrapc);
			negoentry.setExtraactual(extraautual);
			negoentry.setUpdatedate(new Timestamp(new Date().getTime()));
			negoentry.setUpdateuser(getCreOrUpdUser(null));
			negoentryMapper.update(negoentry, queryWrapper);
		} else {
			negoentry = new Negoentry();
			negoentry.setYears(nowYear);
			negoentry.setDeptno(deptNo);
			negoentry.setSuppliercode(supplierCode);
			negoentry.setFlow(flow);
			negoentry.setHypcost(hypcost);
			negoentry.setSupcost(supcost);
			negoentry.setKmcost(kmcost);
			negoentry.setNatcost(natcost);
			negoentry.setHypmargin(hypmargin);
			negoentry.setSupmargin(supmargin);
			negoentry.setKmmargin(kmmargin);
			negoentry.setNatmargin(natmargin);
			negoentry.setHypmarginactual(hypmarginactual);
			negoentry.setSupmarginactual(supmarginactual);
			negoentry.setKmmarginactual(kmmarginactual);
			negoentry.setNatmarginactual(natmarginactual);
			negoentry.setExtra(extra);
			negoentry.setExtrapc(extrapc);
			negoentry.setExtraactual(extraautual);
			negoentry.setCreatedate(new Timestamp(new Date().getTime()));
			negoentry.setCreateuser(getCreOrUpdUser(null));
			negoentry.setUpdatedate(new Timestamp(new Date().getTime()));
			negoentry.setUpdateuser(getCreOrUpdUser(null));
			negoentryMapper.insert(negoentry);
		}
	}
	
	public List<Double> rowListDouble(String jsonData, String displayname) {
		ReadContext json = JsonPath.parse(jsonData);
		List<Double> rowList = null;
		rowList = json.read("$.data[?(@.datatype == '審核評估')].docdetail[?(@.displayname=='" + displayname + "')].row");
		
		return rowList;
	}
	
	public List<Integer> rowListInteger(String jsonData, String displayname) {
		ReadContext json = JsonPath.parse(jsonData);
		List<Integer> rowList = null;
		rowList = json.read("$.data[?(@.datatype == '審核評估')].docdetail[?(@.displayname=='" + displayname + "')].row");
		
		return rowList;
	}
	
	public String jsonPath(String displayname, double row) {
//		int rowNumber = (int)row;
		String result = "$.data[?(@.datatype == '審核評估')].docdetail[?(@.displayname=='" + displayname + "'&&@.row==" + row + ")].value";
	
		return result;
	}

	/**
	 * 刪除檔案(附件)
	 * 
	 * @param id
	 * @param dataId
	 * @return
	 */
	public ProcessResult deleteFile(String id, String dataId) {
		ProcessResult result = new ProcessResult();
		result.setStatus(ProcessResult.NG);
		Contractfile contractFile = null;
		QueryWrapper<Contractfile> queryWrapper = new QueryWrapper<Contractfile>();
		// 若id傳來為serno
		if(id.indexOf("fix") == -1) {
			queryWrapper.eq("SERNO", id);
		} else {
			// 若id傳來為fixid
			queryWrapper.eq("FIXID", id);
			queryWrapper.eq("DATAID", dataId);
		}
		
		List<Contractfile> contractFileList = contractfileMapper.selectList(queryWrapper);
		if(contractFileList.size() > 0) {
			contractFile = contractFileList.get(0);
			File file = new File(contractFile.getFliepath() + "/" + contractFile.getFliename());
			if(file.exists()) {
				file.delete();
			}
			contractfileMapper.delete(queryWrapper);
			result.setStatus(ProcessResult.OK);
			result.addMessage("刪除成功");
		}
		
		return result;
	}
	
	/**
	 * 取得檔案路徑
	 * @param  id
	 * @param dataId
	 * @return
	 */
	public String getFileSerNo(String id, String dataId) {
		Contractfile contractfile = null;
		String result = "";
		
		// 查詢要下載的檔案路徑
		QueryWrapper<Contractfile> queryWrapper = new QueryWrapper<Contractfile>();
		// 若id傳來為serno
		if(id.indexOf("fix") > -1) {
			queryWrapper.eq("FIXID", id);
			queryWrapper.eq("DATAID", dataId);
			List<Contractfile> contractFileList = contractfileMapper.selectList(queryWrapper);
			if(contractFileList.size() > 0) {
				contractfile = contractFileList.get(0);
				result = String.valueOf(contractfile.getSerno());
			} 
		}else {
			result = id;
		}
		return result;
	}
	
	/**
	 * 取得檔案路徑
	 * @param serNo
	 * @return
	 */
	public String getDownloadFile(String serNo) {
		Contractfile contractfile = null;
		String result = "";
		
		// 查詢要下載的檔案路徑
		QueryWrapper<Contractfile> queryWrapper = new QueryWrapper<Contractfile>();
		queryWrapper.eq("SERNO", serNo);
		
		List<Contractfile> contractFileList = contractfileMapper.selectList(queryWrapper);
		if(contractFileList.size() > 0) {
			contractfile = contractFileList.get(0);
			result = contractfile.getFliepath() + "/" + contractfile.getFliename();
		} 

		return result;
		
	}	
	
	/**
	 * 取得供應商資訊ID(以中文名稱查找) 產生下拉式選單
	 * @param suppliercode
	 * @return
	 * @throws Exception
	 */
	public List<Suppliermaster> supplierIdGetList(String suppliercode){
		QueryWrapper<Suppliermaster> queryWrapper = new QueryWrapper<Suppliermaster>();
		queryWrapper.select("DISTINCT SUPPLIERCODE");
		queryWrapper.like("SUPPLIERCODE", suppliercode);
		List<Suppliermaster> supplierMasterList = suppliermasterMapper.selectList(queryWrapper);
		return supplierMasterList;
	}
	/**
	 * 取得供應商資訊(以中文名稱查找) 產生下拉式選單
	 * @param suppliername
	 * @return
	 * @throws Exception
	 */
	
	public List<Suppliermaster> supplierNameGetList(String suppliername){
		QueryWrapper<Suppliermaster> queryWrapper = new QueryWrapper<Suppliermaster>();
		queryWrapper.select("DISTINCT SUPPLIERCNAME");
		queryWrapper.like("SUPPLIERCNAME", suppliername);
		List<Suppliermaster> supplierMasterList = suppliermasterMapper.selectList(queryWrapper);
		return supplierMasterList;
	}
	
	/**
	 * 取得供應商統編(以中文名稱查找) 產生下拉式選單
	 * @param suppliercode
	 * @return
	 * @throws Exception
	 */
	public List<Suppliermaster> supplierGUIGetList(String SupplierGUI){
		QueryWrapper<Suppliermaster> queryWrapper = new QueryWrapper<Suppliermaster>();
		queryWrapper.select("DISTINCT SUPPLIERGUI");
		queryWrapper.like("SUPPLIERGUI", SupplierGUI);
		List<Suppliermaster> supplierMasterList = suppliermasterMapper.selectList(queryWrapper);
		return supplierMasterList;
	}
	
	/**
	 * 取得小卡資料畫面html字串
	 * @param section
	 * @param suppliercode
	 * @return
	 */
	public String getNegoentryHtml(String section, String suppliercode,String contractYear,Map<String,String>checkmap){
		String taskname = MapUtils.getString(checkmap, "taskName");
		String usercname = MapUtils.getString(checkmap, "usercname");
		String contractorAgentUserId=MapUtils.getString(checkmap, "contractorAgentUserId");
		//判斷是否承辦人或是承辦代理人
		usermatch = isTaskApply(usercname,contractorAgentUserId,taskname);
		String result = "";
		StringBuffer content = new StringBuffer();
		QueryWrapper<Negoentry> queryWrapper = new QueryWrapper<Negoentry>();
		queryWrapper.eq("DEPTNO", section);
		queryWrapper.eq("SUPPLIERCODE", suppliercode);
		queryWrapper.in("YEARS", getThreeYears(contractYear));
		queryWrapper.orderByAsc("YEARS");
		List<Negoentry> negoentryList = negoentryMapper.selectList(queryWrapper);
		negoentryList = setNegoentryList(negoentryList, contractYear);
		content.append("<div class='label-1 clr-style' id='approvalDiv'><table border='1' >");
		content.append(genHtmlContent(negoentryList, "合約年度", contractYear));
		content.append(genHtmlContent(negoentryList, LocaleMessage.getMsg("negoentry.field.flow"), contractYear));
		content.append(genHtmlContent(negoentryList, LocaleMessage.getMsg("negoentry.field.hypcost"), contractYear));
		content.append(genHtmlContent(negoentryList, LocaleMessage.getMsg("negoentry.field.supcost"), contractYear));
		content.append(genHtmlContent(negoentryList, LocaleMessage.getMsg("negoentry.field.kmcost"), contractYear));
		content.append(genHtmlContent(negoentryList, LocaleMessage.getMsg("negoentry.field.natcost"), contractYear));
		content.append(genHtmlContent(negoentryList, LocaleMessage.getMsg("negoentry.field.hypmargin"), contractYear));
		content.append(genHtmlContent(negoentryList, LocaleMessage.getMsg("negoentry.field.supmargin"), contractYear));
		content.append(genHtmlContent(negoentryList, LocaleMessage.getMsg("negoentry.field.kmmargin"), contractYear));
		content.append(genHtmlContent(negoentryList, LocaleMessage.getMsg("negoentry.field.natmargin"), contractYear));
		content.append(genHtmlContent(negoentryList, LocaleMessage.getMsg("negoentry.field.hypmarginactual"), contractYear));
		content.append(genHtmlContent(negoentryList, LocaleMessage.getMsg("negoentry.field.supmarginactual"), contractYear));
		content.append(genHtmlContent(negoentryList, LocaleMessage.getMsg("negoentry.field.kmmarginactual"), contractYear));
		content.append(genHtmlContent(negoentryList, LocaleMessage.getMsg("negoentry.field.natmarginactual"), contractYear));
		content.append(genHtmlContent(negoentryList, LocaleMessage.getMsg("negoentry.field.extra"), contractYear));
		content.append(genHtmlContent(negoentryList, LocaleMessage.getMsg("negoentry.field.extrapc"), contractYear));
		content.append(genHtmlContent(negoentryList, LocaleMessage.getMsg("negoentry.field.extraactual"), contractYear));
		content.append("</table>");
		result = content.toString();		
		return result;
	}
	
	/**
	 * 處理小卡資料list，讓list確定有三筆
	 * @param negoentryList
	 * @return
	 */
	public List<Negoentry> setNegoentryList(List<Negoentry> negoentryList, String contractYear) {
		Negoentry negoentryBean = null;
		boolean isYearData = false; // 判斷是否有當年度資料
		List<Negoentry> result = new ArrayList<Negoentry>();
		
		for(String year : getThreeYears(contractYear)) {
			isYearData = false;
			for(Negoentry negoentry : negoentryList) {
				negoentryBean = null;
				if(StringUtils.equals(year, negoentry.getYears())) {
					isYearData = true;
					negoentryBean = negoentry;
					break;
				}
			}
			if(!isYearData) {
				negoentryBean = new Negoentry();
				negoentryBean.setYears(year);
				result.add(negoentryBean);
			} else {
				result.add(negoentryBean);
			}
		}
		
		return result;
	}
	
	/**
	 * 組小卡資料各欄位html
	 * @param negoentryList
	 * @param columnName
	 * @return
	 */
	public String genHtmlContent(List<Negoentry> negoentryList, String columnName, String contractYear) {
		StringBuffer content = new StringBuffer();
		content.append("<tr>");
		if(StringUtils.equals(columnName, LocaleMessage.getMsg("negoentry.field.natcost"))) {
			content.append("<td width='20%' height='50px' align='center' class=''><label id='"+columnName+"' class='TITLE' style='color: #AE0000;'>" + columnName.replace("_", " ") + "</label></td>");
		}else if(StringUtils.equals(columnName, LocaleMessage.getMsg("negoentry.field.natmargin"))) {
			content.append("<td width='20%' height='50px' align='center' class=''><label id='"+columnName+"' class='TITLE' style='color: #006000;'>" + columnName.replace("_", " ") + "</label></td>");
		}else if(StringUtils.equals(columnName, LocaleMessage.getMsg("negoentry.field.natmarginactual"))) {
			content.append("<td width='20%' height='50px' align='center' class=''><label id='"+columnName+"' class='TITLE' style='color: #D26900;'>" + columnName.replace("_", " ") + "</label></td>");
		}else if(StringUtils.equals(columnName, LocaleMessage.getMsg("negoentry.field.extrapc"))) {
			content.append("<td width='20%' height='50px' align='center'><label id='"+"EXTRAPC"+"'>" + columnName.replace("_", " ") + "</label></td>");
		}else if(StringUtils.equals(columnName, LocaleMessage.getMsg("negoentry.field.extraactual"))) {
			content.append("<td width='20%' height='50px' align='center'><label id='"+"EXTRACATUAL"+"' class='TITLE'>" + columnName.replace("_", " ") + "</label></td>");
		}else {
			content.append("<td width='20%' height='50px' align='center'><label id='"+columnName+"'>" + columnName.replace("_", " ") + "</label></td>");
		}
		Negoentry negoentry = null;

		for(int i = 0 ; i < negoentryList.size() ; i ++) {
			negoentry = negoentryList.get(i);
			switch(columnName) {
			case "合約年度" :
				if(i == 2) {
					content.append("<td width='20%' height='50px' align='center'>" + getThreeYears(contractYear).get(2) + "</td>");
				} 
				else {
					content.append("<td width='20%' height='50px' align='center'>" + negoentry.getYears() + "</td>");
				}
				break;
			case "Flow" :
				content.append(getHtmlTd(i, negoentry.getFlow() == null ? "" : String.valueOf(negoentry.getFlow()), columnName, columnName));
				break;
			case "HYP_Cost" :
				content.append(getHtmlTd(i, negoentry.getHypcost() == null ? "" : String.valueOf(negoentry.getHypcost()), columnName, columnName));
				break;
			case "SUP_Cost" :
				content.append(getHtmlTd(i, negoentry.getSupcost() == null ? "" : String.valueOf(negoentry.getSupcost()), columnName, columnName));
				break;
			case "KM_Cost" :
				content.append(getHtmlTd(i, negoentry.getKmcost() == null ? "" : String.valueOf(negoentry.getKmcost()), columnName, columnName));
				break;
			case "AVG_Cost" :
				content.append(getHtmlTd(i, negoentry.getNatcost() == null ? "" : String.valueOf(negoentry.getNatcost()), columnName, columnName));
				break;
			case "HYP_Margin" :
				content.append(getHtmlTd(i, negoentry.getHypmargin() == null ? "" : String.valueOf(negoentry.getHypmargin()), columnName, columnName));
				break;
			case "SUP_Margin" :
				content.append(getHtmlTd(i, negoentry.getSupmargin() == null ? "" : String.valueOf(negoentry.getSupmargin()), columnName, columnName));
				break;
			case "KM_Margin" :
				content.append(getHtmlTd(i, negoentry.getKmmargin() == null ? "" : String.valueOf(negoentry.getKmmargin()), columnName, columnName));
				break;
			case "AVG_Margin_Budget" :
				content.append(getHtmlTd(i, negoentry.getNatmargin() == null ? "" : String.valueOf(negoentry.getNatmargin()), columnName, columnName));
				break;
			case "HYP_Margin_Actual" :
				content.append(getHtmlTd(i, negoentry.getHypmarginactual() == null ? "" : String.valueOf(negoentry.getHypmarginactual()), columnName, columnName));
				break;
			case "SUP_Margin_Actual" :
				content.append(getHtmlTd(i, negoentry.getSupmarginactual() == null ? "" : String.valueOf(negoentry.getSupmarginactual()), columnName, columnName));
				break;
			case "KM_Margin_Actual" :
				content.append(getHtmlTd(i, negoentry.getKmmarginactual() == null ? "" : String.valueOf(negoentry.getKmmarginactual()), columnName, columnName));
				break;
			case "AVG_Margin_Actual" :
				content.append(getHtmlTd(i, negoentry.getNatmarginactual() == null ? "" : String.valueOf(negoentry.getNatmarginactual()), columnName, columnName));
				break;
			case "Extra白單" :
				content.append(getHtmlTd(i, negoentry.getExtra() == null ? "" : String.valueOf(negoentry.getExtra()), "EXTRA", columnName));
				break;
			case "Extra白單%" :
				content.append(getHtmlTd(i, negoentry.getExtrapc() == null ? "" : String.valueOf(negoentry.getExtrapc()), "EXTRAPC", columnName));
				break;
			case "AVG_Margin_Actual_with_白單" :
				content.append(getHtmlTd(i, negoentry.getExtraactual() == null ? "" : String.valueOf(negoentry.getExtraactual()), "EXTRAActual", columnName));
				break;
			}
		}
		content.append("</tr>");
		return content.toString();
	}
	
	public String getHtmlTd(int i, String value, String columnName, String cname) {
		String result = "";
		if(i == 2) {
			//判斷是否為承辦人或是代理承辦人
			if(usermatch) {
				if(StringUtils.equals(cname, LocaleMessage.getMsg("negoentry.field.natcost"))) {
					result = "<td width='20%' height='50px' align='center'><input type='text' style='color:#AE0000;font-weight: bold;text-align:center;' id='" + columnName + "_" + i + "' name='" + columnName + "' data-cname='" + cname + "' onChange='setApprovalData(\"" + columnName + "_" + i +"\")'/><label class='TITLE' style='color: #AE0000;'>%</label></td>";
				}else if(StringUtils.equals(cname, LocaleMessage.getMsg("negoentry.field.natmargin"))) {
					result = "<td width='20%' height='50px' align='center'><input type='text' style='color:#006000;font-weight: bold;text-align:center;' id='" + columnName + "_" + i + "' name='" + columnName + "' data-cname='" + cname + "' onChange='setApprovalData(\"" + columnName + "_" + i +"\")'/><label class='TITLE' style='color: #006000;'>%</label></td>";
				}else if(StringUtils.equals(cname, LocaleMessage.getMsg("negoentry.field.natmarginactual"))) {
					result = "<td width='20%' height='50px' align='center'><input type='text' style='color:#D26900;font-weight: bold;text-align:center;' id='" + columnName + "_" + i + "' name='" + columnName + "' data-cname='" + cname + "' onChange='setApprovalData(\"" + columnName + "_" + i +"\")'/><label class='TITLE' style='color: #D26900;'>%</label></td>";
				}else if(StringUtils.equals(cname, LocaleMessage.getMsg("negoentry.field.extra")) || StringUtils.equals(cname, LocaleMessage.getMsg("negoentry.field.flow"))) {
					result = "<td width='20%' height='50px' align='center'><input type='text' style='text-align:center;' id='" + columnName + "_" + i + "' name='" + columnName + "' data-cname='" + cname + "' onChange='setApprovalData(\"" + columnName + "_" + i +"\")'/></td>";
				}else if(StringUtils.equals(cname, LocaleMessage.getMsg("negoentry.field.extraactual")) ) {
					result = "<td width='20%' height='50px' align='center'><input type='text' style='text-align:center;' id='" + columnName + "_" + i + "' name='" + columnName + "' data-cname='" + cname + "' onChange='setApprovalData(\"" + columnName + "_" + i +"\")' Readonly/><label class='TITLE'>%</label></td>";
				}else {
					result = "<td width='20%' height='50px' align='center'><input type='text' style='text-align:center;' id='" + columnName + "_" + i + "' name='" + columnName + "' data-cname='" + cname + "' onChange='setApprovalData(\"" + columnName + "_" + i +"\")'/><label>%</label></td>";
				}
			}
			//非承辦人改LABEL
			else {
				if(StringUtils.equals(cname, LocaleMessage.getMsg("negoentry.field.natcost"))) {
					result = "<td width='20%' height='50px' align='center'><label type='text' class='TITLE' style='color:#AE0000;' id='" + columnName + "_" + i + "' name='" + columnName + "' data-cname='" + cname + "' onChange='setApprovalData(\"" + columnName + "_" + i +"\")'/><label class='TITLE' style='color: #AE0000;'>%</label></td>";
				}else if(StringUtils.equals(cname, LocaleMessage.getMsg("negoentry.field.natmargin"))) {
					result = "<td width='20%' height='50px' align='center'><label type='text' class='TITLE' style='color:#006000;' id='" + columnName + "_" + i + "' name='" + columnName + "' data-cname='" + cname + "' onChange='setApprovalData(\"" + columnName + "_" + i +"\")'/><label class='TITLE' style='color: #006000;'>%</label></td>";
				}else if(StringUtils.equals(cname, LocaleMessage.getMsg("negoentry.field.natmarginactual"))) {
					result = "<td width='20%' height='50px' align='center'><label type='text' class='TITLE' style='color:#D26900;' id='" + columnName + "_" + i + "' name='" + columnName + "' data-cname='" + cname + "' onChange='setApprovalData(\"" + columnName + "_" + i +"\")'/><label class='TITLE' style='color: #D26900;'>%</label></td>";
				}else if(StringUtils.equals(cname, LocaleMessage.getMsg("negoentry.field.extra")) || StringUtils.equals(cname, LocaleMessage.getMsg("negoentry.field.flow"))) {
					result = "<td width='20%' height='50px' align='center'><label type='text' style='text-align:center;' id='" + columnName + "_" + i + "' name='" + columnName + "' data-cname='" + cname + "' onChange='setApprovalData(\"" + columnName + "_" + i +"\")'/></td>";
				}else if(StringUtils.equals(cname, LocaleMessage.getMsg("negoentry.field.extrapc")) ) {
					result = "<td width='20%' height='50px' align='center'><label type='text' style='text-align:center;' id='" + "EXTRAPC" + "_" + i + "' name='" + columnName + "' data-cname='" + cname + "' onChange='setApprovalData(\"" + columnName + "_" + i +"\")'/><label>%</label></td>";
				}else if(StringUtils.equals(cname, LocaleMessage.getMsg("negoentry.field.extraactual")) ) {
					result = "<td width='20%' height='50px' align='center'><label type='text' class='TITLE' style='text-align:center;' id='" + "EXTRAActual" + "_" + i + "' name='" + columnName + "' data-cname='" + cname + "' onChange='setApprovalData(\"" + columnName + "_" + i +"\")' Readonly/><label class='TITLE'>%</label></td>";
				}
				else {
					result = "<td width='20%' height='50px' align='center'><label type='text' style='text-align:center;' id='" + columnName + "_" + i + "' name='" + columnName + "' data-cname='" + cname + "' onChange='setApprovalData(\"" + columnName + "_" + i +"\")'/><label>%</label></td>";
				}
			}
		} else {
			if(StringUtils.equals(cname, LocaleMessage.getMsg("negoentry.field.flow")) || StringUtils.equals(cname, LocaleMessage.getMsg("negoentry.field.extra"))) {
				result = "<td width='20%' height='50px' align='center'><label id='"+columnName + "_" + i +"'>"+value+"</label></td>";
			} else {
				if(StringUtils.equals(cname, LocaleMessage.getMsg("negoentry.field.natcost"))) {
					result = "<td width='20%' height='50px' align='center'><label id='"+columnName + "_" + i +"' class='TITLE' style='color: #AE0000;'>"+value+"</label><label class='TITLE' style='color: #AE0000;'>%</label></td>";
				}else if(StringUtils.equals(cname, LocaleMessage.getMsg("negoentry.field.natmargin"))) {
					result = "<td width='20%' height='50px' align='center'><label id='"+columnName + "_" + i +"' class='TITLE' style='color: #006000;'>"+value+"</label><label class='TITLE' style='color: #006000;'>%</label></td>";
				}else if(StringUtils.equals(cname, LocaleMessage.getMsg("negoentry.field.natmarginactual"))) {
					result = "<td width='20%' height='50px' align='center'><label id='"+columnName + "_" + i +"' class='TITLE' style='color: #D26900;'>"+value+"</label><label class='TITLE' style='color: #D26900;'>%</label></td>";
				}else if(StringUtils.equals(cname, LocaleMessage.getMsg("negoentry.field.extraactual"))) {
					result = "<td width='20%' height='50px' align='center'><label id='"+columnName + "_" + i +"' class='TITLE'>"+value+"</label><label class='TITLE'>%</label></td>";
				}else {
					result = "<td width='20%' height='50px' align='center'><label id='"+columnName + "_" + i +"'>"+value+"</label>%</td>";
				}
			}
		}
		
		return result;
	}
	
	/**
	 * 取得近三年年度
	 * @return
	 */
	public List<String> getThreeYears(String contractYear){
		List<String> result = new ArrayList<String>();
		for(int i = 2 ; i >= 0 ; i --) {
			result.add(String.valueOf((Integer.parseInt(contractYear) - i)));
		}
		return result;
	}
	
	/**
	 * 將審核評估資料存入Negoentry
	 * @param jsonData
	 * @throws Exception
	 */
	public void insertNegoentryToDb(String jsonData, String contractYear) throws Exception {
		String thisYear = getThreeYears(contractYear).get(2);
		QueryWrapper<Negoentry> queryWrapper = new QueryWrapper<Negoentry>();
		ReadContext json = JsonPath.parse(jsonData);
		List<Map<String, Object>> resultdataList = json.read("$.data[?(@.datatype == '基本資料')].docdetail[?(@.resultdata)].resultdata");
		if(resultdataList != null && resultdataList.size() > 0) {
			Map<String, Object> resultdataMap =  resultdataList.get(0);
			String deptno = (String) resultdataMap.get(LocaleMessage.getMsg("contract.field.section"));
			String suppliercode = (String) resultdataMap.get(LocaleMessage.getMsg("suppliter.field.suppliercode"));
			// 將變數值存入DB
			queryWrapper.eq("YEARS", thisYear);
			queryWrapper.eq("DEPTNO", deptno);
			queryWrapper.eq("SUPPLIERCODE", suppliercode);
			List<Negoentry> negoentryList = negoentryMapper.selectList(queryWrapper);
			Negoentry negoentry = null;
			if(negoentryList.size() > 0) {
				negoentry = negoentryList.get(0);
				setNegoentryData(negoentry, resultdataMap);
				negoentryMapper.update(negoentry, queryWrapper);
			} else {
				negoentry = new Negoentry();
				negoentry.setYears(thisYear);
				negoentry.setDeptno(deptno);
				negoentry.setSuppliercode(suppliercode);
				negoentry.setCreatedate(new Timestamp(new Date().getTime()));
				negoentry.setCreateuser(getCreOrUpdUser(null));
				setNegoentryData(negoentry, resultdataMap);
				negoentryMapper.insert(negoentry);
			}
		}
	}
	
	/**
	 * 組合審核評估資料
	 * @param negoentry
	 * @param resultdataMap
	 * @return
	 * @throws Exception
	 */
	public Negoentry setNegoentryData(Negoentry negoentry, Map<String, Object> resultdataMap) throws Exception {
		
		negoentry.setFlow((String) resultdataMap.get(LocaleMessage.getMsg("negoentry.field.flow")));
		negoentry.setHypcost(NumberUtils.toDouble((String) resultdataMap.get(LocaleMessage.getMsg("negoentry.field.hypcost"))));
		negoentry.setSupcost(NumberUtils.toDouble((String) resultdataMap.get(LocaleMessage.getMsg("negoentry.field.supcost"))));
		negoentry.setKmcost(NumberUtils.toDouble((String) resultdataMap.get(LocaleMessage.getMsg("negoentry.field.kmcost"))));
		negoentry.setNatcost(NumberUtils.toDouble((String) resultdataMap.get(LocaleMessage.getMsg("negoentry.field.natcost"))));
		negoentry.setHypmargin(NumberUtils.toDouble((String) resultdataMap.get(LocaleMessage.getMsg("negoentry.field.hypmargin"))));
		negoentry.setSupmargin(NumberUtils.toDouble((String) resultdataMap.get(LocaleMessage.getMsg("negoentry.field.supmargin"))));
		negoentry.setKmmargin(NumberUtils.toDouble((String) resultdataMap.get(LocaleMessage.getMsg("negoentry.field.kmmargin"))));
		negoentry.setNatmargin(NumberUtils.toDouble((String) resultdataMap.get(LocaleMessage.getMsg("negoentry.field.natmargin"))));
		negoentry.setHypmarginactual(NumberUtils.toDouble((String) resultdataMap.get(LocaleMessage.getMsg("negoentry.field.hypmarginactual"))));
		negoentry.setSupmarginactual(NumberUtils.toDouble((String) resultdataMap.get(LocaleMessage.getMsg("negoentry.field.supmarginactual"))));
		negoentry.setKmmarginactual(NumberUtils.toDouble((String) resultdataMap.get(LocaleMessage.getMsg("negoentry.field.kmmarginactual"))));
		negoentry.setNatmarginactual(NumberUtils.toDouble((String) resultdataMap.get(LocaleMessage.getMsg("negoentry.field.natmarginactual"))));
		negoentry.setExtra(NumberUtils.toDouble((String) resultdataMap.get(LocaleMessage.getMsg("negoentry.field.extra"))));
		negoentry.setExtrapc(NumberUtils.toDouble((String) resultdataMap.get(LocaleMessage.getMsg("negoentry.field.extrapc"))));
		negoentry.setExtraactual(NumberUtils.toDouble((String) resultdataMap.get(LocaleMessage.getMsg("negoentry.field.extraactual"))));
		negoentry.setUpdatedate(new Timestamp(new Date().getTime()));
		negoentry.setUpdateuser(getCreOrUpdUser(null));
		
		return negoentry;
	}
	/**
	 * 合約編輯預覽功能
	 * 
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> previewpath(Map<String, Object> params) throws Exception {
		Map<String, Object> result = new LinkedHashMap<String, Object>();
		String contractNo = MapUtils.getString(params, "contractNo");
		String flowId = "";
		String dataId = "";
		String modelName = "";
		String scFileName = "";
		String jsonData = new Gson().toJson(params.get("data"), Map.class);
		ReadContext json = JsonPath.parse(jsonData);
		
		List<Map<String, Object>> basicDataList = json.read("$.data[?(@.datatype == '基本資料')].docdetail[?(@.resultdata)].resultdata");
		if(basicDataList.size() > 0) {
			modelName = (String) basicDataList.get(0).get("合約模式");
		}
		Contractmaster contractmaster = commonService.getContractmasterData(contractNo);
		if(contractmaster == null) {
			result.put("message", "查無合約資料");
			return result;
		} else {
			dataId = contractmaster.getDataid();
			flowId = contractmaster.getFlowid();
		}
		if(StringUtils.equals(modelName, "制式合約")) {
			scFileName = contractNo + "_1.pdf";
			contractReportService.getcreateScPdf(DOWMLOAD_PDF_URL + dataId, scFileName, jsonData, setPdfValue(jsonData, flowId, modelName), modelName);
		} else {
			QueryWrapper<Contractfile> contractfileQw = new QueryWrapper<Contractfile>();
			contractfileQw.eq("DATAID", contractNo);
			contractfileQw.eq("TYPE", "01");
			contractfileQw.eq("FILETYPE", "合約主檔");
			List<Contractfile> contractfileList = contractfileMapper.selectList(contractfileQw);
			if(contractfileList.size() > 0) {
				Contractfile contractfile = contractfileList.get(0);
				String contractFileName = contractfile.getFliename();
				String resultPdfFileName = StringUtils.split(contractFileName, ".")[0] + "_1.pdf";
				// 組成pdf
				contractReportService.createPdf(setPdfValue(jsonData, flowId, modelName), DOWMLOAD_PDF_URL, resultPdfFileName, contractFileName, modelName);
			}
		}
		
		return result;
	}
	
	/**
	 * 建立ES index
	 * @param params
	 * @param json
	 * @throws Exception
	 */
	@SuppressWarnings("static-access")
	private void insertDesignermgrForES(Map<String, Object> params, String ip, String port, String indexName, String json, String oldJson) throws Exception{
		String contractNo = MapUtils.getString(params, "contractNo");
			
		//檢查連線
		boolean isConnect = elasticSearchUtil.initESClinet(ip, port);
		if(isConnect){
			//index是否存在；存在->寫入，不存在->新增
			if(!elasticSearchUtil.existsIndex(indexName)){
				//建立index
				elasticSearchUtil.createIndex(indexName);
				//寫入資料
				elasticSearchUtil.insertIndex(indexName, contractNo, oldJson);
				elasticSearchUtil.deleteES(indexName);
				elasticSearchUtil.insertIndex(indexName, contractNo, json);
			}
		}
		//關閉連線
		elasticSearchUtil.closeESClient();
	}
	
	/**
	 * 取得個人範本名稱下拉選單
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> getPerConTemplateList(Map<String, Object> params) throws Exception  {
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		Map<String, Object> data = null;
		UserInfo userInfo = userContext.getCurrentUser();
		String module = MapUtils.getString(params, "module");
		String disp = MapUtils.getString(params, "disp");
		
		if(StringUtils.isNotBlank(module) && StringUtils.isNotBlank(disp)) {
			QueryWrapper<Percontracttemplate> perConTemplateWrapper = new QueryWrapper<Percontracttemplate>();
			perConTemplateWrapper.eq("MODULE", module);
			perConTemplateWrapper.eq("FLOWID", disp);
			perConTemplateWrapper.eq("CREATEUSER", userInfo.getIdenId() + ":" + userInfo.getUserId());
			perConTemplateWrapper.isNotNull("PRDTIME");
			perConTemplateWrapper.isNull("DROPTIME");
			List<Percontracttemplate> perConTemplateList = percontracttemplateMapper.selectList(perConTemplateWrapper);
			for(Percontracttemplate percontracttemplate : perConTemplateList) {
				data = new HashMap<String, Object>();
				data.put("dataid", percontracttemplate.getDataid());
				data.put("templatename", percontracttemplate.getTemplatename());
				result.add(data);
			}
		}
		
		return result;
	}
	
	/**
	 * 取得範本種類下拉選單
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> getTemplateTypeList(Map<String, Object> params) throws Exception {
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> perTempList = getPerConTemplateList(params);
		Map<String, Object> data = null;
		List<XauthSysCode> templateTypeList = getSysCode("TEMPLATE_TYPE");
		boolean isPerTemp = false;
		String module = MapUtils.getString(params, "module");
		String disp = MapUtils.getString(params, "disp");

		if (StringUtils.isNotBlank(module) && StringUtils.isNotBlank(disp)) {
			if (templateTypeList != null && templateTypeList.size() > 0) {
				// 判斷是否有個人範本合約
				if (perTempList != null && perTempList.size() > 0) {
					isPerTemp = true;
				}
				// 若有範本才將個人範本下拉選單加入
				for (XauthSysCode templateTypeData : templateTypeList) {
					if (isPerTemp || !StringUtils.equals("PT", templateTypeData.getCode())) {
						data = new HashMap<String, Object>();
						data.put("code", templateTypeData.getCode());
						data.put("cname", templateTypeData.getCname());
						result.add(data);
					}
				}
			}
		}

		return result;
	}
	/**
	 * 取得SHORTAGEPENALTY
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public ProcessResult selectShortagepenalty(@RequestBody Map<String, Object> params) throws Exception {
		String suppliercode = MapUtils.getString(params, "SUPPLIERCODE");
		String deptno = MapUtils.getString(params, "DEPTNO");
		
		String lastyear = "";
		ProcessResult result = new ProcessResult();
		List<String> list = new LinkedList<String>();
        DecimalFormat formatter = new DecimalFormat("#.##");
        
		QueryWrapper<Shortagepenalty> queryWrapper = new QueryWrapper<Shortagepenalty>();
		queryWrapper.eq("DEPTNO", deptno);
		queryWrapper.eq("SUPPLIERCODE", suppliercode);
		queryWrapper.orderByDesc("USCALE");
		List <Shortagepenalty> shortagepenaltyList = shortagepenaltyMapper.selectList(queryWrapper);
		
		if (shortagepenaltyList.size() > 0) {
			lastyear = formatter.format(shortagepenaltyList.get(0).getLastyear());
			shortagepenaltyList.forEach(action -> {
				String lscale = formatter.format(action.getLscale());
				String penalty = action.getLevelpenalty().equals("X") ? action.getLevelpenalty() : formatter.format(Float.parseFloat(action.getLevelpenalty()));
				String uscale = formatter.format(action.getUscale());
				StringBuffer bf = new StringBuffer();
				bf.append(lscale).append("|").append(uscale).append("|").append(penalty);
				list.add(bf.toString());
			});
			StringBuffer bf = new StringBuffer();
			bf.append(lastyear);
			list.add(bf.toString());
		}
		//加入最後前一年到貨率
		result.addResult("depCname",deptno);
		result.addResult("dataList", list);
		return result;
	}

	/**
	 * 建立時間圖檔(pdf)
	 * @param params
	 * @return
	 * @throws Exception 
	 */
	public  byte[] createContranctDateImage(String dataid) throws Exception{  
		logger.info("建立資料夾 ==="+dataid);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		ByteArrayOutputStream baos = null;
		byte[] pic = null;
		int width = 4500;
		int height = 90;
		String pattern = "%02d";
		String dirPath = "";
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH) + 1;
		int day = cal.get(Calendar.DAY_OF_MONTH);
		// (yyyy) | (mm)|" (dd)"
		String str = "Execution Date 簽約日 :   " + year + "  /  " + String.format(pattern, month) + "  / "
				+ String.format(pattern, day);
		File file = null;

		java.awt.Font font = new java.awt.Font("標楷體", java.awt.Font.BOLD, 100);
		BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics2D g2 = (Graphics2D) bi.getGraphics();

		g2.setBackground(Color.WHITE);
		g2.setClip(0, 0, width, height);
		g2.setColor(Color.white);
		g2.fillRect(0, 0, width, height);
		g2.setColor(Color.black);
		g2.setFont(font);//

		FontRenderContext context = g2.getFontRenderContext();
		Rectangle2D bounds = font.getStringBounds(str, context);
		double x = (width - bounds.getWidth()) / 2;
		double y = (height - bounds.getHeight()) / 2;
		double ascent = -bounds.getY();
		double baseY = y + ascent;
		// 繪製字串
		g2.drawString(str, (int) x, (int) baseY);
		String path = "";
		try {
			path = DOWMLOAD_PDF_URL + dataid;
			file = new File(path);
			if (!file.exists() && !file.isDirectory()) {
				file.mkdir();
			}
			dirPath = path;
			path +=  "\\ContranctDate" + sdf.format(new Date()) + ".png";
			file = new File(path);
			ImageIO.write(bi, "png", file);
			BufferedImage bufferedImage = ImageIO.read(file);
			baos = new ByteArrayOutputStream();
			ImageIO.write(bufferedImage, "jpg", baos);
			baos.flush();
			pic = baos.toByteArray();
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			baos.close();
	        FileUtils.forceDelete(new File(dirPath));
		}

		return pic;
	}  
	
	/**
	 * 取得合約前次紀錄
	 * @param year
	 * @param module
	 * @param disp
	 * @param deptno
	 * @param supplierid
	 * @return
	 * @throws Exception
	 */
	public String getBeforeConJson(String year, String module, String disp, String deptno, String supplierid) throws Exception {
		String result = "";
		Designermgr designermgr = null;
		StringBuffer esSql = new StringBuffer();
		UserInfo userInfo = userContext.getCurrentUser();
		String beginColumnName = "data.docdetail.resultdata";
		String indexName = year + "_" + module + "_qryrec_doc";
		
		List<Designermgr> designermgrList = getDesignermgr(disp, module, "", "");
		if(designermgrList != null && designermgrList.size() > 0) {
			designermgr = designermgrList.get(0);
			esSql.append(" where data.docver = '" + designermgr.getDocver() + "'");
			esSql.append("   and todo not in ('暫存', '新建', '作廢')");
			esSql.append("   and " + beginColumnName + "." + LocaleMessage.getMsg("suppliter.field.supplierid") + "=" + "'" + supplierid + "'");
			esSql.append("   and " + beginColumnName + "." + LocaleMessage.getMsg("contract.field.section") + "=" + "'" + deptno + "'");
			esSql.append("   and " + beginColumnName + "." + LocaleMessage.getMsg("contract.field.dispname") + "=" + "'" + disp + "'");
			esSql.append("   and (" + beginColumnName + "." + LocaleMessage.getMsg("contract.field.contractor") + "=" + "'" + userInfo.getUserCname() + "' or "+beginColumnName+ "."+ LocaleMessage.getMsg("contract.field.undertakeuserid") + "=" + "'"+ userInfo.getUserCname() +"')");
  			esSql.append(" order by " + beginColumnName + "." + LocaleMessage.getMsg("contract.field.updtime") + " desc");
			List<String> resultJson = ElasticSearchUtil.searchForES(indexName, "*", esSql.toString());
			if(resultJson != null && resultJson.size() > 0) {
				result = getContractorJson(resultJson);
				
			}else {
				indexName = (Integer.parseInt(year) - 1)  + "_" + module + "_qryrec_doc";
				resultJson = ElasticSearchUtil.searchForES(indexName, "*", esSql.toString());
				if(resultJson != null && resultJson.size() > 0) {
					result = getContractorJson(resultJson);
				}
			}
		}
		
		//清空 "檢核規則" by max
		if(StringUtils.isNotBlank(result)) {			
			DocumentContext documentContext = JsonPath.parse(result);
			JsonPath jsonPath = null;
			for (int i = 1; i <= 8; i++) {
				jsonPath = JsonPath.compile("$..docdetail[?(@.displayname=='檢核規則" + i + "')].value");
				documentContext.set(jsonPath, "");
			}

			result = documentContext.jsonString();
		}			
		return result;

	}
	
	/**
	 * 確認是否有前次合約紀錄
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public ProcessResult getBeforeContract(Map<String, Object> params) throws Exception {
		ProcessResult result = new ProcessResult();
		String module = MapUtils.getString(params, "module");
		String disp = MapUtils.getString(params, "dispName");
		String deptno = MapUtils.getString(params, "section");
		String supplierid = MapUtils.getString(params, "supplierid");
		String section = MapUtils.getString(params, "section");
		String year = MapUtils.getString(params, "year");
		String json = "";
		Suppliermaster suppliermaster = null;

		if (params != null && params.size() > 0) {
			suppliermaster = commonService.getSupplierData(supplierid, section);
			if(suppliermaster == null) {
				result.setStatus(ProcessResult.NG);
				result.addMessage("查無供應商資料");
				return result;
			}
			
			ProcessResult checkRs = checkContractInsertData(params, suppliermaster);
			if (checkRs.getStatus() == ProcessResult.NG) {
				result.setStatus(checkRs.getStatus());
				result.setMessages(checkRs.getMessages());
				return result;
			}
			
			json = getBeforeConJson(year, module, disp, deptno, supplierid);

			if(StringUtils.isBlank(json)) {
				result.addResult("isJson", "N");
			} else {
				result.addResult("isJson", "Y");
				result.addMessage("有前次合約紀錄，是否沿用?");
			}

			result.setStatus(ProcessResult.OK);
		} else {
			result.setStatus(ProcessResult.NG);
			result.addMessage(MSG_KEY.PARAMS_EMPTY.getMessage());
			return result;
		}
		return result;
	}

	/**
	 *回傳檔案位置byte陣列
	 * @param params
	 * @param json
	 * @param fileUpload1
	 * @param fileUpload2
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public ProcessResult getSignFilebyte(String json, Map<String, Object> params, byte[] fileUpload1, byte[] fileUpload2) throws Exception {
		ProcessResult result = new ProcessResult();
		List<Map<String, Object>> imageInfolist = new ArrayList<Map<String, Object>>();
		Map<String, Object> data = new HashMap<String, Object>();
		String filePath = "";
		String fileName = "";
		String contractno = MapUtils.getString(params, "dataid");

		result.setStatus(ProcessResult.NG);
		if (StringUtils.isNotBlank(json)) {
			data = new Gson().fromJson(json, HashMap.class);
		}
		imageInfolist = setPicByte(params, fileUpload1, fileUpload2);
		data.put("imageInfo", imageInfolist);

		// 取得基底檔案
		QueryWrapper<Contractfile> queryWrapper = new QueryWrapper<Contractfile>();
		queryWrapper.eq("DATAID", contractno);
		queryWrapper.eq("TYPE", "02");
		queryWrapper.eq("FILETYPE", "合約主檔");
		queryWrapper.orderByDesc("SERNO");
		List<Contractfile> contravtfileList = contractfileMapper.selectList(queryWrapper);
		if (contravtfileList != null && contravtfileList.size() > 0) {
			Contractfile contractfile = contravtfileList.get(0);
			fileName = contractfile.getFliename();
			filePath = contractfile.getFliepath() + "/" + fileName;
			fileName = fileName.replace(".pdf", "");
			data.put("fn", fileName.substring(0, fileName.length() - 2));
		}
		byte[] buffer = null;
		if (new File(filePath).exists()) {
			File file = new File(filePath);
			FileInputStream fis = new FileInputStream(file);
			ByteArrayOutputStream bos = new ByteArrayOutputStream(1000);
			byte[] b = new byte[1000];
			int n;
			while ((n = fis.read(b)) != -1) {
				bos.write(b, 0, n);
			}
			fis.close();
			bos.close();
			buffer = bos.toByteArray();
			data.put("byteFile", buffer);
			result.addResult("signJson", data);
			result.setStatus(ProcessResult.OK);
		} else {
			result.addMessage("無法取得檔案");
		}
		return result;
	}
	
	/**
	 * 寫入簽章
	 * @param params
	 * @param fileUpload1
	 * @param fileUpload2
	 * @return
	 * @throws Exception
	 */
	public List<Map<String,Object>> setPicByte(Map<String, Object> params, byte[] fileUpload1, byte[] fileUpload2) throws Exception {
		List<Map<String, Object>> imageInfolist = new ArrayList<Map<String, Object>>();
		int supplierLlx = Integer.valueOf(XauthPropUtils.getKey("supplier.llx"));
		int legalLlx = Integer.valueOf(XauthPropUtils.getKey("legal.llx"));
		int companychopLly = Integer.valueOf(XauthPropUtils.getKey("companyChop.lly"));
		int representativechopLly = Integer.valueOf(XauthPropUtils.getKey("representativeChop.lly"));
		int legalExecutiondateLlx = Integer.valueOf(XauthPropUtils.getKey("legal.executionDate.llx"));
		int legalExecutiondateLly = Integer.valueOf(XauthPropUtils.getKey("legal.executionDate.lly"));
		int legalExecutiondateUrx = Integer.valueOf(XauthPropUtils.getKey("legal.executionDate.urx"));
		int legalExecutiondateUry = Integer.valueOf(XauthPropUtils.getKey("legal.executionDate.ury"));
		int pictureSize = Integer.valueOf(XauthPropUtils.getKey("picture.size"));
		String dataid = MapUtils.getString(params, "dataid");

		Integer IsSignature = MapUtils.getInteger(params, "IsSignature");
		if (fileUpload1 != null && fileUpload2 != null) {
			if (IsSignature == 2) {
				Map<String, Object> ClientField = new HashMap<String, Object>();
				ClientField.put("imageBytes", fileUpload1);
				ClientField.put("imageSignName", "ClientFieldName3");
				ClientField.put("imagellx", supplierLlx);
				ClientField.put("imagelly", companychopLly);
				ClientField.put("imageurx", supplierLlx + pictureSize);
				ClientField.put("imageury", companychopLly + pictureSize);
				ClientField.put("imageSerialNo", 2);
				ClientField.put("imageSignNote", "N");
				imageInfolist.add(ClientField);
				ClientField = new HashMap<String, Object>();
				ClientField.put("imageBytes", fileUpload2);
				ClientField.put("imageSignName", "ClientFieldName4");
				ClientField.put("imagellx", supplierLlx);
				ClientField.put("imagelly", representativechopLly);
				ClientField.put("imageurx", supplierLlx + pictureSize);
				ClientField.put("imageury", representativechopLly + pictureSize);
				ClientField.put("imageSerialNo", 3);
				ClientField.put("imageSignNote", "N");
				imageInfolist.add(ClientField);
			} else if (IsSignature == 1) {
				Map<String, Object> ClientField = new HashMap<String, Object>();
				ClientField.put("imageBytes", fileUpload1);
				ClientField.put("imageSignName", "ClientFieldName1");
				ClientField.put("imagellx", legalLlx);
				ClientField.put("imagelly", companychopLly);
				ClientField.put("imageurx", legalLlx + pictureSize);
				ClientField.put("imageury", companychopLly + pictureSize);
				ClientField.put("imageSerialNo", 4);
				ClientField.put("imageSignNote", "N");
				imageInfolist.add(ClientField);

				ClientField = new HashMap<String, Object>();
				ClientField.put("imageBytes", fileUpload2);
				ClientField.put("imageSignName", "ClientFieldName2");
				ClientField.put("imagellx", legalLlx);
				ClientField.put("imagelly", representativechopLly);
				ClientField.put("imageurx", legalLlx + pictureSize);
				ClientField.put("imageury", representativechopLly + pictureSize);
				ClientField.put("imageSerialNo", 5);
				ClientField.put("imageSignNote", "N");
				imageInfolist.add(ClientField);
				// 插入合約產生時間圖片
				byte[] pic = createContranctDateImage(dataid);
				ClientField = new HashMap<String, Object>();
				ClientField.put("imageBytes", pic);
				ClientField.put("imageSignName", "ClientFieldName5");
				ClientField.put("imagellx", legalExecutiondateLlx);
				ClientField.put("imagelly", legalExecutiondateLly);
				ClientField.put("imageurx", legalExecutiondateUrx);
				ClientField.put("imageury", legalExecutiondateUry);
				ClientField.put("imageSerialNo", 6);
				ClientField.put("imageSignNote", "Y");
				imageInfolist.add(ClientField);
			}
		}

		return imageInfolist;
	}
	
	@SuppressWarnings("unchecked")
	public ProcessResult saveSignFile(String json, Map<String, Object> params) throws Exception {
		ProcessResult result = new ProcessResult();
		result.setStatus(ProcessResult.NG);
		Map<String, Object> jsonMap = null;
		List<Map<String, Object>> fileInfos = null;
		String fileName = "";
		byte[] fileBytes = null;
		OutputStream output = null;
		File file = null;
		String dataid = MapUtils.getString(params, "dataid");
		String signName = MapUtils.getString(params, "signName");
		String savePath = "";
		Contractfile insertContractfile = new Contractfile();
		
		if (StringUtils.isNotBlank(json)) {
			jsonMap = new Gson().fromJson(json, HashMap.class);
			fileInfos = (List<Map<String, Object>>) jsonMap.get("fileInfos");
			for(int i = 0 ; i < fileInfos.size() ; i ++) {
				fileName = MapUtils.getString(fileInfos.get(i), "fileName");
				fileBytes = Base64.getDecoder().decode(MapUtils.getString(fileInfos.get(i), "fileBytes"));
				// pdf落檔
				savePath = FILE_URL + dataid + "/" + fileName;
				file = new File(savePath);
				output = new FileOutputStream(file);
				output.write(fileBytes);
				output.close();
				// 存入DB CONTRACTFILE
				insertContractfile.setSerno(null);
				insertContractfile.setDataid(dataid);
				insertContractfile.setFliepath(FILE_URL + dataid);
				insertContractfile.setIsdownload("是");
				insertContractfile.setType("02");
				insertContractfile.setFiletype("合約主檔");
				insertContractfile.setFliename(fileName);
				insertContractfile.setCreatedate(new Timestamp(new Date().getTime()));
				insertContractfile.setCreateuser(getCreOrUpdUser(null));
				if(i == 0) {
					insertContractfile.setFlienote(signName + "加簽");
				} else {
					insertContractfile.setFlienote(signName + "加簽完成");
				}
				contractfileMapper.insert(insertContractfile);
				result.setStatus(ProcessResult.OK);
			}
			
		} else {
			result.addMessage("查無憑證資料");
		}
		
		return result;
	}
	
	/**
	 * 合約草稿查詢
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public GridResult selectDraftConList(Map<String, Object> params) throws Exception {
		GridResult gridResult = null;
		UserInfo userInfo = userContext.getCurrentUser();
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		StringBuffer esSql = null;
		String beginColumnName = "data.docdetail.resultdata";
		String module = MapUtils.getString(params, "module");
		String suppliercode = MapUtils.getString(params, "suppliercode"); // 供應商廠編
		String suppliername = MapUtils.getString(params, "suppliername");
		String suppliergui = MapUtils.getString(params, "suppliergui");
		String deptno = MapUtils.getString(params, "deptno");
		String contractBgnDate = MapUtils.getString(params, "contractBgnDate");
		String contractEndDate = MapUtils.getString(params, "contractEndDate");
		String indexName = "";
		String todo = "";
		String dataid = "";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		
		List<XauthSysCode> deptCodeList = getSysCode("DEPT_CODE"); // 課別資料
		Map<String, Object> resultMap = null;
		Map<String, Object> dataidMap = new HashMap<String, Object>(); // 組dataid的map (key : indexname, value : dataid)
		
		// 至Contractmaster查詢該承辦人員新建合約
		Map<String, Object> compParams = new HashMap<String, Object>();
		if(!StringUtils.equals(userContext.getCurrentUser().getUserType(), USER_TYPE.SYS_ADMIN.getCode())) {
			compParams.put("userid",userInfo.getUserId());
		}
		compParams.put("contractmodel", module);
		compParams.put("contractBgnDate", contractBgnDate);
		compParams.put("contractEndDate", contractEndDate);
		List<Map<String, Object>> contractmasterList = contractMapper.selectDraftConList(compParams);
		
		// 組dataid
		for(Map<String, Object> contractmaster : contractmasterList) {
			dataid = MapUtils.getString(contractmaster, "DATAID");
			indexName = MapUtils.getString(contractmaster, "INDEXNAME");
			// 組index資料
			dataidMap.put(dataid, indexName);
		}
		
		// 依每筆合約編號dataid至ES查詢
		for(String dataidKey : dataidMap.keySet()) {
			indexName = MapUtils.getString(dataidMap, dataidKey);
			esSql = new StringBuffer();
			// 組Elasticsearch條件
			esSql.append(" where " + "_id = '" + dataidKey + "'");
			if(StringUtils.isNotBlank(suppliercode)) {
				esSql.append("and " + beginColumnName + "." + LocaleMessage.getMsg("suppliter.field.suppliercode") + "=" + "'" + suppliercode + "'");
			}
			if(StringUtils.isNotBlank(suppliername)) {
				esSql.append("and " + beginColumnName + "." + LocaleMessage.getMsg("suppliter.field.suppliercname") + "=" + "'" + suppliername + "'");
			}
			if(StringUtils.isNotBlank(suppliergui)) {
				esSql.append("and " + beginColumnName + "." + LocaleMessage.getMsg("suppliter.field.suppliergui") + "=" + "'" + suppliergui + "'");
			}
			if(StringUtils.isNotBlank(deptno)) {
				esSql.append("and " + beginColumnName + "." + LocaleMessage.getMsg("contract.field.section") + "=" + "'" + deptno + "'");
			}
			
			List<String> resultJson = ElasticSearchUtil.searchForES(indexName, "todo," + beginColumnName, esSql.toString());
			if(resultJson != null && resultJson.size() > 0) {
				for(String json : resultJson) {
					// 將查詢後的json放入grid中
					ReadContext readContext = JsonPath.parse(json);
					todo = readContext.read("$.todo");
					List<Map<String, Object>> resultdataList = readContext.read("$..resultdata");
					if(resultdataList.size() > 0) {
						resultMap = resultdataList.get(0);
						resultMap.put("todo", todo);
						resultMap.put("indexName", indexName);
						resultMap.put(LocaleMessage.getMsg("contract.field.docstatus"), "編輯中");
						
						// 取得課別資料中文名稱
						for(XauthSysCode deptCode : deptCodeList) {
							if(StringUtils.equals(deptCode.getCode(), MapUtils.getString(resultMap, LocaleMessage.getMsg("contract.field.section")))) {
								resultMap.put("deptCName", deptCode.getCname());
								resultMap.put("deptNo", deptCode.getCode());
							}
						}

						// 轉換送審日期格式
						String updtime = MapUtils.getString(resultMap, LocaleMessage.getMsg("contract.field.updtime"));
						resultMap.put(LocaleMessage.getMsg("contract.field.updtime"), sdf.format(sdf.parse(updtime)));
						resultList.add(resultMap);
					}
				}
			}
		}
		// 排序及分頁
		gridResult = this.grid(params);
		ElasticSearchUtil.sortPagination(gridResult, resultList, params);
		
		return gridResult;
	}
	
	/**
	 * 取得代理人
	 * String userid
	 * @return
	 */
	public String getAgentUserId (String userid) {
		String agentUserId="";
		QueryWrapper<XauthRoleAgentUser>usrQueryWrapper = new QueryWrapper<XauthRoleAgentUser>();
		usrQueryWrapper.eq("USER_ID",userid);
		List<XauthRoleAgentUser>userlist = xauthRoleAgentUserMapper.selectList(usrQueryWrapper);
		if(userlist.size() > 0) {
			agentUserId = userlist.get(0).getAgentUserId();
		}
		else 
			agentUserId = "查無代理人";
		return agentUserId;
	}
	
	/**
	 * 取得代理人 原始USERID
	 * List
	 * @return
	 */
	public List<String> getPrincipalUserId (String agentUserId) {
		List<String> resultlist = new ArrayList<String>();
		QueryWrapper<XauthRoleAgentUser> usrQueryWrapper = new QueryWrapper<XauthRoleAgentUser>();
		usrQueryWrapper.eq("AGENT_USER_ID", agentUserId);
		List<XauthRoleAgentUser> userlist = xauthRoleAgentUserMapper.selectList(usrQueryWrapper);
		if (userlist.size() > 0) {
			for (XauthRoleAgentUser userinfo : userlist) {
				resultlist.add(userinfo.getUserId());
			}
		}
		return resultlist;
	}
	/**
	 * 取得代理人 原始USERID
	 * String agentUserId
	 * @return
	 */
	public String getPrincipalUserId (String agentUserId,String userId) {
		String resultId = agentUserId;
		QueryWrapper<XauthRoleAgentUser> usrQueryWrapper = new QueryWrapper<XauthRoleAgentUser>();
		usrQueryWrapper.eq("USER_ID", userId);
		usrQueryWrapper.eq("AGENT_USER_ID", agentUserId);
		List<XauthRoleAgentUser> userlist = xauthRoleAgentUserMapper.selectList(usrQueryWrapper);
		if (userlist.size() > 0) {
			resultId = userId;
		}
		return resultId;
	}
	
	/**
	 * 前次合約持有人判斷
	 * @param params
	 * @return
	 * @throws Exception
	 */
	private String getContractorJson(List<String> resultJson) {
		String result = "";
		UserInfo userInfo = userContext.getCurrentUser();
		for(String resultdata : resultJson) {
			ReadContext readContext = JsonPath.parse(resultdata);
			List<Map<String, Object>> resultdataList = readContext.read("$..resultdata");
			if(resultdataList.size() > 0) {
				for(Map<String, Object> resultMap: resultdataList) {
					String contractor = "";
					String undertakeUserId = MapUtils.getString(resultMap, LocaleMessage.getMsg("contract.field.undertakeuserid"));
					if(StringUtils.isBlank(undertakeUserId)) {
						contractor = MapUtils.getString(resultMap, LocaleMessage.getMsg("contract.field.contractor"));
					}
					else {
						contractor = undertakeUserId;
					}
					
					if(StringUtils.equals(contractor, userInfo.getUserCname())) {
						result = resultdata;
						break;
					}
				}
			}
		}
		return result; 
	}
	
	/**
	 * 判斷是否為合約承辦人
	 * @return
	 */
	private boolean isTaskApply (String username,String agentUserId,String taskname) {
		UserInfo userInfo = userContext.getCurrentUser();
		boolean result = false;
		
		if((StringUtils.equals(username, userInfo.getUserCname()) || StringUtils.equals(agentUserId, userInfo.getUserId())) && StringUtils.equals(taskname, "申請人") ) {
			result = true ;
		}
  		return result ;
	}
	
	/**
	 * 備份JSON至本機
	 * @return
	 */
	private void createJsonFile(String json,String dataid,String action) throws IOException {
		String savePath = RESULT_JSON_URL;
		SimpleDateFormat sdFormat = new SimpleDateFormat("yyyyMMdd");
		String date = sdFormat.format(new Date());
		BufferedWriter fw = null;
		savePath = savePath + date + "//";
		File file = new File(savePath);
		if (!file.exists()) {
			file.mkdir();
		}
		try {
			sdFormat = new SimpleDateFormat("yyyyMMddHHmmss");
			date = sdFormat.format(new Date());
			
			String filename = "(" + action + ")reg_qryrec_doc_" + dataid + "_" + date + ".json";
			fw = new BufferedWriter(new FileWriter(savePath + filename));
			fw.write(json);
			fw.flush();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			fw.close();
		}
	}

}

