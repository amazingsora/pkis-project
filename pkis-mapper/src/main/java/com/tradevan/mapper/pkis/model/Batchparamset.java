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
 * @since 2021-01-14
 */
@TableName("BATCHPARAMSET")
public class Batchparamset extends Model<Batchparamset> {

    private static final long serialVersionUID=1L;

    @TableId("SERNO")
    private Long serno;

    /**
     * 排程名稱
     */
    @TableField("BATCHNAME")
    private String batchname;

    /**
     * 狀態 0:關閉 1:啟用
     */
    @TableField("STATUS")
    private String status;

    /**
     * 等待秒數
     */
    @TableField("SLEEPSEC")
    private Long sleepsec;

    /**
     * 最後一次錯誤時間
     */
    @TableField("LASTERRORDATE")
    private Date lasterrordate;

    /**
     * 最後一次錯誤訊息
     */
    @TableField("LASTERRORMSG")
    private String lasterrormsg;

    @TableField("CREATEUSER")
    private String createuser;

    @TableField("CREATEDATE")
    private Date createdate;

    @TableField("UPDATEUSER")
    private String updateuser;

    @TableField("UPDATEDATE")
    private Date updatedate;


    public Long getSerno() {
        return serno;
    }

    public Batchparamset setSerno(Long serno) {
        this.serno = serno;
        return this;
    }

    public String getBatchname() {
        return batchname;
    }

    public Batchparamset setBatchname(String batchname) {
        this.batchname = batchname;
        return this;
    }

    public String getStatus() {
        return status;
    }

    public Batchparamset setStatus(String status) {
        this.status = status;
        return this;
    }

    public Long getSleepsec() {
        return sleepsec;
    }

    public Batchparamset setSleepsec(Long sleepsec) {
        this.sleepsec = sleepsec;
        return this;
    }

    public Date getLasterrordate() {
        return lasterrordate;
    }

    public Batchparamset setLasterrordate(Date lasterrordate) {
        this.lasterrordate = lasterrordate;
        return this;
    }

    public String getLasterrormsg() {
        return lasterrormsg;
    }

    public Batchparamset setLasterrormsg(String lasterrormsg) {
        this.lasterrormsg = lasterrormsg;
        return this;
    }

    public String getCreateuser() {
        return createuser;
    }

    public Batchparamset setCreateuser(String createuser) {
        this.createuser = createuser;
        return this;
    }

    public Date getCreatedate() {
        return createdate;
    }

    public Batchparamset setCreatedate(Date createdate) {
        this.createdate = createdate;
        return this;
    }

    public String getUpdateuser() {
        return updateuser;
    }

    public Batchparamset setUpdateuser(String updateuser) {
        this.updateuser = updateuser;
        return this;
    }

    public Date getUpdatedate() {
        return updatedate;
    }

    public Batchparamset setUpdatedate(Date updatedate) {
        this.updatedate = updatedate;
        return this;
    }

    @Override
    protected Serializable pkVal() {
        return this.serno;
    }

    @Override
    public String toString() {
        return "Batchparamset{" +
        "serno=" + serno +
        ", batchname=" + batchname +
        ", status=" + status +
        ", sleepsec=" + sleepsec +
        ", lasterrordate=" + lasterrordate +
        ", lasterrormsg=" + lasterrormsg +
        ", createuser=" + createuser +
        ", createdate=" + createdate +
        ", updateuser=" + updateuser +
        ", updatedate=" + updatedate +
        "}";
    }
}
