package com.tradevan.aporg.model;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

import com.tradevan.apcommon.persistence.GenericEntity;

@Entity
//@Table(uniqueConstraints = @UniqueConstraint(columnNames = { "userId","natiChiDesc","schoDesc","deptDesc","educDesc","educStDate","educEndDate" }))
@Table
public class UserEducation extends GenericEntity<Long> {
	private static final long serialVersionUID = 1L;

	private Long id;
	private String userId;
	private String natiChiDesc;
	private String schoDesc;
	private String deptDesc;
	private String educDesc;
	private String educStDate;
	private String educEndDate;
	private String createUserId;
	private String updateUserId;
	private Date createTime;
	private Date updateTime;
	private String sysMemo;
	private String createAgentId;
	private String updateAgentId;
	
	public UserEducation() {
		
	}
	
	@Id
	@GeneratedValue
	@Column(name = "serNo", unique = true, nullable = false)
	@Override
	public Long getId() {
		return id;
	}
	
	@Override
	public void setId(Long id) {
		this.id = id;
	}
	
	
	@Column(name = "createUserId", length = 30)
	public String getCreateUserId() {
		return this.createUserId;
	}

	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}

	@Column(name = "updateUserId", length = 30)
	public String getUpdateUserId() {
		return this.updateUserId;
	}

	public void setUpdateUserId(String updateUserId) {
		this.updateUserId = updateUserId;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "createTime", nullable = false, length = 23)
	public Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "updateTime", nullable = false, length = 23)
	public Date getUpdateTime() {
		return this.updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	@Column(name = "sysMemo", length = 50)
	public String getSysMemo() {
		return this.sysMemo;
	}

	public void setSysMemo(String sysMemo) {
		this.sysMemo = sysMemo;
	}
	
	@Column(name = "createAgentId", length = 30)
	public String getCreateAgentId() {
		return createAgentId;
	}

	public void setCreateAgentId(String createAgentId) {
		this.createAgentId = createAgentId;
	}

	@Column(name = "updateAgentId", length = 30)
	public String getUpdateAgentId() {
		return updateAgentId;
	}

	public void setUpdateAgentId(String updateAgentId) {
		this.updateAgentId = updateAgentId;
	}

	@Column(name = "userId")
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@Column(name = "natiChiDesc")
	public String getNatiChiDesc() {
		return natiChiDesc;
	}

	public void setNatiChiDesc(String natiChiDesc) {
		this.natiChiDesc = natiChiDesc;
	}

	@Column(name = "schoDesc")
	public String getSchoDesc() {
		return schoDesc;
	}

	public void setSchoDesc(String schoDesc) {
		this.schoDesc = schoDesc;
	}

	@Column(name = "deptDesc")
	public String getDeptDesc() {
		return deptDesc;
	}

	public void setDeptDesc(String deptDesc) {
		this.deptDesc = deptDesc;
	}

	@Column(name = "educDesc")
	public String getEducDesc() {
		return educDesc;
	}

	public void setEducDesc(String educDesc) {
		this.educDesc = educDesc;
	}

	@Column(name = "educStDate")
	public String getEducStDate() {
		return educStDate;
	}

	public void setEducStDate(String educStDate) {
		this.educStDate = educStDate;
	}

	@Column(name = "educEndDate")
	public String getEducEndDate() {
		return educEndDate;
	}

	public void setEducEndDate(String educEndDate) {
		this.educEndDate = educEndDate;
	}
}
