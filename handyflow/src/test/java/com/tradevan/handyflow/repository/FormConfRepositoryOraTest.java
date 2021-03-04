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
public class FormConfRepositoryOraTest {

	@Autowired
	@Qualifier("jpaFormConfRepositoryOra")
	private FormConfRepository formConfRepository;
	
	@SuppressWarnings("rawtypes")
	@Test
	public void testFindFormConfs() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("sysId","sysId");
		List list = formConfRepository.findFormConfs(params);
		assertNotNull(list);
	}
	
}
