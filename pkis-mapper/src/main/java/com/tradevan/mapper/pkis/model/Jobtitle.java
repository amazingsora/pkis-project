package com.tradevan.mapper.pkis.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.util.Date;
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
@TableName("JOBTITLE")
public class Jobtitle extends Model<Jobtitle> {

    private static final long serialVersionUID=1L;

    @TableId("SERNO")
    private Long serno;

    @TableField("JOBTITLEID")
    private String jobtitleid;

    @TableField("JOBTITLENAME")
    private String jobtitlename;

    @TableField("ISSUPERVISOR")
    private String issupervisor;

    @TableField("STATUS")
    private String status;

    @TableField("CREATEUSERID")
    private String createuserid;

    @TableField("UPDATEUSERID")
    private String updateuserid;

    @TableField("CREATEAGENTID")
    private String createagentid;

    @TableField("UPDATEAGENTID")
    private String updateagentid;

    @TableField("CREATETIME")
    private Date createtime;

    @TableField("UPDATETIME")
    private Date updatetime;

    @TableField("SYSMEMO")
    private String sysmemo;


    public Long getSerno() {
        return serno;
    }

    public Jobtitle setSerno(Long serno) {
        this.serno = serno;
        return this;
    }

    public String getJobtitleid() {
        return jobtitleid;
    }

    public Jobtitle setJobtitleid(String jobtitleid) {
        this.jobtitleid = jobtitleid;
        return this;
    }

    public String getJobtitlename() {
        return jobtitlename;
    }

    public Jobtitle setJobtitlename(String jobtitlename) {
        this.jobtitlename = jobtitlename;
        return this;
    }

    public String getIssupervisor() {
        return issupervisor;
    }

    public Jobtitle setIssupervisor(String issupervisor) {
        this.issupervisor = issupervisor;
        return this;
    }

    public String getStatus() {
        return status;
    }

    public Jobtitle setStatus(String status) {
        this.status = status;
        return this;
    }

    public String getCreateuserid() {
        return createuserid;
    }

    public Jobtitle setCreateuserid(String createuserid) {
        this.createuserid = createuserid;
        return this;
    }

    public String getUpdateuserid() {
        return updateuserid;
    }

    public Jobtitle setUpdateuserid(String updateuserid) {
        this.updateuserid = updateuserid;
        return this;
    }

    public String getCreateagentid() {
        return createagentid;
    }

    public Jobtitle setCreateagentid(String createagentid) {
        this.createagentid = createagentid;
        return this;
    }

    public String getUpdateagentid() {
        return updateagentid;
    }

    public Jobtitle setUpdateagentid(String updateagentid) {
        this.updateagentid = updateagentid;
        return this;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public Jobtitle setCreatetime(Date createtime) {
        this.createtime = createtime;
        return this;
    }

    public Date getUpdatetime() {
        return updatetime;
    }

    public Jobtitle setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
        return this;
    }

    public String getSysmemo() {
        return sysmemo;
    }

    public Jobtitle setSysmemo(String sysmemo) {
        this.sysmemo = sysmemo;
        return this;
    }

    @Override
    protected Serializable pkVal() {
        return this.serno;
    }

    @Override
    public String toString() {
        return "Jobtitle{" +
        "serno=" + serno +
        ", jobtitleid=" + jobtitleid +
        ", jobtitlename=" + jobtitlename +
        ", issupervisor=" + issupervisor +
        ", status=" + status +
        ", createuserid=" + createuserid +
        ", updateuserid=" + updateuserid +
        ", createagentid=" + createagentid +
        ", updateagentid=" + updateagentid +
        ", createtime=" + createtime +
        ", updatetime=" + updatetime +
        ", sysmemo=" + sysmemo +
        "}";
    }
}
