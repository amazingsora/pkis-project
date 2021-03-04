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
@TableName("USERPROJROLE")
public class Userprojrole extends Model<Userprojrole> {

    private static final long serialVersionUID=1L;

    @TableId("SERNO")
    private Long serno;

    @TableField("USERID")
    private String userid;

    @TableField("PROJID")
    private String projid;

    @TableField("ROLEID")
    private String roleid;

    @TableField("USERTYPE")
    private String usertype;

    @TableField("CREATEAGENTID")
    private String createagentid;

    @TableField("CREATEUSERID")
    private String createuserid;

    @TableField("CREATETIME")
    private Date createtime;

    @TableField("SYSMEMO")
    private String sysmemo;


    public Long getSerno() {
        return serno;
    }

    public Userprojrole setSerno(Long serno) {
        this.serno = serno;
        return this;
    }

    public String getUserid() {
        return userid;
    }

    public Userprojrole setUserid(String userid) {
        this.userid = userid;
        return this;
    }

    public String getProjid() {
        return projid;
    }

    public Userprojrole setProjid(String projid) {
        this.projid = projid;
        return this;
    }

    public String getRoleid() {
        return roleid;
    }

    public Userprojrole setRoleid(String roleid) {
        this.roleid = roleid;
        return this;
    }

    public String getUsertype() {
        return usertype;
    }

    public Userprojrole setUsertype(String usertype) {
        this.usertype = usertype;
        return this;
    }

    public String getCreateagentid() {
        return createagentid;
    }

    public Userprojrole setCreateagentid(String createagentid) {
        this.createagentid = createagentid;
        return this;
    }

    public String getCreateuserid() {
        return createuserid;
    }

    public Userprojrole setCreateuserid(String createuserid) {
        this.createuserid = createuserid;
        return this;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public Userprojrole setCreatetime(Date createtime) {
        this.createtime = createtime;
        return this;
    }

    public String getSysmemo() {
        return sysmemo;
    }

    public Userprojrole setSysmemo(String sysmemo) {
        this.sysmemo = sysmemo;
        return this;
    }

    @Override
    protected Serializable pkVal() {
        return this.serno;
    }

    @Override
    public String toString() {
        return "Userprojrole{" +
        "serno=" + serno +
        ", userid=" + userid +
        ", projid=" + projid +
        ", roleid=" + roleid +
        ", usertype=" + usertype +
        ", createagentid=" + createagentid +
        ", createuserid=" + createuserid +
        ", createtime=" + createtime +
        ", sysmemo=" + sysmemo +
        "}";
    }
}
