package com.tradevan.aporg.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

import org.springframework.beans.BeanUtils;

import com.tradevan.apcommon.bean.CreateUserDto;
import com.tradevan.apcommon.persistence.GenericEntity;

/**
 * Title: UserDeptRole<br>
 * Description: <br>
 * Company: Tradevan Co.<br>
 * 
 * @author Neil Chen,Sam Chiang
 * @version 1.3
 */
@IdClass(UserDeptRolePK.class)
@Entity
@Table(name="XAUTH_ROLE_USER")//,uniqueConstraints = @UniqueConstraint(columnNames = {"serNo"}))
public class UserDeptRole extends GenericEntity<Long> {
	private static final long serialVersionUID = 1L;
//	
//	@Id
//	@GeneratedValue
//	@Column(name = "serNo", unique = true, nullable = false)
//	private Long id;
	
	@Id
	@Column(name="APP_ID")
	private String appId;
	
	@Id
	@Column(name="USER_ID")
	private String userId;
	
//	@ManyToOne
//	@JoinColumn(name="userId", referencedColumnName="userId", insertable = false, updatable = false)
//	private UserProf userProf;
	@Id
	@Column(name="IDEN_ID")
	private String deptId;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@MapsId("deptId")
	@JoinColumns({
		   @JoinColumn(name="APP_ID", referencedColumnName="APP_ID",insertable = false, updatable = false),
        @JoinColumn(name="IDEN_ID", referencedColumnName="IDEN_ID",insertable = false, updatable = false)
     
    })
	private DeptProf deptProf;
	
	@Id
	@Column(name="ROLE_ID")
	private String roleId;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@MapsId("roleId")
	@JoinColumns({
		 @JoinColumn(name="ROLE_ID", referencedColumnName="ROLE_ID", insertable = false, updatable = false),
		 @JoinColumn(name="APP_ID", referencedColumnName="APP_ID", insertable = false, updatable = false),
        @JoinColumn(name="IDEN_ID", referencedColumnName="IDEN_ID",insertable = false, updatable = false)
       
       
    })
	private RoleProf roleProf;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@MapsId("userId")
	@JoinColumns({
		 @JoinColumn(name="USER_ID", referencedColumnName="USER_ID", insertable = false, updatable = false),
		 @JoinColumn(name="APP_ID", referencedColumnName="APP_ID", insertable = false, updatable = false),
        @JoinColumn(name="IDEN_ID", referencedColumnName="IDEN_ID",insertable = false, updatable = false)
       
       
    })
	private UserProf userProf;
	
	@Column(name="CRE_USER",length = 30)
	private String createUserId;
	
	@Column(name="CREATEAGENTID", length = 30)
	private String createAgentId;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="CRE_DATE",nullable = false)
	private Date createTime;
	
	@Column(name="UPD_USER",length = 30)
	private String updateUserId;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="UPD_DATE")
	private Date updateTime;
	
	public UserDeptRole() {
		super();
	}
	
	public UserDeptRole(String appId,String userId, String deptId, String roleId, String createUserId) {
		super();
		this.appId = appId;
		this.userId = userId;
		this.deptId = deptId;
		this.roleId = roleId;
		this.createUserId = createUserId;
		this.createTime = new Date();
	}
	
	public UserDeptRole(String userId, String deptId, String roleId, CreateUserDto createUserDto) {
		super();
		this.userId = userId;
		this.deptId = deptId;
		this.roleId = roleId;
		BeanUtils.copyProperties(createUserDto, this);
	}
	
	@Override
	public Long getId() {
		if((null!=appId && !"".equals(appId)) 
				&& (null!=deptId && !"".equals(deptId))
				&& (null!=userId && !"".equals(userId))
				&& (null!=roleId && !"".equals(roleId))) {
			return Long.valueOf(this.hashCode());
		}
		return null;
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
	
	public UserProf getUserProf() {
		return userProf;
	}

	public String getDeptId() {
		return deptId;
	}

	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}

	public DeptProf getDeptProf() {
		return deptProf;
	}

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public RoleProf getRoleProf() {
		return roleProf;
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
	
	
	


	public String getUpdateUserId() {
		return updateUserId;
	}

	public void setUpdateUserId(String updateUserId) {
		this.updateUserId = updateUserId;
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

	public void setDeptProf(DeptProf deptProf) {
		this.deptProf = deptProf;
	}

	public void setRoleProf(RoleProf roleProf) {
		this.roleProf = roleProf;
	}

	public void setUserProf(UserProf userProf) {
		this.userProf = userProf;
	}

	public String toString() {
		return "userId:" + userId + " deptId:" + deptId + " roleId:" + roleId;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.appId.hashCode();
		hash = hash * prime + this.userId.hashCode();
		hash = hash * prime + this.deptId.hashCode();
		hash = hash * prime + this.roleId.hashCode();
		
		return hash;
	}
}
