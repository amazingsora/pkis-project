package com.tradevan.jpa.entity;

import java.io.Serializable;

public class XauthUsersKey implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8250928559894873275L;

	private String appId;

	private String idenId;

	private String userId;

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
	
}
