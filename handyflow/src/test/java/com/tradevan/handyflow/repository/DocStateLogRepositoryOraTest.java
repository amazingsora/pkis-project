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
public class DocStateLogRepositoryOraTest {

	@Autowired
	@Qualifier("jpaDocStateLogRepositoryOra")
	private DocStateLogRepository docStateLogRepository;
	
	@SuppressWarnings("rawtypes")
	@Test
	public void testFindListByFormIdAndApplyNo() {
		List list = docStateLogRepository.findListByFormIdAndApplyNo("1", "2", true);
		assertNotNull(list);
	}
	
	@SuppressWarnings("rawtypes")
	@Test
	public void testFindDocStateLogListMap() {
		List list = docStateLogRepository.findDocStateLogListMap(1L, 2L, 3L);
		assertNotNull(list);
	}
	
	@SuppressWarnings("rawtypes")
	@Test
	public void testFindDocStateLogMemos1() {
		List list = docStateLogRepository.findDocStateLogMemos("1", "2", "3");
		assertNotNull(list);
	}
	
	@SuppressWarnings("rawtypes")
	@Test
	public void testFindDocStateLogMemos2() {
		List list = docStateLogRepository.findDocStateLogMemos("1", "2");
		assertNotNull(list);
	}
	
	@Test
	public void testCountDocStateLogsBy() {
		Long result = docStateLogRepository.countDocStateLogsBy(null, null);
		assertNotNull(result);
	}

	@Test
	public void testFetchDocStateLogs() {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("sourceSerNo", 1);
		
		List<Map<String, Object>> list = docStateLogRepository.fetchDocStateLogs(param);
		assertNotNull(list);
	}
}
