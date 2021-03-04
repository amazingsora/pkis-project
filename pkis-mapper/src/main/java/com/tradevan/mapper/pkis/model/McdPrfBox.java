package com.tradevan.mapper.pkis.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

/**
 * <p>
 * 箱包裝設定檔
 * </p>
 *
 * @author penny
 * @since 2020-03-04
 */
@TableName("MCD_PRF_BOX")
public class McdPrfBox extends Model<McdPrfBox> {

    private static final long serialVersionUID=1L;

    @TableId("BOX_ID")
    private String boxId;

    /**
     * 97162640為預設的。
     */
    @TableField("COMP_BAN")
    private String compBan;

    /**
     * 語系
     */
    @TableField("LANG_ID")
    private String langId;

    /**
     * 箱的名稱
     */
    @TableField("BOX_NAME")
    private String boxName;

    /**
     * 深
     */
    @TableField("DIMENSION_DEEP")
    private Integer dimensionDeep;

    /**
     * 寬
     */
    @TableField("DIMENSION_WIDTH")
    private Integer dimensionWidth;

    /**
     * 高
     */
    @TableField("DIMENSION_HEIGHT")
    private Integer dimensionHeight;

    /**
     * 尺吋單位
     */
    @TableField("DIMENSION_UNIT")
    private String dimensionUnit;

    /**
     * 包裝材質
     */
    @TableField("PACK_MATERIAL")
    private String packMaterial;

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


    public String getBoxId() {
        return boxId;
    }

    public McdPrfBox setBoxId(String boxId) {
        this.boxId = boxId;
        return this;
    }

    public String getCompBan() {
        return compBan;
    }

    public McdPrfBox setCompBan(String compBan) {
        this.compBan = compBan;
        return this;
    }

    public String getLangId() {
        return langId;
    }

    public McdPrfBox setLangId(String langId) {
        this.langId = langId;
        return this;
    }

    public String getBoxName() {
        return boxName;
    }

    public McdPrfBox setBoxName(String boxName) {
        this.boxName = boxName;
        return this;
    }

    public Integer getDimensionDeep() {
        return dimensionDeep;
    }

    public McdPrfBox setDimensionDeep(Integer dimensionDeep) {
        this.dimensionDeep = dimensionDeep;
        return this;
    }

    public Integer getDimensionWidth() {
        return dimensionWidth;
    }

    public McdPrfBox setDimensionWidth(Integer dimensionWidth) {
        this.dimensionWidth = dimensionWidth;
        return this;
    }

    public Integer getDimensionHeight() {
        return dimensionHeight;
    }

    public McdPrfBox setDimensionHeight(Integer dimensionHeight) {
        this.dimensionHeight = dimensionHeight;
        return this;
    }

    public String getDimensionUnit() {
        return dimensionUnit;
    }

    public McdPrfBox setDimensionUnit(String dimensionUnit) {
        this.dimensionUnit = dimensionUnit;
        return this;
    }

    public String getPackMaterial() {
        return packMaterial;
    }

    public McdPrfBox setPackMaterial(String packMaterial) {
        this.packMaterial = packMaterial;
        return this;
    }

    public Integer getSortIndex() {
        return sortIndex;
    }

    public McdPrfBox setSortIndex(Integer sortIndex) {
        this.sortIndex = sortIndex;
        return this;
    }

    public String getCreateUser() {
        return createUser;
    }

    public McdPrfBox setCreateUser(String createUser) {
        this.createUser = createUser;
        return this;
    }

    public String getCreateTime() {
        return createTime;
    }

    public McdPrfBox setCreateTime(String createTime) {
        this.createTime = createTime;
        return this;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public McdPrfBox setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
        return this;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public McdPrfBox setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
        return this;
    }

    public String getSystemMemo() {
        return systemMemo;
    }

    public McdPrfBox setSystemMemo(String systemMemo) {
        this.systemMemo = systemMemo;
        return this;
    }

    @Override
    protected Serializable pkVal() {
        return this.boxId;
    }

    @Override
    public String toString() {
        return "McdPrfBox{" +
        "boxId=" + boxId +
        ", compBan=" + compBan +
        ", langId=" + langId +
        ", boxName=" + boxName +
        ", dimensionDeep=" + dimensionDeep +
        ", dimensionWidth=" + dimensionWidth +
        ", dimensionHeight=" + dimensionHeight +
        ", dimensionUnit=" + dimensionUnit +
        ", packMaterial=" + packMaterial +
        ", sortIndex=" + sortIndex +
        ", createUser=" + createUser +
        ", createTime=" + createTime +
        ", updateUser=" + updateUser +
        ", updateTime=" + updateTime +
        ", systemMemo=" + systemMemo +
        "}";
    }
}
