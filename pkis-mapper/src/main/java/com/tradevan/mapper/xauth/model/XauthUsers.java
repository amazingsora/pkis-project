package com.tradevan.mapper.xauth.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

/**
 * <p>
 * 使用者資料檔
 * </p>
 *
 * @author penny
 * @since 2020-03-02
 */
@TableName("XAUTH_USERS")
public class XauthUsers extends Model<XauthUsers> {

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
     * 使用者姓名
     */
    @TableField("USER_CNAME")
    private String userCname;

    /**
     * 使用者類型 00:系統管理員 01:管理員 02:使用者
     */
    @TableField("USER_TYPE")
    private String userType;

    /**
     * 是否啟用 0:停用 1:啟用
     */
    @TableField("ENABLED")
    private String enabled;

    /**
     * 是否第一次登入 0:否 1:是
     */
    @TableField("IS_FIRST")
    private String isFirst;

    /**
     * 電子郵件
     */
    @TableField("EMAIL")
    private String email;

    /**
     * 電子郵件重新認証識別碼
     */
    @TableField("EMAIL_TOKEN")
    private String emailToken;

    /**
     * 電子郵件重新認証到期日
     */
    @TableField("EMAIL_EXPIRE")
    private Date emailExpire;

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
    
    /**
     * PWDHASH 密碼註解
     */
    @TableField("PWDHASH")
    private String pwdhash;

    

   

	public String getPwdhash() {
		return pwdhash;
	}

	public void setPwdhash(String pwdhash) {
		this.pwdhash = pwdhash;
	}

	public String getAppId() {
        return appId;
    }

    public XauthUsers setAppId(String appId) {
        this.appId = appId;
        return this;
    }

    public String getIdenId() {
        return idenId;
    }

    public XauthUsers setIdenId(String idenId) {
        this.idenId = idenId;
        return this;
    }

    public String getUserId() {
        return userId;
    }

    public XauthUsers setUserId(String userId) {
        this.userId = userId;
        return this;
    }

    public String getUserPw() {
        return userPw;
    }

    public XauthUsers setUserPw(String userPw) {
        this.userPw = userPw;
        return this;
    }

    public String getUserCname() {
        return userCname;
    }

    public XauthUsers setUserCname(String userCname) {
        this.userCname = userCname;
        return this;
    }

    public String getUserType() {
        return userType;
    }

    public XauthUsers setUserType(String userType) {
        this.userType = userType;
        return this;
    }

    public String getEnabled() {
        return enabled;
    }

    public XauthUsers setEnabled(String enabled) {
        this.enabled = enabled;
        return this;
    }

    public String getIsFirst() {
        return isFirst;
    }

    public XauthUsers setIsFirst(String isFirst) {
        this.isFirst = isFirst;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public XauthUsers setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getEmailToken() {
        return emailToken;
    }

    public XauthUsers setEmailToken(String emailToken) {
        this.emailToken = emailToken;
        return this;
    }

    public Date getEmailExpire() {
        return emailExpire;
    }

    public XauthUsers setEmailExpire(Date emailExpire) {
        this.emailExpire = emailExpire;
        return this;
    }

    public String getCreUser() {
        return creUser;
    }

    public XauthUsers setCreUser(String creUser) {
        this.creUser = creUser;
        return this;
    }

    public Date getCreDate() {
        return creDate;
    }

    public XauthUsers setCreDate(Date creDate) {
        this.creDate = creDate;
        return this;
    }

    public String getUpdUser() {
        return updUser;
    }

    public XauthUsers setUpdUser(String updUser) {
        this.updUser = updUser;
        return this;
    }

    public Date getUpdDate() {
        return updDate;
    }

    public XauthUsers setUpdDate(Date updDate) {
        this.updDate = updDate;
        return this;
    }

    @Override
    protected Serializable pkVal() {
        return this.appId;
    }

    @Override
    public String toString() {
        return "XauthUsers{" +
        "appId=" + appId +
        ", idenId=" + idenId +
        ", userId=" + userId +
        ", userPw=" + userPw +
        ", userCname=" + userCname +
        ", userType=" + userType +
        ", enabled=" + enabled +
        ", isFirst=" + isFirst +
        ", email=" + email +
        ", emailToken=" + emailToken +
        ", emailExpire=" + emailExpire +
        ", creUser=" + creUser +
        ", creDate=" + creDate +
        ", updUser=" + updUser +
        ", updDate=" + updDate +
        "}";
    }
}
