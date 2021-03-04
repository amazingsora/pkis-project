import static org.junit.Assert.*;

import java.util.List;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.tradevan.handyflow.bean.DefaultFlowAction;
import com.tradevan.handyflow.bean.DocStateBean;
import com.tradevan.handyflow.bean.FlowBean;
import com.tradevan.handyflow.service.ApplyNoService;
import com.tradevan.handyflow.service.FlowService;
import com.tradevan.handyflow.service.FlowQueryService;

@RunWith(SpringJUnit4ClassRunner.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@ContextConfiguration(locations={"classpath:test-flow-applicationContext.xml"})  
@Transactional
public class F2786_v1Test {
	
	public final static String SYS_ID = "APPKIS";
	public final static String FLOW_ID = "F2786";
	public final static String FLOW_VERSION = "1";
	public final static String FORM_ID = "f0020";
	public final static String APPLYNO_PREFIX = "T";
	public final static String APPLYNO_DATE_FMT = "yyyyMMdd";
	public final static int APPLYNO_SERIAL_LENGTH = 4;
	
	@Autowired
	private ApplyNoService applyNoService;
	
	@Autowired
	private FlowService flowService;
	
	@Autowired
	private FlowQueryService flowQueryService;
	
	@Test
	@Rollback(false)
	public void testA1_task1ToTask2() { // Begin -> Task1 -> Task2
		// Arrange
		String userId = "F000113940";
		
		// Act & Assert
		String applyNo = applyNoService.genApplyNo(FORM_ID, APPLYNO_PREFIX, APPLYNO_DATE_FMT, APPLYNO_SERIAL_LENGTH);
		
		FlowBean flowBean = new FlowBean();
		flowBean.setSysId(SYS_ID);
		flowBean.setFormId(FORM_ID);
		flowBean.setApplyNo(applyNo);
		flowBean.setFlowId(FLOW_ID);
		flowBean.setFlowVersion(FLOW_VERSION);
		flowBean.setUserId(userId);
		
		DocStateBean docState = flowService.startFlow(flowBean, null);
		
		assertEquals("Task1", docState.getNowTaskId());
		assertEquals(userId, docState.getNowUserId());
		
		docState = flowService.apply(docState, null);
		
		assertEquals("Task2", docState.getNowTaskId());
		assertNull(docState.getNowUserId());
		assertEquals("F000022552", docState.getTaskUserIds());
	}
	
	@Test
	@Rollback(false)
	public void testA2_task2ToEnd() { // Task2 -> End
		// Arrange
		String userId = "F000022552";
		
		// Act & Assert
		List<DocStateBean> list = flowQueryService.fetchToDoListBy(userId, FORM_ID, SYS_ID, true, false);
		
		assertTrue(list.size() > 0);
		
		DocStateBean docState = list.get(0);
		
		assertEquals("Task2", docState.getNowTaskId());
		assertEquals(userId, docState.getTaskUserIds());
		assertNull(docState.getNowUserId());
		
		docState = flowService.claim(docState.nowUser(userId, docState.getNowTaskId()));
		
		assertEquals(userId, docState.getNowUserId());
		
		docState = flowService.process(docState.nowUser(userId, docState.getNowTaskId()), DefaultFlowAction.APPROVE);
		
		assertEquals("End", docState.getNowTaskId());
		assertNull(docState.getNowUserId());
		assertNull(docState.getTaskUserIds());
	}
	
}
