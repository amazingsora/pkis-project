package com.tradevan.mapper.pkis.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

/**
 * <p>
 * 附件檔
 * </p>
 *
 * @author penny
 * @since 2020-03-04
 */
@TableName("MCD_REF_ATTACH")
public class McdRefAttach extends Model<McdRefAttach> {

    private static final long serialVersionUID=1L;

    /**
     * 附件ID
     */
    @TableId("ATTACH_ID")
    private String attachId;

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
     * 來源ID，若是規格屬性有檔案上傳時，該欄位存放attributeCode
     */
    @TableField("SOURCE_ID")
    private String sourceId;

    /**
     * 來源型態：A-單一商品，M-複合商品，F-贈品，EA-單品，PK-組合包，CA-箱，PL-棧板，規格-SPC
公司-CMP，檢驗-ISP，檢疫-QAT，原產地證明-COO

     */
    @TableField("SOURCE_TYPE")
    private String sourceType;

    /**
     * 上傳原檔名
     */
    @TableField("ORIGINAL_FILE_NAME")
    private String originalFileName;

    /**
     * 完整路徑
     */
    @TableField("WEB_PATH")
    private String webPath;

    /**
     * 根目錄
     */
    @TableField("ATTACH_ROOT")
    private String attachRoot;

    /**
     * 系統產生的檔名
     */
    @TableField("ATTACH_NAME")
    private String attachName;

    /**
     * 附件檔案類型：DOC/PIC
     */
    @TableField("ATTACH_TYPE")
    private String attachType;

    /**
     * 排序
     */
    @TableField("SORT_INDEX")
    private Integer sortIndex;

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


    public String getAttachId() {
        return attachId;
    }

    public McdRefAttach setAttachId(String attachId) {
        this.attachId = attachId;
        return this;
    }

    public String getItemCode() {
        return itemCode;
    }

    public McdRefAttach setItemCode(String itemCode) {
        this.itemCode = itemCode;
        return this;
    }

    public String getItemType() {
        return itemType;
    }

    public McdRefAttach setItemType(String itemType) {
        this.itemType = itemType;
        return this;
    }

    public String getSourceId() {
        return sourceId;
    }

    public McdRefAttach setSourceId(String sourceId) {
        this.sourceId = sourceId;
        return this;
    }

    public String getSourceType() {
        return sourceType;
    }

    public McdRefAttach setSourceType(String sourceType) {
        this.sourceType = sourceType;
        return this;
    }

    public String getOriginalFileName() {
        return originalFileName;
    }

    public McdRefAttach setOriginalFileName(String originalFileName) {
        this.originalFileName = originalFileName;
        return this;
    }

    public String getWebPath() {
        return webPath;
    }

    public McdRefAttach setWebPath(String webPath) {
        this.webPath = webPath;
        return this;
    }

    public String getAttachRoot() {
        return attachRoot;
    }

    public McdRefAttach setAttachRoot(String attachRoot) {
        this.attachRoot = attachRoot;
        return this;
    }

    public String getAttachName() {
        return attachName;
    }

    public McdRefAttach setAttachName(String attachName) {
        this.attachName = attachName;
        return this;
    }

    public String getAttachType() {
        return attachType;
    }

    public McdRefAttach setAttachType(String attachType) {
        this.attachType = attachType;
        return this;
    }

    public Integer getSortIndex() {
        return sortIndex;
    }

    public McdRefAttach setSortIndex(Integer sortIndex) {
        this.sortIndex = sortIndex;
        return this;
    }

    public String getCreateUser() {
        return createUser;
    }

    public McdRefAttach setCreateUser(String createUser) {
        this.createUser = createUser;
        return this;
    }

    public String getCreateTime() {
        return createTime;
    }

    public McdRefAttach setCreateTime(String createTime) {
        this.createTime = createTime;
        return this;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public McdRefAttach setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
        return this;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public McdRefAttach setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
        return this;
    }

    public String getSystemMemo() {
        return systemMemo;
    }

    public McdRefAttach setSystemMemo(String systemMemo) {
        this.systemMemo = systemMemo;
        return this;
    }

    @Override
    protected Serializable pkVal() {
        return this.attachId;
    }

    @Override
    public String toString() {
        return "McdRefAttach{" +
        "attachId=" + attachId +
        ", itemCode=" + itemCode +
        ", itemType=" + itemType +
        ", sourceId=" + sourceId +
        ", sourceType=" + sourceType +
        ", originalFileName=" + originalFileName +
        ", webPath=" + webPath +
        ", attachRoot=" + attachRoot +
        ", attachName=" + attachName +
        ", attachType=" + attachType +
        ", sortIndex=" + sortIndex +
        ", createUser=" + createUser +
        ", createTime=" + createTime +
        ", updateUser=" + updateUser +
        ", updateTime=" + updateTime +
        ", systemMemo=" + systemMemo +
        "}";
    }
}
