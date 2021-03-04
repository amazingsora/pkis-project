package com.tradevan.handyflow.dto;

import java.io.Serializable;

import org.springframework.beans.BeanUtils;

import com.tradevan.apcommon.util.DateUtil;
import com.tradevan.handyflow.model.form.SubFlowConf;

public class SubFlowConfDto implements Serializable  {

	private static final long serialVersionUID = 1L;
	
	private Long id;
	
	private String flowId;
	
	private String subFlowId;
	
	private String name;
	
	private String desc;
	
	private String finishTo;
	
	private String returnTo;
	
	private String createUserId;
	
	private String updateUserId;
	
	private String createTime;
	
	private String updateTime;

	private String showSelect; //顯示下拉單，Y:是;N:否
	
	
	public SubFlowConfDto() {
		
	}
	
	public SubFlowConfDto(SubFlowConf subFlowConf) {
		BeanUtils.copyProperties(subFlowConf, this);
		setCreateTime(DateUtil.formatDate(subFlowConf.getCreateTime(), DateUtil.FMT_YYYY_MM_DD_HH_MM_SS));
		setUpdateTime(DateUtil.formatDate(subFlowConf.getUpdateTime(), DateUtil.FMT_YYYY_MM_DD_HH_MM_SS));
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFlowId() {
		return flowId;
	}

	public void setFlowId(String flowId) {
		this.flowId = flowId;
	}

	public String getSubFlowId() {
		return subFlowId;
	}

	public void setSubFlowId(String subFlowId) {
		this.subFlowId = subFlowId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getFinishTo() {
		return finishTo;
	}

	public void setFinishTo(String finishTo) {
		this.finishTo = finishTo;
	}

	public String getReturnTo() {
		return returnTo;
	}

	public void setReturnTo(String returnTo) {
		this.returnTo = returnTo;
	}

	public String getCreateUserId() {
		return createUserId;
	}

	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}

	public String getUpdateUserId() {
		return updateUserId;
	}

	public void setUpdateUserId(String updateUserId) {
		this.updateUserId = updateUserId;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public String getShowSelect() {
		return showSelect;
	}

	public void setShowSelect(String showSelect) {
		this.showSelect = showSelect;
	}
	
}
