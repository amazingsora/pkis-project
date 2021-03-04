package com.tradevan.handyflow.repository;

import java.util.List;
import java.util.Map;

import com.tradevan.apcommon.persistence.GenericRepository;
import com.tradevan.handyflow.model.form.FlowStepLink;

/**
 * Title: FlowStepLinkRepository<br>
 * Description: <br>
 * Company: Tradevan Co.<br>
 * 
 * @author Neil Chen
 * @version 1.0
 */
public interface FlowStepLinkRepository extends GenericRepository<FlowStepLink, Long> {

	List<Map<String, Object>> findFlowStepLinks(Map<String, Object> params);
	
	Integer findMaxLinkDispOrd(String flowId, String stepId);
	
	List<Map<String, Object>> findLinksForSubFlow(Map<String, Object> params);
	
}
