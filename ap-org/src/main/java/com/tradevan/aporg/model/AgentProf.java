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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.beans.BeanUtils;

import com.tradevan.apcommon.bean.CreateUserDto;
import com.tradevan.apcommon.persistence.GenericEntity;
import com.tradevan.apcommon.util.DateUtil;
import com.tradevan.aporg.bean.AgentDto;

/**
 * Title: AgentProf<br>
 * Description: <br>
 * Company: Tradevan Co.<br>
 * 
 * @author Neil Chen
 * @version 1.1
 */
@IdClass(AgentProfPK.class)
@Entity
@Table(name="XAUTH_ROLE_AGENT_USER")
public class AgentProf extends GenericEntity<Long> {
	private static final long serialVersionUID = 1L;
	
//	@Id
//	@GeneratedValue
//	@Column(name = "serNo", unique = true, nullable = false)
//	private Long id;
	
	@Id
	@Column(name="USER_ID")
	private String userId;
	
	@Id
	@Column(name="APP_ID")
	private String appId;
	
	@Id
	@Column(name="IDEN_ID")
	private String deptId;
	
	@Id
	@Column(name="AGENT_APP_ID")
	private String agentAppId;
	
	@Id
	@Column(name="AGENT_IDEN_ID")
	private String agentDeptId;

	
	@Column(name="ROLE_ID")
	private String roleId;
	
	@Id
	@Column(name="AGENT_USER_ID",nullable = false)
	private String agentUserId;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumns({
        @JoinColumn(name="USER_ID", referencedColumnName="USER_ID",insertable = false, updatable = false),
        @JoinColumn(name="APP_ID", referencedColumnName="APP_ID",insertable = false, updatable = false),
        @JoinColumn(name="IDEN_ID", referencedColumnName="IDEN_ID",insertable = false, updatable = false)
    })
	private UserProf user;
	
	
//	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumns({
        @JoinColumn(name="AGENT_USER_ID", referencedColumnName="USER_ID",insertable = false, updatable = false),
        @JoinColumn(name="AGENT_APP_ID", referencedColumnName="APP_ID",insertable = false, updatable = false),
        @JoinColumn(name="AGENT_IDEN_ID", referencedColumnName="IDEN_ID",insertable = false, updatable = false)
    })
	private UserProf agent;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable = false)
	private Date begDate;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable = false)
	private Date endDate;
	
	@Column(name="ENABLED",nullable = false, length = 1)
	private String status;
	
	@Column(name="CRE_USER",length = 30)
	private String createUserId;
	
	@Column(name="UPD_USER",length = 30)
	private String updateUserId;
	
	@Column(length = 30)
	private String createAgentId;
	
	@Column(length = 30)
	private String updateAgentId;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="CRE_DATE",nullable = false)
	private Date createTime;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="UPD_DATE",nullable = false)
	private Date updateTime;
	
	public AgentProf() {
		super();
	}

	public AgentProf(String userId, String agentUserId, Date begDate, Date endDate, String createUserId) {
		super();
		this.userId = userId;
		this.agentUserId = agentUserId;
		this.begDate = begDate;
		this.endDate = endDate;
		this.status = "Y";
		this.createUserId = createUserId;
		this.updateUserId = createUserId;
		Date now = new Date();
		this.createTime = now;
		this.updateTime = now;
	}

	public AgentProf(AgentDto dto, CreateUserDto createUserDto) {
		BeanUtils.copyProperties(dto, this, "id");
		BeanUtils.copyProperties(createUserDto, this);
	}
	
	public void applyFormat(AgentDto dto) {
		setBegDate(DateUtil.parseDate(dto.getBeginDate() + "-" + dto.getBeginTime(), DateUtil.FMT_YYYY_MM_DD_HH_MM));
		setEndDate(DateUtil.parseDate(dto.getEndDate() + "-" + dto.getEndTime(), DateUtil.FMT_YYYY_MM_DD_HH_MM));
	}

	@Override
	public Long getId() {
		if((null!=appId && !"".equals(appId)) 
				&& (null!=deptId && !"".equals(deptId))
				&& (null!=userId && !"".equals(userId))
				&& (null!=agentAppId && !"".equals(agentAppId))
				&& (null!=agentDeptId && !"".equals(agentDeptId))
				&& (null!=agentUserId && !"".equals(agentUserId))
				
				) {
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
	
	public UserProf getUser() {
		return user;
	}

	public String getAgentUserId() {
		return agentUserId;
	}

	public void setAgentUserId(String agentUserId) {
		this.agentUserId = agentUserId;
	}
	
	public UserProf getAgent() {
		return agent;
	}

	public Date getBegDate() {
		return begDate;
	}
	
	public void setBegDate(Date begDate) {
		this.begDate = begDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
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

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}






	public String getAgentAppId() {
		return agentAppId;
	}

	public void setAgentAppId(String agentAppId) {
		this.agentAppId = agentAppId;
	}

	public String getDeptId() {
		return deptId;
	}

	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}

	public String getAgentDeptId() {
		return agentDeptId;
	}

	public void setAgentDeptId(String agentDeptId) {
		this.agentDeptId = agentDeptId;
	}

	public void setUser(UserProf user) {
		this.user = user;
	}

	public void setAgent(UserProf agent) {
		this.agent = agent;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.appId.hashCode();
		hash = hash * prime + this.userId.hashCode();
		hash = hash * prime + this.deptId.hashCode();
		hash = hash * prime + this.agentAppId.hashCode();
		hash = hash * prime + this.agentDeptId.hashCode();
		hash = hash * prime + this.agentUserId.hashCode();
		
		return hash;
	}
	
	
}
