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
public class DocToDoRepositoryOraTest {

	@Autowired
	@Qualifier("jpaDocToDoRepositoryOra")
	private DocToDoRepository docToDoRepository;
	
	@SuppressWarnings("rawtypes")
	@Test
	public void testFindBySysIdAndProjId() {
		List list = docToDoRepository.findEntityListByNamedQuery("DocToDo.findBySysIdAndProjId", "1", "2");
		assertNotNull(list);
	}
	
	@Test
	public void testGetMaxToDoSerial() {
		String result = docToDoRepository.getMaxToDoNoSerial("SampleMediaToDo", "todo", "20181026", 4);
		assertNotNull(result);
	}
	
	@SuppressWarnings("rawtypes")
	@Test
	public void testFindOwnedListByUser() {
		List list = docToDoRepository.findOwnedListByUser("1", "2", "3", true);
		assertNotNull(list);
	}
	
	@Test
	public void testUpdateStatusByProjIdAndSysId() {
		int result = docToDoRepository.updateStatusByProjIdAndSysId("1", "2", "3");
		assertNotNull(result);
	}
	
	@Test
	public void testRestoreStatusByProjIdAndSysId() {
		int result = docToDoRepository.restoreStatusByProjIdAndSysId("1", "2");
		assertNotNull(result);
	}
	
	@Test
	public void testUpdateStatusByProjIdAndSysIdAndStatus() {
		int result = docToDoRepository.updateStatusByProjIdAndSysIdAndStatus("1", "2", "3", "4");
		assertNotNull(result);
	}
}
