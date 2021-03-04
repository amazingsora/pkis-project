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
 * @since 2020-07-29
 */
@TableName("REVIEWSETDATACONF")
public class Reviewsetdataconf extends Model<Reviewsetdataconf> {

    private static final long serialVersionUID=1L;

    @TableField("SERNO")
    private Long serno;

    /**
     * 流程編碼
     */
    @TableField("FLOWID")
    private String flowid;

    /**
     * 排序
     */
    @TableField("SEQUENCE")
    private Long sequence;

    @TableField("REVIEWNAME")
    private String reviewname;

    /**
     * 可簽審使用者
     */
    @TableField("REVIEWUSERIDS")
    private String reviewuserids;

    /**
     * 可簽審角色
     */
    @TableField("REVIEWROLES")
    private String reviewroles;

    /**
     * 用途 00:簽核 01:會簽
     */
    @TableField("ACTIONTYPE")
    private String actiontype;

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

    @TableField("FLOWVERSION")
    private String flowversion;


    public Long getSerno() {
        return serno;
    }

    public Reviewsetdataconf setSerno(Long serno) {
        this.serno = serno;
        return this;
    }

    public String getFlowid() {
        return flowid;
    }

    public Reviewsetdataconf setFlowid(String flowid) {
        this.flowid = flowid;
        return this;
    }

    public Long getSequence() {
        return sequence;
    }

    public Reviewsetdataconf setSequence(Long sequence) {
        this.sequence = sequence;
        return this;
    }

    public String getReviewname() {
        return reviewname;
    }

    public Reviewsetdataconf setReviewname(String reviewname) {
        this.reviewname = reviewname;
        return this;
    }

    public String getReviewuserids() {
        return reviewuserids;
    }

    public Reviewsetdataconf setReviewuserids(String reviewuserids) {
        this.reviewuserids = reviewuserids;
        return this;
    }

    public String getReviewroles() {
        return reviewroles;
    }

    public Reviewsetdataconf setReviewroles(String reviewroles) {
        this.reviewroles = reviewroles;
        return this;
    }

    public String getActiontype() {
        return actiontype;
    }

    public Reviewsetdataconf setActiontype(String actiontype) {
        this.actiontype = actiontype;
        return this;
    }

    public String getCreateuser() {
        return createuser;
    }

    public Reviewsetdataconf setCreateuser(String createuser) {
        this.createuser = createuser;
        return this;
    }

    public Date getCreatedate() {
        return createdate;
    }

    public Reviewsetdataconf setCreatedate(Date createdate) {
        this.createdate = createdate;
        return this;
    }

    public String getUpdateuser() {
        return updateuser;
    }

    public Reviewsetdataconf setUpdateuser(String updateuser) {
        this.updateuser = updateuser;
        return this;
    }

    public Date getUpdatedate() {
        return updatedate;
    }

    public Reviewsetdataconf setUpdatedate(Date updatedate) {
        this.updatedate = updatedate;
        return this;
    }

    public String getFlowversion() {
        return flowversion;
    }

    public Reviewsetdataconf setFlowversion(String flowversion) {
        this.flowversion = flowversion;
        return this;
    }

    @Override
    protected Serializable pkVal() {
        return null;
    }

    @Override
    public String toString() {
        return "Reviewsetdataconf{" +
        "serno=" + serno +
        ", flowid=" + flowid +
        ", sequence=" + sequence +
        ", reviewname=" + reviewname +
        ", reviewuserids=" + reviewuserids +
        ", reviewroles=" + reviewroles +
        ", actiontype=" + actiontype +
        ", createuser=" + createuser +
        ", createdate=" + createdate +
        ", updateuser=" + updateuser +
        ", updatedate=" + updatedate +
        ", flowversion=" + flowversion +
        "}";
    }
}
