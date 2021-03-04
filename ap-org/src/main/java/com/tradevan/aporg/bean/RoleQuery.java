package com.tradevan.aporg.bean;

import com.tradevan.apcommon.bean.PageBean;

public class RoleQuery extends PageBean {
	private static final long serialVersionUID = 1L;
	
	private String roleId;
	private String name;
	private String funcId;
	private Boolean isSuperAdmin;
	private String upDeptId2;
	private String deptId2;
	private String userId;
	private String userName;

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

	public String getFuncId() {
		return funcId;
	}

	public void setFuncId(String funcId) {
		this.funcId = funcId;
	}

	public Boolean getIsSuperAdmin() {
		return isSuperAdmin;
	}

	public void setIsSuperAdmin(Boolean isSuperAdmin) {
		this.isSuperAdmin = isSuperAdmin;
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

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
}
