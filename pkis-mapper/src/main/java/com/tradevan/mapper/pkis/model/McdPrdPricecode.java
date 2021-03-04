package com.tradevan.mapper.pkis.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

/**
 * <p>
 * 條碼售價關聯檔
 * </p>
 *
 * @author penny
 * @since 2020-03-04
 */
@TableName("MCD_PRD_PRICECODE")
public class McdPrdPricecode extends Model<McdPrdPricecode> {

    private static final long serialVersionUID=1L;

    /**
     * 流水號
     */
    @TableId("PRICECODE_ID")
    private String pricecodeId;

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
     * 來源ID
     */
    @TableField("SOURCE_ID")
    private String sourceId;

    /**
     * 來源包裝方式：EA-單品，PK-組合包，CA-箱，PL-棧板
     */
    @TableField("SOURCE_TYPE")
    private String sourceType;

    /**
     * 商品條碼
     */
    @TableField("BARCODE")
    private String barcode;

    /**
     * 幣別
     */
    @TableField("CURRENCY")
    private String currency;

    /**
     * 建議售價
     */
    @TableField("ADVICE_PRICE")
    private Integer advicePrice;

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
     * 建立人員
     */
    @TableField("CREATE_USER")
    private String createUser;

    /**
     * 備註
     */
    @TableField("SYSTEM_MEMO")
    private String systemMemo;

    /**
     * 貨號
     */
    @TableField("COMP_PRD_NO")
    private String compPrdNo;

    /**
     * 來源父ID
     */
    @TableField("SOURCE_HEAD_ID")
    private String sourceHeadId;

    /**
     * 語系
     */
    @TableField("LANG_ID")
    private String langId;

    /**
     * 條碼類型
     */
    @TableField("BARCODE_TYPE")
    private String barcodeType;

    /**
     * 正常進價
     */
    @TableField("PURCHASE_PRICE")
    private Integer purchasePrice;


    public String getPricecodeId() {
        return pricecodeId;
    }

    public McdPrdPricecode setPricecodeId(String pricecodeId) {
        this.pricecodeId = pricecodeId;
        return this;
    }

    public String getItemCode() {
        return itemCode;
    }

    public McdPrdPricecode setItemCode(String itemCode) {
        this.itemCode = itemCode;
        return this;
    }

    public String getItemType() {
        return itemType;
    }

    public McdPrdPricecode setItemType(String itemType) {
        this.itemType = itemType;
        return this;
    }

    public String getSourceId() {
        return sourceId;
    }

    public McdPrdPricecode setSourceId(String sourceId) {
        this.sourceId = sourceId;
        return this;
    }

    public String getSourceType() {
        return sourceType;
    }

    public McdPrdPricecode setSourceType(String sourceType) {
        this.sourceType = sourceType;
        return this;
    }

    public String getBarcode() {
        return barcode;
    }

    public McdPrdPricecode setBarcode(String barcode) {
        this.barcode = barcode;
        return this;
    }

    public String getCurrency() {
        return currency;
    }

    public McdPrdPricecode setCurrency(String currency) {
        this.currency = currency;
        return this;
    }

    public Integer getAdvicePrice() {
        return advicePrice;
    }

    public McdPrdPricecode setAdvicePrice(Integer advicePrice) {
        this.advicePrice = advicePrice;
        return this;
    }

    public String getCreateTime() {
        return createTime;
    }

    public McdPrdPricecode setCreateTime(String createTime) {
        this.createTime = createTime;
        return this;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public McdPrdPricecode setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
        return this;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public McdPrdPricecode setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
        return this;
    }

    public String getCreateUser() {
        return createUser;
    }

    public McdPrdPricecode setCreateUser(String createUser) {
        this.createUser = createUser;
        return this;
    }

    public String getSystemMemo() {
        return systemMemo;
    }

    public McdPrdPricecode setSystemMemo(String systemMemo) {
        this.systemMemo = systemMemo;
        return this;
    }

    public String getCompPrdNo() {
        return compPrdNo;
    }

    public McdPrdPricecode setCompPrdNo(String compPrdNo) {
        this.compPrdNo = compPrdNo;
        return this;
    }

    public String getSourceHeadId() {
        return sourceHeadId;
    }

    public McdPrdPricecode setSourceHeadId(String sourceHeadId) {
        this.sourceHeadId = sourceHeadId;
        return this;
    }

    public String getLangId() {
        return langId;
    }

    public McdPrdPricecode setLangId(String langId) {
        this.langId = langId;
        return this;
    }

    public String getBarcodeType() {
        return barcodeType;
    }

    public McdPrdPricecode setBarcodeType(String barcodeType) {
        this.barcodeType = barcodeType;
        return this;
    }

    public Integer getPurchasePrice() {
        return purchasePrice;
    }

    public McdPrdPricecode setPurchasePrice(Integer purchasePrice) {
        this.purchasePrice = purchasePrice;
        return this;
    }

    @Override
    protected Serializable pkVal() {
        return this.pricecodeId;
    }

    @Override
    public String toString() {
        return "McdPrdPricecode{" +
        "pricecodeId=" + pricecodeId +
        ", itemCode=" + itemCode +
        ", itemType=" + itemType +
        ", sourceId=" + sourceId +
        ", sourceType=" + sourceType +
        ", barcode=" + barcode +
        ", currency=" + currency +
        ", advicePrice=" + advicePrice +
        ", createTime=" + createTime +
        ", updateUser=" + updateUser +
        ", updateTime=" + updateTime +
        ", createUser=" + createUser +
        ", systemMemo=" + systemMemo +
        ", compPrdNo=" + compPrdNo +
        ", sourceHeadId=" + sourceHeadId +
        ", langId=" + langId +
        ", barcodeType=" + barcodeType +
        ", purchasePrice=" + purchasePrice +
        "}";
    }
}
