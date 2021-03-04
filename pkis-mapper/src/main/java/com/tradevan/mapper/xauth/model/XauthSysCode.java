package com.tradevan.mapper.xauth.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

/**
 * <p>
 * 系統代碼檔
 * </p>
 *
 * @author penny
 * @since 2020-03-02
 */
@TableName("XAUTH_SYS_CODE")
public class XauthSysCode extends Model<XauthSysCode> {

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
     * 群組
     */
    @TableField("GP")
    private String gp;

    /**
     * 代碼
     */
    @TableField("CODE")
    private String code;

    /**
     * 中文名稱
     */
    @TableField("CNAME")
    private String cname;

    /**
     * 英文名稱
     */
    @TableField("ENAME")
    private String ename;

    /**
     * 顯示順序
     */
    @TableField("ORDER_SEQ")
    private Integer orderSeq;

    /**
     * 是否啟用 0:停用 1:啟用
     */
    @TableField("ENABLED")
    private String enabled;

    /**
     * 起始日期
     */
    @TableField("BGN_DATE")
    private Date bgnDate;

    /**
     * 截止日期
     */
    @TableField("END_DATE")
    private Date endDate;

    /**
     * 備註
     */
    @TableField("MEMO")
    private String memo;

    /**
     * 擴充欄位1
     */
    @TableField("C01")
    private String c01;

    /**
     * 擴充欄位2
     */
    @TableField("C02")
    private String c02;

    /**
     * 擴充欄位3
     */
    @TableField("C03")
    private String c03;

    /**
     * 擴充欄位4
     */
    @TableField("C04")
    private String c04;

    /**
     * 擴充欄位5
     */
    @TableField("C05")
    private String c05;

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

    public XauthSysCode setAppId(String appId) {
        this.appId = appId;
        return this;
    }

    public String getIdenId() {
        return idenId;
    }

    public XauthSysCode setIdenId(String idenId) {
        this.idenId = idenId;
        return this;
    }

    public String getGp() {
        return gp;
    }

    public XauthSysCode setGp(String gp) {
        this.gp = gp;
        return this;
    }

    public String getCode() {
        return code;
    }

    public XauthSysCode setCode(String code) {
        this.code = code;
        return this;
    }

    public String getCname() {
        return cname;
    }

    public XauthSysCode setCname(String cname) {
        this.cname = cname;
        return this;
    }

    public String getEname() {
        return ename;
    }

    public XauthSysCode setEname(String ename) {
        this.ename = ename;
        return this;
    }

    public Integer getOrderSeq() {
        return orderSeq;
    }

    public XauthSysCode setOrderSeq(Integer orderSeq) {
        this.orderSeq = orderSeq;
        return this;
    }

    public String getEnabled() {
        return enabled;
    }

    public XauthSysCode setEnabled(String enabled) {
        this.enabled = enabled;
        return this;
    }

    public Date getBgnDate() {
        return bgnDate;
    }

    public XauthSysCode setBgnDate(Date bgnDate) {
        this.bgnDate = bgnDate;
        return this;
    }

    public Date getEndDate() {
        return endDate;
    }

    public XauthSysCode setEndDate(Date endDate) {
        this.endDate = endDate;
        return this;
    }

    public String getMemo() {
        return memo;
    }

    public XauthSysCode setMemo(String memo) {
        this.memo = memo;
        return this;
    }

    public String getC01() {
        return c01;
    }

    public XauthSysCode setC01(String c01) {
        this.c01 = c01;
        return this;
    }

    public String getC02() {
        return c02;
    }

    public XauthSysCode setC02(String c02) {
        this.c02 = c02;
        return this;
    }

    public String getC03() {
        return c03;
    }

    public XauthSysCode setC03(String c03) {
        this.c03 = c03;
        return this;
    }

    public String getC04() {
        return c04;
    }

    public XauthSysCode setC04(String c04) {
        this.c04 = c04;
        return this;
    }

    public String getC05() {
        return c05;
    }

    public XauthSysCode setC05(String c05) {
        this.c05 = c05;
        return this;
    }

    public String getCreUser() {
        return creUser;
    }

    public XauthSysCode setCreUser(String creUser) {
        this.creUser = creUser;
        return this;
    }

    public Date getCreDate() {
        return creDate;
    }

    public XauthSysCode setCreDate(Date creDate) {
        this.creDate = creDate;
        return this;
    }

    public String getUpdUser() {
        return updUser;
    }

    public XauthSysCode setUpdUser(String updUser) {
        this.updUser = updUser;
        return this;
    }

    public Date getUpdDate() {
        return updDate;
    }

    public XauthSysCode setUpdDate(Date updDate) {
        this.updDate = updDate;
        return this;
    }

    @Override
    protected Serializable pkVal() {
        return this.appId;
    }

    @Override
    public String toString() {
        return "XauthSysCode{" +
        "appId=" + appId +
        ", idenId=" + idenId +
        ", gp=" + gp +
        ", code=" + code +
        ", cname=" + cname +
        ", ename=" + ename +
        ", orderSeq=" + orderSeq +
        ", enabled=" + enabled +
        ", bgnDate=" + bgnDate +
        ", endDate=" + endDate +
        ", memo=" + memo +
        ", c01=" + c01 +
        ", c02=" + c02 +
        ", c03=" + c03 +
        ", c04=" + c04 +
        ", c05=" + c05 +
        ", creUser=" + creUser +
        ", creDate=" + creDate +
        ", updUser=" + updUser +
        ", updDate=" + updDate +
        "}";
    }
}
