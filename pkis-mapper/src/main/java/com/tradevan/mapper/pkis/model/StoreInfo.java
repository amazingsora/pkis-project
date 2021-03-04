package com.tradevan.mapper.pkis.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author penny
 * @since 2020-03-04
 */
@TableName("STORE_INFO")
public class StoreInfo extends Model<StoreInfo> {

    private static final long serialVersionUID=1L;

    /**
     * 門市地區代碼
     */
    @TableField("AREA_CODE")
    private String areaCode;

    /**
     * 門市地區名稱
     */
    @TableField("AREA_NMAE")
    private String areaNmae;

    /**
     * 門市類型
     */
    @TableField("STORE_TYPE")
    private String storeType;

    /**
     * 是否為收檔依據
     */
    @TableField("NOTE")
    private String note;

    /**
     * 門市排序
     */
    @TableField("SORT_INDEX")
    private Integer sortIndex;

    /**
     * 門市店碼
     */
    @TableField("STORE_CODE")
    private String storeCode;

    /**
     * 門市全球供應商識別碼
     */
    @TableField("STORE_GLN")
    private String storeGln;

    /**
     * 門市名稱
     */
    @TableField("STORE_NAME")
    private String storeName;

    /**
     * 門市簡稱
     */
    @TableField("STORE_BRIEF_NAME")
    private String storeBriefName;

    /**
     * 門市英文名稱
     */
    @TableField("STORE_NAME_EN")
    private String storeNameEn;

    /**
     * 門市英文縮寫
     */
    @TableField("STORE_BRIEF_NAME_EN")
    private String storeBriefNameEn;

    /**
     * 門市電話號碼
     */
    @TableField("STORE_TEL")
    private String storeTel;

    /**
     * 門市傳真號碼
     */
    @TableField("STORE_FAX")
    private String storeFax;

    /**
     * 門市收貨課傳真號碼
     */
    @TableField("RECEIVING_FAX")
    private String receivingFax;

    /**
     * 門市郵遞區號
     */
    @TableField("ZIPCODE")
    private String zipcode;

    /**
     * 門市一級行政區
     */
    @TableField("MAIN_REGION")
    private String mainRegion;

    /**
     * 門市地址
     */
    @TableField("STORE_ADDRESS")
    private String storeAddress;

    /**
     * 門市開幕日期
     */
    @TableField("STORE_INAUGURATE")
    private Date storeInaugurate;

    /**
     * 門市閉幕日期
     */
    @TableField("STORE_CONCLUDE")
    private Date storeConclude;

    /**
     * 門市開閉店狀態
     */
    @TableField("STORE_STATE")
    private String storeState;

    /**
     * PS_CODE(會計專用)
     */
    @TableField("PS_CODE")
    private String psCode;

    /**
     * 直退店碼(會計專用)
     */
    @TableField("DIRECT_RETURN_CODE")
    private String directReturnCode;

    /**
     * 收貨單位負責人姓名
     */
    @TableField("RECEIPT_MERCHANDISE_NAME")
    private String receiptMerchandiseName;

    /**
     * 收貨單位負責人英文姓名
     */
    @TableField("RECEIPT_MERCHANDISE_NAME_EN")
    private String receiptMerchandiseNameEn;

    /**
     * 收貨單位負責人分機號碼
     */
    @TableField("RECEIPT_MERCHANDISE_EXT")
    private String receiptMerchandiseExt;

    /**
     * 收貨單位負責人行動電話
     */
    @TableField("RECEIPT_MERCHANDISE_CELLPHONE")
    private String receiptMerchandiseCellphone;


    public String getAreaCode() {
        return areaCode;
    }

    public StoreInfo setAreaCode(String areaCode) {
        this.areaCode = areaCode;
        return this;
    }

    public String getAreaNmae() {
        return areaNmae;
    }

    public StoreInfo setAreaNmae(String areaNmae) {
        this.areaNmae = areaNmae;
        return this;
    }

    public String getStoreType() {
        return storeType;
    }

    public StoreInfo setStoreType(String storeType) {
        this.storeType = storeType;
        return this;
    }

    public String getNote() {
        return note;
    }

    public StoreInfo setNote(String note) {
        this.note = note;
        return this;
    }

    public Integer getSortIndex() {
        return sortIndex;
    }

    public StoreInfo setSortIndex(Integer sortIndex) {
        this.sortIndex = sortIndex;
        return this;
    }

    public String getStoreCode() {
        return storeCode;
    }

    public StoreInfo setStoreCode(String storeCode) {
        this.storeCode = storeCode;
        return this;
    }

    public String getStoreGln() {
        return storeGln;
    }

    public StoreInfo setStoreGln(String storeGln) {
        this.storeGln = storeGln;
        return this;
    }

    public String getStoreName() {
        return storeName;
    }

    public StoreInfo setStoreName(String storeName) {
        this.storeName = storeName;
        return this;
    }

    public String getStoreBriefName() {
        return storeBriefName;
    }

    public StoreInfo setStoreBriefName(String storeBriefName) {
        this.storeBriefName = storeBriefName;
        return this;
    }

    public String getStoreNameEn() {
        return storeNameEn;
    }

    public StoreInfo setStoreNameEn(String storeNameEn) {
        this.storeNameEn = storeNameEn;
        return this;
    }

    public String getStoreBriefNameEn() {
        return storeBriefNameEn;
    }

    public StoreInfo setStoreBriefNameEn(String storeBriefNameEn) {
        this.storeBriefNameEn = storeBriefNameEn;
        return this;
    }

    public String getStoreTel() {
        return storeTel;
    }

    public StoreInfo setStoreTel(String storeTel) {
        this.storeTel = storeTel;
        return this;
    }

    public String getStoreFax() {
        return storeFax;
    }

    public StoreInfo setStoreFax(String storeFax) {
        this.storeFax = storeFax;
        return this;
    }

    public String getReceivingFax() {
        return receivingFax;
    }

    public StoreInfo setReceivingFax(String receivingFax) {
        this.receivingFax = receivingFax;
        return this;
    }

    public String getZipcode() {
        return zipcode;
    }

    public StoreInfo setZipcode(String zipcode) {
        this.zipcode = zipcode;
        return this;
    }

    public String getMainRegion() {
        return mainRegion;
    }

    public StoreInfo setMainRegion(String mainRegion) {
        this.mainRegion = mainRegion;
        return this;
    }

    public String getStoreAddress() {
        return storeAddress;
    }

    public StoreInfo setStoreAddress(String storeAddress) {
        this.storeAddress = storeAddress;
        return this;
    }

    public Date getStoreInaugurate() {
        return storeInaugurate;
    }

    public StoreInfo setStoreInaugurate(Date storeInaugurate) {
        this.storeInaugurate = storeInaugurate;
        return this;
    }

    public Date getStoreConclude() {
        return storeConclude;
    }

    public StoreInfo setStoreConclude(Date storeConclude) {
        this.storeConclude = storeConclude;
        return this;
    }

    public String getStoreState() {
        return storeState;
    }

    public StoreInfo setStoreState(String storeState) {
        this.storeState = storeState;
        return this;
    }

    public String getPsCode() {
        return psCode;
    }

    public StoreInfo setPsCode(String psCode) {
        this.psCode = psCode;
        return this;
    }

    public String getDirectReturnCode() {
        return directReturnCode;
    }

    public StoreInfo setDirectReturnCode(String directReturnCode) {
        this.directReturnCode = directReturnCode;
        return this;
    }

    public String getReceiptMerchandiseName() {
        return receiptMerchandiseName;
    }

    public StoreInfo setReceiptMerchandiseName(String receiptMerchandiseName) {
        this.receiptMerchandiseName = receiptMerchandiseName;
        return this;
    }

    public String getReceiptMerchandiseNameEn() {
        return receiptMerchandiseNameEn;
    }

    public StoreInfo setReceiptMerchandiseNameEn(String receiptMerchandiseNameEn) {
        this.receiptMerchandiseNameEn = receiptMerchandiseNameEn;
        return this;
    }

    public String getReceiptMerchandiseExt() {
        return receiptMerchandiseExt;
    }

    public StoreInfo setReceiptMerchandiseExt(String receiptMerchandiseExt) {
        this.receiptMerchandiseExt = receiptMerchandiseExt;
        return this;
    }

    public String getReceiptMerchandiseCellphone() {
        return receiptMerchandiseCellphone;
    }

    public StoreInfo setReceiptMerchandiseCellphone(String receiptMerchandiseCellphone) {
        this.receiptMerchandiseCellphone = receiptMerchandiseCellphone;
        return this;
    }

    @Override
    protected Serializable pkVal() {
        return null;
    }

    @Override
    public String toString() {
        return "StoreInfo{" +
        "areaCode=" + areaCode +
        ", areaNmae=" + areaNmae +
        ", storeType=" + storeType +
        ", note=" + note +
        ", sortIndex=" + sortIndex +
        ", storeCode=" + storeCode +
        ", storeGln=" + storeGln +
        ", storeName=" + storeName +
        ", storeBriefName=" + storeBriefName +
        ", storeNameEn=" + storeNameEn +
        ", storeBriefNameEn=" + storeBriefNameEn +
        ", storeTel=" + storeTel +
        ", storeFax=" + storeFax +
        ", receivingFax=" + receivingFax +
        ", zipcode=" + zipcode +
        ", mainRegion=" + mainRegion +
        ", storeAddress=" + storeAddress +
        ", storeInaugurate=" + storeInaugurate +
        ", storeConclude=" + storeConclude +
        ", storeState=" + storeState +
        ", psCode=" + psCode +
        ", directReturnCode=" + directReturnCode +
        ", receiptMerchandiseName=" + receiptMerchandiseName +
        ", receiptMerchandiseNameEn=" + receiptMerchandiseNameEn +
        ", receiptMerchandiseExt=" + receiptMerchandiseExt +
        ", receiptMerchandiseCellphone=" + receiptMerchandiseCellphone +
        "}";
    }
}
