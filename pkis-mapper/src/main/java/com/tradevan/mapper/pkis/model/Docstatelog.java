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
@TableName("DOCSTATELOG")
public class Docstatelog extends Model<Docstatelog> {

    private static final long serialVersionUID=1L;

    @TableId("SERNO")
    private Long serno;

    @TableField("SOURCESERNO")
    private Long sourceserno;

    @TableField("FORMID")
    private String formid;

    @TableField("APPLYNO")
    private String applyno;

    @TableField("SERIALNO")
    private Integer serialno;

    @TableField("FLOWID")
    private String flowid;

    @TableField("FLOWVERSION")
    private String flowversion;

    @TableField("TASKTYPE")
    private String tasktype;

    @TableField("FROMTASKID")
    private String fromtaskid;

    @TableField("TOTASKID")
    private String totaskid;

    @TableField("FLOWSTATUS")
    private String flowstatus;

    @TableField("TASKROLEIDS")
    private String taskroleids;

    @TableField("TASKDEPTID")
    private String taskdeptid;

    @TableField("TASKUSERIDS")
    private String taskuserids;

    @TableField("TASKNAME")
    private String taskname;

    @TableField("TASKDESC")
    private String taskdesc;

    @TableField("LINKNAME")
    private String linkname;

    @TableField("LINKDESC")
    private String linkdesc;

    @TableField("APPLICANTID")
    private String applicantid;

    @TableField("USERID")
    private String userid;

    @TableField("BEREPRESENTEDID")
    private String berepresentedid;

    @TableField("AGENTID")
    private String agentid;

    @TableField("BYFLOWADMINID")
    private String byflowadminid;

    @TableField("SUBFLOWDEPTID")
    private String subflowdeptid;

    @TableField("LASTREVIEWDEPTID")
    private String lastreviewdeptid;

    @TableField("PROJID")
    private String projid;

    @TableField("ISPROJROLE")
    private String isprojrole;

    @TableField("ISREVIEWDEPTROLE")
    private String isreviewdeptrole;

    @TableField("MEMO")
    private String memo;

    @TableField("STATECREATETIME")
    private Date statecreatetime;

    @TableField("STATEUPDATETIME")
    private Date stateupdatetime;

    @TableField("CREATETIME")
    private Date createtime;

    @TableField("CANBATCHREVIEW")
    private String canbatchreview;

    @TableField("TASKEXT")
    private String taskext;


    public Long getSerno() {
        return serno;
    }

    public Docstatelog setSerno(Long serno) {
        this.serno = serno;
        return this;
    }

    public Long getSourceserno() {
        return sourceserno;
    }

    public Docstatelog setSourceserno(Long sourceserno) {
        this.sourceserno = sourceserno;
        return this;
    }

    public String getFormid() {
        return formid;
    }

    public Docstatelog setFormid(String formid) {
        this.formid = formid;
        return this;
    }

    public String getApplyno() {
        return applyno;
    }

    public Docstatelog setApplyno(String applyno) {
        this.applyno = applyno;
        return this;
    }

    public Integer getSerialno() {
        return serialno;
    }

    public Docstatelog setSerialno(Integer serialno) {
        this.serialno = serialno;
        return this;
    }

    public String getFlowid() {
        return flowid;
    }

    public Docstatelog setFlowid(String flowid) {
        this.flowid = flowid;
        return this;
    }

    public String getFlowversion() {
        return flowversion;
    }

    public Docstatelog setFlowversion(String flowversion) {
        this.flowversion = flowversion;
        return this;
    }

    public String getTasktype() {
        return tasktype;
    }

    public Docstatelog setTasktype(String tasktype) {
        this.tasktype = tasktype;
        return this;
    }

    public String getFromtaskid() {
        return fromtaskid;
    }

    public Docstatelog setFromtaskid(String fromtaskid) {
        this.fromtaskid = fromtaskid;
        return this;
    }

    public String getTotaskid() {
        return totaskid;
    }

    public Docstatelog setTotaskid(String totaskid) {
        this.totaskid = totaskid;
        return this;
    }

    public String getFlowstatus() {
        return flowstatus;
    }

    public Docstatelog setFlowstatus(String flowstatus) {
        this.flowstatus = flowstatus;
        return this;
    }

    public String getTaskroleids() {
        return taskroleids;
    }

    public Docstatelog setTaskroleids(String taskroleids) {
        this.taskroleids = taskroleids;
        return this;
    }

    public String getTaskdeptid() {
        return taskdeptid;
    }

    public Docstatelog setTaskdeptid(String taskdeptid) {
        this.taskdeptid = taskdeptid;
        return this;
    }

    public String getTaskuserids() {
        return taskuserids;
    }

    public Docstatelog setTaskuserids(String taskuserids) {
        this.taskuserids = taskuserids;
        return this;
    }

    public String getTaskname() {
        return taskname;
    }

    public Docstatelog setTaskname(String taskname) {
        this.taskname = taskname;
        return this;
    }

    public String getTaskdesc() {
        return taskdesc;
    }

    public Docstatelog setTaskdesc(String taskdesc) {
        this.taskdesc = taskdesc;
        return this;
    }

    public String getLinkname() {
        return linkname;
    }

    public Docstatelog setLinkname(String linkname) {
        this.linkname = linkname;
        return this;
    }

    public String getLinkdesc() {
        return linkdesc;
    }

    public Docstatelog setLinkdesc(String linkdesc) {
        this.linkdesc = linkdesc;
        return this;
    }

    public String getApplicantid() {
        return applicantid;
    }

    public Docstatelog setApplicantid(String applicantid) {
        this.applicantid = applicantid;
        return this;
    }

    public String getUserid() {
        return userid;
    }

    public Docstatelog setUserid(String userid) {
        this.userid = userid;
        return this;
    }

    public String getBerepresentedid() {
        return berepresentedid;
    }

    public Docstatelog setBerepresentedid(String berepresentedid) {
        this.berepresentedid = berepresentedid;
        return this;
    }

    public String getAgentid() {
        return agentid;
    }

    public Docstatelog setAgentid(String agentid) {
        this.agentid = agentid;
        return this;
    }

    public String getByflowadminid() {
        return byflowadminid;
    }

    public Docstatelog setByflowadminid(String byflowadminid) {
        this.byflowadminid = byflowadminid;
        return this;
    }

    public String getSubflowdeptid() {
        return subflowdeptid;
    }

    public Docstatelog setSubflowdeptid(String subflowdeptid) {
        this.subflowdeptid = subflowdeptid;
        return this;
    }

    public String getLastreviewdeptid() {
        return lastreviewdeptid;
    }

    public Docstatelog setLastreviewdeptid(String lastreviewdeptid) {
        this.lastreviewdeptid = lastreviewdeptid;
        return this;
    }

    public String getProjid() {
        return projid;
    }

    public Docstatelog setProjid(String projid) {
        this.projid = projid;
        return this;
    }

    public String getIsprojrole() {
        return isprojrole;
    }

    public Docstatelog setIsprojrole(String isprojrole) {
        this.isprojrole = isprojrole;
        return this;
    }

    public String getIsreviewdeptrole() {
        return isreviewdeptrole;
    }

    public Docstatelog setIsreviewdeptrole(String isreviewdeptrole) {
        this.isreviewdeptrole = isreviewdeptrole;
        return this;
    }

    public String getMemo() {
        return memo;
    }

    public Docstatelog setMemo(String memo) {
        this.memo = memo;
        return this;
    }

    public Date getStatecreatetime() {
        return statecreatetime;
    }

    public Docstatelog setStatecreatetime(Date statecreatetime) {
        this.statecreatetime = statecreatetime;
        return this;
    }

    public Date getStateupdatetime() {
        return stateupdatetime;
    }

    public Docstatelog setStateupdatetime(Date stateupdatetime) {
        this.stateupdatetime = stateupdatetime;
        return this;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public Docstatelog setCreatetime(Date createtime) {
        this.createtime = createtime;
        return this;
    }

    public String getCanbatchreview() {
        return canbatchreview;
    }

    public Docstatelog setCanbatchreview(String canbatchreview) {
        this.canbatchreview = canbatchreview;
        return this;
    }

    public String getTaskext() {
        return taskext;
    }

    public Docstatelog setTaskext(String taskext) {
        this.taskext = taskext;
        return this;
    }

    @Override
    protected Serializable pkVal() {
        return this.serno;
    }

    @Override
    public String toString() {
        return "Docstatelog{" +
        "serno=" + serno +
        ", sourceserno=" + sourceserno +
        ", formid=" + formid +
        ", applyno=" + applyno +
        ", serialno=" + serialno +
        ", flowid=" + flowid +
        ", flowversion=" + flowversion +
        ", tasktype=" + tasktype +
        ", fromtaskid=" + fromtaskid +
        ", totaskid=" + totaskid +
        ", flowstatus=" + flowstatus +
        ", taskroleids=" + taskroleids +
        ", taskdeptid=" + taskdeptid +
        ", taskuserids=" + taskuserids +
        ", taskname=" + taskname +
        ", taskdesc=" + taskdesc +
        ", linkname=" + linkname +
        ", linkdesc=" + linkdesc +
        ", applicantid=" + applicantid +
        ", userid=" + userid +
        ", berepresentedid=" + berepresentedid +
        ", agentid=" + agentid +
        ", byflowadminid=" + byflowadminid +
        ", subflowdeptid=" + subflowdeptid +
        ", lastreviewdeptid=" + lastreviewdeptid +
        ", projid=" + projid +
        ", isprojrole=" + isprojrole +
        ", isreviewdeptrole=" + isreviewdeptrole +
        ", memo=" + memo +
        ", statecreatetime=" + statecreatetime +
        ", stateupdatetime=" + stateupdatetime +
        ", createtime=" + createtime +
        ", canbatchreview=" + canbatchreview +
        ", taskext=" + taskext +
        "}";
    }
}
