package com.tradevan.handyform.model.form.medicine;

import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.tradevan.handyform.enums.MedAssessmentType;
import com.tradevan.handyform.model.form.medicine.elem.MedAssessmentElement;

@JsonInclude(Include.NON_NULL)
public class MedFormAssessment extends BaseMedForm {
	private MedAssessmentElement period;
	private List<MedAssessmentElement> secEvalItems;
	private List<MedAssessmentElement> secEvalRangeItems;
	private MedAssessmentElement finalComment;
	private Boolean satisfactionOnOff;
	private MedAssessmentElement satisfaction;
	private MedAssessmentElement satisfaction2;
	
	@JsonIgnore
	@Override
	public BigDecimal calcScore() { // 受評人分數
		int totalNum = 0;
		int score = 0;
		for (MedAssessmentElement elm : secEvalItems) {
			if (elm.getReply() != null && MedAssessmentType.ques5Point.name().equals(elm.getType().name())) {
				try {
					score += Integer.parseInt(elm.getReply());
				}
				catch (NumberFormatException e) {
					// Ignore it
				}
				totalNum++;
			}
		}
		BigDecimal rtnScore = (totalNum == 0) ? BigDecimal.ZERO : new BigDecimal(score * 100).divide(new BigDecimal(totalNum * 5), 1, BigDecimal.ROUND_HALF_UP);
		
		for (MedAssessmentElement elm : secEvalRangeItems) {
			if (elm.getReply() != null && MedAssessmentType.quesNumRangeRank.name().equals(elm.getType().name())) {
				try {
					int reply = Integer.parseInt(elm.getReply());
					rtnScore = rtnScore.add(new BigDecimal(reply));
				}
				catch (NumberFormatException e) {
					// Ignore it
				}
			}
		}
		return rtnScore;
	}
	
	@JsonIgnore
	@Override
	public Integer calcSatisfaction() { // 受評人滿意度
		if (satisfaction != null) {
			try {
				return Integer.parseInt(satisfaction.getReply());
			}
			catch (NumberFormatException e) {
				// Ignore it
			}
		}
		return null;
	}
	
	@JsonIgnore
	@Override
	public Integer calcSatisfaction2() { // 評量人滿意度
		if (satisfaction2 != null) {
			try {
				return Integer.parseInt(satisfaction2.getReply());
			}
			catch (NumberFormatException e) {
				// Ignore it
			}
		}
		return null;
	}
	
	public MedAssessmentElement getPeriod() {
		return period;
	}
	public void setPeriod(MedAssessmentElement period) {
		this.period = period;
	}
	public List<MedAssessmentElement> getSecEvalItems() {
		return secEvalItems;
	}
	public void setSecEvalItems(List<MedAssessmentElement> secEvalItems) {
		this.secEvalItems = secEvalItems;
	}
	public List<MedAssessmentElement> getSecEvalRangeItems() {
		return secEvalRangeItems;
	}
	public void setSecEvalRangeItems(List<MedAssessmentElement> secEvalRangeItems) {
		this.secEvalRangeItems = secEvalRangeItems;
	}
	public MedAssessmentElement getFinalComment() {
		return finalComment;
	}
	public void setFinalComment(MedAssessmentElement finalComment) {
		this.finalComment = finalComment;
	}
	public Boolean getSatisfactionOnOff() {
		return satisfactionOnOff;
	}
	public void setSatisfactionOnOff(Boolean satisfactionOnOff) {
		this.satisfactionOnOff = satisfactionOnOff;
	}
	public MedAssessmentElement getSatisfaction() {
		return satisfaction;
	}
	public void setSatisfaction(MedAssessmentElement satisfaction) {
		this.satisfaction = satisfaction;
	}
	public MedAssessmentElement getSatisfaction2() {
		return satisfaction2;
	}
	public void setSatisfaction2(MedAssessmentElement satisfaction2) {
		this.satisfaction2 = satisfaction2;
	}
	
	@JsonIgnore
	@Override
	public boolean hasForReview() {
		if (satisfactionOnOff != null && satisfactionOnOff) {
			return true;
		}
		return false;
	}
}
