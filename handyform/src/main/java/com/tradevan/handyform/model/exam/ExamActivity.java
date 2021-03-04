package com.tradevan.handyform.model.exam;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tradevan.handyform.enums.QuesType;
import com.tradevan.handyform.model.ques.QuesElement;
import com.tradevan.handyform.model.ques.QuesSatisfaction;

public class ExamActivity {
	private Map<String, Examination> examinations = new HashMap<String, Examination>();
	
	public ExamActivity(Map<String, Examination> examinations) {
		super();
		this.examinations = examinations;
	}

	/**
	 * 計算平均分數
	 * @return
	 */
	public BigDecimal calcAvgScore() {
		int totalScore = 0;
		for (Examination obj : examinations.values()) {
			totalScore += obj.calcScore();
		}
		return (examinations.size() == 0) ? BigDecimal.ZERO : new BigDecimal(totalScore).divide(new BigDecimal(examinations.size()), 1, BigDecimal.ROUND_HALF_UP);
	}
	
	/**
	 * 取得單一筆試卷
	 * @return
	 */
	public Examination getQuesSatisfaction(String userId) {
		return examinations.get(userId);
	}
	
	
}
