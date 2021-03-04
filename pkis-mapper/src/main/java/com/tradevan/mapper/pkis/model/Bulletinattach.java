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
@TableName("BULLETINATTACH")
public class Bulletinattach extends Model<Bulletinattach> {

    private static final long serialVersionUID=1L;

    /**
     * 公告序號
     */
    @TableId("ID")
    private Integer id;

    /**
     * 排序
     */
    @TableField("SEQ")
    private Integer seq;

    /**
     * 檔案路徑名稱
     */
    @TableField("FILENAME")
    private String filename;


    public Integer getId() {
        return id;
    }

    public Bulletinattach setId(Integer id) {
        this.id = id;
        return this;
    }

    public Integer getSeq() {
        return seq;
    }

    public Bulletinattach setSeq(Integer seq) {
        this.seq = seq;
        return this;
    }

    public String getFilename() {
        return filename;
    }

    public Bulletinattach setFilename(String filename) {
        this.filename = filename;
        return this;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "Bulletinattach{" +
        "id=" + id +
        ", seq=" + seq +
        ", filename=" + filename +
        "}";
    }
}
