package com.tradevan.aporg.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tradevan.apcommon.bean.CreateUserDto;
import com.tradevan.apcommon.persistence.GenericEntity;
import com.tradevan.apcommon.util.BeanUtil;

/**
 * Title: DeptProf<br>
 * Description: <br>
 * Company: Tradevan Co.<br>
 * 
 * @author Neil Chen
 * @version 1.3.2
 */
@IdClass(DeptProfPK.class)
@Entity
@Table(name="XAUTH_DEPT")
@org.hibernate.annotations.BatchSize(size = 10)
public class DeptProf extends GenericEntity<Long> {
	private static final long serialVersionUID = 1L;
	
//	@Id
//	@GeneratedValue
//	@Column(name = "serNo", unique = true, nullable = false)
//	private Long id;
	
	@Id
	@Column(name="APP_ID")
	private String appId;
	
	@Column(name="BAN")
	private String ban;
	
	@Column(name="SEQ_NO")
	private Integer seqNo;
	
	@Id
	@Column(name="IDEN_ID")
	private String deptId;
	
	@Column(name = "CNAME", nullable = false, length = 50)
	private String name;
	
	@Column(name = "SHORTNAME", length = 30)
	private String shortName;
	
	@Column(name = "PARENT_SEQ")
	private Integer parentSeq;
	
	

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumns({
        @JoinColumn(name="PARENT_ID", referencedColumnName="IDEN_ID",insertable = false, updatable = false),
        @JoinColumn(name="APP_ID", referencedColumnName="APP_ID",insertable = false, updatable = false)
    })
	@NotFound(action=NotFoundAction.IGNORE)  
	private DeptProf upDept;
	
	@Column(name = "PARENT_ID")
	private String upDeptId;
	
	@OneToMany(fetch = FetchType.LAZY,mappedBy = "upDept")
	private Set<DeptProf> subDepts = new HashSet<DeptProf>();
	
	@Column(name = "\"level\"", nullable = false)
	private Integer level;
	
	@Column(name = "JOBTYPESERNO")
	private Long jobTypeSerNo; // 部門職類序號
	
	@Column(name = "DEPARTMENTNO") //暫時無用 HIS的
	private Integer departmentNo; // 部門序號(HIS)
	
	@Column(name = "PARENTNO") //暫時無用 HIS的
	private Integer parentNo; // 上層部門序號(HIS)
	
	@Column(name = "DEPTCHIEF")
	private String deptChief;
	
	
	//private Set<UserProf> actors = new HashSet<UserProf>();

	
	@OneToMany(fetch = FetchType.LAZY,mappedBy = "depts")
	//@OneToMany(mappedBy = "userProf",fetch=FetchType.LAZY)
	@org.hibernate.annotations.BatchSize(size = 10)
	private Set<UserProf> users = new HashSet<UserProf>();
	

	@ManyToMany(fetch = FetchType.LAZY,cascade = CascadeType.PERSIST)
	@JoinTable(
		name = "XAUTH_ROLE_USER",
		joinColumns = { 
				@JoinColumn(name = "APP_ID", referencedColumnName="APP_ID", insertable = false, updatable = false),
				@JoinColumn(name = "IDEN_ID", referencedColumnName="IDEN_ID", insertable = false, updatable = false)
		},
		inverseJoinColumns = {
				@JoinColumn(name = "USER_ID", referencedColumnName="USER_ID", insertable = false, updatable = false),
				@JoinColumn(name = "APP_ID", referencedColumnName="APP_ID", insertable = false, updatable = false),
				@JoinColumn(name = "IDEN_ID", referencedColumnName="IDEN_ID", insertable = false, updatable = false)
		}
	)
	@org.hibernate.annotations.BatchSize(size = 10)
	private Set<UserProf> otherUsers = new HashSet<UserProf>();
	
	@ManyToMany(fetch = FetchType.LAZY,cascade = CascadeType.PERSIST)
//	@JoinTable(
//		name = "DeptRole",
//		joinColumns = @JoinColumn(name = "deptId", referencedColumnName="deptId"),
//		inverseJoinColumns = @JoinColumn(name = "roleId", referencedColumnName="roleId")
//	)
	@JoinTable(
			name = "XAUTH_ROLE_USER",
			joinColumns = { 
					@JoinColumn(name = "APP_ID", referencedColumnName="APP_ID", insertable = false, updatable = false),
					@JoinColumn(name = "IDEN_ID", referencedColumnName="IDEN_ID", insertable = false, updatable = false)
			},
			inverseJoinColumns = {
					@JoinColumn(name = "ROLE_ID", referencedColumnName="ROLE_ID", insertable = false, updatable = false),
					@JoinColumn(name = "APP_ID", referencedColumnName="APP_ID", insertable = false, updatable = false),
					@JoinColumn(name = "IDEN_ID", referencedColumnName="IDEN_ID", insertable = false, updatable = false)
			}
		)
	@org.hibernate.annotations.BatchSize(size = 10)
	private Set<RoleProf> roles = new HashSet<RoleProf>();
	
	@Column(name="ENABLED",nullable = false, length = 1)
	private String status;
	
	
	@Column(name="CRE_USER",length = 30)
	private String createUserId;
	
	@Column(name="UPD_USER",length = 30)
	private String updateUserId;
	
	@Column(name="CREATEAGENTID")
	private String createAgentId;
	
	@Column(name="UPDATEAGENTID")
	private String updateAgentId;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="CRE_DATE",nullable = false)
	private Date createTime;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="UPD_DATE")
	private Date updateTime;
	
	public DeptProf() {
		super();
	}

	public DeptProf(String deptId, String name, DeptProf upDept, Integer level, String createUserId) {
		this(deptId, name, upDept, level, null, createUserId);
	}
	
	public DeptProf(String deptId, String name, DeptProf upDept, Integer level, String deptChief, String createUserId) {
		super();
		this.deptId = deptId;
		this.name = name;
		this.shortName = name;
		this.upDept = upDept;
		this.level = level;
		this.deptChief = deptChief;
		this.status = "Y";
		this.createUserId = createUserId;
		this.updateUserId = createUserId;
		Date now = new Date();
		this.createTime = now;
		this.updateTime = now;
	}
	
	public DeptProf(String deptId, String name, DeptProf upDept, Integer level, Integer departmentNo, Integer parentNo, String deptChief, CreateUserDto createUserDto) {
		super();
		this.deptId = deptId;
		this.name = name;
		this.shortName = name;
		this.upDept = upDept;
		this.level = level;
		this.departmentNo = departmentNo;
		this.parentNo = parentNo;
		this.deptChief = deptChief;
		this.status = "Y";
		BeanUtil.copyProperties(createUserDto, this);
	}
	
	@Override
	public Long getId() {
		if((null!=appId && !"".equals(appId)) 
				&& (null!=deptId && !"".equals(deptId))) {
			return Long.valueOf(this.hashCode());
		}
		return null;
	}
	
	
	
	

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getBan() {
		return ban;
	}

	public void setBan(String ban) {
		this.ban = ban;
	}

	
	public Integer getSeqNo() {
		return seqNo;
	}

	public void setSeqNo(Integer seqNo) {
		this.seqNo = seqNo;
	}

	public Integer getParentSeq() {
		return parentSeq;
	}

	public void setParentSeq(Integer parentSeq) {
		this.parentSeq = parentSeq;
	}

	public void setUpDeptId(String upDeptId) {
		this.upDeptId = upDeptId;
	}

	public void setSubDepts(Set<DeptProf> subDepts) {
		this.subDepts = subDepts;
	}

	public void setUsers(Set<UserProf> users) {
		this.users = users;
	}

	@Override
	public void setId(Long id) {
		throw new IllegalArgumentException("The id can't be changed.");
	}

	public String getDeptId() {
		return deptId;
	}

	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public DeptProf getUpDept() {
		return upDept;
	}

	public void setUpDept(DeptProf upDept) {
		this.upDept = upDept;
	}
	
	public String getUpDeptId() {
		return upDeptId;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}
	
	public Long getJobTypeSerNo() {
		return jobTypeSerNo;
	}

	public void setJobTypeSerNo(Long jobTypeSerNo) {
		this.jobTypeSerNo = jobTypeSerNo;
	}

	public Integer getDepartmentNo() {
		return departmentNo;
	}

	public void setDepartmentNo(Integer departmentNo) {
		this.departmentNo = departmentNo;
	}

	public Integer getParentNo() {
		return parentNo;
	}

	public void setParentNo(Integer parentNo) {
		this.parentNo = parentNo;
	}

	public String getDeptChief() {
		return deptChief;
	}

	public void setDeptChief(String deptChief) {
		this.deptChief = deptChief;
	}

	public Set<RoleProf> getRoles() {
		return roles;
	}

	public void setRoles(Set<RoleProf> roles) {
		this.roles = roles;
	}

	public void addRole(RoleProf role) {
		roles.add(role);
	}

	public void removeRole(RoleProf role) {
		roles.remove(role);
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

	public Set<DeptProf> getSubDepts() {
		return subDepts;
	}

	public Set<UserProf> getUsers() {
		return users;
	}

	public Set<UserProf> getOtherUsers() {
		return otherUsers;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.appId.hashCode();
	
		hash = hash * prime + this.deptId.hashCode();
		
		return hash;
	}
}
