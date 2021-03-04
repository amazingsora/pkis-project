package com.tradevan.handyflow.model.form;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.beans.BeanUtils;

import com.tradevan.apcommon.bean.CreateUserDto;
import com.tradevan.apcommon.persistence.GenericEntity;
import com.tradevan.handyflow.dto.FormConfDto;

/**
 * Title: FormConf<br>
 * Description: <br>
 * Company: Tradevan Co.<br>
 * 
 * @author Neil Chen
 * @version 1.1
 */
@Entity
public class FormConf extends GenericEntity<Long> {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	@Column(name = "serNo", unique = true, nullable = false)
	private Long id;
	
	@Column(nullable = false, length = 30)
	private String formId;
	
	@Column(name = "formName", nullable = false, length = 50)
	private String name;
	
	@Column(nullable = false, length = 200)
	private String formUrl;
	
	@Column(length = 30)
	private String sysId;
	
	@Column(length = 30)
	private String createUserId;
	
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
	
	public FormConf() {
		super();
	}

	public FormConf(String formId, String name, String formUrl, String sysId, String createUserId) {
		super();
		this.formId = formId;
		this.name = name;
		this.formUrl = formUrl;
		this.sysId = sysId;
		Date now = new Date();
		this.createUserId = createUserId;
		this.updateUserId = createUserId;
		this.createTime = now;
		this.updateTime = now;
		
	}

	public FormConf(FormConfDto formConfDto, CreateUserDto createUserDto) {
		BeanUtils.copyProperties(formConfDto, this, "id");
		BeanUtils.copyProperties(createUserDto, this);
	}
	
	@Override
	public Long getId() {
		return id;
	}
	
	@Override
	public void setId(Long id) {
		throw new IllegalArgumentException("The id can't be changed.");
	}
	
	public String getFormId() {
		return formId;
	}

	public String getName() {
		return name;
	}
	
	public String getFormUrl() {
		return formUrl;
	}

	public String getSysId() {
		return sysId;
	}
	
	public String getCreateUserId() {
		return createUserId;
	}

	public String getUpdateUserId() {
		return updateUserId;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public void setFormUrl(String formUrl) {
		this.formUrl = formUrl;
	}
	
	public void setSysId(String sysId) {
		this.sysId = sysId;
	}

	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}

	public void setUpdateUserId(String updateUserId) {
		this.updateUserId = updateUserId;
	}
	
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public void setFormId(String formId) {
		this.formId = formId;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
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
	
}
