package com.tradevan.handyform.model.ques;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.tradevan.handyform.marshaller.JsonDocMarshaller;
import com.tradevan.handyform.util.FormTestUtil;

/**
 * Title: QuesVoteTest<br>
 * Description: <br>
 * Company: Tradevan Co.<br>
 * 
 * @author Neil Chen
 * @version 1.0
 */
public class QuesVoteTest {

	private JsonDocMarshaller mashaller = new JsonDocMarshaller();
	private QuesVote quesVote;
	
	private String fileName = "QuesVoteSampleReply";
	
	@Before
	public void setup() throws JsonParseException, JsonMappingException, IOException {
		
		String jsonStr = FormTestUtil.getInstance().getTestJsonStr(fileName, QuesVote.class);
		quesVote = mashaller.unmarshal(jsonStr, QuesVote.class);
	}
	
	@Test
	public void testParseResult() {
		assertEquals(2, quesVote.getVoteNum().intValue());
		assertEquals(5, quesVote.getSecVotes().size());
		assertEquals("0", quesVote.getSecVotes().get(0).getReply());
		assertEquals("1", quesVote.getSecVotes().get(1).getReply());
		assertEquals("0", quesVote.getSecVotes().get(2).getReply());
		assertEquals("1", quesVote.getSecVotes().get(3).getReply());
		assertEquals("0", quesVote.getSecVotes().get(4).getReply());
		
		for (QuesElement elm : quesVote.getSecVotes()) {
			System.out.print(elm.getValue());
			System.out.println(" - " + elm.getReply());
		}
	}
}
