package com.tradevan.handyform.model.exam;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.tradevan.handyform.util.FormTestUtil;

/**
 * Title: ExaminationTest<br>
 * Description: <br>
 * Company: Tradevan Co.<br>
 * 
 * @author Neil Chen
 * @version 1.0
 */
public class ExaminationTest {

	private Examination examNormal;
	
	private String fileName = "ExamNormalSampleReply";
	
	@Before
	public void setup() throws JsonParseException, JsonMappingException, IOException {
		
		examNormal = FormTestUtil.getInstance().getTestJsonObj(fileName, Examination.class);
	}
	
	@Test
	public void testCalcScore() {
		
		assertEquals(50, examNormal.calcScore());
	}
}
