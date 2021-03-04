package com.tradevan.handyform.model.form.medicine;

import java.awt.Image;
import java.util.List;

import com.tradevan.handyform.model.form.medicine.elem.MedMilestoneElement;

import net.sf.jasperreports.engine.JRDataSource;

public class EduMedFormReportBean {
	
	private String id;
	private String type;
	private String value;
	private String reply;
	
	//---for Milestone begin
	private String desc;
	private String level1;
	private String level2;
	private String level3;
	private String level4;
	private String level5;
	private String rating;
	private String comment;
	//---for Milestone end
	
	//---for Custom form detail begin	
	private String title;
	private String subTitle;
	private String topic;
	
	private List<EduMedFormReportBean> detailList;
	private List<EduMedFormReportBean> options;
	private Boolean withOthers;
	private String memo;
	private Boolean isEval;	
	private JRDataSource detailDS;
	private JRDataSource evalItemDS;//自訂多筆借用此物件存放master2 多筆子表單title資料
	
	private String topicStr;
	private String optionStr;
	private String optionBlankStr;
	private String optionOthersStr;
	private Image familyTreeImage;
	//---for Custom form detail end	
	
	//---for NursingEval form detail begin
	private String reply2;
	private String reply3;
	private String reply4;
	private String needAdvisingStr;//需要指導 x/x
	private String evalAdvisedStr;//完成指導 x/x
	private String evalDoneStr;//完成考核 x/x
	//---for NursingEval form detail end
	
	//---for CustomMulti form detail begin
	private JRDataSource master1DS;
	private JRDataSource master2DS;//空白表單有額外用途
	private JRDataSource master3ToNDS;
	private String flow;
	private String comments;
	//---for CustomMulti form detail end
	
	//---for CaseLog form detail begin
	private String caseNum;
	//---for CaseLog form detail end
	
	public EduMedFormReportBean() {
		super();
	}
	
	public EduMedFormReportBean(String type, String value, String reply) {
		super();
		this.type = type;
		this.value = value;
		this.reply = reply;
	}
	
	public EduMedFormReportBean(MedMilestoneElement elem) {
		super();
		this.id = (elem.getId() == null)? "" :elem.getId().toString();
		this.type = elem.getType().name();
		this.value = elem.getValue();
		this.desc = elem.getDesc();
		this.level1 = elem.getLevel1();
		this.level2 = elem.getLevel2();
		this.level3 = elem.getLevel3();
		this.level4 = elem.getLevel4();
		this.level5 = elem.getLevel5();
		this.reply = elem.getReply();
		this.rating = elem.getRating();
		this.comment = elem.getComment();
	}
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getReply() {
		return reply;
	}
	public void setReply(String reply) {
		this.reply = reply;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getLevel1() {
		return level1;
	}
	public void setLevel1(String level1) {
		this.level1 = level1;
	}
	public String getLevel2() {
		return level2;
	}
	public void setLevel2(String level2) {
		this.level2 = level2;
	}
	public String getLevel3() {
		return level3;
	}
	public void setLevel3(String level3) {
		this.level3 = level3;
	}
	public String getLevel4() {
		return level4;
	}
	public void setLevel4(String level4) {
		this.level4 = level4;
	}
	public String getLevel5() {
		return level5;
	}
	public void setLevel5(String level5) {
		this.level5 = level5;
	}
	public String getRating() {
		return rating;
	}
	public void setRating(String rating) {
		this.rating = rating;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public List<EduMedFormReportBean> getDetailList() {
		return detailList;
	}
	public void setDetailList(List<EduMedFormReportBean> detailList) {
		this.detailList = detailList;
	}
	public List<EduMedFormReportBean> getOptions() {
		return options;
	}
	public void setOptions(List<EduMedFormReportBean> options) {
		this.options = options;
	}
	public Boolean getIsEval() {
		return isEval;
	}
	public void setIsEval(Boolean isEval) {
		this.isEval = isEval;
	}	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getSubTitle() {
		return subTitle;
	}
	public void setSubTitle(String subTitle) {
		this.subTitle = subTitle;
	}	
	public JRDataSource getDetailDS() {
		return detailDS;
	}
	public void setDetailDS(JRDataSource detailDS) {
		this.detailDS = detailDS;
	}
	public JRDataSource getEvalItemDS() {
		return evalItemDS;
	}
	public void setEvalItemDS(JRDataSource evalItemDS) {
		this.evalItemDS = evalItemDS;
	}
	public Boolean getWithOthers() {
		return withOthers;
	}
	public void setWithOthers(Boolean withOthers) {
		this.withOthers = withOthers;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public String getTopic() {
		return topic;
	}
	public void setTopic(String topic) {
		this.topic = topic;
	}
	public String getTopicStr() {
		return topicStr;
	}
	public void setTopicStr(String topicStr) {
		this.topicStr = topicStr;
	}
	public String getOptionStr() {
		return optionStr;
	}
	public void setOptionStr(String optionStr) {
		this.optionStr = optionStr;
	}
	public String getOptionBlankStr() {
		return optionBlankStr;
	}
	public void setOptionBlankStr(String optionBlankStr) {
		this.optionBlankStr = optionBlankStr;
	}
	public String getOptionOthersStr() {
		return optionOthersStr;
	}
	public void setOptionOthersStr(String optionOthersStr) {
		this.optionOthersStr = optionOthersStr;
	}
	public Image getFamilyTreeImage() {
		return familyTreeImage;
	}
	public void setFamilyTreeImage(Image familyTreeImage) {
		this.familyTreeImage = familyTreeImage;
	}
	public String getReply2() {
		return reply2;
	}
	public void setReply2(String reply2) {
		this.reply2 = reply2;
	}
	public String getReply3() {
		return reply3;
	}
	public void setReply3(String reply3) {
		this.reply3 = reply3;
	}
	public String getReply4() {
		return reply4;
	}
	public void setReply4(String reply4) {
		this.reply4 = reply4;
	}
	public String getNeedAdvisingStr() {
		return needAdvisingStr;
	}
	public void setNeedAdvisingStr(String needAdvisingStr) {
		this.needAdvisingStr = needAdvisingStr;
	}
	public String getEvalAdvisedStr() {
		return evalAdvisedStr;
	}
	public void setEvalAdvisedStr(String evalAdvisedStr) {
		this.evalAdvisedStr = evalAdvisedStr;
	}
	public String getEvalDoneStr() {
		return evalDoneStr;
	}
	public void setEvalDoneStr(String evalDoneStr) {
		this.evalDoneStr = evalDoneStr;
	}
	public JRDataSource getMaster1DS() {
		return master1DS;
	}
	public void setMaster1DS(JRDataSource master1ds) {
		master1DS = master1ds;
	}
	public JRDataSource getMaster2DS() {
		return master2DS;
	}
	public void setMaster2DS(JRDataSource master2ds) {
		master2DS = master2ds;
	}
	public JRDataSource getMaster3ToNDS() {
		return master3ToNDS;
	}
	public void setMaster3ToNDS(JRDataSource master3ToNDS) {
		this.master3ToNDS = master3ToNDS;
	}
	public String getFlow() {
		return flow;
	}
	public void setFlow(String flow) {
		this.flow = flow;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	public String getCaseNum() {
		return caseNum;
	}
	public void setCaseNum(String caseNum) {
		this.caseNum = caseNum;
	}
	
}
