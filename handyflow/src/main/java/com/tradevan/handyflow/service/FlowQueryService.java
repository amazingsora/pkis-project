package com.tradevan.handyflow.service;

import java.util.List;

import com.tradevan.handyflow.bean.DocStateBean;
import com.tradevan.handyflow.model.form.DocStateLog;

/**
 * Title: FlowQueryService<br>
 * Description: <br>
 * Company: Tradevan Co.<br>
 * 
 * @author Neil Chen
 * @version 1.9
 */
public interface FlowQueryService {

	List<DocStateBean> fetchToDoListBy(String userId, String sysId, boolean sortDesc);
	
	List<DocStateBean> fetchToDoListBy(String userId, String formId, String sysId, boolean sortDesc);
	
	List<DocStateBean> fetchToDoListBy(String userId, String sysId, boolean sortDesc, boolean includeAgent);
	
	List<DocStateBean> fetchToDoListBy(String userId, String formId, String sysId, boolean sortDesc, boolean includeAgent);
	
	DocStateBean getDocStateById(Long docStateId);
	
	DocStateBean getDocStateBy(String formId, String applyNo, Integer serialNo);
	
	List<DocStateLog> fetchDocStateLogsBy(String formId, String applyNo);

	String getLatestCommentBy(String formId, String applyNo, String taskId);
	
	String getLatestCommentBy(String formId, String applyNo);
	
	List<String> fetchCommentsBy(String formId, String applyNo, String taskId);
	
	boolean isTaskIdsInDocStateLogs(Long docStateId, List<String> taskIds);
	
	String getApplyNo(Long docStateId);
}
