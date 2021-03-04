package com.tradevan.handyflow.model.form;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

import org.springframework.beans.BeanUtils;

import com.tradevan.apcommon.bean.CreateUserDto;
import com.tradevan.apcommon.persistence.GenericEntity;
import com.tradevan.handyflow.dto.FlowStepLinkDto;

/**
 * Title: FlowStepLink<br>
 * Description: <br>
 * Company: Tradevan Co.<br>
 * 
 * @author Neil Chen
 * @version 1.1
 */
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"flowId", "stepId", "toStepId"}))
public class FlowStepLink extends GenericEntity<Long> {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	@Column(name = "serNo", unique = true, nullable = false)
	private Long id;
	
	@Column(nullable = false, length = 50)
	private String flowId;
	
	@Column(nullable = false, length = 30)
	private String stepId;
	
	@Column(nullable = false, length = 30)
	private String toStepId;
	
	@Column(length = 30)
	private String action;
	
	@Column(nullable = false)
	private Boolean isConcurrent;
	
	@Column(name = "linkName", nullable = false, length = 50)
	private String name;
	
	@Column(name = "linkDesc", length = 50)
	private String desc;
	
	@Column(nullable = false)
	private Integer dispOrd;
	
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
	
	public FlowStepLink() {
		super();
	}

	public FlowStepLink(String flowId, String stepId, String toStepId, String action, Boolean isConcurrent, String name, String desc, Integer dispOrd, String createUserId) {
		super();
		this.flowId = flowId;
		this.stepId = stepId;
		this.toStepId = toStepId;
		this.action = action;
		this.isConcurrent = isConcurrent;
		this.name = name;
		this.desc = desc;
		this.dispOrd = dispOrd;
		Date now = new Date();
		this.createUserId = createUserId;
		this.updateUserId = createUserId;
		this.createTime = now;
		this.updateTime = now;
	}

	public FlowStepLink(FlowStepLinkDto flowStepLinkDto, CreateUserDto createUserDto) {
		BeanUtils.copyProperties(flowStepLinkDto, this, "id");
		setIsConcurrent(flowStepLinkDto.getConcurrent().equals("Y") ? true : false);
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
	
	public String getStepId() {
		return stepId;
	}
	
	public String getToStepId() {
		return toStepId;
	}
	
	public String getAction() {
		return action;
	}

	public Boolean getIsConcurrent() {
		return isConcurrent;
	}

	public String getName() {
		return name;
	}

	public String getDesc() {
		return desc;
	}

	public Integer getDispOrd() {
		return dispOrd;
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

	public void setToStepId(String toStepId) {
		this.toStepId = toStepId;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public void setIsConcurrent(Boolean isConcurrent) {
		this.isConcurrent = isConcurrent;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public void setDispOrd(Integer dispOrd) {
		this.dispOrd = dispOrd;
	}
	
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public void setFlowId(String flowId) {
		this.flowId = flowId;
	}

	public void setStepId(String stepId) {
		this.stepId = stepId;
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
	
}
