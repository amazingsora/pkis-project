package com.tradevan.aporg.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.MapKeyClass;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.commons.codec.digest.DigestUtils;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.beans.BeanUtils;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.tradevan.apcommon.bean.CreateUserDto;
import com.tradevan.apcommon.persistence.GenericEntity;
import com.tradevan.aporg.bean.UserDto;
import com.tradevan.aporg.enums.Gender;
import com.tradevan.aporg.enums.UserState;
import com.tradevan.aporg.enums.UserType;

/**
 * Title: UserProf<br>
 * Description: <br>
 * Company: Tradevan Co.<br>
 * 
 * @author Neil Chen
 * @version 1.8.2
 */
@IdClass(UserProfPK.class)
@Entity
/*
@NamedQueries(value = {
	@NamedQuery(name = "UserProf.findUserIdAndEmailInUserIds", 
		query = "SELECT u.userId, u.email FROM UserProf u WHERE u.userId in ?1 AND u.userState = com.tradevan.aporg.enums.UserState.A AND u.status = 'Y' ORDER BY u.userId"),
	@NamedQuery(name = "UserProf.findUserIdAndEmailInSerNos", 
		query = "SELECT u.userId, u.email FROM UserProf u WHERE u.id in ?1 AND u.userState = com.tradevan.aporg.enums.UserState.A AND u.status = 'Y' ORDER BY u.userId"),
	@NamedQuery(name = "UserProf.findUserIdAndEmailInDeptSerNos", 
		query = "SELECT u.userId, u.email FROM UserProf u join u.dept d WHERE d.id in ?1 AND u.userState = com.tradevan.aporg.enums.UserState.A AND u.status = 'Y' ORDER BY u.userId"),
	@NamedQuery(name = "UserProf.findUserIdAndEmailInJobTypeSerNos", 
		query = "SELECT u.userId, u.email FROM UserProf u WHERE u.jobTypeSerNo in ?1 AND u.userState = com.tradevan.aporg.enums.UserState.A AND u.status = 'Y' ORDER BY u.userId"),
	@NamedQuery(name = "UserProf.findUserIdAndEmailInJobRankSerNos", 
		query = "SELECT u.userId, u.email FROM UserProf u WHERE u.jobRankSerNo in ?1 AND u.userState = com.tradevan.aporg.enums.UserState.A AND u.status = 'Y' ORDER BY u.userId"),
	@NamedQuery(name = "UserProf.findUserIdAndEmailInTrainingOrgSerNos", 
		query = "SELECT u.userId, u.email FROM UserProf u WHERE u.trainingOrgSerNo in ?1 AND u.status = 'Y' ORDER BY u.userId")
})*/
@Table(name="XAUTH_USERS")
public class UserProf extends GenericEntity<Long> implements Comparable<UserProf> {
	private static final long serialVersionUID = 1L;

	
	//@Column(name = "serNo", unique = true, nullable = false)
	//private Long id;
	
	@Id
	@Column(name="APP_ID")
	private String appId;


	@Id
	@Column(name="USER_ID")
	private String userId;
	
	@Id
	@Column(name = "IDEN_ID")
	private String deptId; // 主要部門代號
	
	 /**
     * 電子郵件重新認証識別碼
     */
	@Column(name="EMAIL_TOKEN")
    private String emailToken;

    /**
     * 電子郵件重新認証到期日
     */
	@Column(name="EMAIL_EXPIRE")
    private Date emailExpire;
	
	//@Enumerated(EnumType.STRING)
	@Column(name = "USER_TYPE", nullable = false, length = 20)
	private String userType;
	
	@Column(name = "USER_CNAME", nullable = false, length = 50)
	private String name;
	
	@Column(name = "ENGNAME", length = 50)
	private String engName;
	
	@Column(name = "PWDHASH", nullable = false, length = 200)
	private String pwdHash;
	
	@Column(name = "EMAIL",length = 100)
	private String email;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumns({
        @JoinColumn(name="IDEN_ID", referencedColumnName="IDEN_ID", insertable = false, updatable = false),
        @JoinColumn(name="APP_ID", referencedColumnName="APP_ID", insertable = false, updatable = false)
    })
	private DeptProf dept;
	
	
	
	@Column(name = "JOBTYPESERNO")
	private Long jobTypeSerNo; // 職類序號
	
	@Column(name = "SUBJOBTYPESERNO")
	private Long subJobTypeSerNo; // 職類小類序號
	
	@Column(name = "JOBRANKSERNO")
	private Long jobRankSerNo; // 職級序號
	
	@Column(name = "JOBTITLESERNO")
	private Long jobTitleSerNo; // 職稱序號
	
	@Column(name = "DUTYTYPESERNO")
	private Long dutyTypeSerNo; // 職責類型序號
	
	@Enumerated(EnumType.STRING)
	private Gender gender;
	
	@Column(name = "PHONE", length = 50)
	private String phone;
	
	@Column(name = "EXT", length = 50)
	private String ext;
	
	@Column(name = "MOBILENO", length = 20)
	private String mobileNo;
	
	@Column(name = "IDCARDNO", length = 20)
	private String idCardNo; // 身分證字號
	
	@Column(name = "BIRTHDAY", length = 8)
	private String birthday;
	
	@Column(name = "ONBOARDDATE", length = 8)
	private String onBoardDate;
	
	@Column(name = "LEAVEDATE", length = 8)
	private String leaveDate;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "USERSTATE", length = 5)
	private UserState userState;
	
	@Column(name = "TRAININGORGSERNO")
	private Long trainingOrgSerNo; // 送訓機構序號
	
	@ManyToMany(fetch = FetchType.LAZY,cascade = CascadeType.PERSIST)
	@JoinTable(
		name = "XAUTH_ROLE_USER",
		joinColumns = { 
				@JoinColumn(name = "USER_ID", referencedColumnName="USER_ID", insertable = false, updatable = false),
				@JoinColumn(name = "APP_ID", referencedColumnName="APP_ID", insertable = false, updatable = false),
				@JoinColumn(name = "IDEN_ID", referencedColumnName="IDEN_ID", insertable = false, updatable = false)
		},
		inverseJoinColumns = {
				@JoinColumn(name = "APP_ID", referencedColumnName="APP_ID", insertable = false, updatable = false),
				@JoinColumn(name = "IDEN_ID", referencedColumnName="IDEN_ID", insertable = false, updatable = false)
				
		}
	)
	@org.hibernate.annotations.BatchSize(size = 10)
	private Set<DeptProf> depts = new HashSet<DeptProf>();
	
//	@OneToMany(mappedBy = "deptProf")
//	@org.hibernate.annotations.BatchSize(size = 10)
//	private Set<UserDeptRole> depts = new HashSet<UserDeptRole>();
	
	@ManyToMany(fetch = FetchType.LAZY,cascade = CascadeType.PERSIST)
	@JoinTable(
		name = "XAUTH_ROLE_USER",
		joinColumns={
			 @JoinColumn(name="USER_ID", referencedColumnName="USER_ID", insertable = false, updatable = false),
			 @JoinColumn(name="APP_ID", referencedColumnName="APP_ID", insertable = false, updatable = false),
			 @JoinColumn(name="IDEN_ID", referencedColumnName="IDEN_ID", insertable = false, updatable = false)
		},
		//joinColumns = @JoinColumn(name = "userId", referencedColumnName="userId"),
		//inverseJoinColumns = @JoinColumn(name = "roleId", referencedColumnName="roleId")
		inverseJoinColumns={
			 @JoinColumn(name="ROLE_ID", referencedColumnName="ROLE_ID", insertable = false, updatable = false),
			 @JoinColumn(name="APP_ID", referencedColumnName="APP_ID", insertable = false, updatable = false),
			 @JoinColumn(name="IDEN_ID", referencedColumnName="IDEN_ID", insertable = false, updatable = false)
		}
	)
//	@OneToMany(mappedBy = "userProf")
	@org.hibernate.annotations.BatchSize(size = 10)
	//private Set<UserDeptRole> roles = new HashSet<UserDeptRole>();
	private Set<RoleProf> roles = new HashSet<RoleProf>();
	
//	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
//	@JoinColumn(name = "userId", referencedColumnName="userId")
//	private Set<UserProjRole> projRoles = new HashSet<UserProjRole>();
//	
//	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
//	@JoinColumn(name = "userId", referencedColumnName="userId")
//	private Set<UserProjAuth> projAuths = new HashSet<UserProjAuth>();
	
	@Column(name="SYSID", length = 30)
	private String sysId;
	
	@Column(name="ENABLED",nullable = false, length = 1)
	private String status;
	
	@Column(name = "PREEMPCODE")
	private String preEmpCode; // 職前課程分類
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "PREEMPDATE")
	private Date preEmpDate; // 職前網路課程完成日期
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "ICHALFREALDATE")
	private Date icHalfRealDate; // 半天實體課程完成日期
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "IC2HRDATE")
	private Date ic2HrDate; // 感控課程2小時完成日期
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "IC6HRDATE")
	private Date ic6HrDate; // 感控6小時最後完成日期
	
	@Column(name = "SENDEMAIL", length = 5)
	private String sendEmail; // 是否發送提醒Email
	
	@Column(name = "CARDNO", length = 50)
	private String cardNo;
	
	@Column(name = "CRE_USER",length = 30)
	private String createUserId;
	
	@Column(name = "UPD_USER",length = 30)
	private String updateUserId;
	
	 /**
     * 是否啟用 0:停用 1:啟用
     */
	//@Column(name="ENABLED")
   // private String enabled;

    /**
     * 是否第一次登入 0:否 1:是
     */
    @Column(name="IS_FIRST")
    private String isFirst;
    
    /**
     * 使用者密碼
     */
    @Column(name="USER_PW")
    private String userPw;
	
	@Column(name = "CREATEAGENTID", length = 30)
	private String createAgentId;
	
	@Column(name = "UPDATEAGENTID", length = 50)
	private String updateAgentId;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CRE_DATE", nullable = false)
	private Date createTime;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPD_DATE")
	private Date updateTime;
	
	@Column(name = "SYSMEMO", length = 50)
	private String sysMemo;
	
	@Column(name = "DUTYPLACE", length = 50)
	private String dutyPlace;
	
	public UserProf() {
		super();
	}

	public UserProf(String userId, String name, String pwdHash, String email, DeptProf dept, String sysId, String createUserId) {
		this(userId, UserType.APCRC02, name, pwdHash, email, dept, sysId, createUserId);
	}
	
	public UserProf(String userId, UserType userType, String name, String pwdHash, String email, DeptProf dept, String sysId, String createUserId) {
		super();
		this.userId = userId;
		this.userType = userType.getCode();
		this.name = name;
		this.pwdHash = pwdHash;
		this.email = email;
		this.dept = dept;
		this.sysId = sysId;
		this.status = "Y";
		this.createUserId = createUserId;
		this.updateUserId = createUserId;
		Date now = new Date();
		this.createTime = now;
		this.updateTime = now;
	}
	
	public UserProf(UserDto userDto, CreateUserDto createUserDto) {
		BeanUtils.copyProperties(userDto, this, "id");
		setPwdHash(DigestUtils.sha1Hex(userDto.getPwd()));
		BeanUtils.copyProperties(createUserDto, this);
	}
	
	@Override
	public Long getId() {
		if((null!=appId && !"".equals(appId)) 
				&& (null!=deptId && !"".equals(deptId))
				&& (null!=userId && !"".equals(userId))) {
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

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getEngName() {
		return engName;
	}

	public void setEngName(String engName) {
		this.engName = engName;
	}

	public String getPwdHash() {
		return pwdHash;
	}

	public void setPwdHash(String pwdHash) {
		this.pwdHash = pwdHash;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public DeptProf getDept() {
		return dept;
	}

	public void setDept(DeptProf dept) {
		this.dept = dept;
	}
	
	public String getDeptId() {
		return deptId;
	}

	public Long getJobTypeSerNo() {
		return jobTypeSerNo;
	}

	public void setJobTypeSerNo(Long jobTypeSerNo) {
		this.jobTypeSerNo = jobTypeSerNo;
	}

	public Long getJobRankSerNo() {
		return jobRankSerNo;
	}
	
	public Long getSubJobTypeSerNo() {
		return subJobTypeSerNo;
	}

	public void setSubJobTypeSerNo(Long subJobTypeSerNo) {
		this.subJobTypeSerNo = subJobTypeSerNo;
	}

	public void setJobRankSerNo(Long jobRankSerNo) {
		this.jobRankSerNo = jobRankSerNo;
	}

	public Long getJobTitleSerNo() {
		return jobTitleSerNo;
	}

	public void setJobTitleSerNo(Long jobTitleSerNo) {
		this.jobTitleSerNo = jobTitleSerNo;
	}
	
	public Long getDutyTypeSerNo() {
		return dutyTypeSerNo;
	}

	public void setDutyTypeSerNo(Long dutyTypeSerNo) {
		this.dutyTypeSerNo = dutyTypeSerNo;
	}

	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	public String getIdCardNo() {
		return idCardNo;
	}

	public void setIdCardNo(String idCardNo) {
		this.idCardNo = idCardNo;
	}
	
	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getOnBoardDate() {
		return onBoardDate;
	}

	public void setOnBoardDate(String onBoardDate) {
		this.onBoardDate = onBoardDate;
	}

	public String getLeaveDate() {
		return leaveDate;
	}

	public void setLeaveDate(String leaveDate) {
		this.leaveDate = leaveDate;
	}

	public UserState getUserState() {
		return userState;
	}

	public void setUserState(UserState userState) {
		this.userState = userState;
	}

	public Long getTrainingOrgSerNo() {
		return trainingOrgSerNo;
	}

	public void setTrainingOrgSerNo(Long trainingOrgSerNo) {
		this.trainingOrgSerNo = trainingOrgSerNo;
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

	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
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

	public Set<DeptProf> getDepts() {
		return depts;
	}

	public void setDepts(Set<DeptProf> depts) {
		this.depts = depts;
	}
	
	public void addDept(DeptProf dept) {
		depts.add(dept);
	}

	public void removeDept(DeptProf dept) {
		depts.remove(dept);
	}
	
	

	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}
	
	public Set<RoleProf> getRoles() {
		return roles;
	}
    

	//
	public void setRoles(Set<RoleProf> roles) {
		this.roles = roles;
	}

	public void addRole(RoleProf role) {
		roles.add(role);
	}

	public void removeRole(RoleProf role) {
		roles.remove(role);
	}

//	public Set<UserProjRole> getProjRoles() {
//		return projRoles;
//	}
//
//	public void setProjRoles(Set<UserProjRole> projRoles) {
//		this.projRoles = projRoles;
//	}
//	
//	public void addProjRole(UserProjRole projRole) {
//		projRoles.add(projRole);
//	}

//	public void removeProjRole(UserProjRole projRole) {
//		projRoles.remove(projRole);
//	}
//
//	public Set<UserProjAuth> getProjAuths() {
//		return projAuths;
//	}
//
//	public void setProjAuths(Set<UserProjAuth> projAuths) {
//		this.projAuths = projAuths;
//	}
//	
//	public void addProjAuth(UserProjAuth projAuth) {
//		projAuths.add(projAuth);
//	}
//
//	public void removeProjAuth(UserProjAuth projAuth) {
//		projAuths.remove(projAuth);
//	}

	
	@Override
	public int compareTo(UserProf bean) {
		return this.getUserId().compareTo(bean.getUserId());
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == this)
			return true;
		if (!(obj instanceof UserProf))
			return false;
		UserProf equalCheck = (UserProf) obj;
		if ((appId == null && equalCheck.appId != null) || (appId != null && equalCheck.appId == null))
			return false;
		if (deptId != null && !deptId.equals(equalCheck.deptId))
			return false;
		if ((userId == null && equalCheck.userId != null) || (userId != null && equalCheck.userId == null))
			return false;
		return true;
    }

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getExt() {
		return ext;
	}

	public void setExt(String ext) {
		this.ext = ext;
	}

	public String getPreEmpCode() {
		return preEmpCode;
	}

	public void setPreEmpCode(String preEmpCode) {
		this.preEmpCode = preEmpCode;
	}

	public Date getIcHalfRealDate() {
		return icHalfRealDate;
	}

	public void setIcHalfRealDate(Date icHalfRealDate) {
		this.icHalfRealDate = icHalfRealDate;
	}

	public Date getIc2HrDate() {
		return ic2HrDate;
	}

	public void setIc2HrDate(Date ic2HrDate) {
		this.ic2HrDate = ic2HrDate;
	}

	public Date getIc6HrDate() {
		return ic6HrDate;
	}

	public void setIc6HrDate(Date ic6HrDate) {
		this.ic6HrDate = ic6HrDate;
	}

	public Date getPreEmpDate() {
		return preEmpDate;
	}

	public void setPreEmpDate(Date preEmpDate) {
		this.preEmpDate = preEmpDate;
	}

	public String getSendEmail() {
		return sendEmail;
	}

	public void setSendEmail(String sendEmail) {
		this.sendEmail = sendEmail;
	}

	public String getSysMemo() {
		return sysMemo;
	}

	public void setSysMemo(String sysMemo) {
		this.sysMemo = sysMemo;
	}

	public String getDutyPlace() {
		return dutyPlace;
	}

	public void setDutyPlace(String dutyPlace) {
		this.dutyPlace = dutyPlace;
	}

	


	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getEmailToken() {
		return emailToken;
	}

	public void setEmailToken(String emailToken) {
		this.emailToken = emailToken;
	}

	public Date getEmailExpire() {
		return emailExpire;
	}

	public void setEmailExpire(Date emailExpire) {
		this.emailExpire = emailExpire;
	}

	

	public String getIsFirst() {
		return isFirst;
	}

	public void setIsFirst(String isFirst) {
		this.isFirst = isFirst;
	}

	public String getUserPw() {
		return userPw;
	}

	public void setUserPw(String userPw) {
		this.userPw = userPw;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.appId.hashCode();
		hash = hash * prime + this.userId.hashCode();
		hash = hash * prime + this.deptId.hashCode();
		
		return hash;
	}
	
}
