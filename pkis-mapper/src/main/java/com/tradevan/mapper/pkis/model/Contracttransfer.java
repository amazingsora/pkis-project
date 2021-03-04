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
 * @since 2020-12-07
 */
@TableName("CONTRACTTRANSFER")
public class Contracttransfer extends Model<Contracttransfer> {

    private static final long serialVersionUID=1L;

    @TableField("SERNO")
    private Long serno;

    @TableField("DATAID")
    private String dataid;

    @TableField("DEPTID")
    private String deptid;

    @TableField("ROLEID")
    private String roleid;

    @TableField("USERID")
    private String userid;

    @TableField("UNDERTAKEDEPTID")
    private String undertakedeptid;

    @TableField("UNDERTAKEROLEID")
    private String undertakeroleid;

    @TableField("UNDERTAKEUSERID")
    private String undertakeuserid;

    @TableField("CRATEUSER")
    private String crateuser;

    @TableField("CRATEDATE")
    private Date cratedate;

    @TableField("UPDATEUSER")
    private String updateuser;

    @TableField("UPDATEDATE")
    private Date updatedate;

    @TableField("DISPORD")
    private String dispord;


    public Long getSerno() {
        return serno;
    }

    public Contracttransfer setSerno(Long serno) {
        this.serno = serno;
        return this;
    }

    public String getDataid() {
        return dataid;
    }

    public Contracttransfer setDataid(String dataid) {
        this.dataid = dataid;
        return this;
    }

    public String getDeptid() {
        return deptid;
    }

    public Contracttransfer setDeptid(String deptid) {
        this.deptid = deptid;
        return this;
    }

    public String getRoleid() {
        return roleid;
    }

    public Contracttransfer setRoleid(String roleid) {
        this.roleid = roleid;
        return this;
    }

    public String getUserid() {
        return userid;
    }

    public Contracttransfer setUserid(String userid) {
        this.userid = userid;
        return this;
    }

    public String getUndertakedeptid() {
        return undertakedeptid;
    }

    public Contracttransfer setUndertakedeptid(String undertakedeptid) {
        this.undertakedeptid = undertakedeptid;
        return this;
    }

    public String getUndertakeroleid() {
        return undertakeroleid;
    }

    public Contracttransfer setUndertakeroleid(String undertakeroleid) {
        this.undertakeroleid = undertakeroleid;
        return this;
    }

    public String getUndertakeuserid() {
        return undertakeuserid;
    }

    public Contracttransfer setUndertakeuserid(String undertakeuserid) {
        this.undertakeuserid = undertakeuserid;
        return this;
    }

    public String getCrateuser() {
        return crateuser;
    }

    public Contracttransfer setCrateuser(String crateuser) {
        this.crateuser = crateuser;
        return this;
    }

    public Date getCratedate() {
        return cratedate;
    }

    public Contracttransfer setCratedate(Date cratedate) {
        this.cratedate = cratedate;
        return this;
    }

    public String getUpdateuser() {
        return updateuser;
    }

    public Contracttransfer setUpdateuser(String updateuser) {
        this.updateuser = updateuser;
        return this;
    }

    public Date getUpdatedate() {
        return updatedate;
    }

    public Contracttransfer setUpdatedate(Date updatedate) {
        this.updatedate = updatedate;
        return this;
    }

    public String getDispord() {
        return dispord;
    }

    public Contracttransfer setDispord(String dispord) {
        this.dispord = dispord;
        return this;
    }

    @Override
    protected Serializable pkVal() {
        return null;
    }

    @Override
    public String toString() {
        return "Contracttransfer{" +
        "serno=" + serno +
        ", dataid=" + dataid +
        ", deptid=" + deptid +
        ", roleid=" + roleid +
        ", userid=" + userid +
        ", undertakedeptid=" + undertakedeptid +
        ", undertakeroleid=" + undertakeroleid +
        ", undertakeuserid=" + undertakeuserid +
        ", crateuser=" + crateuser +
        ", cratedate=" + cratedate +
        ", updateuser=" + updateuser +
        ", updatedate=" + updatedate +
        ", dispord=" + dispord +
        "}";
    }
}
