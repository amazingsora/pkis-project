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
@TableName("FORMCONF")
public class Formconf extends Model<Formconf> {

    private static final long serialVersionUID=1L;

    @TableId("SERNO")
    private Long serno;

    @TableField("FORMID")
    private String formid;

    @TableField("FORMNAME")
    private String formname;

    @TableField("FORMURL")
    private String formurl;

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

    @TableField("SYSMEMO")
    private String sysmemo;


    public Long getSerno() {
        return serno;
    }

    public Formconf setSerno(Long serno) {
        this.serno = serno;
        return this;
    }

    public String getFormid() {
        return formid;
    }

    public Formconf setFormid(String formid) {
        this.formid = formid;
        return this;
    }

    public String getFormname() {
        return formname;
    }

    public Formconf setFormname(String formname) {
        this.formname = formname;
        return this;
    }

    public String getFormurl() {
        return formurl;
    }

    public Formconf setFormurl(String formurl) {
        this.formurl = formurl;
        return this;
    }

    public String getSysid() {
        return sysid;
    }

    public Formconf setSysid(String sysid) {
        this.sysid = sysid;
        return this;
    }

    public String getCreateuserid() {
        return createuserid;
    }

    public Formconf setCreateuserid(String createuserid) {
        this.createuserid = createuserid;
        return this;
    }

    public String getUpdateuserid() {
        return updateuserid;
    }

    public Formconf setUpdateuserid(String updateuserid) {
        this.updateuserid = updateuserid;
        return this;
    }

    public String getCreateagentid() {
        return createagentid;
    }

    public Formconf setCreateagentid(String createagentid) {
        this.createagentid = createagentid;
        return this;
    }

    public String getUpdateagentid() {
        return updateagentid;
    }

    public Formconf setUpdateagentid(String updateagentid) {
        this.updateagentid = updateagentid;
        return this;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public Formconf setCreatetime(Date createtime) {
        this.createtime = createtime;
        return this;
    }

    public Date getUpdatetime() {
        return updatetime;
    }

    public Formconf setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
        return this;
    }

    public String getSysmemo() {
        return sysmemo;
    }

    public Formconf setSysmemo(String sysmemo) {
        this.sysmemo = sysmemo;
        return this;
    }

    @Override
    protected Serializable pkVal() {
        return this.serno;
    }

    @Override
    public String toString() {
        return "Formconf{" +
        "serno=" + serno +
        ", formid=" + formid +
        ", formname=" + formname +
        ", formurl=" + formurl +
        ", sysid=" + sysid +
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
