package com.tradevan.mapper.pkis.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
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
@TableName("DOWN_LOAD_FILE")
public class DownLoadFile extends Model<DownLoadFile> {

    private static final long serialVersionUID=1L;

    /**
     * 流水序號
     */
    @TableId("SEQUENCE_NO")
    private String sequenceNo;

    /**
     * 功能名稱(銷售代號為SALES、庫存代號為INVENTORY、進貨代號為RECEIVE、訂單代號為ORDER、退貨代號為RETURN)
     */
    @TableField("FUNCTION_CODE")
    private String functionCode;

    /**
     * 子功能名稱(彙總表代號SUMMARY、全門市全商品代號為ALLSTOREPRODUCT、單一商品代號為ALONEPRODUCT)
     */
    @TableField("ACTION_CODE")
    private String actionCode;

    /**
     * 日期開始日(格式YYYYMMDD)
     */
    @TableField("DATE_START")
    private String dateStart;

    /**
     * 日期結束日(格式YYYYMMDD)
     */
    @TableField("DATE_END")
    private String dateEnd;

    /**
     * 週間開始日(格式YYYYWW)
     */
    @TableField("WEEK_START")
    private String weekStart;

    /**
     * 週間結束日(格式YYYYWW)
     */
    @TableField("WEEK_END")
    private String weekEnd;

    /**
     * 月份開始日(格式YYYYMM)
     */
    @TableField("MONTH_START")
    private String monthStart;

    /**
     * 月份結束日(格式YYYYMMDD)
     */
    @TableField("MONTH_END")
    private String monthEnd;

    /**
     * 門市代碼(多筆資料時，資料與資料間用逗號做區隔)
     */
    @TableField("STORE_CODE")
    private String storeCode;

    /**
     * 商品貨號(多筆資料時，資料與資料間用逗號做區隔)
     */
    @TableField("PRODUCT_CODE")
    private String productCode;

    /**
     * 供應商GLN號碼(多筆資料時，資料與資料間用逗號做區隔)
     */
    @TableField("SUPPLIER_GLN")
    private String supplierGln;

    /**
     * 供應商廠編(多筆資料時，資料與資料間用逗號做區隔)
     */
    @TableField("SUPPLIER_CODE")
    private String supplierCode;

    /**
     * 商品部門(多筆資料時，資料與資料間用逗號做區隔)
     */
    @TableField("PRODUCT_DEPT")
    private String productDept;

    /**
     * 檔案路徑
     */
    @TableField("FILE_PATH")
    private String filePath;

    /**
     * 使用者代號
     */
    @TableField("CREATE_USER")
    private String createUser;

    /**
     * 使用者要求產生檔案的時間
     */
    @TableField("CREATE_TIME")
    private String createTime;

    /**
     * 異動者代號
     */
    @TableField("UPDATE_USER")
    private String updateUser;

    /**
     * 異動時間
     */
    @TableField("UPDATE_TIME")
    private String updateTime;

    /**
     * 檔案產生時間
     */
    @TableField("FILE_CREATE_TIME")
    private String fileCreateTime;

    /**
     * 使用者下載檔案時間
     */
    @TableField("FILE_DOWNLOAD_TIME")
    private String fileDownloadTime;

    /**
     * 檔案刪除時間
     */
    @TableField("FILE_DELETE_TIME")
    private String fileDeleteTime;

    /**
     * 系統備註說明
     */
    @TableField("SYSTEM_MEMO")
    private String systemMemo;

    @TableField("USER_TYPE")
    private String userType;


    public String getSequenceNo() {
        return sequenceNo;
    }

    public DownLoadFile setSequenceNo(String sequenceNo) {
        this.sequenceNo = sequenceNo;
        return this;
    }

    public String getFunctionCode() {
        return functionCode;
    }

    public DownLoadFile setFunctionCode(String functionCode) {
        this.functionCode = functionCode;
        return this;
    }

    public String getActionCode() {
        return actionCode;
    }

    public DownLoadFile setActionCode(String actionCode) {
        this.actionCode = actionCode;
        return this;
    }

    public String getDateStart() {
        return dateStart;
    }

    public DownLoadFile setDateStart(String dateStart) {
        this.dateStart = dateStart;
        return this;
    }

    public String getDateEnd() {
        return dateEnd;
    }

    public DownLoadFile setDateEnd(String dateEnd) {
        this.dateEnd = dateEnd;
        return this;
    }

    public String getWeekStart() {
        return weekStart;
    }

    public DownLoadFile setWeekStart(String weekStart) {
        this.weekStart = weekStart;
        return this;
    }

    public String getWeekEnd() {
        return weekEnd;
    }

    public DownLoadFile setWeekEnd(String weekEnd) {
        this.weekEnd = weekEnd;
        return this;
    }

    public String getMonthStart() {
        return monthStart;
    }

    public DownLoadFile setMonthStart(String monthStart) {
        this.monthStart = monthStart;
        return this;
    }

    public String getMonthEnd() {
        return monthEnd;
    }

    public DownLoadFile setMonthEnd(String monthEnd) {
        this.monthEnd = monthEnd;
        return this;
    }

    public String getStoreCode() {
        return storeCode;
    }

    public DownLoadFile setStoreCode(String storeCode) {
        this.storeCode = storeCode;
        return this;
    }

    public String getProductCode() {
        return productCode;
    }

    public DownLoadFile setProductCode(String productCode) {
        this.productCode = productCode;
        return this;
    }

    public String getSupplierGln() {
        return supplierGln;
    }

    public DownLoadFile setSupplierGln(String supplierGln) {
        this.supplierGln = supplierGln;
        return this;
    }

    public String getSupplierCode() {
        return supplierCode;
    }

    public DownLoadFile setSupplierCode(String supplierCode) {
        this.supplierCode = supplierCode;
        return this;
    }

    public String getProductDept() {
        return productDept;
    }

    public DownLoadFile setProductDept(String productDept) {
        this.productDept = productDept;
        return this;
    }

    public String getFilePath() {
        return filePath;
    }

    public DownLoadFile setFilePath(String filePath) {
        this.filePath = filePath;
        return this;
    }

    public String getCreateUser() {
        return createUser;
    }

    public DownLoadFile setCreateUser(String createUser) {
        this.createUser = createUser;
        return this;
    }

    public String getCreateTime() {
        return createTime;
    }

    public DownLoadFile setCreateTime(String createTime) {
        this.createTime = createTime;
        return this;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public DownLoadFile setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
        return this;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public DownLoadFile setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
        return this;
    }

    public String getFileCreateTime() {
        return fileCreateTime;
    }

    public DownLoadFile setFileCreateTime(String fileCreateTime) {
        this.fileCreateTime = fileCreateTime;
        return this;
    }

    public String getFileDownloadTime() {
        return fileDownloadTime;
    }

    public DownLoadFile setFileDownloadTime(String fileDownloadTime) {
        this.fileDownloadTime = fileDownloadTime;
        return this;
    }

    public String getFileDeleteTime() {
        return fileDeleteTime;
    }

    public DownLoadFile setFileDeleteTime(String fileDeleteTime) {
        this.fileDeleteTime = fileDeleteTime;
        return this;
    }

    public String getSystemMemo() {
        return systemMemo;
    }

    public DownLoadFile setSystemMemo(String systemMemo) {
        this.systemMemo = systemMemo;
        return this;
    }

    public String getUserType() {
        return userType;
    }

    public DownLoadFile setUserType(String userType) {
        this.userType = userType;
        return this;
    }

    @Override
    protected Serializable pkVal() {
        return this.sequenceNo;
    }

    @Override
    public String toString() {
        return "DownLoadFile{" +
        "sequenceNo=" + sequenceNo +
        ", functionCode=" + functionCode +
        ", actionCode=" + actionCode +
        ", dateStart=" + dateStart +
        ", dateEnd=" + dateEnd +
        ", weekStart=" + weekStart +
        ", weekEnd=" + weekEnd +
        ", monthStart=" + monthStart +
        ", monthEnd=" + monthEnd +
        ", storeCode=" + storeCode +
        ", productCode=" + productCode +
        ", supplierGln=" + supplierGln +
        ", supplierCode=" + supplierCode +
        ", productDept=" + productDept +
        ", filePath=" + filePath +
        ", createUser=" + createUser +
        ", createTime=" + createTime +
        ", updateUser=" + updateUser +
        ", updateTime=" + updateTime +
        ", fileCreateTime=" + fileCreateTime +
        ", fileDownloadTime=" + fileDownloadTime +
        ", fileDeleteTime=" + fileDeleteTime +
        ", systemMemo=" + systemMemo +
        ", userType=" + userType +
        "}";
    }
}
