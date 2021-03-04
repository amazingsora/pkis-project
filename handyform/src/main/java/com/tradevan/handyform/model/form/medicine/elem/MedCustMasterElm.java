package com.tradevan.handyform.model.form.medicine.elem;

import java.math.BigDecimal;
import java.util.List;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.tradevan.apcommon.util.ExcelUtil;
import com.tradevan.apcommon.util.StringUtil;
import com.tradevan.handyform.enums.MedCustDetailType;
import com.tradevan.handyform.model.form.medicine.BaseCustMedForm;

@JsonInclude(Include.NON_NULL)
public class MedCustMasterElm extends BaseCustMedForm {
	private Integer id;
	private String title;
	private String subTitle;
	private List<MedCustDetailElm> detailList;
	
	// 評量測驗記錄管理自訂多筆子表單匯入用
	private boolean subNoJsonData = false;
	private BigDecimal subScore;
	private Integer subSatisfaction;
	private Integer subSatisfaction2;
	
	private Boolean isEval; //true=>考核項目
	
	private String isShow ; //add by Arf 2019/03/05 for 護理評值表過濾資料顯示
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
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
	public List<MedCustDetailElm> getDetailList() {
		return detailList;
	}
	public void setDetailList(List<MedCustDetailElm> detailList) {
		this.detailList = detailList;
	}
	public Boolean getIsEval() {
		return isEval;
	}
	public void setIsEval(Boolean isEval) {
		this.isEval = isEval;
	}
	@JsonIgnore
	@Override
	public BigDecimal calcScore() { // 受評人分數
		return calcDetailScoreInternal(detailList, 1, null);
	}
	
	@JsonIgnore
	@Override
	public String getPassStatus() { // Cust表單結果-通過狀態
		return getDetailPassStatusInternal(detailList, 1, null);
	}
	
	@JsonIgnore
	public String getNursingEvalNeedAdvising() { // 護理評值表-需要指導
		int needAdvisingCnt = 0;
		for (MedCustDetailElm detail : detailList) {
			if (detail.getType() == MedCustDetailType.nursingEval) {
				if ("Y".equalsIgnoreCase(detail.getReply())) {
					needAdvisingCnt++;
				}
			}
		}
		return needAdvisingCnt + "/" + detailList.size();
	}
	
	@JsonIgnore
	public String getNursingEvalAdvised() { // 護理評值表-完成指導
		int advisedCnt = 0;
		int needAdvisingCnt = 0;
		for (MedCustDetailElm detail : detailList) {
			if (detail.getType() == MedCustDetailType.nursingEval) {
				if ("Y".equalsIgnoreCase(detail.getReply())) {
					needAdvisingCnt++;
				}
				if ("Y".equalsIgnoreCase(detail.getReply2())) {
					advisedCnt++;
				}
			}
		}
		return advisedCnt + "/" + needAdvisingCnt;
	}
	
	@JsonIgnore
	public String getNursingEvalDone() { // 護理評值表-完成考核
		int doneCnt = 0;
		int needEvalCnt = detailList.size();
		for (MedCustDetailElm detail : detailList) {
			if (detail.getType() == MedCustDetailType.nursingEval) {
				if ("N".equalsIgnoreCase(detail.getReply3()) || "N".equalsIgnoreCase(detail.getReply4())) {
					needEvalCnt--;
				}
				else if ("Y".equalsIgnoreCase(detail.getReply3()) || "Y".equalsIgnoreCase(detail.getReply4())) {
					doneCnt++;
				}
			}
		}
		return doneCnt + "/" + needEvalCnt;
	}
	
	@JsonIgnore
	public void replaceComments(String comments) {
		this.comments = comments;
	}
	
	public String getIsShow() {
		return isShow;
	}
	public void setIsShow(String isShow) {
		this.isShow = isShow;
	}
	
	@JsonIgnore
	public boolean isSubNoJsonData() {
		return subNoJsonData;
	}
	public void setSubNoJsonData(boolean subNoJsonData) {
		this.subNoJsonData = subNoJsonData;
	}
	
	@JsonIgnore
	public BigDecimal getSubScore() {
		return subScore;
	}
	public void setSubScore(BigDecimal subScore) {
		this.subScore = subScore;
	}
	
	@JsonIgnore
	public Integer getSubSatisfaction() {
		return subSatisfaction;
	}
	public void setSubSatisfaction(Integer subSatisfaction) {
		this.subSatisfaction = subSatisfaction;
	}
	
	@JsonIgnore
	public Integer getSubSatisfaction2() {
		return subSatisfaction2;
	}
	public void setSubSatisfaction2(Integer subSatisfaction2) {
		this.subSatisfaction2 = subSatisfaction2;
	}
	
	@JsonIgnore
	@Override
	public void addExcelColsTitle(Row row) throws Exception {
		if (detailList != null) {
			int colIndex = row.getLastCellNum();
			for (MedCustDetailElm detail : detailList) {
				row.createCell(colIndex).setCellValue(detail.getTopic());
				colIndex++;
			}
		}
	}
	
	@JsonIgnore
	@Override
	public void addExcelCells(Row row, int colIndex, CellStyle intCellStyle, CellStyle floatCellStyle) throws Exception {
		if (detailList != null) {
			for (MedCustDetailElm detail : detailList) {
				String reply = detail.getReply();
				if (StringUtil.isNotBlank(reply) && detail.getOptions() != null) {
					for (MedCustOptionElm option : detail.getOptions()) {
						if (option.getWithOthers() != null && option.getWithOthers()) {
							if (option.getValue().equals(reply)) {
								reply += ("：" + option.getMemo());
								break;
							}
							else if (StringUtil.isValuesContains(reply, option.getValue())) {
								int idx = reply.indexOf(option.getValue()) + option.getValue().length();
								reply = reply.substring(0, idx) + "：" + option.getMemo() + reply.substring(idx);
							}
						}
					}
				}
				ExcelUtil.addCell2Row(row, colIndex, reply, intCellStyle, floatCellStyle);
				colIndex++;
			}
		}
	}
}
