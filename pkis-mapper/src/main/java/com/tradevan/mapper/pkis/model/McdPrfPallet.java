package com.tradevan.mapper.pkis.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

/**
 * <p>
 * 棧板設定檔

 * </p>
 *
 * @author penny
 * @since 2020-03-04
 */
@TableName("MCD_PRF_PALLET")
public class McdPrfPallet extends Model<McdPrfPallet> {

    private static final long serialVersionUID=1L;

    @TableId("PALLET_ID")
    private String palletId;

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
     * 棧板的名稱
     */
    @TableField("PALLET_NAME")
    private String palletName;

    /**
     * 材質
     */
    @TableField("MATERIAL")
    private String material;

    /**
     * 負荷重量
     */
    @TableField("LOAD_WEIGHT")
    private String loadWeight;

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


    public String getPalletId() {
        return palletId;
    }

    public McdPrfPallet setPalletId(String palletId) {
        this.palletId = palletId;
        return this;
    }

    public String getCompBan() {
        return compBan;
    }

    public McdPrfPallet setCompBan(String compBan) {
        this.compBan = compBan;
        return this;
    }

    public String getLangId() {
        return langId;
    }

    public McdPrfPallet setLangId(String langId) {
        this.langId = langId;
        return this;
    }

    public String getPalletName() {
        return palletName;
    }

    public McdPrfPallet setPalletName(String palletName) {
        this.palletName = palletName;
        return this;
    }

    public String getMaterial() {
        return material;
    }

    public McdPrfPallet setMaterial(String material) {
        this.material = material;
        return this;
    }

    public String getLoadWeight() {
        return loadWeight;
    }

    public McdPrfPallet setLoadWeight(String loadWeight) {
        this.loadWeight = loadWeight;
        return this;
    }

    public Integer getDimensionDeep() {
        return dimensionDeep;
    }

    public McdPrfPallet setDimensionDeep(Integer dimensionDeep) {
        this.dimensionDeep = dimensionDeep;
        return this;
    }

    public Integer getDimensionWidth() {
        return dimensionWidth;
    }

    public McdPrfPallet setDimensionWidth(Integer dimensionWidth) {
        this.dimensionWidth = dimensionWidth;
        return this;
    }

    public Integer getDimensionHeight() {
        return dimensionHeight;
    }

    public McdPrfPallet setDimensionHeight(Integer dimensionHeight) {
        this.dimensionHeight = dimensionHeight;
        return this;
    }

    public String getDimensionUnit() {
        return dimensionUnit;
    }

    public McdPrfPallet setDimensionUnit(String dimensionUnit) {
        this.dimensionUnit = dimensionUnit;
        return this;
    }

    public String getCreateUser() {
        return createUser;
    }

    public McdPrfPallet setCreateUser(String createUser) {
        this.createUser = createUser;
        return this;
    }

    public String getCreateTime() {
        return createTime;
    }

    public McdPrfPallet setCreateTime(String createTime) {
        this.createTime = createTime;
        return this;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public McdPrfPallet setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
        return this;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public McdPrfPallet setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
        return this;
    }

    public String getSystemMemo() {
        return systemMemo;
    }

    public McdPrfPallet setSystemMemo(String systemMemo) {
        this.systemMemo = systemMemo;
        return this;
    }

    @Override
    protected Serializable pkVal() {
        return this.palletId;
    }

    @Override
    public String toString() {
        return "McdPrfPallet{" +
        "palletId=" + palletId +
        ", compBan=" + compBan +
        ", langId=" + langId +
        ", palletName=" + palletName +
        ", material=" + material +
        ", loadWeight=" + loadWeight +
        ", dimensionDeep=" + dimensionDeep +
        ", dimensionWidth=" + dimensionWidth +
        ", dimensionHeight=" + dimensionHeight +
        ", dimensionUnit=" + dimensionUnit +
        ", createUser=" + createUser +
        ", createTime=" + createTime +
        ", updateUser=" + updateUser +
        ", updateTime=" + updateTime +
        ", systemMemo=" + systemMemo +
        "}";
    }
}
