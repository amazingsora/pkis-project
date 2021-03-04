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
 * Title: SubJobType<br>
 * Description: <br>
 * Company: Tradevan Co.<br>
 * 
 * @author Neil Chen
 * @version 1.0
 */
@Entity
public class SubJobType extends GenericEntity<Long> {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue
	@Column(name = "serNo", unique = true, nullable = false)
	private Long id;
	
	@Column(unique = true, nullable = false, length = 30)
	private String subJobTypeId;
	
	@Column(nullable = false, length = 50)
	private String subJobTypeName;
	
	@Column(nullable = false, length = 30)
	private String jobTypeId;
	
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
	
	public SubJobType() {
		super();
	}

	public SubJobType(String subJobTypeId, String subJobTypeName, String jobTypeId, String createUserId) {
		super();
		this.subJobTypeId = subJobTypeId;
		this.subJobTypeName = subJobTypeName;
		this.jobTypeId = jobTypeId;
		this.status = "Y";
		this.createUserId = createUserId;
		this.updateUserId = createUserId;
		Date now = new Date();
		this.createTime = now;
		this.updateTime = now;
	}
	
	public SubJobType(String subJobTypeId, String subJobTypeName, String jobTypeId, CreateUserDto createUserDto) {
		super();
		this.subJobTypeId = subJobTypeId;
		this.subJobTypeName = subJobTypeName;
		this.jobTypeId = jobTypeId;
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

	public String getSubJobTypeId() {
		return subJobTypeId;
	}

	public void setSubJobTypeId(String subJobTypeId) {
		this.subJobTypeId = subJobTypeId;
	}

	public String getSubJobTypeName() {
		return subJobTypeName;
	}

	public void setSubJobTypeName(String subJobTypeName) {
		this.subJobTypeName = subJobTypeName;
	}

	public String getJobTypeId() {
		return jobTypeId;
	}

	public void setJobTypeId(String jobTypeId) {
		this.jobTypeId = jobTypeId;
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
