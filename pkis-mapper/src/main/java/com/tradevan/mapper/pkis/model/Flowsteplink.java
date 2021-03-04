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
@TableName("FLOWSTEPLINK")
public class Flowsteplink extends Model<Flowsteplink> {

    private static final long serialVersionUID=1L;

    @TableId("SERNO")
    private Long serno;

    @TableField("FLOWID")
    private String flowid;

    @TableField("STEPID")
    private String stepid;

    @TableField("TOSTEPID")
    private String tostepid;

    @TableField("ACTION")
    private String action;

    @TableField("ISCONCURRENT")
    private Integer isconcurrent;

    @TableField("LINKNAME")
    private String linkname;

    @TableField("LINKDESC")
    private String linkdesc;

    @TableField("DISPORD")
    private Integer dispord;

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

    public Flowsteplink setSerno(Long serno) {
        this.serno = serno;
        return this;
    }

    public String getFlowid() {
        return flowid;
    }

    public Flowsteplink setFlowid(String flowid) {
        this.flowid = flowid;
        return this;
    }

    public String getStepid() {
        return stepid;
    }

    public Flowsteplink setStepid(String stepid) {
        this.stepid = stepid;
        return this;
    }

    public String getTostepid() {
        return tostepid;
    }

    public Flowsteplink setTostepid(String tostepid) {
        this.tostepid = tostepid;
        return this;
    }

    public String getAction() {
        return action;
    }

    public Flowsteplink setAction(String action) {
        this.action = action;
        return this;
    }

    public Integer getIsconcurrent() {
        return isconcurrent;
    }

    public Flowsteplink setIsconcurrent(Integer isconcurrent) {
        this.isconcurrent = isconcurrent;
        return this;
    }

    public String getLinkname() {
        return linkname;
    }

    public Flowsteplink setLinkname(String linkname) {
        this.linkname = linkname;
        return this;
    }

    public String getLinkdesc() {
        return linkdesc;
    }

    public Flowsteplink setLinkdesc(String linkdesc) {
        this.linkdesc = linkdesc;
        return this;
    }

    public Integer getDispord() {
        return dispord;
    }

    public Flowsteplink setDispord(Integer dispord) {
        this.dispord = dispord;
        return this;
    }

    public String getCreateuserid() {
        return createuserid;
    }

    public Flowsteplink setCreateuserid(String createuserid) {
        this.createuserid = createuserid;
        return this;
    }

    public String getUpdateuserid() {
        return updateuserid;
    }

    public Flowsteplink setUpdateuserid(String updateuserid) {
        this.updateuserid = updateuserid;
        return this;
    }

    public String getCreateagentid() {
        return createagentid;
    }

    public Flowsteplink setCreateagentid(String createagentid) {
        this.createagentid = createagentid;
        return this;
    }

    public String getUpdateagentid() {
        return updateagentid;
    }

    public Flowsteplink setUpdateagentid(String updateagentid) {
        this.updateagentid = updateagentid;
        return this;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public Flowsteplink setCreatetime(Date createtime) {
        this.createtime = createtime;
        return this;
    }

    public Date getUpdatetime() {
        return updatetime;
    }

    public Flowsteplink setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
        return this;
    }

    public String getSysmemo() {
        return sysmemo;
    }

    public Flowsteplink setSysmemo(String sysmemo) {
        this.sysmemo = sysmemo;
        return this;
    }

    @Override
    protected Serializable pkVal() {
        return this.serno;
    }

    @Override
    public String toString() {
        return "Flowsteplink{" +
        "serno=" + serno +
        ", flowid=" + flowid +
        ", stepid=" + stepid +
        ", tostepid=" + tostepid +
        ", action=" + action +
        ", isconcurrent=" + isconcurrent +
        ", linkname=" + linkname +
        ", linkdesc=" + linkdesc +
        ", dispord=" + dispord +
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
