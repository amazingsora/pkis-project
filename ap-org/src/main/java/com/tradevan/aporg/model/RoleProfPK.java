package com.tradevan.aporg.model;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the UserProfPK database table.
 * 
 */
public class RoleProfPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

    


	private String appId;
	

	private String deptId;
	


	private String roleId;

	public RoleProfPK() {
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




	public String getRoleId() {
		return roleId;
	}


	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	@Override
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof RoleProfPK)) {
			return false;
		}
		RoleProfPK castOther = (RoleProfPK)other;
		return 
			this.appId.equals(castOther.appId)
			&& this.roleId.equals(castOther.roleId)
			&& this.deptId.equals(castOther.deptId);
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.appId.hashCode();
		hash = hash * prime + this.roleId.hashCode();
		hash = hash * prime + this.deptId.hashCode();
		
		return hash;
	}
}