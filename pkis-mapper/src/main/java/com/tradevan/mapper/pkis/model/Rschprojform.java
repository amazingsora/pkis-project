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
@TableName("RSCHPROJFORM")
public class Rschprojform extends Model<Rschprojform> {

    private static final long serialVersionUID=1L;

    @TableId("SERNO")
    private Long serno;

    @TableField("SOURCESERNO")
    private Long sourceserno;

    @TableField("PROJID")
    private String projid;

    @TableField("FORMID")
    private String formid;

    @TableField("APPLYNO")
    private String applyno;

    @TableField("DOCSTATESERNO")
    private Long docstateserno;

    @TableField("CREATEUSERID")
    private String createuserid;

    @TableField("UPDATEUSERID")
    private String updateuserid;

    @TableField("CREATETIME")
    private Date createtime;

    @TableField("UPDATETIME")
    private Date updatetime;

    @TableField("SYSMEMO")
    private String sysmemo;

    @TableField("CREATEAGENTID")
    private String createagentid;

    @TableField("UPDATEAGENTID")
    private String updateagentid;

    @TableField("CHANGEREASON")
    private String changereason;


    public Long getSerno() {
        return serno;
    }

    public Rschprojform setSerno(Long serno) {
        this.serno = serno;
        return this;
    }

    public Long getSourceserno() {
        return sourceserno;
    }

    public Rschprojform setSourceserno(Long sourceserno) {
        this.sourceserno = sourceserno;
        return this;
    }

    public String getProjid() {
        return projid;
    }

    public Rschprojform setProjid(String projid) {
        this.projid = projid;
        return this;
    }

    public String getFormid() {
        return formid;
    }

    public Rschprojform setFormid(String formid) {
        this.formid = formid;
        return this;
    }

    public String getApplyno() {
        return applyno;
    }

    public Rschprojform setApplyno(String applyno) {
        this.applyno = applyno;
        return this;
    }

    public Long getDocstateserno() {
        return docstateserno;
    }

    public Rschprojform setDocstateserno(Long docstateserno) {
        this.docstateserno = docstateserno;
        return this;
    }

    public String getCreateuserid() {
        return createuserid;
    }

    public Rschprojform setCreateuserid(String createuserid) {
        this.createuserid = createuserid;
        return this;
    }

    public String getUpdateuserid() {
        return updateuserid;
    }

    public Rschprojform setUpdateuserid(String updateuserid) {
        this.updateuserid = updateuserid;
        return this;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public Rschprojform setCreatetime(Date createtime) {
        this.createtime = createtime;
        return this;
    }

    public Date getUpdatetime() {
        return updatetime;
    }

    public Rschprojform setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
        return this;
    }

    public String getSysmemo() {
        return sysmemo;
    }

    public Rschprojform setSysmemo(String sysmemo) {
        this.sysmemo = sysmemo;
        return this;
    }

    public String getCreateagentid() {
        return createagentid;
    }

    public Rschprojform setCreateagentid(String createagentid) {
        this.createagentid = createagentid;
        return this;
    }

    public String getUpdateagentid() {
        return updateagentid;
    }

    public Rschprojform setUpdateagentid(String updateagentid) {
        this.updateagentid = updateagentid;
        return this;
    }

    public String getChangereason() {
        return changereason;
    }

    public Rschprojform setChangereason(String changereason) {
        this.changereason = changereason;
        return this;
    }

    @Override
    protected Serializable pkVal() {
        return this.serno;
    }

    @Override
    public String toString() {
        return "Rschprojform{" +
        "serno=" + serno +
        ", sourceserno=" + sourceserno +
        ", projid=" + projid +
        ", formid=" + formid +
        ", applyno=" + applyno +
        ", docstateserno=" + docstateserno +
        ", createuserid=" + createuserid +
        ", updateuserid=" + updateuserid +
        ", createtime=" + createtime +
        ", updatetime=" + updatetime +
        ", sysmemo=" + sysmemo +
        ", createagentid=" + createagentid +
        ", updateagentid=" + updateagentid +
        ", changereason=" + changereason +
        "}";
    }
}
