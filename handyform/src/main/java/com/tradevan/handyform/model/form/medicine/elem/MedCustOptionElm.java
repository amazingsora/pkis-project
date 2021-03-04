package com.tradevan.handyform.model.form.medicine.elem;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class MedCustOptionElm {
	private String value;
	private Boolean isDefault;
	private Boolean withOthers;
	private BigDecimal scoreConv;
	private Boolean passConv;
	private String memo;
	private Boolean isNoScoring; // 是否為不計分項目
	
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public Boolean getIsDefault() {
		return isDefault;
	}
	public void setIsDefault(Boolean isDefault) {
		this.isDefault = isDefault;
	}
	public Boolean getWithOthers() {
		return withOthers;
	}
	public void setWithOthers(Boolean withOthers) {
		this.withOthers = withOthers;
	}
	public BigDecimal getScoreConv() {
		return scoreConv;
	}
	public void setScoreConv(BigDecimal scoreConv) {
		this.scoreConv = scoreConv;
	}
	public Boolean getPassConv() {
		return passConv;
	}
	public void setPassConv(Boolean passConv) {
		this.passConv = passConv;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public Boolean getIsNoScoring() {
		return isNoScoring;
	}
	public void setIsNoScoring(Boolean isNoScoring) {
		this.isNoScoring = isNoScoring;
	}
}
