package com.tradevan.handyflow.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.tradevan.apcommon.bean.NameValuePair;
import com.tradevan.handyflow.bean.DocStateBean;
import com.tradevan.handyflow.bean.FlowBean;
import com.tradevan.handyflow.model.flow.LinkAction;
import com.tradevan.handyflow.service.ApplyNoService;
import com.tradevan.handyflow.service.HandyFlowFacade;
import com.tradevan.handyflow.service.FlowService;
import com.tradevan.handyflow.service.FlowQueryService;

/**
 * Title: HandyFlowFacadeImpl<br>
 * Description: <br>
 * Company: Tradevan Co.<br>
 * 
 * @author Neil Chen
 * @version 1.2.1
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class HandyFlowFacadeImplOra implements HandyFlowFacade {

	@Autowired
	//@Qualifier("applyNoServiceImplOra")
	private ApplyNoService applyNoService;
	
	@Autowired
	//@Qualifier("flowServiceImplOra")
	private FlowService flowService;
	
	@Autowired
	//@Qualifier("flowQueryServiceImplOra")
	private FlowQueryService flowQueryService;
	
	@Override
	public DocStateBean applyNew(FlowBean flowBean, LinkAction startAction, LinkAction applyAction) {
		
		String applyNo = applyNoService.genApplyNo(flowBean.getFormId(), flowBean.getApplyNoPrefix(), flowBean.getApplyNoDateFmt(), flowBean.getApplyNoSerialLen());
		
		flowBean.setApplyNo(applyNo);
		
		return flowService.apply(flowService.startFlow(flowBean, startAction), applyAction);
	}

	@Override
	public List<DocStateBean> fetchToDoList(String userId, String formId, String sysId, boolean sortDesc, boolean includeAgent) {
		
		if (formId == null) {
			return flowQueryService.fetchToDoListBy(userId, sysId, sortDesc, includeAgent);
		}
		else {
			return flowQueryService.fetchToDoListBy(userId, formId, sysId, sortDesc, includeAgent);
		}
	}

	@Override
	public DocStateBean claimAndProcess(DocStateBean docStateBean, LinkAction action) {
		
		DocStateBean docStateBean2 = flowService.claim(docStateBean);
		if (docStateBean.isSuper()) {
			docStateBean2.superUser();
		}
		return flowService.process(docStateBean2.nowUser(docStateBean.getUserId()), action);
	}

	@Override
	public List<NameValuePair> fetchNextCandidatesNameValuePair(DocStateBean docStateBean, LinkAction action) {
		
		return flowService.fetchNextCandidatesNameValuePair(docStateBean, action);
	}
	
	@Override
	public DocStateBean processTo(String nextUserId, DocStateBean docStateBean, LinkAction action) {
		
		return flowService.processTo(nextUserId, docStateBean, action);
	}
}
