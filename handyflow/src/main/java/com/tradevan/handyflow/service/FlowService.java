package com.tradevan.handyflow.service;

import java.util.List;
import java.util.Map;

import com.tradevan.apcommon.bean.NameValuePair;
import com.tradevan.aporg.bean.UserDto;
import com.tradevan.handyflow.bean.DocStateBean;
import com.tradevan.handyflow.bean.FlowBean;
import com.tradevan.handyflow.model.flow.LinkAction;

/**
 * Title: FlowService<br>
 * Description: <br>
 * Company: Tradevan Co.<br>
 * 
 * @author Neil Chen
 * @version 1.8.3
 */
public interface FlowService {

	String IS_FLOW_AUTH = "isFlowAuth";
	
	String getNowFlowVersion(String flowId);
	
	String getFlowAdminId(String flowId);
	
	DocStateBean startFlow(FlowBean flowBean, LinkAction action);
	
	DocStateBean apply(DocStateBean docStateBean, LinkAction action);
	
	DocStateBean claim(DocStateBean docStateBean);
	
	DocStateBean process(DocStateBean docStateBean, LinkAction action);
	
	Map<String, Boolean> fetchFlowActionAuth(DocStateBean docStateBean, String userId);
	
	List<UserDto> fetchNowCandidates(DocStateBean docStateBean);
	
	List<UserDto> fetchNextCandidates(DocStateBean docStateBean, LinkAction action);
	
	List<NameValuePair> fetchNextCandidatesNameValuePair(DocStateBean docStateBean, LinkAction action);
	
	DocStateBean applyTo(String nextUserId, DocStateBean docStateBean, LinkAction action);
	
	DocStateBean processTo(String nextUserId, DocStateBean docStateBean, LinkAction action);
	
	DocStateBean applyConcurrentTo(String[] nextUserIds, DocStateBean docStateBean, LinkAction action);
	
	DocStateBean processConcurrentTo(String[] nextUserIds, DocStateBean docStateBean, LinkAction action);
	
	boolean isFinalFlowTask(DocStateBean docStateBean, LinkAction action);
	
	String getNextFlowTaskId(String flowId, LinkAction action);
	
	String getNextFlowTaskId(DocStateBean docStateBean, LinkAction action);
	
	String getNextFlowTaskExt(String flowId, LinkAction action);
	
	String getNextFlowTaskExt(DocStateBean docStateBean, LinkAction action);
	
	boolean updateMainFormId(Long id, Long mainFormId);
	
	DocStateBean forceCancel(DocStateBean docStateBean);
	
	Short getFlowTaskNum(String flowId);
}
