package com.tradevan.aporg.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.BeanUtils;

import com.tradevan.apcommon.util.DateUtil;
import com.tradevan.aporg.enums.Gender;
import com.tradevan.aporg.enums.UserState;
import com.tradevan.aporg.enums.UserType;
import com.tradevan.aporg.model.DeptProf;
import com.tradevan.aporg.model.RoleProf;
import com.tradevan.aporg.model.UserProf;

/**
 * Title: UserDto<br>
 * Description: <br>
 * Company: Tradevan Co.<br>
 * 
 * @author Neil Chen
 * @version 1.3
 */
public class UserDto implements Serializable {
	private static final long serialVersionUID = 1L;
	
	public static final String FAKE_PASSWORD = "********";
	
	private Long id;
	
	private String userId;
	
	private String name;
	
	private String engName;
	
	private String pwd;
	
	private String email;
	
	private String deptId;
	
	private Long jobTypeSerNo;
	
	private Long jobRankSerNo;
	
	private Long jobTitleSerNo;
	
	private Long dutyTypeSerNo;
	
	private Gender gender;
	
	private String phone;
	
	private String ext;
	
	private String mobileNo;
	
	private String idCardNo;
	
	private String birthday;
	
	private String onBoardDate;
	
	private String leaveDate;
	
	private UserState userState;
	
	private Long trainingOrgSerNo;
	
	private List<String> deptIds;
	
	private List<String> roleIds;

	private String sysId;
	
	private UserType userType;
	
	private String status;
	
	private String createUserId;
	
	private String updateUserId;
	
	private String createTime;
	
	private String updateTime;

	private String changeCode;
	
	private String pwdConfirm;
	
	private String origPwd;
	
	private String pwdHash;
	
	
	public UserDto() {
	}

	public UserDto(UserProf user) {
		BeanUtils.copyProperties(user, this);
		setPwd(FAKE_PASSWORD);
		DeptProf mainDept = user.getDept();
		if (mainDept != null) {
			setDeptId(mainDept.getDeptId());
		}
		Set<DeptProf> depts = user.getDepts();
		if (depts != null && depts.size() > 0) {
			List<String> deptIds = new ArrayList<String>();
			for (DeptProf dept : depts) {
				deptIds.add(dept.getDeptId());
			}
			setDeptIds(deptIds);
		}
		Set<RoleProf> roles = user.getRoles();
		if (roles != null && roles.size() > 0) {
			List<String> roleIds = new ArrayList<String>();
			for (RoleProf role : roles) {
				roleIds.add(role.getRoleId());
			}
			setRoleIds(roleIds);
		}
		setCreateTime(DateUtil.formatDate(user.getCreateTime(), DateUtil.FMT_YYYY_MM_DD_HH_MM_SS));
		setUpdateTime(DateUtil.formatDate(user.getUpdateTime(), DateUtil.FMT_YYYY_MM_DD_HH_MM_SS));
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
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

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getDeptId() {
		return deptId;
	}

	public void setDeptId(String deptId) {
		this.deptId = deptId;
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

	public List<String> getDeptIds() {
		return deptIds;
	}

	public void setDeptIds(List<String> deptIds) {
		this.deptIds = deptIds;
	}

	public List<String> getRoleIds() {
		return roleIds;
	}

	public void setRoleIds(List<String> roleIds) {
		this.roleIds = roleIds;
	}
	
	public String getSysId() {
		return sysId;
	}

	public void setSysId(String sysId) {
		this.sysId = sysId;
	}
	
	public UserType getUserType() {
		return userType;
	}

	public void setUserType(UserType userType) {
		this.userType = userType;
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

	public String getChangeCode() {
		return changeCode;
	}

	public void setChangeCode(String changeCode) {
		this.changeCode = changeCode;
	}

	public String getPwdConfirm() {
		return pwdConfirm;
	}

	public void setPwdConfirm(String pwdConfirm) {
		this.pwdConfirm = pwdConfirm;
	}

	public String getOrigPwd() {
		return origPwd;
	}

	public void setOrigPwd(String origPwd) {
		this.origPwd = origPwd;
	}

	public String getPwdHash() {
		return pwdHash;
	}

	public void setPwdHash(String pwdHash) {
		this.pwdHash = pwdHash;
	}
	
}
