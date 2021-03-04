package com.tradevan.aporg.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.tradevan.apcommon.bean.CreateUserDto;
import com.tradevan.apcommon.persistence.GenericEntity;
import com.tradevan.apcommon.util.BeanUtil;

/**
 * Title: JobRank<br>
 * Description: <br>
 * Company: Tradevan Co.<br>
 * 
 * @author Neil Chen
 * @version 1.0
 */
@Entity
public class JobRank extends GenericEntity<Long> {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue
	@Column(name = "serNo", unique = true, nullable = false)
	private Long id;
	
	@Column(unique = true, nullable = false, length = 30)
	private String jobRankId;
	
	@Column(nullable = false, length = 50)
	private String jobRankName;
	
	@Column(nullable = false, length = 1)
	private String status;
	
	@Column(length = 30)
	private String createUserId;
	
	@Column(length = 30)
	private String updateUserId;
	
	@Column(length = 30)
	private String createAgentId;
	
	@Column(length = 30)
	private String updateAgentId;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable = false)
	private Date createTime;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable = false)
	private Date updateTime;
	
	public JobRank() {
		super();
	}

	public JobRank(String jobRankId, String jobRankName, String createUserId) {
		super();
		this.jobRankId = jobRankId;
		this.jobRankName = jobRankName;
		this.status = "Y";
		this.createUserId = createUserId;
		this.updateUserId = createUserId;
		Date now = new Date();
		this.createTime = now;
		this.updateTime = now;
	}
	
	public JobRank(String jobRankId, String jobRankName, CreateUserDto createUserDto) {
		super();
		this.jobRankId = jobRankId;
		this.jobRankName = jobRankName;
		this.status = "Y";
		BeanUtil.copyProperties(createUserDto, this);
	}
	
	@Override
	public Long getId() {
		return id;
	}
	
	@Override
	public void setId(Long id) {
		throw new IllegalArgumentException("The id can't be changed.");
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
