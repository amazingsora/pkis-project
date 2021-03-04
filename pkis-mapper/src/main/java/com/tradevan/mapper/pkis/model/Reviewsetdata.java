package com.tradevan.mapper.pkis.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
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
@TableName("REVIEWSETDATA")
public class Reviewsetdata extends Model<Reviewsetdata> {

    private static final long serialVersionUID=1L;

    @TableField("SERNO")
    private Long serno;

    @TableField("REVIEWNAME")
    private String reviewname;

    @TableField("REVIEWUSERIDS")
    private String reviewuserids;

    @TableField("REVIEWROLES")
    private String reviewroles;

    @TableField("ACTIONTYPE")
    private String actiontype;


    public Long getSerno() {
        return serno;
    }

    public Reviewsetdata setSerno(Long serno) {
        this.serno = serno;
        return this;
    }

    public String getReviewname() {
        return reviewname;
    }

    public Reviewsetdata setReviewname(String reviewname) {
        this.reviewname = reviewname;
        return this;
    }

    public String getReviewuserids() {
        return reviewuserids;
    }

    public Reviewsetdata setReviewuserids(String reviewuserids) {
        this.reviewuserids = reviewuserids;
        return this;
    }

    public String getReviewroles() {
        return reviewroles;
    }

    public Reviewsetdata setReviewroles(String reviewroles) {
        this.reviewroles = reviewroles;
        return this;
    }

    public String getActiontype() {
        return actiontype;
    }

    public Reviewsetdata setActiontype(String actiontype) {
        this.actiontype = actiontype;
        return this;
    }

    @Override
    protected Serializable pkVal() {
        return null;
    }

    @Override
    public String toString() {
        return "Reviewsetdata{" +
        "serno=" + serno +
        ", reviewname=" + reviewname +
        ", reviewuserids=" + reviewuserids +
        ", reviewroles=" + reviewroles +
        ", actiontype=" + actiontype +
        "}";
    }
}
