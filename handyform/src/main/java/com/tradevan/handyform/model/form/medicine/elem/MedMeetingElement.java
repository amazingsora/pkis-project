package com.tradevan.handyform.model.form.medicine.elem;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.tradevan.handyform.enums.MedMeetingType;

@JsonInclude(Include.NON_NULL)
public class MedMeetingElement {
	private Integer id;
	private MedMeetingType type;
	private String value;
	private Integer max;
	private Boolean Required;
	private List<String> options;
	private String reply;
	private String extInfo;
	private String passportId;

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public MedMeetingType getType() {
		return type;
	}
	public void setType(MedMeetingType type) {
		this.type = type;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
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
	public String getExtInfo() {
		return extInfo;
	}
	public void setExtInfo(String extInfo) {
		this.extInfo = extInfo;
	}
	public String getPassportId() {
		return passportId;
	}
	public void setPassportId(String passportId) {
		this.passportId = passportId;
	}
}
