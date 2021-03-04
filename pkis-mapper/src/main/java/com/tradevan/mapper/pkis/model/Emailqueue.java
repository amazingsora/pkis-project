package com.tradevan.mapper.pkis.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.sql.Timestamp;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author system
 * @since 2020-08-19
 */
@TableName("EMAILQUEUE")
public class Emailqueue extends Model<Emailqueue> {

    private static final long serialVersionUID=1L;

    @TableId("SERNO")
    private Long serno;

    @TableField("ATTACHMENT")
    private String attachment;

    @TableField("CATEGORY")
    private String category;

    @TableField("CONTENT")
    private String content;

    @TableField("CREATETIME")
    private Timestamp createtime;

    @TableField("CREATEUSERID")
    private String createuserid;

    @TableField("FAILREASON")
    private String failreason;

    @TableField("MAILTO")
    private String mailto;

    @TableField("MAILTOBCC")
    private String mailtobcc;

    @TableField("MAILTOCC")
    private String mailtocc;

    @TableField("PRIORITY")
    private String priority;

    @TableField("RETRYCNT")
    private Integer retrycnt;

    @TableField("STATUS")
    private String status;

    @TableField("SUBJECT")
    private String subject;

    @TableField("SYSID")
    private String sysid;

    @TableField("SYSMEMO")
    private String sysmemo;

    @TableField("UPDATETIME")
    private Timestamp updatetime;

    @TableField("UPDATEUSERID")
    private String updateuserid;


    public Long getSerno() {
        return serno;
    }

    public Emailqueue setSerno(Long serno) {
        this.serno = serno;
        return this;
    }

    public String getAttachment() {
        return attachment;
    }

    public Emailqueue setAttachment(String attachment) {
        this.attachment = attachment;
        return this;
    }

    public String getCategory() {
        return category;
    }

    public Emailqueue setCategory(String category) {
        this.category = category;
        return this;
    }

    public String getContent() {
        return content;
    }

    public Emailqueue setContent(String content) {
        this.content = content;
        return this;
    }

    public Timestamp getCreatetime() {
        return createtime;
    }

    public Emailqueue setCreatetime(Timestamp createtime) {
        this.createtime = createtime;
        return this;
    }

    public String getCreateuserid() {
        return createuserid;
    }

    public Emailqueue setCreateuserid(String createuserid) {
        this.createuserid = createuserid;
        return this;
    }

    public String getFailreason() {
        return failreason;
    }

    public Emailqueue setFailreason(String failreason) {
        this.failreason = failreason;
        return this;
    }

    public String getMailto() {
        return mailto;
    }

    public Emailqueue setMailto(String mailto) {
        this.mailto = mailto;
        return this;
    }

    public String getMailtobcc() {
        return mailtobcc;
    }

    public Emailqueue setMailtobcc(String mailtobcc) {
        this.mailtobcc = mailtobcc;
        return this;
    }

    public String getMailtocc() {
        return mailtocc;
    }

    public Emailqueue setMailtocc(String mailtocc) {
        this.mailtocc = mailtocc;
        return this;
    }

    public String getPriority() {
        return priority;
    }

    public Emailqueue setPriority(String priority) {
        this.priority = priority;
        return this;
    }

    public Integer getRetrycnt() {
        return retrycnt;
    }

    public Emailqueue setRetrycnt(Integer retrycnt) {
        this.retrycnt = retrycnt;
        return this;
    }

    public String getStatus() {
        return status;
    }

    public Emailqueue setStatus(String status) {
        this.status = status;
        return this;
    }

    public String getSubject() {
        return subject;
    }

    public Emailqueue setSubject(String subject) {
        this.subject = subject;
        return this;
    }

    public String getSysid() {
        return sysid;
    }

    public Emailqueue setSysid(String sysid) {
        this.sysid = sysid;
        return this;
    }

    public String getSysmemo() {
        return sysmemo;
    }

    public Emailqueue setSysmemo(String sysmemo) {
        this.sysmemo = sysmemo;
        return this;
    }

    public Timestamp getUpdatetime() {
        return updatetime;
    }

    public Emailqueue setUpdatetime(Timestamp updatetime) {
        this.updatetime = updatetime;
        return this;
    }

    public String getUpdateuserid() {
        return updateuserid;
    }

    public Emailqueue setUpdateuserid(String updateuserid) {
        this.updateuserid = updateuserid;
        return this;
    }

    @Override
    protected Serializable pkVal() {
        return this.serno;
    }

    @Override
    public String toString() {
        return "Emailqueue{" +
        "serno=" + serno +
        ", attachment=" + attachment +
        ", category=" + category +
        ", content=" + content +
        ", createtime=" + createtime +
        ", createuserid=" + createuserid +
        ", failreason=" + failreason +
        ", mailto=" + mailto +
        ", mailtobcc=" + mailtobcc +
        ", mailtocc=" + mailtocc +
        ", priority=" + priority +
        ", retrycnt=" + retrycnt +
        ", status=" + status +
        ", subject=" + subject +
        ", sysid=" + sysid +
        ", sysmemo=" + sysmemo +
        ", updatetime=" + updatetime +
        ", updateuserid=" + updateuserid +
        "}";
    }
}
