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
@TableName("BULLETINDATA")
public class Bulletindata extends Model<Bulletindata> {

    private static final long serialVersionUID=1L;

    /**
     * 公告序號
     */
    @TableId("ID")
    private Integer id;

    /**
     * 公告開始時間
     */
    @TableField("STARTDATE")
    private String startdate;

    /**
     * 公告結束時間
     */
    @TableField("ENDDATE")
    private String enddate;

    /**
     * 公告主旨
     */
    @TableField("TOPIC")
    private String topic;

    /**
     * 公告內容
     */
    @TableField("CONTENT")
    private String content;

    /**
     * 異動人員
     */
    @TableField("UPDATEUSER")
    private String updateuser;

    /**
     * 異動日期
     */
    @TableField("UPDATEDATE")
    private String updatedate;

    /**
     * 建立人員
     */
    @TableField("CREATEUSER")
    private String createuser;

    /**
     * 建立日期
     */
    @TableField("CREATEDATE")
    private String createdate;


    public Integer getId() {
        return id;
    }

    public Bulletindata setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getStartdate() {
        return startdate;
    }

    public Bulletindata setStartdate(String startdate) {
        this.startdate = startdate;
        return this;
    }

    public String getEnddate() {
        return enddate;
    }

    public Bulletindata setEnddate(String enddate) {
        this.enddate = enddate;
        return this;
    }

    public String getTopic() {
        return topic;
    }

    public Bulletindata setTopic(String topic) {
        this.topic = topic;
        return this;
    }

    public String getContent() {
        return content;
    }

    public Bulletindata setContent(String content) {
        this.content = content;
        return this;
    }

    public String getUpdateuser() {
        return updateuser;
    }

    public Bulletindata setUpdateuser(String updateuser) {
        this.updateuser = updateuser;
        return this;
    }

    public String getUpdatedate() {
        return updatedate;
    }

    public Bulletindata setUpdatedate(String updatedate) {
        this.updatedate = updatedate;
        return this;
    }

    public String getCreateuser() {
        return createuser;
    }

    public Bulletindata setCreateuser(String createuser) {
        this.createuser = createuser;
        return this;
    }

    public String getCreatedate() {
        return createdate;
    }

    public Bulletindata setCreatedate(String createdate) {
        this.createdate = createdate;
        return this;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "Bulletindata{" +
        "id=" + id +
        ", startdate=" + startdate +
        ", enddate=" + enddate +
        ", topic=" + topic +
        ", content=" + content +
        ", updateuser=" + updateuser +
        ", updatedate=" + updatedate +
        ", createuser=" + createuser +
        ", createdate=" + createdate +
        "}";
    }
}
