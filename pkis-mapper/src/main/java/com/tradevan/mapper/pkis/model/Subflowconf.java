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
@TableName("SUBFLOWCONF")
public class Subflowconf extends Model<Subflowconf> {

    private static final long serialVersionUID=1L;

    @TableId("SERNO")
    private Long serno;

    @TableField("FLOWID")
    private String flowid;

    @TableField("SUBFLOWID")
    private String subflowid;

    @TableField("SUBFLOWNAME")
    private String subflowname;

    @TableField("SUBFLOWDESC")
    private String subflowdesc;

    @TableField("FINISHTO")
    private String finishto;

    @TableField("RETURNTO")
    private String returnto;

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

    public Subflowconf setSerno(Long serno) {
        this.serno = serno;
        return this;
    }

    public String getFlowid() {
        return flowid;
    }

    public Subflowconf setFlowid(String flowid) {
        this.flowid = flowid;
        return this;
    }

    public String getSubflowid() {
        return subflowid;
    }

    public Subflowconf setSubflowid(String subflowid) {
        this.subflowid = subflowid;
        return this;
    }

    public String getSubflowname() {
        return subflowname;
    }

    public Subflowconf setSubflowname(String subflowname) {
        this.subflowname = subflowname;
        return this;
    }

    public String getSubflowdesc() {
        return subflowdesc;
    }

    public Subflowconf setSubflowdesc(String subflowdesc) {
        this.subflowdesc = subflowdesc;
        return this;
    }

    public String getFinishto() {
        return finishto;
    }

    public Subflowconf setFinishto(String finishto) {
        this.finishto = finishto;
        return this;
    }

    public String getReturnto() {
        return returnto;
    }

    public Subflowconf setReturnto(String returnto) {
        this.returnto = returnto;
        return this;
    }

    public String getCreateuserid() {
        return createuserid;
    }

    public Subflowconf setCreateuserid(String createuserid) {
        this.createuserid = createuserid;
        return this;
    }

    public String getUpdateuserid() {
        return updateuserid;
    }

    public Subflowconf setUpdateuserid(String updateuserid) {
        this.updateuserid = updateuserid;
        return this;
    }

    public String getCreateagentid() {
        return createagentid;
    }

    public Subflowconf setCreateagentid(String createagentid) {
        this.createagentid = createagentid;
        return this;
    }

    public String getUpdateagentid() {
        return updateagentid;
    }

    public Subflowconf setUpdateagentid(String updateagentid) {
        this.updateagentid = updateagentid;
        return this;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public Subflowconf setCreatetime(Date createtime) {
        this.createtime = createtime;
        return this;
    }

    public Date getUpdatetime() {
        return updatetime;
    }

    public Subflowconf setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
        return this;
    }

    public String getSysmemo() {
        return sysmemo;
    }

    public Subflowconf setSysmemo(String sysmemo) {
        this.sysmemo = sysmemo;
        return this;
    }

    @Override
    protected Serializable pkVal() {
        return this.serno;
    }

    @Override
    public String toString() {
        return "Subflowconf{" +
        "serno=" + serno +
        ", flowid=" + flowid +
        ", subflowid=" + subflowid +
        ", subflowname=" + subflowname +
        ", subflowdesc=" + subflowdesc +
        ", finishto=" + finishto +
        ", returnto=" + returnto +
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
