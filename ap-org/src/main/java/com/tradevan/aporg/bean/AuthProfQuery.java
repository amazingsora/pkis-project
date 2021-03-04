package com.tradevan.aporg.bean;

import com.tradevan.apcommon.bean.PageBean;

public class AuthProfQuery extends PageBean {
	private static final long serialVersionUID = 1L;
	
	private String authId;
	private String name;
	
	public String getAuthId() {
		return authId;
	}
	public void setAuthId(String authId) {
		this.authId = authId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
}
