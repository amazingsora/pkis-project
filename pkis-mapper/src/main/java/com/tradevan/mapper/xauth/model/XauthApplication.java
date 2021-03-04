package com.tradevan.mapper.xauth.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

/**
 * <p>
 * 系統資料檔
 * </p>
 *
 * @author penny
 * @since 2020-03-02
 */
@TableName("XAUTH_APPLICATION")
public class XauthApplication extends Model<XauthApplication> {

    private static final long serialVersionUID=1L;

    /**
     * 系統代號
     */
    @TableId("APP_ID")
    private String appId;

    /**
     * 系統名稱
     */
    @TableField("APP_NAME")
    private String appName;

    /**
     * 是否啟用 0:停用 1:啟用
     */
    @TableField("ENABLED")
    private String enabled;

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

    public XauthApplication setAppId(String appId) {
        this.appId = appId;
        return this;
    }

    public String getAppName() {
        return appName;
    }

    public XauthApplication setAppName(String appName) {
        this.appName = appName;
        return this;
    }

    public String getEnabled() {
        return enabled;
    }

    public XauthApplication setEnabled(String enabled) {
        this.enabled = enabled;
        return this;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public XauthApplication setPublicKey(String publicKey) {
        this.publicKey = publicKey;
        return this;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public XauthApplication setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
        return this;
    }

    public String getCreUser() {
        return creUser;
    }

    public XauthApplication setCreUser(String creUser) {
        this.creUser = creUser;
        return this;
    }

    public Date getCreDate() {
        return creDate;
    }

    public XauthApplication setCreDate(Date creDate) {
        this.creDate = creDate;
        return this;
    }

    public String getUpdUser() {
        return updUser;
    }

    public XauthApplication setUpdUser(String updUser) {
        this.updUser = updUser;
        return this;
    }

    public Date getUpdDate() {
        return updDate;
    }

    public XauthApplication setUpdDate(Date updDate) {
        this.updDate = updDate;
        return this;
    }

    @Override
    protected Serializable pkVal() {
        return this.appId;
    }

    @Override
    public String toString() {
        return "XauthApplication{" +
        "appId=" + appId +
        ", appName=" + appName +
        ", enabled=" + enabled +
        ", publicKey=" + publicKey +
        ", privateKey=" + privateKey +
        ", creUser=" + creUser +
        ", creDate=" + creDate +
        ", updUser=" + updUser +
        ", updDate=" + updDate +
        "}";
    }
}
