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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.JoinColumnOrFormula;
import org.hibernate.annotations.JoinColumnsOrFormulas;
import org.hibernate.annotations.JoinFormula;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.tradevan.apcommon.persistence.GenericEntity;
import com.tradevan.aporg.model.DeptProf;
import com.tradevan.aporg.model.UserProf;

/**
 * Title: DocStateLog<br>
 * Description: <br>
 * Company: Tradevan Co.<br>
 * 
 * @author Neil Chen
 * @version 1.4.1
 */
@Entity
public class DocStateLog extends GenericEntity<Long> {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue
	@Column(name = "serNo", unique = true, nullable = false)
	private Long id;
	
	@Column(name="APP_ID", length = 30, insertable = false, updatable = false)
	private String appId;
	
	@Column(name="IDEN_ID", length = 30, insertable = false, updatable = false)
	private String deptId;
	
	@ManyToOne
	@JoinColumn(name="sourceSerNo", nullable = false)
	private DocState docState;
	
	@ManyToOne
	@JoinColumn(name="formId", referencedColumnName="formId", nullable = false)
	private FormConf formConf;
	
	@Column(nullable = false, length = 30)
	private String applyNo;
	
	@Column(nullable = false)
	private Integer serialNo;
	
	@Column(nullable = false, length = 50)
	private String flowId;
	
	@Column(nullable = false, length = 10)
	private String flowVersion;
	
	@Column(length = 20)
	private String taskType;
	
	@Column(nullable = false, length = 50)
	private String fromTaskId;
	
	@Column(nullable = false, length = 50)
	private String toTaskId;
	
	@Column(nullable = false, length = 20)
	private String flowStatus;
	
	@Column(length = 300)
	private String taskRoleIds;
	
	@Column(length = 30)
	private String taskDeptId;
	
	@Column(length = 300)
	private String taskUserIds;
	
	@Column(length = 100)
	private String taskExt;
	
	@Column(length = 100)
	private String taskName;
	
	@Column(length = 200)
	private String taskDesc;
	
	@Column(length = 100)
	private String linkName;
	
	@Column(length = 200)
	private String linkDesc;
	
	@ManyToOne
	//@JoinColumn(name="applicantId", referencedColumnName="USER_ID", nullable = false)
	@JoinColumns({
		@JoinColumn(name="applicantId", referencedColumnName="USER_ID", nullable = false),
		@JoinColumn(name="APP_ID", referencedColumnName="APP_ID", nullable = false),
		@JoinColumn(name="IDEN_ID", referencedColumnName="IDEN_ID", nullable = false)
	})
	//@JoinColumnsOrFormulas({
	//	@JoinColumnOrFormula(column = @JoinColumn(name="applicantId", referencedColumnName="USER_ID", nullable = false)),
	//	@JoinColumnOrFormula(formula = @JoinFormula(referencedColumnName = "APP_ID", value = "APP_ID")),
	//	@JoinColumnOrFormula(formula = @JoinFormula(referencedColumnName = "IDEN_ID", value = "IDEN_ID"))
	//})
	private UserProf applicant;
	
	@Column(length = 30, insertable = false, updatable = false)
	private String applicantId;
	
	@ManyToOne
	@NotFound(action = NotFoundAction.IGNORE)
	//@JoinColumn(name="userId", referencedColumnName="USER_ID")
	@JoinColumnsOrFormulas({
		@JoinColumnOrFormula(column = @JoinColumn(name="userId", referencedColumnName="USER_ID")),
		@JoinColumnOrFormula(formula = @JoinFormula(referencedColumnName = "APP_ID", value = "APP_ID")),
		@JoinColumnOrFormula(formula = @JoinFormula(referencedColumnName = "IDEN_ID", value = "IDEN_ID"))
	})
	private UserProf user;
	
	@Column(length = 30, insertable = false, updatable = false)
	private String userId;
	
	@ManyToOne
	@NotFound(action = NotFoundAction.IGNORE)
	//@JoinColumn(name="beRepresentedId", referencedColumnName="USER_ID")
	@JoinColumnsOrFormulas({
		@JoinColumnOrFormula(column = @JoinColumn(name="beRepresentedId", referencedColumnName="USER_ID")),
		@JoinColumnOrFormula(formula = @JoinFormula(referencedColumnName = "APP_ID", value = "APP_ID")),
		@JoinColumnOrFormula(formula = @JoinFormula(referencedColumnName = "IDEN_ID", value = "IDEN_ID"))
	})
	private UserProf beRepresented;
	
	@Column(length = 30, insertable = false, updatable = false)
	private String beRepresentedId;
	
	@ManyToOne
	@NotFound(action = NotFoundAction.IGNORE)
	//@JoinColumn(name="agentId", referencedColumnName="USER_ID")
	@JoinColumnsOrFormulas({
		@JoinColumnOrFormula(column = @JoinColumn(name="agentId", referencedColumnName="USER_ID")),
		@JoinColumnOrFormula(formula = @JoinFormula(referencedColumnName = "APP_ID", value = "APP_ID")),
		@JoinColumnOrFormula(formula = @JoinFormula(referencedColumnName = "IDEN_ID", value = "IDEN_ID"))
	})
	private UserProf agent;
	
	@Column(length = 30, insertable = false, updatable = false)
	private String agentId;
	
	
	@Column(length = 30)
	private String byFlowAdminId;
	
	@ManyToOne
	@NotFound(action = NotFoundAction.IGNORE)
	//@JoinColumn(name="subFlowDeptId", referencedColumnName="IDEN_ID")
	@JoinColumnsOrFormulas({
		@JoinColumnOrFormula(column = @JoinColumn(name="subFlowDeptId", referencedColumnName="IDEN_ID")),
		@JoinColumnOrFormula(formula = @JoinFormula(referencedColumnName = "APP_ID", value = "APP_ID")),
	})
	private DeptProf subFlowDept;
	
	@Column(length = 30, insertable = false, updatable = false)
	private String subFlowDeptId;
	
	@ManyToOne
	@NotFound(action = NotFoundAction.IGNORE)
	//@JoinColumn(name="lastReviewDeptId", referencedColumnName="IDEN_ID")
	@JoinColumnsOrFormulas({
		@JoinColumnOrFormula(column = @JoinColumn(name="lastReviewDeptId", referencedColumnName="IDEN_ID")),
		@JoinColumnOrFormula(formula = @JoinFormula(referencedColumnName = "APP_ID", value = "APP_ID")),
	})
	private DeptProf lastReviewDept;
	
	@Column(length = 30, insertable = false, updatable = false)
	private String lastReviewDeptId;
	
	@Column(length = 30)
	private String projId;
	
	@Column(length = 1)
	private String isProjRole;
	
	@Column(length = 1)
	private String isReviewDeptRole;
	
	@Column(length = 1)
	private String canBatchReview;
	
	@Column(length = 2000)
	private String memo;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column
	private Date stateCreateTime;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column
	private Date stateUpdateTime;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable = false)
	private Date createTime;
	
	public DocStateLog() {
		super();
	}

	public DocStateLog(DocState docState, String taskType, String fromTaskId, String toTaskId, UserProf beRepresented, UserProf agent, String memo) {
		super();
		this.docState = docState;
		this.formConf = docState.getFormConf();
		this.applyNo = docState.getApplyNo();
		this.serialNo = docState.getSerialNo();
		this.flowId = docState.getFlowId();
		this.flowVersion = docState.getFlowVersion();
		this.flowStatus = docState.getFlowStatus();
		this.taskRoleIds = docState.getTaskRoleIds();
		this.taskDeptId = docState.getTaskDeptId();
		this.taskUserIds = docState.getTaskUserIds();
		this.taskExt = docState.getTaskExt();
		this.taskName = docState.getTaskName();
		this.taskDesc = docState.getTaskDesc();
		this.linkName = docState.getLinkName();
		this.linkDesc = docState.getLinkDesc();
		this.applicant = docState.getApplicant();
		if (docState.getApplicant() != null) {
			this.applicantId = docState.getApplicant().getUserId();
			this.appId = docState.getApplicant().getAppId();
			this.deptId = docState.getApplicant().getDeptId();
		}
		this.user = docState.getNowUser();
		if (docState.getNowUser() != null) {
			this.userId = docState.getNowUser().getUserId();
		}
		this.beRepresented = beRepresented;
		if (beRepresented != null) {
			this.beRepresentedId = beRepresented.getUserId();
		}
		this.agent = agent;
		if (agent != null) {
			this.agentId = agent.getUserId();
		}
		this.byFlowAdminId = docState.getByFlowAdminId();
		this.subFlowDept = docState.getSubFlowDept();
		if (docState.getSubFlowDept() != null) {
			this.subFlowDeptId = docState.getSubFlowDept().getDeptId();
		}
		this.lastReviewDept = docState.getLastReviewDept();
		if (docState.getLastReviewDept() != null) {
			this.lastReviewDeptId = docState.getLastReviewDept().getDeptId();
		}
		this.stateCreateTime = docState.getCreateTime();
		this.stateUpdateTime = docState.getUpdateTime();
		this.taskType = taskType;
		this.fromTaskId = fromTaskId;
		this.toTaskId = toTaskId;
		this.projId = docState.getProjId();
		this.isProjRole = docState.getIsProjRole();
		this.isReviewDeptRole = docState.getIsReviewDeptRole();
		this.canBatchReview = docState.getCanBatchReview();
		this.memo = memo;
		this.createTime = new Date();
	}

	@Override
	public Long getId() {
		return id;
	}
	
	@Override
	public void setId(Long id) {
		throw new IllegalArgumentException("The id can't be changed.");
	}

	public DocState getDocState() {
		return docState;
	}

	public FormConf getFormConf() {
		return formConf;
	}

	public String getApplyNo() {
		return applyNo;
	}

	public Integer getSerialNo() {
		return serialNo;
	}
	
	public String getFlowId() {
		return flowId;
	}

	public String getFlowVersion() {
		return flowVersion;
	}
	
	public String getTaskType() {
		return taskType;
	}

	public String getFromTaskId() {
		return fromTaskId;
	}

	public String getToTaskId() {
		return toTaskId;
	}

	public String getFlowStatus() {
		return flowStatus;
	}
	
	public String getTaskRoleIds() {
		return taskRoleIds;
	}

	public String getTaskDeptId() {
		return taskDeptId;
	}
	
	public String getTaskUserIds() {
		return taskUserIds;
	}

	public String getTaskExt() {
		return taskExt;
	}
	
	public String getTaskName() {
		return taskName;
	}

	public String getTaskDesc() {
		return taskDesc;
	}

	public String getLinkName() {
		return linkName;
	}

	public String getLinkDesc() {
		return linkDesc;
	}
	
	public UserProf getApplicant() {
		return applicant;
	}

	public UserProf getUser() {
		return user;
	}
	
	public UserProf getBeRepresented() {
		return beRepresented;
	}
	
	public UserProf getAgent() {
		return agent;
	}

	public String getByFlowAdminId() {
		return byFlowAdminId;
	}
	
	public DeptProf getSubFlowDept() {
		return subFlowDept;
	}
	
	public DeptProf getLastReviewDept() {
		return lastReviewDept;
	}
	
	public String getProjId() {
		return projId;
	}
	
	public String getIsProjRole() {
		return isProjRole;
	}
	
	public String getIsReviewDeptRole() {
		return isReviewDeptRole;
	}

	public String getCanBatchReview() {
		return canBatchReview;
	}

	public String getMemo() {
		return memo;
	}

	public Date getStateCreateTime() {
		return stateCreateTime;
	}

	public Date getStateUpdateTime() {
		return stateUpdateTime;
	}

	public Date getCreateTime() {
		return createTime;
	}
	public String getAppId() {
		return appId;
	}

//	public void setAppId(String appId) {
//		this.appId = appId;
//	}
	
	
	public String getDeptId() {
		return deptId;
	}
//	public void setDeptId(String deptId) {
//		this.deptId = deptId;
//	}

	public String getAgentId() {
		return agentId;
	}

//	public void setAgentId(String agentId) {
//		this.agentId = agentId;
//	}
	
	
}
