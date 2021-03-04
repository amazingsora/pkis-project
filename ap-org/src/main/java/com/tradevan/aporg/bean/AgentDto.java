package com.tradevan.aporg.bean;

import java.io.Serializable;

import org.springframework.beans.BeanUtils;

import com.tradevan.apcommon.util.DateUtil;
import com.tradevan.aporg.model.AgentProf;

/**
 * Title: AgentDto<br>
 * Description: <br>
 * Company: Tradevan Co.<br>
 * 
 * @author Neil Chen
 * @version 1.0
 */
public class AgentDto implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Long id;
	
	private String userId;
	
	private String agentUserId;
	
	private String beginDate;
	
	private String beginTime;
	
	private String endDate;
	
	private String endTime;
	
	private String status;
	
	private String createUserId;
	
	private String updateUserId;
	
	private String createAgentId;
	
	private String updateAgentId;
	
	private String createTime;
	
	private String updateTime;
	
	public AgentDto() {
	}

	public AgentDto(AgentProf agent) {
		BeanUtils.copyProperties(agent, this);
		setBeginDate(DateUtil.formatDate(agent.getBegDate(), DateUtil.FMT_YYYY_MM_DD));
		setBeginTime(DateUtil.formatDate(agent.getBegDate(), DateUtil.FMT_HH_MM));
		setEndDate(DateUtil.formatDate(agent.getEndDate(), DateUtil.FMT_YYYY_MM_DD));
		setEndTime(DateUtil.formatDate(agent.getEndDate(), DateUtil.FMT_HH_MM));
		setCreateTime(DateUtil.formatDate(agent.getCreateTime(), DateUtil.FMT_YYYY_MM_DD_HH_MM_SS));
		setUpdateTime(DateUtil.formatDate(agent.getUpdateTime(), DateUtil.FMT_YYYY_MM_DD_HH_MM_SS));
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getAgentUserId() {
		return agentUserId;
	}

	public void setAgentUserId(String agentUserId) {
		this.agentUserId = agentUserId;
	}

	public String getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(String beginDate) {
		this.beginDate = beginDate;
	}

	public String getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(String beginTime) {
		this.beginTime = beginTime;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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

	public String getCreateAgentId() {
		return createAgentId;
	}

	public void setCreateAgentId(String createAgentId) {
		this.createAgentId = createAgentId;
	}

	public String getUpdateAgentId() {
		return updateAgentId;
	}

	public void setUpdateAgentId(String updateAgentId) {
		this.updateAgentId = updateAgentId;
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
}
