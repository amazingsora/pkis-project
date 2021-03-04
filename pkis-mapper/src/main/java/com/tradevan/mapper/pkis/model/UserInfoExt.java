package com.tradevan.mapper.pkis.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.util.Date;

import org.apache.ibatis.type.JdbcType;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

/**
 * <p>
 * 使用者資料擴充檔
 * </p>
 *
 * @author system
 * @since 2020-07-24
 */
@TableName("USER_INFO_EXT")
public class UserInfoExt extends Model<UserInfoExt> {

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
     * 起始日期
     */
    @TableField("BGN_DATE")
    private Date bgnDate;

    /**
     * 截止日期
     */
    @TableField(value = "END_DATE", insertStrategy = FieldStrategy.IGNORED, updateStrategy = FieldStrategy.IGNORED, jdbcType = JdbcType.DATE)
    private Date endDate;

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

    @TableField("DIRECT_MANAGER")
    private String directManager;


    public String getAppId() {
        return appId;
    }

    public UserInfoExt setAppId(String appId) {
        this.appId = appId;
        return this;
    }

    public String getIdenId() {
        return idenId;
    }

    public UserInfoExt setIdenId(String idenId) {
        this.idenId = idenId;
        return this;
    }

    public String getUserId() {
        return userId;
    }

    public UserInfoExt setUserId(String userId) {
        this.userId = userId;
        return this;
    }

    public Date getBgnDate() {
        return bgnDate;
    }

    public UserInfoExt setBgnDate(Date bgnDate) {
        this.bgnDate = bgnDate;
        return this;
    }

    public Date getEndDate() {
        return endDate;
    }

    public UserInfoExt setEndDate(Date endDate) {
        this.endDate = endDate;
        return this;
    }

    public String getCreUser() {
        return creUser;
    }

    public UserInfoExt setCreUser(String creUser) {
        this.creUser = creUser;
        return this;
    }

    public Date getCreDate() {
        return creDate;
    }

    public UserInfoExt setCreDate(Date creDate) {
        this.creDate = creDate;
        return this;
    }

    public String getUpdUser() {
        return updUser;
    }

    public UserInfoExt setUpdUser(String updUser) {
        this.updUser = updUser;
        return this;
    }

    public Date getUpdDate() {
        return updDate;
    }

    public UserInfoExt setUpdDate(Date updDate) {
        this.updDate = updDate;
        return this;
    }

    public String getDirectManager() {
        return directManager;
    }

    public UserInfoExt setDirectManager(String directManager) {
        this.directManager = directManager;
        return this;
    }

    @Override
    protected Serializable pkVal() {
        return this.appId;
    }

    @Override
    public String toString() {
        return "UserInfoExt{" +
        "appId=" + appId +
        ", idenId=" + idenId +
        ", userId=" + userId +
        ", bgnDate=" + bgnDate +
        ", endDate=" + endDate +
        ", creUser=" + creUser +
        ", creDate=" + creDate +
        ", updUser=" + updUser +
        ", updDate=" + updDate +
        ", directManager=" + directManager +
        "}";
    }
}
