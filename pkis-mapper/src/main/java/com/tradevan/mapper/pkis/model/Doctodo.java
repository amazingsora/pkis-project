package com.tradevan.mapper.pkis.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.util.Date;
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
@TableName("DOCTODO")
public class Doctodo extends Model<Doctodo> {

    private static final long serialVersionUID=1L;

    @TableId("SERNO")
    private Long serno;

    @TableField("FORMID")
    private String formid;

    @TableField("TODONO")
    private String todono;

    @TableField("SERIALNO")
    private Integer serialno;

    @TableField("USERID")
    private String userid;

    @TableField("SYSID")
    private String sysid;

    @TableField("PROJID")
    private String projid;

    @TableField("SUBJECT")
    private String subject;

    @TableField("STATUS")
    private String status;

    @TableField("OLDSTATUS")
    private String oldstatus;

    @TableField("MAINFORMSERNO")
    private Long mainformserno;

    @TableField("OTHERINFO")
    private String otherinfo;

    @TableField("OTHERSERNO")
    private Long otherserno;

    @TableField("CREATEUSERID")
    private String createuserid;

    @TableField("UPDATEUSERID")
    private String updateuserid;

    @TableField("CREATEAGENTID")
    private String createagentid;

    @TableField("UPDATEAGENTID")
    private String updateagentid;

    @TableField("CREATETIME")
    private Date createtime;

    @TableField("UPDATETIME")
    private Date updatetime;

    @TableField("SYSMEMO")
    private String sysmemo;


    public Long getSerno() {
        return serno;
    }

    public Doctodo setSerno(Long serno) {
        this.serno = serno;
        return this;
    }

    public String getFormid() {
        return formid;
    }

    public Doctodo setFormid(String formid) {
        this.formid = formid;
        return this;
    }

    public String getTodono() {
        return todono;
    }

    public Doctodo setTodono(String todono) {
        this.todono = todono;
        return this;
    }

    public Integer getSerialno() {
        return serialno;
    }

    public Doctodo setSerialno(Integer serialno) {
        this.serialno = serialno;
        return this;
    }

    public String getUserid() {
        return userid;
    }

    public Doctodo setUserid(String userid) {
        this.userid = userid;
        return this;
    }

    public String getSysid() {
        return sysid;
    }

    public Doctodo setSysid(String sysid) {
        this.sysid = sysid;
        return this;
    }

    public String getProjid() {
        return projid;
    }

    public Doctodo setProjid(String projid) {
        this.projid = projid;
        return this;
    }

    public String getSubject() {
        return subject;
    }

    public Doctodo setSubject(String subject) {
        this.subject = subject;
        return this;
    }

    public String getStatus() {
        return status;
    }

    public Doctodo setStatus(String status) {
        this.status = status;
        return this;
    }

    public String getOldstatus() {
        return oldstatus;
    }

    public Doctodo setOldstatus(String oldstatus) {
        this.oldstatus = oldstatus;
        return this;
    }

    public Long getMainformserno() {
        return mainformserno;
    }

    public Doctodo setMainformserno(Long mainformserno) {
        this.mainformserno = mainformserno;
        return this;
    }

    public String getOtherinfo() {
        return otherinfo;
    }

    public Doctodo setOtherinfo(String otherinfo) {
        this.otherinfo = otherinfo;
        return this;
    }

    public Long getOtherserno() {
        return otherserno;
    }

    public Doctodo setOtherserno(Long otherserno) {
        this.otherserno = otherserno;
        return this;
    }

    public String getCreateuserid() {
        return createuserid;
    }

    public Doctodo setCreateuserid(String createuserid) {
        this.createuserid = createuserid;
        return this;
    }

    public String getUpdateuserid() {
        return updateuserid;
    }

    public Doctodo setUpdateuserid(String updateuserid) {
        this.updateuserid = updateuserid;
        return this;
    }

    public String getCreateagentid() {
        return createagentid;
    }

    public Doctodo setCreateagentid(String createagentid) {
        this.createagentid = createagentid;
        return this;
    }

    public String getUpdateagentid() {
        return updateagentid;
    }

    public Doctodo setUpdateagentid(String updateagentid) {
        this.updateagentid = updateagentid;
        return this;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public Doctodo setCreatetime(Date createtime) {
        this.createtime = createtime;
        return this;
    }

    public Date getUpdatetime() {
        return updatetime;
    }

    public Doctodo setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
        return this;
    }

    public String getSysmemo() {
        return sysmemo;
    }

    public Doctodo setSysmemo(String sysmemo) {
        this.sysmemo = sysmemo;
        return this;
    }

    @Override
    protected Serializable pkVal() {
        return this.serno;
    }

    @Override
    public String toString() {
        return "Doctodo{" +
        "serno=" + serno +
        ", formid=" + formid +
        ", todono=" + todono +
        ", serialno=" + serialno +
        ", userid=" + userid +
        ", sysid=" + sysid +
        ", projid=" + projid +
        ", subject=" + subject +
        ", status=" + status +
        ", oldstatus=" + oldstatus +
        ", mainformserno=" + mainformserno +
        ", otherinfo=" + otherinfo +
        ", otherserno=" + otherserno +
        ", createuserid=" + createuserid +
        ", updateuserid=" + updateuserid +
        ", createagentid=" + createagentid +
        ", updateagentid=" + updateagentid +
        ", createtime=" + createtime +
        ", updatetime=" + updatetime +
        ", sysmemo=" + sysmemo +
        "}";
    }
}
