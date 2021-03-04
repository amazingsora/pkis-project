package com.tradevan.mapper.pkis.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author system
 * @since 2020-09-21
 */
@TableName("STATISTICALREPORT")
public class StatisticalReport extends Model<StatisticalReport> {

    private static final long serialVersionUID=1L;

    /**
     * 報表編號
     */
    @TableId("RPTID")
    private String rptid;

    /**
     * 建立人員
     */
    @TableField("CREATEUSER")
    private String createuser;

    /**
     * 建立日期
     */
    @TableField("CREATEDATE")
    private Date createdate;

    /**
     * 異動使用者
     */
    @TableField("UPDATEUSER")
    private String updateuser;

    /**
     * 異動日期
     */
    @TableField("UPDATEDATE")
    private Date updatedate;

    /**
     * 查詢結果
     */
    @TableField("JSON")
    private String json;

    /**
     * 檔案路徑
     */
    @TableField("DOWNLOADPATH")
    private String downloadpath;


    public String getRptid() {
        return rptid;
    }

    public StatisticalReport setRptid(String rptid) {
        this.rptid = rptid;
        return this;
    }

    public String getCreateuser() {
        return createuser;
    }

    public StatisticalReport setCreateuser(String createuser) {
        this.createuser = createuser;
        return this;
    }

    public Date getCreatedate() {
        return createdate;
    }

    public StatisticalReport setCreatedate(Date createdate) {
        this.createdate = createdate;
        return this;
    }

    public String getUpdateuser() {
        return updateuser;
    }

    public StatisticalReport setUpdateuser(String updateuser) {
        this.updateuser = updateuser;
        return this;
    }

    public Date getUpdatedate() {
        return updatedate;
    }

    public StatisticalReport setUpdatedate(Date updatedate) {
        this.updatedate = updatedate;
        return this;
    }

    public String getJson() {
        return json;
    }

    public StatisticalReport setJson(String json) {
        this.json = json;
        return this;
    }

    public String getDownloadpath() {
        return downloadpath;
    }

    public StatisticalReport setDownloadpath(String downloadpath) {
        this.downloadpath = downloadpath;
        return this;
    }

    @Override
    protected Serializable pkVal() {
        return this.rptid;
    }

    @Override
    public String toString() {
        return "StatisticalReport{" +
        "rptid=" + rptid +
        ", createuser=" + createuser +
        ", createdate=" + createdate +
        ", updateuser=" + updateuser +
        ", updatedate=" + updatedate +
        ", json=" + json +
        ", downloadpath=" + downloadpath +
        "}";
    }
}
