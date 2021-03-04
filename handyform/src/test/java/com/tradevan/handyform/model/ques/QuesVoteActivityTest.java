package com.tradevan.handyform.model.ques;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.tradevan.handyform.util.FormTestUtil;

/**
 * Title: QuesVoteActivityTest<br>
 * Description: <br>
 * Company: Tradevan Co.<br>
 * 
 * @author Neil Chen
 * @version 1.0
 */
public class QuesVoteActivityTest {

	private QuesVoteActivity activity;
	
	private String fileName = "QuesVoteSampleReply";
	
	@Before
	public void setup() throws JsonParseException, JsonMappingException, IOException {
		Map<String, QuesVote> votes = new HashMap<String, QuesVote>();
		for (int x = 0; x < 50; x++) {
			votes.put("user" + x, FormTestUtil.getInstance().getTestJsonObj(fileName, QuesVote.class));
		}
		activity = new QuesVoteActivity(votes);
	}		
	
	@Test
	public void testCalcVoteSum() {
		Map<String, Integer> voteSum = activity.calcVoteSum();
		
		assertEquals(5, voteSum.size());
		Integer[] votes = voteSum.values().toArray(new Integer[0]);
		assertEquals(0, votes[0].intValue());
		assertEquals(50, votes[1].intValue());
		assertEquals(0, votes[2].intValue());
		assertEquals(50, votes[3].intValue());
		assertEquals(0, votes[4].intValue());
		
		/*for (String value : voteSum.keySet()) {
			System.out.print(value);
			System.out.println(" - " + voteSum.get(value));
		}*/
	}
	
	@Test
	public void testFetchSumResult() {
		List<Map<String, Object>> list = activity.fetchSumResult();
		
		assertEquals(5, list.size());
		assertEquals("3", list.get(0).get("rank"));
		assertEquals("1", list.get(1).get("rank"));
		assertEquals("3", list.get(2).get("rank"));
		assertEquals("1", list.get(3).get("rank"));
		assertEquals("3", list.get(4).get("rank"));
		
		for (Map<String, Object> map : list) {
			System.out.print(map.get("topic")); // 題目
			System.out.print(" - " + map.get("numOfVotes")); // 得票數
			System.out.println(" - " + map.get("rank")); // 排名
		}
	}
	
	@Test
	public void testFetchTopic() {
		List<String> list = activity.fetchTopics();
		
		for (String topic : list) {
			System.out.println(topic); // 題目
		}
	}
	
}
