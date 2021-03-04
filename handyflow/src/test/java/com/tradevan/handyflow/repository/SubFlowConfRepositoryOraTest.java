package com.tradevan.handyflow.repository;

import static org.junit.Assert.assertNotNull;

import java.util.List;

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
public class SubFlowConfRepositoryOraTest {

	@Autowired
	@Qualifier("jpaSubFlowConfRepositoryOra")
	private SubFlowConfRepository subFlowConfRepository;
	
	@Test
	public void testFindMaxSubFlowId() {
		String result = subFlowConfRepository.findMaxSubFlowId("flowId");
		assertNotNull(result);
	}
	
}
