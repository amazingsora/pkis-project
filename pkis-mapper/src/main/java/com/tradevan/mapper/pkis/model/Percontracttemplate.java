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
 * @author system
 * @since 2020-10-12
 */
@TableName("PERCONTRACTTEMPLATE")
public class Percontracttemplate extends Model<Percontracttemplate> {

    private static final long serialVersionUID=1L;

    @TableId("DATAID")
    private String dataid;

    @TableField("FLOWID")
    private String flowid;

    @TableField("JSON")
    private String json;

    @TableField("CREATEUSER")
    private String createuser;

    @TableField("CREATEDATE")
    private Date createdate;

    @TableField("UPDATEUSER")
    private String updateuser;

    @TableField("UPDATEDATE")
    private Date updatedate;

    @TableField("MODULE")
    private String module;

    @TableField("TEMPLATENAME")
    private String templatename;

    @TableField("PRDTIME")
    private Date prdtime;

    @TableField("DROPTIME")
    private Date droptime;

    @TableField("DOCVER")
    private String docver;


    public String getDataid() {
        return dataid;
    }

    public Percontracttemplate setDataid(String dataid) {
        this.dataid = dataid;
        return this;
    }

    public String getFlowid() {
        return flowid;
    }

    public Percontracttemplate setFlowid(String flowid) {
        this.flowid = flowid;
        return this;
    }

    public String getJson() {
        return json;
    }

    public Percontracttemplate setJson(String json) {
        this.json = json;
        return this;
    }

    public String getCreateuser() {
        return createuser;
    }

    public Percontracttemplate setCreateuser(String createuser) {
        this.createuser = createuser;
        return this;
    }

    public Date getCreatedate() {
        return createdate;
    }

    public Percontracttemplate setCreatedate(Date createdate) {
        this.createdate = createdate;
        return this;
    }

    public String getUpdateuser() {
        return updateuser;
    }

    public Percontracttemplate setUpdateuser(String updateuser) {
        this.updateuser = updateuser;
        return this;
    }

    public Date getUpdatedate() {
        return updatedate;
    }

    public Percontracttemplate setUpdatedate(Date updatedate) {
        this.updatedate = updatedate;
        return this;
    }

    public String getModule() {
        return module;
    }

    public Percontracttemplate setModule(String module) {
        this.module = module;
        return this;
    }

    public String getTemplatename() {
        return templatename;
    }

    public Percontracttemplate setTemplatename(String templatename) {
        this.templatename = templatename;
        return this;
    }

    public Date getPrdtime() {
        return prdtime;
    }

    public Percontracttemplate setPrdtime(Date prdtime) {
        this.prdtime = prdtime;
        return this;
    }

    public Date getDroptime() {
        return droptime;
    }

    public Percontracttemplate setDroptime(Date droptime) {
        this.droptime = droptime;
        return this;
    }

    public String getDocver() {
        return docver;
    }

    public Percontracttemplate setDocver(String docver) {
        this.docver = docver;
        return this;
    }

    @Override
    protected Serializable pkVal() {
        return this.dataid;
    }

    @Override
    public String toString() {
        return "Percontracttemplate{" +
        "dataid=" + dataid +
        ", flowid=" + flowid +
        ", json=" + json +
        ", createuser=" + createuser +
        ", createdate=" + createdate +
        ", updateuser=" + updateuser +
        ", updatedate=" + updatedate +
        ", module=" + module +
        ", templatename=" + templatename +
        ", prdtime=" + prdtime +
        ", droptime=" + droptime +
        ", docver=" + docver +
        "}";
    }
}
