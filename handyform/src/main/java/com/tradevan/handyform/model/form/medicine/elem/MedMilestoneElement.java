package com.tradevan.handyform.model.form.medicine.elem;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.tradevan.handyform.enums.MedMilestoneType;

@JsonInclude(Include.NON_NULL)
public class MedMilestoneElement {
	private Integer id;
	private MedMilestoneType type;
	private String value;
	private Boolean Required;
	private String desc;
	private String level1;
	private String level2;
	private String level3;
	private String level4;
	private String level5;
	private String reply;
	private String rating;	
	private String comment;	

	public MedMilestoneElement() {
		super();
	}
	
	public MedMilestoneElement(Integer id, MedMilestoneType type, String value, Boolean required) {
		super();
		this.id = id;
		this.type = type;
		this.value = value;
		Required = required;
	}

	@JsonIgnore
	public BigDecimal calcRating() {
		int score = 0;
		if (reply != null && MedMilestoneType.ques09PointComment.name().equals(type.name())) {
			try {
				score += Integer.parseInt(reply);
			}
			catch (NumberFormatException e) {
				// Ignore it
			}
		}
		return new BigDecimal(score * 5).divide(new BigDecimal(9), 1, BigDecimal.ROUND_HALF_UP);
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public MedMilestoneType getType() {
		return type;
	}
	public void setType(MedMilestoneType type) {
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
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getLevel1() {
		return level1;
	}
	public void setLevel1(String level1) {
		this.level1 = level1;
	}
	public String getLevel2() {
		return level2;
	}
	public void setLevel2(String level2) {
		this.level2 = level2;
	}
	public String getLevel3() {
		return level3;
	}
	public void setLevel3(String level3) {
		this.level3 = level3;
	}
	public String getLevel4() {
		return level4;
	}
	public void setLevel4(String level4) {
		this.level4 = level4;
	}
	public String getLevel5() {
		return level5;
	}
	public void setLevel5(String level5) {
		this.level5 = level5;
	}
	public String getReply() {
		return reply;
	}
	public void setReply(String reply) {
		this.reply = reply;
	}
	public String getRating() {
		return rating;
	}
	public void setRating(String rating) {
		this.rating = rating;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
}
