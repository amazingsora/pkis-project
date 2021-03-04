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
 * Title: MedFormStudyEvalTest<br>
 * Description: <br>
 * Company: Tradevan Co.<br>
 * 
 * @author Neil Chen
 * @version 1.0
 */
public class MedFormStudyEvalTest {

	private MedFormStudyEval medFormStudyEval;
	
	private String fileName = "MedFormStudyEvalSampleReply";
	
	@Before
	public void setup() throws JsonParseException, JsonMappingException, IOException {
		
		medFormStudyEval = FormTestUtil.getInstance().getTestJsonObj(fileName, MedFormStudyEval.class);
	}
	
	@Test
	public void testCalcScore() {
		
		assertEquals(new BigDecimal("60.0"), medFormStudyEval.calcScore());
		assertEquals(new BigDecimal("64.0"), medFormStudyEval.calcScore2());
	}
}
