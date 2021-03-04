package com.tradevan.handyflow.dto;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;

import com.tradevan.apcommon.util.DateUtil;
import com.tradevan.handyflow.model.form.FlowStep;

public class FlowStepDto implements Serializable  {

	private static final long serialVersionUID = 1L;
	
	private Long id;
	
	private String flowId;
	
	private String stepId;
	
	private String stepType;
	
	private String name;
	
	private String desc;
	
	private Integer dispOrd;
	
	private String deptId;
	
	private String roleIds;
	
	private String userIds;
	
	private String sameUserAs;
	
	private String extension;
	
	private String projRole;
	
	private String reviewDeptRole;
	
	private Integer parallelPassCount;
	
	private String subFlowId;
	
	private String createUserId;
	
	private String updateUserId;
	
	private String createTime;
	
	private String updateTime;

	/**
	 * 2018/04/12 Added by Sephiro : 找尋同一系統別下，未被指定此關卡的角色
	 */
	private List<Map<String, Object>> nasRoleList; 
	
	/**
	 * 2018/04/12 Added by Sephiro : 找尋同一系統別下，已被指定此關卡的角色
	 */
	private List<Map<String, Object>> asRoleList; 
	
	/**
	 * 未被指定此關卡的部門階層角色
	 */
	private List<Map<String, Object>> nasLeaderRoleList; 
	
	/**
	 * 已被指定此關卡的部門階層角色
	 */
	private List<Map<String, Object>> asLeaderRoleList;
	
	/**
	 * 未被指定此關卡的部門組織角色
	 */
	private List<Map<String, Object>> nasDeptRoleList; 
	
	/**
	 * 已被指定此關卡的部門組織角色
	 */
	private List<Map<String, Object>> asDeptRoleList;
	
	/**
	 * 未被指定此關卡的專案角色
	 */
	private List<Map<String, Object>> nasProjRoleList; 
	
	/**
	 * 已被指定此關卡的專案角色
	 */
	private List<Map<String, Object>> asProjRoleList;
	
	/**
	 * 2018/04/12 Added by Sephiro : 最後要指定此關卡的角色們
	 */
	private String[] asRoleAry;

	/**
	 * 2018/04/12 Added by Sephiro : 找尋同一系統別下，未被指定此關卡的使用者
	 */
	private List<Map<String, Object>> nasUserList; 
	
	/**
	 * 2018/04/12 Added by Sephiro : 找尋同一系統別下，已被指定此關卡的使用者
	 */
	private List<Map<String, Object>> asUserList; 
	
	/**
	 * 2018/04/12 Added by Sephiro : 最後要指定此關卡的使用者們
	 */
	private String[] asUserAry;
	
	private List<Map<String, Object>> sameList; //下拉單：相同使用者
	
	private String maxStepId; //新增關卡：預設的stepId
	
	private Integer maxDispOrd; //新增關卡 : 預設的dispOrd
	
	private String reviewPersons; //審核人員
	
	public FlowStepDto() {
		
	}
	
	public FlowStepDto(FlowStep flowStep) {
		BeanUtils.copyProperties(flowStep, this);
		setProjRole(flowStep.getIsProjRole()==true ? "Y" : "N");
		setReviewDeptRole(flowStep.getIsReviewDeptRole()==true ? "Y" : "N");
		setCreateTime(DateUtil.formatDate(flowStep.getCreateTime(), DateUtil.FMT_YYYY_MM_DD_HH_MM_SS));
		setUpdateTime(DateUtil.formatDate(flowStep.getUpdateTime(), DateUtil.FMT_YYYY_MM_DD_HH_MM_SS));
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFlowId() {
		return flowId;
	}

	public void setFlowId(String flowId) {
		this.flowId = flowId;
	}

	public String getStepId() {
		return stepId;
	}

	public void setStepId(String stepId) {
		this.stepId = stepId;
	}

	public String getStepType() {
		return stepType;
	}

	public void setStepType(String stepType) {
		this.stepType = stepType;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public Integer getDispOrd() {
		return dispOrd;
	}

	public void setDispOrd(Integer dispOrd) {
		this.dispOrd = dispOrd;
	}

	public String getDeptId() {
		return deptId;
	}

	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}

	public String getRoleIds() {
		return roleIds;
	}

	public void setRoleIds(String roleIds) {
		this.roleIds = roleIds;
	}

	public String getUserIds() {
		return userIds;
	}

	public void setUserIds(String userIds) {
		this.userIds = userIds;
	}

	public String getSameUserAs() {
		return sameUserAs;
	}

	public void setSameUserAs(String sameUserAs) {
		this.sameUserAs = sameUserAs;
	}

	public String getExtension() {
		return extension;
	}

	public void setExtension(String extension) {
		this.extension = extension;
	}

	public String getProjRole() {
		return projRole;
	}

	public void setProjRole(String projRole) {
		this.projRole = projRole;
	}
	
	public String getReviewDeptRole() {
		return reviewDeptRole;
	}

	public void setReviewDeptRole(String reviewDeptRole) {
		this.reviewDeptRole = reviewDeptRole;
	}

	public Integer getParallelPassCount() {
		return parallelPassCount;
	}

	public void setParallelPassCount(Integer parallelPassCount) {
		this.parallelPassCount = parallelPassCount;
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

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public List<Map<String, Object>> getNasRoleList() {
		return nasRoleList;
	}

	public void setNasRoleList(List<Map<String, Object>> nasRoleList) {
		this.nasRoleList = nasRoleList;
	}

	public List<Map<String, Object>> getAsRoleList() {
		return asRoleList;
	}

	public void setAsRoleList(List<Map<String, Object>> asRoleList) {
		this.asRoleList = asRoleList;
	}
	
	public List<Map<String, Object>> getNasLeaderRoleList() {
		return nasLeaderRoleList;
	}

	public void setNasLeaderRoleList(List<Map<String, Object>> nasLeaderRoleList) {
		this.nasLeaderRoleList = nasLeaderRoleList;
	}

	public List<Map<String, Object>> getAsLeaderRoleList() {
		return asLeaderRoleList;
	}

	public void setAsLeaderRoleList(List<Map<String, Object>> asLeaderRoleList) {
		this.asLeaderRoleList = asLeaderRoleList;
	}

	public List<Map<String, Object>> getNasDeptRoleList() {
		return nasDeptRoleList;
	}

	public void setNasDeptRoleList(List<Map<String, Object>> nasDeptRoleList) {
		this.nasDeptRoleList = nasDeptRoleList;
	}

	public List<Map<String, Object>> getAsDeptRoleList() {
		return asDeptRoleList;
	}

	public void setAsDeptRoleList(List<Map<String, Object>> asDeptRoleList) {
		this.asDeptRoleList = asDeptRoleList;
	}

	public List<Map<String, Object>> getNasProjRoleList() {
		return nasProjRoleList;
	}

	public void setNasProjRoleList(List<Map<String, Object>> nasProjRoleList) {
		this.nasProjRoleList = nasProjRoleList;
	}

	public List<Map<String, Object>> getAsProjRoleList() {
		return asProjRoleList;
	}

	public void setAsProjRoleList(List<Map<String, Object>> asProjRoleList) {
		this.asProjRoleList = asProjRoleList;
	}

	public String[] getAsRoleAry() {
		return asRoleAry;
	}

	public void setAsRoleAry(String[] asRoleAry) {
		this.asRoleAry = asRoleAry;
	}

	public List<Map<String, Object>> getNasUserList() {
		return nasUserList;
	}

	public void setNasUserList(List<Map<String, Object>> nasUserList) {
		this.nasUserList = nasUserList;
	}

	public List<Map<String, Object>> getAsUserList() {
		return asUserList;
	}

	public void setAsUserList(List<Map<String, Object>> asUserList) {
		this.asUserList = asUserList;
	}

	public String[] getAsUserAry() {
		return asUserAry;
	}

	public void setAsUserAry(String[] asUserAry) {
		this.asUserAry = asUserAry;
	}

	public List<Map<String, Object>> getSameList() {
		return sameList;
	}

	public void setSameList(List<Map<String, Object>> sameList) {
		this.sameList = sameList;
	}

	public String getMaxStepId() {
		return maxStepId;
	}

	public void setMaxStepId(String maxStepId) {
		this.maxStepId = maxStepId;
	}

	public Integer getMaxDispOrd() {
		return maxDispOrd;
	}

	public void setMaxDispOrd(Integer maxDispOrd) {
		this.maxDispOrd = maxDispOrd;
	}

	public String getReviewPersons() {
		return reviewPersons;
	}

	public void setReviewPersons(String reviewPersons) {
		this.reviewPersons = reviewPersons;
	}

	public String getSubFlowId() {
		return subFlowId;
	}

	public void setSubFlowId(String subFlowId) {
		this.subFlowId = subFlowId;
	}
	
}
