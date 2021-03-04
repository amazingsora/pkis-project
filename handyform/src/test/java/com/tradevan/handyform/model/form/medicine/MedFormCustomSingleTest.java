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
 * Title: MedFormCustomSingleTest<br>
 * Description: <br>
 * Company: Tradevan Co.<br>
 * 
 * @author Neil Chen
 * @version 1.0
 */
public class MedFormCustomSingleTest {

	private MedFormCustomSingle medFormCustomSingle1;
	private MedFormCustomSingle medFormCustomSingle2;
	private MedFormCustomSingle medFormCustomSingle3;
	private MedFormCustomSingle medFormCustomSingle4;
	private MedFormCustomSingle medFormCustomSingle5;
	private MedFormCustomSingle medFormCustomSingle6;
	private MedFormCustomSingle medFormCustomSingle7;
	private MedFormCustomSingle medFormCustomSingle8;
	
	private String fileName1 = "MedFormCustomSingleSample1Reply";
	private String fileName2 = "MedFormCustomSingleSample2Reply";
	private String fileName3 = "MedFormCustomSingleSample3Reply";
	private String fileName4 = "MedFormCustomSingleSample4Reply";
	private String fileName5 = "MedFormCustomSingleSample5Reply";
	private String fileName6 = "MedFormCustomSingleSample6Reply";
	private String fileName7 = "MedFormCustomSingleSample7Reply";
	private String fileName8 = "MedFormCustomSingleSample8Reply";
	
	@Before
	public void setup() throws JsonParseException, JsonMappingException, IOException {
		
		medFormCustomSingle1 = FormTestUtil.getInstance().getTestJsonObj(fileName1, MedFormCustomSingle.class);
		medFormCustomSingle2 = FormTestUtil.getInstance().getTestJsonObj(fileName2, MedFormCustomSingle.class);
		medFormCustomSingle3 = FormTestUtil.getInstance().getTestJsonObj(fileName3, MedFormCustomSingle.class);
		medFormCustomSingle4 = FormTestUtil.getInstance().getTestJsonObj(fileName4, MedFormCustomSingle.class);
		medFormCustomSingle5 = FormTestUtil.getInstance().getTestJsonObj(fileName5, MedFormCustomSingle.class);
		medFormCustomSingle6 = FormTestUtil.getInstance().getTestJsonObj(fileName6, MedFormCustomSingle.class);
		medFormCustomSingle7 = FormTestUtil.getInstance().getTestJsonObj(fileName7, MedFormCustomSingle.class);
		medFormCustomSingle8 = FormTestUtil.getInstance().getTestJsonObj(fileName8, MedFormCustomSingle.class);
	}
	
	@Test
	public void testSumCustFormScore() {
		
		assertEquals(new BigDecimal("44.0"), medFormCustomSingle1.sumCustFormScore()); // AVG
		
		assertEquals(new BigDecimal("240"), medFormCustomSingle2.sumCustFormScore()); // SUM
		
		assertEquals(new BigDecimal("70"), medFormCustomSingle3.sumCustFormScore()); // TOP
		
		assertEquals(new BigDecimal("0"), medFormCustomSingle4.sumCustFormScore()); // LOW
		
		assertEquals(new BigDecimal("55.0"), medFormCustomSingle5.sumCustFormScore());
		
		assertEquals(new BigDecimal("55.0"), medFormCustomSingle6.sumCustFormScore());
		
		assertEquals(new BigDecimal("60.0"), medFormCustomSingle7.sumCustFormScore());
		
		assertEquals(new BigDecimal("66.7"), medFormCustomSingle8.sumCustFormScore());
	}
	
	@Test
	public void testCalcScore() {
		
		assertEquals(new BigDecimal("44"), medFormCustomSingle1.calcScore());
		
		assertEquals(new BigDecimal("240"), medFormCustomSingle2.calcScore());
		
		assertEquals(new BigDecimal("70"), medFormCustomSingle3.calcScore());
		
		assertEquals(new BigDecimal("0"), medFormCustomSingle4.calcScore());
		
		assertEquals(new BigDecimal("55.0"), medFormCustomSingle5.calcScore()); // isEval has no scoring (scoringConv)
		
		assertEquals(new BigDecimal("55"), medFormCustomSingle6.calcScore());
		
		assertEquals(new BigDecimal("60.0"), medFormCustomSingle7.calcScore()); // isEval option has no scoring (scoringConv)
		
		assertEquals(new BigDecimal("66.7"), medFormCustomSingle8.calcScore());
	}
	
	@Test
	public void testCalcSatisfaction() {
		
		assertEquals(new Integer("5"), medFormCustomSingle1.calcSatisfaction());
		
		assertEquals(new Integer("6"), medFormCustomSingle2.calcSatisfaction());
		
		assertEquals(new Integer("7"), medFormCustomSingle3.calcSatisfaction());
		
		assertEquals(new Integer("2"), medFormCustomSingle4.calcSatisfaction());
		
		assertEquals(new Integer("5"), medFormCustomSingle5.calcSatisfaction());
		
		assertEquals(new Integer("5"), medFormCustomSingle6.calcSatisfaction());
		
		assertEquals(new Integer("5"), medFormCustomSingle7.calcSatisfaction());
		
		assertEquals(new Integer("5"), medFormCustomSingle8.calcSatisfaction());
	}
	
	@Test
	public void testCalcSatisfaction2() {
		
		assertEquals(new Integer("4"), medFormCustomSingle1.calcSatisfaction2());
		
		assertEquals(new Integer("5"), medFormCustomSingle2.calcSatisfaction2());
		
		assertEquals(new Integer("6"), medFormCustomSingle3.calcSatisfaction2());
		
		assertEquals(new Integer("1"), medFormCustomSingle4.calcSatisfaction2());
		
		assertEquals(new Integer("4"), medFormCustomSingle5.calcSatisfaction2());
		
		assertEquals(new Integer("4"), medFormCustomSingle6.calcSatisfaction2());
		
		assertEquals(new Integer("4"), medFormCustomSingle7.calcSatisfaction2());
		
		assertEquals(new Integer("4"), medFormCustomSingle8.calcSatisfaction2());
	}
	
	@Test
	public void testGetPassStatus() {
		
		assertEquals("N", medFormCustomSingle1.getPassStatus());
		
		assertEquals("Y", medFormCustomSingle2.getPassStatus());
		
		assertEquals("N", medFormCustomSingle3.getPassStatus());
		
		assertEquals("N", medFormCustomSingle4.getPassStatus());
		
		assertEquals("N", medFormCustomSingle5.getPassStatus());
		
		assertEquals("N", medFormCustomSingle6.getPassStatus()); // isEval has no scoring (passConv)
		
		assertEquals("N", medFormCustomSingle7.getPassStatus());
		
		assertEquals("Y", medFormCustomSingle8.getPassStatus()); // isEval option has no scoring (passConv)
	}
}
