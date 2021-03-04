package com.tradevan.handyform.model.ques;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.tradevan.handyform.enums.QuesType;

@JsonInclude(Include.NON_NULL)
public class QuesElement {
	private Integer id;
	private QuesType type;
	private String value;
	private Boolean Required;
	private List<String> options;
	private String reply;
	private String revise;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public QuesType getType() {
		return type;
	}
	public void setType(QuesType type) {
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
	public String getRevise() {
		return revise;
	}
	public void setRevise(String revise) {
		this.revise = revise;
	}
}
