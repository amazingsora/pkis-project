package com.tradevan.mapper.pkis.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

/**
 * <p>
 * 顏色設定檔
 * </p>
 *
 * @author penny
 * @since 2020-03-04
 */
@TableName("MCD_PRF_COLOR")
public class McdPrfColor extends Model<McdPrfColor> {

    private static final long serialVersionUID=1L;

    /**
     * 顏色流水號
     */
    @TableId("COLOR_ID")
    private String colorId;

    /**
     * 顏色代碼，例如：白色為000000
     */
    @TableField("COLOR_NO")
    private String colorNo;

    /**
     * 供應商統編，97162640為預設的顏色色系。
     */
    @TableField("COMP_BAN")
    private String compBan;

    /**
     * 語系
     */
    @TableField("LANG_ID")
    private String langId;

    /**
     * 顏色名稱
     */
    @TableField("COLOR_NAME")
    private String colorName;

    /**
     * 顯示的順序
     */
    @TableField("SORT_INDEX")
    private Integer sortIndex;

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
     * 更新人員
     */
    @TableField("UPDATE_USER")
    private String updateUser;

    /**
     * 更新時間
     */
    @TableField("UPDATE_TIME")
    private String updateTime;

    /**
     * 備註
     */
    @TableField("SYSTEM_MEMO")
    private String systemMemo;

    /**
     * 類型：C-顏色，T-口味
     */
    @TableField("CONTENT_TYPE")
    private String contentType;


    public String getColorId() {
        return colorId;
    }

    public McdPrfColor setColorId(String colorId) {
        this.colorId = colorId;
        return this;
    }

    public String getColorNo() {
        return colorNo;
    }

    public McdPrfColor setColorNo(String colorNo) {
        this.colorNo = colorNo;
        return this;
    }

    public String getCompBan() {
        return compBan;
    }

    public McdPrfColor setCompBan(String compBan) {
        this.compBan = compBan;
        return this;
    }

    public String getLangId() {
        return langId;
    }

    public McdPrfColor setLangId(String langId) {
        this.langId = langId;
        return this;
    }

    public String getColorName() {
        return colorName;
    }

    public McdPrfColor setColorName(String colorName) {
        this.colorName = colorName;
        return this;
    }

    public Integer getSortIndex() {
        return sortIndex;
    }

    public McdPrfColor setSortIndex(Integer sortIndex) {
        this.sortIndex = sortIndex;
        return this;
    }

    public String getCreateUser() {
        return createUser;
    }

    public McdPrfColor setCreateUser(String createUser) {
        this.createUser = createUser;
        return this;
    }

    public String getCreateTime() {
        return createTime;
    }

    public McdPrfColor setCreateTime(String createTime) {
        this.createTime = createTime;
        return this;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public McdPrfColor setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
        return this;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public McdPrfColor setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
        return this;
    }

    public String getSystemMemo() {
        return systemMemo;
    }

    public McdPrfColor setSystemMemo(String systemMemo) {
        this.systemMemo = systemMemo;
        return this;
    }

    public String getContentType() {
        return contentType;
    }

    public McdPrfColor setContentType(String contentType) {
        this.contentType = contentType;
        return this;
    }

    @Override
    protected Serializable pkVal() {
        return this.colorId;
    }

    @Override
    public String toString() {
        return "McdPrfColor{" +
        "colorId=" + colorId +
        ", colorNo=" + colorNo +
        ", compBan=" + compBan +
        ", langId=" + langId +
        ", colorName=" + colorName +
        ", sortIndex=" + sortIndex +
        ", createUser=" + createUser +
        ", createTime=" + createTime +
        ", updateUser=" + updateUser +
        ", updateTime=" + updateTime +
        ", systemMemo=" + systemMemo +
        ", contentType=" + contentType +
        "}";
    }
}
