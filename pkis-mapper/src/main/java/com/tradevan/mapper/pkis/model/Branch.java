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
@TableName("BRANCH")
public class Branch extends Model<Branch> {

    private static final long serialVersionUID=1L;

    @TableField("POSTGLN")
    private String postgln;

    @TableId("POSTCODE")
    private String postcode;

    @TableField("POSTBRIEFNAME")
    private String postbriefname;

    @TableField("POSTNAME")
    private String postname;

    @TableField("STORETYPE")
    private String storetype;

    @TableField("OPENDATE")
    private String opendate;

    @TableField("CLOSEDATE")
    private String closedate;

    @TableField("AREACODE")
    private String areacode;


    public String getPostgln() {
        return postgln;
    }

    public Branch setPostgln(String postgln) {
        this.postgln = postgln;
        return this;
    }

    public String getPostcode() {
        return postcode;
    }

    public Branch setPostcode(String postcode) {
        this.postcode = postcode;
        return this;
    }

    public String getPostbriefname() {
        return postbriefname;
    }

    public Branch setPostbriefname(String postbriefname) {
        this.postbriefname = postbriefname;
        return this;
    }

    public String getPostname() {
        return postname;
    }

    public Branch setPostname(String postname) {
        this.postname = postname;
        return this;
    }

    public String getStoretype() {
        return storetype;
    }

    public Branch setStoretype(String storetype) {
        this.storetype = storetype;
        return this;
    }

    public String getOpendate() {
        return opendate;
    }

    public Branch setOpendate(String opendate) {
        this.opendate = opendate;
        return this;
    }

    public String getClosedate() {
        return closedate;
    }

    public Branch setClosedate(String closedate) {
        this.closedate = closedate;
        return this;
    }

    public String getAreacode() {
        return areacode;
    }

    public Branch setAreacode(String areacode) {
        this.areacode = areacode;
        return this;
    }

    @Override
    protected Serializable pkVal() {
        return this.postcode;
    }

    @Override
    public String toString() {
        return "Branch{" +
        "postgln=" + postgln +
        ", postcode=" + postcode +
        ", postbriefname=" + postbriefname +
        ", postname=" + postname +
        ", storetype=" + storetype +
        ", opendate=" + opendate +
        ", closedate=" + closedate +
        ", areacode=" + areacode +
        "}";
    }
}
