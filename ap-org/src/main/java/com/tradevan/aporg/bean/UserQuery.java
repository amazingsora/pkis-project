package com.tradevan.aporg.bean;

import com.tradevan.apcommon.bean.PageBean;

public class UserQuery extends PageBean {
	private static final long serialVersionUID = 1L;
	
	private String userId;
	private String name;
	private String jobTitleSerNo;
	private String upDeptId;
	private String deptId;
	private String idCardNo;
	private String inputUserId;
	private String userState;
	private Boolean isSuperAdmin;
	private String radioChecked;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getJobTitleSerNo() {
		return jobTitleSerNo;
	}

	public void setJobTitleSerNo(String jobTitleSerNo) {
		this.jobTitleSerNo = jobTitleSerNo;
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

	public String getIdCardNo() {
		return idCardNo;
	}

	public void setIdCardNo(String idCardNo) {
		this.idCardNo = idCardNo;
	}

	public String getInputUserId() {
		return inputUserId;
	}

	public void setInputUserId(String inputUserId) {
		this.inputUserId = inputUserId;
	}

	public String getUserState() {
		return userState;
	}

	public void setUserState(String userState) {
		this.userState = userState;
	}	
	
	public Boolean getIsSuperAdmin() {
		return isSuperAdmin;
	}

	public void setIsSuperAdmin(Boolean isSuperAdmin) {
		this.isSuperAdmin = isSuperAdmin;
	}

	public String getRadioChecked() {
		return radioChecked;
	}

	public void setRadioChecked(String radioChecked) {
		this.radioChecked = radioChecked;
	}
}
