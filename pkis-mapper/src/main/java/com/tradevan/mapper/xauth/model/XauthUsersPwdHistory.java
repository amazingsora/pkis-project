package com.tradevan.mapper.xauth.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

/**
 * <p>
 * 使用者密碼歷史檔
 * </p>
 *
 * @author penny
 * @since 2020-03-02
 */
@TableName("XAUTH_USERS_PWD_HISTORY")
public class XauthUsersPwdHistory extends Model<XauthUsersPwdHistory> {

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
     * 使用者密碼
     */
    @TableField("USER_PW")
    private String userPw;

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

    public XauthUsersPwdHistory setAppId(String appId) {
        this.appId = appId;
        return this;
    }

    public String getIdenId() {
        return idenId;
    }

    public XauthUsersPwdHistory setIdenId(String idenId) {
        this.idenId = idenId;
        return this;
    }

    public String getUserId() {
        return userId;
    }

    public XauthUsersPwdHistory setUserId(String userId) {
        this.userId = userId;
        return this;
    }

    public String getUserPw() {
        return userPw;
    }

    public XauthUsersPwdHistory setUserPw(String userPw) {
        this.userPw = userPw;
        return this;
    }

    public String getCreUser() {
        return creUser;
    }

    public XauthUsersPwdHistory setCreUser(String creUser) {
        this.creUser = creUser;
        return this;
    }

    public Date getCreDate() {
        return creDate;
    }

    public XauthUsersPwdHistory setCreDate(Date creDate) {
        this.creDate = creDate;
        return this;
    }

    public String getUpdUser() {
        return updUser;
    }

    public XauthUsersPwdHistory setUpdUser(String updUser) {
        this.updUser = updUser;
        return this;
    }

    public Date getUpdDate() {
        return updDate;
    }

    public XauthUsersPwdHistory setUpdDate(Date updDate) {
        this.updDate = updDate;
        return this;
    }

    @Override
    protected Serializable pkVal() {
        return this.appId;
    }

    @Override
    public String toString() {
        return "XauthUsersPwdHistory{" +
        "appId=" + appId +
        ", idenId=" + idenId +
        ", userId=" + userId +
        ", userPw=" + userPw +
        ", creUser=" + creUser +
        ", creDate=" + creDate +
        ", updUser=" + updUser +
        ", updDate=" + updDate +
        "}";
    }
}
