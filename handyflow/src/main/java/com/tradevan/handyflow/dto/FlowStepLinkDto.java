package com.tradevan.handyflow.dto;

import java.io.Serializable;
import java.util.List;

import org.springframework.beans.BeanUtils;

import com.tradevan.apcommon.bean.NameValuePair;
import com.tradevan.apcommon.util.DateUtil;
import com.tradevan.handyflow.model.form.FlowStepLink;

public class FlowStepLinkDto implements Serializable  {

	private static final long serialVersionUID = 1L;
	
	private Long id;
	
	private String flowId;
	
	private String stepId;
	
	private String action;
	
	private String concurrent;
	
	private String toStepId;
	
	private String name;
	
	private String desc;
	
	private Integer dispOrd;
	
	private String createUserId;
	
	private String updateUserId;
	
	private String createTime;
	
	private String updateTime;

	/**----------  下拉單  -------------------**/
	private List<NameValuePair> toStepIdList; //下一關卡代號
	
	private List<NameValuePair> actionList; //動作
	
	private List<NameValuePair> concurrentList; //同時會簽
	
	private String flowName; //流程名稱
	
	private String stepName; //關卡名稱
	
	private Integer maxDispOrd; //最大排序
	
	private String showConcurrent; //是否顯示同時會簽欄位，Y:是;N:否(只有flowStep.stepType="P"才顯示)
	
	private String showAction; //是否顯示動作下拉單，Y:是;N:否(flowStep.stepType="P"不顯示)
	
	
	public FlowStepLinkDto() {
		
	}

	public FlowStepLinkDto(FlowStepLink link) {
		BeanUtils.copyProperties(link, this);
		setConcurrent(link.getIsConcurrent() == true ? "Y" : "N");
		setCreateTime(DateUtil.formatDate(link.getCreateTime(), DateUtil.FMT_YYYY_MM_DD_HH_MM_SS));
		setUpdateTime(DateUtil.formatDate(link.getUpdateTime(), DateUtil.FMT_YYYY_MM_DD_HH_MM_SS));
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

	public String getStepId() {
		return stepId;
	}

	public void setStepId(String stepId) {
		this.stepId = stepId;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getToStepId() {
		return toStepId;
	}

	public void setToStepId(String toStepId) {
		this.toStepId = toStepId;
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

	public Integer getDispOrd() {
		return dispOrd;
	}

	public void setDispOrd(Integer dispOrd) {
		this.dispOrd = dispOrd;
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

	public List<NameValuePair> getToStepIdList() {
		return toStepIdList;
	}

	public void setToStepIdList(List<NameValuePair> toStepIdList) {
		this.toStepIdList = toStepIdList;
	}

	public List<NameValuePair> getActionList() {
		return actionList;
	}

	public void setActionList(List<NameValuePair> actionList) {
		this.actionList = actionList;
	}

	public String getConcurrent() {
		return concurrent;
	}

	public void setConcurrent(String concurrent) {
		this.concurrent = concurrent;
	}

	public List<NameValuePair> getConcurrentList() {
		return concurrentList;
	}

	public void setConcurrentList(List<NameValuePair> concurrentList) {
		this.concurrentList = concurrentList;
	}

	public String getFlowName() {
		return flowName;
	}

	public void setFlowName(String flowName) {
		this.flowName = flowName;
	}

	public String getStepName() {
		return stepName;
	}

	public void setStepName(String stepName) {
		this.stepName = stepName;
	}

	public Integer getMaxDispOrd() {
		return maxDispOrd;
	}

	public void setMaxDispOrd(Integer maxDispOrd) {
		this.maxDispOrd = maxDispOrd;
	}

	public String getShowConcurrent() {
		return showConcurrent;
	}

	public void setShowConcurrent(String showConcurrent) {
		this.showConcurrent = showConcurrent;
	}

	public String getShowAction() {
		return showAction;
	}

	public void setShowAction(String showAction) {
		this.showAction = showAction;
	}
	
}
