package com.tradevan.handyflow.bean;

import java.io.Serializable;
import java.util.Date;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import com.tradevan.apcommon.bean.CreateUserDto;
import com.tradevan.apcommon.bean.UpdateUserDto;
import com.tradevan.apcommon.util.DateUtil;
import com.tradevan.handyflow.model.form.DocState;
import com.tradevan.handyflow.model.form.DocToDo;

/**
 * Title: DocStateBean<br>
 * Description: <br>
 * Company: Tradevan Co.<br>
 * 
 * @author Neil Chen
 * @version 1.8.7
 */
@Component
@Scope(value = "prototype", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class DocStateBean implements Serializable, Comparable<DocStateBean> {
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private String formId;
	private String formName;
	private String formUrl;
	private String applyNo;
	private Integer serialNo;
	private String flowId;
	private String flowVersion;
	private String nowTaskId;
	private String flowStatus;
	private String taskRoleIds;
	private String taskDeptId;
	private String taskUserIds;
	private String taskExt;
	private String taskName;
	private String taskDesc;
	private String linkName;
	private String linkDesc;
	private String previousTaskId;
	private String applicantId;
	private String applicantName;
	private String applicantInfo;
	private String nowUserId;
	private String previousUserId;
	private String beRepresentedId;
	private String agentId;
	private String subFlowDeptId;
	private String lastReviewDeptId;
	private String sysId;
	private String projId;
	private String isProjRole;
	private String isReviewDeptRole;
	private String canBatchReview;
	private String subject;
	private Long mainFormId;
	private String mainFormTable;
	private Date createTime;
	private Date updateTime;
	private String symbol = "";
	
	private long upDocStateId = 0;
	private Long docStateLogId;
	
	private String userId = null;
	private String taskId = null;
	private boolean isSuper = false;
	private boolean isRtnToOrig = true;
	private String noParallelSubFlows;
	private Integer assignParallelPassCount; 
	private String memo;
	private String newFormId;
	private String reviewDeptId;
	
	private String irbNumber;
	private String irbPiName;
	private String ibrProjName;
	
	public DocStateBean() {
		super();
	}

	public DocStateBean(DocState docState, String userId, String beRepresentedId, String agentId, boolean isSuper, Long docStateLogId) {
		super();
		this.id = docState.getId();
		this.formId = docState.getFormConf().getFormId();
		this.formName = docState.getFormConf().getName();
		this.formUrl = docState.getFormConf().getFormUrl();
		this.applyNo = docState.getApplyNo();
		this.serialNo = docState.getSerialNo();
		this.flowId = docState.getFlowId();
		this.flowVersion = docState.getFlowVersion();
		this.nowTaskId = docState.getNowTaskId();
		this.flowStatus = docState.getFlowStatus();
		this.taskRoleIds = docState.getTaskRoleIds();
		this.taskDeptId = docState.getTaskDeptId();
		this.taskUserIds = docState.getTaskUserIds();
		this.taskExt = docState.getTaskExt();
		this.taskName = docState.getTaskName();
		this.taskDesc = docState.getTaskDesc();
		this.linkName = docState.getLinkName();
		this.linkDesc = docState.getLinkDesc();
		this.previousTaskId = docState.getPreviousTaskId();
		this.applicantId = docState.getApplicant() != null ? docState.getApplicant().getUserId() : null;
		this.applicantName = docState.getApplicant() != null ? docState.getApplicant().getName() : null;
		this.applicantInfo = docState.getApplicant() != null ? 
				docState.getApplicant().getUserId() + "/" + docState.getApplicant().getName() +
					(docState.getApplicantAgentId() != null ? " (" + docState.getApplicantAgentId() + "_代)" : "")
				: null;
		//this.nowUserId = docState.getNowUser() != null ? docState.getNowUser().getUserId() : null;
		this.nowUserId = docState.getNowUser() != null ? docState.getNowUser().getUserId() : docState.getNowUserId();
		this.previousUserId = docState.getPreviousUserId();
		this.subFlowDeptId = docState.getSubFlowDept() != null ? docState.getSubFlowDept().getDeptId() : null;
		this.lastReviewDeptId = docState.getLastReviewDept() != null ? docState.getLastReviewDept().getDeptId() : null;
		this.sysId = docState.getSysId();
		this.projId = docState.getProjId();
		this.isProjRole = docState.getIsProjRole();
		this.isReviewDeptRole = docState.getIsReviewDeptRole();
		this.canBatchReview = docState.getCanBatchReview();
		this.subject = docState.getSubject();
		this.mainFormId = docState.getMainFormId();
		this.mainFormTable = docState.getMainFormTable();
		this.createTime = docState.getCreateTime();
		this.updateTime = docState.getUpdateTime();
		this.docStateLogId = docStateLogId;
		this.userId = userId;
		this.beRepresentedId = beRepresentedId;
		this.agentId = agentId;
		this.isSuper = isSuper;
		if (docState.getUpDocState() != null) {
			upDocStateId = docState.getUpDocState().getId();
		}
		if (docState.getByFlowAdminId() != null) {
			this.symbol = "(*)";
		}
	}

	public DocStateBean(DocToDo docToDo, String beRepresentedId, String agentId) {
		super();
		this.id = docToDo.getId();
		this.formId = docToDo.getFormConf().getFormId();
		this.formName = docToDo.getFormConf().getName();
		this.formUrl = docToDo.getFormConf().getFormUrl();
		this.applyNo = docToDo.getToDoNo();
		this.serialNo = docToDo.getSerialNo();
		this.nowUserId = docToDo.getUserId();
		this.sysId = docToDo.getSysId();
		this.projId = docToDo.getProjId();
		this.subject = docToDo.getSubject();
		this.beRepresentedId = beRepresentedId;
		this.agentId = agentId;
		this.createTime = docToDo.getCreateTime();
		this.updateTime = docToDo.getUpdateTime();
		this.mainFormId = docToDo.getMainFormId();
		this.applicantInfo = docToDo.getCreateUser() != null ?
				docToDo.getCreateUser().getUserId() + "/" + docToDo.getCreateUser().getName() +
				(docToDo.getCreateAgentId() != null ? " (" + docToDo.getCreateAgentId() + "_代)" : "")
			: null;
	}
	
	@Override
	public int compareTo(DocStateBean bean) {
		return bean.getApplyNo().compareTo(this.getApplyNo());
	}
	
	public Long getId() {
		return id;
	}

	public String getFormId() {
		return formId;
	}
	
	public String getFormName() {
		return formName;
	}
	
	public String getFormUrl() {
		return formUrl;
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

	public String getApplicantId() {
		return applicantId;
	}
	
	public String getApplicantName() {
		return applicantName;
	}
	
	public String getApplicantInfo() {
		return applicantInfo;
	}

	public String getNowUserId() {
		return nowUserId;
	}
	
	public String getPreviousUserId() {
		return previousUserId;
	}

	public String getBeRepresentedId() {
		return beRepresentedId;
	}

	public String getAgentId() {
		return agentId;
	}

	public String getSubFlowDeptId() {
		return subFlowDeptId;
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

	public String getMemo() {
		return memo;
	}
	
	public Date getCreateTime() {
		return createTime;
	}

	public String getCreateDateStr() {
		return DateUtil.formatDate(createTime, DateUtil.FMT_YYYY_MM_DD);
	}
	
	public Date getUpdateTime() {
		return updateTime;
	}
	
	public String getUpdateDateStr() {
		return DateUtil.formatDate(updateTime, DateUtil.FMT_YYYY_MM_DD);
	}
	
	public String getSymbol() {
		return symbol;
	}

	public long getUpDocStateId() {
		return upDocStateId;
	}

	public Long getDocStateLogId() {
		return docStateLogId;
	}

	public String getUserId() {
		return userId;
	}

	public String getTaskId() {
		return taskId;
	}

	public boolean isSuper() {
		return isSuper;
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

	public String getNewFormId() {
		return newFormId;
	}
	
	public String getReviewDeptId() {
		return reviewDeptId;
	}

	/**
	 * @deprecated - please change to nowUser(String userId, String taskId) instead
	 *               taskId: please provide to-do's nowTaskId for check
	 */
	public DocStateBean nowUser(String userId) {
		return nowUser(userId, taskId);
	}
	
	public DocStateBean nowUser(String userId, String taskId) {
		this.userId = userId;
		this.taskId = taskId;
		return this;
	}
	
	/**
	 * @deprecated - please change to nowUser(CreateUserDto createUserDto, String taskId) instead
	 *               taskId: please provide to-do's nowTaskId for check
	 */
	public DocStateBean nowUser(CreateUserDto createUserDto) {
		return nowUser(createUserDto, taskId);
	}
	
	public DocStateBean nowUser(CreateUserDto createUserDto, String taskId) {
		this.beRepresentedId = createUserDto.getBeRepresentedId();
		this.agentId = createUserDto.getCreateAgentId();
		return nowUser(createUserDto.getCreateUserId(), taskId);
	}
	
	/**
	 * @deprecated - please change to nowUser(UpdateUserDto updateUserDto, String taskId) instead
	 *               taskId: please provide to-do's nowTaskId for check
	 */
	public DocStateBean nowUser(UpdateUserDto updateUserDto) {
		return nowUser(updateUserDto, taskId);
	}
	
	public DocStateBean nowUser(UpdateUserDto updateUserDto, String taskId) {
		this.beRepresentedId = updateUserDto.getBeRepresentedId();
		this.agentId = updateUserDto.getUpdateAgentId();
		return nowUser(updateUserDto.getUpdateUserId(), taskId);
	}
	
	public DocStateBean superUser() {
		this.isSuper = true;
		return this;
	}
	
	public DocStateBean notRtnToOrig() {
		this.isRtnToOrig = false;
		return this;
	}
	
	public DocStateBean subject(String subject) {
		this.subject = subject;
		return this;
	}
	
	public DocStateBean comment(String comment) {
		this.memo = comment;
		return this;
	}
	
	public DocStateBean newFormId(String newFormId) {
		this.newFormId = newFormId;
		return this;
	}
	
	public DocStateBean reviewDeptId(String reviewDeptId) {
		this.reviewDeptId = reviewDeptId;
		return this;
	}
	
	public DocStateBean noParallelSubFlows(String noParallelSubFlows) {
		this.noParallelSubFlows = noParallelSubFlows;
		return this;
	}

	public DocStateBean assignParallelPassCount(Integer assignParallelPassCount) {
		this.assignParallelPassCount = assignParallelPassCount;
		return this;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj != null && obj instanceof DocStateBean) {
			DocStateBean bean = (DocStateBean) obj;
			if (formId == null || applyNo == null) {
				return (bean.formId == null || bean.applyNo == null);
			}
			else {
				return formId.equals(bean.formId) && applyNo.equals(bean.applyNo);
			}
		}
		return false;
    }

	public String getIrbPiName() {
		return irbPiName;
	}

	public void setIrbPiName(String irbPiName) {
		this.irbPiName = irbPiName;
	}

	public String getIbrProjName() {
		return ibrProjName;
	}

	public void setIbrProjName(String ibrProjName) {
		this.ibrProjName = ibrProjName;
	}

	public String getIrbNumber() {
		return irbNumber;
	}

	public void setIrbNumber(String irbNumber) {
		this.irbNumber = irbNumber;
	}

	public String getPreviousTaskId() {
		return previousTaskId;
	}

	public void setPreviousTaskId(String previousTaskId) {
		this.previousTaskId = previousTaskId;
	}
}
