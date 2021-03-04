package com.tradevan.handyflow.model.form;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.JoinColumnOrFormula;
import org.hibernate.annotations.JoinColumnsOrFormulas;
import org.hibernate.annotations.JoinFormula;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.tradevan.apcommon.persistence.GenericEntity;
import com.tradevan.aporg.model.DeptProf;
import com.tradevan.aporg.model.UserProf;

/**
 * Title: DocState<br>
 * Description: <br>
 * Company: Tradevan Co.<br>
 * 
 * @author Neil Chen
 * @version 1.9.4
 */
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"APP_ID","IDEN_ID","formId", "applyNo", "serialNo"}))
public class DocState extends GenericEntity<Long> {
	private static final long serialVersionUID = 1L;

	public static final String FLOW_STATUS_DRAFT = "DRAFT";
	public static final String FLOW_STATUS_PROCESS = "PROCESS";
	public static final String FLOW_STATUS_PARALLEL = "PARALLEL";
	public static final String FLOW_STATUS_PARALLEL_FINISH = "PARALLEL_FINISH";
	public static final String FLOW_STATUS_PARALLEL_RETURN = "PARALLEL_RETURN";
	public static final String FLOW_STATUS_SUB_SUSPEND = "SUB_SUSPEND";
	
	@Id
	@GeneratedValue
	@Column(name = "serNo", unique = true, nullable = false)
	private Long id;
	
	//@Id
	@Column(name="APP_ID", length = 30, nullable = false, insertable = false, updatable = false)
	private String appId;
	//@Id
	@Column(name="IDEN_ID", length = 30, nullable = false, insertable = false, updatable = false)
	private String deptId;
	
	@ManyToOne
	@JoinColumn(name="formId", referencedColumnName="formId", nullable = false)
	private FormConf formConf;
	
	//@Id
	@Column(name = "formId", length = 30, insertable = false, updatable = false)
	private String formId;
	
	//@Id
	@Column(nullable = false, length = 30)
	private String applyNo;
	
	//@Id
	@Column(nullable = false)
	private Integer serialNo;
	
	@Column(nullable = false, length = 50)
	private String flowId;
	
	@Column(nullable = false, length = 10)
	private String flowVersion;
	
	@Column(nullable = false, length = 50)
	private String nowTaskId;
	
	@Column(nullable = false, length = 20)
	private String flowStatus;
	
	@Column(length = 150)
	private String taskRoleIds;
	
	@Column(length = 20)
	private String taskDeptId;
	
	@Column(length = 300)
	private String taskUserIds;
	
	@Column(length = 100)
	private String taskExt;
	
	@Column(length = 200)
	private String taskName;
	
	@Column(length = 200)
	private String taskDesc;
	
	@Column(length = 100)
	private String linkName;
	
	@Column(length = 200)
	private String linkDesc;
	
	@ManyToOne
	@JoinColumn(name="upDocStateId")
	private DocState upDocState;
	
	@Column(name = "upDocStateId", insertable = false, updatable = false)
	private Long upDocStateId;
	
	@OneToMany(mappedBy = "upDocState",fetch=FetchType.LAZY)
	private List<DocState> subDocStates = new ArrayList<DocState>();
	
	@Column
	private Integer parallelPassCount;
	
	@Column(length = 50)
	private String previousTaskId;
	
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
	
	@Column(name = "applicantId", length = 30, insertable = false, updatable = false)
	private String applicantId;
	
	
	@Column(length = 30)
	private String applicantAgentId;
	
	@ManyToOne
	@NotFound(action = NotFoundAction.IGNORE)
	//@JoinColumn(name="nowUserId", referencedColumnName="USER_ID")
	@JoinColumnsOrFormulas({
		@JoinColumnOrFormula(column = @JoinColumn(name="nowUserId", referencedColumnName="USER_ID")),
		@JoinColumnOrFormula(formula = @JoinFormula(referencedColumnName = "APP_ID", value = "APP_ID")),
		@JoinColumnOrFormula(formula = @JoinFormula(referencedColumnName = "IDEN_ID", value = "IDEN_ID"))
	})
	private UserProf nowUser;
	
	@Column(name = "nowUserId", length = 30, insertable = false, updatable = false)
	private String nowUserId;
	
	@Column(length = 30)
	private String previousUserId;
	
	@Column(length = 30)
	private String flowAdminId;
	
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
	
	@Column(name = "subFlowDeptId", length = 30, insertable = false, updatable = false)
	private String subFlowDeptId;
	
	@ManyToOne
	@NotFound(action = NotFoundAction.IGNORE)
	//@JoinColumn(name="lastReviewDeptId", referencedColumnName="IDEN_ID")
	@JoinColumnsOrFormulas({
		@JoinColumnOrFormula(column = @JoinColumn(name="lastReviewDeptId", referencedColumnName="IDEN_ID")),
		@JoinColumnOrFormula(formula = @JoinFormula(referencedColumnName = "APP_ID", value = "APP_ID")),
	})
	private DeptProf lastReviewDept;
	
	@Column(name = "lastReviewDeptId", length = 30, insertable = false, updatable = false)
	private String lastReviewDeptId;
	
	@Column(length = 30)
	private String sysId;
	
	@Column(length = 30)
	private String projId;
	
	@Column(length = 1)
	private String isProjRole;
	
	@Column(length = 1)
	private String isReviewDeptRole;
	
	@Column(length = 1)
	private String canBatchReview;
	
	@Column(length = 1000)
	private String subject;
	
	@Column(name = "mainFormSerNo")
	private Long mainFormId;
	
	@Column(length = 50)
	private String mainFormTable;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable = false)
	private Date createTime;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable = false)
	private Date updateTime;
	
	@OneToMany(mappedBy = "docState")
	private List<DocStateLog> docStateLogs = new ArrayList<DocStateLog>();
	
	@Transient
	private boolean isAssigned;
	
	@Transient
	private boolean isRtnToOrig;
	
	@Transient
	private String noParallelSubFlows;
	
	@Transient
	private Integer assignParallelPassCount;
	
	public DocState() {
		super();
	}

	public DocState(FormConf formConf, String applyNo, Integer serialNo, String flowId, String flowVersion, String flowStatus, UserProf applicant, String agentId, 
			String projId, Long mainFormId) {
		super();
		this.formConf = formConf;
		this.applyNo = applyNo;
		this.serialNo = serialNo;
		this.flowId = flowId;
		this.flowVersion = flowVersion;
		this.flowStatus = flowStatus;
		this.applicant = applicant;
		this.nowUser = applicant;
		this.applicantAgentId = agentId;
		this.projId = projId;
		this.mainFormId =  mainFormId;
		Date now = new Date();
		this.createTime = now;
		this.updateTime = now;
		this.appId = applicant.getAppId();
		this.deptId = applicant.getDeptId();
		this.nowUserId= applicant.getUserId();
		this.subFlowDeptId = applicant.getDeptId();
		this.lastReviewDeptId = applicant.getDeptId();
		this.applicantId= applicant.getUserId();
	}
	
	public DocState(DocState docState, Integer serialNo) {
		super();
		this.formConf = docState.getFormConf();
		this.applyNo = docState.getApplyNo();
		this.serialNo = serialNo;
		this.flowId = docState.getFlowId();
		this.flowVersion = docState.getFlowVersion();
		this.upDocState = docState;
		this.applicant = docState.getApplicant();
		this.appId =  docState.getApplicant().getAppId();
		this.deptId =  docState.getApplicant().getDeptId();
		this.applicantId =  docState.getApplicant().getUserId();
		this.applicantAgentId = docState.getApplicantAgentId();
		this.sysId = docState.getSysId();
		this.projId = docState.getProjId();
		this.isProjRole = docState.getIsProjRole();
		this.subject = docState.getSubject();
		this.mainFormId = docState.getMainFormId();
		Date now = new Date();
		this.createTime = now;
		this.updateTime = now;
	}

	@Override
	public Long getId() {
		return this.id;
	}
	
	@Override
	public void setId(Long id) {
		throw new IllegalArgumentException("The id can't be changed.");
	}

	public FormConf getFormConf() {
		return formConf;
	}

	public void setFormConf(FormConf formConf) {
		this.formConf = formConf;
	}
	
	public String getFormId() {
		return formId;
	}

	public String getApplyNo() {
		return applyNo;
	}
	
	public void setApplyNo(String applyNo) {
		this.applyNo = applyNo;
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

	public String getNowTaskId() {
		return nowTaskId;
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

	public DocState getUpDocState() {
		return upDocState;
	}

	public Long getUpDocStateId() {
		return upDocStateId;
	}

	public List<DocState> getSubDocStates() {
		return subDocStates;
	}
	
	public Integer getParallelPassCount() {
		return parallelPassCount;
	}
	
	public String getPreviousTaskId() {
		return previousTaskId;
	}

	public UserProf getApplicant() {
		return applicant;
	}
	
	public String getApplicantAgentId() {
		return applicantAgentId;
	}

	public UserProf getNowUser() {
		return nowUser;
	}
	
	public String getNowUserId() {
		return nowUserId;
	}

	public String getPreviousUserId() {
		return previousUserId;
	}
	
	public String getFlowAdminId() {
		return flowAdminId;
	}
	
	public String getByFlowAdminId() {
		return byFlowAdminId;
	}

	public DeptProf getSubFlowDept() {
		return subFlowDept;
	}
	
	public String getSubFlowDeptId() {
		return subFlowDeptId;
	}

	public DeptProf getLastReviewDept() {
		return lastReviewDept;
	}
	
	public String getLastReviewDeptId() {
		return lastReviewDeptId;
	}

	public String getSysId() {
		return sysId;
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

	public String getSubject() {
		return subject;
	}
	
	public Long getMainFormId() {
		return mainFormId;
	}

	public String getMainFormTable() {
		return mainFormTable;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public List<DocStateLog> getDocStateLogs() {
		return docStateLogs;
	}
	
	public boolean isAssigned() {
		return isAssigned;
	}

	public boolean isRtnToOrig() {
		return isRtnToOrig;
	}
	
	public String getNoParallelSubFlows() {
		return noParallelSubFlows;
	}

	public Integer getAssignParallelPassCount() {
		return assignParallelPassCount;
	}

	public void setFlowVersion(String flowVersion) {
		this.flowVersion = flowVersion;
	}
	
	public void setNowTaskId(String nowTaskId) {
		setPreviousTaskId(this.nowTaskId);
		this.nowTaskId = nowTaskId;
	}
	
	public void setFlowStatus(String flowStatus) {
		this.flowStatus = flowStatus;
	}
	
	public void setTaskRoleIds(String roleIds) {
		this.taskRoleIds = roleIds;
	}
	
	public void setTaskDeptId(String deptId) {
		this.taskDeptId = deptId;
	}
	
	public void setTaskUserIds(String taskUserIds) {
		this.taskUserIds = taskUserIds;
	}
	
	public void setTaskExt(String taskExt) {
		this.taskExt = taskExt;
	}
	
	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}
	
	public void setTaskDesc(String taskDesc) {
		this.taskDesc = taskDesc;
	}
	
	public void setLinkName(String linkName) {
		this.linkName = linkName;
	}
	
	public void setLinkDesc(String linkDesc) {
		this.linkDesc = linkDesc;
	}
	
	public void setUpDocState(DocState upDocState) {
		this.upDocState = upDocState;
	}
	
	public void setSubDocStates(List<DocState> subDocStates) {
		this.subDocStates = subDocStates;
	}
	
	public void setParallelPassCount(Integer parallelPassCount) {
		this.parallelPassCount = parallelPassCount;
	}
	
	public void setPreviousTaskId(String previousTaskId) {
		this.previousTaskId = previousTaskId;
	}
	
	public void setNowUser(UserProf nowUser) {
		if (this.nowUser != null && !this.nowUser.equals(nowUser)) {
			setPreviousUserId(this.nowUser.getUserId());
		}
		else if (nowUser == null) {
			this.nowUserId = null;
		}
		this.nowUser = nowUser;
	}
	
	public void setPreviousUserId(String previousUserId) {
		this.previousUserId = previousUserId;
	}
	
	public void setFlowAdminId(String flowAdminId) {
		this.flowAdminId = flowAdminId;
	}
	
	public void setByFlowAdminId(String byFlowAdminId) {
		this.byFlowAdminId = byFlowAdminId;
	}
	
	public void setSubFlowDept(DeptProf subFlowDept) {
		this.subFlowDept = subFlowDept;
	}
	
	public void setLastReviewDept(DeptProf lastReviewDept) {
		this.lastReviewDept = lastReviewDept;
	}
	
	public void setSysId(String sysId) {
		this.sysId = sysId;
	}
	
	public void setProjId(String projId) {
		this.projId = projId;
	}
	
	public void setIsProjRole(String isProjRole) {
		this.isProjRole = isProjRole;
	}
	
	public void setIsReviewDeptRole(String isReviewDeptRole) {
		this.isReviewDeptRole = isReviewDeptRole;
	}
	
	public void setCanBatchReview(String canBatchReview) {
		this.canBatchReview = canBatchReview;
	}
	
	public void setSubject(String subject) {
		this.subject = subject;
	}

	public void setMainFormId(Long mainFormId) {
		this.mainFormId = mainFormId;
	}

	public void setMainFormTable(String mainFormTable) {
		this.mainFormTable = mainFormTable;
	}
	
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	
	public void setAssigned(boolean isAssigned) {
		if (! isAssigned) {
			this.nowUserId = null;
		}
		this.isAssigned = isAssigned;
	}
	
	public void setRtnToOrig(boolean isRtnToOrig) {
		this.isRtnToOrig = isRtnToOrig;
	}
	
	public void setNoParallelSubFlows(String noParallelSubFlows) {
		this.noParallelSubFlows = noParallelSubFlows;
	}
	
	public void setAssignParallelPassCount(Integer assignParallelPassCount) {
		this.assignParallelPassCount = assignParallelPassCount;
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
	
	
	
	public String getApplicantId() {
		return applicantId;
	}

	public void setApplicantId(String applicantId) {
		this.applicantId = applicantId;
	}

	public void setApplicant(UserProf applicant) {
		this.applicant = applicant;
	}

//	public void setNowUserId(String nowUserId) {
//		this.nowUserId = nowUserId;
//	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.appId.hashCode();
		hash = hash * prime + this.deptId.hashCode();
		hash = hash * prime + this.formId.hashCode();
		hash = hash * prime + this.applyNo.hashCode();
		hash = hash * prime + this.serialNo.hashCode();
		// {"formId", "applyNo", "serialNo"
		return hash;
	}
	
}
