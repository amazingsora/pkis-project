package com.tradevan.mapper.pkis.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

/**
 * <p>
 * 部門資料擴充檔
 * </p>
 *
 * @author system
 * @since 2020-07-30
 */
@TableName("DEPT_INFO_EXT")
public class DeptInfoExt extends Model<DeptInfoExt> {

    private static final long serialVersionUID=1L;

    /**
     * 系統代號
     */
    @TableId("APP_ID")
    private String appId;

    /**
     * 識別碼
     */
    @TableField("IDEN_ID")
    private String idenId;

    /**
     * 身份別 00:系統 01:通路 02:供應商
     */
    @TableField("IDEN_TYPE")
    private String idenType;

    /**
     * 建立人員
     */
    @TableField("CRE_USER")
    private String creUser;

    /**
     * 建立日期
     */
    @TableField("CRE_DATE")
    private Date creDate;

    /**
     * 異動人員
     */
    @TableField("UPD_USER")
    private String updUser;

    /**
     * 異動日期
     */
    @TableField("UPD_DATE")
    private Date updDate;

    @TableField("DIRECT_MANAGER")
    private String directManager;


    public String getAppId() {
        return appId;
    }

    public DeptInfoExt setAppId(String appId) {
        this.appId = appId;
        return this;
    }

    public String getIdenId() {
        return idenId;
    }

    public DeptInfoExt setIdenId(String idenId) {
        this.idenId = idenId;
        return this;
    }

    public String getIdenType() {
        return idenType;
    }

    public DeptInfoExt setIdenType(String idenType) {
        this.idenType = idenType;
        return this;
    }

    public String getCreUser() {
        return creUser;
    }

    public DeptInfoExt setCreUser(String creUser) {
        this.creUser = creUser;
        return this;
    }

    public Date getCreDate() {
        return creDate;
    }

    public DeptInfoExt setCreDate(Date creDate) {
        this.creDate = creDate;
        return this;
    }

    public String getUpdUser() {
        return updUser;
    }

    public DeptInfoExt setUpdUser(String updUser) {
        this.updUser = updUser;
        return this;
    }

    public Date getUpdDate() {
        return updDate;
    }

    public DeptInfoExt setUpdDate(Date updDate) {
        this.updDate = updDate;
        return this;
    }

    public String getDirectManager() {
        return directManager;
    }

    public DeptInfoExt setDirectManager(String directManager) {
        this.directManager = directManager;
        return this;
    }

    @Override
    protected Serializable pkVal() {
        return this.appId;
    }

    @Override
    public String toString() {
        return "DeptInfoExt{" +
        "appId=" + appId +
        ", idenId=" + idenId +
        ", idenType=" + idenType +
        ", creUser=" + creUser +
        ", creDate=" + creDate +
        ", updUser=" + updUser +
        ", updDate=" + updDate +
        ", directManager=" + directManager +
        "}";
    }
}
