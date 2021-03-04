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
@TableName("FUNCTIONLIST")
public class Functionlist extends Model<Functionlist> {

    private static final long serialVersionUID=1L;

    @TableField("FUNCTIONCODE")
    private String functioncode;

    @TableField("FUNCTIONNAME")
    private String functionname;

    @TableField("MENUGROUP")
    private String menugroup;

    @TableField("MENUCODE")
    private String menucode;

    @TableField("PARENTMENUCODE")
    private String parentmenucode;

    @TableField("URL")
    private String url;

    @TableField("TARGET")
    private String target;

    @TableField("XSAP")
    private String xsap;

    @TableField("XSURL")
    private String xsurl;


    public String getFunctioncode() {
        return functioncode;
    }

    public Functionlist setFunctioncode(String functioncode) {
        this.functioncode = functioncode;
        return this;
    }

    public String getFunctionname() {
        return functionname;
    }

    public Functionlist setFunctionname(String functionname) {
        this.functionname = functionname;
        return this;
    }

    public String getMenugroup() {
        return menugroup;
    }

    public Functionlist setMenugroup(String menugroup) {
        this.menugroup = menugroup;
        return this;
    }

    public String getMenucode() {
        return menucode;
    }

    public Functionlist setMenucode(String menucode) {
        this.menucode = menucode;
        return this;
    }

    public String getParentmenucode() {
        return parentmenucode;
    }

    public Functionlist setParentmenucode(String parentmenucode) {
        this.parentmenucode = parentmenucode;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public Functionlist setUrl(String url) {
        this.url = url;
        return this;
    }

    public String getTarget() {
        return target;
    }

    public Functionlist setTarget(String target) {
        this.target = target;
        return this;
    }

    public String getXsap() {
        return xsap;
    }

    public Functionlist setXsap(String xsap) {
        this.xsap = xsap;
        return this;
    }

    public String getXsurl() {
        return xsurl;
    }

    public Functionlist setXsurl(String xsurl) {
        this.xsurl = xsurl;
        return this;
    }

    @Override
    protected Serializable pkVal() {
        return null;
    }

    @Override
    public String toString() {
        return "Functionlist{" +
        "functioncode=" + functioncode +
        ", functionname=" + functionname +
        ", menugroup=" + menugroup +
        ", menucode=" + menucode +
        ", parentmenucode=" + parentmenucode +
        ", url=" + url +
        ", target=" + target +
        ", xsap=" + xsap +
        ", xsurl=" + xsurl +
        "}";
    }
}
