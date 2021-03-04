package com.tradevan.aporg.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

import org.springframework.beans.BeanUtils;

import com.tradevan.apcommon.bean.CreateUserDto;
import com.tradevan.apcommon.persistence.GenericEntity;

/**
 * Title: UserProjAuth<br>
 * Description: <br>
 * Company: Tradevan Co.<br>
 * 
 * @author Neil Chen
 * @version 1.0
 */
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"userId", "projId", "authId"}))
public class UserProjAuth extends GenericEntity<Long> {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue
	@Column(name = "serNo", unique = true, nullable = false)
	private Long id;
	
	@Column(nullable = false, length = 30)
	private String userId;
	
	@Column(nullable = false, length = 30)
	private String projId;
	
	@Column(nullable = false, length = 30)
	private String authId;
	
	@Column(length = 30)
	private String createUserId;
	
	@Column(length = 30)
	private String createAgentId;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable = false)
	private Date createTime;
	
	public UserProjAuth() {
		super();
	}
	
	public UserProjAuth(String userId, String projId, String authId, String createUserId) {
		super();
		this.userId = userId;
		this.projId = projId;
		this.authId = authId;
		this.createUserId = createUserId;
		this.createTime = new Date();;
	}
	
	public UserProjAuth(String userId, String projId, String authId, CreateUserDto createUserDto) {
		super();
		this.userId = userId;
		this.projId = projId;
		this.authId = authId;
		BeanUtils.copyProperties(createUserDto, this);
	}
	
	@Override
	public Long getId() {
		return id;
	}
	
	@Override
	public void setId(Long id) {
		throw new IllegalArgumentException("The id can't be changed.");
	}
	
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getProjId() {
		return projId;
	}

	public void setProjId(String projId) {
		this.projId = projId;
	}

	public String getAuthId() {
		return authId;
	}

	public void setAuthId(String authId) {
		this.authId = authId;
	}

	public String getCreateUserId() {
		return createUserId;
	}

	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}

	public String getCreateAgentId() {
		return createAgentId;
	}

	public void setCreateAgentId(String createAgentId) {
		this.createAgentId = createAgentId;
	}
	
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
}
