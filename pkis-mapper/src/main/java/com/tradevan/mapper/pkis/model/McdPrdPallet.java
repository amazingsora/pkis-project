package com.tradevan.mapper.pkis.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

/**
 * <p>
 * 棧板關聯檔
 * </p>
 *
 * @author penny
 * @since 2020-03-04
 */
@TableName("MCD_PRD_PALLET")
public class McdPrdPallet extends Model<McdPrdPallet> {

    private static final long serialVersionUID=1L;

    /**
     * 流水號：ITEM_CODE(14碼)+包裝方式(EA-單品，PK-組合包，CA-箱，PL-棧板)+數量(2碼)+SUB數量(2碼)=20碼
     */
    @TableId("PALLET_SUBID")
    private String palletSubid;

    /**
     * 棧板設定檔ID
     */
    @TableField("PALLET_ID")
    private String palletId;

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
     * 箱ID
     */
    @TableField("BOX_ID")
    private String boxId;

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


    public String getPalletSubid() {
        return palletSubid;
    }

    public McdPrdPallet setPalletSubid(String palletSubid) {
        this.palletSubid = palletSubid;
        return this;
    }

    public String getPalletId() {
        return palletId;
    }

    public McdPrdPallet setPalletId(String palletId) {
        this.palletId = palletId;
        return this;
    }

    public String getItemCode() {
        return itemCode;
    }

    public McdPrdPallet setItemCode(String itemCode) {
        this.itemCode = itemCode;
        return this;
    }

    public String getItemType() {
        return itemType;
    }

    public McdPrdPallet setItemType(String itemType) {
        this.itemType = itemType;
        return this;
    }

    public String getStatus() {
        return status;
    }

    public McdPrdPallet setStatus(String status) {
        this.status = status;
        return this;
    }

    public String getBoxId() {
        return boxId;
    }

    public McdPrdPallet setBoxId(String boxId) {
        this.boxId = boxId;
        return this;
    }

    public Integer getLayerNum() {
        return layerNum;
    }

    public McdPrdPallet setLayerNum(Integer layerNum) {
        this.layerNum = layerNum;
        return this;
    }

    public Integer getBoardLayer() {
        return boardLayer;
    }

    public McdPrdPallet setBoardLayer(Integer boardLayer) {
        this.boardLayer = boardLayer;
        return this;
    }

    public String getCreateUser() {
        return createUser;
    }

    public McdPrdPallet setCreateUser(String createUser) {
        this.createUser = createUser;
        return this;
    }

    public String getCreateTime() {
        return createTime;
    }

    public McdPrdPallet setCreateTime(String createTime) {
        this.createTime = createTime;
        return this;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public McdPrdPallet setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
        return this;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public McdPrdPallet setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
        return this;
    }

    public String getSystemMemo() {
        return systemMemo;
    }

    public McdPrdPallet setSystemMemo(String systemMemo) {
        this.systemMemo = systemMemo;
        return this;
    }

    @Override
    protected Serializable pkVal() {
        return this.palletSubid;
    }

    @Override
    public String toString() {
        return "McdPrdPallet{" +
        "palletSubid=" + palletSubid +
        ", palletId=" + palletId +
        ", itemCode=" + itemCode +
        ", itemType=" + itemType +
        ", status=" + status +
        ", boxId=" + boxId +
        ", layerNum=" + layerNum +
        ", boardLayer=" + boardLayer +
        ", createUser=" + createUser +
        ", createTime=" + createTime +
        ", updateUser=" + updateUser +
        ", updateTime=" + updateTime +
        ", systemMemo=" + systemMemo +
        "}";
    }
}
