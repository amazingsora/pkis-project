package com.tradevan.mapper.pkis.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

/**
 * <p>
 * 單品包裝資料檔
 * </p>
 *
 * @author penny
 * @since 2020-03-04
 */
@TableName("MCD_ITM_SINGLE")
public class McdItmSingle extends Model<McdItmSingle> {

    private static final long serialVersionUID=1L;

    /**
     * 單品主檔流水號：ITEM_CODE(14碼)+包裝方式(EA-單品，PK-組合包，CA-箱，PL-棧板)+數量(2碼)=18碼
     */
    @TableId("SINGLE_ID")
    private String singleId;

    /**
     * 品項代碼
     */
    @TableField("ITEM_CODE")
    private String itemCode;

    /**
     * 商品型態：A-單一商品、M-複合綜合、F-贈品。
     */
    @TableField("ITEM_TYPE")
    private String itemType;

    /**
     * normal:一般
size;尺吋
color:顏色
complex:綜合
     */
    @TableField("PACK_TYPE")
    private String packType;

    /**
     * 商品狀態：D-刪除
     */
    @TableField("STATUS")
    private String status;

    /**
     * 包裝材質
     */
    @TableField("PACK_MATERIAL")
    private String packMaterial;

    /**
     * 包裝單位
     */
    @TableField("PACK_UNIT")
    private String packUnit;

    /**
     * 是否為銷售單位(上架商品)
     */
    @TableField("SALES_STATE")
    private String salesState;

    /**
     * 尺吋容量ID
     */
    @TableField("DIMENSION_ID")
    private String dimensionId;

    /**
     * 單品_深長
     */
    @TableField("DIMENSION_DEEP")
    private Integer dimensionDeep;

    /**
     * 單品_面寬
     */
    @TableField("DIMENSION_WIDTH")
    private Integer dimensionWidth;

    /**
     * 單品_高度
     */
    @TableField("DIMENSION_HEIGHT")
    private Integer dimensionHeight;

    /**
     * 單品_尺吋單位
     */
    @TableField("DIMENSION_UNIT")
    private String dimensionUnit;

    /**
     * 單品_淨重
     */
    @TableField("WEIGHT_NET")
    private Integer weightNet;

    /**
     * 單品_毛重
     */
    @TableField("WEIGHT_GROSS")
    private Integer weightGross;

    /**
     * 單品_重量單位
     */
    @TableField("WEIGHT_UNIT")
    private String weightUnit;

    /**
     * 單品_淨容量
     */
    @TableField("CAPACITY_NET")
    private Integer capacityNet;

    /**
     * 單品_毛容量
     */
    @TableField("CAPACITY_GROSS")
    private Integer capacityGross;

    /**
     * 單品_容量單位
     */
    @TableField("CAPACITY_UNIT")
    private String capacityUnit;

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
     * 正常進價起始日
     */
    @TableField("PURCHASE_PRICE_START_DATE")
    private String purchasePriceStartDate;

    /**
     * 促銷進價
     */
    @TableField("PROMOTION_PRICE")
    private Integer promotionPrice;

    /**
     * 促銷進價起日
     */
    @TableField("PROMOTION_PRICE_START_DATE")
    private String promotionPriceStartDate;

    /**
     * 促銷進價迄日
     */
    @TableField("PROMOTION_PRICE_END_DATE")
    private String promotionPriceEndDate;

    /**
     * 顏色或口味註記
     */
    @TableField("COLOR_OR_TASTE")
    private String colorOrTaste;


    public String getSingleId() {
        return singleId;
    }

    public McdItmSingle setSingleId(String singleId) {
        this.singleId = singleId;
        return this;
    }

    public String getItemCode() {
        return itemCode;
    }

    public McdItmSingle setItemCode(String itemCode) {
        this.itemCode = itemCode;
        return this;
    }

    public String getItemType() {
        return itemType;
    }

    public McdItmSingle setItemType(String itemType) {
        this.itemType = itemType;
        return this;
    }

    public String getPackType() {
        return packType;
    }

    public McdItmSingle setPackType(String packType) {
        this.packType = packType;
        return this;
    }

    public String getStatus() {
        return status;
    }

    public McdItmSingle setStatus(String status) {
        this.status = status;
        return this;
    }

    public String getPackMaterial() {
        return packMaterial;
    }

    public McdItmSingle setPackMaterial(String packMaterial) {
        this.packMaterial = packMaterial;
        return this;
    }

    public String getPackUnit() {
        return packUnit;
    }

    public McdItmSingle setPackUnit(String packUnit) {
        this.packUnit = packUnit;
        return this;
    }

    public String getSalesState() {
        return salesState;
    }

    public McdItmSingle setSalesState(String salesState) {
        this.salesState = salesState;
        return this;
    }

    public String getDimensionId() {
        return dimensionId;
    }

    public McdItmSingle setDimensionId(String dimensionId) {
        this.dimensionId = dimensionId;
        return this;
    }

    public Integer getDimensionDeep() {
        return dimensionDeep;
    }

    public McdItmSingle setDimensionDeep(Integer dimensionDeep) {
        this.dimensionDeep = dimensionDeep;
        return this;
    }

    public Integer getDimensionWidth() {
        return dimensionWidth;
    }

    public McdItmSingle setDimensionWidth(Integer dimensionWidth) {
        this.dimensionWidth = dimensionWidth;
        return this;
    }

    public Integer getDimensionHeight() {
        return dimensionHeight;
    }

    public McdItmSingle setDimensionHeight(Integer dimensionHeight) {
        this.dimensionHeight = dimensionHeight;
        return this;
    }

    public String getDimensionUnit() {
        return dimensionUnit;
    }

    public McdItmSingle setDimensionUnit(String dimensionUnit) {
        this.dimensionUnit = dimensionUnit;
        return this;
    }

    public Integer getWeightNet() {
        return weightNet;
    }

    public McdItmSingle setWeightNet(Integer weightNet) {
        this.weightNet = weightNet;
        return this;
    }

    public Integer getWeightGross() {
        return weightGross;
    }

    public McdItmSingle setWeightGross(Integer weightGross) {
        this.weightGross = weightGross;
        return this;
    }

    public String getWeightUnit() {
        return weightUnit;
    }

    public McdItmSingle setWeightUnit(String weightUnit) {
        this.weightUnit = weightUnit;
        return this;
    }

    public Integer getCapacityNet() {
        return capacityNet;
    }

    public McdItmSingle setCapacityNet(Integer capacityNet) {
        this.capacityNet = capacityNet;
        return this;
    }

    public Integer getCapacityGross() {
        return capacityGross;
    }

    public McdItmSingle setCapacityGross(Integer capacityGross) {
        this.capacityGross = capacityGross;
        return this;
    }

    public String getCapacityUnit() {
        return capacityUnit;
    }

    public McdItmSingle setCapacityUnit(String capacityUnit) {
        this.capacityUnit = capacityUnit;
        return this;
    }

    public String getCreateUser() {
        return createUser;
    }

    public McdItmSingle setCreateUser(String createUser) {
        this.createUser = createUser;
        return this;
    }

    public String getCreateTime() {
        return createTime;
    }

    public McdItmSingle setCreateTime(String createTime) {
        this.createTime = createTime;
        return this;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public McdItmSingle setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
        return this;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public McdItmSingle setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
        return this;
    }

    public String getSystemMemo() {
        return systemMemo;
    }

    public McdItmSingle setSystemMemo(String systemMemo) {
        this.systemMemo = systemMemo;
        return this;
    }

    public String getBaseUnit() {
        return baseUnit;
    }

    public McdItmSingle setBaseUnit(String baseUnit) {
        this.baseUnit = baseUnit;
        return this;
    }

    public String getInvoiceUnit() {
        return invoiceUnit;
    }

    public McdItmSingle setInvoiceUnit(String invoiceUnit) {
        this.invoiceUnit = invoiceUnit;
        return this;
    }

    public String getOrderUnit() {
        return orderUnit;
    }

    public McdItmSingle setOrderUnit(String orderUnit) {
        this.orderUnit = orderUnit;
        return this;
    }

    public Integer getOrderQuantityMultiple() {
        return orderQuantityMultiple;
    }

    public McdItmSingle setOrderQuantityMultiple(Integer orderQuantityMultiple) {
        this.orderQuantityMultiple = orderQuantityMultiple;
        return this;
    }

    public String getPurchasePriceStartDate() {
        return purchasePriceStartDate;
    }

    public McdItmSingle setPurchasePriceStartDate(String purchasePriceStartDate) {
        this.purchasePriceStartDate = purchasePriceStartDate;
        return this;
    }

    public Integer getPromotionPrice() {
        return promotionPrice;
    }

    public McdItmSingle setPromotionPrice(Integer promotionPrice) {
        this.promotionPrice = promotionPrice;
        return this;
    }

    public String getPromotionPriceStartDate() {
        return promotionPriceStartDate;
    }

    public McdItmSingle setPromotionPriceStartDate(String promotionPriceStartDate) {
        this.promotionPriceStartDate = promotionPriceStartDate;
        return this;
    }

    public String getPromotionPriceEndDate() {
        return promotionPriceEndDate;
    }

    public McdItmSingle setPromotionPriceEndDate(String promotionPriceEndDate) {
        this.promotionPriceEndDate = promotionPriceEndDate;
        return this;
    }

    public String getColorOrTaste() {
        return colorOrTaste;
    }

    public McdItmSingle setColorOrTaste(String colorOrTaste) {
        this.colorOrTaste = colorOrTaste;
        return this;
    }

    @Override
    protected Serializable pkVal() {
        return this.singleId;
    }

    @Override
    public String toString() {
        return "McdItmSingle{" +
        "singleId=" + singleId +
        ", itemCode=" + itemCode +
        ", itemType=" + itemType +
        ", packType=" + packType +
        ", status=" + status +
        ", packMaterial=" + packMaterial +
        ", packUnit=" + packUnit +
        ", salesState=" + salesState +
        ", dimensionId=" + dimensionId +
        ", dimensionDeep=" + dimensionDeep +
        ", dimensionWidth=" + dimensionWidth +
        ", dimensionHeight=" + dimensionHeight +
        ", dimensionUnit=" + dimensionUnit +
        ", weightNet=" + weightNet +
        ", weightGross=" + weightGross +
        ", weightUnit=" + weightUnit +
        ", capacityNet=" + capacityNet +
        ", capacityGross=" + capacityGross +
        ", capacityUnit=" + capacityUnit +
        ", createUser=" + createUser +
        ", createTime=" + createTime +
        ", updateUser=" + updateUser +
        ", updateTime=" + updateTime +
        ", systemMemo=" + systemMemo +
        ", baseUnit=" + baseUnit +
        ", invoiceUnit=" + invoiceUnit +
        ", orderUnit=" + orderUnit +
        ", orderQuantityMultiple=" + orderQuantityMultiple +
        ", purchasePriceStartDate=" + purchasePriceStartDate +
        ", promotionPrice=" + promotionPrice +
        ", promotionPriceStartDate=" + promotionPriceStartDate +
        ", promotionPriceEndDate=" + promotionPriceEndDate +
        ", colorOrTaste=" + colorOrTaste +
        "}";
    }
}
