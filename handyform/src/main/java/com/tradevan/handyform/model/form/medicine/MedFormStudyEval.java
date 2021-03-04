package com.tradevan.handyform.model.form.medicine;

import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.tradevan.handyform.enums.MedStudyEvalType;
import com.tradevan.handyform.model.form.medicine.elem.MedStudyEvalElement;

@JsonInclude(Include.NON_NULL)
public class MedFormStudyEval extends BaseMedForm {
	private MedStudyEvalElement phase;
	private List<MedStudyEvalElement> secEvalItems;
	private Boolean commentOnOff;
	private MedStudyEvalElement comment;
	
	@JsonIgnore
	@Override
	public BigDecimal calcScore() { // 受評人分數
		int totalNum = 0;
		int score = 0;
		for (MedStudyEvalElement elm : secEvalItems) {
			if (elm.getReply1() != null && MedStudyEvalType.ques5Point2.name().equals(elm.getType().name())) {
				try {
					score += Integer.parseInt(elm.getReply1());
				}
				catch (NumberFormatException e) {
					// Ignore it
				}
				totalNum++;
			}
		}
		return (totalNum == 0) ? BigDecimal.ZERO : new BigDecimal(score * 100).divide(new BigDecimal(totalNum * 5), 1, BigDecimal.ROUND_HALF_UP);
	}
	
	@JsonIgnore
	@Override
	public BigDecimal calcScore2() { // 評量人分數
		int totalNum = 0;
		int score = 0;
		for (MedStudyEvalElement elm : secEvalItems) {
			if (elm.getReply2() != null && MedStudyEvalType.ques5Point2.name().equals(elm.getType().name())) {
				try {
					score += Integer.parseInt(elm.getReply2());
				}
				catch (NumberFormatException e) {
					// Ignore it
				}
				totalNum++;
			}
		}
		return (totalNum == 0) ? BigDecimal.ZERO : new BigDecimal(score * 100).divide(new BigDecimal(totalNum * 5), 1, BigDecimal.ROUND_HALF_UP);
	}
	
	public MedStudyEvalElement getPhase() {
		return phase;
	}
	public void setPhase(MedStudyEvalElement phase) {
		this.phase = phase;
	}
	public List<MedStudyEvalElement> getSecEvalItems() {
		return secEvalItems;
	}
	public void setSecEvalItems(List<MedStudyEvalElement> secEvalItems) {
		this.secEvalItems = secEvalItems;
	}
	public Boolean getCommentOnOff() {
		return commentOnOff;
	}
	public void setCommentOnOff(Boolean commentOnOff) {
		this.commentOnOff = commentOnOff;
	}
	public MedStudyEvalElement getComment() {
		return comment;
	}
	public void setComment(MedStudyEvalElement comment) {
		this.comment = comment;
	}
}
