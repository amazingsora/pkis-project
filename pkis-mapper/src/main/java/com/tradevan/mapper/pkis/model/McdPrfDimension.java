package com.tradevan.mapper.pkis.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

/**
 * <p>
 * 尺吋容量設定檔
 * </p>
 *
 * @author penny
 * @since 2020-03-04
 */
@TableName("MCD_PRF_DIMENSION")
public class McdPrfDimension extends Model<McdPrfDimension> {

    private static final long serialVersionUID=1L;

    @TableId("DIMENSION_ID")
    private String dimensionId;

    /**
     * 供應商統編
     */
    @TableField("COMP_BAN")
    private String compBan;

    /**
     * 數量，數字為空白時，表示單位為Ｓ、Ｍ、Ｌ、ＸＬ。
     */
    @TableField("DIMENSION_NUM")
    private Integer dimensionNum;

    /**
     * 單位，有包含單位為Ｓ、Ｍ、Ｌ、ＸＬ。
     */
    @TableField("DIMENSION_UNIT")
    private String dimensionUnit;

    /**
     * 大分類
     */
    @TableField("CATE1")
    private String cate1;

    /**
     * 中分類
     */
    @TableField("CATE2")
    private String cate2;

    /**
     * 小分類
     */
    @TableField("CATE3")
    private String cate3;

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


    public String getDimensionId() {
        return dimensionId;
    }

    public McdPrfDimension setDimensionId(String dimensionId) {
        this.dimensionId = dimensionId;
        return this;
    }

    public String getCompBan() {
        return compBan;
    }

    public McdPrfDimension setCompBan(String compBan) {
        this.compBan = compBan;
        return this;
    }

    public Integer getDimensionNum() {
        return dimensionNum;
    }

    public McdPrfDimension setDimensionNum(Integer dimensionNum) {
        this.dimensionNum = dimensionNum;
        return this;
    }

    public String getDimensionUnit() {
        return dimensionUnit;
    }

    public McdPrfDimension setDimensionUnit(String dimensionUnit) {
        this.dimensionUnit = dimensionUnit;
        return this;
    }

    public String getCate1() {
        return cate1;
    }

    public McdPrfDimension setCate1(String cate1) {
        this.cate1 = cate1;
        return this;
    }

    public String getCate2() {
        return cate2;
    }

    public McdPrfDimension setCate2(String cate2) {
        this.cate2 = cate2;
        return this;
    }

    public String getCate3() {
        return cate3;
    }

    public McdPrfDimension setCate3(String cate3) {
        this.cate3 = cate3;
        return this;
    }

    public Integer getSortIndex() {
        return sortIndex;
    }

    public McdPrfDimension setSortIndex(Integer sortIndex) {
        this.sortIndex = sortIndex;
        return this;
    }

    public String getCreateUser() {
        return createUser;
    }

    public McdPrfDimension setCreateUser(String createUser) {
        this.createUser = createUser;
        return this;
    }

    public String getCreateTime() {
        return createTime;
    }

    public McdPrfDimension setCreateTime(String createTime) {
        this.createTime = createTime;
        return this;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public McdPrfDimension setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
        return this;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public McdPrfDimension setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
        return this;
    }

    public String getSystemMemo() {
        return systemMemo;
    }

    public McdPrfDimension setSystemMemo(String systemMemo) {
        this.systemMemo = systemMemo;
        return this;
    }

    @Override
    protected Serializable pkVal() {
        return this.dimensionId;
    }

    @Override
    public String toString() {
        return "McdPrfDimension{" +
        "dimensionId=" + dimensionId +
        ", compBan=" + compBan +
        ", dimensionNum=" + dimensionNum +
        ", dimensionUnit=" + dimensionUnit +
        ", cate1=" + cate1 +
        ", cate2=" + cate2 +
        ", cate3=" + cate3 +
        ", sortIndex=" + sortIndex +
        ", createUser=" + createUser +
        ", createTime=" + createTime +
        ", updateUser=" + updateUser +
        ", updateTime=" + updateTime +
        ", systemMemo=" + systemMemo +
        "}";
    }
}
