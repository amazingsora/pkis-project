package com.tradevan.mapper.xauth.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

/**
 * <p>
 * 角色代理人資料檔
 * </p>
 *
 * @author penny
 * @since 2020-03-02
 */
@TableName("XAUTH_ROLE_AGENT_USER")
public class XauthRoleAgentUser extends Model<XauthRoleAgentUser> {

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
     * 角色代號
     */
    @TableField("ROLE_ID")
    private String roleId;

    /**
     * 代理系統代號
     */
    @TableField("AGENT_APP_ID")
    private String agentAppId;

    /**
     * 代理識別碼
     */
    @TableField("AGENT_IDEN_ID")
    private String agentIdenId;

    /**
     * 代理使用者帳號
     */
    @TableField("AGENT_USER_ID")
    private String agentUserId;

    /**
     * 是否啟用 0:停用 1:啟用
     */
    @TableField("ENABLED")
    private String enabled;

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
    

    /**
     * 使用者
     */
    @TableField("USER_ID")
    private String userId;

    
   

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getAppId() {
        return appId;
    }

    public XauthRoleAgentUser setAppId(String appId) {
        this.appId = appId;
        return this;
    }

    public String getIdenId() {
        return idenId;
    }

    public XauthRoleAgentUser setIdenId(String idenId) {
        this.idenId = idenId;
        return this;
    }

    public String getRoleId() {
        return roleId;
    }

    public XauthRoleAgentUser setRoleId(String roleId) {
        this.roleId = roleId;
        return this;
    }

    public String getAgentAppId() {
        return agentAppId;
    }

    public XauthRoleAgentUser setAgentAppId(String agentAppId) {
        this.agentAppId = agentAppId;
        return this;
    }

    public String getAgentIdenId() {
        return agentIdenId;
    }

    public XauthRoleAgentUser setAgentIdenId(String agentIdenId) {
        this.agentIdenId = agentIdenId;
        return this;
    }

    public String getAgentUserId() {
        return agentUserId;
    }

    public XauthRoleAgentUser setAgentUserId(String agentUserId) {
        this.agentUserId = agentUserId;
        return this;
    }

    public String getEnabled() {
        return enabled;
    }

    public XauthRoleAgentUser setEnabled(String enabled) {
        this.enabled = enabled;
        return this;
    }

    public String getCreUser() {
        return creUser;
    }

    public XauthRoleAgentUser setCreUser(String creUser) {
        this.creUser = creUser;
        return this;
    }

    public Date getCreDate() {
        return creDate;
    }

    public XauthRoleAgentUser setCreDate(Date creDate) {
        this.creDate = creDate;
        return this;
    }

    public String getUpdUser() {
        return updUser;
    }

    public XauthRoleAgentUser setUpdUser(String updUser) {
        this.updUser = updUser;
        return this;
    }

    public Date getUpdDate() {
        return updDate;
    }

    public XauthRoleAgentUser setUpdDate(Date updDate) {
        this.updDate = updDate;
        return this;
    }

    @Override
    protected Serializable pkVal() {
        return this.appId;
    }

    @Override
    public String toString() {
        return "XauthRoleAgentUser{" +
        "appId=" + appId +
        ", idenId=" + idenId +
        ", roleId=" + roleId +
        ", agentAppId=" + agentAppId +
        ", agentIdenId=" + agentIdenId +
        ", agentUserId=" + agentUserId +
        ", enabled=" + enabled +
        ", creUser=" + creUser +
        ", creDate=" + creDate +
        ", updUser=" + updUser +
        ", updDate=" + updDate +
        "}";
    }
}
