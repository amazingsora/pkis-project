package com.tradevan.aporg.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.beans.BeanUtils;

import com.tradevan.apcommon.bean.CreateUserDto;
import com.tradevan.apcommon.persistence.GenericEntity;
import com.tradevan.aporg.bean.ProjDto;

/**
 * Title: ProjProf<br>
 * Description: <br>
 * Company: Tradevan Co.<br>
 * 
 * @author Neil Chen
 * @version 1.1
 */
@Entity
public class ProjProf extends GenericEntity<Long> {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue
	@Column(name = "serNo", unique = true, nullable = false)
	private Long id;
	
	@Column(unique = true, nullable = false, length = 30)
	private String projId;
	
	@Column(name = "projName", nullable = false, length = 300)
	private String name;
	
//	@ManyToMany(cascade = CascadeType.PERSIST)
//	@JoinTable(
//		name = "ProjRole",
//		joinColumns = @JoinColumn(name = "projId", referencedColumnName="projId"),
//		inverseJoinColumns = @JoinColumn(name = "roleId", referencedColumnName="ROLE_ID")
//	)
//	@org.hibernate.annotations.BatchSize(size = 10)
//	private Set<RoleProf> roles = new HashSet<RoleProf>();
	
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "projId", referencedColumnName="projId")
	private Set<UserProjRole> roleUsers = new HashSet<UserProjRole>();
	
	@ManyToMany(cascade = CascadeType.PERSIST)
	@JoinTable(
		name = "ProjAuth",
		joinColumns = @JoinColumn(name = "projId", referencedColumnName="projId"),
		inverseJoinColumns = @JoinColumn(name = "authId", referencedColumnName="authId")
	)
	@org.hibernate.annotations.BatchSize(size = 10)
	private Set<AuthProf> auths = new HashSet<AuthProf>();
	
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "projId", referencedColumnName="projId")
	private Set<UserProjAuth> authUsers = new HashSet<UserProjAuth>();
	
	@Column(length = 30)
	private String sysId;
	
	@Column(nullable = false, length = 1)
	private String status;
	
	@Column(length = 30)
	private String createUserId;
	
	@Column(length = 30)
	private String updateUserId;
	
	@Column(length = 30)
	private String createAgentId;
	
	@Column(length = 30)
	private String updateAgentId;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable = false)
	private Date createTime;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable = false)
	private Date updateTime;
	
	public ProjProf() {
		super();
	}

	public ProjProf(String projId, String name, String sysId, String createUserId) {
		super();
		this.projId = projId;
		this.name = name;
		this.sysId = sysId;
		this.status = "Y";
		this.createUserId = createUserId;
		this.updateUserId = createUserId;
		Date now = new Date();
		this.createTime = now;
		this.updateTime = now;
	}
	
	public ProjProf(ProjDto projDto, CreateUserDto createUserDto) {
		BeanUtils.copyProperties(projDto, this, "id");
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

	public String getProjId() {
		return projId;
	}

	public void setProjId(String projId) {
		this.projId = projId;
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
	
//	public Set<RoleProf> getRoles() {
//		return roles;
//	}
//
//	public void setRoles(Set<RoleProf> roles) {
//		this.roles = roles;
//	}
//
//	public void addRole(RoleProf role) {
//		roles.add(role);
//	}
//
//	public void removeRole(RoleProf role) {
//		roles.remove(role);
//	}
	
	public Set<UserProjRole> getRoleUsers() {
		return roleUsers;
	}

	public void setRoleUsers(Set<UserProjRole> roleUsers) {
		this.roleUsers = roleUsers;
	}
	
	public void addRoleUser(UserProjRole roleUser) {
		roleUsers.add(roleUser);
	}

	public void removeRoleUser(UserProjRole roleUser) {
		roleUsers.remove(roleUser);
	}

	public Set<AuthProf> getAuths() {
		return auths;
	}

	public void setAuths(Set<AuthProf> auths) {
		this.auths = auths;
	}
	
	public void addAuth(AuthProf auth) {
		auths.add(auth);
	}

	public void removeAuth(AuthProf auth) {
		auths.remove(auth);
	}

	public Set<UserProjAuth> getAuthUsers() {
		return authUsers;
	}

	public void setAuthUsers(Set<UserProjAuth> authUsers) {
		this.authUsers = authUsers;
	}
	
	public void addAuthUser(UserProjAuth authUser) {
		authUsers.add(authUser);
	}

	public void removeAuthUser(UserProjAuth authUser) {
		authUsers.remove(authUser);
	}
}
