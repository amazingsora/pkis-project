package com.tradevan.handyflow.bean;

import java.io.Serializable;

import com.tradevan.apcommon.bean.CreateUserDto;

/**
 * Title: FlowBean<br>
 * Description: <br>
 * Company: Tradevan Co.<br>
 * 
 * @author Neil Chen
 * @version 1.5
 */
public class FlowBean implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String formId;
	private String applyNo;
	private String flowId;
	private String flowVersion;
	private String userId;
	private String beRepresentedId;
	private String agentId;
	private String sysId;
	private String projId;
	private String subject;
	private Long mainFormId;
	private String mainFormTable;
	
	private String applyNoPrefix;
	private String applyNoDateFmt;
	private int applyNoSerialLen;
	
	private String canBatchReview;
	
	public String getFormId() {
		return formId;
	}
	public void setFormId(String formId) {
		this.formId = formId;
	}
	public String getApplyNo() {
		return applyNo;
	}
	public void setApplyNo(String applyNo) {
		this.applyNo = applyNo;
	}
	public String getFlowId() {
		return flowId;
	}
	public void setFlowId(String flowId) {
		this.flowId = flowId;
	}
	public String getFlowVersion() {
		return flowVersion;
	}
	public void setFlowVersion(String version) {
		this.flowVersion = version;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getBeRepresentedId() {
		return beRepresentedId;
	}
	public void setBeRepresentedId(String beRepresentedId) {
		this.beRepresentedId = beRepresentedId;
	}
	public String getAgentId() {
		return agentId;
	}
	public void setAgentId(String agentId) {
		this.agentId = agentId;
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
	public Long getMainFormId() {
		return mainFormId;
	}
	public void setMainFormId(Long mainFormId) {
		this.mainFormId = mainFormId;
	}
	public String getMainFormTable() {
		return mainFormTable;
	}
	public void setMainFormTable(String mainFormTable) {
		this.mainFormTable = mainFormTable;
	}
	public String getApplyNoPrefix() {
		return applyNoPrefix;
	}
	public void setApplyNoPrefix(String applyNoPrefix) {
		this.applyNoPrefix = applyNoPrefix;
	}
	public String getApplyNoDateFmt() {
		return applyNoDateFmt;
	}
	public void setApplyNoDateFmt(String applyNoDateFmt) {
		this.applyNoDateFmt = applyNoDateFmt;
	}
	public int getApplyNoSerialLen() {
		return applyNoSerialLen;
	}
	public void setApplyNoSerialLen(int applyNoSerialLen) {
		this.applyNoSerialLen = applyNoSerialLen;
	}
	public String getCanBatchReview() {
		return canBatchReview;
	}
	public void setCanBatchReview(String canBatchReview) {
		this.canBatchReview = canBatchReview;
	}
	
	public FlowBean nowUser(CreateUserDto createUserDto) {
		this.userId = createUserDto.getCreateUserId();
		this.beRepresentedId = createUserDto.getBeRepresentedId();
		this.agentId = createUserDto.getCreateAgentId();
		return this;
	}
}
