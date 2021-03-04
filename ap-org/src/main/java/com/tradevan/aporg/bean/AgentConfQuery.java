package com.tradevan.aporg.bean;

import com.tradevan.apcommon.bean.PageBean;

public class AgentConfQuery extends PageBean {
	private static final long serialVersionUID = 1L;
	
	private String sysId;
	private String userId;
	private String agentUserId;
	private String agentDate;
	
	public String getSysId() {
		return sysId;
	}
	public void setSysId(String sysId) {
		this.sysId = sysId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getAgentUserId() {
		return agentUserId;
	}
	public void setAgentUserId(String agentUserId) {
		this.agentUserId = agentUserId;
	}
	public String getAgentDate() {
		return agentDate;
	}
	public void setAgentDate(String agentDate) {
		this.agentDate = agentDate;
	}
}
