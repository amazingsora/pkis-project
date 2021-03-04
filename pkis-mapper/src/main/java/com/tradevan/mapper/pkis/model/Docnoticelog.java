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
 * @author penny
 * @since 2020-03-04
 */
@TableName("DOCNOTICELOG")
public class Docnoticelog extends Model<Docnoticelog> {

    private static final long serialVersionUID=1L;

    @TableId("SERNO")
    private Long serno;

    @TableField("USERID")
    private String userid;

    @TableField("SYSID")
    private String sysid;

    @TableField("APPLYNO")
    private String applyno;

    @TableField("STATUS")
    private String status;

    @TableField("CREATETIME")
    private Date createtime;

    @TableField("UPDATETIME")
    private Date updatetime;

    @TableField("SYSMEMO")
    private String sysmemo;

    @TableField("SERIALNO")
    private Integer serialno;


    public Long getSerno() {
        return serno;
    }

    public Docnoticelog setSerno(Long serno) {
        this.serno = serno;
        return this;
    }

    public String getUserid() {
        return userid;
    }

    public Docnoticelog setUserid(String userid) {
        this.userid = userid;
        return this;
    }

    public String getSysid() {
        return sysid;
    }

    public Docnoticelog setSysid(String sysid) {
        this.sysid = sysid;
        return this;
    }

    public String getApplyno() {
        return applyno;
    }

    public Docnoticelog setApplyno(String applyno) {
        this.applyno = applyno;
        return this;
    }

    public String getStatus() {
        return status;
    }

    public Docnoticelog setStatus(String status) {
        this.status = status;
        return this;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public Docnoticelog setCreatetime(Date createtime) {
        this.createtime = createtime;
        return this;
    }

    public Date getUpdatetime() {
        return updatetime;
    }

    public Docnoticelog setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
        return this;
    }

    public String getSysmemo() {
        return sysmemo;
    }

    public Docnoticelog setSysmemo(String sysmemo) {
        this.sysmemo = sysmemo;
        return this;
    }

    public Integer getSerialno() {
        return serialno;
    }

    public Docnoticelog setSerialno(Integer serialno) {
        this.serialno = serialno;
        return this;
    }

    @Override
    protected Serializable pkVal() {
        return this.serno;
    }

    @Override
    public String toString() {
        return "Docnoticelog{" +
        "serno=" + serno +
        ", userid=" + userid +
        ", sysid=" + sysid +
        ", applyno=" + applyno +
        ", status=" + status +
        ", createtime=" + createtime +
        ", updatetime=" + updatetime +
        ", sysmemo=" + sysmemo +
        ", serialno=" + serialno +
        "}";
    }
}
