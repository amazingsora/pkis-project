package com.tradevan.handyflow.model.form;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.tradevan.apcommon.persistence.GenericEntity;

/**
 * Title: DocNoticeLog<br>
 * Description: <br>
 * Company: Tradevan Co.<br>
 * 
 * @author Neil Chen
 * @version 1.0.1
 */
@Entity
public class DocNoticeLog extends GenericEntity<Long> {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue
	@Column(name = "serNo", unique = true, nullable = false)
	private Long id;
	
	@Column(nullable = false, length = 30)
	private String userId;
	
	@Column(nullable = false, length = 30)
	private String sysId;
	
	@Column(nullable = false, length = 30)
	private String applyNo;
	
	@Column(nullable = false)
	private Integer serialNo;
	
	@Column(nullable = false, length = 1)
	private String status;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "createTime", nullable = false)
	private Date createTime;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "updateTime", nullable = false)
	private Date updateTime;
	
	@Column(name = "sysMemo", length = 50)
	private String sysMemo;

	public DocNoticeLog() {
	}

	public DocNoticeLog(String userId, String sysId, String applyNo, Integer serialNo) {
		super();
		this.userId = userId;
		this.sysId = sysId;
		this.applyNo = applyNo;
		this.serialNo = serialNo;
		this.status = "N";
		Date now = new Date();
		this.createTime = now;
		this.updateTime = now;
	}

	@Override
	public Long getId() {
		return id;
	}
	
	@Override
	public void setId(Long id) {
		throw new IllegalArgumentException("The id can't be changed.");
	}

	public String getUserId() {
		return this.userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getSysId() {
		return this.sysId;
	}

	public void setSysId(String sysId) {
		this.sysId = sysId;
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

	public void setSerialNo(Integer serialNo) {
		this.serialNo = serialNo;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return this.updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getSysMemo() {
		return this.sysMemo;
	}

	public void setSysMemo(String sysMemo) {
		this.sysMemo = sysMemo;
	}

}
