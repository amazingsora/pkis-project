package com.tradevan.apcommon.bean;

import java.io.Serializable;
import java.util.Date;

/**
 * Title: UpdateUserDto<br>
 * Description: <br>
 * Company: Tradevan Co.<br>
 * 
 * @author Neil Chen
 * @version 1.1
 */
public class UpdateUserDto implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String updateUserId;
	
	private String updateAgentId;
	
	private Date updateTime;

	public UpdateUserDto() {
	}

	public String getBeRepresentedId() {
		return (updateAgentId != null) ? updateUserId : null;
	}
	
	public String getUpdateUserId() {
		return updateUserId;
	}

	public void setUpdateUserId(String updateUserId) {
		this.updateUserId = updateUserId;
	}
	
	public String getUpdateAgentId() {
		return updateAgentId;
	}

	public void setUpdateAgentId(String updateAgentId) {
		this.updateAgentId = updateAgentId;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
}
