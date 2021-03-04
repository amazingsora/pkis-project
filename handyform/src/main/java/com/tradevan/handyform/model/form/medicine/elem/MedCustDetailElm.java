package com.tradevan.handyform.model.form.medicine.elem;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.tradevan.handyform.enums.MedCustFormResult;
import com.tradevan.handyform.enums.MedCustFormScoring;
import com.tradevan.handyform.enums.MedCustPassType;
import com.tradevan.handyform.enums.MedCustScoringType;
import com.tradevan.handyform.enums.MedCustDetailType;

@JsonInclude(Include.NON_NULL)
public class MedCustDetailElm {
	private Integer id;
	private String topic;
	
	// 答題方式
	private Boolean forReview; // 是否為審查輸入欄位
	private MedCustDetailType type;
	private Integer min;
	private Integer max;
	private Integer rows;
	private List<MedCustOptionElm> options;
	
	// 多項評量
	private Boolean isEval;
	private Boolean isShowEvalResult;
	private Boolean isOnlyForReview;
	private List<MedCustEvalElm> evalItems;
	private MedCustScoringType scoringType;
	private MedCustPassType passType;
	
	// 表單計分
	private MedCustFormScoring formScoring;
	
	// 表單結果
	private MedCustFormResult formResult;
	
	private String reply;
	private String reply2;
	private String reply2a;
	private String reply2b;
	private String reply2c;
	private String reply3;
	private String reply3a;
	private String reply3b;
	private String reply3c;
	private String reply4;
	private String reply4a;
	private String reply4b;
	private String reply4c;
	private String memo;
	
	private String isShow ; //add by Arf 2019/03/05 for 護理評值表過濾資料顯示
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getTopic() {
		return topic;
	}
	public void setTopic(String topic) {
		this.topic = topic;
	}
	public MedCustDetailType getType() {
		return type;
	}
	public void setType(MedCustDetailType type) {
		this.type = type;
	}
	public Integer getMin() {
		return min;
	}
	public void setMin(Integer min) {
		this.min = min;
	}
	public Integer getMax() {
		return max;
	}
	public void setMax(Integer max) {
		this.max = max;
	}
	public Integer getRows() {
		return rows;
	}
	public void setRows(Integer rows) {
		this.rows = rows;
	}
	public List<MedCustOptionElm> getOptions() {
		return options;
	}
	public void setOptions(List<MedCustOptionElm> options) {
		this.options = options;
	}
	public Boolean getIsEval() {
		return isEval;
	}
	public void setIsEval(Boolean isEval) {
		this.isEval = isEval;
	}
	public Boolean getIsShowEvalResult() {
		return isShowEvalResult;
	}
	public void setIsShowEvalResult(Boolean isShowEvalResult) {
		this.isShowEvalResult = isShowEvalResult;
	}
	public Boolean getIsOnlyForReview() {
		return isOnlyForReview;
	}
	public void setIsOnlyForReview(Boolean isOnlyForReview) {
		this.isOnlyForReview = isOnlyForReview;
	}
	public List<MedCustEvalElm> getEvalItems() {
		return evalItems;
	}
	public void setEvalItems(List<MedCustEvalElm> evalItems) {
		this.evalItems = evalItems;
	}
	public MedCustScoringType getScoringType() {
		return scoringType;
	}
	public void setScoringType(MedCustScoringType scoringType) {
		this.scoringType = scoringType;
	}
	public MedCustPassType getPassType() {
		return passType;
	}
	public void setPassType(MedCustPassType passType) {
		this.passType = passType;
	}
	public MedCustFormScoring getFormScoring() {
		return formScoring;
	}
	public void setFormScoring(MedCustFormScoring formScoring) {
		this.formScoring = formScoring;
	}
	public MedCustFormResult getFormResult() {
		return formResult;
	}
	public void setFormResult(MedCustFormResult formResult) {
		this.formResult = formResult;
	}
	public Boolean getForReview() {
		return forReview;
	}
	public void setForReview(Boolean forReview) {
		this.forReview = forReview;
	}
	public String getReply() {
		return reply;
	}
	public void setReply(String reply) {
		this.reply = reply;
	}
	public String getReply2() {
		return reply2;
	}
	public void setReply2(String reply2) {
		this.reply2 = reply2;
	}
	public String getReply2a() {
		return reply2a;
	}
	public void setReply2a(String reply2a) {
		this.reply2a = reply2a;
	}
	public String getReply2b() {
		return reply2b;
	}
	public void setReply2b(String reply2b) {
		this.reply2b = reply2b;
	}
	public String getReply2c() {
		return reply2c;
	}
	public void setReply2c(String reply2c) {
		this.reply2c = reply2c;
	}
	public String getReply3() {
		return reply3;
	}
	public void setReply3(String reply3) {
		this.reply3 = reply3;
	}
	public String getReply3a() {
		return reply3a;
	}
	public void setReply3a(String reply3a) {
		this.reply3a = reply3a;
	}
	public String getReply3b() {
		return reply3b;
	}
	public void setReply3b(String reply3b) {
		this.reply3b = reply3b;
	}
	public String getReply3c() {
		return reply3c;
	}
	public void setReply3c(String reply3c) {
		this.reply3c = reply3c;
	}
	public String getReply4() {
		return reply4;
	}
	public void setReply4(String reply4) {
		this.reply4 = reply4;
	}
	public String getReply4a() {
		return reply4a;
	}
	public void setReply4a(String reply4a) {
		this.reply4a = reply4a;
	}
	public String getReply4b() {
		return reply4b;
	}
	public void setReply4b(String reply4b) {
		this.reply4b = reply4b;
	}
	public String getReply4c() {
		return reply4c;
	}
	public void setReply4c(String reply4c) {
		this.reply4c = reply4c;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public String getIsShow() {
		return isShow;
	}
	public void setIsShow(String isShow) {
		this.isShow = isShow;
	}
	
}
