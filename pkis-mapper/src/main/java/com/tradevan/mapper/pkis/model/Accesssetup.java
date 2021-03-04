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
@TableName("ACCESSSETUP")
public class Accesssetup extends Model<Accesssetup> {

    private static final long serialVersionUID=1L;

    @TableField("COLUMN1")
    private String column1;

    @TableField("COLUMN2")
    private String column2;

    @TableField("COLUMN3")
    private String column3;

    @TableField("COLUMN4")
    private String column4;

    @TableField("COLUMN5")
    private String column5;


    public String getColumn1() {
        return column1;
    }

    public Accesssetup setColumn1(String column1) {
        this.column1 = column1;
        return this;
    }

    public String getColumn2() {
        return column2;
    }

    public Accesssetup setColumn2(String column2) {
        this.column2 = column2;
        return this;
    }

    public String getColumn3() {
        return column3;
    }

    public Accesssetup setColumn3(String column3) {
        this.column3 = column3;
        return this;
    }

    public String getColumn4() {
        return column4;
    }

    public Accesssetup setColumn4(String column4) {
        this.column4 = column4;
        return this;
    }

    public String getColumn5() {
        return column5;
    }

    public Accesssetup setColumn5(String column5) {
        this.column5 = column5;
        return this;
    }

    @Override
    protected Serializable pkVal() {
        return null;
    }

    @Override
    public String toString() {
        return "Accesssetup{" +
        "column1=" + column1 +
        ", column2=" + column2 +
        ", column3=" + column3 +
        ", column4=" + column4 +
        ", column5=" + column5 +
        "}";
    }
}
