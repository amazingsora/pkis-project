package com.tradevan.mapper.pkis.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

/**
 * <p>
 * 系統排程設定檔
 * </p>
 *
 * @author penny
 * @since 2020-03-04
 */
@TableName("PROF_SCHEDULE")
public class ProfSchedule extends Model<ProfSchedule> {

    private static final long serialVersionUID=1L;

    /**
     * 流水序號
     */
    @TableId("SEQUENCENO")
    private Integer sequenceno;

    /**
     * 排程類型
     */
    @TableField("SCHEDULE_TYPE")
    private String scheduleType;

    @TableField("EXEC_SECOND")
    private String execSecond;

    /**
     * 執行時間_分鐘
     */
    @TableField("EXEC_MINUTE")
    private String execMinute;

    @TableField("EXEC_HOUR")
    private String execHour;

    /**
     * 執行時間_日期
     */
    @TableField("EXEC_DAY")
    private String execDay;

    /**
     * 執行時間_日期
     */
    @TableField("EXEC_MONTH")
    private String execMonth;

    @TableField("EXEC_WEEK")
    private String execWeek;

    /**
     * 程式名稱
     */
    @TableField("SCHEDULE_NAME")
    private String scheduleName;

    /**
     * 執行的程式名稱
     */
    @TableField("PROGRAM_NAME")
    private String programName;

    /**
     * 排程參數值
     */
    @TableField("SCHEDULE_VALUE")
    private String scheduleValue;

    /**
     * 排程狀態
     */
    @TableField("SCHEDULE_STATE")
    private String scheduleState;

    /**
     * 獨立執行的程式間隔多久去執行(單位為秒)
     */
    @TableField("SLEEP_VALUE")
    private Integer sleepValue;

    /**
     * 處理狀態
     */
    @TableField("PROCESS_STATE")
    private String processState;

    /**
     * 啟動通知信代號
     */
    @TableField("NOTIFY_EMAIL")
    private String notifyEmail;

    /**
     * 錯誤通知信代號
     */
    @TableField("FAILURE_EMAIL")
    private String failureEmail;

    /**
     * 重啟狀態
     */
    @TableField("REDO_STATE")
    private String redoState;

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
     * 重啟人員
     */
    @TableField("REDO_USER")
    private String redoUser;

    /**
     * 重啟時間
     */
    @TableField("REDO_TIME")
    private String redoTime;

    /**
     * 重啟備註
     */
    @TableField("REDO_MEMO")
    private String redoMemo;

    /**
     * 系統備註說明
     */
    @TableField("SYSTEM_MEMO")
    private String systemMemo;


    public Integer getSequenceno() {
        return sequenceno;
    }

    public ProfSchedule setSequenceno(Integer sequenceno) {
        this.sequenceno = sequenceno;
        return this;
    }

    public String getScheduleType() {
        return scheduleType;
    }

    public ProfSchedule setScheduleType(String scheduleType) {
        this.scheduleType = scheduleType;
        return this;
    }

    public String getExecSecond() {
        return execSecond;
    }

    public ProfSchedule setExecSecond(String execSecond) {
        this.execSecond = execSecond;
        return this;
    }

    public String getExecMinute() {
        return execMinute;
    }

    public ProfSchedule setExecMinute(String execMinute) {
        this.execMinute = execMinute;
        return this;
    }

    public String getExecHour() {
        return execHour;
    }

    public ProfSchedule setExecHour(String execHour) {
        this.execHour = execHour;
        return this;
    }

    public String getExecDay() {
        return execDay;
    }

    public ProfSchedule setExecDay(String execDay) {
        this.execDay = execDay;
        return this;
    }

    public String getExecMonth() {
        return execMonth;
    }

    public ProfSchedule setExecMonth(String execMonth) {
        this.execMonth = execMonth;
        return this;
    }

    public String getExecWeek() {
        return execWeek;
    }

    public ProfSchedule setExecWeek(String execWeek) {
        this.execWeek = execWeek;
        return this;
    }

    public String getScheduleName() {
        return scheduleName;
    }

    public ProfSchedule setScheduleName(String scheduleName) {
        this.scheduleName = scheduleName;
        return this;
    }

    public String getProgramName() {
        return programName;
    }

    public ProfSchedule setProgramName(String programName) {
        this.programName = programName;
        return this;
    }

    public String getScheduleValue() {
        return scheduleValue;
    }

    public ProfSchedule setScheduleValue(String scheduleValue) {
        this.scheduleValue = scheduleValue;
        return this;
    }

    public String getScheduleState() {
        return scheduleState;
    }

    public ProfSchedule setScheduleState(String scheduleState) {
        this.scheduleState = scheduleState;
        return this;
    }

    public Integer getSleepValue() {
        return sleepValue;
    }

    public ProfSchedule setSleepValue(Integer sleepValue) {
        this.sleepValue = sleepValue;
        return this;
    }

    public String getProcessState() {
        return processState;
    }

    public ProfSchedule setProcessState(String processState) {
        this.processState = processState;
        return this;
    }

    public String getNotifyEmail() {
        return notifyEmail;
    }

    public ProfSchedule setNotifyEmail(String notifyEmail) {
        this.notifyEmail = notifyEmail;
        return this;
    }

    public String getFailureEmail() {
        return failureEmail;
    }

    public ProfSchedule setFailureEmail(String failureEmail) {
        this.failureEmail = failureEmail;
        return this;
    }

    public String getRedoState() {
        return redoState;
    }

    public ProfSchedule setRedoState(String redoState) {
        this.redoState = redoState;
        return this;
    }

    public String getCreateUser() {
        return createUser;
    }

    public ProfSchedule setCreateUser(String createUser) {
        this.createUser = createUser;
        return this;
    }

    public String getCreateTime() {
        return createTime;
    }

    public ProfSchedule setCreateTime(String createTime) {
        this.createTime = createTime;
        return this;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public ProfSchedule setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
        return this;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public ProfSchedule setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
        return this;
    }

    public String getRedoUser() {
        return redoUser;
    }

    public ProfSchedule setRedoUser(String redoUser) {
        this.redoUser = redoUser;
        return this;
    }

    public String getRedoTime() {
        return redoTime;
    }

    public ProfSchedule setRedoTime(String redoTime) {
        this.redoTime = redoTime;
        return this;
    }

    public String getRedoMemo() {
        return redoMemo;
    }

    public ProfSchedule setRedoMemo(String redoMemo) {
        this.redoMemo = redoMemo;
        return this;
    }

    public String getSystemMemo() {
        return systemMemo;
    }

    public ProfSchedule setSystemMemo(String systemMemo) {
        this.systemMemo = systemMemo;
        return this;
    }

    @Override
    protected Serializable pkVal() {
        return this.sequenceno;
    }

    @Override
    public String toString() {
        return "ProfSchedule{" +
        "sequenceno=" + sequenceno +
        ", scheduleType=" + scheduleType +
        ", execSecond=" + execSecond +
        ", execMinute=" + execMinute +
        ", execHour=" + execHour +
        ", execDay=" + execDay +
        ", execMonth=" + execMonth +
        ", execWeek=" + execWeek +
        ", scheduleName=" + scheduleName +
        ", programName=" + programName +
        ", scheduleValue=" + scheduleValue +
        ", scheduleState=" + scheduleState +
        ", sleepValue=" + sleepValue +
        ", processState=" + processState +
        ", notifyEmail=" + notifyEmail +
        ", failureEmail=" + failureEmail +
        ", redoState=" + redoState +
        ", createUser=" + createUser +
        ", createTime=" + createTime +
        ", updateUser=" + updateUser +
        ", updateTime=" + updateTime +
        ", redoUser=" + redoUser +
        ", redoTime=" + redoTime +
        ", redoMemo=" + redoMemo +
        ", systemMemo=" + systemMemo +
        "}";
    }
}
