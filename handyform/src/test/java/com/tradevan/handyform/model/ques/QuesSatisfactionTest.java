package com.tradevan.handyform.model.ques;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.tradevan.handyform.util.FormTestUtil;

/**
 * Title: QuesSatisfactionTest<br>
 * Description: <br>
 * Company: Tradevan Co.<br>
 * 
 * @author Neil Chen
 * @version 1.1
 */
public class QuesSatisfactionTest {

	private QuesSatisfaction quesSatisfaction;
	
	private String fileName = "QuesSatisfactionSampleReply";
	
	@Before
	public void setup() throws JsonParseException, JsonMappingException, IOException {
		
		quesSatisfaction = FormTestUtil.getInstance().getTestJsonObj(fileName, QuesSatisfaction.class);
	}
	
	@Test
	public void testCalcScore() {
		
		assertEquals(new BigDecimal("60.0"), quesSatisfaction.calcScore());
	}
}
