package com.tradevan.mapper.pkis.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

/**
 * <p>
 * 商品層級發佈歷史檔
 * </p>
 *
 * @author penny
 * @since 2020-03-04
 */
@TableName("MCD_HRC_PUBLIC_HISTORY")
public class McdHrcPublicHistory extends Model<McdHrcPublicHistory> {

    private static final long serialVersionUID=1L;

    @TableField("MASTER_ID")
    private String masterId;

    /**
     * 品項代號
     */
    @TableField("ITEM_CODE")
    private String itemCode;

    /**
     * 家樂福部門
     */
    @TableField("RETAILER_DEPARTMENT")
    private String retailerDepartment;

    /**
     * 供應商廠編
     */
    @TableField("SUPPLIER_CODE")
    private String supplierCode;

    /**
     * 品牌
     */
    @TableField("BRAND")
    private String brand;

    /**
     * 原產地
     */
    @TableField("ORGN_COUNTRY")
    private String orgnCountry;

    /**
     * 商品狀態
     */
    @TableField("PRD_STATUS")
    private String prdStatus;

    /**
     * 開始供貨日期
     */
    @TableField("START_AVAILABILITY_DATE")
    private String startAvailabilityDate;

    /**
     * 停止供貨日期
     */
    @TableField("END_AVAILABILITY_DATE")
    private String endAvailabilityDate;

    /**
     * 停止供貨日期
     */
    @TableField("STOP_SUPPLIER_DESC_TW")
    private String stopSupplierDescTw;

    /**
     * 主要運送方式
     */
    @TableField("MAIN_SHIP_METHODS")
    private String mainShipMethods;

    /**
     * 商品型態：A：單品、M：複合綜合、F：贈品
     */
    @TableField("ITEM_TYPE")
    private String itemType;

    /**
     * 單品流水號
     */
    @TableField("SINGLE_ID")
    private String singleId;

    /**
     * 單品流水號細項編號
     */
    @TableField("SINGLE_SUBID")
    private String singleSubid;

    /**
     * 單->組->箱的綜合顯示
     */
    @TableField("HIERARCHY_LAYERNUM")
    private String hierarchyLayernum;

    /**
     * 單->組->箱
     */
    @TableField("HIERARCHY_LAYER")
    private String hierarchyLayer;

    /**
     * 單->組->箱的數量，換算到單品
     */
    @TableField("HIERARCHY_EA_NUM")
    private String hierarchyEaNum;

    /**
     * 發佈GTIN
     */
    @TableField("PUBLIC_GTIN")
    private String publicGtin;

    /**
     * 發佈商品名稱
     */
    @TableField("PUBLIC_PRD_GTIN_NAME_TW")
    private String publicPrdGtinNameTw;

    /**
     * 發佈商品名稱
     */
    @TableField("PUBLIC_PRD_GTIN_NAME_EN")
    private String publicPrdGtinNameEn;

    /**
     * 發佈狀態
     */
    @TableField("PUBLIC_STATUS")
    private String publicStatus;

    /**
     * 發佈時間
     */
    @TableField("PUBLIC_TIME")
    private String publicTime;

    /**
     * 退件原因
     */
    @TableField("REJECT_REASON")
    private String rejectReason;

    /**
     * 被取代GTIN
     */
    @TableField("REPLACE_GTIN")
    private String replaceGtin;

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

    /**
     * 規格，包含尺吋／容量／顏色／口味
     */
    @TableField("ITEM_SPECIFICATION_TW")
    private String itemSpecificationTw;

    /**
     * 細目名稱，包含尺吋／容量／顏色／口味
     */
    @TableField("ITEM_SPECIFICATION_EN")
    private String itemSpecificationEn;

    @TableField("DETAIL_ID")
    private String detailId;

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
     * 條碼
     */
    @TableField("BARCODE")
    private String barcode;

    /**
     * 條碼類型
     */
    @TableField("BARCODE_TYPE")
    private String barcodeType;

    /**
     * 流水序號
     */
    @TableId("HISTORY_ID")
    private String historyId;

    /**
     * 正常進價
     */
    @TableField("PURCHASE_PRICE")
    private Integer purchasePrice;

    /**
     * 每層箱數
     */
    @TableField("BOARD_LAYER")
    private Integer boardLayer;

    /**
     * 每板層數
     */
    @TableField("LAYER_NUM")
    private Integer layerNum;

    /**
     * 商品分類
     */
    @TableField("CATE_4")
    private String cate4;

    /**
     * CIN檔案識別碼(關貿產生商品提報檔案給FSE.net)
     */
    @TableField("UNIQUE_IDENTIFICATION_CIN")
    private String uniqueIdentificationCin;

    /**
     * CIN檔案存放的位置
     */
    @TableField("FILE_PATH_CIN")
    private String filePathCin;

    /**
     * CIN檔案的處理人員
     */
    @TableField("CREATE_USER_CIN")
    private String createUserCin;

    /**
     * CIN檔案產生的時間
     */
    @TableField("CREATE_TIME_CIN")
    private String createTimeCin;

    /**
     * CINR檔案識別碼(關貿接收FSE.net回覆)
     */
    @TableField("UNIQUE_IDENTIFICATION_CINR")
    private String uniqueIdentificationCinr;

    /**
     * CINR檔案中的<sh:DocumentIdentification>識別碼
     */
    @TableField("IDENTIFIER_DOCUMENT_CINR")
    private String identifierDocumentCinr;

    /**
     * CINR檔案中的<sh:BusinessScope>識別碼
     */
    @TableField("IDENTIFIER_BUSINESSSCOPE_CINR")
    private String identifierBusinessscopeCinr;

    /**
     * CINR檔案存放的位置
     */
    @TableField("FILE_PATH_CINR")
    private String filePathCinr;

    /**
     * CINR檔案的處理人員
     */
    @TableField("CREATE_USER_CINR")
    private String createUserCinr;

    /**
     * CINR檔案接收的時間
     */
    @TableField("CREATE_TIME_CINR")
    private String createTimeCinr;

    /**
     * CIC檔案識別碼(家樂福審核結果的檔案傳給關貿)
     */
    @TableField("UNIQUE_IDENTIFICATION_CIC")
    private String uniqueIdentificationCic;

    /**
     * CIC檔案存放的位置
     */
    @TableField("FILE_PATH_CIC")
    private String filePathCic;

    /**
     * CIC檔案的處理人員
     */
    @TableField("CREATE_USER_CIC")
    private String createUserCic;

    /**
     * CIC檔案接收的時間
     */
    @TableField("CREATE_TIME_CIC")
    private String createTimeCic;

    /**
     * CICR檔案識別碼(關貿回覆CIC接收完成)
     */
    @TableField("UNIQUE_IDENTIFICATION_CICR")
    private String uniqueIdentificationCicr;

    /**
     * CICR檔案存放的位置
     */
    @TableField("FILE_PATH_CICR")
    private String filePathCicr;

    /**
     * CICR檔案的處理人員
     */
    @TableField("CREATE_USER_CICR")
    private String createUserCicr;

    /**
     * CICR檔案回覆的時間
     */
    @TableField("CREATE_TIME_CICR")
    private String createTimeCicr;

    /**
     * 發佈商品細目
     */
    @TableField("PUBLIC_ITEM_SPECIFICATION_TW")
    private String publicItemSpecificationTw;

    /**
     * 家樂福箱入數
     */
    @TableField("CASE_ENTRY_NUM")
    private Integer caseEntryNum;

    /**
     * 發佈商品細目
     */
    @TableField("PUBLIC_ITEM_SPECIFICATION_EN")
    private String publicItemSpecificationEn;

    /**
     * 是否為秤重商品
     */
    @TableField("IS_VARIABLE")
    private String isVariable;

    /**
     * 是否為危險物品
     */
    @TableField("IS_DANGER")
    private String isDanger;

    /**
     * 稅率
     */
    @TableField("RATE")
    private String rate;

    /**
     * 商品描述(中文)
     */
    @TableField("PRD_DESCRIPTION_TW")
    private String prdDescriptionTw;

    /**
     * 商品描述(英文)
     */
    @TableField("PRD_DESCRIPTION_EN")
    private String prdDescriptionEn;

    @TableField("SUPPLIER_GLN")
    private String supplierGln;

    @TableField("COMPANY_NAME")
    private String companyName;


    public String getMasterId() {
        return masterId;
    }

    public McdHrcPublicHistory setMasterId(String masterId) {
        this.masterId = masterId;
        return this;
    }

    public String getItemCode() {
        return itemCode;
    }

    public McdHrcPublicHistory setItemCode(String itemCode) {
        this.itemCode = itemCode;
        return this;
    }

    public String getRetailerDepartment() {
        return retailerDepartment;
    }

    public McdHrcPublicHistory setRetailerDepartment(String retailerDepartment) {
        this.retailerDepartment = retailerDepartment;
        return this;
    }

    public String getSupplierCode() {
        return supplierCode;
    }

    public McdHrcPublicHistory setSupplierCode(String supplierCode) {
        this.supplierCode = supplierCode;
        return this;
    }

    public String getBrand() {
        return brand;
    }

    public McdHrcPublicHistory setBrand(String brand) {
        this.brand = brand;
        return this;
    }

    public String getOrgnCountry() {
        return orgnCountry;
    }

    public McdHrcPublicHistory setOrgnCountry(String orgnCountry) {
        this.orgnCountry = orgnCountry;
        return this;
    }

    public String getPrdStatus() {
        return prdStatus;
    }

    public McdHrcPublicHistory setPrdStatus(String prdStatus) {
        this.prdStatus = prdStatus;
        return this;
    }

    public String getStartAvailabilityDate() {
        return startAvailabilityDate;
    }

    public McdHrcPublicHistory setStartAvailabilityDate(String startAvailabilityDate) {
        this.startAvailabilityDate = startAvailabilityDate;
        return this;
    }

    public String getEndAvailabilityDate() {
        return endAvailabilityDate;
    }

    public McdHrcPublicHistory setEndAvailabilityDate(String endAvailabilityDate) {
        this.endAvailabilityDate = endAvailabilityDate;
        return this;
    }

    public String getStopSupplierDescTw() {
        return stopSupplierDescTw;
    }

    public McdHrcPublicHistory setStopSupplierDescTw(String stopSupplierDescTw) {
        this.stopSupplierDescTw = stopSupplierDescTw;
        return this;
    }

    public String getMainShipMethods() {
        return mainShipMethods;
    }

    public McdHrcPublicHistory setMainShipMethods(String mainShipMethods) {
        this.mainShipMethods = mainShipMethods;
        return this;
    }

    public String getItemType() {
        return itemType;
    }

    public McdHrcPublicHistory setItemType(String itemType) {
        this.itemType = itemType;
        return this;
    }

    public String getSingleId() {
        return singleId;
    }

    public McdHrcPublicHistory setSingleId(String singleId) {
        this.singleId = singleId;
        return this;
    }

    public String getSingleSubid() {
        return singleSubid;
    }

    public McdHrcPublicHistory setSingleSubid(String singleSubid) {
        this.singleSubid = singleSubid;
        return this;
    }

    public String getHierarchyLayernum() {
        return hierarchyLayernum;
    }

    public McdHrcPublicHistory setHierarchyLayernum(String hierarchyLayernum) {
        this.hierarchyLayernum = hierarchyLayernum;
        return this;
    }

    public String getHierarchyLayer() {
        return hierarchyLayer;
    }

    public McdHrcPublicHistory setHierarchyLayer(String hierarchyLayer) {
        this.hierarchyLayer = hierarchyLayer;
        return this;
    }

    public String getHierarchyEaNum() {
        return hierarchyEaNum;
    }

    public McdHrcPublicHistory setHierarchyEaNum(String hierarchyEaNum) {
        this.hierarchyEaNum = hierarchyEaNum;
        return this;
    }

    public String getPublicGtin() {
        return publicGtin;
    }

    public McdHrcPublicHistory setPublicGtin(String publicGtin) {
        this.publicGtin = publicGtin;
        return this;
    }

    public String getPublicPrdGtinNameTw() {
        return publicPrdGtinNameTw;
    }

    public McdHrcPublicHistory setPublicPrdGtinNameTw(String publicPrdGtinNameTw) {
        this.publicPrdGtinNameTw = publicPrdGtinNameTw;
        return this;
    }

    public String getPublicPrdGtinNameEn() {
        return publicPrdGtinNameEn;
    }

    public McdHrcPublicHistory setPublicPrdGtinNameEn(String publicPrdGtinNameEn) {
        this.publicPrdGtinNameEn = publicPrdGtinNameEn;
        return this;
    }

    public String getPublicStatus() {
        return publicStatus;
    }

    public McdHrcPublicHistory setPublicStatus(String publicStatus) {
        this.publicStatus = publicStatus;
        return this;
    }

    public String getPublicTime() {
        return publicTime;
    }

    public McdHrcPublicHistory setPublicTime(String publicTime) {
        this.publicTime = publicTime;
        return this;
    }

    public String getRejectReason() {
        return rejectReason;
    }

    public McdHrcPublicHistory setRejectReason(String rejectReason) {
        this.rejectReason = rejectReason;
        return this;
    }

    public String getReplaceGtin() {
        return replaceGtin;
    }

    public McdHrcPublicHistory setReplaceGtin(String replaceGtin) {
        this.replaceGtin = replaceGtin;
        return this;
    }

    public String getCreateUser() {
        return createUser;
    }

    public McdHrcPublicHistory setCreateUser(String createUser) {
        this.createUser = createUser;
        return this;
    }

    public String getCreateTime() {
        return createTime;
    }

    public McdHrcPublicHistory setCreateTime(String createTime) {
        this.createTime = createTime;
        return this;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public McdHrcPublicHistory setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
        return this;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public McdHrcPublicHistory setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
        return this;
    }

    public String getSystemMemo() {
        return systemMemo;
    }

    public McdHrcPublicHistory setSystemMemo(String systemMemo) {
        this.systemMemo = systemMemo;
        return this;
    }

    public String getItemSpecificationTw() {
        return itemSpecificationTw;
    }

    public McdHrcPublicHistory setItemSpecificationTw(String itemSpecificationTw) {
        this.itemSpecificationTw = itemSpecificationTw;
        return this;
    }

    public String getItemSpecificationEn() {
        return itemSpecificationEn;
    }

    public McdHrcPublicHistory setItemSpecificationEn(String itemSpecificationEn) {
        this.itemSpecificationEn = itemSpecificationEn;
        return this;
    }

    public String getDetailId() {
        return detailId;
    }

    public McdHrcPublicHistory setDetailId(String detailId) {
        this.detailId = detailId;
        return this;
    }

    public String getPackMaterial() {
        return packMaterial;
    }

    public McdHrcPublicHistory setPackMaterial(String packMaterial) {
        this.packMaterial = packMaterial;
        return this;
    }

    public String getPackUnit() {
        return packUnit;
    }

    public McdHrcPublicHistory setPackUnit(String packUnit) {
        this.packUnit = packUnit;
        return this;
    }

    public Integer getDimensionDeep() {
        return dimensionDeep;
    }

    public McdHrcPublicHistory setDimensionDeep(Integer dimensionDeep) {
        this.dimensionDeep = dimensionDeep;
        return this;
    }

    public Integer getDimensionWidth() {
        return dimensionWidth;
    }

    public McdHrcPublicHistory setDimensionWidth(Integer dimensionWidth) {
        this.dimensionWidth = dimensionWidth;
        return this;
    }

    public Integer getDimensionHeight() {
        return dimensionHeight;
    }

    public McdHrcPublicHistory setDimensionHeight(Integer dimensionHeight) {
        this.dimensionHeight = dimensionHeight;
        return this;
    }

    public String getDimensionUnit() {
        return dimensionUnit;
    }

    public McdHrcPublicHistory setDimensionUnit(String dimensionUnit) {
        this.dimensionUnit = dimensionUnit;
        return this;
    }

    public Integer getWeightNet() {
        return weightNet;
    }

    public McdHrcPublicHistory setWeightNet(Integer weightNet) {
        this.weightNet = weightNet;
        return this;
    }

    public Integer getWeightGross() {
        return weightGross;
    }

    public McdHrcPublicHistory setWeightGross(Integer weightGross) {
        this.weightGross = weightGross;
        return this;
    }

    public String getWeightUnit() {
        return weightUnit;
    }

    public McdHrcPublicHistory setWeightUnit(String weightUnit) {
        this.weightUnit = weightUnit;
        return this;
    }

    public Integer getCapacityNet() {
        return capacityNet;
    }

    public McdHrcPublicHistory setCapacityNet(Integer capacityNet) {
        this.capacityNet = capacityNet;
        return this;
    }

    public Integer getCapacityGross() {
        return capacityGross;
    }

    public McdHrcPublicHistory setCapacityGross(Integer capacityGross) {
        this.capacityGross = capacityGross;
        return this;
    }

    public String getCapacityUnit() {
        return capacityUnit;
    }

    public McdHrcPublicHistory setCapacityUnit(String capacityUnit) {
        this.capacityUnit = capacityUnit;
        return this;
    }

    public String getSalesState() {
        return salesState;
    }

    public McdHrcPublicHistory setSalesState(String salesState) {
        this.salesState = salesState;
        return this;
    }

    public String getBaseUnit() {
        return baseUnit;
    }

    public McdHrcPublicHistory setBaseUnit(String baseUnit) {
        this.baseUnit = baseUnit;
        return this;
    }

    public String getInvoiceUnit() {
        return invoiceUnit;
    }

    public McdHrcPublicHistory setInvoiceUnit(String invoiceUnit) {
        this.invoiceUnit = invoiceUnit;
        return this;
    }

    public String getOrderUnit() {
        return orderUnit;
    }

    public McdHrcPublicHistory setOrderUnit(String orderUnit) {
        this.orderUnit = orderUnit;
        return this;
    }

    public Integer getOrderQuantityMultiple() {
        return orderQuantityMultiple;
    }

    public McdHrcPublicHistory setOrderQuantityMultiple(Integer orderQuantityMultiple) {
        this.orderQuantityMultiple = orderQuantityMultiple;
        return this;
    }

    public String getPurchasePriceStartDate() {
        return purchasePriceStartDate;
    }

    public McdHrcPublicHistory setPurchasePriceStartDate(String purchasePriceStartDate) {
        this.purchasePriceStartDate = purchasePriceStartDate;
        return this;
    }

    public Integer getPromotionPrice() {
        return promotionPrice;
    }

    public McdHrcPublicHistory setPromotionPrice(Integer promotionPrice) {
        this.promotionPrice = promotionPrice;
        return this;
    }

    public String getPromotionPriceStartDate() {
        return promotionPriceStartDate;
    }

    public McdHrcPublicHistory setPromotionPriceStartDate(String promotionPriceStartDate) {
        this.promotionPriceStartDate = promotionPriceStartDate;
        return this;
    }

    public String getPromotionPriceEndDate() {
        return promotionPriceEndDate;
    }

    public McdHrcPublicHistory setPromotionPriceEndDate(String promotionPriceEndDate) {
        this.promotionPriceEndDate = promotionPriceEndDate;
        return this;
    }

    public Integer getEntryEaNum() {
        return entryEaNum;
    }

    public McdHrcPublicHistory setEntryEaNum(Integer entryEaNum) {
        this.entryEaNum = entryEaNum;
        return this;
    }

    public Integer getEntryNum() {
        return entryNum;
    }

    public McdHrcPublicHistory setEntryNum(Integer entryNum) {
        this.entryNum = entryNum;
        return this;
    }

    public String getSourceId() {
        return sourceId;
    }

    public McdHrcPublicHistory setSourceId(String sourceId) {
        this.sourceId = sourceId;
        return this;
    }

    public String getSourceType() {
        return sourceType;
    }

    public McdHrcPublicHistory setSourceType(String sourceType) {
        this.sourceType = sourceType;
        return this;
    }

    public String getSourceLayer() {
        return sourceLayer;
    }

    public McdHrcPublicHistory setSourceLayer(String sourceLayer) {
        this.sourceLayer = sourceLayer;
        return this;
    }

    public String getBarcode() {
        return barcode;
    }

    public McdHrcPublicHistory setBarcode(String barcode) {
        this.barcode = barcode;
        return this;
    }

    public String getBarcodeType() {
        return barcodeType;
    }

    public McdHrcPublicHistory setBarcodeType(String barcodeType) {
        this.barcodeType = barcodeType;
        return this;
    }

    public String getHistoryId() {
        return historyId;
    }

    public McdHrcPublicHistory setHistoryId(String historyId) {
        this.historyId = historyId;
        return this;
    }

    public Integer getPurchasePrice() {
        return purchasePrice;
    }

    public McdHrcPublicHistory setPurchasePrice(Integer purchasePrice) {
        this.purchasePrice = purchasePrice;
        return this;
    }

    public Integer getBoardLayer() {
        return boardLayer;
    }

    public McdHrcPublicHistory setBoardLayer(Integer boardLayer) {
        this.boardLayer = boardLayer;
        return this;
    }

    public Integer getLayerNum() {
        return layerNum;
    }

    public McdHrcPublicHistory setLayerNum(Integer layerNum) {
        this.layerNum = layerNum;
        return this;
    }

    public String getCate4() {
        return cate4;
    }

    public McdHrcPublicHistory setCate4(String cate4) {
        this.cate4 = cate4;
        return this;
    }

    public String getUniqueIdentificationCin() {
        return uniqueIdentificationCin;
    }

    public McdHrcPublicHistory setUniqueIdentificationCin(String uniqueIdentificationCin) {
        this.uniqueIdentificationCin = uniqueIdentificationCin;
        return this;
    }

    public String getFilePathCin() {
        return filePathCin;
    }

    public McdHrcPublicHistory setFilePathCin(String filePathCin) {
        this.filePathCin = filePathCin;
        return this;
    }

    public String getCreateUserCin() {
        return createUserCin;
    }

    public McdHrcPublicHistory setCreateUserCin(String createUserCin) {
        this.createUserCin = createUserCin;
        return this;
    }

    public String getCreateTimeCin() {
        return createTimeCin;
    }

    public McdHrcPublicHistory setCreateTimeCin(String createTimeCin) {
        this.createTimeCin = createTimeCin;
        return this;
    }

    public String getUniqueIdentificationCinr() {
        return uniqueIdentificationCinr;
    }

    public McdHrcPublicHistory setUniqueIdentificationCinr(String uniqueIdentificationCinr) {
        this.uniqueIdentificationCinr = uniqueIdentificationCinr;
        return this;
    }

    public String getIdentifierDocumentCinr() {
        return identifierDocumentCinr;
    }

    public McdHrcPublicHistory setIdentifierDocumentCinr(String identifierDocumentCinr) {
        this.identifierDocumentCinr = identifierDocumentCinr;
        return this;
    }

    public String getIdentifierBusinessscopeCinr() {
        return identifierBusinessscopeCinr;
    }

    public McdHrcPublicHistory setIdentifierBusinessscopeCinr(String identifierBusinessscopeCinr) {
        this.identifierBusinessscopeCinr = identifierBusinessscopeCinr;
        return this;
    }

    public String getFilePathCinr() {
        return filePathCinr;
    }

    public McdHrcPublicHistory setFilePathCinr(String filePathCinr) {
        this.filePathCinr = filePathCinr;
        return this;
    }

    public String getCreateUserCinr() {
        return createUserCinr;
    }

    public McdHrcPublicHistory setCreateUserCinr(String createUserCinr) {
        this.createUserCinr = createUserCinr;
        return this;
    }

    public String getCreateTimeCinr() {
        return createTimeCinr;
    }

    public McdHrcPublicHistory setCreateTimeCinr(String createTimeCinr) {
        this.createTimeCinr = createTimeCinr;
        return this;
    }

    public String getUniqueIdentificationCic() {
        return uniqueIdentificationCic;
    }

    public McdHrcPublicHistory setUniqueIdentificationCic(String uniqueIdentificationCic) {
        this.uniqueIdentificationCic = uniqueIdentificationCic;
        return this;
    }

    public String getFilePathCic() {
        return filePathCic;
    }

    public McdHrcPublicHistory setFilePathCic(String filePathCic) {
        this.filePathCic = filePathCic;
        return this;
    }

    public String getCreateUserCic() {
        return createUserCic;
    }

    public McdHrcPublicHistory setCreateUserCic(String createUserCic) {
        this.createUserCic = createUserCic;
        return this;
    }

    public String getCreateTimeCic() {
        return createTimeCic;
    }

    public McdHrcPublicHistory setCreateTimeCic(String createTimeCic) {
        this.createTimeCic = createTimeCic;
        return this;
    }

    public String getUniqueIdentificationCicr() {
        return uniqueIdentificationCicr;
    }

    public McdHrcPublicHistory setUniqueIdentificationCicr(String uniqueIdentificationCicr) {
        this.uniqueIdentificationCicr = uniqueIdentificationCicr;
        return this;
    }

    public String getFilePathCicr() {
        return filePathCicr;
    }

    public McdHrcPublicHistory setFilePathCicr(String filePathCicr) {
        this.filePathCicr = filePathCicr;
        return this;
    }

    public String getCreateUserCicr() {
        return createUserCicr;
    }

    public McdHrcPublicHistory setCreateUserCicr(String createUserCicr) {
        this.createUserCicr = createUserCicr;
        return this;
    }

    public String getCreateTimeCicr() {
        return createTimeCicr;
    }

    public McdHrcPublicHistory setCreateTimeCicr(String createTimeCicr) {
        this.createTimeCicr = createTimeCicr;
        return this;
    }

    public String getPublicItemSpecificationTw() {
        return publicItemSpecificationTw;
    }

    public McdHrcPublicHistory setPublicItemSpecificationTw(String publicItemSpecificationTw) {
        this.publicItemSpecificationTw = publicItemSpecificationTw;
        return this;
    }

    public Integer getCaseEntryNum() {
        return caseEntryNum;
    }

    public McdHrcPublicHistory setCaseEntryNum(Integer caseEntryNum) {
        this.caseEntryNum = caseEntryNum;
        return this;
    }

    public String getPublicItemSpecificationEn() {
        return publicItemSpecificationEn;
    }

    public McdHrcPublicHistory setPublicItemSpecificationEn(String publicItemSpecificationEn) {
        this.publicItemSpecificationEn = publicItemSpecificationEn;
        return this;
    }

    public String getIsVariable() {
        return isVariable;
    }

    public McdHrcPublicHistory setIsVariable(String isVariable) {
        this.isVariable = isVariable;
        return this;
    }

    public String getIsDanger() {
        return isDanger;
    }

    public McdHrcPublicHistory setIsDanger(String isDanger) {
        this.isDanger = isDanger;
        return this;
    }

    public String getRate() {
        return rate;
    }

    public McdHrcPublicHistory setRate(String rate) {
        this.rate = rate;
        return this;
    }

    public String getPrdDescriptionTw() {
        return prdDescriptionTw;
    }

    public McdHrcPublicHistory setPrdDescriptionTw(String prdDescriptionTw) {
        this.prdDescriptionTw = prdDescriptionTw;
        return this;
    }

    public String getPrdDescriptionEn() {
        return prdDescriptionEn;
    }

    public McdHrcPublicHistory setPrdDescriptionEn(String prdDescriptionEn) {
        this.prdDescriptionEn = prdDescriptionEn;
        return this;
    }

    public String getSupplierGln() {
        return supplierGln;
    }

    public McdHrcPublicHistory setSupplierGln(String supplierGln) {
        this.supplierGln = supplierGln;
        return this;
    }

    public String getCompanyName() {
        return companyName;
    }

    public McdHrcPublicHistory setCompanyName(String companyName) {
        this.companyName = companyName;
        return this;
    }

    @Override
    protected Serializable pkVal() {
        return this.historyId;
    }

    @Override
    public String toString() {
        return "McdHrcPublicHistory{" +
        "masterId=" + masterId +
        ", itemCode=" + itemCode +
        ", retailerDepartment=" + retailerDepartment +
        ", supplierCode=" + supplierCode +
        ", brand=" + brand +
        ", orgnCountry=" + orgnCountry +
        ", prdStatus=" + prdStatus +
        ", startAvailabilityDate=" + startAvailabilityDate +
        ", endAvailabilityDate=" + endAvailabilityDate +
        ", stopSupplierDescTw=" + stopSupplierDescTw +
        ", mainShipMethods=" + mainShipMethods +
        ", itemType=" + itemType +
        ", singleId=" + singleId +
        ", singleSubid=" + singleSubid +
        ", hierarchyLayernum=" + hierarchyLayernum +
        ", hierarchyLayer=" + hierarchyLayer +
        ", hierarchyEaNum=" + hierarchyEaNum +
        ", publicGtin=" + publicGtin +
        ", publicPrdGtinNameTw=" + publicPrdGtinNameTw +
        ", publicPrdGtinNameEn=" + publicPrdGtinNameEn +
        ", publicStatus=" + publicStatus +
        ", publicTime=" + publicTime +
        ", rejectReason=" + rejectReason +
        ", replaceGtin=" + replaceGtin +
        ", createUser=" + createUser +
        ", createTime=" + createTime +
        ", updateUser=" + updateUser +
        ", updateTime=" + updateTime +
        ", systemMemo=" + systemMemo +
        ", itemSpecificationTw=" + itemSpecificationTw +
        ", itemSpecificationEn=" + itemSpecificationEn +
        ", detailId=" + detailId +
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
        ", barcode=" + barcode +
        ", barcodeType=" + barcodeType +
        ", historyId=" + historyId +
        ", purchasePrice=" + purchasePrice +
        ", boardLayer=" + boardLayer +
        ", layerNum=" + layerNum +
        ", cate4=" + cate4 +
        ", uniqueIdentificationCin=" + uniqueIdentificationCin +
        ", filePathCin=" + filePathCin +
        ", createUserCin=" + createUserCin +
        ", createTimeCin=" + createTimeCin +
        ", uniqueIdentificationCinr=" + uniqueIdentificationCinr +
        ", identifierDocumentCinr=" + identifierDocumentCinr +
        ", identifierBusinessscopeCinr=" + identifierBusinessscopeCinr +
        ", filePathCinr=" + filePathCinr +
        ", createUserCinr=" + createUserCinr +
        ", createTimeCinr=" + createTimeCinr +
        ", uniqueIdentificationCic=" + uniqueIdentificationCic +
        ", filePathCic=" + filePathCic +
        ", createUserCic=" + createUserCic +
        ", createTimeCic=" + createTimeCic +
        ", uniqueIdentificationCicr=" + uniqueIdentificationCicr +
        ", filePathCicr=" + filePathCicr +
        ", createUserCicr=" + createUserCicr +
        ", createTimeCicr=" + createTimeCicr +
        ", publicItemSpecificationTw=" + publicItemSpecificationTw +
        ", caseEntryNum=" + caseEntryNum +
        ", publicItemSpecificationEn=" + publicItemSpecificationEn +
        ", isVariable=" + isVariable +
        ", isDanger=" + isDanger +
        ", rate=" + rate +
        ", prdDescriptionTw=" + prdDescriptionTw +
        ", prdDescriptionEn=" + prdDescriptionEn +
        ", supplierGln=" + supplierGln +
        ", companyName=" + companyName +
        "}";
    }
}
