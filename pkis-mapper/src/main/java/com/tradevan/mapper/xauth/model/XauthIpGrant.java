package com.tradevan.mapper.xauth.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

/**
 * <p>
 * IP授權資料檔
 * </p>
 *
 * @author penny
 * @since 2020-03-02
 */
@TableName("XAUTH_IP_GRANT")
public class XauthIpGrant extends Model<XauthIpGrant> {

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
     * 網路IP位址
     */
    @TableField("IP_ADDR")
    private String ipAddr;

    /**
     * 系統別 REF:XAUTH_SYS_CODE.GP
     */
    @TableField("SYS_TYPE")
    private String sysType;

    /**
     * 是否啟用 0:停用 1:啟用
     */
    @TableField("ENABLED")
    private String enabled;

    /**
     * 備註
     */
    @TableField("MEMO")
    private String memo;

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

    public XauthIpGrant setAppId(String appId) {
        this.appId = appId;
        return this;
    }

    public String getIdenId() {
        return idenId;
    }

    public XauthIpGrant setIdenId(String idenId) {
        this.idenId = idenId;
        return this;
    }

    public String getIpAddr() {
        return ipAddr;
    }

    public XauthIpGrant setIpAddr(String ipAddr) {
        this.ipAddr = ipAddr;
        return this;
    }

    public String getSysType() {
        return sysType;
    }

    public XauthIpGrant setSysType(String sysType) {
        this.sysType = sysType;
        return this;
    }

    public String getEnabled() {
        return enabled;
    }

    public XauthIpGrant setEnabled(String enabled) {
        this.enabled = enabled;
        return this;
    }

    public String getMemo() {
        return memo;
    }

    public XauthIpGrant setMemo(String memo) {
        this.memo = memo;
        return this;
    }

    public String getCreUser() {
        return creUser;
    }

    public XauthIpGrant setCreUser(String creUser) {
        this.creUser = creUser;
        return this;
    }

    public Date getCreDate() {
        return creDate;
    }

    public XauthIpGrant setCreDate(Date creDate) {
        this.creDate = creDate;
        return this;
    }

    public String getUpdUser() {
        return updUser;
    }

    public XauthIpGrant setUpdUser(String updUser) {
        this.updUser = updUser;
        return this;
    }

    public Date getUpdDate() {
        return updDate;
    }

    public XauthIpGrant setUpdDate(Date updDate) {
        this.updDate = updDate;
        return this;
    }

    @Override
    protected Serializable pkVal() {
        return this.appId;
    }

    @Override
    public String toString() {
        return "XauthIpGrant{" +
        "appId=" + appId +
        ", idenId=" + idenId +
        ", ipAddr=" + ipAddr +
        ", sysType=" + sysType +
        ", enabled=" + enabled +
        ", memo=" + memo +
        ", creUser=" + creUser +
        ", creDate=" + creDate +
        ", updUser=" + updUser +
        ", updDate=" + updDate +
        "}";
    }
}
