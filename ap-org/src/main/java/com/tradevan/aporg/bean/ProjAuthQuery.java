package com.tradevan.aporg.bean;

import com.tradevan.apcommon.bean.PageBean;

public class ProjAuthQuery extends PageBean {
	private static final long serialVersionUID = 1L;
	
	private String projId;
	private String name;
	
	public String getProjId() {
		return projId;
	}
	public void setProjId(String projId) {
		this.projId = projId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
}
