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
@TableName("USERDEPTROLE")
public class Userdeptrole extends Model<Userdeptrole> {

    private static final long serialVersionUID=1L;

    @TableId("SERNO")
    private Long serno;

    @TableField("USERID")
    private String userid;

    @TableField("DEPTID")
    private String deptid;

    @TableField("ROLEID")
    private String roleid;

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

    public Userdeptrole setSerno(Long serno) {
        this.serno = serno;
        return this;
    }

    public String getUserid() {
        return userid;
    }

    public Userdeptrole setUserid(String userid) {
        this.userid = userid;
        return this;
    }

    public String getDeptid() {
        return deptid;
    }

    public Userdeptrole setDeptid(String deptid) {
        this.deptid = deptid;
        return this;
    }

    public String getRoleid() {
        return roleid;
    }

    public Userdeptrole setRoleid(String roleid) {
        this.roleid = roleid;
        return this;
    }

    public String getCreateagentid() {
        return createagentid;
    }

    public Userdeptrole setCreateagentid(String createagentid) {
        this.createagentid = createagentid;
        return this;
    }

    public String getCreateuserid() {
        return createuserid;
    }

    public Userdeptrole setCreateuserid(String createuserid) {
        this.createuserid = createuserid;
        return this;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public Userdeptrole setCreatetime(Date createtime) {
        this.createtime = createtime;
        return this;
    }

    public String getSysmemo() {
        return sysmemo;
    }

    public Userdeptrole setSysmemo(String sysmemo) {
        this.sysmemo = sysmemo;
        return this;
    }

    @Override
    protected Serializable pkVal() {
        return this.serno;
    }

    @Override
    public String toString() {
        return "Userdeptrole{" +
        "serno=" + serno +
        ", userid=" + userid +
        ", deptid=" + deptid +
        ", roleid=" + roleid +
        ", createagentid=" + createagentid +
        ", createuserid=" + createuserid +
        ", createtime=" + createtime +
        ", sysmemo=" + sysmemo +
        "}";
    }
}
