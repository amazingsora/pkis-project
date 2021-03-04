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
@TableName("FLOWXML")
public class Flowxml extends Model<Flowxml> {

    private static final long serialVersionUID=1L;

    @TableId("SERNO")
    private Long serno;

    @TableField("FLOWID")
    private String flowid;

    @TableField("FLOWVERSION")
    private String flowversion;

    @TableField("FLOWXML")
    private String flowxml;

    @TableField("CREATEUSERID")
    private String createuserid;

    @TableField("CREATEAGENTID")
    private String createagentid;

    @TableField("CREATETIME")
    private Date createtime;

    @TableField("SYSMEMO")
    private String sysmemo;


    public Long getSerno() {
        return serno;
    }

    public Flowxml setSerno(Long serno) {
        this.serno = serno;
        return this;
    }

    public String getFlowid() {
        return flowid;
    }

    public Flowxml setFlowid(String flowid) {
        this.flowid = flowid;
        return this;
    }

    public String getFlowversion() {
        return flowversion;
    }

    public Flowxml setFlowversion(String flowversion) {
        this.flowversion = flowversion;
        return this;
    }

    public String getFlowxml() {
        return flowxml;
    }

    public Flowxml setFlowxml(String flowxml) {
        this.flowxml = flowxml;
        return this;
    }

    public String getCreateuserid() {
        return createuserid;
    }

    public Flowxml setCreateuserid(String createuserid) {
        this.createuserid = createuserid;
        return this;
    }

    public String getCreateagentid() {
        return createagentid;
    }

    public Flowxml setCreateagentid(String createagentid) {
        this.createagentid = createagentid;
        return this;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public Flowxml setCreatetime(Date createtime) {
        this.createtime = createtime;
        return this;
    }

    public String getSysmemo() {
        return sysmemo;
    }

    public Flowxml setSysmemo(String sysmemo) {
        this.sysmemo = sysmemo;
        return this;
    }

    @Override
    protected Serializable pkVal() {
        return this.serno;
    }

    @Override
    public String toString() {
        return "Flowxml{" +
        "serno=" + serno +
        ", flowid=" + flowid +
        ", flowversion=" + flowversion +
        ", flowxml=" + flowxml +
        ", createuserid=" + createuserid +
        ", createagentid=" + createagentid +
        ", createtime=" + createtime +
        ", sysmemo=" + sysmemo +
        "}";
    }
}
