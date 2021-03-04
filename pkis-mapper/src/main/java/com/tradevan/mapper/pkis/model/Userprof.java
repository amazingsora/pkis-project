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
@TableName("USERPROF")
public class Userprof extends Model<Userprof> {

    private static final long serialVersionUID=1L;

    @TableId("SERNO")
    private Long serno;

    @TableField("USERID")
    private String userid;

    @TableField("USERTYPE")
    private String usertype;

    @TableField("USERNAME")
    private String username;

    @TableField("ENGNAME")
    private String engname;

    @TableField("PWDHASH")
    private String pwdhash;

    @TableField("EMAIL")
    private String email;

    @TableField("PHONE")
    private String phone;

    @TableField("EXT")
    private String ext;

    @TableField("DEPTID")
    private String deptid;

    @TableField("JOBTYPESERNO")
    private Long jobtypeserno;

    @TableField("SUBJOBTYPESERNO")
    private Long subjobtypeserno;

    @TableField("JOBRANKSERNO")
    private Long jobrankserno;

    @TableField("JOBTITLESERNO")
    private Long jobtitleserno;

    @TableField("DUTYTYPESERNO")
    private Long dutytypeserno;

    @TableField("GENDER")
    private String gender;

    @TableField("MOBILENO")
    private String mobileno;

    @TableField("IDCARDNO")
    private String idcardno;

    @TableField("BIRTHDAY")
    private String birthday;

    @TableField("ONBOARDDATE")
    private String onboarddate;

    @TableField("LEAVEDATE")
    private String leavedate;

    @TableField("USERSTATE")
    private String userstate;

    @TableField("TRAININGORGSERNO")
    private Long trainingorgserno;

    @TableField("SYSID")
    private String sysid;

    @TableField("STATUS")
    private String status;

    @TableField("PREEMPDATE")
    private Date preempdate;

    @TableField("ICHALFREALDATE")
    private Date ichalfrealdate;

    @TableField("IC2HRDATE")
    private Date ic2hrdate;

    @TableField("IC6HRDATE")
    private Date ic6hrdate;

    @TableField("SENDEMAIL")
    private String sendemail;

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

    @TableField("PREEMPCODE")
    private String preempcode;

    @TableField("DUTYPLACE")
    private String dutyplace;

    @TableField("CARDNO")
    private String cardno;


    public Long getSerno() {
        return serno;
    }

    public Userprof setSerno(Long serno) {
        this.serno = serno;
        return this;
    }

    public String getUserid() {
        return userid;
    }

    public Userprof setUserid(String userid) {
        this.userid = userid;
        return this;
    }

    public String getUsertype() {
        return usertype;
    }

    public Userprof setUsertype(String usertype) {
        this.usertype = usertype;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public Userprof setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getEngname() {
        return engname;
    }

    public Userprof setEngname(String engname) {
        this.engname = engname;
        return this;
    }

    public String getPwdhash() {
        return pwdhash;
    }

    public Userprof setPwdhash(String pwdhash) {
        this.pwdhash = pwdhash;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public Userprof setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getPhone() {
        return phone;
    }

    public Userprof setPhone(String phone) {
        this.phone = phone;
        return this;
    }

    public String getExt() {
        return ext;
    }

    public Userprof setExt(String ext) {
        this.ext = ext;
        return this;
    }

    public String getDeptid() {
        return deptid;
    }

    public Userprof setDeptid(String deptid) {
        this.deptid = deptid;
        return this;
    }

    public Long getJobtypeserno() {
        return jobtypeserno;
    }

    public Userprof setJobtypeserno(Long jobtypeserno) {
        this.jobtypeserno = jobtypeserno;
        return this;
    }

    public Long getSubjobtypeserno() {
        return subjobtypeserno;
    }

    public Userprof setSubjobtypeserno(Long subjobtypeserno) {
        this.subjobtypeserno = subjobtypeserno;
        return this;
    }

    public Long getJobrankserno() {
        return jobrankserno;
    }

    public Userprof setJobrankserno(Long jobrankserno) {
        this.jobrankserno = jobrankserno;
        return this;
    }

    public Long getJobtitleserno() {
        return jobtitleserno;
    }

    public Userprof setJobtitleserno(Long jobtitleserno) {
        this.jobtitleserno = jobtitleserno;
        return this;
    }

    public Long getDutytypeserno() {
        return dutytypeserno;
    }

    public Userprof setDutytypeserno(Long dutytypeserno) {
        this.dutytypeserno = dutytypeserno;
        return this;
    }

    public String getGender() {
        return gender;
    }

    public Userprof setGender(String gender) {
        this.gender = gender;
        return this;
    }

    public String getMobileno() {
        return mobileno;
    }

    public Userprof setMobileno(String mobileno) {
        this.mobileno = mobileno;
        return this;
    }

    public String getIdcardno() {
        return idcardno;
    }

    public Userprof setIdcardno(String idcardno) {
        this.idcardno = idcardno;
        return this;
    }

    public String getBirthday() {
        return birthday;
    }

    public Userprof setBirthday(String birthday) {
        this.birthday = birthday;
        return this;
    }

    public String getOnboarddate() {
        return onboarddate;
    }

    public Userprof setOnboarddate(String onboarddate) {
        this.onboarddate = onboarddate;
        return this;
    }

    public String getLeavedate() {
        return leavedate;
    }

    public Userprof setLeavedate(String leavedate) {
        this.leavedate = leavedate;
        return this;
    }

    public String getUserstate() {
        return userstate;
    }

    public Userprof setUserstate(String userstate) {
        this.userstate = userstate;
        return this;
    }

    public Long getTrainingorgserno() {
        return trainingorgserno;
    }

    public Userprof setTrainingorgserno(Long trainingorgserno) {
        this.trainingorgserno = trainingorgserno;
        return this;
    }

    public String getSysid() {
        return sysid;
    }

    public Userprof setSysid(String sysid) {
        this.sysid = sysid;
        return this;
    }

    public String getStatus() {
        return status;
    }

    public Userprof setStatus(String status) {
        this.status = status;
        return this;
    }

    public Date getPreempdate() {
        return preempdate;
    }

    public Userprof setPreempdate(Date preempdate) {
        this.preempdate = preempdate;
        return this;
    }

    public Date getIchalfrealdate() {
        return ichalfrealdate;
    }

    public Userprof setIchalfrealdate(Date ichalfrealdate) {
        this.ichalfrealdate = ichalfrealdate;
        return this;
    }

    public Date getIc2hrdate() {
        return ic2hrdate;
    }

    public Userprof setIc2hrdate(Date ic2hrdate) {
        this.ic2hrdate = ic2hrdate;
        return this;
    }

    public Date getIc6hrdate() {
        return ic6hrdate;
    }

    public Userprof setIc6hrdate(Date ic6hrdate) {
        this.ic6hrdate = ic6hrdate;
        return this;
    }

    public String getSendemail() {
        return sendemail;
    }

    public Userprof setSendemail(String sendemail) {
        this.sendemail = sendemail;
        return this;
    }

    public String getCreateuserid() {
        return createuserid;
    }

    public Userprof setCreateuserid(String createuserid) {
        this.createuserid = createuserid;
        return this;
    }

    public String getUpdateuserid() {
        return updateuserid;
    }

    public Userprof setUpdateuserid(String updateuserid) {
        this.updateuserid = updateuserid;
        return this;
    }

    public String getCreateagentid() {
        return createagentid;
    }

    public Userprof setCreateagentid(String createagentid) {
        this.createagentid = createagentid;
        return this;
    }

    public String getUpdateagentid() {
        return updateagentid;
    }

    public Userprof setUpdateagentid(String updateagentid) {
        this.updateagentid = updateagentid;
        return this;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public Userprof setCreatetime(Date createtime) {
        this.createtime = createtime;
        return this;
    }

    public Date getUpdatetime() {
        return updatetime;
    }

    public Userprof setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
        return this;
    }

    public String getSysmemo() {
        return sysmemo;
    }

    public Userprof setSysmemo(String sysmemo) {
        this.sysmemo = sysmemo;
        return this;
    }

    public String getPreempcode() {
        return preempcode;
    }

    public Userprof setPreempcode(String preempcode) {
        this.preempcode = preempcode;
        return this;
    }

    public String getDutyplace() {
        return dutyplace;
    }

    public Userprof setDutyplace(String dutyplace) {
        this.dutyplace = dutyplace;
        return this;
    }

    public String getCardno() {
        return cardno;
    }

    public Userprof setCardno(String cardno) {
        this.cardno = cardno;
        return this;
    }

    @Override
    protected Serializable pkVal() {
        return this.serno;
    }

    @Override
    public String toString() {
        return "Userprof{" +
        "serno=" + serno +
        ", userid=" + userid +
        ", usertype=" + usertype +
        ", username=" + username +
        ", engname=" + engname +
        ", pwdhash=" + pwdhash +
        ", email=" + email +
        ", phone=" + phone +
        ", ext=" + ext +
        ", deptid=" + deptid +
        ", jobtypeserno=" + jobtypeserno +
        ", subjobtypeserno=" + subjobtypeserno +
        ", jobrankserno=" + jobrankserno +
        ", jobtitleserno=" + jobtitleserno +
        ", dutytypeserno=" + dutytypeserno +
        ", gender=" + gender +
        ", mobileno=" + mobileno +
        ", idcardno=" + idcardno +
        ", birthday=" + birthday +
        ", onboarddate=" + onboarddate +
        ", leavedate=" + leavedate +
        ", userstate=" + userstate +
        ", trainingorgserno=" + trainingorgserno +
        ", sysid=" + sysid +
        ", status=" + status +
        ", preempdate=" + preempdate +
        ", ichalfrealdate=" + ichalfrealdate +
        ", ic2hrdate=" + ic2hrdate +
        ", ic6hrdate=" + ic6hrdate +
        ", sendemail=" + sendemail +
        ", createuserid=" + createuserid +
        ", updateuserid=" + updateuserid +
        ", createagentid=" + createagentid +
        ", updateagentid=" + updateagentid +
        ", createtime=" + createtime +
        ", updatetime=" + updatetime +
        ", sysmemo=" + sysmemo +
        ", preempcode=" + preempcode +
        ", dutyplace=" + dutyplace +
        ", cardno=" + cardno +
        "}";
    }
}
