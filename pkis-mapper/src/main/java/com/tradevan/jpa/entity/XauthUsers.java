package com.tradevan.jpa.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@IdClass(XauthUsersKey.class)
@Table(name = "XAUTH_USERS")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer"})
public class XauthUsers implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4712509811833804097L;

	@Id
	@Column(name = "APP_ID")
	private String appId;
	
	@Id
	@Column(name = "IDEN_ID")
	private String idenId;
	
	@Id
	@Column(name = "USER_ID")
	private String userId;
	
	@Column(name = "USER_PW")
	private String userPw;
	
	@Column(name = "USER_CNAME")
	private String userCname;
	
	@Column(name = "USER_TYPE")
	private String userType;
	
	@Column(name = "ENABLED")
	private String enabled;
	
	@Column(name = "IS_FIRST")
    private String isFirst;
	
	@Column(name = "EMAIL")
    private String email;
	
	@Column(name = "EMAIL_TOKEN")
	private String emailToken;
	
	@Column(name = "EMAIL_EXPIRE")
	private Date emailExpire;
	
	@Column(name = "CRE_USER")
    private String creUser;
	
	@Column(name = "CRE_DATE")
    private Date creDate;
	
	@Column(name = "UPD_USER")
    private String updUser;
	
	@Column(name = "UPD_DATE")
    private Date updDate;

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
		
}
