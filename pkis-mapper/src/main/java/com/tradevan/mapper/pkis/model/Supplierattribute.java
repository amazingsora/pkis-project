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
 * @author penny
 * @since 2020-03-04
 */
@TableName("SUPPLIERATTRIBUTE")
public class Supplierattribute extends Model<Supplierattribute> {

    private static final long serialVersionUID=1L;

    @TableField("SUPPLIERCODE")
    private String suppliercode;

    @TableField("DATAPERIODFLAG")
    private String dataperiodflag;

    @TableField("CONTRACTSTARTDATE")
    private String contractstartdate;

    @TableField("CONTRACTENDDATE")
    private String contractenddate;


    public String getSuppliercode() {
        return suppliercode;
    }

    public Supplierattribute setSuppliercode(String suppliercode) {
        this.suppliercode = suppliercode;
        return this;
    }

    public String getDataperiodflag() {
        return dataperiodflag;
    }

    public Supplierattribute setDataperiodflag(String dataperiodflag) {
        this.dataperiodflag = dataperiodflag;
        return this;
    }

    public String getContractstartdate() {
        return contractstartdate;
    }

    public Supplierattribute setContractstartdate(String contractstartdate) {
        this.contractstartdate = contractstartdate;
        return this;
    }

    public String getContractenddate() {
        return contractenddate;
    }

    public Supplierattribute setContractenddate(String contractenddate) {
        this.contractenddate = contractenddate;
        return this;
    }

    @Override
    protected Serializable pkVal() {
        return null;
    }

    @Override
    public String toString() {
        return "Supplierattribute{" +
        "suppliercode=" + suppliercode +
        ", dataperiodflag=" + dataperiodflag +
        ", contractstartdate=" + contractstartdate +
        ", contractenddate=" + contractenddate +
        "}";
    }
}
