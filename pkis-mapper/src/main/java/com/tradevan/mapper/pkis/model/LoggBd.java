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
@TableName("LOGG_BD")
public class LoggBd extends Model<LoggBd> {

    private static final long serialVersionUID=1L;

    /**
     * 檔案類型(P4CMA為銷售資料、P4CML為庫存檔案)
     */
    @TableId("FILES_TYPE")
    private String filesType;

    /**
     * 門市代碼
     */
    @TableField("STORE_NO")
    private String storeNo;

    /**
     * 年月
     */
    @TableField("DATE_YM")
    private String dateYm;

    /**
     * 1號狀態(空值：沒有檔案、1：檔案接收完成、2：檔案處理中、3：檔案處理完畢)
     */
    @TableField("D01_STATUS")
    private String d01Status;

    /**
     * 1號異動時間
     */
    @TableField("D01_DATE_TIME")
    private String d01DateTime;

    /**
     * 2號狀態
     */
    @TableField("D02_STATUS")
    private String d02Status;

    /**
     * 2號異動時間
     */
    @TableField("D02_DATE_TIME")
    private String d02DateTime;

    /**
     * 3號狀態
     */
    @TableField("D03_STATUS")
    private String d03Status;

    /**
     * 3號異動時間
     */
    @TableField("D03_DATE_TIME")
    private String d03DateTime;

    /**
     * 4號狀態
     */
    @TableField("D04_STATUS")
    private String d04Status;

    /**
     * 4號異動時間
     */
    @TableField("D04_DATE_TIME")
    private String d04DateTime;

    /**
     * 5號狀態
     */
    @TableField("D05_STATUS")
    private String d05Status;

    /**
     * 5號異動時間
     */
    @TableField("D05_DATE_TIME")
    private String d05DateTime;

    /**
     * 6號狀態
     */
    @TableField("D06_STATUS")
    private String d06Status;

    /**
     * 6號異動時間
     */
    @TableField("D06_DATE_TIME")
    private String d06DateTime;

    /**
     * 7號狀態
     */
    @TableField("D07_STATUS")
    private String d07Status;

    /**
     * 7號異動時間
     */
    @TableField("D07_DATE_TIME")
    private String d07DateTime;

    /**
     * 8號狀態
     */
    @TableField("D08_STATUS")
    private String d08Status;

    /**
     * 8號異動時間
     */
    @TableField("D08_DATE_TIME")
    private String d08DateTime;

    /**
     * 9號狀態
     */
    @TableField("D09_STATUS")
    private String d09Status;

    /**
     * 9號異動時間
     */
    @TableField("D09_DATE_TIME")
    private String d09DateTime;

    /**
     * 10號狀態
     */
    @TableField("D10_STATUS")
    private String d10Status;

    /**
     * 10號異動時間
     */
    @TableField("D10_DATE_TIME")
    private String d10DateTime;

    /**
     * 11號狀態
     */
    @TableField("D11_STATUS")
    private String d11Status;

    /**
     * 11號異動時間
     */
    @TableField("D11_DATE_TIME")
    private String d11DateTime;

    /**
     * 12號狀態
     */
    @TableField("D12_STATUS")
    private String d12Status;

    /**
     * 12號異動時間
     */
    @TableField("D12_DATE_TIME")
    private String d12DateTime;

    /**
     * 13號狀態
     */
    @TableField("D13_STATUS")
    private String d13Status;

    /**
     * 13號異動時間
     */
    @TableField("D13_DATE_TIME")
    private String d13DateTime;

    /**
     * 14號狀態
     */
    @TableField("D14_STATUS")
    private String d14Status;

    /**
     * 14號異動時間
     */
    @TableField("D14_DATE_TIME")
    private String d14DateTime;

    /**
     * 15號狀態
     */
    @TableField("D15_STATUS")
    private String d15Status;

    /**
     * 15號異動時間
     */
    @TableField("D15_DATE_TIME")
    private String d15DateTime;

    /**
     * 16號狀態
     */
    @TableField("D16_STATUS")
    private String d16Status;

    /**
     * 16號異動時間
     */
    @TableField("D16_DATE_TIME")
    private String d16DateTime;

    /**
     * 17號狀態
     */
    @TableField("D17_STATUS")
    private String d17Status;

    /**
     * 17號異動時間
     */
    @TableField("D17_DATE_TIME")
    private String d17DateTime;

    /**
     * 18號狀態
     */
    @TableField("D18_STATUS")
    private String d18Status;

    /**
     * 18號異動時間
     */
    @TableField("D18_DATE_TIME")
    private String d18DateTime;

    /**
     * 19號狀態
     */
    @TableField("D19_STATUS")
    private String d19Status;

    /**
     * 19號異動時間
     */
    @TableField("D19_DATE_TIME")
    private String d19DateTime;

    /**
     * 20號狀態
     */
    @TableField("D20_STATUS")
    private String d20Status;

    /**
     * 20號異動時間
     */
    @TableField("D20_DATE_TIME")
    private String d20DateTime;

    /**
     * 21號狀態
     */
    @TableField("D21_STATUS")
    private String d21Status;

    /**
     * 21號異動時間
     */
    @TableField("D21_DATE_TIME")
    private String d21DateTime;

    /**
     * 22號狀態
     */
    @TableField("D22_STATUS")
    private String d22Status;

    /**
     * 22號異動時間
     */
    @TableField("D22_DATE_TIME")
    private String d22DateTime;

    /**
     * 23號狀態
     */
    @TableField("D23_STATUS")
    private String d23Status;

    /**
     * 23號異動時間
     */
    @TableField("D23_DATE_TIME")
    private String d23DateTime;

    /**
     * 24號狀態
     */
    @TableField("D24_STATUS")
    private String d24Status;

    /**
     * 24號異動時間
     */
    @TableField("D24_DATE_TIME")
    private String d24DateTime;

    /**
     * 25號狀態
     */
    @TableField("D25_STATUS")
    private String d25Status;

    /**
     * 25號異動時間
     */
    @TableField("D25_DATE_TIME")
    private String d25DateTime;

    /**
     * 26號狀態
     */
    @TableField("D26_STATUS")
    private String d26Status;

    /**
     * 26號異動時間
     */
    @TableField("D26_DATE_TIME")
    private String d26DateTime;

    /**
     * 27號狀態
     */
    @TableField("D27_STATUS")
    private String d27Status;

    /**
     * 27號異動時間
     */
    @TableField("D27_DATE_TIME")
    private String d27DateTime;

    /**
     * 28號狀態
     */
    @TableField("D28_STATUS")
    private String d28Status;

    /**
     * 28號異動時間
     */
    @TableField("D28_DATE_TIME")
    private String d28DateTime;

    /**
     * 29號狀態
     */
    @TableField("D29_STATUS")
    private String d29Status;

    /**
     * 29號異動時間
     */
    @TableField("D29_DATE_TIME")
    private String d29DateTime;

    /**
     * 30號狀態
     */
    @TableField("D30_STATUS")
    private String d30Status;

    /**
     * 30號異動時間
     */
    @TableField("D30_DATE_TIME")
    private String d30DateTime;

    /**
     * 31號狀態
     */
    @TableField("D31_STATUS")
    private String d31Status;

    /**
     * 31號異動時間
     */
    @TableField("D31_DATE_TIME")
    private String d31DateTime;

    /**
     * 系統備註
     */
    @TableField("SYSTEM_MEMO")
    private String systemMemo;


    public String getFilesType() {
        return filesType;
    }

    public LoggBd setFilesType(String filesType) {
        this.filesType = filesType;
        return this;
    }

    public String getStoreNo() {
        return storeNo;
    }

    public LoggBd setStoreNo(String storeNo) {
        this.storeNo = storeNo;
        return this;
    }

    public String getDateYm() {
        return dateYm;
    }

    public LoggBd setDateYm(String dateYm) {
        this.dateYm = dateYm;
        return this;
    }

    public String getD01Status() {
        return d01Status;
    }

    public LoggBd setD01Status(String d01Status) {
        this.d01Status = d01Status;
        return this;
    }

    public String getD01DateTime() {
        return d01DateTime;
    }

    public LoggBd setD01DateTime(String d01DateTime) {
        this.d01DateTime = d01DateTime;
        return this;
    }

    public String getD02Status() {
        return d02Status;
    }

    public LoggBd setD02Status(String d02Status) {
        this.d02Status = d02Status;
        return this;
    }

    public String getD02DateTime() {
        return d02DateTime;
    }

    public LoggBd setD02DateTime(String d02DateTime) {
        this.d02DateTime = d02DateTime;
        return this;
    }

    public String getD03Status() {
        return d03Status;
    }

    public LoggBd setD03Status(String d03Status) {
        this.d03Status = d03Status;
        return this;
    }

    public String getD03DateTime() {
        return d03DateTime;
    }

    public LoggBd setD03DateTime(String d03DateTime) {
        this.d03DateTime = d03DateTime;
        return this;
    }

    public String getD04Status() {
        return d04Status;
    }

    public LoggBd setD04Status(String d04Status) {
        this.d04Status = d04Status;
        return this;
    }

    public String getD04DateTime() {
        return d04DateTime;
    }

    public LoggBd setD04DateTime(String d04DateTime) {
        this.d04DateTime = d04DateTime;
        return this;
    }

    public String getD05Status() {
        return d05Status;
    }

    public LoggBd setD05Status(String d05Status) {
        this.d05Status = d05Status;
        return this;
    }

    public String getD05DateTime() {
        return d05DateTime;
    }

    public LoggBd setD05DateTime(String d05DateTime) {
        this.d05DateTime = d05DateTime;
        return this;
    }

    public String getD06Status() {
        return d06Status;
    }

    public LoggBd setD06Status(String d06Status) {
        this.d06Status = d06Status;
        return this;
    }

    public String getD06DateTime() {
        return d06DateTime;
    }

    public LoggBd setD06DateTime(String d06DateTime) {
        this.d06DateTime = d06DateTime;
        return this;
    }

    public String getD07Status() {
        return d07Status;
    }

    public LoggBd setD07Status(String d07Status) {
        this.d07Status = d07Status;
        return this;
    }

    public String getD07DateTime() {
        return d07DateTime;
    }

    public LoggBd setD07DateTime(String d07DateTime) {
        this.d07DateTime = d07DateTime;
        return this;
    }

    public String getD08Status() {
        return d08Status;
    }

    public LoggBd setD08Status(String d08Status) {
        this.d08Status = d08Status;
        return this;
    }

    public String getD08DateTime() {
        return d08DateTime;
    }

    public LoggBd setD08DateTime(String d08DateTime) {
        this.d08DateTime = d08DateTime;
        return this;
    }

    public String getD09Status() {
        return d09Status;
    }

    public LoggBd setD09Status(String d09Status) {
        this.d09Status = d09Status;
        return this;
    }

    public String getD09DateTime() {
        return d09DateTime;
    }

    public LoggBd setD09DateTime(String d09DateTime) {
        this.d09DateTime = d09DateTime;
        return this;
    }

    public String getD10Status() {
        return d10Status;
    }

    public LoggBd setD10Status(String d10Status) {
        this.d10Status = d10Status;
        return this;
    }

    public String getD10DateTime() {
        return d10DateTime;
    }

    public LoggBd setD10DateTime(String d10DateTime) {
        this.d10DateTime = d10DateTime;
        return this;
    }

    public String getD11Status() {
        return d11Status;
    }

    public LoggBd setD11Status(String d11Status) {
        this.d11Status = d11Status;
        return this;
    }

    public String getD11DateTime() {
        return d11DateTime;
    }

    public LoggBd setD11DateTime(String d11DateTime) {
        this.d11DateTime = d11DateTime;
        return this;
    }

    public String getD12Status() {
        return d12Status;
    }

    public LoggBd setD12Status(String d12Status) {
        this.d12Status = d12Status;
        return this;
    }

    public String getD12DateTime() {
        return d12DateTime;
    }

    public LoggBd setD12DateTime(String d12DateTime) {
        this.d12DateTime = d12DateTime;
        return this;
    }

    public String getD13Status() {
        return d13Status;
    }

    public LoggBd setD13Status(String d13Status) {
        this.d13Status = d13Status;
        return this;
    }

    public String getD13DateTime() {
        return d13DateTime;
    }

    public LoggBd setD13DateTime(String d13DateTime) {
        this.d13DateTime = d13DateTime;
        return this;
    }

    public String getD14Status() {
        return d14Status;
    }

    public LoggBd setD14Status(String d14Status) {
        this.d14Status = d14Status;
        return this;
    }

    public String getD14DateTime() {
        return d14DateTime;
    }

    public LoggBd setD14DateTime(String d14DateTime) {
        this.d14DateTime = d14DateTime;
        return this;
    }

    public String getD15Status() {
        return d15Status;
    }

    public LoggBd setD15Status(String d15Status) {
        this.d15Status = d15Status;
        return this;
    }

    public String getD15DateTime() {
        return d15DateTime;
    }

    public LoggBd setD15DateTime(String d15DateTime) {
        this.d15DateTime = d15DateTime;
        return this;
    }

    public String getD16Status() {
        return d16Status;
    }

    public LoggBd setD16Status(String d16Status) {
        this.d16Status = d16Status;
        return this;
    }

    public String getD16DateTime() {
        return d16DateTime;
    }

    public LoggBd setD16DateTime(String d16DateTime) {
        this.d16DateTime = d16DateTime;
        return this;
    }

    public String getD17Status() {
        return d17Status;
    }

    public LoggBd setD17Status(String d17Status) {
        this.d17Status = d17Status;
        return this;
    }

    public String getD17DateTime() {
        return d17DateTime;
    }

    public LoggBd setD17DateTime(String d17DateTime) {
        this.d17DateTime = d17DateTime;
        return this;
    }

    public String getD18Status() {
        return d18Status;
    }

    public LoggBd setD18Status(String d18Status) {
        this.d18Status = d18Status;
        return this;
    }

    public String getD18DateTime() {
        return d18DateTime;
    }

    public LoggBd setD18DateTime(String d18DateTime) {
        this.d18DateTime = d18DateTime;
        return this;
    }

    public String getD19Status() {
        return d19Status;
    }

    public LoggBd setD19Status(String d19Status) {
        this.d19Status = d19Status;
        return this;
    }

    public String getD19DateTime() {
        return d19DateTime;
    }

    public LoggBd setD19DateTime(String d19DateTime) {
        this.d19DateTime = d19DateTime;
        return this;
    }

    public String getD20Status() {
        return d20Status;
    }

    public LoggBd setD20Status(String d20Status) {
        this.d20Status = d20Status;
        return this;
    }

    public String getD20DateTime() {
        return d20DateTime;
    }

    public LoggBd setD20DateTime(String d20DateTime) {
        this.d20DateTime = d20DateTime;
        return this;
    }

    public String getD21Status() {
        return d21Status;
    }

    public LoggBd setD21Status(String d21Status) {
        this.d21Status = d21Status;
        return this;
    }

    public String getD21DateTime() {
        return d21DateTime;
    }

    public LoggBd setD21DateTime(String d21DateTime) {
        this.d21DateTime = d21DateTime;
        return this;
    }

    public String getD22Status() {
        return d22Status;
    }

    public LoggBd setD22Status(String d22Status) {
        this.d22Status = d22Status;
        return this;
    }

    public String getD22DateTime() {
        return d22DateTime;
    }

    public LoggBd setD22DateTime(String d22DateTime) {
        this.d22DateTime = d22DateTime;
        return this;
    }

    public String getD23Status() {
        return d23Status;
    }

    public LoggBd setD23Status(String d23Status) {
        this.d23Status = d23Status;
        return this;
    }

    public String getD23DateTime() {
        return d23DateTime;
    }

    public LoggBd setD23DateTime(String d23DateTime) {
        this.d23DateTime = d23DateTime;
        return this;
    }

    public String getD24Status() {
        return d24Status;
    }

    public LoggBd setD24Status(String d24Status) {
        this.d24Status = d24Status;
        return this;
    }

    public String getD24DateTime() {
        return d24DateTime;
    }

    public LoggBd setD24DateTime(String d24DateTime) {
        this.d24DateTime = d24DateTime;
        return this;
    }

    public String getD25Status() {
        return d25Status;
    }

    public LoggBd setD25Status(String d25Status) {
        this.d25Status = d25Status;
        return this;
    }

    public String getD25DateTime() {
        return d25DateTime;
    }

    public LoggBd setD25DateTime(String d25DateTime) {
        this.d25DateTime = d25DateTime;
        return this;
    }

    public String getD26Status() {
        return d26Status;
    }

    public LoggBd setD26Status(String d26Status) {
        this.d26Status = d26Status;
        return this;
    }

    public String getD26DateTime() {
        return d26DateTime;
    }

    public LoggBd setD26DateTime(String d26DateTime) {
        this.d26DateTime = d26DateTime;
        return this;
    }

    public String getD27Status() {
        return d27Status;
    }

    public LoggBd setD27Status(String d27Status) {
        this.d27Status = d27Status;
        return this;
    }

    public String getD27DateTime() {
        return d27DateTime;
    }

    public LoggBd setD27DateTime(String d27DateTime) {
        this.d27DateTime = d27DateTime;
        return this;
    }

    public String getD28Status() {
        return d28Status;
    }

    public LoggBd setD28Status(String d28Status) {
        this.d28Status = d28Status;
        return this;
    }

    public String getD28DateTime() {
        return d28DateTime;
    }

    public LoggBd setD28DateTime(String d28DateTime) {
        this.d28DateTime = d28DateTime;
        return this;
    }

    public String getD29Status() {
        return d29Status;
    }

    public LoggBd setD29Status(String d29Status) {
        this.d29Status = d29Status;
        return this;
    }

    public String getD29DateTime() {
        return d29DateTime;
    }

    public LoggBd setD29DateTime(String d29DateTime) {
        this.d29DateTime = d29DateTime;
        return this;
    }

    public String getD30Status() {
        return d30Status;
    }

    public LoggBd setD30Status(String d30Status) {
        this.d30Status = d30Status;
        return this;
    }

    public String getD30DateTime() {
        return d30DateTime;
    }

    public LoggBd setD30DateTime(String d30DateTime) {
        this.d30DateTime = d30DateTime;
        return this;
    }

    public String getD31Status() {
        return d31Status;
    }

    public LoggBd setD31Status(String d31Status) {
        this.d31Status = d31Status;
        return this;
    }

    public String getD31DateTime() {
        return d31DateTime;
    }

    public LoggBd setD31DateTime(String d31DateTime) {
        this.d31DateTime = d31DateTime;
        return this;
    }

    public String getSystemMemo() {
        return systemMemo;
    }

    public LoggBd setSystemMemo(String systemMemo) {
        this.systemMemo = systemMemo;
        return this;
    }

    @Override
    protected Serializable pkVal() {
        return this.filesType;
    }

    @Override
    public String toString() {
        return "LoggBd{" +
        "filesType=" + filesType +
        ", storeNo=" + storeNo +
        ", dateYm=" + dateYm +
        ", d01Status=" + d01Status +
        ", d01DateTime=" + d01DateTime +
        ", d02Status=" + d02Status +
        ", d02DateTime=" + d02DateTime +
        ", d03Status=" + d03Status +
        ", d03DateTime=" + d03DateTime +
        ", d04Status=" + d04Status +
        ", d04DateTime=" + d04DateTime +
        ", d05Status=" + d05Status +
        ", d05DateTime=" + d05DateTime +
        ", d06Status=" + d06Status +
        ", d06DateTime=" + d06DateTime +
        ", d07Status=" + d07Status +
        ", d07DateTime=" + d07DateTime +
        ", d08Status=" + d08Status +
        ", d08DateTime=" + d08DateTime +
        ", d09Status=" + d09Status +
        ", d09DateTime=" + d09DateTime +
        ", d10Status=" + d10Status +
        ", d10DateTime=" + d10DateTime +
        ", d11Status=" + d11Status +
        ", d11DateTime=" + d11DateTime +
        ", d12Status=" + d12Status +
        ", d12DateTime=" + d12DateTime +
        ", d13Status=" + d13Status +
        ", d13DateTime=" + d13DateTime +
        ", d14Status=" + d14Status +
        ", d14DateTime=" + d14DateTime +
        ", d15Status=" + d15Status +
        ", d15DateTime=" + d15DateTime +
        ", d16Status=" + d16Status +
        ", d16DateTime=" + d16DateTime +
        ", d17Status=" + d17Status +
        ", d17DateTime=" + d17DateTime +
        ", d18Status=" + d18Status +
        ", d18DateTime=" + d18DateTime +
        ", d19Status=" + d19Status +
        ", d19DateTime=" + d19DateTime +
        ", d20Status=" + d20Status +
        ", d20DateTime=" + d20DateTime +
        ", d21Status=" + d21Status +
        ", d21DateTime=" + d21DateTime +
        ", d22Status=" + d22Status +
        ", d22DateTime=" + d22DateTime +
        ", d23Status=" + d23Status +
        ", d23DateTime=" + d23DateTime +
        ", d24Status=" + d24Status +
        ", d24DateTime=" + d24DateTime +
        ", d25Status=" + d25Status +
        ", d25DateTime=" + d25DateTime +
        ", d26Status=" + d26Status +
        ", d26DateTime=" + d26DateTime +
        ", d27Status=" + d27Status +
        ", d27DateTime=" + d27DateTime +
        ", d28Status=" + d28Status +
        ", d28DateTime=" + d28DateTime +
        ", d29Status=" + d29Status +
        ", d29DateTime=" + d29DateTime +
        ", d30Status=" + d30Status +
        ", d30DateTime=" + d30DateTime +
        ", d31Status=" + d31Status +
        ", d31DateTime=" + d31DateTime +
        ", systemMemo=" + systemMemo +
        "}";
    }
}
