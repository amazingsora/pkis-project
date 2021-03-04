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
 * Title: MedFormMilestoneTest<br>
 * Description: <br>
 * Company: Tradevan Co.<br>
 * 
 * @author Neil Chen
 * @version 1.0
 */
public class MedFormMilestoneTest {

	private MedFormMilestone medFormMilestone;
	
	private String fileName = "MedFormMilestoneSampleReply";
	
	@Before
	public void setup() throws JsonParseException, JsonMappingException, IOException {
		
		medFormMilestone = FormTestUtil.getInstance().getTestJsonObj(fileName, MedFormMilestone.class);
	}
	
	@Test
	public void testCalcScore() {
		assertEquals(new BigDecimal("2.2"), medFormMilestone.getSecEvalItems().get(0).calcRating());
		assertEquals(new BigDecimal("0.0"), medFormMilestone.getSecEvalItems().get(1).calcRating());
		assertEquals(new BigDecimal("2.8"), medFormMilestone.getSecEvalItems().get(2).calcRating());
		assertEquals(new BigDecimal("1.7"), medFormMilestone.getSecEvalItems().get(3).calcRating());
		
		assertEquals(new BigDecimal("1.7"), medFormMilestone.calcScore());
	}
}
