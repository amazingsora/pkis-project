package com.tradevan.mapper.pkis.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author system
 * @since 2020-10-15
 */
@TableName("SHORTAGEPENALTY")
public class Shortagepenalty extends Model<Shortagepenalty> {

    private static final long serialVersionUID=1L;

    @TableField("DEPTNO")
    private String deptno;

    @TableField("SUPPLIERCODE")
    private String suppliercode;

    @TableField("LASTYEAR")
    private Double lastyear;

    @TableField("LSCALE")
    private Double lscale;

    @TableField("USCALE")
    private Double uscale;

    @TableField("LEVELPENALTY")
    private String levelpenalty;

    @TableField("PENALTY")
    private Double penalty;

    @TableField("CMP")
    private String cmp;


    public String getDeptno() {
        return deptno;
    }

    public Shortagepenalty setDeptno(String deptno) {
        this.deptno = deptno;
        return this;
    }

    public String getSuppliercode() {
        return suppliercode;
    }

    public Shortagepenalty setSuppliercode(String suppliercode) {
        this.suppliercode = suppliercode;
        return this;
    }

    public Double getLastyear() {
        return lastyear;
    }

    public Shortagepenalty setLastyear(Double lastyear) {
        this.lastyear = lastyear;
        return this;
    }

    public Double getLscale() {
        return lscale;
    }

    public Shortagepenalty setLscale(Double lscale) {
        this.lscale = lscale;
        return this;
    }

    public Double getUscale() {
        return uscale;
    }

    public Shortagepenalty setUscale(Double uscale) {
        this.uscale = uscale;
        return this;
    }

    public String getLevelpenalty() {
        return levelpenalty;
    }

    public Shortagepenalty setLevelpenalty(String levelpenalty) {
        this.levelpenalty = levelpenalty;
        return this;
    }

    public Double getPenalty() {
        return penalty;
    }

    public Shortagepenalty setPenalty(Double penalty) {
        this.penalty = penalty;
        return this;
    }

    public String getCmp() {
        return cmp;
    }

    public Shortagepenalty setCmp(String cmp) {
        this.cmp = cmp;
        return this;
    }

    @Override
    protected Serializable pkVal() {
        return null;
    }

    @Override
    public String toString() {
        return "Shortagepenalty{" +
        "deptno=" + deptno +
        ", suppliercode=" + suppliercode +
        ", lastyear=" + lastyear +
        ", lscale=" + lscale +
        ", uscale=" + uscale +
        ", levelpenalty=" + levelpenalty +
        ", penalty=" + penalty +
        ", cmp=" + cmp +
        "}";
    }
}
