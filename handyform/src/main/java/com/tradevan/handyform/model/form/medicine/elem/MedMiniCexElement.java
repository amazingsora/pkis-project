package com.tradevan.handyform.model.form.medicine.elem;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.tradevan.handyform.enums.MedMiniCexType;

@JsonInclude(Include.NON_NULL)
public class MedMiniCexElement {
	private Integer id;
	private MedMiniCexType type;
	private String value;
	private Boolean Required;
	private List<String> options;
	private String reply;
	private String other;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public MedMiniCexType getType() {
		return type;
	}
	public void setType(MedMiniCexType type) {
		this.type = type;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
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
	public String getOther() {
		return other;
	}
	public void setOther(String other) {
		this.other = other;
	}
}
