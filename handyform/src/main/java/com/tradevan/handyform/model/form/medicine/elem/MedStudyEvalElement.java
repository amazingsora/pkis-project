package com.tradevan.handyform.model.form.medicine.elem;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.tradevan.handyform.enums.MedStudyEvalType;

@JsonInclude(Include.NON_NULL)
public class MedStudyEvalElement {
	private Integer id;
	private MedStudyEvalType type;
	private String value;
	private String phase1;
	private String phase2;
	private Boolean Required;
	private String reply1;
	private String reply2;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public MedStudyEvalType getType() {
		return type;
	}
	public void setType(MedStudyEvalType type) {
		this.type = type;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getPhase1() {
		return phase1;
	}
	public void setPhase1(String phase1) {
		this.phase1 = phase1;
	}
	public String getPhase2() {
		return phase2;
	}
	public void setPhase2(String phase2) {
		this.phase2 = phase2;
	}
	public Boolean getRequired() {
		return Required;
	}
	public void setRequired(Boolean required) {
		Required = required;
	}
	public String getReply1() {
		return reply1;
	}
	public void setReply1(String reply1) {
		this.reply1 = reply1;
	}
	public String getReply2() {
		return reply2;
	}
	public void setReply2(String reply2) {
		this.reply2 = reply2;
	}
}
