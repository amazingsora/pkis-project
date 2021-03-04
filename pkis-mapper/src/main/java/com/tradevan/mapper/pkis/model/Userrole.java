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
@TableName("USERROLE")
public class Userrole extends Model<Userrole> {

    private static final long serialVersionUID=1L;

    @TableId("USERID")
    private String userid;

    @TableField("ROLEID")
    private String roleid;


    public String getUserid() {
        return userid;
    }

    public Userrole setUserid(String userid) {
        this.userid = userid;
        return this;
    }

    public String getRoleid() {
        return roleid;
    }

    public Userrole setRoleid(String roleid) {
        this.roleid = roleid;
        return this;
    }

    @Override
    protected Serializable pkVal() {
        return this.userid;
    }

    @Override
    public String toString() {
        return "Userrole{" +
        "userid=" + userid +
        ", roleid=" + roleid +
        "}";
    }
}
