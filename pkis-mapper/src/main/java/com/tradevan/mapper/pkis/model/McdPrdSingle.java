package com.tradevan.mapper.pkis.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

/**
 * <p>
 * 單品關聯檔
 * </p>
 *
 * @author penny
 * @since 2020-03-04
 */
@TableName("MCD_PRD_SINGLE")
public class McdPrdSingle extends Model<McdPrdSingle> {

    private static final long serialVersionUID=1L;

    /**
     * 流水號：SINGE_ID(18碼)+數量(2碼)=20碼
     */
    @TableId("SINGLE_SUBID")
    private String singleSubid;

    /**
     * 單品流水號
     */
    @TableField("SINGLE_ID")
    private String singleId;

    /**
     * 品項ID
     */
    @TableField("ITEM_CODE")
    private String itemCode;

    /**
     * 複合商品項次品項ID
     */
    @TableField("PARENT_ITEM_CODE")
    private String parentItemCode;

    /**
     * normal:一般
size;尺吋
color:顏色
complex:綜合
     */
    @TableField("PACK_TYPE")
    private String packType;

    /**
     * 尺吋容量ID
     */
    @TableField("DIMENSION_ID")
    private String dimensionId;

    /**
     * 顏色ID
     */
    @TableField("COLOR_ID")
    private String colorId;

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

    @TableField("ENTRY_NUM")
    private Integer entryNum;


    public String getSingleSubid() {
        return singleSubid;
    }

    public McdPrdSingle setSingleSubid(String singleSubid) {
        this.singleSubid = singleSubid;
        return this;
    }

    public String getSingleId() {
        return singleId;
    }

    public McdPrdSingle setSingleId(String singleId) {
        this.singleId = singleId;
        return this;
    }

    public String getItemCode() {
        return itemCode;
    }

    public McdPrdSingle setItemCode(String itemCode) {
        this.itemCode = itemCode;
        return this;
    }

    public String getParentItemCode() {
        return parentItemCode;
    }

    public McdPrdSingle setParentItemCode(String parentItemCode) {
        this.parentItemCode = parentItemCode;
        return this;
    }

    public String getPackType() {
        return packType;
    }

    public McdPrdSingle setPackType(String packType) {
        this.packType = packType;
        return this;
    }

    public String getDimensionId() {
        return dimensionId;
    }

    public McdPrdSingle setDimensionId(String dimensionId) {
        this.dimensionId = dimensionId;
        return this;
    }

    public String getColorId() {
        return colorId;
    }

    public McdPrdSingle setColorId(String colorId) {
        this.colorId = colorId;
        return this;
    }

    public String getCreateUser() {
        return createUser;
    }

    public McdPrdSingle setCreateUser(String createUser) {
        this.createUser = createUser;
        return this;
    }

    public String getCreateTime() {
        return createTime;
    }

    public McdPrdSingle setCreateTime(String createTime) {
        this.createTime = createTime;
        return this;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public McdPrdSingle setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
        return this;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public McdPrdSingle setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
        return this;
    }

    public String getSystemMemo() {
        return systemMemo;
    }

    public McdPrdSingle setSystemMemo(String systemMemo) {
        this.systemMemo = systemMemo;
        return this;
    }

    public Integer getEntryNum() {
        return entryNum;
    }

    public McdPrdSingle setEntryNum(Integer entryNum) {
        this.entryNum = entryNum;
        return this;
    }

    @Override
    protected Serializable pkVal() {
        return this.singleSubid;
    }

    @Override
    public String toString() {
        return "McdPrdSingle{" +
        "singleSubid=" + singleSubid +
        ", singleId=" + singleId +
        ", itemCode=" + itemCode +
        ", parentItemCode=" + parentItemCode +
        ", packType=" + packType +
        ", dimensionId=" + dimensionId +
        ", colorId=" + colorId +
        ", createUser=" + createUser +
        ", createTime=" + createTime +
        ", updateUser=" + updateUser +
        ", updateTime=" + updateTime +
        ", systemMemo=" + systemMemo +
        ", entryNum=" + entryNum +
        "}";
    }
}
