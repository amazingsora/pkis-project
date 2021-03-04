package com.tradevan.mapper.xauth.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

/**
 * <p>
 * 使用者登入識別檔
 * </p>
 *
 * @author penny
 * @since 2020-03-02
 */
@TableName("XAUTH_USERS_TOKEN")
public class XauthUsersToken extends Model<XauthUsersToken> {

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
     * 使用者帳號
     */
    @TableField("USER_ID")
    private String userId;

    /**
     * TOKEN
     */
    @TableField("TOKEN")
    private String token;

    /**
     * 啟用日期
     */
    @TableField("ACTIVE_DATE")
    private Date activeDate;

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

    public XauthUsersToken setAppId(String appId) {
        this.appId = appId;
        return this;
    }

    public String getIdenId() {
        return idenId;
    }

    public XauthUsersToken setIdenId(String idenId) {
        this.idenId = idenId;
        return this;
    }

    public String getUserId() {
        return userId;
    }

    public XauthUsersToken setUserId(String userId) {
        this.userId = userId;
        return this;
    }

    public String getToken() {
        return token;
    }

    public XauthUsersToken setToken(String token) {
        this.token = token;
        return this;
    }

    public Date getActiveDate() {
        return activeDate;
    }

    public XauthUsersToken setActiveDate(Date activeDate) {
        this.activeDate = activeDate;
        return this;
    }

    public String getCreUser() {
        return creUser;
    }

    public XauthUsersToken setCreUser(String creUser) {
        this.creUser = creUser;
        return this;
    }

    public Date getCreDate() {
        return creDate;
    }

    public XauthUsersToken setCreDate(Date creDate) {
        this.creDate = creDate;
        return this;
    }

    public String getUpdUser() {
        return updUser;
    }

    public XauthUsersToken setUpdUser(String updUser) {
        this.updUser = updUser;
        return this;
    }

    public Date getUpdDate() {
        return updDate;
    }

    public XauthUsersToken setUpdDate(Date updDate) {
        this.updDate = updDate;
        return this;
    }

    @Override
    protected Serializable pkVal() {
        return this.appId;
    }

    @Override
    public String toString() {
        return "XauthUsersToken{" +
        "appId=" + appId +
        ", idenId=" + idenId +
        ", userId=" + userId +
        ", token=" + token +
        ", activeDate=" + activeDate +
        ", creUser=" + creUser +
        ", creDate=" + creDate +
        ", updUser=" + updUser +
        ", updDate=" + updDate +
        "}";
    }
}
