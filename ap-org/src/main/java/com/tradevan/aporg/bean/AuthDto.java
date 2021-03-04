package com.tradevan.aporg.bean;

import java.io.Serializable;

import javax.persistence.Entity;

import org.springframework.beans.BeanUtils;

import com.tradevan.apcommon.util.DateUtil;
import com.tradevan.aporg.model.AuthProf;

/**
 * Title: AuthDto<br>
 * Description: <br>
 * Company: Tradevan Co.<br>
 * 
 * @author Neil Chen
 * @version 1.0
 */
@Entity
public class AuthDto implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Long id;
	
	private String authId;
	
	private String name;
	
	private String sysId;
	
	private String status;
	
	private String createUserId;
	
	private String updateUserId;
	
	private String createTime;
	
	private String updateTime;
	
	public AuthDto() {
	}

	public AuthDto(AuthProf auth) {
		BeanUtils.copyProperties(auth, this);
		setCreateTime(DateUtil.formatDate(auth.getCreateTime(), DateUtil.FMT_YYYY_MM_DD_HH_MM_SS));
		setUpdateTime(DateUtil.formatDate(auth.getUpdateTime(), DateUtil.FMT_YYYY_MM_DD_HH_MM_SS));
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

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

	public String getSysId() {
		return sysId;
	}

	public void setSysId(String sysId) {
		this.sysId = sysId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
}
