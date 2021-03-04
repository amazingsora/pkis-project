package com.tradevan.mapper.xauth.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

/**
 * <p>
 * 選單資料檔
 * </p>
 *
 * @author penny
 * @since 2020-03-02
 */
@TableName("XAUTH_MENU")
public class XauthMenu extends Model<XauthMenu> {

    private static final long serialVersionUID=1L;

    /**
     * 系統代號
     */
    @TableId("APP_ID")
    private String appId;

    /**
     * 選單編號
     */
    @TableField("MENU_ID")
    private String menuId;

    /**
     * 選單網址
     */
    @TableField("MENU_URL")
    private String menuUrl;

    /**
     * 選單名稱
     */
    @TableField("MENU_CNAME")
    private String menuCname;

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

    public XauthMenu setAppId(String appId) {
        this.appId = appId;
        return this;
    }

    public String getMenuId() {
        return menuId;
    }

    public XauthMenu setMenuId(String menuId) {
        this.menuId = menuId;
        return this;
    }

    public String getMenuUrl() {
        return menuUrl;
    }

    public XauthMenu setMenuUrl(String menuUrl) {
        this.menuUrl = menuUrl;
        return this;
    }

    public String getMenuCname() {
        return menuCname;
    }

    public XauthMenu setMenuCname(String menuCname) {
        this.menuCname = menuCname;
        return this;
    }

    public String getParentId() {
        return parentId;
    }

    public XauthMenu setParentId(String parentId) {
        this.parentId = parentId;
        return this;
    }

    public Integer getSeqNo() {
        return seqNo;
    }

    public XauthMenu setSeqNo(Integer seqNo) {
        this.seqNo = seqNo;
        return this;
    }

    public Integer getParentSeq() {
        return parentSeq;
    }

    public XauthMenu setParentSeq(Integer parentSeq) {
        this.parentSeq = parentSeq;
        return this;
    }

    public String getMemo() {
        return memo;
    }

    public XauthMenu setMemo(String memo) {
        this.memo = memo;
        return this;
    }

    public String getCreUser() {
        return creUser;
    }

    public XauthMenu setCreUser(String creUser) {
        this.creUser = creUser;
        return this;
    }

    public Date getCreDate() {
        return creDate;
    }

    public XauthMenu setCreDate(Date creDate) {
        this.creDate = creDate;
        return this;
    }

    public String getUpdUser() {
        return updUser;
    }

    public XauthMenu setUpdUser(String updUser) {
        this.updUser = updUser;
        return this;
    }

    public Date getUpdDate() {
        return updDate;
    }

    public XauthMenu setUpdDate(Date updDate) {
        this.updDate = updDate;
        return this;
    }

    @Override
    protected Serializable pkVal() {
        return this.appId;
    }

    @Override
    public String toString() {
        return "XauthMenu{" +
        "appId=" + appId +
        ", menuId=" + menuId +
        ", menuUrl=" + menuUrl +
        ", menuCname=" + menuCname +
        ", parentId=" + parentId +
        ", seqNo=" + seqNo +
        ", parentSeq=" + parentSeq +
        ", memo=" + memo +
        ", creUser=" + creUser +
        ", creDate=" + creDate +
        ", updUser=" + updUser +
        ", updDate=" + updDate +
        "}";
    }
}
