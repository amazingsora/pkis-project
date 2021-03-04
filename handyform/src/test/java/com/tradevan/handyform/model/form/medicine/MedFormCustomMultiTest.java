package com.tradevan.handyform.model.form.medicine;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.tradevan.handyform.model.form.medicine.elem.MedCustMasterElm;
import com.tradevan.handyform.util.FormTestUtil;

/**
 * Title: MedFormCustomMultiTest<br>
 * Description: <br>
 * Company: Tradevan Co.<br>
 * 
 * @author Neil Chen
 * @version 1.0
 */
public class MedFormCustomMultiTest {

	private MedFormCustomMulti medFormCustomMulti; // 主文件
	private MedCustMasterElm medCustMasterElmM1;   // 子文件
	private MedCustMasterElm medCustMasterElmM2;   // 子文件
	
	private String fileName = "MedFormCustomMultiSampleReply";
	private String fileNameM1 = "MedFormCustomMultiSampleReplyM1";
	private String fileNameM2 = "MedFormCustomMultiSampleReplyM2";
	
	@Before
	public void setup() throws JsonParseException, JsonMappingException, IOException {
		
		medFormCustomMulti = FormTestUtil.getInstance().getTestJsonObj(fileName, MedFormCustomMulti.class);
		medCustMasterElmM1 = FormTestUtil.getInstance().getTestJsonObj(fileNameM1, MedCustMasterElm.class);
		medCustMasterElmM2 = FormTestUtil.getInstance().getTestJsonObj(fileNameM2, MedCustMasterElm.class);
		
		medFormCustomMulti.addToMaster2List(medCustMasterElmM1);
		medFormCustomMulti.addToMaster2List(medCustMasterElmM2);
	}
	
	@Test
	public void testCalcScore() {
		
		assertEquals(new BigDecimal("60"), medCustMasterElmM1.calcScore());
		assertEquals(new BigDecimal("80"), medCustMasterElmM2.calcScore());
	}
	
	@Test
	public void testGetPassStatus() {
		
		assertEquals("N", medCustMasterElmM1.getPassStatus());
		assertEquals("Y", medCustMasterElmM2.getPassStatus());
	}
	
	@Test
	public void testMaster2ListSize() {
		
		assertEquals(2, medFormCustomMulti.getMaster2List().size());
	}
}
