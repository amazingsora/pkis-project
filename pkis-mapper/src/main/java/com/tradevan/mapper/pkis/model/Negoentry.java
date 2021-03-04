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
 * @since 2020-07-31
 */
@TableName("NEGOENTRY")
public class Negoentry extends Model<Negoentry> {

    private static final long serialVersionUID=1L;

    /**
     * 年(YYYY)
     */
    @TableField("YEARS")
    private String years;

    /**
     * 課別
     */
    @TableField("DEPTNO")
    private String deptno;

    /**
     * 供應商廠編
     */
    @TableField("SUPPLIERCODE")
    private String suppliercode;

    @TableField("FLOW")
    private String flow;

    @TableField("HYPCOST")
    private Double hypcost;

    @TableField("SUPCOST")
    private Double supcost;

    @TableField("KMCOST")
    private Double kmcost;

    @TableField("NATCOST")
    private Double natcost;

    @TableField("HYPMARGIN")
    private Double hypmargin;

    @TableField("SUPMARGIN")
    private Double supmargin;

    @TableField("KMMARGIN")
    private Double kmmargin;

    @TableField("NATMARGIN")
    private Double natmargin;

    @TableField("HYPMARGINACTUAL")
    private Double hypmarginactual;

    @TableField("SUPMARGINACTUAL")
    private Double supmarginactual;

    @TableField("KMMARGINACTUAL")
    private Double kmmarginactual;

    @TableField("NATMARGINACTUAL")
    private Double natmarginactual;

    @TableField("EXTRA")
    private Double extra;

    @TableField("EXTRAPC")
    private Double extrapc;

    @TableField("EXTRAACTUAL")
    private Double extraactual;

    /**
     * 0:Nonfood 1:food
     */
    @TableField("TYPE")
    private String type;

    @TableField("CREATEUSER")
    private String createuser;

    @TableField("CREATEDATE")
    private Date createdate;

    @TableField("UPDATEUSER")
    private String updateuser;

    @TableField("UPDATEDATE")
    private Date updatedate;


    public String getYears() {
        return years;
    }

    public Negoentry setYears(String years) {
        this.years = years;
        return this;
    }

    public String getDeptno() {
        return deptno;
    }

    public Negoentry setDeptno(String deptno) {
        this.deptno = deptno;
        return this;
    }

    public String getSuppliercode() {
        return suppliercode;
    }

    public Negoentry setSuppliercode(String suppliercode) {
        this.suppliercode = suppliercode;
        return this;
    }

    public String getFlow() {
        return flow;
    }

    public Negoentry setFlow(String flow) {
        this.flow = flow;
        return this;
    }

    public Double getHypcost() {
        return hypcost;
    }

    public Negoentry setHypcost(Double hypcost) {
        this.hypcost = hypcost;
        return this;
    }

    public Double getSupcost() {
        return supcost;
    }

    public Negoentry setSupcost(Double supcost) {
        this.supcost = supcost;
        return this;
    }

    public Double getKmcost() {
        return kmcost;
    }

    public Negoentry setKmcost(Double kmcost) {
        this.kmcost = kmcost;
        return this;
    }

    public Double getNatcost() {
        return natcost;
    }

    public Negoentry setNatcost(Double natcost) {
        this.natcost = natcost;
        return this;
    }

    public Double getHypmargin() {
        return hypmargin;
    }

    public Negoentry setHypmargin(Double hypmargin) {
        this.hypmargin = hypmargin;
        return this;
    }

    public Double getSupmargin() {
        return supmargin;
    }

    public Negoentry setSupmargin(Double supmargin) {
        this.supmargin = supmargin;
        return this;
    }

    public Double getKmmargin() {
        return kmmargin;
    }

    public Negoentry setKmmargin(Double kmmargin) {
        this.kmmargin = kmmargin;
        return this;
    }

    public Double getNatmargin() {
        return natmargin;
    }

    public Negoentry setNatmargin(Double natmargin) {
        this.natmargin = natmargin;
        return this;
    }

    public Double getHypmarginactual() {
        return hypmarginactual;
    }

    public Negoentry setHypmarginactual(Double hypmarginactual) {
        this.hypmarginactual = hypmarginactual;
        return this;
    }

    public Double getSupmarginactual() {
        return supmarginactual;
    }

    public Negoentry setSupmarginactual(Double supmarginactual) {
        this.supmarginactual = supmarginactual;
        return this;
    }

    public Double getKmmarginactual() {
        return kmmarginactual;
    }

    public Negoentry setKmmarginactual(Double kmmarginactual) {
        this.kmmarginactual = kmmarginactual;
        return this;
    }

    public Double getNatmarginactual() {
        return natmarginactual;
    }

    public Negoentry setNatmarginactual(Double natmarginactual) {
        this.natmarginactual = natmarginactual;
        return this;
    }

    public Double getExtra() {
        return extra;
    }

    public Negoentry setExtra(Double extra) {
        this.extra = extra;
        return this;
    }

    public Double getExtrapc() {
        return extrapc;
    }

    public Negoentry setExtrapc(Double extrapc) {
        this.extrapc = extrapc;
        return this;
    }

    public Double getExtraactual() {
        return extraactual;
    }

    public Negoentry setExtraactual(Double extraactual) {
        this.extraactual = extraactual;
        return this;
    }

    public String getType() {
        return type;
    }

    public Negoentry setType(String type) {
        this.type = type;
        return this;
    }

    public String getCreateuser() {
        return createuser;
    }

    public Negoentry setCreateuser(String createuser) {
        this.createuser = createuser;
        return this;
    }

    public Date getCreatedate() {
        return createdate;
    }

    public Negoentry setCreatedate(Date createdate) {
        this.createdate = createdate;
        return this;
    }

    public String getUpdateuser() {
        return updateuser;
    }

    public Negoentry setUpdateuser(String updateuser) {
        this.updateuser = updateuser;
        return this;
    }

    public Date getUpdatedate() {
        return updatedate;
    }

    public Negoentry setUpdatedate(Date updatedate) {
        this.updatedate = updatedate;
        return this;
    }

    @Override
    protected Serializable pkVal() {
        return null;
    }

    @Override
    public String toString() {
        return "Negoentry{" +
        "years=" + years +
        ", deptno=" + deptno +
        ", suppliercode=" + suppliercode +
        ", flow=" + flow +
        ", hypcost=" + hypcost +
        ", supcost=" + supcost +
        ", kmcost=" + kmcost +
        ", natcost=" + natcost +
        ", hypmargin=" + hypmargin +
        ", supmargin=" + supmargin +
        ", kmmargin=" + kmmargin +
        ", natmargin=" + natmargin +
        ", hypmarginactual=" + hypmarginactual +
        ", supmarginactual=" + supmarginactual +
        ", kmmarginactual=" + kmmarginactual +
        ", natmarginactual=" + natmarginactual +
        ", extra=" + extra +
        ", extrapc=" + extrapc +
        ", extraactual=" + extraactual +
        ", type=" + type +
        ", createuser=" + createuser +
        ", createdate=" + createdate +
        ", updateuser=" + updateuser +
        ", updatedate=" + updatedate +
        "}";
    }
}
