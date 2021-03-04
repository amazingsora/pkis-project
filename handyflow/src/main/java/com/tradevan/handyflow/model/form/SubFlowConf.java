package com.tradevan.handyflow.model.form;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

import org.springframework.beans.BeanUtils;

import com.tradevan.apcommon.bean.CreateUserDto;
import com.tradevan.apcommon.persistence.GenericEntity;
import com.tradevan.handyflow.dto.SubFlowConfDto;

/**
 * Title: SubFlowConf<br>
 * Description: <br>
 * Company: Tradevan Co.<br>
 * 
 * @author Neil Chen
 * @version 1.1
 */
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"flowId", "subFlowId"}))
public class SubFlowConf extends GenericEntity<Long> {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	@Column(name = "serNo", unique = true, nullable = false)
	private Long id;
	
	@Column(nullable = false, length = 50)
	private String flowId;
	
	@Column(nullable = false, length = 50)
	private String subFlowId;
	
	@Column(name = "subFlowName", nullable = false, length = 50)
	private String name;
	
	@Column(name = "subFlowDesc", length = 50)
	private String desc;
	
	@Column(nullable = false, length = 30)
	private String finishTo;
	
	@Column(nullable = false, length = 30)
	private String returnTo;
	
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
	@JoinColumn(name = "flowId", referencedColumnName="subFlowId")
	private List<FlowStep> flowSteps = new ArrayList<FlowStep>();
	
	public SubFlowConf() {
		super();
	}

	public SubFlowConf(String flowId, String subFlowId, String name, String desc, String createUserId) {
		super();
		this.flowId = flowId;
		this.subFlowId = subFlowId;
		this.name = name;
		this.desc = desc;
		Date now = new Date();
		this.createUserId = createUserId;
		this.updateUserId = createUserId;
		this.createTime = now;
		this.updateTime = now;
		
	}

	public SubFlowConf(SubFlowConfDto subFlowConfDto, CreateUserDto createUserDto) {
		BeanUtils.copyProperties(subFlowConfDto, this, "id");
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

	public String getSubFlowId() {
		return subFlowId;
	}

	public String getName() {
		return name;
	}

	public String getDesc() {
		return desc;
	}

	public String getFinishTo() {
		return finishTo;
	}

	public String getReturnTo() {
		return returnTo;
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

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public void setFinishTo(String finishTo) {
		this.finishTo = finishTo;
	}

	public void setReturnTo(String returnTo) {
		this.returnTo = returnTo;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public void setFlowId(String flowId) {
		this.flowId = flowId;
	}

	public void setSubFlowId(String subFlowId) {
		this.subFlowId = subFlowId;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
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
	
}
