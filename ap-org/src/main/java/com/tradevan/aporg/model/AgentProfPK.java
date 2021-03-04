package com.tradevan.aporg.model;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the UserProfPK database table.
 * 
 */
public class AgentProfPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

    


	private String appId;
	private String deptId;
	private String userId;
	private String agentAppId;
	private String agentDeptId;
	private String agentUserId;
	
	public AgentProfPK() {
	}
	
	


	public String getAppId() {
		return appId;
	}




	public void setAppId(String appId) {
		this.appId = appId;
	}




	public String getDeptId() {
		return deptId;
	}




	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}




	public String getUserId() {
		return userId;
	}




	public void setUserId(String userId) {
		this.userId = userId;
	}




	public String getAgentAppId() {
		return agentAppId;
	}




	public void setAgentAppId(String agentAppId) {
		this.agentAppId = agentAppId;
	}




	public String getAgentDeptId() {
		return agentDeptId;
	}




	public void setAgentDeptId(String agentDeptId) {
		this.agentDeptId = agentDeptId;
	}




	public String getAgentUserId() {
		return agentUserId;
	}




	public void setAgentUserId(String agentUserId) {
		this.agentUserId = agentUserId;
	}



	@Override
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof AgentProfPK)) {
			return false;
		}
		AgentProfPK castOther = (AgentProfPK)other;
		return 
			this.appId.equals(castOther.appId)
			&& this.userId.equals(castOther.userId)
			&& this.deptId.equals(castOther.deptId)
			&& this.agentAppId.equals(castOther.agentAppId)
			&& this.agentDeptId.equals(castOther.agentDeptId)
			&& this.agentUserId.equals(castOther.agentUserId);
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.appId.hashCode();
		hash = hash * prime + this.userId.hashCode();
		hash = hash * prime + this.deptId.hashCode();
		hash = hash * prime + this.agentAppId.hashCode();
		hash = hash * prime + this.agentDeptId.hashCode();
		hash = hash * prime + this.agentUserId.hashCode();
		
		return hash;
	}
}