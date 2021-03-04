package com.tradevan.handyform.model.ques;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.tradevan.handyform.model.ques.QuesSatisfactionActivity;
import com.tradevan.handyform.util.FormTestUtil;

/**
 * Title: QuesSatisfactionActivityTest<br>
 * Description: <br>
 * Company: Tradevan Co.<br>
 * 
 * @author Neil Chen
 * @version 1.0
 */
public class QuesSatisfactionActivityTest {

	private QuesSatisfactionActivity activity;
	
	private String fileName = "QuesSatisfactionSampleReply";
	
	@Before
	public void setup() throws JsonParseException, JsonMappingException, IOException {
		Map<String, QuesSatisfaction> satisfactions = new HashMap<String, QuesSatisfaction>();
		for (int x = 0; x < 100; x++) {
			satisfactions.put("user" + x, FormTestUtil.getInstance().getTestJsonObj(fileName, QuesSatisfaction.class));
		}
		activity = new QuesSatisfactionActivity(satisfactions);
	}		
	
	@Test
	public void testCalcAvgScore() {
		
		assertEquals(new BigDecimal("60.0"), activity.calcAvgScore());
	}
}
