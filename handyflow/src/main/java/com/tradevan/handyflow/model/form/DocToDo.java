package com.tradevan.handyflow.model.form;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

import org.springframework.beans.BeanUtils;

import com.tradevan.apcommon.bean.CreateUserDto;
import com.tradevan.apcommon.persistence.GenericEntity;
import com.tradevan.aporg.model.UserProf;

/**
 * Title: DocToDo<br>
 * Description: <br>
 * Company: Tradevan Co.<br>
 * 
 * @author Neil Chen
 * @version 1.4
 */
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"formId", "toDoNo", "serialNo"}))
@NamedQueries(value = {
	@NamedQuery(name = "DocToDo.findBySysIdAndProjId", 
		query = "SELECT t FROM DocToDo t WHERE t.sysId = ?1 AND t.projId = ?2 ORDER BY t.userId"),
})
public class DocToDo extends GenericEntity<Long> {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue
	@Column(name = "serNo", unique = true, nullable = false)
	private Long id;
	
	@Column(name="APP_ID",length = 30)
	private String appId;
	
	@Column(name="IDEN_ID",length = 30)
	private String deptId;
	
	@ManyToOne
	@JoinColumn(name="formId", referencedColumnName="formId", nullable = false)
	private FormConf formConf;
	
	@Column(name = "formId", length = 30, insertable = false, updatable = false)
	private String formId;
	
	@Column(nullable = false, length = 30)
	private String toDoNo;
	
	@Column(nullable = false)
	private Integer serialNo;
	
	@Column(nullable = false, length = 30)
	private String userId;
	
	@Column(length = 30)
	private String sysId;
	
	@Column(length = 30)
	private String projId;
	
	@Column(length = 1000)
	private String subject;
	
	@Column(nullable = false, length = 1)
	private String status;
	
	@Column(length = 1)
	private String oldStatus;
	
	@Column(name = "mainFormSerNo")
	private Long mainFormId;
	
	@Column(length = 100)
	private String otherInfo;
	
	@Column(name = "otherSerNo")
	private Long otherSerNo;
	
	@Column(length = 30)
	private String createUserId;
	
	@ManyToOne(fetch=FetchType.LAZY)
	//@JoinColumn(name="createUserId", referencedColumnName="USER_ID", insertable = false, updatable = false)
	@JoinColumns({
			@JoinColumn(name="createUserId", referencedColumnName="USER_ID", nullable = false, insertable = false, updatable = false),
			@JoinColumn(name="APP_ID", referencedColumnName="APP_ID", nullable = false, insertable = false, updatable = false),
			@JoinColumn(name="IDEN_ID", referencedColumnName="IDEN_ID", nullable = false, insertable = false, updatable = false)
	})
	private UserProf createUser;
	
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
	
	public DocToDo() {
		super();
	}

	public DocToDo(FormConf formConf, String toDoNo, Integer serialNo, String userId, String sysId, String projId, String subject, Long mainFormId) {
		super();
		this.formConf = formConf;
		this.toDoNo = toDoNo;
		this.serialNo = serialNo;
		this.userId = userId;
		this.sysId = sysId;
		this.projId = projId;
		this.subject = subject;
		this.status = "W";
		this.mainFormId = mainFormId;
	}
	
	public DocToDo(FormConf formConf, String toDoNo, Integer serialNo, String userId, String sysId, String projId, String subject, Long mainFormId, String createUserId) {
		this(formConf, toDoNo, serialNo, userId, sysId, projId, subject, mainFormId);
		Date now = new Date();
		this.createUserId = createUserId;
		this.updateUserId = createUserId;
		this.createTime = now;
		this.updateTime = now;
	}

	public DocToDo(FormConf formConf, String toDoNo, Integer serialNo, String userId, String sysId, String projId, String subject, Long mainFormId, CreateUserDto createUserDto) {
		this(formConf, toDoNo, serialNo, userId, sysId, projId, subject, mainFormId);
		BeanUtils.copyProperties(createUserDto, this);
	}
	
	public DocToDo(FormConf formConf, String toDoNo, Integer serialNo, String userId, String sysId, String projId, String subject, Long mainFormId, 
			String otherInfo, Long otherSerNo, String createUserId) {
		this(formConf, toDoNo, serialNo, userId, sysId, projId, subject, mainFormId, createUserId);
		this.otherInfo = otherInfo;
		this.otherSerNo = otherSerNo;
	}
	
	public DocToDo(FormConf formConf, String toDoNo, Integer serialNo, String userId, String sysId, String projId, String subject, Long mainFormId, 
			String otherInfo, Long otherSerNo, CreateUserDto createUserDto) {
		this(formConf, toDoNo, serialNo, userId, sysId, projId, subject, mainFormId, createUserDto);
		this.otherInfo = otherInfo;
		this.otherSerNo = otherSerNo;
	}
	
	@Override
	public Long getId() {
		return id;
	}
	
	@Override
	public void setId(Long id) {
		throw new IllegalArgumentException("The id can't be changed.");
	}

	public FormConf getFormConf() {
		return formConf;
	}
	
	public String getFormId() {
		return formId;
	}

	public String getToDoNo() {
		return toDoNo;
	}

	public Integer getSerialNo() {
		return serialNo;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getSysId() {
		return sysId;
	}

	public void setSysId(String sysId) {
		this.sysId = sysId;
	}

	public String getProjId() {
		return projId;
	}

	public void setProjId(String projId) {
		this.projId = projId;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	public String getOldStatus() {
		return oldStatus;
	}

	public void setOldStatus(String oldStatus) {
		this.oldStatus = oldStatus;
	}

	public Long getMainFormId() {
		return mainFormId;
	}

	public void setMainFormId(Long mainFormId) {
		this.mainFormId = mainFormId;
	}
	
	public String getOtherInfo() {
		return otherInfo;
	}

	public void setOtherInfo(String otherInfo) {
		this.otherInfo = otherInfo;
	}

	public Long getOtherSerNo() {
		return otherSerNo;
	}

	public void setOtherSerNo(Long otherSerNo) {
		this.otherSerNo = otherSerNo;
	}

	public String getCreateUserId() {
		return createUserId;
	}

	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}
	
	public UserProf getCreateUser() {
		return createUser;
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
	
}
