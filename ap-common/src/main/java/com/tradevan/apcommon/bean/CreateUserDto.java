package com.tradevan.apcommon.bean;

import java.io.Serializable;
import java.util.Date;

/**
 * Title: CreateUserDto<br>
 * Description: <br>
 * Company: Tradevan Co.<br>
 * 
 * @author Neil Chen
 * @version 1.1
 */
public class CreateUserDto implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String createUserId;
	
	private String updateUserId;
	
	private String createAgentId;
	
	private String updateAgentId;
	
	private Date createTime;
	
	private Date updateTime;

	public CreateUserDto() {
	}

	public String getBeRepresentedId() {
		return (createAgentId != null) ? updateUserId : null;
	}
	
	public String getCreateUserId() {
		return createUserId;
	}

	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}

	public String getUpdateUserId() {
		return updateUserId;
	}

	public void setUpdateUserId(String updateUserId) {
		this.updateUserId = updateUserId;
	}
	
	public String getCreateAgentId() {
		return createAgentId;
	}

	public void setCreateAgentId(String createAgentId) {
		this.createAgentId = createAgentId;
	}

	public String getUpdateAgentId() {
		return updateAgentId;
	}

	public void setUpdateAgentId(String updateAgentId) {
		this.updateAgentId = updateAgentId;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
}
