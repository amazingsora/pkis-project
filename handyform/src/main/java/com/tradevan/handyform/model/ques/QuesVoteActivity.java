package com.tradevan.handyform.model.ques;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Title: QuesVoteActivity<br>
 * Description: <br>
 * Company: Tradevan Co.<br>
 * 
 * @author Neil Chen
 * @version 1.0
 */
public class QuesVoteActivity {
	private Map<String, QuesVote> quesVotes = new HashMap<String, QuesVote>();
	private Map<String, Integer> voteSum;
	
	public QuesVoteActivity(Map<String, QuesVote> quesVotes) {
		super();
		this.quesVotes = quesVotes;
	}
	
	/**
	 * 計算得票數
	 * @return
	 */
	public Map<String, Integer> calcVoteSum() {
		voteSum = new LinkedHashMap<String, Integer>();
		for (QuesVote vote : quesVotes.values()) {
			for (QuesElement elm : vote.getSecVotes()) {
				String value = elm.getValue();
				if (voteSum.containsKey(value) && "1".equals(elm.getReply())) {
					Integer n = voteSum.get(value);
					voteSum.replace(value, ++n);
				}
				else {
					if ("1".equals(elm.getReply())) {
						voteSum.put(value, 1);
					}
					else {
						voteSum.put(value, 0);
					}
				}
			}
		}
		return voteSum;
	}
	
	/**
	 * 取得得票數及排名
	 * @return
	 */
	public List<Map<String, Object>> fetchSumResult() {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		if (voteSum == null) {
			voteSum = calcVoteSum();
		}
		
		Integer[] sortArr = voteSum.values().toArray(new Integer[0]);
		Arrays.sort(sortArr);
		
		for (Entry<String, Integer> entry : voteSum.entrySet()) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("topic", entry.getKey());
			map.put("numOfVotes", entry.getValue().toString());
			map.put("rank", String.valueOf(calcRank(entry.getValue(), sortArr)));
			list.add(map);
		}
		return list;
	}
	
	private int calcRank(Integer numOfVotes, Integer[] sortArr) {
		int rank = 1, total = sortArr.length;
		if (numOfVotes == sortArr[total-1]) {
			return rank;
		}
		for (int i = rank; i < total; i++) {
			if (sortArr[i-1] != sortArr[i] || sortArr[i-1] == sortArr[i]) {
				rank++;
			}
			if (numOfVotes == sortArr[i]) {
				return ++rank;
			}
		}
		return 0;
    }
	
	/**
	 * 取得題目清單
	 * @return
	 */
	public List<String> fetchTopics() {
		List<String> list = new ArrayList<String>();
		if (voteSum == null) {
			voteSum = calcVoteSum();
		}
		
		for (String topic : voteSum.keySet()) {
			list.add(topic);
		}
		return list;
	}
	
	/**
	 * 取得單一筆問卷
	 * @return
	 */
	public QuesVote getQuesVote(String userId) {
		return quesVotes.get(userId);
	}
	
}
