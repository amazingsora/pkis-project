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
//@Table(uniqueConstraints = @UniqueConstraint(columnNames = { "userId","organDesc","deviCode","deviDesc","dutyDesc","expeStDate","expeEndDate" }))
@Table
public class UserExperience extends GenericEntity<Long> {
	private static final long serialVersionUID = 1L;

	private Long id;
	private String userId;
	private String organDesc;
	private String deviCode;
	private String deviDesc;
	private String dutyDesc;
	private String expeStDate;
	private String expeEndDate;
	private String createUserId;
	private String updateUserId;
	private Date createTime;
	private Date updateTime;
	private String sysMemo;
	private String createAgentId;
	private String updateAgentId;
	
	public UserExperience() {
		
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

	@Column(name = "organDesc")
	public String getOrganDesc() {
		return organDesc;
	}

	public void setOrganDesc(String organDesc) {
		this.organDesc = organDesc;
	}

	@Column(name = "deviCode")
	public String getDeviCode() {
		return deviCode;
	}

	public void setDeviCode(String deviCode) {
		this.deviCode = deviCode;
	}

	@Column(name = "deviDesc")
	public String getDeviDesc() {
		return deviDesc;
	}

	public void setDeviDesc(String deviDesc) {
		this.deviDesc = deviDesc;
	}

	@Column(name = "dutyDesc")
	public String getDutyDesc() {
		return dutyDesc;
	}

	public void setDutyDesc(String dutyDesc) {
		this.dutyDesc = dutyDesc;
	}

	@Column(name = "expeStDate")
	public String getExpeStDate() {
		return expeStDate;
	}

	public void setExpeStDate(String expeStDate) {
		this.expeStDate = expeStDate;
	}

	@Column(name = "expeEndDate")
	public String getExpeEndDate() {
		return expeEndDate;
	}

	public void setExpeEndDate(String expeEndDate) {
		this.expeEndDate = expeEndDate;
	}
}
