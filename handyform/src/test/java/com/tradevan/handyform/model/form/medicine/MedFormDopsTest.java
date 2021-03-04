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
 * Title: MedFormDopsTest<br>
 * Description: <br>
 * Company: Tradevan Co.<br>
 * 
 * @author Neil Chen
 * @version 1.0
 */
public class MedFormDopsTest {

	private MedFormDops medFormDops;
	
	private String fileName = "MedFormDopsSampleReply";
	
	@Before
	public void setup() throws JsonParseException, JsonMappingException, IOException {
		
		medFormDops = FormTestUtil.getInstance().getTestJsonObj(fileName, MedFormDops.class);
	}
	
	@Test
	public void testCalcScore() {
		
		assertEquals(new BigDecimal("50.0"), medFormDops.calcScore());
	}
}
