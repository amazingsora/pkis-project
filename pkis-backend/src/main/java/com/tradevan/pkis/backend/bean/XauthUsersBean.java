package com.tradevan.pkis.backend.bean;

import java.util.Date;


public class XauthUsersBean {

    private static final long serialVersionUID=1L;

    /**
     * 系統代號
     */
    private String appId;

    /**
     * 識別碼
     */
    private String idenId;

    /**
     * 使用者帳號
     */
    private String userId;

    /**
     * 使用者密碼
     */
    private String userPw;

    /**
     * 使用者姓名
     */
    private String userCname;

    /**
     * 使用者類型 00:系統管理員 01:管理員 02:使用者
     */
    private String userType;

    /**
     * 是否啟用 0:停用 1:啟用
     */
    private String enabled;

    /**
     * 是否第一次登入 0:否 1:是
     */
    private String isFirst;

    /**
     * 電子郵件
     */
    private String email;

    /**
     * 電子郵件重新認証識別碼
     */
    private String emailToken;

    /**
     * 電子郵件重新認証到期日
     */
    private Date emailExpire;

    /**
     * 建立人員
     */
    private String creUser;

    /**
     * 建立日期
     */
    private Date creDate;

    /**
     * 異動人員
     */
    private String updUser;

    /**
     * 異動日期
     */
    private Date updDate;
    
    /**
     * PWDHASH 密碼註解
     */
    private String pHash;

    private String roleId;
    
    /**
     * 在職狀態
     */
    private String onJob;

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getIdenId() {
		return idenId;
	}

	public void setIdenId(String idenId) {
		this.idenId = idenId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserPw() {
		return userPw;
	}

	public void setUserPw(String userPw) {
		this.userPw = userPw;
	}

	public String getUserCname() {
		return userCname;
	}

	public void setUserCname(String userCname) {
		this.userCname = userCname;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public String getEnabled() {
		return enabled;
	}

	public void setEnabled(String enabled) {
		this.enabled = enabled;
	}

	public String getIsFirst() {
		return isFirst;
	}

	public void setIsFirst(String isFirst) {
		this.isFirst = isFirst;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getEmailToken() {
		return emailToken;
	}

	public void setEmailToken(String emailToken) {
		this.emailToken = emailToken;
	}

	public Date getEmailExpire() {
		return emailExpire;
	}

	public void setEmailExpire(Date emailExpire) {
		this.emailExpire = emailExpire;
	}

	public String getCreUser() {
		return creUser;
	}

	public void setCreUser(String creUser) {
		this.creUser = creUser;
	}

	public Date getCreDate() {
		return creDate;
	}

	public void setCreDate(Date creDate) {
		this.creDate = creDate;
	}

	public String getUpdUser() {
		return updUser;
	}

	public void setUpdUser(String updUser) {
		this.updUser = updUser;
	}

	public Date getUpdDate() {
		return updDate;
	}

	public void setUpdDate(Date updDate) {
		this.updDate = updDate;
	}

	public String getpHash() {
		return pHash;
	}

	public void setpHash(String pHash) {
		this.pHash = pHash;
	}

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getOnJob() {
		return onJob;
	}

	public void setOnJob(String onJob) {
		this.onJob = onJob;
	}
	
}
