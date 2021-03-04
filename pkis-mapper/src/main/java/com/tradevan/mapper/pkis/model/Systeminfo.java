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
@TableName("SYSTEMINFO")
public class Systeminfo extends Model<Systeminfo> {

    private static final long serialVersionUID=1L;

    @TableField("CODE")
    private String code;

    @TableField("DATA1")
    private String data1;

    @TableField("DATA2")
    private String data2;

    @TableField("DATA3")
    private String data3;


    public String getCode() {
        return code;
    }

    public Systeminfo setCode(String code) {
        this.code = code;
        return this;
    }

    public String getData1() {
        return data1;
    }

    public Systeminfo setData1(String data1) {
        this.data1 = data1;
        return this;
    }

    public String getData2() {
        return data2;
    }

    public Systeminfo setData2(String data2) {
        this.data2 = data2;
        return this;
    }

    public String getData3() {
        return data3;
    }

    public Systeminfo setData3(String data3) {
        this.data3 = data3;
        return this;
    }

    @Override
    protected Serializable pkVal() {
        return null;
    }

    @Override
    public String toString() {
        return "Systeminfo{" +
        "code=" + code +
        ", data1=" + data1 +
        ", data2=" + data2 +
        ", data3=" + data3 +
        "}";
    }
}
