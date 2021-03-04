package com.tradevan.aporg.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

import org.springframework.beans.BeanUtils;

import com.tradevan.apcommon.bean.CreateUserDto;
import com.tradevan.apcommon.persistence.GenericEntity;
import com.tradevan.aporg.enums.UserType;

/**
 * Title: UserProjRole<br>
 * Description: <br>
 * Company: Tradevan Co.<br>
 * 
 * @author Neil Chen
 * @version 1.1
 */
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"userId", "projId", "roleId"}))
public class UserProjRole extends GenericEntity<Long> {
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
	private String roleId;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "userType", nullable = false, length = 20)
	private UserType userType;
	
	@Column(length = 30)
	private String createUserId;
	
	@Column(length = 30)
	private String createAgentId;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable = false)
	private Date createTime;
	
	public UserProjRole() {
		super();
	}
	
	public UserProjRole(String userId, String projId, String roleId, String createUserId) {
		super();
		this.userId = userId;
		this.projId = projId;
		this.roleId = roleId;
		this.userType = UserType.IN;
		this.createUserId = createUserId;
		this.createTime = new Date();
	}
	
	public UserProjRole(String userId, String projId, String roleId, CreateUserDto createUserDto) {
		super();
		this.userId = userId;
		this.projId = projId;
		this.roleId = roleId;
		this.userType = UserType.IN;
		BeanUtils.copyProperties(createUserDto, this);
	}
	
	public UserProjRole(String userId, String projId, String roleId, UserType userType, String createUserId) {
		super();
		this.userId = userId;
		this.projId = projId;
		this.roleId = roleId;
		this.userType = userType;
		this.createUserId = createUserId;
		this.createTime = new Date();
	}
	
	public UserProjRole(String userId, String projId, String roleId, UserType userType, CreateUserDto createUserDto) {
		super();
		this.userId = userId;
		this.projId = projId;
		this.roleId = roleId;
		this.userType = userType;
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

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public UserType getUserType() {
		return userType;
	}

	public void setUserType(UserType userType) {
		this.userType = userType;
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
