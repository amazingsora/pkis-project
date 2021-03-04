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
 * @since 2020-08-21
 */
@TableName("DOCSTATE")
public class Docstate extends Model<Docstate> {

    private static final long serialVersionUID=1L;

    @TableId("SERNO")
    private Long serno;

    @TableField("APP_ID")
    private String appId;

    @TableField("APPLICANTAGENTID")
    private String applicantagentid;

    @TableField("APPLYNO")
    private String applyno;

    @TableField("BYFLOWADMINID")
    private String byflowadminid;

    @TableField("CANBATCHREVIEW")
    private String canbatchreview;

    @TableField("CREATETIME")
    private Timestamp createtime;

    @TableField("IDEN_ID")
    private String idenId;

    @TableField("FLOWADMINID")
    private String flowadminid;

    @TableField("FLOWID")
    private String flowid;

    @TableField("FLOWSTATUS")
    private String flowstatus;

    @TableField("FLOWVERSION")
    private String flowversion;

    @TableField("FORMID")
    private String formid;

    @TableField("ISPROJROLE")
    private String isprojrole;

    @TableField("ISREVIEWDEPTROLE")
    private String isreviewdeptrole;

    @TableField("LASTREVIEWDEPTID")
    private String lastreviewdeptid;

    @TableField("LINKDESC")
    private String linkdesc;

    @TableField("LINKNAME")
    private String linkname;

    @TableField("MAINFORMSERNO")
    private Long mainformserno;

    @TableField("MAINFORMTABLE")
    private String mainformtable;

    @TableField("NOWTASKID")
    private String nowtaskid;

    @TableField("NOWUSERID")
    private String nowuserid;

    @TableField("PARALLELPASSCOUNT")
    private Long parallelpasscount;

    @TableField("PREVIOUSTASKID")
    private String previoustaskid;

    @TableField("PREVIOUSUSERID")
    private String previoususerid;

    @TableField("PROJID")
    private String projid;

    @TableField("SERIALNO")
    private Long serialno;

    @TableField("SUBFLOWDEPTID")
    private String subflowdeptid;

    @TableField("SUBJECT")
    private String subject;

    @TableField("SYSID")
    private String sysid;

    @TableField("TASKDEPTID")
    private String taskdeptid;

    @TableField("TASKDESC")
    private String taskdesc;

    @TableField("TASKEXT")
    private String taskext;

    @TableField("TASKNAME")
    private String taskname;

    @TableField("TASKROLEIDS")
    private String taskroleids;

    @TableField("TASKUSERIDS")
    private String taskuserids;

    @TableField("UPDOCSTATEID")
    private Long updocstateid;

    @TableField("UPDATETIME")
    private Timestamp updatetime;

    @TableField("APPLICANTID")
    private String applicantid;


    public Long getSerno() {
        return serno;
    }

    public Docstate setSerno(Long serno) {
        this.serno = serno;
        return this;
    }

    public String getAppId() {
        return appId;
    }

    public Docstate setAppId(String appId) {
        this.appId = appId;
        return this;
    }

    public String getApplicantagentid() {
        return applicantagentid;
    }

    public Docstate setApplicantagentid(String applicantagentid) {
        this.applicantagentid = applicantagentid;
        return this;
    }

    public String getApplyno() {
        return applyno;
    }

    public Docstate setApplyno(String applyno) {
        this.applyno = applyno;
        return this;
    }

    public String getByflowadminid() {
        return byflowadminid;
    }

    public Docstate setByflowadminid(String byflowadminid) {
        this.byflowadminid = byflowadminid;
        return this;
    }

    public String getCanbatchreview() {
        return canbatchreview;
    }

    public Docstate setCanbatchreview(String canbatchreview) {
        this.canbatchreview = canbatchreview;
        return this;
    }

    public Timestamp getCreatetime() {
        return createtime;
    }

    public Docstate setCreatetime(Timestamp createtime) {
        this.createtime = createtime;
        return this;
    }

    public String getIdenId() {
        return idenId;
    }

    public Docstate setIdenId(String idenId) {
        this.idenId = idenId;
        return this;
    }

    public String getFlowadminid() {
        return flowadminid;
    }

    public Docstate setFlowadminid(String flowadminid) {
        this.flowadminid = flowadminid;
        return this;
    }

    public String getFlowid() {
        return flowid;
    }

    public Docstate setFlowid(String flowid) {
        this.flowid = flowid;
        return this;
    }

    public String getFlowstatus() {
        return flowstatus;
    }

    public Docstate setFlowstatus(String flowstatus) {
        this.flowstatus = flowstatus;
        return this;
    }

    public String getFlowversion() {
        return flowversion;
    }

    public Docstate setFlowversion(String flowversion) {
        this.flowversion = flowversion;
        return this;
    }

    public String getFormid() {
        return formid;
    }

    public Docstate setFormid(String formid) {
        this.formid = formid;
        return this;
    }

    public String getIsprojrole() {
        return isprojrole;
    }

    public Docstate setIsprojrole(String isprojrole) {
        this.isprojrole = isprojrole;
        return this;
    }

    public String getIsreviewdeptrole() {
        return isreviewdeptrole;
    }

    public Docstate setIsreviewdeptrole(String isreviewdeptrole) {
        this.isreviewdeptrole = isreviewdeptrole;
        return this;
    }

    public String getLastreviewdeptid() {
        return lastreviewdeptid;
    }

    public Docstate setLastreviewdeptid(String lastreviewdeptid) {
        this.lastreviewdeptid = lastreviewdeptid;
        return this;
    }

    public String getLinkdesc() {
        return linkdesc;
    }

    public Docstate setLinkdesc(String linkdesc) {
        this.linkdesc = linkdesc;
        return this;
    }

    public String getLinkname() {
        return linkname;
    }

    public Docstate setLinkname(String linkname) {
        this.linkname = linkname;
        return this;
    }

    public Long getMainformserno() {
        return mainformserno;
    }

    public Docstate setMainformserno(Long mainformserno) {
        this.mainformserno = mainformserno;
        return this;
    }

    public String getMainformtable() {
        return mainformtable;
    }

    public Docstate setMainformtable(String mainformtable) {
        this.mainformtable = mainformtable;
        return this;
    }

    public String getNowtaskid() {
        return nowtaskid;
    }

    public Docstate setNowtaskid(String nowtaskid) {
        this.nowtaskid = nowtaskid;
        return this;
    }

    public String getNowuserid() {
        return nowuserid;
    }

    public Docstate setNowuserid(String nowuserid) {
        this.nowuserid = nowuserid;
        return this;
    }

    public Long getParallelpasscount() {
        return parallelpasscount;
    }

    public Docstate setParallelpasscount(Long parallelpasscount) {
        this.parallelpasscount = parallelpasscount;
        return this;
    }

    public String getPrevioustaskid() {
        return previoustaskid;
    }

    public Docstate setPrevioustaskid(String previoustaskid) {
        this.previoustaskid = previoustaskid;
        return this;
    }

    public String getPrevioususerid() {
        return previoususerid;
    }

    public Docstate setPrevioususerid(String previoususerid) {
        this.previoususerid = previoususerid;
        return this;
    }

    public String getProjid() {
        return projid;
    }

    public Docstate setProjid(String projid) {
        this.projid = projid;
        return this;
    }

    public Long getSerialno() {
        return serialno;
    }

    public Docstate setSerialno(Long serialno) {
        this.serialno = serialno;
        return this;
    }

    public String getSubflowdeptid() {
        return subflowdeptid;
    }

    public Docstate setSubflowdeptid(String subflowdeptid) {
        this.subflowdeptid = subflowdeptid;
        return this;
    }

    public String getSubject() {
        return subject;
    }

    public Docstate setSubject(String subject) {
        this.subject = subject;
        return this;
    }

    public String getSysid() {
        return sysid;
    }

    public Docstate setSysid(String sysid) {
        this.sysid = sysid;
        return this;
    }

    public String getTaskdeptid() {
        return taskdeptid;
    }

    public Docstate setTaskdeptid(String taskdeptid) {
        this.taskdeptid = taskdeptid;
        return this;
    }

    public String getTaskdesc() {
        return taskdesc;
    }

    public Docstate setTaskdesc(String taskdesc) {
        this.taskdesc = taskdesc;
        return this;
    }

    public String getTaskext() {
        return taskext;
    }

    public Docstate setTaskext(String taskext) {
        this.taskext = taskext;
        return this;
    }

    public String getTaskname() {
        return taskname;
    }

    public Docstate setTaskname(String taskname) {
        this.taskname = taskname;
        return this;
    }

    public String getTaskroleids() {
        return taskroleids;
    }

    public Docstate setTaskroleids(String taskroleids) {
        this.taskroleids = taskroleids;
        return this;
    }

    public String getTaskuserids() {
        return taskuserids;
    }

    public Docstate setTaskuserids(String taskuserids) {
        this.taskuserids = taskuserids;
        return this;
    }

    public Long getUpdocstateid() {
        return updocstateid;
    }

    public Docstate setUpdocstateid(Long updocstateid) {
        this.updocstateid = updocstateid;
        return this;
    }

    public Timestamp getUpdatetime() {
        return updatetime;
    }

    public Docstate setUpdatetime(Timestamp updatetime) {
        this.updatetime = updatetime;
        return this;
    }

    public String getApplicantid() {
        return applicantid;
    }

    public Docstate setApplicantid(String applicantid) {
        this.applicantid = applicantid;
        return this;
    }

    @Override
    protected Serializable pkVal() {
        return this.serno;
    }

    @Override
    public String toString() {
        return "Docstate{" +
        "serno=" + serno +
        ", appId=" + appId +
        ", applicantagentid=" + applicantagentid +
        ", applyno=" + applyno +
        ", byflowadminid=" + byflowadminid +
        ", canbatchreview=" + canbatchreview +
        ", createtime=" + createtime +
        ", idenId=" + idenId +
        ", flowadminid=" + flowadminid +
        ", flowid=" + flowid +
        ", flowstatus=" + flowstatus +
        ", flowversion=" + flowversion +
        ", formid=" + formid +
        ", isprojrole=" + isprojrole +
        ", isreviewdeptrole=" + isreviewdeptrole +
        ", lastreviewdeptid=" + lastreviewdeptid +
        ", linkdesc=" + linkdesc +
        ", linkname=" + linkname +
        ", mainformserno=" + mainformserno +
        ", mainformtable=" + mainformtable +
        ", nowtaskid=" + nowtaskid +
        ", nowuserid=" + nowuserid +
        ", parallelpasscount=" + parallelpasscount +
        ", previoustaskid=" + previoustaskid +
        ", previoususerid=" + previoususerid +
        ", projid=" + projid +
        ", serialno=" + serialno +
        ", subflowdeptid=" + subflowdeptid +
        ", subject=" + subject +
        ", sysid=" + sysid +
        ", taskdeptid=" + taskdeptid +
        ", taskdesc=" + taskdesc +
        ", taskext=" + taskext +
        ", taskname=" + taskname +
        ", taskroleids=" + taskroleids +
        ", taskuserids=" + taskuserids +
        ", updocstateid=" + updocstateid +
        ", updatetime=" + updatetime +
        ", applicantid=" + applicantid +
        "}";
    }
}
