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
 * Title: MedFormResidentAssessTest<br>
 * Description: <br>
 * Company: Tradevan Co.<br>
 * 
 * @author Neil Chen
 * @version 1.0
 */
public class MedFormResidentAssessTest {

	private MedFormResidentAssess medFormResidentAssess;
	
	private String fileName = "MedFormResidentAssessSampleReply";
	
	@Before
	public void setup() throws JsonParseException, JsonMappingException, IOException {
		
		medFormResidentAssess = FormTestUtil.getInstance().getTestJsonObj(fileName, MedFormResidentAssess.class);
	}
	
	@Test
	public void testSumCustFormScore() {
		
		assertEquals(new BigDecimal("53"), medFormResidentAssess.sumCustFormScore());
	}
	
	@Test
	public void testSumCustFormScore2() {
		
		assertEquals(new BigDecimal("73"), medFormResidentAssess.sumCustFormScore2());
	}
	
	@Test
	public void testCalcScore() {
		
		assertEquals(new BigDecimal("73"), medFormResidentAssess.calcScore());
	}
	
	@Test
	public void testCalcScore2() {
		
		assertEquals(new BigDecimal("53"), medFormResidentAssess.calcScore2());
	}
	
	@Test
	public void testCalcSatisfaction() {
		
		assertEquals(new Integer("5"), medFormResidentAssess.calcSatisfaction());
	}
	
	@Test
	public void testCalcSatisfaction2() {
		
		assertEquals(new Integer("4"), medFormResidentAssess.calcSatisfaction2());
	}
}
