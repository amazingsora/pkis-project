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
 * @since 2020-07-30
 */
@TableName("REVIEWCONF")
public class Reviewconf extends Model<Reviewconf> {

    private static final long serialVersionUID=1L;

    /**
     * 流程編號
     */
    @TableField("FLOWID")
    private String flowid;

    /**
     * 流程名稱
     */
    @TableField("FLOWNAME")
    private String flowname;

    /**
     * 合約模式
     */
    @TableField("CONTRACTMODEL")
    private String contractmodel;

    /**
     * 條件
     */
    @TableField("CONDITION")
    private String condition;

    /**
     * 前置流程
     */
    @TableField("ISPREREVIEW")
    private String isprereview;

    /**
     * 單位主管
     */
    @TableField("ISDEPTREVIEW")
    private String isdeptreview;

    /**
     * 組織主管(總經理下一層)
     */
    @TableField("ISORGREVIEW")
    private String isorgreview;

    /**
     * 流程版次
     */
    @TableField("FLOWVERSION")
    private String flowversion;

    /**
     * 狀態
     */
    @TableField("STATUS")
    private String status;

    /**
     * 建立人員
     */
    @TableField("CREATEUSER")
    private String createuser;

    /**
     * 建立時間
     */
    @TableField("CREATEDATE")
    private Date createdate;

    /**
     * 更新人員
     */
    @TableField("UPDATEUSER")
    private String updateuser;

    /**
     * 更新時間
     */
    @TableField("UPDATEDATE")
    private Date updatedate;


    public String getFlowid() {
        return flowid;
    }

    public Reviewconf setFlowid(String flowid) {
        this.flowid = flowid;
        return this;
    }

    public String getFlowname() {
        return flowname;
    }

    public Reviewconf setFlowname(String flowname) {
        this.flowname = flowname;
        return this;
    }

    public String getContractmodel() {
        return contractmodel;
    }

    public Reviewconf setContractmodel(String contractmodel) {
        this.contractmodel = contractmodel;
        return this;
    }

    public String getCondition() {
        return condition;
    }

    public Reviewconf setCondition(String condition) {
        this.condition = condition;
        return this;
    }

    public String getIsprereview() {
        return isprereview;
    }

    public Reviewconf setIsprereview(String isprereview) {
        this.isprereview = isprereview;
        return this;
    }

    public String getIsdeptreview() {
        return isdeptreview;
    }

    public Reviewconf setIsdeptreview(String isdeptreview) {
        this.isdeptreview = isdeptreview;
        return this;
    }

    public String getIsorgreview() {
        return isorgreview;
    }

    public Reviewconf setIsorgreview(String isorgreview) {
        this.isorgreview = isorgreview;
        return this;
    }

    public String getFlowversion() {
        return flowversion;
    }

    public Reviewconf setFlowversion(String flowversion) {
        this.flowversion = flowversion;
        return this;
    }

    public String getStatus() {
        return status;
    }

    public Reviewconf setStatus(String status) {
        this.status = status;
        return this;
    }

    public String getCreateuser() {
        return createuser;
    }

    public Reviewconf setCreateuser(String createuser) {
        this.createuser = createuser;
        return this;
    }

    public Date getCreatedate() {
        return createdate;
    }

    public Reviewconf setCreatedate(Date createdate) {
        this.createdate = createdate;
        return this;
    }

    public String getUpdateuser() {
        return updateuser;
    }

    public Reviewconf setUpdateuser(String updateuser) {
        this.updateuser = updateuser;
        return this;
    }

    public Date getUpdatedate() {
        return updatedate;
    }

    public Reviewconf setUpdatedate(Date updatedate) {
        this.updatedate = updatedate;
        return this;
    }

    @Override
    protected Serializable pkVal() {
        return null;
    }

    @Override
    public String toString() {
        return "Reviewconf{" +
        "flowid=" + flowid +
        ", flowname=" + flowname +
        ", contractmodel=" + contractmodel +
        ", condition=" + condition +
        ", isprereview=" + isprereview +
        ", isdeptreview=" + isdeptreview +
        ", isorgreview=" + isorgreview +
        ", flowversion=" + flowversion +
        ", status=" + status +
        ", createuser=" + createuser +
        ", createdate=" + createdate +
        ", updateuser=" + updateuser +
        ", updatedate=" + updatedate +
        "}";
    }
}
