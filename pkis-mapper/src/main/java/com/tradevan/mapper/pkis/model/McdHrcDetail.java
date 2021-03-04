package com.tradevan.mapper.pkis.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

/**
 * <p>
 * 商品層級明細檔
 * </p>
 *
 * @author penny
 * @since 2020-03-04
 */
@TableName("MCD_HRC_DETAIL")
public class McdHrcDetail extends Model<McdHrcDetail> {

    private static final long serialVersionUID=1L;

    @TableId("DETAIL_ID")
    private String detailId;

    @TableField("MASTER_ID")
    private String masterId;

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
     * 單位
     */
    @TableField("DIMENSION_UNIT")
    private String dimensionUnit;

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
     * 單位
     */
    @TableField("WEIGHT_UNIT")
    private String weightUnit;

    /**
     * 淨容量
     */
    @TableField("CAPACITY_NET")
    private Integer capacityNet;

    /**
     * 毛容量
     */
    @TableField("CAPACITY_GROSS")
    private Integer capacityGross;

    /**
     * 單位
     */
    @TableField("CAPACITY_UNIT")
    private String capacityUnit;

    /**
     * 銷售單位
     */
    @TableField("SALES_STATE")
    private String salesState;

    /**
     * 庫存單位
     */
    @TableField("BASE_UNIT")
    private String baseUnit;

    /**
     * 發票單位
     */
    @TableField("INVOICE_UNIT")
    private String invoiceUnit;

    /**
     * 訂貨單位
     */
    @TableField("ORDER_UNIT")
    private String orderUnit;

    /**
     * 訂貨倍數值
     */
    @TableField("ORDER_QUANTITY_MULTIPLE")
    private Integer orderQuantityMultiple;

    /**
     * 正常進價起日
     */
    @TableField("PURCHASE_PRICE_START_DATE")
    private String purchasePriceStartDate;

    /**
     * 促銷價
     */
    @TableField("PROMOTION_PRICE")
    private Integer promotionPrice;

    /**
     * 促銷起日
     */
    @TableField("PROMOTION_PRICE_START_DATE")
    private String promotionPriceStartDate;

    /**
     * 促銷結束日
     */
    @TableField("PROMOTION_PRICE_END_DATE")
    private String promotionPriceEndDate;

    /**
     * 單品總入數
     */
    @TableField("ENTRY_EA_NUM")
    private Integer entryEaNum;

    /**
     * 內容物入數
     */
    @TableField("ENTRY_NUM")
    private Integer entryNum;

    /**
     * 來源ID
     */
    @TableField("SOURCE_ID")
    private String sourceId;

    /**
     * 來源類型
     */
    @TableField("SOURCE_TYPE")
    private String sourceType;

    /**
     * 包裝層級排序：10-單品、20-組合包、30-箱
     */
    @TableField("SOURCE_LAYER")
    private String sourceLayer;

    /**
     * 規格，包含尺吋／容量／顏色／口味
     */
    @TableField("ITEM_SPECIFICATION_TW")
    private String itemSpecificationTw;

    /**
     * 條碼
     */
    @TableField("BARCODE")
    private String barcode;

    @TableField("CREATE_USER")
    private String createUser;

    @TableField("CREATE_TIME")
    private String createTime;

    @TableField("UPDATE_USER")
    private String updateUser;

    @TableField("UPDATE_TIME")
    private String updateTime;

    @TableField("SYSTEM_MEMO")
    private String systemMemo;

    @TableField("ITEM_CODE")
    private String itemCode;

    /**
     * 來源類型名稱
     */
    @TableField("SOURCE_TYPE_NAME")
    private String sourceTypeName;

    /**
     * 正常進價
     */
    @TableField("PURCHASE_PRICE")
    private Integer purchasePrice;

    /**
     * 每層箱數
     */
    @TableField("LAYER_NUM")
    private Integer layerNum;

    /**
     * 每板層數
     */
    @TableField("BOARD_LAYER")
    private Integer boardLayer;

    /**
     * 條碼類型
     */
    @TableField("BARCODE_TYPE")
    private String barcodeType;


    public String getDetailId() {
        return detailId;
    }

    public McdHrcDetail setDetailId(String detailId) {
        this.detailId = detailId;
        return this;
    }

    public String getMasterId() {
        return masterId;
    }

    public McdHrcDetail setMasterId(String masterId) {
        this.masterId = masterId;
        return this;
    }

    public String getPackMaterial() {
        return packMaterial;
    }

    public McdHrcDetail setPackMaterial(String packMaterial) {
        this.packMaterial = packMaterial;
        return this;
    }

    public String getPackUnit() {
        return packUnit;
    }

    public McdHrcDetail setPackUnit(String packUnit) {
        this.packUnit = packUnit;
        return this;
    }

    public Integer getDimensionDeep() {
        return dimensionDeep;
    }

    public McdHrcDetail setDimensionDeep(Integer dimensionDeep) {
        this.dimensionDeep = dimensionDeep;
        return this;
    }

    public Integer getDimensionWidth() {
        return dimensionWidth;
    }

    public McdHrcDetail setDimensionWidth(Integer dimensionWidth) {
        this.dimensionWidth = dimensionWidth;
        return this;
    }

    public Integer getDimensionHeight() {
        return dimensionHeight;
    }

    public McdHrcDetail setDimensionHeight(Integer dimensionHeight) {
        this.dimensionHeight = dimensionHeight;
        return this;
    }

    public String getDimensionUnit() {
        return dimensionUnit;
    }

    public McdHrcDetail setDimensionUnit(String dimensionUnit) {
        this.dimensionUnit = dimensionUnit;
        return this;
    }

    public Integer getWeightNet() {
        return weightNet;
    }

    public McdHrcDetail setWeightNet(Integer weightNet) {
        this.weightNet = weightNet;
        return this;
    }

    public Integer getWeightGross() {
        return weightGross;
    }

    public McdHrcDetail setWeightGross(Integer weightGross) {
        this.weightGross = weightGross;
        return this;
    }

    public String getWeightUnit() {
        return weightUnit;
    }

    public McdHrcDetail setWeightUnit(String weightUnit) {
        this.weightUnit = weightUnit;
        return this;
    }

    public Integer getCapacityNet() {
        return capacityNet;
    }

    public McdHrcDetail setCapacityNet(Integer capacityNet) {
        this.capacityNet = capacityNet;
        return this;
    }

    public Integer getCapacityGross() {
        return capacityGross;
    }

    public McdHrcDetail setCapacityGross(Integer capacityGross) {
        this.capacityGross = capacityGross;
        return this;
    }

    public String getCapacityUnit() {
        return capacityUnit;
    }

    public McdHrcDetail setCapacityUnit(String capacityUnit) {
        this.capacityUnit = capacityUnit;
        return this;
    }

    public String getSalesState() {
        return salesState;
    }

    public McdHrcDetail setSalesState(String salesState) {
        this.salesState = salesState;
        return this;
    }

    public String getBaseUnit() {
        return baseUnit;
    }

    public McdHrcDetail setBaseUnit(String baseUnit) {
        this.baseUnit = baseUnit;
        return this;
    }

    public String getInvoiceUnit() {
        return invoiceUnit;
    }

    public McdHrcDetail setInvoiceUnit(String invoiceUnit) {
        this.invoiceUnit = invoiceUnit;
        return this;
    }

    public String getOrderUnit() {
        return orderUnit;
    }

    public McdHrcDetail setOrderUnit(String orderUnit) {
        this.orderUnit = orderUnit;
        return this;
    }

    public Integer getOrderQuantityMultiple() {
        return orderQuantityMultiple;
    }

    public McdHrcDetail setOrderQuantityMultiple(Integer orderQuantityMultiple) {
        this.orderQuantityMultiple = orderQuantityMultiple;
        return this;
    }

    public String getPurchasePriceStartDate() {
        return purchasePriceStartDate;
    }

    public McdHrcDetail setPurchasePriceStartDate(String purchasePriceStartDate) {
        this.purchasePriceStartDate = purchasePriceStartDate;
        return this;
    }

    public Integer getPromotionPrice() {
        return promotionPrice;
    }

    public McdHrcDetail setPromotionPrice(Integer promotionPrice) {
        this.promotionPrice = promotionPrice;
        return this;
    }

    public String getPromotionPriceStartDate() {
        return promotionPriceStartDate;
    }

    public McdHrcDetail setPromotionPriceStartDate(String promotionPriceStartDate) {
        this.promotionPriceStartDate = promotionPriceStartDate;
        return this;
    }

    public String getPromotionPriceEndDate() {
        return promotionPriceEndDate;
    }

    public McdHrcDetail setPromotionPriceEndDate(String promotionPriceEndDate) {
        this.promotionPriceEndDate = promotionPriceEndDate;
        return this;
    }

    public Integer getEntryEaNum() {
        return entryEaNum;
    }

    public McdHrcDetail setEntryEaNum(Integer entryEaNum) {
        this.entryEaNum = entryEaNum;
        return this;
    }

    public Integer getEntryNum() {
        return entryNum;
    }

    public McdHrcDetail setEntryNum(Integer entryNum) {
        this.entryNum = entryNum;
        return this;
    }

    public String getSourceId() {
        return sourceId;
    }

    public McdHrcDetail setSourceId(String sourceId) {
        this.sourceId = sourceId;
        return this;
    }

    public String getSourceType() {
        return sourceType;
    }

    public McdHrcDetail setSourceType(String sourceType) {
        this.sourceType = sourceType;
        return this;
    }

    public String getSourceLayer() {
        return sourceLayer;
    }

    public McdHrcDetail setSourceLayer(String sourceLayer) {
        this.sourceLayer = sourceLayer;
        return this;
    }

    public String getItemSpecificationTw() {
        return itemSpecificationTw;
    }

    public McdHrcDetail setItemSpecificationTw(String itemSpecificationTw) {
        this.itemSpecificationTw = itemSpecificationTw;
        return this;
    }

    public String getBarcode() {
        return barcode;
    }

    public McdHrcDetail setBarcode(String barcode) {
        this.barcode = barcode;
        return this;
    }

    public String getCreateUser() {
        return createUser;
    }

    public McdHrcDetail setCreateUser(String createUser) {
        this.createUser = createUser;
        return this;
    }

    public String getCreateTime() {
        return createTime;
    }

    public McdHrcDetail setCreateTime(String createTime) {
        this.createTime = createTime;
        return this;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public McdHrcDetail setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
        return this;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public McdHrcDetail setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
        return this;
    }

    public String getSystemMemo() {
        return systemMemo;
    }

    public McdHrcDetail setSystemMemo(String systemMemo) {
        this.systemMemo = systemMemo;
        return this;
    }

    public String getItemCode() {
        return itemCode;
    }

    public McdHrcDetail setItemCode(String itemCode) {
        this.itemCode = itemCode;
        return this;
    }

    public String getSourceTypeName() {
        return sourceTypeName;
    }

    public McdHrcDetail setSourceTypeName(String sourceTypeName) {
        this.sourceTypeName = sourceTypeName;
        return this;
    }

    public Integer getPurchasePrice() {
        return purchasePrice;
    }

    public McdHrcDetail setPurchasePrice(Integer purchasePrice) {
        this.purchasePrice = purchasePrice;
        return this;
    }

    public Integer getLayerNum() {
        return layerNum;
    }

    public McdHrcDetail setLayerNum(Integer layerNum) {
        this.layerNum = layerNum;
        return this;
    }

    public Integer getBoardLayer() {
        return boardLayer;
    }

    public McdHrcDetail setBoardLayer(Integer boardLayer) {
        this.boardLayer = boardLayer;
        return this;
    }

    public String getBarcodeType() {
        return barcodeType;
    }

    public McdHrcDetail setBarcodeType(String barcodeType) {
        this.barcodeType = barcodeType;
        return this;
    }

    @Override
    protected Serializable pkVal() {
        return this.detailId;
    }

    @Override
    public String toString() {
        return "McdHrcDetail{" +
        "detailId=" + detailId +
        ", masterId=" + masterId +
        ", packMaterial=" + packMaterial +
        ", packUnit=" + packUnit +
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
        ", salesState=" + salesState +
        ", baseUnit=" + baseUnit +
        ", invoiceUnit=" + invoiceUnit +
        ", orderUnit=" + orderUnit +
        ", orderQuantityMultiple=" + orderQuantityMultiple +
        ", purchasePriceStartDate=" + purchasePriceStartDate +
        ", promotionPrice=" + promotionPrice +
        ", promotionPriceStartDate=" + promotionPriceStartDate +
        ", promotionPriceEndDate=" + promotionPriceEndDate +
        ", entryEaNum=" + entryEaNum +
        ", entryNum=" + entryNum +
        ", sourceId=" + sourceId +
        ", sourceType=" + sourceType +
        ", sourceLayer=" + sourceLayer +
        ", itemSpecificationTw=" + itemSpecificationTw +
        ", barcode=" + barcode +
        ", createUser=" + createUser +
        ", createTime=" + createTime +
        ", updateUser=" + updateUser +
        ", updateTime=" + updateTime +
        ", systemMemo=" + systemMemo +
        ", itemCode=" + itemCode +
        ", sourceTypeName=" + sourceTypeName +
        ", purchasePrice=" + purchasePrice +
        ", layerNum=" + layerNum +
        ", boardLayer=" + boardLayer +
        ", barcodeType=" + barcodeType +
        "}";
    }
}
