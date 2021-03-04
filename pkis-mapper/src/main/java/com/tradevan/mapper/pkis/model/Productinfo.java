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
@TableName("PRODUCTINFO")
public class Productinfo extends Model<Productinfo> {

    private static final long serialVersionUID=1L;

    @TableField("SUPPLIERGLN")
    private String suppliergln;

    @TableField("BARCODE")
    private String barcode;

    @TableField("PRODUCTCODE")
    private String productcode;

    @TableField("PRODUCTNAME")
    private String productname;

    /**
     * 異動人員
     */
    @TableField("UPDUSER")
    private String upduser;

    /**
     * 異動時間
     */
    @TableField("UPDDATE")
    private String upddate;


    public String getSuppliergln() {
        return suppliergln;
    }

    public Productinfo setSuppliergln(String suppliergln) {
        this.suppliergln = suppliergln;
        return this;
    }

    public String getBarcode() {
        return barcode;
    }

    public Productinfo setBarcode(String barcode) {
        this.barcode = barcode;
        return this;
    }

    public String getProductcode() {
        return productcode;
    }

    public Productinfo setProductcode(String productcode) {
        this.productcode = productcode;
        return this;
    }

    public String getProductname() {
        return productname;
    }

    public Productinfo setProductname(String productname) {
        this.productname = productname;
        return this;
    }

    public String getUpduser() {
        return upduser;
    }

    public Productinfo setUpduser(String upduser) {
        this.upduser = upduser;
        return this;
    }

    public String getUpddate() {
        return upddate;
    }

    public Productinfo setUpddate(String upddate) {
        this.upddate = upddate;
        return this;
    }

    @Override
    protected Serializable pkVal() {
        return null;
    }

    @Override
    public String toString() {
        return "Productinfo{" +
        "suppliergln=" + suppliergln +
        ", barcode=" + barcode +
        ", productcode=" + productcode +
        ", productname=" + productname +
        ", upduser=" + upduser +
        ", upddate=" + upddate +
        "}";
    }
}
