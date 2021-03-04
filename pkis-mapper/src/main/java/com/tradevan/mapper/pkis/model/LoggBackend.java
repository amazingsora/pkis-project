package com.tradevan.mapper.pkis.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

/**
 * <p>
 * 後端Backend排程記錄檔
 * </p>
 *
 * @author penny
 * @since 2020-03-04
 */
@TableName("LOGG_BACKEND")
public class LoggBackend extends Model<LoggBackend> {

    private static final long serialVersionUID=1L;

    /**
     * 流水序號
     */
    @TableId("SEQUENCENO")
    private String sequenceno;

    /**
     * 執行的程式名稱
     */
    @TableField("PROGRAM_NAME")
    private String programName;

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
     * 資料總筆數
     */
    @TableField("TOTAL_QTY")
    private Integer totalQty;

    /**
     * 成功筆數
     */
    @TableField("SUCCESS_QTY")
    private Integer successQty;

    /**
     * 失敗筆數
     */
    @TableField("FAILURE_QTY")
    private Integer failureQty;

    /**
     * 執行結果的狀態
     */
    @TableField("FUNCTION_STATUS")
    private String functionStatus;

    /**
     * 接收/傳送型態
     */
    @TableField("FILE_TRANSPORT")
    private String fileTransport;

    /**
     * 接收/傳送的檔案名稱
     */
    @TableField("FILE_NAME")
    private String fileName;

    @TableField("LOGG_MEMO")
    private String loggMemo;

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

    public LoggBackend setSequenceno(String sequenceno) {
        this.sequenceno = sequenceno;
        return this;
    }

    public String getProgramName() {
        return programName;
    }

    public LoggBackend setProgramName(String programName) {
        this.programName = programName;
        return this;
    }

    public String getActionCode() {
        return actionCode;
    }

    public LoggBackend setActionCode(String actionCode) {
        this.actionCode = actionCode;
        return this;
    }

    public String getArgumentValue() {
        return argumentValue;
    }

    public LoggBackend setArgumentValue(String argumentValue) {
        this.argumentValue = argumentValue;
        return this;
    }

    public Integer getTotalQty() {
        return totalQty;
    }

    public LoggBackend setTotalQty(Integer totalQty) {
        this.totalQty = totalQty;
        return this;
    }

    public Integer getSuccessQty() {
        return successQty;
    }

    public LoggBackend setSuccessQty(Integer successQty) {
        this.successQty = successQty;
        return this;
    }

    public Integer getFailureQty() {
        return failureQty;
    }

    public LoggBackend setFailureQty(Integer failureQty) {
        this.failureQty = failureQty;
        return this;
    }

    public String getFunctionStatus() {
        return functionStatus;
    }

    public LoggBackend setFunctionStatus(String functionStatus) {
        this.functionStatus = functionStatus;
        return this;
    }

    public String getFileTransport() {
        return fileTransport;
    }

    public LoggBackend setFileTransport(String fileTransport) {
        this.fileTransport = fileTransport;
        return this;
    }

    public String getFileName() {
        return fileName;
    }

    public LoggBackend setFileName(String fileName) {
        this.fileName = fileName;
        return this;
    }

    public String getLoggMemo() {
        return loggMemo;
    }

    public LoggBackend setLoggMemo(String loggMemo) {
        this.loggMemo = loggMemo;
        return this;
    }

    public String getErrorNotice() {
        return errorNotice;
    }

    public LoggBackend setErrorNotice(String errorNotice) {
        this.errorNotice = errorNotice;
        return this;
    }

    public String getNoticeState() {
        return noticeState;
    }

    public LoggBackend setNoticeState(String noticeState) {
        this.noticeState = noticeState;
        return this;
    }

    public String getNoticeUser() {
        return noticeUser;
    }

    public LoggBackend setNoticeUser(String noticeUser) {
        this.noticeUser = noticeUser;
        return this;
    }

    public String getCreateUser() {
        return createUser;
    }

    public LoggBackend setCreateUser(String createUser) {
        this.createUser = createUser;
        return this;
    }

    public String getCreateTime() {
        return createTime;
    }

    public LoggBackend setCreateTime(String createTime) {
        this.createTime = createTime;
        return this;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public LoggBackend setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
        return this;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public LoggBackend setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
        return this;
    }

    public String getSystemMemo() {
        return systemMemo;
    }

    public LoggBackend setSystemMemo(String systemMemo) {
        this.systemMemo = systemMemo;
        return this;
    }

    @Override
    protected Serializable pkVal() {
        return this.sequenceno;
    }

    @Override
    public String toString() {
        return "LoggBackend{" +
        "sequenceno=" + sequenceno +
        ", programName=" + programName +
        ", actionCode=" + actionCode +
        ", argumentValue=" + argumentValue +
        ", totalQty=" + totalQty +
        ", successQty=" + successQty +
        ", failureQty=" + failureQty +
        ", functionStatus=" + functionStatus +
        ", fileTransport=" + fileTransport +
        ", fileName=" + fileName +
        ", loggMemo=" + loggMemo +
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
