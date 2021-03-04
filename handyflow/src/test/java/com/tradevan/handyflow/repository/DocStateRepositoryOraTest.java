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

import com.tradevan.handyflow.model.form.DocState;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:test-flow-applicationContext.xml"})  
@Transactional
public class DocStateRepositoryOraTest {

	@Autowired
	@Qualifier("jpaDocStateRepositoryOra")
	private DocStateRepository docStateRepository;
	
	@Test
	public void testGetMaxApplyNoSerial() {
		String result = docStateRepository.getMaxApplyNoSerial("todo", "20181026", 4);
		assertNotNull(result);
	}
	
	@Test
	public void testGetMaxApplyNoSerial2() {
		String result = docStateRepository.getMaxApplyNoSerial("SampleMediaToDo", "todo", "20181026", 4);
		assertNotNull(result);
	}
	
	@SuppressWarnings("rawtypes")
	@Test
	public void testFindOwnedListByUser() {
		List list = docStateRepository.findOwnedListByUser("1", "2", "3", true);
		assertNotNull(list);
	}
	
	@SuppressWarnings("rawtypes")
	@Test
	public void testFindNoOwnerListByDeptAndRole() {
		List list = docStateRepository.findNoOwnerListByDeptAndRole("deptId", "roleId", "SampleMediaToDo", "sysId", true);
		assertNotNull(list);
	}
	
	@SuppressWarnings("rawtypes")
	@Test
	public void testFindNoOwnerListByApplicantDeptRole() {
		List list = docStateRepository.findNoOwnerListByApplicantDeptRole("deptId", "roleId", "SampleMediaToDo", "sysId", true,true);
		assertNotNull(list);
	}
	
	@SuppressWarnings("rawtypes")
	@Test
	public void testFindNoOwnerListByReviewDeptRole() {
		List list = docStateRepository.findNoOwnerListByReviewDeptRole("deptId", "roleId", "SampleMediaToDo", "sysId", true,true);
		assertNotNull(list);
	}
	
	@SuppressWarnings("rawtypes")
	@Test
	public void testFindNoOwnerListByRole() {
		List list = docStateRepository.findNoOwnerListByRole("roleId", "SampleMediaToDo", "sysId", true);
		assertNotNull(list);
	}
	
	@SuppressWarnings("rawtypes")
	@Test
	public void testFindNoOwnerListByUserProjRole() {
		List list = docStateRepository.findNoOwnerListByUserProjRole("userId", "SampleMediaToDo", "sysId", true);
		assertNotNull(list);
	}
	
	@SuppressWarnings("rawtypes")
	@Test
	public void testFindNoOwnerListByDept() {
		List list = docStateRepository.findNoOwnerListByDept("deptId", "SampleMediaToDo", "sysId", true);
		assertNotNull(list);
	}
	
	@SuppressWarnings("rawtypes")
	@Test
	public void testFindNoOwnerListByUser() {
		List list = docStateRepository.findNoOwnerListByUser("userId", "SampleMediaToDo", "sysId", true);
		assertNotNull(list);
	}
	
	@Test
	public void testGetMaxSerialNo() {
		int result = docStateRepository.getMaxSerialNo(null);
		assertNotNull(result);
	}
	
	@Test
	public void testGetDocState() {
		DocState object = docStateRepository.getDocState("SampleMediaToDo", "applyNo", 4);
		assertNotNull(object);
	}
	
//	@SuppressWarnings("rawtypes")
//	@Test
//	public void testFindCmToDos() {
//		List list = docStateRepository.findCmToDos();
//		assertNotNull(list);
//	}
	
//	@SuppressWarnings("rawtypes")
//	@Test
//	public void testFindDNUpdateTimes() {
//		List list = docStateRepository.findDNUpdateTimes();
//		assertNotNull(list);
//	}
}
