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
 * @since 2020-12-29
 */
@TableName("CONTRACTREVIEW")
public class Contractreview extends Model<Contractreview> {

    private static final long serialVersionUID=1L;

    @TableField("DATAID")
    private String dataid;

    @TableField("STEPID")
    private String stepid;

    @TableField("REVIEWNOTE")
    private String reviewnote;

    @TableField("CREATEUSER")
    private String createuser;

    @TableField("CREATEDATE")
    private Date createdate;

    @TableField("UPDATEUSER")
    private String updateuser;

    @TableField("UPDATEDATE")
    private Date updatedate;

    @TableField("REVIEWRLT")
    private String reviewrlt;

    @TableField("REVIEWUSER")
    private String reviewuser;

    @TableField("STEPNAME")
    private String stepname;

    @TableField("USERIDS")
    private String userids;


    public String getDataid() {
        return dataid;
    }

    public Contractreview setDataid(String dataid) {
        this.dataid = dataid;
        return this;
    }

    public String getStepid() {
        return stepid;
    }

    public Contractreview setStepid(String stepid) {
        this.stepid = stepid;
        return this;
    }

    public String getReviewnote() {
        return reviewnote;
    }

    public Contractreview setReviewnote(String reviewnote) {
        this.reviewnote = reviewnote;
        return this;
    }

    public String getCreateuser() {
        return createuser;
    }

    public Contractreview setCreateuser(String createuser) {
        this.createuser = createuser;
        return this;
    }

    public Date getCreatedate() {
        return createdate;
    }

    public Contractreview setCreatedate(Date createdate) {
        this.createdate = createdate;
        return this;
    }

    public String getUpdateuser() {
        return updateuser;
    }

    public Contractreview setUpdateuser(String updateuser) {
        this.updateuser = updateuser;
        return this;
    }

    public Date getUpdatedate() {
        return updatedate;
    }

    public Contractreview setUpdatedate(Date updatedate) {
        this.updatedate = updatedate;
        return this;
    }

    public String getReviewrlt() {
        return reviewrlt;
    }

    public Contractreview setReviewrlt(String reviewrlt) {
        this.reviewrlt = reviewrlt;
        return this;
    }

    public String getReviewuser() {
        return reviewuser;
    }

    public Contractreview setReviewuser(String reviewuser) {
        this.reviewuser = reviewuser;
        return this;
    }

    public String getStepname() {
        return stepname;
    }

    public Contractreview setStepname(String stepname) {
        this.stepname = stepname;
        return this;
    }

    public String getUserids() {
        return userids;
    }

    public Contractreview setUserids(String userids) {
        this.userids = userids;
        return this;
    }

    @Override
    protected Serializable pkVal() {
        return null;
    }

    @Override
    public String toString() {
        return "Contractreview{" +
        "dataid=" + dataid +
        ", stepid=" + stepid +
        ", reviewnote=" + reviewnote +
        ", createuser=" + createuser +
        ", createdate=" + createdate +
        ", updateuser=" + updateuser +
        ", updatedate=" + updatedate +
        ", reviewrlt=" + reviewrlt +
        ", reviewuser=" + reviewuser +
        ", stepname=" + stepname +
        ", userids=" + userids +
        "}";
    }
}
