package com.tradevan.aporg.model;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the UserProfPK database table.
 * 
 */
public class UserProfPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

    


	private String appId;
	

	private String deptId;
	


	private String userId;

	public UserProfPK() {
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

	@Override
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof UserProfPK)) {
			return false;
		}
		UserProfPK castOther = (UserProfPK)other;
		return 
			this.appId.equals(castOther.appId)
			&& this.userId.equals(castOther.userId)
			&& this.deptId.equals(castOther.deptId);
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.appId.hashCode();
		hash = hash * prime + this.userId.hashCode();
		hash = hash * prime + this.deptId.hashCode();
		
		return hash;
	}
}