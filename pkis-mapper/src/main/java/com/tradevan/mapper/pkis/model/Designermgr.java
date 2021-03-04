package com.tradevan.mapper.pkis.model;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;

@TableName("DESIGNERMGR")
public class Designermgr extends Model<Designermgr> {

	private static final long serialVersionUID = 1L;
	
	@TableId("DOCVER")
	private String docver;
	
	@TableField("UPDTIME")
	private Date updtime;
	
	@TableField("EFFTIME")
	private Date efftime;
	
	@TableField("PRDTIME")
	private Date prdtime;
	
	@TableField("DROPTIME")
	private Date droptime;
	
	@TableField("DOCSTATUS")
	private String docstatus;
	
	@TableField("MODULE")
	private String module;
	
	@TableField("KIND")
	private String kind;
	
	@TableField("SPEC")
	private String spec;
	
	@TableField("EXTEND")
	private String extend;
	
	@TableField("VERSION")
	private String version;
	
	@TableField("DOCCODE")
	private String doccode;
	
	@TableField("DISP")
	private String disp;
	
	@TableField("SHORTDISP")
	private String shortdisp;
	
	@TableField("FULLPATH")
	private String fullpath;
	
	@TableField("ERRMESSAGE")
	private String errmessage;
	
	@TableField("JSON")
	private String json;
	
	@TableField("YEAR")
	private String year;
	
	@TableField("CREATEUSER")
	private String createuser;

	public String getDocver() {
		return docver;
	}

	public void setDocver(String docver) {
		this.docver = docver;
	}

	public Date getUpdtime() {
		return updtime;
	}

	public void setUpdtime(Date updtime) {
		this.updtime = updtime;
	}

	public Date getEfftime() {
		return efftime;
	}

	public void setEfftime(Date efftime) {
		this.efftime = efftime;
	}

	public Date getPrdtime() {
		return prdtime;
	}

	public void setPrdtime(Date prdtime) {
		this.prdtime = prdtime;
	}

	public Date getDroptime() {
		return droptime;
	}

	public void setDroptime(Date droptime) {
		this.droptime = droptime;
	}

	public String getDocstatus() {
		return docstatus;
	}

	public void setDocstatus(String docstatus) {
		this.docstatus = docstatus;
	}

	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}
	
	public String getKind() {
		return kind;
	}

	public void setKind(String kind) {
		this.kind = kind;
	}

	public String getSpec() {
		return spec;
	}

	public void setSpec(String spec) {
		this.spec = spec;
	}

	public String getExtend() {
		return extend;
	}

	public void setExtend(String extend) {
		this.extend = extend;
	}
	
	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getDoccode() {
		return doccode;
	}

	public void setDoccode(String doccode) {
		this.doccode = doccode;
	}

	public String getDisp() {
		return disp;
	}

	public void setDisp(String disp) {
		this.disp = disp;
	}

	public String getShortdisp() {
		return shortdisp;
	}

	public void setShortdisp(String shortdisp) {
		this.shortdisp = shortdisp;
	}

	public String getErrmessage() {
		return errmessage;
	}

	public void setErrmessage(String errmessage) {
		this.errmessage = errmessage;
	}
	
	public String getFullpath() {
		return fullpath;
	}

	public void setFullpath(String fullpath) {
		this.fullpath = fullpath;
	}

	public String getJson() {
		return json;
	}

	public void setJson(String json) {
		this.json = json;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getCreateuser() {
		return createuser;
	}

	public void setCreateuser(String createuser) {
		this.createuser = createuser;
	}
}
