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
@TableName("FLOWCONF")
public class Flowconf extends Model<Flowconf> {

    private static final long serialVersionUID=1L;

    @TableId("SERNO")
    private Long serno;

    @TableField("FLOWID")
    private String flowid;

    @TableField("FLOWNAME")
    private String flowname;

    @TableField("FLOWVERSION")
    private String flowversion;

    @TableField("FLOWDESC")
    private String flowdesc;

    @TableField("FLOWADMINID")
    private String flowadminid;

    @TableField("SYSID")
    private String sysid;

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

    public Long getSerno() {
        return serno;
    }

    public Flowconf setSerno(Long serno) {
        this.serno = serno;
        return this;
    }

    public String getFlowid() {
        return flowid;
    }

    public Flowconf setFlowid(String flowid) {
        this.flowid = flowid;
        return this;
    }

    public String getFlowname() {
        return flowname;
    }

    public Flowconf setFlowname(String flowname) {
        this.flowname = flowname;
        return this;
    }

    public String getFlowversion() {
        return flowversion;
    }

    public Flowconf setFlowversion(String flowversion) {
        this.flowversion = flowversion;
        return this;
    }

    public String getFlowdesc() {
        return flowdesc;
    }

    public Flowconf setFlowdesc(String flowdesc) {
        this.flowdesc = flowdesc;
        return this;
    }

    public String getFlowadminid() {
        return flowadminid;
    }

    public Flowconf setFlowadminid(String flowadminid) {
        this.flowadminid = flowadminid;
        return this;
    }

    public String getSysid() {
        return sysid;
    }

    public Flowconf setSysid(String sysid) {
        this.sysid = sysid;
        return this;
    }

    public String getCreateuserid() {
        return createuserid;
    }

    public Flowconf setCreateuserid(String createuserid) {
        this.createuserid = createuserid;
        return this;
    }

    public String getUpdateuserid() {
        return updateuserid;
    }

    public Flowconf setUpdateuserid(String updateuserid) {
        this.updateuserid = updateuserid;
        return this;
    }

    public String getCreateagentid() {
        return createagentid;
    }

    public Flowconf setCreateagentid(String createagentid) {
        this.createagentid = createagentid;
        return this;
    }

    public String getUpdateagentid() {
        return updateagentid;
    }

    public Flowconf setUpdateagentid(String updateagentid) {
        this.updateagentid = updateagentid;
        return this;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public Flowconf setCreatetime(Date createtime) {
        this.createtime = createtime;
        return this;
    }

    public Date getUpdatetime() {
        return updatetime;
    }

    public Flowconf setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
        return this;
    }

    @Override
    protected Serializable pkVal() {
        return this.serno;
    }

    @Override
    public String toString() {
        return "Flowconf{" +
        "serno=" + serno +
        ", flowid=" + flowid +
        ", flowname=" + flowname +
        ", flowversion=" + flowversion +
        ", flowdesc=" + flowdesc +
        ", flowadminid=" + flowadminid +
        ", sysid=" + sysid +
        ", createuserid=" + createuserid +
        ", updateuserid=" + updateuserid +
        ", createagentid=" + createagentid +
        ", updateagentid=" + updateagentid +
        ", createtime=" + createtime +
        ", updatetime=" + updatetime +
        "}";
    }
}
