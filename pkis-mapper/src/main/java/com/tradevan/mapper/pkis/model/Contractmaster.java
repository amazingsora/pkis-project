package com.tradevan.mapper.pkis.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author system
 * @since 2020-08-07
 */
@TableName("CONTRACTMASTER")
public class Contractmaster extends Model<Contractmaster> {

    private static final long serialVersionUID=1L;

    /**
     * 合約編號
     */
    @TableField("DATAID")
    private String dataid;

    /**
     * 流程編碼
     */
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

    @TableField("INDEXNAME")
    private String indexname;

    @TableField("CONTRACTMODEL")
    private String contractmodel;
    
    @TableField("SENDDATE")
    private Date senddate;

    public String getDataid() {
        return dataid;
    }

    public Contractmaster setDataid(String dataid) {
        this.dataid = dataid;
        return this;
    }

    public String getFlowid() {
        return flowid;
    }

    public Contractmaster setFlowid(String flowid) {
        this.flowid = flowid;
        return this;
    }

    public String getJson() {
        return json;
    }

    public Contractmaster setJson(String json) {
        this.json = json;
        return this;
    }

    public String getCreateuser() {
        return createuser;
    }

    public Contractmaster setCreateuser(String createuser) {
        this.createuser = createuser;
        return this;
    }

    public Date getCreatedate() {
        return createdate;
    }

    public Contractmaster setCreatedate(Date createdate) {
        this.createdate = createdate;
        return this;
    }

    public String getUpdateuser() {
        return updateuser;
    }

    public Contractmaster setUpdateuser(String updateuser) {
        this.updateuser = updateuser;
        return this;
    }

    public Date getUpdatedate() {
        return updatedate;
    }

    public Contractmaster setUpdatedate(Date updatedate) {
        this.updatedate = updatedate;
        return this;
    }

    public String getIndexname() {
        return indexname;
    }

    public Contractmaster setIndexname(String indexname) {
        this.indexname = indexname;
        return this;
    }

    public String getContractmodel() {
        return contractmodel;
    }

    public Contractmaster setContractmodel(String contractmodel) {
        this.contractmodel = contractmodel;
        return this;
    }
    
    public Date getSendDate() {
        return senddate;
    }

    public Contractmaster setSendDate(Date senddate) {
        this.senddate = senddate;
        return this;
    }

    @Override
    protected Serializable pkVal() {
        return null;
    }

    @Override
    public String toString() {
        return "Contractmaster{" +
        "dataid=" + dataid +
        ", flowid=" + flowid +
        ", json=" + json +
        ", createuser=" + createuser +
        ", createdate=" + createdate +
        ", updateuser=" + updateuser +
        ", updatedate=" + updatedate +
        ", indexname=" + indexname +
        ", senddate=" + senddate +
        ", contractmodel=" + contractmodel +
        "}";
    }
}
