package com.tradevan.handyform.model.form.medicine.elem;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class MedCustEvalElm {
	private String value;
	private String reply;
	private String reply2;
	private String replyScore;
	private String reply2Score;
	private String memo;
	private Map<String, String> memoMap;
	private Boolean isNoScoring; // 是否為多項評量不計分項目
	
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getReply() {
		return reply;
	}
	public void setReply(String reply) {
		this.reply = reply;
	}
	public String getReply2() {
		return reply2;
	}
	public void setReply2(String reply2) {
		this.reply2 = reply2;
	}
	public String getReplyScore() {
		return replyScore;
	}
	public void setReplyScore(String replyScore) {
		this.replyScore = replyScore;
	}
	public String getReply2Score() {
		return reply2Score;
	}
	public void setReply2Score(String reply2Score) {
		this.reply2Score = reply2Score;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public Map<String, String> getMemoMap() {
		return memoMap;
	}
	public void setMemoMap(Map<String, String> memoMap) {
		this.memoMap = memoMap;
	}
	public Boolean getIsNoScoring() {
		return isNoScoring;
	}
	public void setIsNoScoring(Boolean isNoScoring) {
		this.isNoScoring = isNoScoring;
	}
	
	@JsonIgnore
	public String convertReplyScore(List<MedCustOptionElm> options) { // 將評量項目(初審)選項轉換為分數
		replyScore = convertToScore(options, reply);
		return replyScore;
	}
	
	@JsonIgnore
	public String convertReply2Score(List<MedCustOptionElm> options) { // 將評量項目(複審)選項轉換為分數
		this.reply2Score = convertToScore(options, this.reply2);
		return this.reply2Score;
	}
	
	@JsonIgnore
	private String convertToScore(List<MedCustOptionElm> options, String reply) {
		if (reply != null && options != null) {
			for (MedCustOptionElm elm : options) {
				if (reply.equals(elm.getValue())) {
					if (elm.getScoreConv() != null) {
						return String.valueOf(elm.getScoreConv());
					}
				}
			}
		}
		return null;
	}
}
