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
 * Title: MedFormCbd<br>
 * Description: <br>
 * Company: Tradevan Co.<br>
 * 
 * @author Neil Chen
 * @version 1.0
 */
public class MedFormCbdTest {

	private MedFormCbd medFormCbd;
	
	private String fileName = "MedFormCbdSampleReply";
	
	@Before
	public void setup() throws JsonParseException, JsonMappingException, IOException {
		
		medFormCbd = FormTestUtil.getInstance().getTestJsonObj(fileName, MedFormCbd.class);
	}
	
	@Test
	public void testCalcScore() {
		
		assertEquals(new BigDecimal("66.7"), medFormCbd.calcScore());
	}
}
