package com.tradevan.handyform.model.form.medicine;

import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.tradevan.handyform.model.form.medicine.elem.MedOtherScElement;

@JsonInclude(Include.NON_NULL)
public class MedFormOtherSc extends BaseMedForm {
	private List<MedOtherScElement> secScoring;
	private List<MedOtherScElement> secNonScoring;
	private Boolean satisfactionOnOff;
	private MedOtherScElement satisfaction;
	private MedOtherScElement satisfaction2;
	
	@JsonIgnore
	@Override
	public BigDecimal calcScore() {
		int score = 0;
		for (MedOtherScElement elm : secScoring) {
			if (elm.getReply() != null && elm.getReply().equals(elm.getAnswer()) && elm.getPercent() != null) {
				score += elm.getPercent();
			}
		}
		return new BigDecimal(score);
	}
	
	public List<MedOtherScElement> getSecScoring() {
		return secScoring;
	}
	public void setSecScoring(List<MedOtherScElement> secScoring) {
		this.secScoring = secScoring;
	}
	public List<MedOtherScElement> getSecNonScoring() {
		return secNonScoring;
	}
	public void setSecNonScoring(List<MedOtherScElement> secNonScoring) {
		this.secNonScoring = secNonScoring;
	}
	public Boolean getSatisfactionOnOff() {
		return satisfactionOnOff;
	}
	public void setSatisfactionOnOff(Boolean satisfactionOnOff) {
		this.satisfactionOnOff = satisfactionOnOff;
	}
	public MedOtherScElement getSatisfaction() {
		return satisfaction;
	}
	public void setSatisfaction(MedOtherScElement satisfaction) {
		this.satisfaction = satisfaction;
	}
	public MedOtherScElement getSatisfaction2() {
		return satisfaction2;
	}
	public void setSatisfaction2(MedOtherScElement satisfaction2) {
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
