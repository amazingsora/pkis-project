package com.tradevan.mapper.pkis.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
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
@TableName("AREA")
public class Area extends Model<Area> {

    private static final long serialVersionUID=1L;

    @TableId("AREACODE")
    private String areacode;

    @TableField("AREANAME")
    private String areaname;

    @TableField("CREATEUSER")
    private String createuser;

    @TableField("CREATETIME")
    private String createtime;


    public String getAreacode() {
        return areacode;
    }

    public Area setAreacode(String areacode) {
        this.areacode = areacode;
        return this;
    }

    public String getAreaname() {
        return areaname;
    }

    public Area setAreaname(String areaname) {
        this.areaname = areaname;
        return this;
    }

    public String getCreateuser() {
        return createuser;
    }

    public Area setCreateuser(String createuser) {
        this.createuser = createuser;
        return this;
    }

    public String getCreatetime() {
        return createtime;
    }

    public Area setCreatetime(String createtime) {
        this.createtime = createtime;
        return this;
    }

    @Override
    protected Serializable pkVal() {
        return this.areacode;
    }

    @Override
    public String toString() {
        return "Area{" +
        "areacode=" + areacode +
        ", areaname=" + areaname +
        ", createuser=" + createuser +
        ", createtime=" + createtime +
        "}";
    }
}
