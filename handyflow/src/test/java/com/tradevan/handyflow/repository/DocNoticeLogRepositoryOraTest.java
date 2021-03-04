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
public class DocNoticeLogRepositoryOraTest {

	@Autowired
	@Qualifier("jpaDocNoticeLogRepositoryOra")
	private DocNoticeLogRepository docNoticeLogRepository;
	
	
	@Test
	public void testUpdateStatus2Y() {
		int result = docNoticeLogRepository.updateStatus2Y("admin", "sysId");
		assertNotNull(result);
	}
	
	@Test
	public void testUpdateStatusWithDocToDoId() {
		int result = docNoticeLogRepository.updateStatusWithDocToDoId("W", "sysId",1L);
		assertNotNull(result);
	}
	
	@Test
	public void testUpdateStatus() {
		int result = docNoticeLogRepository.updateStatus("W", "sysId","applyNo",1);
		assertNotNull(result);
	}
	
	@SuppressWarnings("rawtypes")
	@Test
	public void testFetchNeedNoticeLogs() {
		List list = docNoticeLogRepository.fetchNeedNoticeLogs();
		assertNotNull(list);
	}
	

}
