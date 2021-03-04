package com.tradevan.handyflow.model.form;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the UserProfPK database table.
 * 
 */
public class DocStatePK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

    

	@Id
	private String appId;
	
	@Id
	private String deptId;
	

	
	private String formId;
	@Id
	private String applyNo;
	@Id
	private Integer serialNo;
	
	

	public String getFormId() {
		return formId;
	}



	public void setFormId(String formId) {
		this.formId = formId;
	}



	public String getApplyNo() {
		return applyNo;
	}



	public void setApplyNo(String applyNo) {
		this.applyNo = applyNo;
	}



	



	public Integer getSerialNo() {
		return serialNo;
	}



	public void setSerialNo(Integer serialNo) {
		this.serialNo = serialNo;
	}



	public DocStatePK() {
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



	@Override
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof DocStatePK)) {
			return false;
		}
		DocStatePK castOther = (DocStatePK)other;
		return 
			this.appId.equals(castOther.appId)
			&& this.deptId.equals(castOther.deptId)
			&& this.formId.equals(castOther.formId)
			&& this.applyNo.equals(castOther.applyNo)
			&& this.serialNo.equals(castOther.serialNo);
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.appId.hashCode();
		hash = hash * prime + this.deptId.hashCode();
		hash = hash * prime + this.formId.hashCode();
		hash = hash * prime + this.applyNo.hashCode();
		hash = hash * prime + this.serialNo.hashCode();
		// {"formId", "applyNo", "serialNo"
		return hash;
	}
}