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
 * @author system
 * @since 2020-08-25
 */
@TableName("CONTRACTFILE")
public class Contractfile extends Model<Contractfile> {

    private static final long serialVersionUID=1L;

    @TableField("SERNO")
    private Long serno;

    @TableField("DATAID")
    private String dataid;

    @TableField("FLIEPATH")
    private String fliepath;

    @TableField("FLIENAME")
    private String fliename;

    @TableField("ISDOWNLOAD")
    private String isdownload;

    @TableField("FLIENOTE")
    private String flienote;

    @TableField("TYPE")
    private String type;

    @TableField("FIXID")
    private String fixid;

    @TableField("CREATEUSER")
    private String createuser;

    @TableField("CREATEDATE")
    private Date createdate;

    @TableField("UPDATEUSER")
    private String updateuser;

    @TableField("UPDATEDATE")
    private Date updatedate;

    /**
     * 附件資料 檔案類型
     */
    @TableField("FILETYPE")
    private String filetype;


    public Long getSerno() {
        return serno;
    }

    public Contractfile setSerno(Long serno) {
        this.serno = serno;
        return this;
    }

    public String getDataid() {
        return dataid;
    }

    public Contractfile setDataid(String dataid) {
        this.dataid = dataid;
        return this;
    }

    public String getFliepath() {
        return fliepath;
    }

    public Contractfile setFliepath(String fliepath) {
        this.fliepath = fliepath;
        return this;
    }

    public String getFliename() {
        return fliename;
    }

    public Contractfile setFliename(String fliename) {
        this.fliename = fliename;
        return this;
    }

    public String getIsdownload() {
        return isdownload;
    }

    public Contractfile setIsdownload(String isdownload) {
        this.isdownload = isdownload;
        return this;
    }

    public String getFlienote() {
        return flienote;
    }

    public Contractfile setFlienote(String flienote) {
        this.flienote = flienote;
        return this;
    }

    public String getType() {
        return type;
    }

    public Contractfile setType(String type) {
        this.type = type;
        return this;
    }

    public String getFixid() {
        return fixid;
    }

    public Contractfile setFixid(String fixid) {
        this.fixid = fixid;
        return this;
    }

    public String getCreateuser() {
        return createuser;
    }

    public Contractfile setCreateuser(String createuser) {
        this.createuser = createuser;
        return this;
    }

    public Date getCreatedate() {
        return createdate;
    }

    public Contractfile setCreatedate(Date createdate) {
        this.createdate = createdate;
        return this;
    }

    public String getUpdateuser() {
        return updateuser;
    }

    public Contractfile setUpdateuser(String updateuser) {
        this.updateuser = updateuser;
        return this;
    }

    public Date getUpdatedate() {
        return updatedate;
    }

    public Contractfile setUpdatedate(Date updatedate) {
        this.updatedate = updatedate;
        return this;
    }

    public String getFiletype() {
        return filetype;
    }

    public Contractfile setFiletype(String filetype) {
        this.filetype = filetype;
        return this;
    }

    @Override
    protected Serializable pkVal() {
        return null;
    }

    @Override
    public String toString() {
        return "Contractfile{" +
        "serno=" + serno +
        ", dataid=" + dataid +
        ", fliepath=" + fliepath +
        ", fliename=" + fliename +
        ", isdownload=" + isdownload +
        ", flienote=" + flienote +
        ", type=" + type +
        ", fixid=" + fixid +
        ", createuser=" + createuser +
        ", createdate=" + createdate +
        ", updateuser=" + updateuser +
        ", updatedate=" + updatedate +
        ", filetype=" + filetype +
        "}";
    }
}
