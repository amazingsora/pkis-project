package com.tradevan.aporg.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.Entity;

import org.springframework.beans.BeanUtils;

import com.tradevan.apcommon.util.DateUtil;
import com.tradevan.aporg.model.ProjProf;
import com.tradevan.aporg.model.RoleProf;

/**
 * Title: ProjDto<br>
 * Description: <br>
 * Company: Tradevan Co.<br>
 * 
 * @author Neil Chen
 * @version 1.0
 */
@Entity
public class ProjDto implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Long id;
	
	private String projId;
	
	private String name;
	
	private List<String> roleIds;
	
	private String sysId;
	
	private String status;
	
	private String createUserId;
	
	private String updateUserId;
	
	private String createTime;
	
	private String updateTime;

	/**
	 * 2018/03/22 Added by Sephiro : 找尋同一計劃下，未被指定的權限別/帳號/角色
	 */
	private List<Map<String, Object>> notAssignedList; 
	
	/**
	 * 2018/03/22 Added by Sephiro : 找尋同一系統別下，已被指定的權限別/帳號/角色
	 */
	private List<Map<String, Object>> assignedList; 
	
	/**
	 * 2018/03/22 Added by Sephiro : 最後要指定此角色的帳號們
	 */
	private String[] asAry;
	
	private Long serNo;
	
	private String authId;
	
	private String authName;
	
	private String roleId;
	
	private String roleName;
	
	public ProjDto() {
	}

	public ProjDto(ProjProf proj) {
		BeanUtils.copyProperties(proj, this);
//		Set<RoleProf> roles = proj.getRoles();
//		if (roles != null && roles.size() > 0) {
//			List<String> roleIds = new ArrayList<String>();
//			for (RoleProf role : roles) {
//				roleIds.add(role.getRoleId());
//			}
//			setRoleIds(roleIds);
//		}
		setCreateTime(DateUtil.formatDate(proj.getCreateTime(), DateUtil.FMT_YYYY_MM_DD_HH_MM_SS));
		setUpdateTime(DateUtil.formatDate(proj.getUpdateTime(), DateUtil.FMT_YYYY_MM_DD_HH_MM_SS));
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public List<Map<String, Object>> getNotAssignedList() {
		return notAssignedList;
	}

	public void setNotAssignedList(List<Map<String, Object>> notAssignedList) {
		this.notAssignedList = notAssignedList;
	}

	public List<Map<String, Object>> getAssignedList() {
		return assignedList;
	}

	public void setAssignedList(List<Map<String, Object>> assignedList) {
		this.assignedList = assignedList;
	}

	public String[] getAsAry() {
		return asAry;
	}

	public void setAsAry(String[] asAry) {
		this.asAry = asAry;
	}

	public String getAuthId() {
		return authId;
	}

	public void setAuthId(String authId) {
		this.authId = authId;
	}

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public String getAuthName() {
		return authName;
	}

	public void setAuthName(String authName) {
		this.authName = authName;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public Long getSerNo() {
		return serNo;
	}

	public void setSerNo(Long serNo) {
		this.serNo = serNo;
	}
	
}
