package com.tradevan.mapper.xauth.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

/**
 * <p>
 * 角色資料檔
 * </p>
 *
 * @author penny
 * @since 2020-03-02
 */
@TableName("XAUTH_ROLE")
public class XauthRole extends Model<XauthRole> {

    private static final long serialVersionUID=1L;

    /**
     * 系統代號
     */
    @TableId("APP_ID")
    private String appId;

    /**
     * 識別碼
     */
    @TableField("IDEN_ID")
    private String idenId;

    /**
     * 角色代號
     */
    @TableField("ROLE_ID")
    private String roleId;

    /**
     * 角色名稱
     */
    @TableField("ROLE_CNAME")
    private String roleCname;

    /**
     * 建立人員
     */
    @TableField("CRE_USER")
    private String creUser;

    /**
     * 建立日期
     */
    @TableField("CRE_DATE")
    private Date creDate;

    /**
     * 異動人員
     */
    @TableField("UPD_USER")
    private String updUser;

    /**
     * 異動日期
     */
    @TableField("UPD_DATE")
    private Date updDate;


    public String getAppId() {
        return appId;
    }

    public XauthRole setAppId(String appId) {
        this.appId = appId;
        return this;
    }

    public String getIdenId() {
        return idenId;
    }

    public XauthRole setIdenId(String idenId) {
        this.idenId = idenId;
        return this;
    }

    public String getRoleId() {
        return roleId;
    }

    public XauthRole setRoleId(String roleId) {
        this.roleId = roleId;
        return this;
    }

    public String getRoleCname() {
        return roleCname;
    }

    public XauthRole setRoleCname(String roleCname) {
        this.roleCname = roleCname;
        return this;
    }

    public String getCreUser() {
        return creUser;
    }

    public XauthRole setCreUser(String creUser) {
        this.creUser = creUser;
        return this;
    }

    public Date getCreDate() {
        return creDate;
    }

    public XauthRole setCreDate(Date creDate) {
        this.creDate = creDate;
        return this;
    }

    public String getUpdUser() {
        return updUser;
    }

    public XauthRole setUpdUser(String updUser) {
        this.updUser = updUser;
        return this;
    }

    public Date getUpdDate() {
        return updDate;
    }

    public XauthRole setUpdDate(Date updDate) {
        this.updDate = updDate;
        return this;
    }

    @Override
    protected Serializable pkVal() {
        return this.appId;
    }

    @Override
    public String toString() {
        return "XauthRole{" +
        "appId=" + appId +
        ", idenId=" + idenId +
        ", roleId=" + roleId +
        ", roleCname=" + roleCname +
        ", creUser=" + creUser +
        ", creDate=" + creDate +
        ", updUser=" + updUser +
        ", updDate=" + updDate +
        "}";
    }
}
