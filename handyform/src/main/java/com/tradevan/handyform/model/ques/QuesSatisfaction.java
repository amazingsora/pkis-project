package com.tradevan.handyform.model.ques;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.tradevan.handyform.enums.QuesType;

@JsonInclude(Include.NON_NULL)
public class QuesSatisfaction {
	private List<QuesElement> secScoring;
	private List<QuesElement> secNonScoring;
	
	/**
	 * 取得單一題目回覆結果
	 */
	@JsonIgnore
	public String getSatisfactionReply(String topic) {
		for (QuesElement elm : secScoring) {
			if (topic.equals(elm.getValue())) {
				return elm.getReply();
			}
		}
		for (QuesElement elm : secNonScoring) {
			if (topic.equals(elm.getValue())) {
				return elm.getReply();
			}
		}
		return "";
	}
	
	/**
	 * 設定單一題目回覆結果
	 */
	@JsonIgnore
	public void setSatisfactionReply(String topic, String reply) {
		for (QuesElement elm : secScoring) {
			if (topic.equals(elm.getValue())) {
				elm.setReply(reply);
				return;
			}
		}
		for (QuesElement elm : secNonScoring) {
			if (topic.equals(elm.getValue())) {
				elm.setReply(reply);
				return;
			}
		}
	}
	
	@JsonIgnore
	public BigDecimal calcScore() {
		int totalNum = 0;
		int score = 0;
		for (QuesElement elm : secScoring) {
			if (elm.getReply() != null && QuesType.ques5Point.name().equals(elm.getType().name())) {
				try {
					score += Integer.parseInt(elm.getReply());
				}
				catch (NumberFormatException e) {
					// Ignore it
				}
				totalNum++;
			}
		}
		return (totalNum == 0) ? BigDecimal.ZERO : new BigDecimal(score * 100).divide(new BigDecimal(totalNum * 5), 1, BigDecimal.ROUND_HALF_UP);
	}
	
	public List<QuesElement> getSecScoring() {
		return secScoring;
	}
	public void setSecScoring(List<QuesElement> secScoring) {
		this.secScoring = secScoring;
	}
	public List<QuesElement> getSecNonScoring() {
		return secNonScoring;
	}
	public void setSecNonScoring(List<QuesElement> secNonScoring) {
		this.secNonScoring = secNonScoring;
	}
	
	public List<String> fetchSecScoringTopics() {
		return fetchSecScoringTopics(false);
	}
	
	public List<String> fetchSecScoringTopics(boolean forSample) {
		List<String> list = new ArrayList<String>();
		int i=0;
		for (QuesElement elm : secScoring) {
			if (elm.getType() == QuesType.ques5Point) {
				i++;
				if (forSample) {
					list.add(i+"."+elm.getValue()+"^"+elm.getId());
				}else {
					list.add(elm.getValue());
				}
			}
		}
		return list;
	}
	
	public List<String> fetchSecNonScoringTopics() {
		return fetchSecNonScoringTopics(false);
	}
	
	public List<String> fetchSecNonScoringTopics(boolean forSample) {
		List<String> list = new ArrayList<String>();
		int i=0;
		for (QuesElement elm : secNonScoring) {
			if (elm.getType() != QuesType.caption) {
				i++;
				if (forSample) {
					list.add(i+"."+elm.getValue()+"^"+elm.getId());
				}else {
					list.add(elm.getValue());
				}
			}
		}
		return list;
	}
}
