package com.tradevan.mapper.xauth.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

/**
 * <p>
 * 使用者密鑰檔
 * </p>
 *
 * @author penny
 * @since 2020-03-02
 */
@TableName("XAUTH_USERS_SECRET")
public class XauthUsersSecret extends Model<XauthUsersSecret> {

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
     * 到期日期
     */
    @TableField("EXPIRE_DATE")
    private Date expireDate;

    /**
     * 公鑰
     */
    @TableField("PUBLIC_KEY")
    private String publicKey;

    /**
     * 私鑰
     */
    @TableField("PRIVATE_KEY")
    private String privateKey;

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

    public XauthUsersSecret setAppId(String appId) {
        this.appId = appId;
        return this;
    }

    public String getIdenId() {
        return idenId;
    }

    public XauthUsersSecret setIdenId(String idenId) {
        this.idenId = idenId;
        return this;
    }

    public String getUserId() {
        return userId;
    }

    public XauthUsersSecret setUserId(String userId) {
        this.userId = userId;
        return this;
    }

    public String getToken() {
        return token;
    }

    public XauthUsersSecret setToken(String token) {
        this.token = token;
        return this;
    }

    public Date getExpireDate() {
        return expireDate;
    }

    public XauthUsersSecret setExpireDate(Date expireDate) {
        this.expireDate = expireDate;
        return this;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public XauthUsersSecret setPublicKey(String publicKey) {
        this.publicKey = publicKey;
        return this;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public XauthUsersSecret setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
        return this;
    }

    public String getCreUser() {
        return creUser;
    }

    public XauthUsersSecret setCreUser(String creUser) {
        this.creUser = creUser;
        return this;
    }

    public Date getCreDate() {
        return creDate;
    }

    public XauthUsersSecret setCreDate(Date creDate) {
        this.creDate = creDate;
        return this;
    }

    public String getUpdUser() {
        return updUser;
    }

    public XauthUsersSecret setUpdUser(String updUser) {
        this.updUser = updUser;
        return this;
    }

    public Date getUpdDate() {
        return updDate;
    }

    public XauthUsersSecret setUpdDate(Date updDate) {
        this.updDate = updDate;
        return this;
    }

    @Override
    protected Serializable pkVal() {
        return this.appId;
    }

    @Override
    public String toString() {
        return "XauthUsersSecret{" +
        "appId=" + appId +
        ", idenId=" + idenId +
        ", userId=" + userId +
        ", token=" + token +
        ", expireDate=" + expireDate +
        ", publicKey=" + publicKey +
        ", privateKey=" + privateKey +
        ", creUser=" + creUser +
        ", creDate=" + creDate +
        ", updUser=" + updUser +
        ", updDate=" + updDate +
        "}";
    }
}
