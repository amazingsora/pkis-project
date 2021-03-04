package com.tradevan.mapper.pkis.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author system
 * @since 2020-07-22
 */
@TableName("SUPPLIERMASTER")
public class Suppliermaster extends Model<Suppliermaster> {

    private static final long serialVersionUID=1L;

    @TableField("IDENID")
    private String idenid;

    @TableField("SUPPLIERID")
    private String supplierid;

    @TableField("SUPPLIERCODE")
    private String suppliercode;

    @TableField("SUPPLIERGUI")
    private String suppliergui;

    @TableField("SUPPLIERTYPE")
    private String suppliertype;

    @TableField("SUPPLIERCNAME")
    private String suppliercname;

    @TableField("SUPPLIERENAME")
    private String supplierename;

    @TableField("SUPPLIERCADDR")
    private String suppliercaddr;

    @TableField("SUPPLIEREADDR")
    private String suppliereaddr;

    @TableField("PICUSER")
    private String picuser;

    @TableField("SUPPLIERETEL")
    private String supplieretel;

    @TableField("CONTACRUSER")
    private String contacruser;

    @TableField("SUPPLIEREMAIL")
    private String supplieremail;

    @TableField("CREATEUSER")
    private String createuser;

    @TableField("CREATEDATE")
    private Date createdate;

    @TableField("UPDATEUSER")
    private String updateuser;

    @TableField("UPDATEDATE")
    private Date updatedate;

    @TableField("ENABLED")
    private String enabled;

    @TableField("DEPTNO")
    private String deptno;


    public String getIdenid() {
        return idenid;
    }

    public Suppliermaster setIdenid(String idenid) {
        this.idenid = idenid;
        return this;
    }

    public String getSupplierid() {
        return supplierid;
    }

    public Suppliermaster setSupplierid(String supplierid) {
        this.supplierid = supplierid;
        return this;
    }

    public String getSuppliercode() {
        return suppliercode;
    }

    public Suppliermaster setSuppliercode(String suppliercode) {
        this.suppliercode = suppliercode;
        return this;
    }

    public String getSuppliergui() {
        return suppliergui;
    }

    public Suppliermaster setSuppliergui(String suppliergui) {
        this.suppliergui = suppliergui;
        return this;
    }

    public String getSuppliertype() {
        return suppliertype;
    }

    public Suppliermaster setSuppliertype(String suppliertype) {
        this.suppliertype = suppliertype;
        return this;
    }

    public String getSuppliercname() {
        return suppliercname;
    }

    public Suppliermaster setSuppliercname(String suppliercname) {
        this.suppliercname = suppliercname;
        return this;
    }

    public String getSupplierename() {
        return supplierename;
    }

    public Suppliermaster setSupplierename(String supplierename) {
        this.supplierename = supplierename;
        return this;
    }

    public String getSuppliercaddr() {
        return suppliercaddr;
    }

    public Suppliermaster setSuppliercaddr(String suppliercaddr) {
        this.suppliercaddr = suppliercaddr;
        return this;
    }

    public String getSuppliereaddr() {
        return suppliereaddr;
    }

    public Suppliermaster setSuppliereaddr(String suppliereaddr) {
        this.suppliereaddr = suppliereaddr;
        return this;
    }

    public String getPicuser() {
        return picuser;
    }

    public Suppliermaster setPicuser(String picuser) {
        this.picuser = picuser;
        return this;
    }

    public String getSupplieretel() {
        return supplieretel;
    }

    public Suppliermaster setSupplieretel(String supplieretel) {
        this.supplieretel = supplieretel;
        return this;
    }

    public String getContacruser() {
        return contacruser;
    }

    public Suppliermaster setContacruser(String contacruser) {
        this.contacruser = contacruser;
        return this;
    }

    public String getSupplieremail() {
        return supplieremail;
    }

    public Suppliermaster setSupplieremail(String supplieremail) {
        this.supplieremail = supplieremail;
        return this;
    }

    public String getCreateuser() {
        return createuser;
    }

    public Suppliermaster setCreateuser(String createuser) {
        this.createuser = createuser;
        return this;
    }

    public Date getCreatedate() {
        return createdate;
    }

    public Suppliermaster setCreatedate(Date createdate) {
        this.createdate = createdate;
        return this;
    }

    public String getUpdateuser() {
        return updateuser;
    }

    public Suppliermaster setUpdateuser(String updateuser) {
        this.updateuser = updateuser;
        return this;
    }

    public Date getUpdatedate() {
        return updatedate;
    }

    public Suppliermaster setUpdatedate(Date updatedate) {
        this.updatedate = updatedate;
        return this;
    }

    public String getEnabled() {
        return enabled;
    }

    public Suppliermaster setEnabled(String enabled) {
        this.enabled = enabled;
        return this;
    }

    public String getDeptno() {
        return deptno;
    }

    public Suppliermaster setDeptno(String deptno) {
        this.deptno = deptno;
        return this;
    }

    @Override
    protected Serializable pkVal() {
        return null;
    }

    @Override
    public String toString() {
        return "Suppliermaster{" +
        "idenid=" + idenid +
        ", supplierid=" + supplierid +
        ", suppliercode=" + suppliercode +
        ", suppliergui=" + suppliergui +
        ", suppliertype=" + suppliertype +
        ", suppliercname=" + suppliercname +
        ", supplierename=" + supplierename +
        ", suppliercaddr=" + suppliercaddr +
        ", suppliereaddr=" + suppliereaddr +
        ", picuser=" + picuser +
        ", supplieretel=" + supplieretel +
        ", contacruser=" + contacruser +
        ", supplieremail=" + supplieremail +
        ", createuser=" + createuser +
        ", createdate=" + createdate +
        ", updateuser=" + updateuser +
        ", updatedate=" + updatedate +
        ", enabled=" + enabled +
        ", deptno=" + deptno +
        "}";
    }
}
