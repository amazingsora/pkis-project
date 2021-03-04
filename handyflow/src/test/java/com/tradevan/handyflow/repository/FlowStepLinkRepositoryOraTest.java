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
public class FlowStepLinkRepositoryOraTest {

	@Autowired
	@Qualifier("jpaFlowStepLinkRepositoryOra")
	private FlowStepLinkRepository flowStepLinkRepository;
	
	@SuppressWarnings("rawtypes")
	@Test
	public void testFindFlowStepLinks() {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("flowId", "flowId");
		params.put("stepId", "stepId");
		
		List list = flowStepLinkRepository.findFlowStepLinks(params);
		assertNotNull(list);
	}
	
	@Test
	public void testFindMaxLinkDispOrd() {
		int result = flowStepLinkRepository.findMaxLinkDispOrd("SampleMediaToDo", "W");
		assertNotNull(result);
	}
	
	@SuppressWarnings("rawtypes")
	@Test
	public void testFindLinksForSubFlow() {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("flowId", "flowId");
		params.put("stepId", "stepId");
		
		List list = flowStepLinkRepository.findLinksForSubFlow(params);
		assertNotNull(list);
	}
	
}
