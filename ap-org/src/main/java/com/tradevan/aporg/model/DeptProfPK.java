package com.tradevan.aporg.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Id;

/**
 * The primary key class for the UserProfPK database table.
 * 
 */
public class DeptProfPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	public DeptProfPK() {
	}

	private String appId;
	

	private String deptId;


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
	@Override
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof DeptProfPK)) {
			return false;
		}
		DeptProfPK castOther = (DeptProfPK)other;
		return 
			this.appId.equals(castOther.appId)
			
			&& this.deptId.equals(castOther.deptId);
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.appId.hashCode();
	
		hash = hash * prime + this.deptId.hashCode();
		
		return hash;
	}

	
}