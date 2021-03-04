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
 * @since 2021-01-26
 */
@TableName("SSOTOKEN")
public class Ssotoken extends Model<Ssotoken> {

    private static final long serialVersionUID=1L;

    @TableField("SERNO")
    private Long serno;

    /**
     * REQUEST JSON
     */
    @TableField("SPIDJSON")
    private String spidjson;

    /**
     * TOKEN
     */
    @TableField("TOKEN")
    private String token;

    /**
     * REQUEST供應商帳號筆數
     */
    @TableField("SPIDCOUNT")
    private Long spidcount;

    /**
     * LGC帳號
     */
    @TableField("LGCUSERID")
    private String lgcuserid;

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

    public Ssotoken setSerno(Long serno) {
        this.serno = serno;
        return this;
    }

    public String getSpidjson() {
        return spidjson;
    }

    public Ssotoken setSpidjson(String spidjson) {
        this.spidjson = spidjson;
        return this;
    }

    public String getToken() {
        return token;
    }

    public Ssotoken setToken(String token) {
        this.token = token;
        return this;
    }

    public Long getSpidcount() {
        return spidcount;
    }

    public Ssotoken setSpidcount(Long spidcount) {
        this.spidcount = spidcount;
        return this;
    }

    public String getLgcuserid() {
        return lgcuserid;
    }

    public Ssotoken setLgcuserid(String lgcuserid) {
        this.lgcuserid = lgcuserid;
        return this;
    }

    public String getCreateuser() {
        return createuser;
    }

    public Ssotoken setCreateuser(String createuser) {
        this.createuser = createuser;
        return this;
    }

    public Date getCreatedate() {
        return createdate;
    }

    public Ssotoken setCreatedate(Date createdate) {
        this.createdate = createdate;
        return this;
    }

    public String getUpdateuser() {
        return updateuser;
    }

    public Ssotoken setUpdateuser(String updateuser) {
        this.updateuser = updateuser;
        return this;
    }

    public Date getUpdatedate() {
        return updatedate;
    }

    public Ssotoken setUpdatedate(Date updatedate) {
        this.updatedate = updatedate;
        return this;
    }

    @Override
    protected Serializable pkVal() {
        return null;
    }

    @Override
    public String toString() {
        return "Ssotoken{" +
        "serno=" + serno +
        ", spidjson=" + spidjson +
        ", token=" + token +
        ", spidcount=" + spidcount +
        ", lgcuserid=" + lgcuserid +
        ", createuser=" + createuser +
        ", createdate=" + createdate +
        ", updateuser=" + updateuser +
        ", updatedate=" + updatedate +
        "}";
    }
}
