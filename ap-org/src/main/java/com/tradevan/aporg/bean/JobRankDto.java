package com.tradevan.aporg.bean;

import java.io.Serializable;
import java.util.Date;

import org.springframework.beans.BeanUtils;

import com.tradevan.aporg.model.JobRank;

public class JobRankDto implements Serializable{
	
	private static final long serialVersionUID = -5690869386349281656L;
	
	private Long id;	
	private String jobRankId;
	private String jobRankName;
	private String status;
	private String createUserId;
	private String updateUserId;
	private String createAgentId;
	private String updateAgentId;
	private Date createTime;
	private Date updateTime;
	
	public JobRankDto() {
		super();
	}
	
	public JobRankDto(JobRank jobRank) {
		BeanUtils.copyProperties(jobRank, this);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getJobRankId() {
		return jobRankId;
	}

	public void setJobRankId(String jobRankId) {
		this.jobRankId = jobRankId;
	}

	public String getJobRankName() {
		return jobRankName;
	}

	public void setJobRankName(String jobRankName) {
		this.jobRankName = jobRankName;
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
