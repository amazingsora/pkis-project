package com.tradevan.aporg.bean;

import com.tradevan.apcommon.bean.PageBean;

public class RoleDeptQuery extends PageBean {
	private static final long serialVersionUID = 1L;
	
	private String sysId;
	private String upDeptId;
	private String deptId;
	private String roleId;
	private String userId;
	private Boolean isSuperAdmin;
	private String[] userIds;
	private String upDeptId2;
	private String deptId2;
	private String userName;

	public String getSysId() {
		return sysId;
	}

	public void setSysId(String sysId) {
		this.sysId = sysId;
	}
	
	public String getUpDeptId() {
		return upDeptId;
	}

	public void setUpDeptId(String upDeptId) {
		this.upDeptId = upDeptId;
	}

	public String getDeptId() {
		return deptId;
	}

	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}
	
	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Boolean getIsSuperAdmin() {
		return isSuperAdmin;
	}

	public void setIsSuperAdmin(Boolean isSuperAdmin) {
		this.isSuperAdmin = isSuperAdmin;
	}

	public String[] getUserIds() {
		return userIds;
	}

	public void setUserIds(String[] userIds) {
		this.userIds = userIds;
	}

	public String getUpDeptId2() {
		return upDeptId2;
	}

	public void setUpDeptId2(String upDeptId2) {
		this.upDeptId2 = upDeptId2;
	}

	public String getDeptId2() {
		return deptId2;
	}

	public void setDeptId2(String deptId2) {
		this.deptId2 = deptId2;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
}
