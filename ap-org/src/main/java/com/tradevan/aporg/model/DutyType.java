package com.tradevan.aporg.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.tradevan.apcommon.persistence.GenericEntity;

/**
 * Title: DutyType<br>
 * Description: <br>
 * Company: Tradevan Co.<br>
 * 
 * @author Neil Chen
 * @version 1.0
 */
@Entity
public class DutyType extends GenericEntity<Long> {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue
	@Column(name = "serNo", unique = true, nullable = false)
	private Long id;
	
	@Column(unique = true, nullable = false, length = 30)
	private String dutyTypeId;
	
	@Column(nullable = false, length = 50)
	private String dutyTypeName;
	
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
	
	public DutyType() {
		super();
	}

	public DutyType(String dutyTypeId, String dutyTypeName, String createUserId) {
		super();
		this.dutyTypeId = dutyTypeId;
		this.dutyTypeName = dutyTypeName;
		this.status = "Y";
		this.createUserId = createUserId;
		this.updateUserId = createUserId;
		Date now = new Date();
		this.createTime = now;
		this.updateTime = now;
	}
	
	@Override
	public Long getId() {
		return id;
	}
	
	@Override
	public void setId(Long id) {
		throw new IllegalArgumentException("The id can't be changed.");
	}

	public String getDutyTypeId() {
		return dutyTypeId;
	}

	public void setDutyTypeId(String dutyTypeId) {
		this.dutyTypeId = dutyTypeId;
	}

	public String getDutyTypeName() {
		return dutyTypeName;
	}

	public void setDutyTypeName(String dutyTypeName) {
		this.dutyTypeName = dutyTypeName;
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
