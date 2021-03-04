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
 * @since 2020-08-05
 */
@TableName("FLOWSTEP")
public class Flowstep extends Model<Flowstep> {

    private static final long serialVersionUID=1L;

    @TableId("SERNO")
    private Long serno;

    @TableField("CREATEAGENTID")
    private String createagentid;

    @TableField("CREATETIME")
    private Timestamp createtime;

    @TableField("CREATEUSERID")
    private String createuserid;

    @TableField("DEPTID")
    private String deptid;

    @TableField("STEPDESC")
    private String stepdesc;

    @TableField("DISPORD")
    private Long dispord;

    @TableField("EXTENSION")
    private String extension;

    @TableField("FLOWID")
    private String flowid;

    @TableField("ISPROJROLE")
    private Integer isprojrole;

    @TableField("ISREVIEWDEPTROLE")
    private Integer isreviewdeptrole;

    @TableField("STEPNAME")
    private String stepname;

    @TableField("PARALLELPASSCOUNT")
    private Long parallelpasscount;

    @TableField("ROLEIDS")
    private String roleids;

    @TableField("SAMEUSERAS")
    private String sameuseras;

    @TableField("STEPID")
    private String stepid;

    @TableField("STEPTYPE")
    private String steptype;

    @TableField("SUBFLOWID")
    private String subflowid;

    @TableField("UPDATEAGENTID")
    private String updateagentid;

    @TableField("UPDATETIME")
    private Timestamp updatetime;

    @TableField("UPDATEUSERID")
    private String updateuserid;

    @TableField("USERIDS")
    private String userids;


    public Long getSerno() {
        return serno;
    }

    public Flowstep setSerno(Long serno) {
        this.serno = serno;
        return this;
    }

    public String getCreateagentid() {
        return createagentid;
    }

    public Flowstep setCreateagentid(String createagentid) {
        this.createagentid = createagentid;
        return this;
    }

    public Timestamp getCreatetime() {
        return createtime;
    }

    public Flowstep setCreatetime(Timestamp createtime) {
        this.createtime = createtime;
        return this;
    }

    public String getCreateuserid() {
        return createuserid;
    }

    public Flowstep setCreateuserid(String createuserid) {
        this.createuserid = createuserid;
        return this;
    }

    public String getDeptid() {
        return deptid;
    }

    public Flowstep setDeptid(String deptid) {
        this.deptid = deptid;
        return this;
    }

    public String getStepdesc() {
        return stepdesc;
    }

    public Flowstep setStepdesc(String stepdesc) {
        this.stepdesc = stepdesc;
        return this;
    }

    public Long getDispord() {
        return dispord;
    }

    public Flowstep setDispord(Long dispord) {
        this.dispord = dispord;
        return this;
    }

    public String getExtension() {
        return extension;
    }

    public Flowstep setExtension(String extension) {
        this.extension = extension;
        return this;
    }

    public String getFlowid() {
        return flowid;
    }

    public Flowstep setFlowid(String flowid) {
        this.flowid = flowid;
        return this;
    }

    public Integer getIsprojrole() {
        return isprojrole;
    }

    public Flowstep setIsprojrole(Integer isprojrole) {
        this.isprojrole = isprojrole;
        return this;
    }

    public Integer getIsreviewdeptrole() {
        return isreviewdeptrole;
    }

    public Flowstep setIsreviewdeptrole(Integer isreviewdeptrole) {
        this.isreviewdeptrole = isreviewdeptrole;
        return this;
    }

    public String getStepname() {
        return stepname;
    }

    public Flowstep setStepname(String stepname) {
        this.stepname = stepname;
        return this;
    }

    public Long getParallelpasscount() {
        return parallelpasscount;
    }

    public Flowstep setParallelpasscount(Long parallelpasscount) {
        this.parallelpasscount = parallelpasscount;
        return this;
    }

    public String getRoleids() {
        return roleids;
    }

    public Flowstep setRoleids(String roleids) {
        this.roleids = roleids;
        return this;
    }

    public String getSameuseras() {
        return sameuseras;
    }

    public Flowstep setSameuseras(String sameuseras) {
        this.sameuseras = sameuseras;
        return this;
    }

    public String getStepid() {
        return stepid;
    }

    public Flowstep setStepid(String stepid) {
        this.stepid = stepid;
        return this;
    }

    public String getSteptype() {
        return steptype;
    }

    public Flowstep setSteptype(String steptype) {
        this.steptype = steptype;
        return this;
    }

    public String getSubflowid() {
        return subflowid;
    }

    public Flowstep setSubflowid(String subflowid) {
        this.subflowid = subflowid;
        return this;
    }

    public String getUpdateagentid() {
        return updateagentid;
    }

    public Flowstep setUpdateagentid(String updateagentid) {
        this.updateagentid = updateagentid;
        return this;
    }

    public Timestamp getUpdatetime() {
        return updatetime;
    }

    public Flowstep setUpdatetime(Timestamp updatetime) {
        this.updatetime = updatetime;
        return this;
    }

    public String getUpdateuserid() {
        return updateuserid;
    }

    public Flowstep setUpdateuserid(String updateuserid) {
        this.updateuserid = updateuserid;
        return this;
    }

    public String getUserids() {
        return userids;
    }

    public Flowstep setUserids(String userids) {
        this.userids = userids;
        return this;
    }

    @Override
    protected Serializable pkVal() {
        return this.serno;
    }

    @Override
    public String toString() {
        return "Flowstep{" +
        "serno=" + serno +
        ", createagentid=" + createagentid +
        ", createtime=" + createtime +
        ", createuserid=" + createuserid +
        ", deptid=" + deptid +
        ", stepdesc=" + stepdesc +
        ", dispord=" + dispord +
        ", extension=" + extension +
        ", flowid=" + flowid +
        ", isprojrole=" + isprojrole +
        ", isreviewdeptrole=" + isreviewdeptrole +
        ", stepname=" + stepname +
        ", parallelpasscount=" + parallelpasscount +
        ", roleids=" + roleids +
        ", sameuseras=" + sameuseras +
        ", stepid=" + stepid +
        ", steptype=" + steptype +
        ", subflowid=" + subflowid +
        ", updateagentid=" + updateagentid +
        ", updatetime=" + updatetime +
        ", updateuserid=" + updateuserid +
        ", userids=" + userids +
        "}";
    }
}
