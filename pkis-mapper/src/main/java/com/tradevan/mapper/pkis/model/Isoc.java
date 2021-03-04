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
@TableName("ISOC")
public class Isoc extends Model<Isoc> {

    private static final long serialVersionUID=1L;

    @TableField("SHORTNAME")
    private String shortname;

    @TableField("ALPHA2")
    private String alpha2;

    @TableField("ALPHA3")
    private String alpha3;

    @TableField("NUMBERICCODE")
    private String numbericcode;


    public String getShortname() {
        return shortname;
    }

    public Isoc setShortname(String shortname) {
        this.shortname = shortname;
        return this;
    }

    public String getAlpha2() {
        return alpha2;
    }

    public Isoc setAlpha2(String alpha2) {
        this.alpha2 = alpha2;
        return this;
    }

    public String getAlpha3() {
        return alpha3;
    }

    public Isoc setAlpha3(String alpha3) {
        this.alpha3 = alpha3;
        return this;
    }

    public String getNumbericcode() {
        return numbericcode;
    }

    public Isoc setNumbericcode(String numbericcode) {
        this.numbericcode = numbericcode;
        return this;
    }

    @Override
    protected Serializable pkVal() {
        return null;
    }

    @Override
    public String toString() {
        return "Isoc{" +
        "shortname=" + shortname +
        ", alpha2=" + alpha2 +
        ", alpha3=" + alpha3 +
        ", numbericcode=" + numbericcode +
        "}";
    }
}
