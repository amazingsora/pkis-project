package com.tradevan.mapper.xauth.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

/**
 * <p>
 * 部門資料檔
 * </p>
 *
 * @author penny
 * @since 2020-03-02
 */
@TableName("XAUTH_DEPT")
public class XauthDept extends Model<XauthDept> {

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
     * 統一編號
     */
    @TableField("BAN")
    private String ban;

    /**
     * 公司名稱
     */
    @TableField("CNAME")
    private String cname;

    /**
     * 上層選單編號
     */
    @TableField("PARENT_ID")
    private String parentId;

    /**
     * 顯示順序
     */
    @TableField("SEQ_NO")
    private Integer seqNo;

    /**
     * 上層順序
     */
    @TableField("PARENT_SEQ")
    private Integer parentSeq;

    /**
     * 是否啟用
     */
    @TableField("ENABLED")
    private String enabled;

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

    public XauthDept setAppId(String appId) {
        this.appId = appId;
        return this;
    }

    public String getIdenId() {
        return idenId;
    }

    public XauthDept setIdenId(String idenId) {
        this.idenId = idenId;
        return this;
    }

    public String getBan() {
        return ban;
    }

    public XauthDept setBan(String ban) {
        this.ban = ban;
        return this;
    }

    public String getCname() {
        return cname;
    }

    public XauthDept setCname(String cname) {
        this.cname = cname;
        return this;
    }

    public String getParentId() {
        return parentId;
    }

    public XauthDept setParentId(String parentId) {
        this.parentId = parentId;
        return this;
    }

    public Integer getSeqNo() {
        return seqNo;
    }

    public XauthDept setSeqNo(Integer seqNo) {
        this.seqNo = seqNo;
        return this;
    }

    public Integer getParentSeq() {
        return parentSeq;
    }

    public XauthDept setParentSeq(Integer parentSeq) {
        this.parentSeq = parentSeq;
        return this;
    }

    public String getEnabled() {
        return enabled;
    }

    public XauthDept setEnabled(String enabled) {
        this.enabled = enabled;
        return this;
    }

    public String getCreUser() {
        return creUser;
    }

    public XauthDept setCreUser(String creUser) {
        this.creUser = creUser;
        return this;
    }

    public Date getCreDate() {
        return creDate;
    }

    public XauthDept setCreDate(Date creDate) {
        this.creDate = creDate;
        return this;
    }

    public String getUpdUser() {
        return updUser;
    }

    public XauthDept setUpdUser(String updUser) {
        this.updUser = updUser;
        return this;
    }

    public Date getUpdDate() {
        return updDate;
    }

    public XauthDept setUpdDate(Date updDate) {
        this.updDate = updDate;
        return this;
    }

    @Override
    protected Serializable pkVal() {
        return this.appId;
    }

    @Override
    public String toString() {
        return "XauthDept{" +
        "appId=" + appId +
        ", idenId=" + idenId +
        ", ban=" + ban +
        ", cname=" + cname +
        ", parentId=" + parentId +
        ", seqNo=" + seqNo +
        ", parentSeq=" + parentSeq +
        ", enabled=" + enabled +
        ", creUser=" + creUser +
        ", creDate=" + creDate +
        ", updUser=" + updUser +
        ", updDate=" + updDate +
        "}";
    }
}
