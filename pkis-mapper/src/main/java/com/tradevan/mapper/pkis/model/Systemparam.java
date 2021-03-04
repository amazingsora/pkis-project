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
 * @since 2020-09-23
 */
@TableName("SYSTEMPARAM")
public class Systemparam extends Model<Systemparam> {

    private static final long serialVersionUID=1L;

    @TableField("APPID")
    private String appid;

    /**
     * 合約模式
     */
    @TableField("CONTRACTMODEL")
    private String contractmodel;

    /**
     * 流程編號
     */
    @TableField("FLOWID")
    private String flowid;

    /**
     * 課別
     */
    @TableField("DEPTNO")
    private String deptno;

    /**
     * 使用者帳號
     */
    @TableField("USERIDS")
    private String userids;

    /**
     * 角色名稱
     */
    @TableField("ROLENAME")
    private String rolename;

    /**
     * 角色代碼
     */
    @TableField("ROLEIDS")
    private String roleids;

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

    /**
     * 流程版次
     */
    @TableField("ACTIONTYPE")
    private String actiontype;

    @TableField("IDENID")
    private String idenid;

    @TableField("FLOWNAME")
    private String flowname;

    @TableField("IDENIDS")
    private String idenids;

    @TableField("SERNO")
    private Long serno;


    public String getAppid() {
        return appid;
    }

    public Systemparam setAppid(String appid) {
        this.appid = appid;
        return this;
    }

    public String getContractmodel() {
        return contractmodel;
    }

    public Systemparam setContractmodel(String contractmodel) {
        this.contractmodel = contractmodel;
        return this;
    }

    public String getFlowid() {
        return flowid;
    }

    public Systemparam setFlowid(String flowid) {
        this.flowid = flowid;
        return this;
    }

    public String getDeptno() {
        return deptno;
    }

    public Systemparam setDeptno(String deptno) {
        this.deptno = deptno;
        return this;
    }

    public String getUserids() {
        return userids;
    }

    public Systemparam setUserids(String userids) {
        this.userids = userids;
        return this;
    }

    public String getRolename() {
        return rolename;
    }

    public Systemparam setRolename(String rolename) {
        this.rolename = rolename;
        return this;
    }

    public String getRoleids() {
        return roleids;
    }

    public Systemparam setRoleids(String roleids) {
        this.roleids = roleids;
        return this;
    }

    public String getCreateuser() {
        return createuser;
    }

    public Systemparam setCreateuser(String createuser) {
        this.createuser = createuser;
        return this;
    }

    public Date getCreatedate() {
        return createdate;
    }

    public Systemparam setCreatedate(Date createdate) {
        this.createdate = createdate;
        return this;
    }

    public String getUpdateuser() {
        return updateuser;
    }

    public Systemparam setUpdateuser(String updateuser) {
        this.updateuser = updateuser;
        return this;
    }

    public Date getUpdatedate() {
        return updatedate;
    }

    public Systemparam setUpdatedate(Date updatedate) {
        this.updatedate = updatedate;
        return this;
    }

    public String getActiontype() {
        return actiontype;
    }

    public Systemparam setActiontype(String actiontype) {
        this.actiontype = actiontype;
        return this;
    }

    public String getIdenid() {
        return idenid;
    }

    public Systemparam setIdenid(String idenid) {
        this.idenid = idenid;
        return this;
    }

    public String getFlowname() {
        return flowname;
    }

    public Systemparam setFlowname(String flowname) {
        this.flowname = flowname;
        return this;
    }

    public String getIdenids() {
        return idenids;
    }

    public Systemparam setIdenids(String idenids) {
        this.idenids = idenids;
        return this;
    }

    public Long getSerno() {
        return serno;
    }

    public Systemparam setSerno(Long serno) {
        this.serno = serno;
        return this;
    }

    @Override
    protected Serializable pkVal() {
        return null;
    }

    @Override
    public String toString() {
        return "Systemparam{" +
        "appid=" + appid +
        ", contractmodel=" + contractmodel +
        ", flowid=" + flowid +
        ", deptno=" + deptno +
        ", userids=" + userids +
        ", rolename=" + rolename +
        ", roleids=" + roleids +
        ", createuser=" + createuser +
        ", createdate=" + createdate +
        ", updateuser=" + updateuser +
        ", updatedate=" + updatedate +
        ", actiontype=" + actiontype +
        ", idenid=" + idenid +
        ", flowname=" + flowname +
        ", idenids=" + idenids +
        ", serno=" + serno +
        "}";
    }
}
