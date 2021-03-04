package com.tradevan.mapper.pkis.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

/**
 * <p>
 * 箱的關聯檔
 * </p>
 *
 * @author penny
 * @since 2020-03-04
 */
@TableName("MCD_PRD_BOX")
public class McdPrdBox extends Model<McdPrdBox> {

    private static final long serialVersionUID=1L;

    /**
     * 流水號：ITEM_CODE(14碼)+包裝方式(EA-單品，PK-組合包，CA-箱，PL-棧板)+數量(2碼)=18碼
     */
    @TableId("BOX_SUBID")
    private String boxSubid;

    /**
     * 箱設定檔ID
     */
    @TableField("BOX_ID")
    private String boxId;

    /**
     * 品項ID
     */
    @TableField("ITEM_CODE")
    private String itemCode;

    /**
     * 商品型態：A-單一商品、M-複合商品、F-贈品。
     */
    @TableField("ITEM_TYPE")
    private String itemType;

    /**
     * 商品狀態：D-刪除
     */
    @TableField("STATUS")
    private String status;

    /**
     * 來源ID
     */
    @TableField("SOURCE_ID")
    private String sourceId;

    /**
     * 包裝方式：EA-單品，PK-組合包，CA-箱，PL-棧板
     */
    @TableField("SOURCE_TYPE")
    private String sourceType;

    /**
     * 淨重
     */
    @TableField("WEIGHT_NET")
    private Integer weightNet;

    /**
     * 毛重
     */
    @TableField("WEIGHT_GROSS")
    private Integer weightGross;

    /**
     * 重量單位
     */
    @TableField("WEIGHT_UNIT")
    private String weightUnit;

    /**
     * 單品或組合包放進箱子的數量
     */
    @TableField("ENTRY_NUM")
    private Integer entryNum;

    /**
     * 是否為銷售單位，是：Ｙ、否：Ｎ
     */
    @TableField("SALES_STATE")
    private String salesState;

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
     * 是否為最小基礎單位(庫存單位)
     */
    @TableField("BASE_UNIT")
    private String baseUnit;

    /**
     * 是否為發票單位
     */
    @TableField("INVOICE_UNIT")
    private String invoiceUnit;

    /**
     * 是否為訂貨單位
     */
    @TableField("ORDER_UNIT")
    private String orderUnit;

    /**
     * 訂貨單位倍數值
     */
    @TableField("ORDER_QUANTITY_MULTIPLE")
    private Integer orderQuantityMultiple;

    /**
     * 被取代GTIN
     */
    @TableField("REPLACE_GTIN")
    private String replaceGtin;

    /**
     * 放進箱子單品的數量
     */
    @TableField("ENTRY_EA_NUM")
    private Integer entryEaNum;

    /**
     * 淨容量
     */
    @TableField("CAPACITY_NET")
    private Integer capacityNet;

    /**
     * 容量單位
     */
    @TableField("CAPACITY_UNIT")
    private String capacityUnit;


    public String getBoxSubid() {
        return boxSubid;
    }

    public McdPrdBox setBoxSubid(String boxSubid) {
        this.boxSubid = boxSubid;
        return this;
    }

    public String getBoxId() {
        return boxId;
    }

    public McdPrdBox setBoxId(String boxId) {
        this.boxId = boxId;
        return this;
    }

    public String getItemCode() {
        return itemCode;
    }

    public McdPrdBox setItemCode(String itemCode) {
        this.itemCode = itemCode;
        return this;
    }

    public String getItemType() {
        return itemType;
    }

    public McdPrdBox setItemType(String itemType) {
        this.itemType = itemType;
        return this;
    }

    public String getStatus() {
        return status;
    }

    public McdPrdBox setStatus(String status) {
        this.status = status;
        return this;
    }

    public String getSourceId() {
        return sourceId;
    }

    public McdPrdBox setSourceId(String sourceId) {
        this.sourceId = sourceId;
        return this;
    }

    public String getSourceType() {
        return sourceType;
    }

    public McdPrdBox setSourceType(String sourceType) {
        this.sourceType = sourceType;
        return this;
    }

    public Integer getWeightNet() {
        return weightNet;
    }

    public McdPrdBox setWeightNet(Integer weightNet) {
        this.weightNet = weightNet;
        return this;
    }

    public Integer getWeightGross() {
        return weightGross;
    }

    public McdPrdBox setWeightGross(Integer weightGross) {
        this.weightGross = weightGross;
        return this;
    }

    public String getWeightUnit() {
        return weightUnit;
    }

    public McdPrdBox setWeightUnit(String weightUnit) {
        this.weightUnit = weightUnit;
        return this;
    }

    public Integer getEntryNum() {
        return entryNum;
    }

    public McdPrdBox setEntryNum(Integer entryNum) {
        this.entryNum = entryNum;
        return this;
    }

    public String getSalesState() {
        return salesState;
    }

    public McdPrdBox setSalesState(String salesState) {
        this.salesState = salesState;
        return this;
    }

    public String getCreateUser() {
        return createUser;
    }

    public McdPrdBox setCreateUser(String createUser) {
        this.createUser = createUser;
        return this;
    }

    public String getCreateTime() {
        return createTime;
    }

    public McdPrdBox setCreateTime(String createTime) {
        this.createTime = createTime;
        return this;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public McdPrdBox setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
        return this;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public McdPrdBox setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
        return this;
    }

    public String getSystemMemo() {
        return systemMemo;
    }

    public McdPrdBox setSystemMemo(String systemMemo) {
        this.systemMemo = systemMemo;
        return this;
    }

    public String getBaseUnit() {
        return baseUnit;
    }

    public McdPrdBox setBaseUnit(String baseUnit) {
        this.baseUnit = baseUnit;
        return this;
    }

    public String getInvoiceUnit() {
        return invoiceUnit;
    }

    public McdPrdBox setInvoiceUnit(String invoiceUnit) {
        this.invoiceUnit = invoiceUnit;
        return this;
    }

    public String getOrderUnit() {
        return orderUnit;
    }

    public McdPrdBox setOrderUnit(String orderUnit) {
        this.orderUnit = orderUnit;
        return this;
    }

    public Integer getOrderQuantityMultiple() {
        return orderQuantityMultiple;
    }

    public McdPrdBox setOrderQuantityMultiple(Integer orderQuantityMultiple) {
        this.orderQuantityMultiple = orderQuantityMultiple;
        return this;
    }

    public String getReplaceGtin() {
        return replaceGtin;
    }

    public McdPrdBox setReplaceGtin(String replaceGtin) {
        this.replaceGtin = replaceGtin;
        return this;
    }

    public Integer getEntryEaNum() {
        return entryEaNum;
    }

    public McdPrdBox setEntryEaNum(Integer entryEaNum) {
        this.entryEaNum = entryEaNum;
        return this;
    }

    public Integer getCapacityNet() {
        return capacityNet;
    }

    public McdPrdBox setCapacityNet(Integer capacityNet) {
        this.capacityNet = capacityNet;
        return this;
    }

    public String getCapacityUnit() {
        return capacityUnit;
    }

    public McdPrdBox setCapacityUnit(String capacityUnit) {
        this.capacityUnit = capacityUnit;
        return this;
    }

    @Override
    protected Serializable pkVal() {
        return this.boxSubid;
    }

    @Override
    public String toString() {
        return "McdPrdBox{" +
        "boxSubid=" + boxSubid +
        ", boxId=" + boxId +
        ", itemCode=" + itemCode +
        ", itemType=" + itemType +
        ", status=" + status +
        ", sourceId=" + sourceId +
        ", sourceType=" + sourceType +
        ", weightNet=" + weightNet +
        ", weightGross=" + weightGross +
        ", weightUnit=" + weightUnit +
        ", entryNum=" + entryNum +
        ", salesState=" + salesState +
        ", createUser=" + createUser +
        ", createTime=" + createTime +
        ", updateUser=" + updateUser +
        ", updateTime=" + updateTime +
        ", systemMemo=" + systemMemo +
        ", baseUnit=" + baseUnit +
        ", invoiceUnit=" + invoiceUnit +
        ", orderUnit=" + orderUnit +
        ", orderQuantityMultiple=" + orderQuantityMultiple +
        ", replaceGtin=" + replaceGtin +
        ", entryEaNum=" + entryEaNum +
        ", capacityNet=" + capacityNet +
        ", capacityUnit=" + capacityUnit +
        "}";
    }
}
