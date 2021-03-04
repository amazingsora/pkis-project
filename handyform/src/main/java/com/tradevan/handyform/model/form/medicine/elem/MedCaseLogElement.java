package com.tradevan.handyform.model.form.medicine.elem;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.tradevan.handyform.enums.MedCaseLogType;

@JsonInclude(Include.NON_NULL)
public class MedCaseLogElement {
	private Integer id;
	private MedCaseLogType type;
	private String value;
	private Integer caseNum;
	private Integer max;
	private Boolean Required;
	private List<String> options;
	private String reply;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public MedCaseLogType getType() {
		return type;
	}
	public void setType(MedCaseLogType type) {
		this.type = type;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public Integer getCaseNum() {
		return caseNum;
	}
	public void setCaseNum(Integer caseNum) {
		this.caseNum = caseNum;
	}
	public Integer getMax() {
		return max;
	}
	public void setMax(Integer max) {
		this.max = max;
	}
	public Boolean getRequired() {
		return Required;
	}
	public void setRequired(Boolean required) {
		Required = required;
	}
	public List<String> getOptions() {
		return options;
	}
	public void setOptions(List<String> options) {
		this.options = options;
	}
	public String getReply() {
		return reply;
	}
	public void setReply(String reply) {
		this.reply = reply;
	}
}
