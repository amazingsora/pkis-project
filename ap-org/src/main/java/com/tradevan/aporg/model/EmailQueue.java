package com.tradevan.aporg.model;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.beans.BeanUtils;

import com.tradevan.apcommon.bean.CreateUserDto;
import com.tradevan.apcommon.persistence.GenericEntity;
import com.tradevan.aporg.enums.MailCategory;

/**
 * EmailQueue generated by hbm2java
 */
@Entity
public class EmailQueue extends GenericEntity<Long> {
	private static final long serialVersionUID = 1L;

	private Long id;
	private String sysId;
	private MailCategory category;
	private String mailTo;
	private String mailToCc;
	private String mailToBcc;
	private String subject;
	private String content;
	private String attachment;
	private String priority;
	private String status;
	private String failReason;
	private Short retryCnt;
	private String createUserId;
	private String updateUserId;
	private Date createTime;
	private Date updateTime;
	private String sysMemo;

	public EmailQueue() {
	}

	private EmailQueue(String sysId, MailCategory category, String mailTo, String subject, String content) {
		this.sysId = sysId;
		this.category = category;
		this.mailTo = mailTo;
		this.subject = subject;
		this.content = content;
		this.priority = "M";
		this.status = "W";
	}
	
	public EmailQueue(String sysId, MailCategory category, String mailTo, String subject, String content, String userId) {
		this(sysId, category, mailTo, subject, content);
		this.createUserId = userId;
		this.updateUserId = userId;
		Date now = new Date();
		this.createTime = now;
		this.updateTime = now;
	}
	
	public EmailQueue(String sysId, MailCategory category, String mailTo, String subject, String content, CreateUserDto createUserDto) {
		this(sysId, category, mailTo, subject, content);
		BeanUtils.copyProperties(createUserDto, this);
	}

	@Id
	@GeneratedValue
	@Column(name = "serNo", unique = true, nullable = false)
	@Override
	public Long getId() {
		return id;
	}
	
	@Override
	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "sysId", nullable = false, length = 30)
	public String getSysId() {
		return sysId;
	}

	public void setSysId(String sysId) {
		this.sysId = sysId;
	}

	@Enumerated(EnumType.STRING)
	@Column(name = "category", nullable = false, length = 30)
	public MailCategory getCategory() {
		return this.category;
	}

	public void setCategory(MailCategory category) {
		this.category = category;
	}

	@Column(name = "mailTo", nullable = false, length = 1000)
	public String getMailTo() {
		return this.mailTo;
	}

	public void setMailTo(String mailTo) {
		this.mailTo = mailTo;
	}

	@Column(name = "mailToCc", length = 1000)
	public String getMailToCc() {
		return this.mailToCc;
	}

	public void setMailToCc(String mailToCc) {
		this.mailToCc = mailToCc;
	}

	@Column(name = "mailToBcc", length = 1000)
	public String getMailToBcc() {
		return this.mailToBcc;
	}

	public void setMailToBcc(String mailToBcc) {
		this.mailToBcc = mailToBcc;
	}

	@Column(name = "subject", nullable = false)
	public String getSubject() {
		return this.subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	@Column(name = "content")
	public String getContent() {
		return this.content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Column(name = "attachment", length = 200)
	public String getAttachment() {
		return this.attachment;
	}

	public void setAttachment(String attachment) {
		this.attachment = attachment;
	}

	@Column(name = "priority", nullable = false, length = 5)
	public String getPriority() {
		return this.priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	@Column(name = "status", nullable = false, length = 5)
	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Column(name = "failReason", length = 200)
	public String getFailReason() {
		return this.failReason;
	}

	public void setFailReason(String failReason) {
		this.failReason = failReason;
	}

	@Column(name = "retryCnt")
	public Short getRetryCnt() {
		return this.retryCnt;
	}

	public void setRetryCnt(Short retryCnt) {
		this.retryCnt = retryCnt;
	}

	@Column(name = "createUserId", length = 30)
	public String getCreateUserId() {
		return this.createUserId;
	}

	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}

	@Column(name = "updateUserId", length = 30)
	public String getUpdateUserId() {
		return this.updateUserId;
	}

	public void setUpdateUserId(String updateUserId) {
		this.updateUserId = updateUserId;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "createTime", nullable = false, length = 23)
	public Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "updateTime", nullable = false, length = 23)
	public Date getUpdateTime() {
		return this.updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	@Column(name = "sysMemo", length = 50)
	public String getSysMemo() {
		return this.sysMemo;
	}

	public void setSysMemo(String sysMemo) {
		this.sysMemo = sysMemo;
	}

}