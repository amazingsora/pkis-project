package com.tradevan.mapper.pkis.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author penny
 * @since 2020-03-04
 */
@TableName("PROF_MAIL")
public class ProfMail extends Model<ProfMail> {

    private static final long serialVersionUID=1L;

    /**
     * 電子郵件信箱流水序號
     */
    @TableId("MAIL_ID")
    private String mailId;

    /**
     * GLN
     */
    @TableField("SUPPLIER_GLN")
    private String supplierGln;

    /**
     * 廠編
     */
    @TableField("SUPPLIER_CODE")
    private String supplierCode;

    /**
     * 家樂福部門
     */
    @TableField("DEPARTMENT")
    private String department;

    /**
     * 電子郵件信箱
     */
    @TableField("MAIL_ADDRESS")
    private String mailAddress;

    /**
     * 啟用狀態(預設為Y：啟用、N：停用)
     */
    @TableField("MAIL_STATUS")
    private String mailStatus;

    /**
     * 建立人員
     */
    @TableField("CREATE_USER")
    private String createUser;

    /**
     * 建立時間
     */
    @TableField("CREATE_TIME")
    private String createTime;

    /**
     * 異動人員
     */
    @TableField("UPDATE_USER")
    private String updateUser;

    /**
     * 異動時間
     */
    @TableField("UPDATE_TIME")
    private String updateTime;

    /**
     * 系統備註說明
     */
    @TableField("SYSTEM_MEMO")
    private String systemMemo;

    /**
     * 電子信箱人員名稱
     */
    @TableField("MAIL_NAME")
    private String mailName;


    public String getMailId() {
        return mailId;
    }

    public ProfMail setMailId(String mailId) {
        this.mailId = mailId;
        return this;
    }

    public String getSupplierGln() {
        return supplierGln;
    }

    public ProfMail setSupplierGln(String supplierGln) {
        this.supplierGln = supplierGln;
        return this;
    }

    public String getSupplierCode() {
        return supplierCode;
    }

    public ProfMail setSupplierCode(String supplierCode) {
        this.supplierCode = supplierCode;
        return this;
    }

    public String getDepartment() {
        return department;
    }

    public ProfMail setDepartment(String department) {
        this.department = department;
        return this;
    }

    public String getMailAddress() {
        return mailAddress;
    }

    public ProfMail setMailAddress(String mailAddress) {
        this.mailAddress = mailAddress;
        return this;
    }

    public String getMailStatus() {
        return mailStatus;
    }

    public ProfMail setMailStatus(String mailStatus) {
        this.mailStatus = mailStatus;
        return this;
    }

    public String getCreateUser() {
        return createUser;
    }

    public ProfMail setCreateUser(String createUser) {
        this.createUser = createUser;
        return this;
    }

    public String getCreateTime() {
        return createTime;
    }

    public ProfMail setCreateTime(String createTime) {
        this.createTime = createTime;
        return this;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public ProfMail setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
        return this;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public ProfMail setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
        return this;
    }

    public String getSystemMemo() {
        return systemMemo;
    }

    public ProfMail setSystemMemo(String systemMemo) {
        this.systemMemo = systemMemo;
        return this;
    }

    public String getMailName() {
        return mailName;
    }

    public ProfMail setMailName(String mailName) {
        this.mailName = mailName;
        return this;
    }

    @Override
    protected Serializable pkVal() {
        return this.mailId;
    }

    @Override
    public String toString() {
        return "ProfMail{" +
        "mailId=" + mailId +
        ", supplierGln=" + supplierGln +
        ", supplierCode=" + supplierCode +
        ", department=" + department +
        ", mailAddress=" + mailAddress +
        ", mailStatus=" + mailStatus +
        ", createUser=" + createUser +
        ", createTime=" + createTime +
        ", updateUser=" + updateUser +
        ", updateTime=" + updateTime +
        ", systemMemo=" + systemMemo +
        ", mailName=" + mailName +
        "}";
    }
}
