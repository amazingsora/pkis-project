package com.tradevan.mapper.pkis.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

/**
 * <p>
 * 商品基本資料
 * </p>
 *
 * @author penny
 * @since 2020-03-04
 */
@TableName("MCD_PRD_ITEM")
public class McdPrdItem extends Model<McdPrdItem> {

    private static final long serialVersionUID=1L;

    /**
     * 品項代碼：統編(8碼)+品項類型(A:單一商品,M:複合商品,F:贈品)+商品數(5碼)=14碼
     */
    @TableId("ITEM_CODE")
    private String itemCode;

    /**
     * 商品型態：A：單品、M：複合綜合、F：贈品。
     */
    @TableField("ITEM_TYPE")
    private String itemType;

    /**
     * 家樂福部門
     */
    @TableField("RETAILER_DEPARTMENT")
    private String retailerDepartment;

    /**
     * 關貿商品大分類
     */
    @TableField("CATE_1")
    private String cate1;

    /**
     * 關貿商品中分類
     */
    @TableField("CATE_2")
    private String cate2;

    /**
     * 關貿商品小分類
     */
    @TableField("CATE_3")
    private String cate3;

    /**
     * 關貿商品細分類
     */
    @TableField("CATE_4")
    private String cate4;

    /**
     * 供應商統編
     */
    @TableField("COMP_BAN")
    private String compBan;

    /**
     * 供應商廠編
     */
    @TableField("SUPPLIER_CODE")
    private String supplierCode;

    /**
     * 製造商統編
     */
    @TableField("MFRS_BAN")
    private String mfrsBan;

    /**
     * 商品品名(中文)
     */
    @TableField("PRD_NAME_TW")
    private String prdNameTw;

    /**
     * 商品品名(英文)
     */
    @TableField("PRD_NAME_EN")
    private String prdNameEn;

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

    /**
     * 品牌
     */
    @TableField("BRAND")
    private String brand;

    /**
     * 原產地：REF TO PROF_SYSTEM
     */
    @TableField("ORGN_COUNTRY")
    private String orgnCountry;

    /**
     * 上市日期
     */
    @TableField("DATE_AVAILABLE")
    private String dateAvailable;

    /**
     * 商品狀態：A-上架收費，E-編輯提報，F-全部下架，P-部分下架
     */
    @TableField("PRD_STATUS")
    private String prdStatus;

    /**
     * 春：S、夏：U、秋：Ａ、冬：W，值用倒V符號做區隔。
例如：S^U^A^W、S^^A^，沒有值倒V符號還要留著。
     */
    @TableField("FOR_SEASON")
    private String forSeason;

    /**
     * 是否可以供通路下載商品資料
     */
    @TableField("SHARE_DATA")
    private String shareData;

    /**
     * 是否可配合通路調整商品規格
     */
    @TableField("CHANGE_SPEC")
    private String changeSpec;

    /**
     * 資料來源方式(單筆或批次)
     */
    @TableField("DATA_SOURCE")
    private String dataSource;

    /**
     * 是否為可度量商品
     */
    @TableField("IS_VARIABLE")
    private String isVariable;

    /**
     * 是否為危險物品
     */
    @TableField("IS_DANGER")
    private String isDanger;

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
     * 停止供貨說明(中文)
     */
    @TableField("STOP_SUPPLIER_DESC_TW")
    private String stopSupplierDescTw;

    /**
     * 停止供貨說明(英文)
     */
    @TableField("STOP_SUPPLIER_DESC_EN")
    private String stopSupplierDescEn;

    /**
     * 稅率
     */
    @TableField("RATE")
    private String rate;

    /**
     * 主要運送方式
     */
    @TableField("MAIN_SHIP_METHODS")
    private String mainShipMethods;

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
     * 被複製的ID
     */
    @TableField("COPY_ITEM_CODE")
    private String copyItemCode;

    /**
     * 供應商GLN
     */
    @TableField("SUPPLIER_GLN")
    private String supplierGln;

    /**
     * 供應商公司名稱
     */
    @TableField("COMPANY_NAME")
    private String companyName;


    public String getItemCode() {
        return itemCode;
    }

    public McdPrdItem setItemCode(String itemCode) {
        this.itemCode = itemCode;
        return this;
    }

    public String getItemType() {
        return itemType;
    }

    public McdPrdItem setItemType(String itemType) {
        this.itemType = itemType;
        return this;
    }

    public String getRetailerDepartment() {
        return retailerDepartment;
    }

    public McdPrdItem setRetailerDepartment(String retailerDepartment) {
        this.retailerDepartment = retailerDepartment;
        return this;
    }

    public String getCate1() {
        return cate1;
    }

    public McdPrdItem setCate1(String cate1) {
        this.cate1 = cate1;
        return this;
    }

    public String getCate2() {
        return cate2;
    }

    public McdPrdItem setCate2(String cate2) {
        this.cate2 = cate2;
        return this;
    }

    public String getCate3() {
        return cate3;
    }

    public McdPrdItem setCate3(String cate3) {
        this.cate3 = cate3;
        return this;
    }

    public String getCate4() {
        return cate4;
    }

    public McdPrdItem setCate4(String cate4) {
        this.cate4 = cate4;
        return this;
    }

    public String getCompBan() {
        return compBan;
    }

    public McdPrdItem setCompBan(String compBan) {
        this.compBan = compBan;
        return this;
    }

    public String getSupplierCode() {
        return supplierCode;
    }

    public McdPrdItem setSupplierCode(String supplierCode) {
        this.supplierCode = supplierCode;
        return this;
    }

    public String getMfrsBan() {
        return mfrsBan;
    }

    public McdPrdItem setMfrsBan(String mfrsBan) {
        this.mfrsBan = mfrsBan;
        return this;
    }

    public String getPrdNameTw() {
        return prdNameTw;
    }

    public McdPrdItem setPrdNameTw(String prdNameTw) {
        this.prdNameTw = prdNameTw;
        return this;
    }

    public String getPrdNameEn() {
        return prdNameEn;
    }

    public McdPrdItem setPrdNameEn(String prdNameEn) {
        this.prdNameEn = prdNameEn;
        return this;
    }

    public String getPrdDescriptionTw() {
        return prdDescriptionTw;
    }

    public McdPrdItem setPrdDescriptionTw(String prdDescriptionTw) {
        this.prdDescriptionTw = prdDescriptionTw;
        return this;
    }

    public String getPrdDescriptionEn() {
        return prdDescriptionEn;
    }

    public McdPrdItem setPrdDescriptionEn(String prdDescriptionEn) {
        this.prdDescriptionEn = prdDescriptionEn;
        return this;
    }

    public String getBrand() {
        return brand;
    }

    public McdPrdItem setBrand(String brand) {
        this.brand = brand;
        return this;
    }

    public String getOrgnCountry() {
        return orgnCountry;
    }

    public McdPrdItem setOrgnCountry(String orgnCountry) {
        this.orgnCountry = orgnCountry;
        return this;
    }

    public String getDateAvailable() {
        return dateAvailable;
    }

    public McdPrdItem setDateAvailable(String dateAvailable) {
        this.dateAvailable = dateAvailable;
        return this;
    }

    public String getPrdStatus() {
        return prdStatus;
    }

    public McdPrdItem setPrdStatus(String prdStatus) {
        this.prdStatus = prdStatus;
        return this;
    }

    public String getForSeason() {
        return forSeason;
    }

    public McdPrdItem setForSeason(String forSeason) {
        this.forSeason = forSeason;
        return this;
    }

    public String getShareData() {
        return shareData;
    }

    public McdPrdItem setShareData(String shareData) {
        this.shareData = shareData;
        return this;
    }

    public String getChangeSpec() {
        return changeSpec;
    }

    public McdPrdItem setChangeSpec(String changeSpec) {
        this.changeSpec = changeSpec;
        return this;
    }

    public String getDataSource() {
        return dataSource;
    }

    public McdPrdItem setDataSource(String dataSource) {
        this.dataSource = dataSource;
        return this;
    }

    public String getIsVariable() {
        return isVariable;
    }

    public McdPrdItem setIsVariable(String isVariable) {
        this.isVariable = isVariable;
        return this;
    }

    public String getIsDanger() {
        return isDanger;
    }

    public McdPrdItem setIsDanger(String isDanger) {
        this.isDanger = isDanger;
        return this;
    }

    public String getStartAvailabilityDate() {
        return startAvailabilityDate;
    }

    public McdPrdItem setStartAvailabilityDate(String startAvailabilityDate) {
        this.startAvailabilityDate = startAvailabilityDate;
        return this;
    }

    public String getEndAvailabilityDate() {
        return endAvailabilityDate;
    }

    public McdPrdItem setEndAvailabilityDate(String endAvailabilityDate) {
        this.endAvailabilityDate = endAvailabilityDate;
        return this;
    }

    public String getStopSupplierDescTw() {
        return stopSupplierDescTw;
    }

    public McdPrdItem setStopSupplierDescTw(String stopSupplierDescTw) {
        this.stopSupplierDescTw = stopSupplierDescTw;
        return this;
    }

    public String getStopSupplierDescEn() {
        return stopSupplierDescEn;
    }

    public McdPrdItem setStopSupplierDescEn(String stopSupplierDescEn) {
        this.stopSupplierDescEn = stopSupplierDescEn;
        return this;
    }

    public String getRate() {
        return rate;
    }

    public McdPrdItem setRate(String rate) {
        this.rate = rate;
        return this;
    }

    public String getMainShipMethods() {
        return mainShipMethods;
    }

    public McdPrdItem setMainShipMethods(String mainShipMethods) {
        this.mainShipMethods = mainShipMethods;
        return this;
    }

    public String getCreateUser() {
        return createUser;
    }

    public McdPrdItem setCreateUser(String createUser) {
        this.createUser = createUser;
        return this;
    }

    public String getCreateTime() {
        return createTime;
    }

    public McdPrdItem setCreateTime(String createTime) {
        this.createTime = createTime;
        return this;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public McdPrdItem setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
        return this;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public McdPrdItem setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
        return this;
    }

    public String getSystemMemo() {
        return systemMemo;
    }

    public McdPrdItem setSystemMemo(String systemMemo) {
        this.systemMemo = systemMemo;
        return this;
    }

    public String getCopyItemCode() {
        return copyItemCode;
    }

    public McdPrdItem setCopyItemCode(String copyItemCode) {
        this.copyItemCode = copyItemCode;
        return this;
    }

    public String getSupplierGln() {
        return supplierGln;
    }

    public McdPrdItem setSupplierGln(String supplierGln) {
        this.supplierGln = supplierGln;
        return this;
    }

    public String getCompanyName() {
        return companyName;
    }

    public McdPrdItem setCompanyName(String companyName) {
        this.companyName = companyName;
        return this;
    }

    @Override
    protected Serializable pkVal() {
        return this.itemCode;
    }

    @Override
    public String toString() {
        return "McdPrdItem{" +
        "itemCode=" + itemCode +
        ", itemType=" + itemType +
        ", retailerDepartment=" + retailerDepartment +
        ", cate1=" + cate1 +
        ", cate2=" + cate2 +
        ", cate3=" + cate3 +
        ", cate4=" + cate4 +
        ", compBan=" + compBan +
        ", supplierCode=" + supplierCode +
        ", mfrsBan=" + mfrsBan +
        ", prdNameTw=" + prdNameTw +
        ", prdNameEn=" + prdNameEn +
        ", prdDescriptionTw=" + prdDescriptionTw +
        ", prdDescriptionEn=" + prdDescriptionEn +
        ", brand=" + brand +
        ", orgnCountry=" + orgnCountry +
        ", dateAvailable=" + dateAvailable +
        ", prdStatus=" + prdStatus +
        ", forSeason=" + forSeason +
        ", shareData=" + shareData +
        ", changeSpec=" + changeSpec +
        ", dataSource=" + dataSource +
        ", isVariable=" + isVariable +
        ", isDanger=" + isDanger +
        ", startAvailabilityDate=" + startAvailabilityDate +
        ", endAvailabilityDate=" + endAvailabilityDate +
        ", stopSupplierDescTw=" + stopSupplierDescTw +
        ", stopSupplierDescEn=" + stopSupplierDescEn +
        ", rate=" + rate +
        ", mainShipMethods=" + mainShipMethods +
        ", createUser=" + createUser +
        ", createTime=" + createTime +
        ", updateUser=" + updateUser +
        ", updateTime=" + updateTime +
        ", systemMemo=" + systemMemo +
        ", copyItemCode=" + copyItemCode +
        ", supplierGln=" + supplierGln +
        ", companyName=" + companyName +
        "}";
    }
}
