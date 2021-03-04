package com.tradevan.pkis.web.service.pending;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.ReadContext;
import com.tradevan.handyflow.bean.DocStateBean;
import com.tradevan.handyflow.service.FlowQueryService;
import com.tradevan.mapper.contract.dao.ContractMapper;
import com.tradevan.mapper.pkis.dao.ContractmasterMapper;
import com.tradevan.mapper.pkis.dao.ReviewsetdataconfMapper;
import com.tradevan.mapper.pkis.dao.SystemparamMapper;
import com.tradevan.mapper.pkis.model.Contractmaster;
import com.tradevan.mapper.pkis.model.Reviewsetdataconf;
import com.tradevan.mapper.pkis.model.Systemparam;
import com.tradevan.mapper.xauth.dao.XauthSysCodeMapper;
import com.tradevan.mapper.xauth.dao.XauthTransferMapper;
import com.tradevan.mapper.xauth.model.XauthSysCode;
import com.tradevan.pkis.web.service.common.CommonService;
import com.tradevan.pkis.web.service.contract.ContractService;
import com.tradevan.pkis.web.service.xauth.XauthTransferService;
import com.tradevan.pkis.web.util.ElasticSearchUtil;
import com.tradevan.pkis.web.util.JsonUtil;
import com.tradevan.xauthframework.common.utils.ConvertUtils;
import com.tradevan.xauthframework.core.common.LocaleMessage;
import com.tradevan.xauthframework.core.security.UserInfo;
import com.tradevan.xauthframework.dao.bean.GridResult;
import com.tradevan.xauthframework.web.service.DefaultService;

@Service("SupervisorPendingService")
@Transactional(rollbackFor = Exception.class)
public class SupervisorPendingService extends DefaultService {
	
	@Autowired
	ContractmasterMapper contractmasterMapper;
	
	@Autowired
	ReviewsetdataconfMapper reviewsetdataconfMapper;
	
	@Autowired
	SystemparamMapper systemparamMapper;
	
	@Autowired
	XauthSysCodeMapper xauthSysCodeMapper;
	
	@Autowired
	ContractMapper contractMapper;
		
	@Autowired
	private FlowQueryService flowQueryService;
	
	@Autowired
	private ContractService contractService;
	
	@Autowired
	XauthTransferService xauthTransferService;
	
	@Autowired
	XauthTransferMapper xauthTransferMapper;
	
	@Autowired
	CommonService commonService;
	
	/**
	 * 編輯頁面
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> getFlowVeiwData(Map<String, Object> params) throws Exception{
		Map<String, Object> result = new HashMap<String, Object>();
		List<Systemparam> systemparamList = new ArrayList<Systemparam>();
		UserInfo userInfo = userContext.getCurrentUser();
		String applyNo = MapUtils.getString(params, "applyNo");
		String flowid = MapUtils.getString(params, "formId");
		String orgFlowid = flowid.replace(applyNo,"");
		String nowUserId = userInfo.getUserId();
		String modal = "";
		String deptNo = "";
		int needSignature = 0;
		Contractmaster contractmaster = null;
		
		// Act & Assert
		List<DocStateBean> list = flowQueryService.fetchToDoListBy(nowUserId, flowid, APP_ID, true, false);
		//改查代理人
		if(list.size() == 0){
			List<String> agentlist = contractService.getPrincipalUserId(nowUserId);
			if(agentlist.size() > 0) {
				for(String userid : agentlist) {
					list = flowQueryService.fetchToDoListBy(userid, flowid, APP_ID, true, false);
					if(list.size() > 0) {
						break ;
					}
				}
			}
			
		}
		//查詢合約轉移人
		if(list.size() == 0) {
			Map<String , String> transferuserids = xauthTransferService.getTranferOriUserids(nowUserId ,applyNo);
			for(String userid : transferuserids.keySet()) {
				list = flowQueryService.fetchToDoListBy(userid, flowid, APP_ID, true, false);
				if(list.size() > 0) {
					break;
				}
			}
		}
		
		if((list.size() == 0)) {
			result.put("rtnCode", -1);
			result.put("message", "查無合約流程資料");
			return result;
		}
		DocStateBean docState = list.get(0);
		contractmaster = commonService.getContractmasterData(applyNo);
		if(contractmaster != null) {
			modal = contractmaster.getContractmodel();
			
			String esJson = ElasticSearchUtil.serachById(contractmaster.getDataid(), contractmaster.getIndexname());
			esJson = JsonUtil.jsonSkipToString(esJson);
			ReadContext json = JsonPath.parse(esJson);
			List<Map<String, Object>> resultdataList = json.read("$.data[?(@.datatype == '基本資料')].docdetail[?(@.resultdata)].resultdata");
			for(Map<String, Object> resultdata : resultdataList) {
				deptNo = MapUtils.getString(resultdata, LocaleMessage.getMsg("contract.field.section"));
			}
			
			if(StringUtils.isBlank(deptNo)) {
				logger.info("查無ResultData");
			}
			
			//供應商
			if(StringUtils.equals("999999999", userInfo.getIdenId())) {
				needSignature = 2;
			}
			
			//末關卡
			//法務CS
			if(StringUtils.equals("SC", modal)) {
				QueryWrapper<Systemparam> systemparamQueryWrapper = new QueryWrapper<Systemparam>();
				systemparamQueryWrapper.eq("DEPTNO", deptNo);
				systemparamQueryWrapper.eq("CONTRACTMODEL", modal);
				systemparamQueryWrapper.eq("ACTIONTYPE", "02");
				systemparamList = systemparamMapper.selectList(systemparamQueryWrapper);
				
				if(StringUtils.equals(userInfo.getUserId(), systemparamList.get(0).getUserids())) {
					needSignature = 1;
				}
			}
			
			//法務NSC
			if(StringUtils.equals("NSC", modal)) {
				QueryWrapper<Systemparam> systemparamQueryWrapper = new QueryWrapper<Systemparam>();
				systemparamQueryWrapper.eq("FLOWID", orgFlowid);
				systemparamQueryWrapper.eq("CONTRACTMODEL", modal);
				systemparamQueryWrapper.eq("ACTIONTYPE", "02");
				systemparamList = systemparamMapper.selectList(systemparamQueryWrapper);
				
				if(systemparamList.size() > 0) {
					if(StringUtils.equals(userInfo.getUserId(), systemparamList.get(0).getUserids()) && !StringUtils.equals("Task2", docState.getNowTaskId())) {
						needSignature = 1;
					}
				}
			}
			
			result.put("dataId", applyNo);
			result.put("dataType", "基本資料");
			result.put("signature", needSignature);
			result.put("indexName", contractmaster.getIndexname());
			result.put("currentUserCname", userInfo.getUserCname());
			result.put("idenId", userInfo.getIdenId());
			result.put("modal", modal);
			result.put("currentUserid", userContext.getCurrentUser().getUserId());
			result.put("contractorAgentUserId", contractService.getAgentUserId(contractmaster.getCreateuser().split(":")[1]));
			result.put("taskName", docState.getTaskName());
		} else {
			result.put("rtnCode", -1);
			result.put("message", "查無此合約編號資料");
			return result;
		}
		
		return result;
	}
	
	public List<Reviewsetdataconf> sortOutReviewSetDataList(List<Reviewsetdataconf> reviewsetdataconfList) {
		List<Reviewsetdataconf> resultReviewSetDataList = new ArrayList<Reviewsetdataconf>();
		
		int size = reviewsetdataconfList.size();
		int j = 1;
		
		if(size == 0) {
			return null;
		}
		
		for(int i = 0 ; i < size ; i++) {
			if(j == reviewsetdataconfList.get(i).getSequence()) {
				resultReviewSetDataList.add(reviewsetdataconfList.get(i));
			}
			j++;
		}
		
		return resultReviewSetDataList;
	}
	
	/**
	 * 待辦清單Grid
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public GridResult selectToDoList(Map<String, Object> params) throws Exception {
		GridResult gridResult = this.grid(params);
		String beginColumnName = "data.docdetail.resultdata";
		List<DocStateBean> docStateBeanList = null;
		List<Map<String, Object>> list = null;
		Map<String, Object> resultMap = null;
		Map<String, Object> dataMap = new HashMap<String, Object>();
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		List<String> agentusers = null;
		Set<String>useridlist = new HashSet<String>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		StringBuffer esSql = new StringBuffer();
		Contractmaster contractmaster = null;
		XauthSysCode xauthSysCode = null;
		
		//查詢條件
		String uiCheckResults = MapUtils.getString(params, "checkResult");
		String uiApplyNo = MapUtils.getString(params, "applyNo");
		String uiCheckResult = "";
		
		UserInfo userInfo = userContext.getCurrentUser();
		
		//原始單據加入
		useridlist.add(userInfo.getUserId());
		//原始單據代理人加入
		agentusers = contractService.getPrincipalUserId(userInfo.getUserId());
		if(agentusers.size() > 0) {
			for(String userid : agentusers) {
				useridlist.add(userid);		
			}
		}
		//移轉清單加入(含代理人)
		Map<String, Object> compParams = new HashMap<String, Object>();
		compParams.put("userid", userInfo.getUserId());
		List<Map<String, Object>> searchlist = xauthTransferMapper.searchtransferUserId(compParams);
		if(searchlist.size()>0) {
			for(Map<String, Object> map:searchlist) {
				String searchid = MapUtils.getString(map, "USERID");
				useridlist.add(searchid);
			}
		}
		//遍歷此user相關單據
		for(String userid : useridlist) {
			docStateBeanList = flowQueryService.fetchToDoListBy(userid, APP_ID, true);
			if (docStateBeanList != null) {
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

		//20200922 待辦清單調整
		for(Map<String, Object> data : list) {
			String applyNo = MapUtils.getString(data, "applyNo");
			String flowStatus = MapUtils.getString(data, "flowStatus");
			String taskName = MapUtils.getString(data, "taskName");
			
			//判斷是否有移轉合約
			if(!xauthTransferService.isNowContractTaskUser(applyNo,userInfo.getUserId())) {
				continue;
			}
			contractmaster = commonService.getContractmasterData(applyNo);
			
			if(contractmaster != null) {
				String indexname = contractmaster.getIndexname();
				String model = contractmaster.getContractmodel();
				esSql = new StringBuffer();
				xauthSysCode = commonService.getSysCodeData("CONTRACT_MODE_CODE", model);
				dataMap.put("contractmodel", xauthSysCode.getCname());
				esSql.append(" where " + beginColumnName + "." + LocaleMessage.getMsg("contract.field.no") + "='" + applyNo + "'");
//				if(StringUtils.isNotBlank(uiCheckStatic)) {
//					esSql.append("and " + beginColumnName + "." + LocaleMessage.getMsg("contract.field.check_static") + "=" + "'" + uiCheckStatic + "'");
//				}
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
							String suppliergui = MapUtils.getString(resultMap, LocaleMessage.getMsg("suppliter.field.suppliergui"));
							String supplierCname= MapUtils.getString(resultMap, LocaleMessage.getMsg("suppliter.field.suppliercname"));
							String checkStatic = MapUtils.getString(resultMap, LocaleMessage.getMsg("contract.field.check_static"));
							String checkResult = MapUtils.getString(resultMap, LocaleMessage.getMsg("contract.field.check_result"));
							String checkResult_text = MapUtils.getString(resultMap, LocaleMessage.getMsg("contract.field.check_result_text"));

							boolean isUiApplyNoBlank = true;
							
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
							dataMap.put("flowStatus", flowStatus);

							if(StringUtils.isNotBlank(uiApplyNo) && StringUtils.equals(applyNo, uiApplyNo)) {
								isUiApplyNoBlank = false;
								dataMap.put("contracttype", MapUtils.getString(resultMap, LocaleMessage.getMsg("contract.field.name")));
								dataMap.put("startDay", sdf.format(sdf.parse(startDay)));
								dataMap.put("deptNo", deptNo);
								dataMap.put("contractor", contractor);
								dataMap.put("suppliergui", suppliergui);
								dataMap.put("supplierCname", supplierCname);
								dataMap.put("checkStatic", checkStatic);
								dataMap.put("checkResult", checkResult);
								dataMap.put("indexname", indexname);
								resultMap = new HashMap<String, Object>();
								resultMap.putAll(data);
								resultMap.putAll(dataMap);
							}else if(!StringUtils.isNotBlank(uiApplyNo)){
								isUiApplyNoBlank = false;
								dataMap.put("contracttype", MapUtils.getString(resultMap, LocaleMessage.getMsg("contract.field.name")));
								dataMap.put("startDay", sdf.format(sdf.parse(startDay)));
								dataMap.put("deptNo", deptNo);
								dataMap.put("contractor", contractor);
								dataMap.put("suppliergui", suppliergui);
								dataMap.put("supplierCname", supplierCname);
								dataMap.put("checkStatic", checkStatic);
								dataMap.put("checkResult", checkResult);
								dataMap.put("indexname", indexname);
								resultMap = new HashMap<String, Object>();
								resultMap.putAll(data);
								resultMap.putAll(dataMap);
							}
							
							if(!isUiApplyNoBlank) {
								if(StringUtils.isNotBlank(uiCheckResults)) {
									for(String uiCheckResultCode : uiCheckResults.split(",")) {
										uiCheckResult = getCheckResultList(uiCheckResultCode).get(0).getCname();
										if(StringUtils.contains(checkResult_text, uiCheckResult)) {
											result.add(resultMap);
											break;
										}
									}
								} else {
									result.add(resultMap);
								}
							}
						}
					}
				}
			}
		}
		
		if(result != null) {
			// 排序及分頁
			gridResult = this.grid(params);
			ElasticSearchUtil.sortPagination(gridResult, result, params);
		}
		
		return gridResult;
	}
	
	/**
	 * 批次審簽核
	 * @List<String> dataList
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> batchSign(List<String> dataList) throws Exception{
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> param = new HashMap<String, Object>();
		Contractmaster contractmaster = null;
		for(String data: dataList) {
			JsonElement jsonElement = new JsonParser().parse(data);
			contractmaster = commonService.getContractmasterData(jsonElement.getAsJsonObject().get("contractNo").getAsString());
			if(contractmaster != null) {
				String indexname = contractmaster.getIndexname();
				String contractNo = contractmaster.getDataid();
				Map<String, Object> map = new HashMap<String, Object>();
				param.put("contractNo", contractNo);
				param.put("dataType", "審核意見");
				param.put("indexName", indexname);
//				param.put("type", "審核");
				param.put("type", "核准");
				param.put("data", (Map<String, Object>) new Gson().fromJson(contractmaster.getJson(), map.getClass()));
				result = contractService.flowDeal(param, "核准", contractNo, false);
//				result = contractService.flowDeal(param, "審核", contractNo, false);
			}
		}
		return result;
	}
	
	/**
	 * 審核評估檢核結果比對
	 * @param code
	 * @return
	 * @throws Exception
	 */
	public List<XauthSysCode> getCheckResultList(String code) throws Exception {
		List<XauthSysCode> result = null;
		QueryWrapper<XauthSysCode> sysCodeWrapper = new QueryWrapper<XauthSysCode>();
		sysCodeWrapper.eq("APP_ID", APP_ID);
		sysCodeWrapper.eq("GP", "CHECK_RESULT_CODE");
		if(StringUtils.isNotBlank(code)) {
			sysCodeWrapper.eq("CODE", code);
		}
		sysCodeWrapper.orderByAsc("ORDER_SEQ");
		result = xauthSysCodeMapper.selectList(sysCodeWrapper);
		
		return result;
	}
	
	/**
	 * 批次駁回
	 * @List<String> dataList
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> btnReject(List<String> dataList) throws Exception{
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> param = new HashMap<String, Object>();
		Contractmaster contractmaster = null;
		for(String data: dataList) {
			JsonElement jsonElement = new JsonParser().parse(data);
			contractmaster = commonService.getContractmasterData(jsonElement.getAsJsonObject().get("contractNo").getAsString());
			if(contractmaster != null) {
				String indexname = contractmaster.getIndexname();
				String contractNo = contractmaster.getDataid();
				Map<String, Object> map = new HashMap<String, Object>();
				param.put("contractNo", contractNo);
				param.put("dataType", "審核意見");
				param.put("indexName", indexname);
				param.put("type", "駁回");
				String jsonData = contractmaster.getJson();
				//清空審核意見欄位
				DocumentContext documentContext = JsonPath.parse(jsonData);
//				JsonPath jsonPath = JsonPath.compile("$.data[?(@.datatype == '審核意見' )].docdetail[?(@.displayname == '審核意見'  && @.uitype == 'label')].value");
//				documentContext.set(jsonPath, "批次駁回");
				jsonData = documentContext.jsonString();

				param.put("data", (Map<String, Object>) new Gson().fromJson(jsonData, map.getClass()));
				result = contractService.flowDeal(param, "駁回", contractNo, false);
			}
		}
		return result;
	}
}
