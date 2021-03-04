package com.tradevan.handyflow.bean;

import java.io.Serializable;

import com.tradevan.apcommon.util.BeanUtil;
import com.tradevan.handyflow.model.form.DocToDo;

/**
 * Title: DocToDoBean<br>
 * Description: <br>
 * Company: Tradevan Co.<br>
 * 
 * @author Neil Chen
 * @version 1.1
 */
public class DocToDoBean implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private String formId;
	private String toDoNo;
	private Integer serialNo;
	private String userId;
	private String sysId;
	private String projId;
	private String subject;
	private String status;
	private Long mainFormId;
	private String otherInfo;
	private Long otherSerNo;
	
	public DocToDoBean() {
	}
	
	public DocToDoBean(DocToDo docToDo) {
		BeanUtil.copyProperties(docToDo, this);
		setFormId(docToDo.getFormConf().getFormId());
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getFormId() {
		return formId;
	}
	public void setFormId(String formId) {
		this.formId = formId;
	}
	public String getToDoNo() {
		return toDoNo;
	}
	public void setToDoNo(String toDoNo) {
		this.toDoNo = toDoNo;
	}
	public Integer getSerialNo() {
		return serialNo;
	}
	public void setSerialNo(Integer serialNo) {
		this.serialNo = serialNo;
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
}
