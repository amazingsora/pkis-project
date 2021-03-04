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
public class DocToDoRepositoryTest {

	@Autowired
	@Qualifier("jpaDocToDoRepository")
	private DocToDoRepository docToDoRepository;
	
	@SuppressWarnings("rawtypes")
	@Test
	public void testFindBySysIdAndProjId() {
		List list = docToDoRepository.findEntityListByNamedQuery("DocToDo.findBySysIdAndProjId", "1", "2");
		assertNotNull(list);
	}
	
}
