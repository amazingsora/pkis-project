package com.tradevan.handyflow.model.form;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.beans.BeanUtils;

import com.tradevan.apcommon.bean.CreateUserDto;
import com.tradevan.apcommon.persistence.GenericEntity;
import com.tradevan.handyflow.dto.FlowConfDto;

/**
 * Title: FlowConf<br>
 * Description: <br>
 * Company: Tradevan Co.<br>
 * 
 * @author Neil Chen
 * @version 1.2
 */
@Entity
@Table(name = "FLOWCONF")
public class FlowConf extends GenericEntity<Long> {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.TABLE)
	@Column(name = "serNo", unique = true, nullable = false)
	private Long id;
	
	@Column(nullable = false, length = 50)
	private String flowId;
	
	@Column(name = "flowName", nullable = false, length = 50)
	private String name;
	
	@Column(name = "flowVersion", nullable = false, length = 10)
	private String version;
	
	@Column(name = "flowDesc", length = 50)
	private String desc;
	
	@Column(length = 30)
	private String flowAdminId;
	
	@Column(length = 30)
	private String sysId;
	
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
	
	@OneToMany
	@JoinColumn(name = "flowId", referencedColumnName="flowId")
	private List<FlowStep> flowSteps = new ArrayList<FlowStep>();
	
	@OneToMany
	@JoinColumn(name = "flowId", referencedColumnName="flowId")
	private List<SubFlowConf> subFlowConfs = new ArrayList<SubFlowConf>();
	
	public FlowConf() {
		super();
	}

	public FlowConf(String flowId, String name, String version, String sysId, String createUserId) {
		this(flowId, name, version, sysId, null, createUserId);
		
	}

	public FlowConf(String flowId, String name, String version, String sysId, String flowAdminId, String createUserId) {
		super();
		this.flowId = flowId;
		this.name = name;
		this.version = version;
		this.sysId = sysId;
		this.flowAdminId = flowAdminId;
		Date now = new Date();
		this.createUserId = createUserId;
		this.updateUserId = createUserId;
		this.createTime = now;
		this.updateTime = now;
		
	}
	
	public FlowConf(FlowConfDto flowConfDto, CreateUserDto createUserDto) {
		BeanUtils.copyProperties(flowConfDto, this, "id");
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
		return flowId;
	}

	public String getName() {
		return name;
	}
	
	public String getVersion() {
		return version;
	}

	public String getDesc() {
		return desc;
	}
	
	public String getFlowAdminId() {
		return flowAdminId;
	}

	public String getSysId() {
		return sysId;
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
	
	public Date getCreateTime() {
		return createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setVersion(String version) {
		this.version = version;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public void setFlowAdminId(String flowAdminId) {
		this.flowAdminId = flowAdminId;
	}
	
	public void setSysId(String sysId) {
		this.sysId = sysId;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public void setFlowId(String flowId) {
		this.flowId = flowId;
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

	public List<FlowStep> getFlowSteps() {
		return flowSteps;
	}

	public List<SubFlowConf> getSubFlowConfs() {
		return subFlowConfs;
	}
	
}
