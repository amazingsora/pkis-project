package com.tradevan.handyflow.repository;

import static org.junit.Assert.assertNotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:test-flow-applicationContext.xml"})  
@Transactional
public class FlowStepRepositoryOraTest {

	@Autowired
	@Qualifier("jpaFlowStepRepositoryOra")
	private FlowStepRepository flowStepRepository;
	
	@SuppressWarnings("rawtypes")
	@Test
	public void testFindFlowSteps() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("subFlowId","subFlowId");
		List list = flowStepRepository.findFlowSteps(params);
		assertNotNull(list);
	}
	
	@SuppressWarnings("rawtypes")
	@Test
	public void testFindAllSameUserAs() {
		List list = flowStepRepository.findAllSameUserAs("flowId", 4);
		assertNotNull(list);
	}
	
	@Test
	public void testFindMaxStepId() {
		String result = flowStepRepository.findMaxStepId("flowId", "subFlowId");
		assertNotNull(result);
	}
	
	@Test
	public void testFindMaxStepDispOrd() {
		int result = flowStepRepository.findMaxStepDispOrd("flowId", "subFlowId");
		assertNotNull(result);
	}
	
	@SuppressWarnings("rawtypes")
	@Test
	public void testListFlowStep() {
		List list = flowStepRepository.listFlowStep("flowId", "stepId", "subFlowId", false);
		assertNotNull(list);
	}
	
	@SuppressWarnings("rawtypes")
	@Test
	public void testFindFlowStepsNotBySubFlow() {
		List list = flowStepRepository.findFlowStepsNotBySubFlow("flowId");
		assertNotNull(list);
	}
	
	@SuppressWarnings("rawtypes")
	@Test
	public void testFindFlowStepsBySubFlow() {
		List list = flowStepRepository.findFlowStepsBySubFlow("flowId", "subFlowId");
		assertNotNull(list);
	}
	
	@SuppressWarnings("rawtypes")
	@Test
	public void testFindSimpleFlowSteps() {
		List list = flowStepRepository.findSimpleFlowSteps("flowId");
		assertNotNull(list);
	}
}
