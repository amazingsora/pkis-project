package com.tradevan.handyflow.service;

import com.tradevan.apcommon.bean.NameValuePair;
import com.tradevan.handyflow.bean.DocStateBean;
import com.tradevan.handyflow.bean.FlowBean;
import com.tradevan.handyflow.model.flow.LinkAction;

import java.util.List;

/**
 * Title: HandyFlowFacade<br>
 * Description: <br>
 * Company: Tradevan Co.<br>
 * 
 * @author Neil Chen
 * @version 1.2.1
 */
public interface HandyFlowFacade {

	DocStateBean applyNew(FlowBean flowBean, LinkAction startAction, LinkAction applyAction);
	
	List<DocStateBean> fetchToDoList(String userId, String formId, String sysId, boolean sortDesc, boolean includeAgent);
	
	DocStateBean claimAndProcess(DocStateBean docStateBean, LinkAction action);
	
	List<NameValuePair> fetchNextCandidatesNameValuePair(DocStateBean docStateBean, LinkAction action);
	
	DocStateBean processTo(String nextUserId, DocStateBean docStateBean, LinkAction action);
}
