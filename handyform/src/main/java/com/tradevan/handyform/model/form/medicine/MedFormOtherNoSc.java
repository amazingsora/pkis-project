package com.tradevan.handyform.model.form.medicine;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.tradevan.handyform.model.form.medicine.elem.MedOtherNoScElement;

@JsonInclude(Include.NON_NULL)
public class MedFormOtherNoSc extends BaseMedForm {
	private List<MedOtherNoScElement> secNonScoring;
	
	public List<MedOtherNoScElement> getSecNonScoring() {
		return secNonScoring;
	}
	public void setSecNonScoring(List<MedOtherNoScElement> secNonScoring) {
		this.secNonScoring = secNonScoring;
	}
}
