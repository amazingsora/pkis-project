package com.tradevan.mapper.pkis.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

/**
 * <p>
 * 商品層級主檔
 * </p>
 *
 * @author penny
 * @since 2020-03-04
 */
@TableName("MCD_HRC_MASTER")
public class McdHrcMaster extends Model<McdHrcMaster> {

    private static final long serialVersionUID=1L;

    @TableId("MASTER_ID")
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

    /**
     * 細目名稱，包含尺吋／容量／顏色／口味
     */
    @TableField("ITEM_SPECIFICATION_TW")
    private String itemSpecificationTw;

    /**
     * 細目名稱，包含尺吋／容量／顏色／口味
     */
    @TableField("ITEM_SPECIFICATION_EN")
    private String itemSpecificationEn;

    /**
     * 層級檢查_庫存單位
     */
    @TableField("HIERARCHY_CHECK_BASE_UNIT")
    private String hierarchyCheckBaseUnit;

    /**
     * 商品中文名稱
     */
    @TableField("PRD_NAME_TW")
    private String prdNameTw;

    /**
     * 層級檢查_銷售單位
     */
    @TableField("HIERARCHY_CHECK_SALES_UNIT")
    private String hierarchyCheckSalesUnit;

    /**
     * 層級檢查_發票單位
     */
    @TableField("HIERARCHY_CHECK_INVOICE_UNIT")
    private String hierarchyCheckInvoiceUnit;

    /**
     * 層級檢查_訂貨單位
     */
    @TableField("HIERARCHY_CHECK_ORDER_UNIT")
    private String hierarchyCheckOrderUnit;

    @TableField("HIERARCHY_CHECK")
    private String hierarchyCheck;

    @TableField("CATE_4")
    private String cate4;

    /**
     * 箱入數(訂貨倍數值)
     */
    @TableField("CASE_ENTRY_NUM")
    private Integer caseEntryNum;

    /**
     * 發佈細目名稱
     */
    @TableField("PUBLIC_ITEM_SPECIFICATION_TW")
    private String publicItemSpecificationTw;

    /**
     * 發佈細目名稱
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

    public McdHrcMaster setMasterId(String masterId) {
        this.masterId = masterId;
        return this;
    }

    public String getItemCode() {
        return itemCode;
    }

    public McdHrcMaster setItemCode(String itemCode) {
        this.itemCode = itemCode;
        return this;
    }

    public String getRetailerDepartment() {
        return retailerDepartment;
    }

    public McdHrcMaster setRetailerDepartment(String retailerDepartment) {
        this.retailerDepartment = retailerDepartment;
        return this;
    }

    public String getSupplierCode() {
        return supplierCode;
    }

    public McdHrcMaster setSupplierCode(String supplierCode) {
        this.supplierCode = supplierCode;
        return this;
    }

    public String getBrand() {
        return brand;
    }

    public McdHrcMaster setBrand(String brand) {
        this.brand = brand;
        return this;
    }

    public String getOrgnCountry() {
        return orgnCountry;
    }

    public McdHrcMaster setOrgnCountry(String orgnCountry) {
        this.orgnCountry = orgnCountry;
        return this;
    }

    public String getPrdStatus() {
        return prdStatus;
    }

    public McdHrcMaster setPrdStatus(String prdStatus) {
        this.prdStatus = prdStatus;
        return this;
    }

    public String getStartAvailabilityDate() {
        return startAvailabilityDate;
    }

    public McdHrcMaster setStartAvailabilityDate(String startAvailabilityDate) {
        this.startAvailabilityDate = startAvailabilityDate;
        return this;
    }

    public String getEndAvailabilityDate() {
        return endAvailabilityDate;
    }

    public McdHrcMaster setEndAvailabilityDate(String endAvailabilityDate) {
        this.endAvailabilityDate = endAvailabilityDate;
        return this;
    }

    public String getStopSupplierDescTw() {
        return stopSupplierDescTw;
    }

    public McdHrcMaster setStopSupplierDescTw(String stopSupplierDescTw) {
        this.stopSupplierDescTw = stopSupplierDescTw;
        return this;
    }

    public String getMainShipMethods() {
        return mainShipMethods;
    }

    public McdHrcMaster setMainShipMethods(String mainShipMethods) {
        this.mainShipMethods = mainShipMethods;
        return this;
    }

    public String getItemType() {
        return itemType;
    }

    public McdHrcMaster setItemType(String itemType) {
        this.itemType = itemType;
        return this;
    }

    public String getSingleId() {
        return singleId;
    }

    public McdHrcMaster setSingleId(String singleId) {
        this.singleId = singleId;
        return this;
    }

    public String getSingleSubid() {
        return singleSubid;
    }

    public McdHrcMaster setSingleSubid(String singleSubid) {
        this.singleSubid = singleSubid;
        return this;
    }

    public String getHierarchyLayernum() {
        return hierarchyLayernum;
    }

    public McdHrcMaster setHierarchyLayernum(String hierarchyLayernum) {
        this.hierarchyLayernum = hierarchyLayernum;
        return this;
    }

    public String getHierarchyLayer() {
        return hierarchyLayer;
    }

    public McdHrcMaster setHierarchyLayer(String hierarchyLayer) {
        this.hierarchyLayer = hierarchyLayer;
        return this;
    }

    public String getHierarchyEaNum() {
        return hierarchyEaNum;
    }

    public McdHrcMaster setHierarchyEaNum(String hierarchyEaNum) {
        this.hierarchyEaNum = hierarchyEaNum;
        return this;
    }

    public String getPublicGtin() {
        return publicGtin;
    }

    public McdHrcMaster setPublicGtin(String publicGtin) {
        this.publicGtin = publicGtin;
        return this;
    }

    public String getPublicPrdGtinNameTw() {
        return publicPrdGtinNameTw;
    }

    public McdHrcMaster setPublicPrdGtinNameTw(String publicPrdGtinNameTw) {
        this.publicPrdGtinNameTw = publicPrdGtinNameTw;
        return this;
    }

    public String getPublicPrdGtinNameEn() {
        return publicPrdGtinNameEn;
    }

    public McdHrcMaster setPublicPrdGtinNameEn(String publicPrdGtinNameEn) {
        this.publicPrdGtinNameEn = publicPrdGtinNameEn;
        return this;
    }

    public String getPublicStatus() {
        return publicStatus;
    }

    public McdHrcMaster setPublicStatus(String publicStatus) {
        this.publicStatus = publicStatus;
        return this;
    }

    public String getPublicTime() {
        return publicTime;
    }

    public McdHrcMaster setPublicTime(String publicTime) {
        this.publicTime = publicTime;
        return this;
    }

    public String getRejectReason() {
        return rejectReason;
    }

    public McdHrcMaster setRejectReason(String rejectReason) {
        this.rejectReason = rejectReason;
        return this;
    }

    public String getReplaceGtin() {
        return replaceGtin;
    }

    public McdHrcMaster setReplaceGtin(String replaceGtin) {
        this.replaceGtin = replaceGtin;
        return this;
    }

    public String getBarcode() {
        return barcode;
    }

    public McdHrcMaster setBarcode(String barcode) {
        this.barcode = barcode;
        return this;
    }

    public String getCreateUser() {
        return createUser;
    }

    public McdHrcMaster setCreateUser(String createUser) {
        this.createUser = createUser;
        return this;
    }

    public String getCreateTime() {
        return createTime;
    }

    public McdHrcMaster setCreateTime(String createTime) {
        this.createTime = createTime;
        return this;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public McdHrcMaster setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
        return this;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public McdHrcMaster setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
        return this;
    }

    public String getSystemMemo() {
        return systemMemo;
    }

    public McdHrcMaster setSystemMemo(String systemMemo) {
        this.systemMemo = systemMemo;
        return this;
    }

    public String getItemSpecificationTw() {
        return itemSpecificationTw;
    }

    public McdHrcMaster setItemSpecificationTw(String itemSpecificationTw) {
        this.itemSpecificationTw = itemSpecificationTw;
        return this;
    }

    public String getItemSpecificationEn() {
        return itemSpecificationEn;
    }

    public McdHrcMaster setItemSpecificationEn(String itemSpecificationEn) {
        this.itemSpecificationEn = itemSpecificationEn;
        return this;
    }

    public String getHierarchyCheckBaseUnit() {
        return hierarchyCheckBaseUnit;
    }

    public McdHrcMaster setHierarchyCheckBaseUnit(String hierarchyCheckBaseUnit) {
        this.hierarchyCheckBaseUnit = hierarchyCheckBaseUnit;
        return this;
    }

    public String getPrdNameTw() {
        return prdNameTw;
    }

    public McdHrcMaster setPrdNameTw(String prdNameTw) {
        this.prdNameTw = prdNameTw;
        return this;
    }

    public String getHierarchyCheckSalesUnit() {
        return hierarchyCheckSalesUnit;
    }

    public McdHrcMaster setHierarchyCheckSalesUnit(String hierarchyCheckSalesUnit) {
        this.hierarchyCheckSalesUnit = hierarchyCheckSalesUnit;
        return this;
    }

    public String getHierarchyCheckInvoiceUnit() {
        return hierarchyCheckInvoiceUnit;
    }

    public McdHrcMaster setHierarchyCheckInvoiceUnit(String hierarchyCheckInvoiceUnit) {
        this.hierarchyCheckInvoiceUnit = hierarchyCheckInvoiceUnit;
        return this;
    }

    public String getHierarchyCheckOrderUnit() {
        return hierarchyCheckOrderUnit;
    }

    public McdHrcMaster setHierarchyCheckOrderUnit(String hierarchyCheckOrderUnit) {
        this.hierarchyCheckOrderUnit = hierarchyCheckOrderUnit;
        return this;
    }

    public String getHierarchyCheck() {
        return hierarchyCheck;
    }

    public McdHrcMaster setHierarchyCheck(String hierarchyCheck) {
        this.hierarchyCheck = hierarchyCheck;
        return this;
    }

    public String getCate4() {
        return cate4;
    }

    public McdHrcMaster setCate4(String cate4) {
        this.cate4 = cate4;
        return this;
    }

    public Integer getCaseEntryNum() {
        return caseEntryNum;
    }

    public McdHrcMaster setCaseEntryNum(Integer caseEntryNum) {
        this.caseEntryNum = caseEntryNum;
        return this;
    }

    public String getPublicItemSpecificationTw() {
        return publicItemSpecificationTw;
    }

    public McdHrcMaster setPublicItemSpecificationTw(String publicItemSpecificationTw) {
        this.publicItemSpecificationTw = publicItemSpecificationTw;
        return this;
    }

    public String getPublicItemSpecificationEn() {
        return publicItemSpecificationEn;
    }

    public McdHrcMaster setPublicItemSpecificationEn(String publicItemSpecificationEn) {
        this.publicItemSpecificationEn = publicItemSpecificationEn;
        return this;
    }

    public String getIsVariable() {
        return isVariable;
    }

    public McdHrcMaster setIsVariable(String isVariable) {
        this.isVariable = isVariable;
        return this;
    }

    public String getIsDanger() {
        return isDanger;
    }

    public McdHrcMaster setIsDanger(String isDanger) {
        this.isDanger = isDanger;
        return this;
    }

    public String getRate() {
        return rate;
    }

    public McdHrcMaster setRate(String rate) {
        this.rate = rate;
        return this;
    }

    public String getPrdDescriptionTw() {
        return prdDescriptionTw;
    }

    public McdHrcMaster setPrdDescriptionTw(String prdDescriptionTw) {
        this.prdDescriptionTw = prdDescriptionTw;
        return this;
    }

    public String getPrdDescriptionEn() {
        return prdDescriptionEn;
    }

    public McdHrcMaster setPrdDescriptionEn(String prdDescriptionEn) {
        this.prdDescriptionEn = prdDescriptionEn;
        return this;
    }

    public String getSupplierGln() {
        return supplierGln;
    }

    public McdHrcMaster setSupplierGln(String supplierGln) {
        this.supplierGln = supplierGln;
        return this;
    }

    public String getCompanyName() {
        return companyName;
    }

    public McdHrcMaster setCompanyName(String companyName) {
        this.companyName = companyName;
        return this;
    }

    @Override
    protected Serializable pkVal() {
        return this.masterId;
    }

    @Override
    public String toString() {
        return "McdHrcMaster{" +
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
        ", barcode=" + barcode +
        ", createUser=" + createUser +
        ", createTime=" + createTime +
        ", updateUser=" + updateUser +
        ", updateTime=" + updateTime +
        ", systemMemo=" + systemMemo +
        ", itemSpecificationTw=" + itemSpecificationTw +
        ", itemSpecificationEn=" + itemSpecificationEn +
        ", hierarchyCheckBaseUnit=" + hierarchyCheckBaseUnit +
        ", prdNameTw=" + prdNameTw +
        ", hierarchyCheckSalesUnit=" + hierarchyCheckSalesUnit +
        ", hierarchyCheckInvoiceUnit=" + hierarchyCheckInvoiceUnit +
        ", hierarchyCheckOrderUnit=" + hierarchyCheckOrderUnit +
        ", hierarchyCheck=" + hierarchyCheck +
        ", cate4=" + cate4 +
        ", caseEntryNum=" + caseEntryNum +
        ", publicItemSpecificationTw=" + publicItemSpecificationTw +
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
