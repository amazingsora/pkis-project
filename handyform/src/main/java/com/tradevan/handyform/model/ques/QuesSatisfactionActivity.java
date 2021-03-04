package com.tradevan.handyform.model.ques;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tradevan.handyform.enums.QuesType;

public class QuesSatisfactionActivity {
	private Map<String, QuesSatisfaction> quesSatisfactions = new HashMap<String, QuesSatisfaction>();
	
	public QuesSatisfactionActivity(Map<String, QuesSatisfaction> quesSatisfactions) {
		super();
		this.quesSatisfactions = quesSatisfactions;
	}

	/**
	 * 計算平均分數
	 * @return
	 */
	public BigDecimal calcAvgScore() {
		BigDecimal totalScore = new BigDecimal(0);
		for (QuesSatisfaction obj : quesSatisfactions.values()) {
			totalScore = totalScore.add(obj.calcScore());
		}
		return (quesSatisfactions.size() == 0) ? BigDecimal.ZERO : totalScore.divide(new BigDecimal(quesSatisfactions.size()), 1, BigDecimal.ROUND_HALF_UP);
	}
	
	/**
	 * 取得題目清單
	 * @return
	 */
	public List<String> fetchTopics() {
		List<String> list = new ArrayList<String>();
		
		for (QuesSatisfaction satis : quesSatisfactions.values()) {
			for (QuesElement elm : satis.getSecScoring()) {
				if (elm.getType() == QuesType.ques5Point) {
					list.add(elm.getValue());
				}
			}
			for (QuesElement elm : satis.getSecNonScoring()) {
				if (elm.getType() != QuesType.caption) {
					list.add(elm.getValue());
				}
			}
		}
		return list;
	}
	
	public List<String> fetchSecScoringTopics() {
		List<String> list = new ArrayList<String>();
		
		for (QuesSatisfaction satis : quesSatisfactions.values()) {
			for (QuesElement elm : satis.getSecScoring()) {
				if (elm.getType() == QuesType.ques5Point) {
					list.add(elm.getValue());
				}
			}
		}
		return list;
	}
	
	public List<String> fetchSecNonScoringTopics() {
		List<String> list = new ArrayList<String>();
		
		for (QuesSatisfaction satis : quesSatisfactions.values()) {
			for (QuesElement elm : satis.getSecNonScoring()) {
				if (elm.getType() != QuesType.caption) {
					list.add(elm.getValue());
				}
			}
		}
		return list;
	}
	
	/**
	 * 取得單一筆問卷
	 * @return
	 */
	public QuesSatisfaction getQuesSatisfaction(String userId) {
		return quesSatisfactions.get(userId);
	}
	
}
