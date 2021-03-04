package com.tradevan.mapper.pkis.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

/**
 * <p>
 * 系統錯誤記錄檔
 * </p>
 *
 * @author penny
 * @since 2020-03-04
 */
@TableName("LOGG_ERROR")
public class LoggError extends Model<LoggError> {

    private static final long serialVersionUID=1L;

    /**
     * 流水序號
     */
    @TableId("SEQUENCENO")
    private String sequenceno;

    /**
     * 主機IP
     */
    @TableField("SERVER_IP")
    private String serverIp;

    /**
     * 主機位置
     */
    @TableField("SERVER_SITE")
    private String serverSite;

    /**
     * 使用者帳號
     */
    @TableField("USER_ID")
    private String userId;

    /**
     * 使用者統編
     */
    @TableField("USER_BAN")
    private String userBan;

    /**
     * 使用者session id
     */
    @TableField("USER_SESSION_ID")
    private String userSessionId;

    /**
     * 使用者IP
     */
    @TableField("USER_IP")
    private String userIp;

    /**
     * 錯誤分類
     */
    @TableField("ERROR_TYPE")
    private String errorType;

    /**
     * 程式功能代碼
     */
    @TableField("FUNCTION_CODE")
    private String functionCode;

    /**
     * 功能執行的method
     */
    @TableField("ACTION_CODE")
    private String actionCode;

    /**
     * 傳入的條件
     */
    @TableField("ARGUMENT_VALUE")
    private String argumentValue;

    /**
     * 錯誤記錄說明
     */
    @TableField("ERROR_MEMO")
    private String errorMemo;

    /**
     * 錯誤訊息通知
     */
    @TableField("ERROR_NOTICE")
    private String errorNotice;

    /**
     * 錯誤訊息通知狀態
     */
    @TableField("NOTICE_STATE")
    private String noticeState;

    /**
     * 錯誤訊息通知人員
     */
    @TableField("NOTICE_USER")
    private String noticeUser;

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


    public String getSequenceno() {
        return sequenceno;
    }

    public LoggError setSequenceno(String sequenceno) {
        this.sequenceno = sequenceno;
        return this;
    }

    public String getServerIp() {
        return serverIp;
    }

    public LoggError setServerIp(String serverIp) {
        this.serverIp = serverIp;
        return this;
    }

    public String getServerSite() {
        return serverSite;
    }

    public LoggError setServerSite(String serverSite) {
        this.serverSite = serverSite;
        return this;
    }

    public String getUserId() {
        return userId;
    }

    public LoggError setUserId(String userId) {
        this.userId = userId;
        return this;
    }

    public String getUserBan() {
        return userBan;
    }

    public LoggError setUserBan(String userBan) {
        this.userBan = userBan;
        return this;
    }

    public String getUserSessionId() {
        return userSessionId;
    }

    public LoggError setUserSessionId(String userSessionId) {
        this.userSessionId = userSessionId;
        return this;
    }

    public String getUserIp() {
        return userIp;
    }

    public LoggError setUserIp(String userIp) {
        this.userIp = userIp;
        return this;
    }

    public String getErrorType() {
        return errorType;
    }

    public LoggError setErrorType(String errorType) {
        this.errorType = errorType;
        return this;
    }

    public String getFunctionCode() {
        return functionCode;
    }

    public LoggError setFunctionCode(String functionCode) {
        this.functionCode = functionCode;
        return this;
    }

    public String getActionCode() {
        return actionCode;
    }

    public LoggError setActionCode(String actionCode) {
        this.actionCode = actionCode;
        return this;
    }

    public String getArgumentValue() {
        return argumentValue;
    }

    public LoggError setArgumentValue(String argumentValue) {
        this.argumentValue = argumentValue;
        return this;
    }

    public String getErrorMemo() {
        return errorMemo;
    }

    public LoggError setErrorMemo(String errorMemo) {
        this.errorMemo = errorMemo;
        return this;
    }

    public String getErrorNotice() {
        return errorNotice;
    }

    public LoggError setErrorNotice(String errorNotice) {
        this.errorNotice = errorNotice;
        return this;
    }

    public String getNoticeState() {
        return noticeState;
    }

    public LoggError setNoticeState(String noticeState) {
        this.noticeState = noticeState;
        return this;
    }

    public String getNoticeUser() {
        return noticeUser;
    }

    public LoggError setNoticeUser(String noticeUser) {
        this.noticeUser = noticeUser;
        return this;
    }

    public String getCreateUser() {
        return createUser;
    }

    public LoggError setCreateUser(String createUser) {
        this.createUser = createUser;
        return this;
    }

    public String getCreateTime() {
        return createTime;
    }

    public LoggError setCreateTime(String createTime) {
        this.createTime = createTime;
        return this;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public LoggError setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
        return this;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public LoggError setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
        return this;
    }

    public String getSystemMemo() {
        return systemMemo;
    }

    public LoggError setSystemMemo(String systemMemo) {
        this.systemMemo = systemMemo;
        return this;
    }

    @Override
    protected Serializable pkVal() {
        return this.sequenceno;
    }

    @Override
    public String toString() {
        return "LoggError{" +
        "sequenceno=" + sequenceno +
        ", serverIp=" + serverIp +
        ", serverSite=" + serverSite +
        ", userId=" + userId +
        ", userBan=" + userBan +
        ", userSessionId=" + userSessionId +
        ", userIp=" + userIp +
        ", errorType=" + errorType +
        ", functionCode=" + functionCode +
        ", actionCode=" + actionCode +
        ", argumentValue=" + argumentValue +
        ", errorMemo=" + errorMemo +
        ", errorNotice=" + errorNotice +
        ", noticeState=" + noticeState +
        ", noticeUser=" + noticeUser +
        ", createUser=" + createUser +
        ", createTime=" + createTime +
        ", updateUser=" + updateUser +
        ", updateTime=" + updateTime +
        ", systemMemo=" + systemMemo +
        "}";
    }
}
