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
@TableName("BULLETINREF")
public class Bulletinref extends Model<Bulletinref> {

    private static final long serialVersionUID=1L;

    /**
     * 公告序號
     */
    @TableField("ID")
    private Integer id;

    /**
     * 接收者
     */
    @TableField("RECEIVER")
    private String receiver;


    public Integer getId() {
        return id;
    }

    public Bulletinref setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getReceiver() {
        return receiver;
    }

    public Bulletinref setReceiver(String receiver) {
        this.receiver = receiver;
        return this;
    }

    @Override
    protected Serializable pkVal() {
        return null;
    }

    @Override
    public String toString() {
        return "Bulletinref{" +
        "id=" + id +
        ", receiver=" + receiver +
        "}";
    }
}
