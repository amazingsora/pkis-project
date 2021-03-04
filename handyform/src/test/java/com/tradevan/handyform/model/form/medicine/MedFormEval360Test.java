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
 * Title: MedFormEval360Test<br>
 * Description: <br>
 * Company: Tradevan Co.<br>
 * 
 * @author Neil Chen
 * @version 1.0
 */
public class MedFormEval360Test {

	private MedFormEval360 medFormEval360;
	
	private String fileName = "MedFormEval360SampleReply";
	
	@Before
	public void setup() throws JsonParseException, JsonMappingException, IOException {
		
		medFormEval360 = FormTestUtil.getInstance().getTestJsonObj(fileName, MedFormEval360.class);
	}
	
	@Test
	public void testCalcScore() {
		
		assertEquals(new BigDecimal("41.7"), medFormEval360.calcScore());
	}
}
