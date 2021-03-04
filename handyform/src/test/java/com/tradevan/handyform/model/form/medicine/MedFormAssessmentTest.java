package com.tradevan.handyform.model.form.medicine;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.tradevan.handyform.util.FormTestUtil;

/**
 * Title: MedFormAssessmentTest<br>
 * Description: <br>
 * Company: Tradevan Co.<br>
 * 
 * @author Neil Chen
 * @version 1.0
 */
public class MedFormAssessmentTest {

	private MedFormAssessment medFormAssessment;
	
	private String fileName = "MedFormAssessmentSampleReply";
	
	@Before
	public void setup() throws JsonParseException, JsonMappingException, IOException {
		
		medFormAssessment = FormTestUtil.getInstance().getTestJsonObj(fileName, MedFormAssessment.class);
	}
	
	@Test
	public void testCalcScore() {
		
		assertEquals(new BigDecimal("69.0"), medFormAssessment.calcScore());
	}
}
