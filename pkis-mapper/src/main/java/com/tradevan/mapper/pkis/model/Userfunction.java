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
@TableName("USERFUNCTION")
public class Userfunction extends Model<Userfunction> {

    private static final long serialVersionUID=1L;

    @TableField("USERS")
    private String users;

    @TableField("FUNCTIONCODE")
    private String functioncode;


    public String getUsers() {
        return users;
    }

    public Userfunction setUsers(String users) {
        this.users = users;
        return this;
    }

    public String getFunctioncode() {
        return functioncode;
    }

    public Userfunction setFunctioncode(String functioncode) {
        this.functioncode = functioncode;
        return this;
    }

    @Override
    protected Serializable pkVal() {
        return null;
    }

    @Override
    public String toString() {
        return "Userfunction{" +
        "users=" + users +
        ", functioncode=" + functioncode +
        "}";
    }
}
