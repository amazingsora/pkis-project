package com.tradevan.handyflow.model.form;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

import org.springframework.beans.BeanUtils;

import com.tradevan.apcommon.bean.CreateUserDto;
import com.tradevan.apcommon.persistence.GenericEntity;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = { "flowId", "flowVersion" }))
public class FlowXml extends GenericEntity<Long> {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	@Column(name = "serNo", unique = true, nullable = false)
	private Long id;
	
	@Column(name = "flowId", nullable = false, length = 50)
	private String flowId;
	
	@Column(name = "flowVersion", nullable = false, length = 10)
	private String flowVersion;
	
	@Lob
	@Column(name = "flowXml")
	private String flowXml;
	
	@Column(name = "createUserId", length = 30)
	private String createUserId;
	
	@Column(name = "createAgentId", length = 30)
	private String createAgentId;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "createTime", nullable = false, length = 23)
	private Date createTime;
	
	@Column(name = "sysMemo", length = 50)
	private String sysMemo;

	public FlowXml() {
	}

	private FlowXml(String flowId, String flowVersion) {
		this.flowId = flowId;
		this.flowVersion = flowVersion;
	}
	
	public FlowXml(String flowId, String flowVersion, String createUserId) {
		this(flowId, flowVersion);
		this.createUserId = createUserId;
		this.createTime = new Date();
	}
	
	public FlowXml(String flowId, String flowVersion, CreateUserDto createUserDto) {
		this(flowId, flowVersion);
		BeanUtils.copyProperties(createUserDto, this);
	}

	@Override
	public Long getId() {
		return id;
	}
	
	@Override
	public void setId(Long id) {
		throw new IllegalArgumentException("The id can't be changed.");
	}

	public String getFlowId() {
		return this.flowId;
	}

	public void setFlowId(String flowId) {
		this.flowId = flowId;
	}

	public String getFlowVersion() {
		return this.flowVersion;
	}

	public void setFlowVersion(String flowVersion) {
		this.flowVersion = flowVersion;
	}

	public String getFlowXml() {
		return this.flowXml;
	}

	public void setFlowXml(String flowXml) {
		this.flowXml = flowXml;
	}

	public String getCreateUserId() {
		return this.createUserId;
	}

	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}

	public String getCreateAgentId() {
		return this.createAgentId;
	}

	public void setCreateAgentId(String createAgentId) {
		this.createAgentId = createAgentId;
	}

	public Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getSysMemo() {
		return this.sysMemo;
	}

	public void setSysMemo(String sysMemo) {
		this.sysMemo = sysMemo;
	}

}
