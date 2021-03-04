package com.tradevan.handyflow.model.form;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

import org.springframework.beans.BeanUtils;

import com.tradevan.apcommon.bean.CreateUserDto;
import com.tradevan.apcommon.persistence.GenericEntity;
import com.tradevan.handyflow.dto.FlowStepDto;

/**
 * Title: FlowStep<br>
 * Description: <br>
 * Company: Tradevan Co.<br>
 * 
 * @author Neil Chen
 * @version 1.2.1
 */
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"flowId", "stepId"}))
public class FlowStep extends GenericEntity<Long> {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	@Column(name = "serNo", unique = true, nullable = false)
	private Long id;
	
	@Column(nullable = false, length = 50)
	private String flowId;
	
	@Column(nullable = false, length = 30)
	private String stepId;
	
	@Column(nullable = false, length = 10)
	private String stepType;
	
	@Column(name = "stepName", nullable = false, length = 50)
	private String name;
	
	@Column(name = "stepDesc", length = 50)
	private String desc;
	
	@Column(nullable = false)
	private Integer dispOrd;
	
	@Column(name = "deptId", length = 30)
	private String deptId;
	
	@Column(name = "roleIds", length = 300)
	private String roleIds;
	
	@Column(name = "userIds", length = 300)
	private String userIds;
	
	@Column(name = "sameUserAs", length = 30)
	private String sameUserAs;
	
	@Column(name = "extension", length = 100)
	private String extension;
	
	@Column(nullable = false)
	private Boolean isProjRole;
	
	@Column(nullable = false)
	private Boolean isReviewDeptRole;
	
	@Column
	private Integer parallelPassCount;
	
	@Column(length = 30)
	private String subFlowId;
	
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
	@JoinColumns({
		@JoinColumn(name = "flowId", referencedColumnName="flowId"),
		@JoinColumn(name = "stepId", referencedColumnName="stepId")
	})
	private List<FlowStepLink> flowStepLinks = new ArrayList<FlowStepLink>();
	
	public FlowStep() {
		super();
	}

	public FlowStep(String flowId, String stepId, String stepType, String name, String desc, Integer dispOrd, String createUserId) {
		super();
		this.flowId = flowId;
		this.stepId = stepId;
		this.stepType = stepType;
		this.name = name;
		this.desc = desc;
		this.dispOrd = dispOrd;
		this.isProjRole = false;
		this.isReviewDeptRole = false;
		Date now = new Date();
		this.createUserId = createUserId;
		this.updateUserId = createUserId;
		this.createTime = now;
		this.updateTime = now;
		
	}

	public FlowStep(FlowStepDto flowStepDto, CreateUserDto createUserDto) {
		BeanUtils.copyProperties(flowStepDto, this, "id");
		BeanUtils.copyProperties(createUserDto, this);
		setIsProjRole(flowStepDto.getProjRole().equals("Y") ? true : false);
		setIsReviewDeptRole(flowStepDto.getReviewDeptRole().equals("Y") ? true : false);
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
	
	public String getStepType() {
		return stepType;
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
	
	public String getDeptId() {
		return deptId;
	}

	public String getRoleIds() {
		return roleIds;
	}
	
	public String getUserIds() {
		return userIds;
	}
	
	public String getSameUserAs() {
		return sameUserAs;
	}

	public String getExtension() {
		return extension;
	}

	public Boolean getIsProjRole() {
		return isProjRole;
	}

	public Boolean getIsReviewDeptRole() {
		return isReviewDeptRole;
	}

	public Integer getParallelPassCount() {
		return parallelPassCount;
	}

	public String getSubFlowId() {
		return subFlowId;
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
	
	public void setDispOrd(Integer dispOrd) {
		this.dispOrd = dispOrd;
	}
	
	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}

	public void setRoleIds(String roleIds) {
		this.roleIds = roleIds;
	}

	public void setUserIds(String userIds) {
		this.userIds = userIds;
	}
	
	public void setSameUserAs(String sameUserAs) {
		this.sameUserAs = sameUserAs;
	}
	
	public void setExtension(String extension) {
		this.extension = extension;
	}
	
	public void setIsProjRole(Boolean isProjRole) {
		this.isProjRole = isProjRole;
	}

	public void setIsReviewDeptRole(Boolean isReviewDeptRole) {
		this.isReviewDeptRole = isReviewDeptRole;
	}
	
	public void setParallelPassCount(Integer parallelPassCount) {
		this.parallelPassCount = parallelPassCount;
	}

	public void setSubFlowId(String subFlowId) {
		this.subFlowId = subFlowId;
	}

	public void setUpdateUserId(String updateUserId) {
		this.updateUserId = updateUserId;
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

	public void setStepType(String stepType) {
		this.stepType = stepType;
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

	public List<FlowStepLink> getFlowStepLinks() {
		return flowStepLinks;
	}
	
}
