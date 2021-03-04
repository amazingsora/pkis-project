package com.tradevan.handyform.model.ques;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class QuesVote {
	private Integer voteNum;
	private List<QuesElement> secVotes;
	
	/**
	 * 取得單一題目回覆結果
	 */
	public String getVoteReply(String topic) {
		for (QuesElement elm : secVotes) {
			if (topic.equals(elm.getValue())) {
				return elm.getReply();
			}
		}
		return "";
	}
	
	/**
	 * 設定單一題目回覆結果
	 */
	public void setVoteReply(String topic, String reply) {
		for (QuesElement elm : secVotes) {
			if (topic.equals(elm.getValue())) {
				elm.setReply(reply);
			}
		}
	}
	
	public Integer getVoteNum() {
		return voteNum;
	}
	public void setVoteNum(Integer voteNum) {
		this.voteNum = voteNum;
	}
	public List<QuesElement> getSecVotes() {
		return secVotes;
	}
	public void setSecVotes(List<QuesElement> secVotes) {
		this.secVotes = secVotes;
	}
}
