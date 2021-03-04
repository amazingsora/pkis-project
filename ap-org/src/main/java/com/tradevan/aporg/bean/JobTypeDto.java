package com.tradevan.aporg.bean;

import java.io.Serializable;
import java.util.Date;

import org.springframework.beans.BeanUtils;

import com.tradevan.aporg.model.JobType;

public class JobTypeDto implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private Long id;	
	private String jobTypeId;
	private String jobTypeName;
	private String status;
	private String createUserId;
	private String updateUserId;
	private String createAgentId;
	private String updateAgentId;
	private Date createTime;
	private Date updateTime;
	
	public JobTypeDto() {
		super();
	}
	
	public JobTypeDto(JobType jobType) {
		BeanUtils.copyProperties(jobType, this);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getJobTypeId() {
		return jobTypeId;
	}

	public void setJobTypeId(String jobTypeId) {
		this.jobTypeId = jobTypeId;
	}

	public String getJobTypeName() {
		return jobTypeName;
	}

	public void setJobTypeName(String jobTypeName) {
		this.jobTypeName = jobTypeName;
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

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
}
