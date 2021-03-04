package com.tradevan.handyform.model.exam;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.tradevan.handyform.util.FormTestUtil;

/**
 * Title: ExamActivityTest<br>
 * Description: <br>
 * Company: Tradevan Co.<br>
 * 
 * @author Neil Chen
 * @version 1.0
 */
public class ExamActivityTest {

	private ExamActivity activity;
	
	private String fileName = "ExamNormalSampleReply";
	
	@Before
	public void setup() throws JsonParseException, JsonMappingException, IOException {
		Map<String, Examination> examinations = new HashMap<String, Examination>();
		for (int x = 0; x < 100; x++) {
			examinations.put("user" + x, FormTestUtil.getInstance().getTestJsonObj(fileName, Examination.class));
		}
		activity = new ExamActivity(examinations);
	}		
	
	@Test
	public void testCalcAvgScore() {
		
		assertEquals(new BigDecimal("50.0"), activity.calcAvgScore());
	}
}
