package com.tradevan.mapper.pkis.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

/**
 * <p>
 * 組合包關聯檔
 * </p>
 *
 * @author penny
 * @since 2020-03-04
 */
@TableName("MCD_PRD_BUNDLE")
public class McdPrdBundle extends Model<McdPrdBundle> {

    private static final long serialVersionUID=1L;

    /**
     * 流水號：ITEM_CODE(14碼)+包裝方式(EA-單品，PK-組合包，CA-箱，PL-棧板)+數量(2碼)+SUB數量(2碼)=20碼
     */
    @TableId("BUNDLE_SUBID")
    private String bundleSubid;

    /**
     * 組合包ID
     */
    @TableField("BUNDLE_ID")
    private String bundleId;

    /**
     * 單品的顏色尺吋ID
     */
    @TableField("SINGLE_SUBID")
    private String singleSubid;

    /**
     * 不是組合商品的總入數
     */
    @TableField("ENTRY_NUM")
    private Integer entryNum;

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

    @TableField("ITEM_CODE")
    private String itemCode;

    /**
     * 單品入數
     */
    @TableField("ENTRY_EA_NUM")
    private Integer entryEaNum;


    public String getBundleSubid() {
        return bundleSubid;
    }

    public McdPrdBundle setBundleSubid(String bundleSubid) {
        this.bundleSubid = bundleSubid;
        return this;
    }

    public String getBundleId() {
        return bundleId;
    }

    public McdPrdBundle setBundleId(String bundleId) {
        this.bundleId = bundleId;
        return this;
    }

    public String getSingleSubid() {
        return singleSubid;
    }

    public McdPrdBundle setSingleSubid(String singleSubid) {
        this.singleSubid = singleSubid;
        return this;
    }

    public Integer getEntryNum() {
        return entryNum;
    }

    public McdPrdBundle setEntryNum(Integer entryNum) {
        this.entryNum = entryNum;
        return this;
    }

    public String getCreateUser() {
        return createUser;
    }

    public McdPrdBundle setCreateUser(String createUser) {
        this.createUser = createUser;
        return this;
    }

    public String getCreateTime() {
        return createTime;
    }

    public McdPrdBundle setCreateTime(String createTime) {
        this.createTime = createTime;
        return this;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public McdPrdBundle setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
        return this;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public McdPrdBundle setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
        return this;
    }

    public String getSystemMemo() {
        return systemMemo;
    }

    public McdPrdBundle setSystemMemo(String systemMemo) {
        this.systemMemo = systemMemo;
        return this;
    }

    public String getItemCode() {
        return itemCode;
    }

    public McdPrdBundle setItemCode(String itemCode) {
        this.itemCode = itemCode;
        return this;
    }

    public Integer getEntryEaNum() {
        return entryEaNum;
    }

    public McdPrdBundle setEntryEaNum(Integer entryEaNum) {
        this.entryEaNum = entryEaNum;
        return this;
    }

    @Override
    protected Serializable pkVal() {
        return this.bundleSubid;
    }

    @Override
    public String toString() {
        return "McdPrdBundle{" +
        "bundleSubid=" + bundleSubid +
        ", bundleId=" + bundleId +
        ", singleSubid=" + singleSubid +
        ", entryNum=" + entryNum +
        ", createUser=" + createUser +
        ", createTime=" + createTime +
        ", updateUser=" + updateUser +
        ", updateTime=" + updateTime +
        ", systemMemo=" + systemMemo +
        ", itemCode=" + itemCode +
        ", entryEaNum=" + entryEaNum +
        "}";
    }
}
