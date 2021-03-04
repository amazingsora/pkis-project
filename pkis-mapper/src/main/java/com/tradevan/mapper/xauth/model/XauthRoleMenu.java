package com.tradevan.mapper.xauth.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

/**
 * <p>
 * 角色選單資料檔
 * </p>
 *
 * @author penny
 * @since 2020-03-02
 */
@TableName("XAUTH_ROLE_MENU")
public class XauthRoleMenu extends Model<XauthRoleMenu> {

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
     * 選單編號
     */
    @TableField("MENU_ID")
    private String menuId;

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

    public XauthRoleMenu setAppId(String appId) {
        this.appId = appId;
        return this;
    }

    public String getIdenId() {
        return idenId;
    }

    public XauthRoleMenu setIdenId(String idenId) {
        this.idenId = idenId;
        return this;
    }

    public String getRoleId() {
        return roleId;
    }

    public XauthRoleMenu setRoleId(String roleId) {
        this.roleId = roleId;
        return this;
    }

    public String getMenuId() {
        return menuId;
    }

    public XauthRoleMenu setMenuId(String menuId) {
        this.menuId = menuId;
        return this;
    }

    public String getCreUser() {
        return creUser;
    }

    public XauthRoleMenu setCreUser(String creUser) {
        this.creUser = creUser;
        return this;
    }

    public Date getCreDate() {
        return creDate;
    }

    public XauthRoleMenu setCreDate(Date creDate) {
        this.creDate = creDate;
        return this;
    }

    public String getUpdUser() {
        return updUser;
    }

    public XauthRoleMenu setUpdUser(String updUser) {
        this.updUser = updUser;
        return this;
    }

    public Date getUpdDate() {
        return updDate;
    }

    public XauthRoleMenu setUpdDate(Date updDate) {
        this.updDate = updDate;
        return this;
    }

    @Override
    protected Serializable pkVal() {
        return this.appId;
    }

    @Override
    public String toString() {
        return "XauthRoleMenu{" +
        "appId=" + appId +
        ", idenId=" + idenId +
        ", roleId=" + roleId +
        ", menuId=" + menuId +
        ", creUser=" + creUser +
        ", creDate=" + creDate +
        ", updUser=" + updUser +
        ", updDate=" + updDate +
        "}";
    }
}
