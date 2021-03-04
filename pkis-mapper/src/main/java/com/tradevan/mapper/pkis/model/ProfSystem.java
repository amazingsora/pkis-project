package com.tradevan.mapper.pkis.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

/**
 * <p>
 * 系統參數代碼設定檔
 * </p>
 *
 * @author penny
 * @since 2020-03-04
 */
@TableName("PROF_SYSTEM")
public class ProfSystem extends Model<ProfSystem> {

    private static final long serialVersionUID=1L;

    /**
     * 流水序號
     */
    @TableField("SEQUENCENO")
    private String sequenceno;

    /**
     * 語系
     */
    @TableField("LANG_ID")
    private String langId;

    /**
     * 設定屬性
     */
    @TableField("PROF_TYPE")
    private String profType;

    /**
     * 設定屬性代碼
     */
    @TableField("PROF_ID")
    private String profId;

    /**
     * 資料內容
     */
    @TableField("PROF_VALUE")
    private String profValue;

    /**
     * 階層關係
     */
    @TableField("PARENT_ID")
    private String parentId;

    /**
     * 排序
     */
    @TableField("SORT_INDEX")
    private Integer sortIndex;

    /**
     * 使用狀態
     */
    @TableField("USE_STATE")
    private String useState;

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

    public ProfSystem setSequenceno(String sequenceno) {
        this.sequenceno = sequenceno;
        return this;
    }

    public String getLangId() {
        return langId;
    }

    public ProfSystem setLangId(String langId) {
        this.langId = langId;
        return this;
    }

    public String getProfType() {
        return profType;
    }

    public ProfSystem setProfType(String profType) {
        this.profType = profType;
        return this;
    }

    public String getProfId() {
        return profId;
    }

    public ProfSystem setProfId(String profId) {
        this.profId = profId;
        return this;
    }

    public String getProfValue() {
        return profValue;
    }

    public ProfSystem setProfValue(String profValue) {
        this.profValue = profValue;
        return this;
    }

    public String getParentId() {
        return parentId;
    }

    public ProfSystem setParentId(String parentId) {
        this.parentId = parentId;
        return this;
    }

    public Integer getSortIndex() {
        return sortIndex;
    }

    public ProfSystem setSortIndex(Integer sortIndex) {
        this.sortIndex = sortIndex;
        return this;
    }

    public String getUseState() {
        return useState;
    }

    public ProfSystem setUseState(String useState) {
        this.useState = useState;
        return this;
    }

    public String getCreateUser() {
        return createUser;
    }

    public ProfSystem setCreateUser(String createUser) {
        this.createUser = createUser;
        return this;
    }

    public String getCreateTime() {
        return createTime;
    }

    public ProfSystem setCreateTime(String createTime) {
        this.createTime = createTime;
        return this;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public ProfSystem setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
        return this;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public ProfSystem setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
        return this;
    }

    public String getSystemMemo() {
        return systemMemo;
    }

    public ProfSystem setSystemMemo(String systemMemo) {
        this.systemMemo = systemMemo;
        return this;
    }

    @Override
    protected Serializable pkVal() {
        return null;
    }

    @Override
    public String toString() {
        return "ProfSystem{" +
        "sequenceno=" + sequenceno +
        ", langId=" + langId +
        ", profType=" + profType +
        ", profId=" + profId +
        ", profValue=" + profValue +
        ", parentId=" + parentId +
        ", sortIndex=" + sortIndex +
        ", useState=" + useState +
        ", createUser=" + createUser +
        ", createTime=" + createTime +
        ", updateUser=" + updateUser +
        ", updateTime=" + updateTime +
        ", systemMemo=" + systemMemo +
        "}";
    }
}
