package com.tradevan.mapper.pkis.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

/**
 * <p>
 * 前端AP Server操作記錄檔
 * </p>
 *
 * @author penny
 * @since 2020-03-04
 */
@TableName("LOGG_APPLICATION")
public class LoggApplication extends Model<LoggApplication> {

    private static final long serialVersionUID=1L;

    /**
     * 流水序號
     */
    @TableField("SEQUENCENO")
    private String sequenceno;

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
     * 使用者登入成功後的session id
     */
    @TableField("USER_SESSION_ID")
    private String userSessionId;

    /**
     * 使用者IP
     */
    @TableField("USER_IP")
    private String userIp;

    /**
     * 記錄分類
     */
    @TableField("LOGG_TYPE")
    private String loggType;

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
     * 網址列上的URL
     */
    @TableField("LOGG_MEMO")
    private String loggMemo;

    /**
     * 接收/傳送的檔案名稱
     */
    @TableField("FILE_NAME")
    private String fileName;

    /**
     * 接收/傳送的檔案副檔名
     */
    @TableField("FILE_TYPE")
    private String fileType;

    /**
     * 建立人員
     */
    @TableField("CREATE_USER")
    private String createUser;

    /**
     * 異動時間
     */
    @TableField("UPDATE_TIME")
    private String updateTime;

    /**
     * 異動人員
     */
    @TableField("UPDATE_USER")
    private String updateUser;

    /**
     * 建立時間
     */
    @TableField("CREATE_TIME")
    private String createTime;

    /**
     * 系統備註說明
     */
    @TableField("SYSTEM_MEMO")
    private String systemMemo;

    /**
     * 使用者尚未登入前的session id
     */
    @TableField("ORGN_SESSION_ID")
    private String orgnSessionId;

    /**
     * 使用者使用的瀏覽器資訊
     */
    @TableField("USER_AGENT")
    private String userAgent;


    public String getSequenceno() {
        return sequenceno;
    }

    public LoggApplication setSequenceno(String sequenceno) {
        this.sequenceno = sequenceno;
        return this;
    }

    public String getUserId() {
        return userId;
    }

    public LoggApplication setUserId(String userId) {
        this.userId = userId;
        return this;
    }

    public String getUserBan() {
        return userBan;
    }

    public LoggApplication setUserBan(String userBan) {
        this.userBan = userBan;
        return this;
    }

    public String getUserSessionId() {
        return userSessionId;
    }

    public LoggApplication setUserSessionId(String userSessionId) {
        this.userSessionId = userSessionId;
        return this;
    }

    public String getUserIp() {
        return userIp;
    }

    public LoggApplication setUserIp(String userIp) {
        this.userIp = userIp;
        return this;
    }

    public String getLoggType() {
        return loggType;
    }

    public LoggApplication setLoggType(String loggType) {
        this.loggType = loggType;
        return this;
    }

    public String getFunctionCode() {
        return functionCode;
    }

    public LoggApplication setFunctionCode(String functionCode) {
        this.functionCode = functionCode;
        return this;
    }

    public String getActionCode() {
        return actionCode;
    }

    public LoggApplication setActionCode(String actionCode) {
        this.actionCode = actionCode;
        return this;
    }

    public String getArgumentValue() {
        return argumentValue;
    }

    public LoggApplication setArgumentValue(String argumentValue) {
        this.argumentValue = argumentValue;
        return this;
    }

    public String getLoggMemo() {
        return loggMemo;
    }

    public LoggApplication setLoggMemo(String loggMemo) {
        this.loggMemo = loggMemo;
        return this;
    }

    public String getFileName() {
        return fileName;
    }

    public LoggApplication setFileName(String fileName) {
        this.fileName = fileName;
        return this;
    }

    public String getFileType() {
        return fileType;
    }

    public LoggApplication setFileType(String fileType) {
        this.fileType = fileType;
        return this;
    }

    public String getCreateUser() {
        return createUser;
    }

    public LoggApplication setCreateUser(String createUser) {
        this.createUser = createUser;
        return this;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public LoggApplication setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
        return this;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public LoggApplication setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
        return this;
    }

    public String getCreateTime() {
        return createTime;
    }

    public LoggApplication setCreateTime(String createTime) {
        this.createTime = createTime;
        return this;
    }

    public String getSystemMemo() {
        return systemMemo;
    }

    public LoggApplication setSystemMemo(String systemMemo) {
        this.systemMemo = systemMemo;
        return this;
    }

    public String getOrgnSessionId() {
        return orgnSessionId;
    }

    public LoggApplication setOrgnSessionId(String orgnSessionId) {
        this.orgnSessionId = orgnSessionId;
        return this;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public LoggApplication setUserAgent(String userAgent) {
        this.userAgent = userAgent;
        return this;
    }

    @Override
    protected Serializable pkVal() {
        return null;
    }

    @Override
    public String toString() {
        return "LoggApplication{" +
        "sequenceno=" + sequenceno +
        ", userId=" + userId +
        ", userBan=" + userBan +
        ", userSessionId=" + userSessionId +
        ", userIp=" + userIp +
        ", loggType=" + loggType +
        ", functionCode=" + functionCode +
        ", actionCode=" + actionCode +
        ", argumentValue=" + argumentValue +
        ", loggMemo=" + loggMemo +
        ", fileName=" + fileName +
        ", fileType=" + fileType +
        ", createUser=" + createUser +
        ", updateTime=" + updateTime +
        ", updateUser=" + updateUser +
        ", createTime=" + createTime +
        ", systemMemo=" + systemMemo +
        ", orgnSessionId=" + orgnSessionId +
        ", userAgent=" + userAgent +
        "}";
    }
}
