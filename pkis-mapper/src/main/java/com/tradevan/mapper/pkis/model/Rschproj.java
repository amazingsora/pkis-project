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
@TableName("RSCHPROJ")
public class Rschproj extends Model<Rschproj> {

    private static final long serialVersionUID=1L;

    @TableId("SERNO")
    private Long serno;

    @TableField("PROJID")
    private String projid;

    @TableField("APPLYNO")
    private String applyno;

    @TableField("STATUS")
    private String status;

    @TableField("ISOUTER")
    private String isouter;

    @TableField("CHECKNUM")
    private String checknum;

    @TableField("ORGANSERNO")
    private Long organserno;

    @TableField("OUTPROJID")
    private String outprojid;

    @TableField("PROJTYPE")
    private String projtype;

    @TableField("OTHERTYPE")
    private String othertype;

    @TableField("PROJNAME")
    private String projname;

    @TableField("PROJENGNAME")
    private String projengname;

    @TableField("BEGDATE")
    private Date begdate;

    @TableField("ENDDATE")
    private Date enddate;

    @TableField("PLACE")
    private Long place;

    @TableField("PLACENAME")
    private String placename;

    @TableField("NATURE")
    private Long nature;

    @TableField("USETYPE")
    private Long usetype;

    @TableField("HOSTTYPE")
    private String hosttype;

    @TableField("EXPERIMENT")
    private String experiment;

    @TableField("BIONAME")
    private String bioname;

    @TableField("LEV")
    private Long lev;

    @TableField("STOPDATE")
    private Date stopdate;

    @TableField("REASON")
    private String reason;

    @TableField("CONTDATE")
    private Date contdate;

    @TableField("CONTREASON")
    private String contreason;

    @TableField("CHANGEAPPLYUSERID")
    private String changeapplyuserid;

    @TableField("REFSERNO")
    private Long refserno;

    @TableField("BUDGETNO")
    private String budgetno;

    @TableField("ISSYNCEXPENSES")
    private String issyncexpenses;

    @TableField("CREATEUSERID")
    private String createuserid;


    public Long getSerno() {
        return serno;
    }

    public Rschproj setSerno(Long serno) {
        this.serno = serno;
        return this;
    }

    public String getProjid() {
        return projid;
    }

    public Rschproj setProjid(String projid) {
        this.projid = projid;
        return this;
    }

    public String getApplyno() {
        return applyno;
    }

    public Rschproj setApplyno(String applyno) {
        this.applyno = applyno;
        return this;
    }

    public String getStatus() {
        return status;
    }

    public Rschproj setStatus(String status) {
        this.status = status;
        return this;
    }

    public String getIsouter() {
        return isouter;
    }

    public Rschproj setIsouter(String isouter) {
        this.isouter = isouter;
        return this;
    }

    public String getChecknum() {
        return checknum;
    }

    public Rschproj setChecknum(String checknum) {
        this.checknum = checknum;
        return this;
    }

    public Long getOrganserno() {
        return organserno;
    }

    public Rschproj setOrganserno(Long organserno) {
        this.organserno = organserno;
        return this;
    }

    public String getOutprojid() {
        return outprojid;
    }

    public Rschproj setOutprojid(String outprojid) {
        this.outprojid = outprojid;
        return this;
    }

    public String getProjtype() {
        return projtype;
    }

    public Rschproj setProjtype(String projtype) {
        this.projtype = projtype;
        return this;
    }

    public String getOthertype() {
        return othertype;
    }

    public Rschproj setOthertype(String othertype) {
        this.othertype = othertype;
        return this;
    }

    public String getProjname() {
        return projname;
    }

    public Rschproj setProjname(String projname) {
        this.projname = projname;
        return this;
    }

    public String getProjengname() {
        return projengname;
    }

    public Rschproj setProjengname(String projengname) {
        this.projengname = projengname;
        return this;
    }

    public Date getBegdate() {
        return begdate;
    }

    public Rschproj setBegdate(Date begdate) {
        this.begdate = begdate;
        return this;
    }

    public Date getEnddate() {
        return enddate;
    }

    public Rschproj setEnddate(Date enddate) {
        this.enddate = enddate;
        return this;
    }

    public Long getPlace() {
        return place;
    }

    public Rschproj setPlace(Long place) {
        this.place = place;
        return this;
    }

    public String getPlacename() {
        return placename;
    }

    public Rschproj setPlacename(String placename) {
        this.placename = placename;
        return this;
    }

    public Long getNature() {
        return nature;
    }

    public Rschproj setNature(Long nature) {
        this.nature = nature;
        return this;
    }

    public Long getUsetype() {
        return usetype;
    }

    public Rschproj setUsetype(Long usetype) {
        this.usetype = usetype;
        return this;
    }

    public String getHosttype() {
        return hosttype;
    }

    public Rschproj setHosttype(String hosttype) {
        this.hosttype = hosttype;
        return this;
    }

    public String getExperiment() {
        return experiment;
    }

    public Rschproj setExperiment(String experiment) {
        this.experiment = experiment;
        return this;
    }

    public String getBioname() {
        return bioname;
    }

    public Rschproj setBioname(String bioname) {
        this.bioname = bioname;
        return this;
    }

    public Long getLev() {
        return lev;
    }

    public Rschproj setLev(Long lev) {
        this.lev = lev;
        return this;
    }

    public Date getStopdate() {
        return stopdate;
    }

    public Rschproj setStopdate(Date stopdate) {
        this.stopdate = stopdate;
        return this;
    }

    public String getReason() {
        return reason;
    }

    public Rschproj setReason(String reason) {
        this.reason = reason;
        return this;
    }

    public Date getContdate() {
        return contdate;
    }

    public Rschproj setContdate(Date contdate) {
        this.contdate = contdate;
        return this;
    }

    public String getContreason() {
        return contreason;
    }

    public Rschproj setContreason(String contreason) {
        this.contreason = contreason;
        return this;
    }

    public String getChangeapplyuserid() {
        return changeapplyuserid;
    }

    public Rschproj setChangeapplyuserid(String changeapplyuserid) {
        this.changeapplyuserid = changeapplyuserid;
        return this;
    }

    public Long getRefserno() {
        return refserno;
    }

    public Rschproj setRefserno(Long refserno) {
        this.refserno = refserno;
        return this;
    }

    public String getBudgetno() {
        return budgetno;
    }

    public Rschproj setBudgetno(String budgetno) {
        this.budgetno = budgetno;
        return this;
    }

    public String getIssyncexpenses() {
        return issyncexpenses;
    }

    public Rschproj setIssyncexpenses(String issyncexpenses) {
        this.issyncexpenses = issyncexpenses;
        return this;
    }

    public String getCreateuserid() {
        return createuserid;
    }

    public Rschproj setCreateuserid(String createuserid) {
        this.createuserid = createuserid;
        return this;
    }

    @Override
    protected Serializable pkVal() {
        return this.serno;
    }

    @Override
    public String toString() {
        return "Rschproj{" +
        "serno=" + serno +
        ", projid=" + projid +
        ", applyno=" + applyno +
        ", status=" + status +
        ", isouter=" + isouter +
        ", checknum=" + checknum +
        ", organserno=" + organserno +
        ", outprojid=" + outprojid +
        ", projtype=" + projtype +
        ", othertype=" + othertype +
        ", projname=" + projname +
        ", projengname=" + projengname +
        ", begdate=" + begdate +
        ", enddate=" + enddate +
        ", place=" + place +
        ", placename=" + placename +
        ", nature=" + nature +
        ", usetype=" + usetype +
        ", hosttype=" + hosttype +
        ", experiment=" + experiment +
        ", bioname=" + bioname +
        ", lev=" + lev +
        ", stopdate=" + stopdate +
        ", reason=" + reason +
        ", contdate=" + contdate +
        ", contreason=" + contreason +
        ", changeapplyuserid=" + changeapplyuserid +
        ", refserno=" + refserno +
        ", budgetno=" + budgetno +
        ", issyncexpenses=" + issyncexpenses +
        ", createuserid=" + createuserid +
        "}";
    }
}
