package com.tradevan.aporg.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;

import com.tradevan.apcommon.bean.CreateUserDto;
import com.tradevan.apcommon.persistence.GenericEntity;
import com.tradevan.aporg.bean.RoleDto;

/**
 * Title: RoleProf<br>
 * Description: <br>
 * Company: Tradevan Co.<br>
 * 
 * @author Neil Chen
 * @version 1.2
 */
@IdClass(RoleProfPK.class)
@Entity
@Table(name="XAUTH_ROLE")
public class RoleProf extends GenericEntity<Long> {
	private static final long serialVersionUID = 1L;
	
	
//	@Column(name = "serNo", unique = true, nullable = false)
//	private Long id;
	
	@Id
	@Column(name="ROLE_ID")
	private String roleId;
	
	@Id
	@Column(name = "APP_ID")
	private String appId;
	
	@Id
	@Column(name = "IDEN_ID")
	private String deptId;
	
	@Column(name = "ROLE_CNAME", nullable = false, length = 255)
	private String name;
	
	@Column(name="\"level\"",nullable = false)
	private Integer level;
	
	@Column(name = "ISDEPTROLE", nullable = false)
	private Boolean isDeptRole;
	
	//@OneToMany(mappedBy = "userProf")//@ManyToMany(mappedBy = "roles")
	//private Set<UserDeptRole> userDeptRole = new HashSet<UserDeptRole>();
	//private Set<UserProf> actors = new HashSet<UserProf>();
	//@ManyToMany(mappedBy = "roles")
	//@OneToMany(mappedBy = "roleProf",fetch=FetchType.LAZY)
	
	//@ManyToMany(mappedBy = "roles")
	@ManyToMany(fetch = FetchType.LAZY,cascade = CascadeType.PERSIST)
	@JoinTable(
		name = "XAUTH_ROLE_USER",
		joinColumns={
				 @JoinColumn(name="ROLE_ID", referencedColumnName="ROLE_ID", insertable = false, updatable = false),
				 @JoinColumn(name="APP_ID", referencedColumnName="APP_ID", insertable = false, updatable = false),
				 @JoinColumn(name="IDEN_ID", referencedColumnName="IDEN_ID", insertable = false, updatable = false)
		},
		inverseJoinColumns={
				 @JoinColumn(name="USER_ID", referencedColumnName="USER_ID", insertable = false, updatable = false),
				 @JoinColumn(name="APP_ID", referencedColumnName="APP_ID", insertable = false, updatable = false),
				 @JoinColumn(name="IDEN_ID", referencedColumnName="IDEN_ID", insertable = false, updatable = false)
			
		}
	)
//	@OneToMany(mappedBy = "roleProf",fetch=FetchType.LAZY)
	@org.hibernate.annotations.BatchSize(size = 10)
	private Set<UserProf> actors = new HashSet<UserProf>();
	
	@Column(name="SYSID", length = 30)
	private String sysId;
	
	@Column(name="CATEGORY", length = 30)
	private String category;
	
	@Column(name="STATUS", nullable = false, length = 1)
	private String status;
	
	@Column(name="CRE_USER",length = 30)
	private String createUserId;
	
	@Column(name="UPD_USER",length = 30)
	private String updateUserId;
	
	@Column(name="CREATEAGENTID", length = 30)
	private String createAgentId;
	
	@Column(name="UPDATEAGENTID", length = 30)
	private String updateAgentId;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="CRE_DATE",nullable = false)
	private Date createTime;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="UPD_DATE")
	private Date updateTime;
	
	public RoleProf() {
		super();
	}

	public RoleProf(String roleId, String name, Integer level, Boolean isDeptRole, String sysId, String createUserId) {
		this(roleId, name, level, isDeptRole, sysId, null, createUserId);
	}
	
	public RoleProf(String roleId, String name, Integer level, Boolean isDeptRole, String sysId, String category, String createUserId) {
		super();
		this.roleId = roleId;
		this.name = name;
		this.level = level;
		this.isDeptRole = isDeptRole;
		this.sysId = sysId;
		this.category = category;
		this.status = "Y";
		this.createUserId = createUserId;
		this.updateUserId = createUserId;
		Date now = new Date();
		this.createTime = now;
		this.updateTime = now;
	}
	
	public RoleProf(RoleDto roleDto, CreateUserDto createUserDto) {
		BeanUtils.copyProperties(roleDto, this, "id");
		BeanUtils.copyProperties(createUserDto, this);
	}
	
	public void applyDefault(String deptRole) {
		setLevel(0);
		setDeptRole(StringUtils.equals(deptRole, "Y") ? true : false);
	}

	@Override
	public Long getId() {
		if((null!=appId && !"".equals(appId)) 
				&& (null!=deptId && !"".equals(deptId))
				&& (null!=roleId && !"".equals(roleId))) {
			return Long.valueOf(this.hashCode());
		}
		return null;
	}
	
	@Override
	public void setId(Long id) {
		throw new IllegalArgumentException("The id can't be changed.");
	}
	
	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public Boolean isDeptRole() {
		return isDeptRole;
	}

	public void setDeptRole(Boolean isDeptRole) {
		this.isDeptRole = isDeptRole;
	}
	
	public String getSysId() {
		return sysId;
	}

	public void setSysId(String sysId) {
		this.sysId = sysId;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
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

	public void setActors(Set<UserProf> actors) {
		this.actors = actors;
	}
	
	
	public Set<UserProf> getActors() {
		return actors;
	}
	
	public Boolean getIsDeptRole() {
		return isDeptRole;
	}

	public void setIsDeptRole(Boolean isDeptRole) {
		this.isDeptRole = isDeptRole;
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
