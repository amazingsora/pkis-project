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
 * @since 2020-07-22
 */
@TableName("CODELIST")
public class Codelist extends Model<Codelist> {

    private static final long serialVersionUID=1L;

    @TableField("SYS")
    private String sys;

    @TableField("CLASSTYPE")
    private String classtype;

    @TableField("YEARS")
    private String years;

    @TableField("ANUMBER")
    private Integer anumber;

    @TableField("NOTE")
    private String note;

    @TableField("DEPTNO")
    private String deptno;

    @TableField("SUPPLIERCODE")
    private String suppliercode;


    public String getSys() {
        return sys;
    }

    public Codelist setSys(String sys) {
        this.sys = sys;
        return this;
    }

    public String getClasstype() {
        return classtype;
    }

    public Codelist setClasstype(String classtype) {
        this.classtype = classtype;
        return this;
    }

    public String getYears() {
        return years;
    }

    public Codelist setYears(String years) {
        this.years = years;
        return this;
    }

    public Integer getAnumber() {
        return anumber;
    }

    public Codelist setAnumber(Integer anumber) {
        this.anumber = anumber;
        return this;
    }

    public String getNote() {
        return note;
    }

    public Codelist setNote(String note) {
        this.note = note;
        return this;
    }

    public String getDeptno() {
        return deptno;
    }

    public Codelist setDeptno(String deptno) {
        this.deptno = deptno;
        return this;
    }

    public String getSuppliercode() {
        return suppliercode;
    }

    public Codelist setSuppliercode(String suppliercode) {
        this.suppliercode = suppliercode;
        return this;
    }

    @Override
    protected Serializable pkVal() {
        return null;
    }

    @Override
    public String toString() {
        return "Codelist{" +
        "sys=" + sys +
        ", classtype=" + classtype +
        ", years=" + years +
        ", anumber=" + anumber +
        ", note=" + note +
        ", deptno=" + deptno +
        ", suppliercode=" + suppliercode +
        "}";
    }
}
