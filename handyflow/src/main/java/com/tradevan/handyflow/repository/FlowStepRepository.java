package com.tradevan.handyflow.repository;

import java.util.List;
import java.util.Map;

import com.tradevan.apcommon.persistence.GenericRepository;
import com.tradevan.handyflow.model.form.FlowStep;

/**
 * Title: FlowStepRepository<br>
 * Description: <br>
 * Company: Tradevan Co.<br>
 * 
 * @author Neil Chen
 * @version 1.0
 */
public interface FlowStepRepository extends GenericRepository<FlowStep, Long> {

	List<Map<String, Object>> findFlowSteps(Map<String, Object> params);
	
	List<Map<String, Object>> findAllSameUserAs(String flowId, Integer dispOrd);
	
	String findMaxStepId(String flowId, String subFlowId);
	
	Integer findMaxStepDispOrd(String flowId, String subFlowId);
	
	List<Map<String, Object>> listFlowStep(String flowId, String stepId, String subFlowId, boolean isFinSelect);
	
	List<Map<String, Object>> findFlowStepsNotBySubFlow(String flowId);
	
	List<Map<String, Object>> findFlowStepsBySubFlow(String flowId, String subFlowId);

	List<Map<String, Object>> findSimpleFlowSteps(String flowId);
	
}
