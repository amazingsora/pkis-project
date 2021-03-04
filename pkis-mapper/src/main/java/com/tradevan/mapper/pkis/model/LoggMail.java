package com.tradevan.mapper.pkis.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

/**
 * <p>
 * 系統信件寄送記錄檔
 * </p>
 *
 * @author penny
 * @since 2020-03-04
 */
@TableName("LOGG_MAIL")
public class LoggMail extends Model<LoggMail> {

    private static final long serialVersionUID=1L;

    /**
     * 流水序號
     */
    @TableId("MAIL_ID")
    private String mailId;

    /**
     * 來源的代碼
     */
    @TableField("SOURCE_ID")
    private String sourceId;

    /**
     * 郵件來源類型
     */
    @TableField("SOURCE_TYPE")
    private String sourceType;

    /**
     * 郵件收件者
     */
    @TableField("MAIL_TO")
    private String mailTo;

    /**
     * 郵件副本人員
     */
    @TableField("MAIL_CC")
    private String mailCc;

    /**
     * 密件副本人員
     */
    @TableField("MAIL_BCC")
    private String mailBcc;

    /**
     * 郵件主旨
     */
    @TableField("MAIL_SUBJECT")
    private String mailSubject;

    @TableField("MAIL_BODY")
    private String mailBody;

    /**
     * 郵件附件
     */
    @TableField("MAIL_ATTACH")
    private String mailAttach;

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


    public String getMailId() {
        return mailId;
    }

    public LoggMail setMailId(String mailId) {
        this.mailId = mailId;
        return this;
    }

    public String getSourceId() {
        return sourceId;
    }

    public LoggMail setSourceId(String sourceId) {
        this.sourceId = sourceId;
        return this;
    }

    public String getSourceType() {
        return sourceType;
    }

    public LoggMail setSourceType(String sourceType) {
        this.sourceType = sourceType;
        return this;
    }

    public String getMailTo() {
        return mailTo;
    }

    public LoggMail setMailTo(String mailTo) {
        this.mailTo = mailTo;
        return this;
    }

    public String getMailCc() {
        return mailCc;
    }

    public LoggMail setMailCc(String mailCc) {
        this.mailCc = mailCc;
        return this;
    }

    public String getMailBcc() {
        return mailBcc;
    }

    public LoggMail setMailBcc(String mailBcc) {
        this.mailBcc = mailBcc;
        return this;
    }

    public String getMailSubject() {
        return mailSubject;
    }

    public LoggMail setMailSubject(String mailSubject) {
        this.mailSubject = mailSubject;
        return this;
    }

    public String getMailBody() {
        return mailBody;
    }

    public LoggMail setMailBody(String mailBody) {
        this.mailBody = mailBody;
        return this;
    }

    public String getMailAttach() {
        return mailAttach;
    }

    public LoggMail setMailAttach(String mailAttach) {
        this.mailAttach = mailAttach;
        return this;
    }

    public String getCreateUser() {
        return createUser;
    }

    public LoggMail setCreateUser(String createUser) {
        this.createUser = createUser;
        return this;
    }

    public String getCreateTime() {
        return createTime;
    }

    public LoggMail setCreateTime(String createTime) {
        this.createTime = createTime;
        return this;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public LoggMail setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
        return this;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public LoggMail setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
        return this;
    }

    public String getSystemMemo() {
        return systemMemo;
    }

    public LoggMail setSystemMemo(String systemMemo) {
        this.systemMemo = systemMemo;
        return this;
    }

    @Override
    protected Serializable pkVal() {
        return this.mailId;
    }

    @Override
    public String toString() {
        return "LoggMail{" +
        "mailId=" + mailId +
        ", sourceId=" + sourceId +
        ", sourceType=" + sourceType +
        ", mailTo=" + mailTo +
        ", mailCc=" + mailCc +
        ", mailBcc=" + mailBcc +
        ", mailSubject=" + mailSubject +
        ", mailBody=" + mailBody +
        ", mailAttach=" + mailAttach +
        ", createUser=" + createUser +
        ", createTime=" + createTime +
        ", updateUser=" + updateUser +
        ", updateTime=" + updateTime +
        ", systemMemo=" + systemMemo +
        "}";
    }
}
