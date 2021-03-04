package com.tradevan.mapper.pkis.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author penny
 * @since 2020-03-04
 */
@TableName("REF_MAIL")
public class RefMail extends Model<RefMail> {

    private static final long serialVersionUID=1L;

    /**
     * 使用者帳號
     */
    @TableId("USER_ID")
    private String userId;

    /**
     * 電子郵件代碼
     */
    @TableField("MAIL_ID")
    private String mailId;

    /**
     * MAIL設定的型態(MERCHANDISE：商品提報)
     */
    @TableField("MAIL_TYPE")
    private String mailType;

    /**
     * 建立人員
     */
    @TableField("CREATE_USER")
    private String createUser;

    /**
     * 建立時間
     */
    @TableField("CREATE_TIME")
    private String createTime;

    /**
     * 異動人員
     */
    @TableField("UPDATE_USER")
    private String updateUser;

    /**
     * 異動時間
     */
    @TableField("UPDATE_TIME")
    private String updateTime;

    /**
     * 系統備註說明
     */
    @TableField("SYSTEM_MEMO")
    private String systemMemo;


    public String getUserId() {
        return userId;
    }

    public RefMail setUserId(String userId) {
        this.userId = userId;
        return this;
    }

    public String getMailId() {
        return mailId;
    }

    public RefMail setMailId(String mailId) {
        this.mailId = mailId;
        return this;
    }

    public String getMailType() {
        return mailType;
    }

    public RefMail setMailType(String mailType) {
        this.mailType = mailType;
        return this;
    }

    public String getCreateUser() {
        return createUser;
    }

    public RefMail setCreateUser(String createUser) {
        this.createUser = createUser;
        return this;
    }

    public String getCreateTime() {
        return createTime;
    }

    public RefMail setCreateTime(String createTime) {
        this.createTime = createTime;
        return this;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public RefMail setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
        return this;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public RefMail setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
        return this;
    }

    public String getSystemMemo() {
        return systemMemo;
    }

    public RefMail setSystemMemo(String systemMemo) {
        this.systemMemo = systemMemo;
        return this;
    }

    @Override
    protected Serializable pkVal() {
        return this.userId;
    }

    @Override
    public String toString() {
        return "RefMail{" +
        "userId=" + userId +
        ", mailId=" + mailId +
        ", mailType=" + mailType +
        ", createUser=" + createUser +
        ", createTime=" + createTime +
        ", updateUser=" + updateUser +
        ", updateTime=" + updateTime +
        ", systemMemo=" + systemMemo +
        "}";
    }
}
