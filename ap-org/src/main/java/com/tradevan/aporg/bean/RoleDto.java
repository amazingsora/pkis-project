package com.tradevan.aporg.bean;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;

import com.tradevan.apcommon.util.DateUtil;
import com.tradevan.aporg.model.RoleProf;

/**
 * Title: RoleDto<br>
 * Description: <br>
 * Company: Tradevan Co.<br>
 * 
 * @author Neil Chen
 * @version 1.2
 */
public class RoleDto implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Long id;
	
	private String roleId;
	
	private String name;
	
	private Integer level;
	
	private String deptRole; //2018/04/11 Sephiro : 對應roleProf.isDeptRole, Y:是, N:否
	
	private String sysId;
	
	private String category;
	
	private String status;
	
	private String createUserId;
	
	private String updateUserId;
	
	private String createTime;
	
	private String updateTime;

	/**
	 * 2018/03/22 Added by Sephiro : 找尋同一系統別下，未被指定此角色的帳號
	 */
	private List<Map<String, Object>> notAssignedUserList; 
	
	/**
	 * 2018/03/22 Added by Sephiro : 找尋同一系統別下，所有已被指定此角色的帳號
	 */
	private List<Map<String, Object>> assignedUserList; 
	
	/**
	 * 2018/03/22 Added by Sephiro : 最後要指定此角色的帳號們
	 */
	private String[] asAry;
	
	public RoleDto() {
	}

	public RoleDto(RoleProf role) {
		BeanUtils.copyProperties(role, this);
		setLevel(0);
		setDeptRole(role.isDeptRole()==true ? "Y" : "N");
		setCreateTime(DateUtil.formatDate(role.getCreateTime(), DateUtil.FMT_YYYY_MM_DD_HH_MM_SS));
		setUpdateTime(DateUtil.formatDate(role.getUpdateTime(), DateUtil.FMT_YYYY_MM_DD_HH_MM_SS));
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public String getSysId() {
		return sysId;
	}

	public void setSysId(String sysId) {
		this.sysId = sysId;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
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

	public List<Map<String, Object>> getAssignedUserList() {
		return assignedUserList;
	}

	public void setAssignedUserList(List<Map<String, Object>> assignedUserList) {
		this.assignedUserList = assignedUserList;
	}

	public List<Map<String, Object>> getNotAssignedUserList() {
		return notAssignedUserList;
	}

	public void setNotAssignedUserList(List<Map<String, Object>> notAssignedUserList) {
		this.notAssignedUserList = notAssignedUserList;
	}

	public String[] getAsAry() {
		return asAry;
	}

	public void setAsAry(String[] asAry) {
		this.asAry = asAry;
	}

	public String getDeptRole() {
		return deptRole;
	}

	public void setDeptRole(String deptRole) {
		this.deptRole = deptRole;
	}
	
}
