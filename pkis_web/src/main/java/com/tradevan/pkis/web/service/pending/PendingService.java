package com.tradevan.pkis.web.service.pending;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.ReadContext;
import com.tradevan.handyflow.bean.DocStateBean;
import com.tradevan.handyflow.service.FlowQueryService;
import com.tradevan.mapper.contract.dao.ContractMapper;
import com.tradevan.mapper.pkis.dao.ContractmasterMapper;
import com.tradevan.mapper.pkis.dao.ReviewsetdataconfMapper;
import com.tradevan.mapper.pkis.dao.SystemparamMapper;
import com.tradevan.mapper.pkis.model.Reviewsetdataconf;
import com.tradevan.mapper.xauth.dao.XauthRoleUserMapper;
import com.tradevan.mapper.xauth.dao.XauthTransferMapper;
import com.tradevan.pkis.web.service.contract.ContractService;
import com.tradevan.pkis.web.util.ElasticSearchUtil;
import com.tradevan.pkis.web.util.JsonUtil;
import com.tradevan.xauthframework.core.common.LocaleMessage;
import com.tradevan.xauthframework.core.security.UserInfo;
import com.tradevan.xauthframework.web.service.DefaultService;

@Service("PendingService")
@Transactional(rollbackFor = Exception.class)
public class PendingService extends DefaultService {
	
	@Autowired
	ContractmasterMapper contractmasterMapper;
	
	@Autowired
	ReviewsetdataconfMapper reviewsetdataconfMapper;
	
	@Autowired
	SystemparamMapper systemparamMapper;
	
	@Autowired
	XauthRoleUserMapper xauthRoleUserMapper;
	
	@Autowired
	private FlowQueryService flowQueryService;
	
	@Autowired
	ContractService contractService;
	
	@Autowired
	ContractMapper contractMapper;
	
	@Autowired
	XauthTransferMapper xauthTransferMapper ;
	
	public Map<String, Object> getFlowVeiwData(Map<String, Object> params) throws Exception{
		Map<String, Object> result = new HashMap<String, Object>();
		List<DocStateBean> list = new ArrayList<DocStateBean>();
		UserInfo userInfo = userContext.getCurrentUser();
		String applyNo = MapUtils.getString(params, "applyNo");
		String flowid = MapUtils.getString(params, "formId");
		String nowUserId = userInfo.getUserId();
		String modal = "";
		String deptNo = "";
		int needSignature = 0;
		String indexname = "";
		String nowuserid = "" ;
		
		// Act & Assert
		//1.採移轉合約
		Map<String, Object> compParams = new HashMap<String, Object>();
		compParams.put("userid", userInfo.getUserId());
		List<Map<String, Object>> searchlist = xauthTransferMapper.searchtransferUserId(compParams);
		if(searchlist.size() > 0) {
			for(Map<String, Object> map : searchlist) {
				String searchid = MapUtils.getString(map, "USERID");
				list = flowQueryService.fetchToDoListBy(searchid, flowid, APP_ID, true, false);
				if(list.size() > 0) {
					break;
				}
			}
		}
		
		//2.採原始單據
		if((list.size() == 0 )) {
			logger.info("原始單據ID ===" + nowUserId);
			list = flowQueryService.fetchToDoListBy(nowUserId, flowid, APP_ID, true, false);
		}
		
		//3.採取代理人模式
		if(list.size() == 0 ){
			List<String> agentlist = contractService.getPrincipalUserId(nowUserId);
			if(agentlist.size() > 0) {
				for(String userid : agentlist) {
					logger.info("代理人單據ID ==="+userid);
					list = flowQueryService.fetchToDoListBy(userid, flowid, APP_ID, true, false);
					if(list.size() > 0) {
						break ;
					}
				}
			}
			
		}
		if((list.size() == 0)) {
			logger.info("代辦查無此單據");
			result.put("rtnCode", -1);
			result.put("message", "查無合約流程資料");
			return result;
		}		

		DocStateBean docState = list.get(0);
		Map<String, Object> contractmaster = new HashMap<String, Object>();
		contractmaster.put("dataid", applyNo);
		//取得selectContractMaster資訊 NOWUSERID 為合約建立人 為判斷是否為承辦人或承辦代理人
		List<Map<String, Object>> contractmasterList = contractMapper.selectContractMasterByTask1(contractmaster);
		if(contractmasterList.size() > 0) {
			modal = MapUtils.getString(contractmasterList.get(0), "CONTRACTMODEL");
			indexname = MapUtils.getString(contractmasterList.get(0), "INDEXNAME");
			nowuserid = MapUtils.getString(contractmasterList.get(0), "NOWUSERID") ;

			String esJson = ElasticSearchUtil.serachById(applyNo , indexname);

			esJson = JsonUtil.jsonSkipToString(esJson);
			
			ReadContext json = JsonPath.parse(esJson);
			List<Map<String, Object>> resultdataList = json.read("$.data[?(@.datatype == '基本資料')].docdetail[?(@.resultdata)].resultdata");
			
			logger.info("ES Json ResultData === " + resultdataList.toString());
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
			if(docState != null) {
				if(StringUtils.equals("SC", modal) && StringUtils.equals(docState.getTaskDesc(), "法務審核")) {
					needSignature = 1;
				}
				
				//法務NSC
				if(StringUtils.equals("NSC", modal) && StringUtils.equals(docState.getTaskDesc(), "法務簽章")) {
					needSignature = 1;
				}
			}
			
			if(contractmasterList.size() != 0) {
				result.put("dataId", applyNo);
				result.put("dataType", "基本資料");
				result.put("signature", needSignature);
				result.put("indexName", indexname);
				result.put("currentUserCname", userInfo.getUserCname());
				result.put("idenId", userInfo.getIdenId());
				result.put("modal", modal);
				result.put("currentUserid", userContext.getCurrentUser().getUserId());
				result.put("contractorAgentUserId", contractService.getAgentUserId(nowuserid));
				
				result.put("taskName", docState.getTaskName());
			}
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
}
