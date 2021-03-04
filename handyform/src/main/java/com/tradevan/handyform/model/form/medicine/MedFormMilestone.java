package com.tradevan.handyform.model.form.medicine;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.tradevan.apcommon.util.ExcelUtil;
import com.tradevan.handyform.enums.MedMilestoneType;
import com.tradevan.handyform.model.form.medicine.elem.MedMilestoneElement;

@JsonInclude(Include.NON_NULL)
public class MedFormMilestone extends BaseMedForm {
	private List<MedMilestoneElement> secEvalItems;
	private Boolean satisfactionOnOff;
	private MedMilestoneElement satisfaction;
	private MedMilestoneElement satisfaction2;

	@JsonIgnore
	@Override
	public BigDecimal calcScore() { // 受評人分數
		int totalNum = 0;
		int score = 0;
		for (MedMilestoneElement elm : secEvalItems) {
			if (elm.getReply() != null && MedMilestoneType.ques09PointComment.name().equals(elm.getType().name())) {
				try {
					elm.setRating(elm.calcRating().toString());
					score += Integer.parseInt(elm.getReply());
				}
				catch (NumberFormatException e) {
					// Ignore it
				}
				totalNum++;
			}
		}
		return (totalNum == 0) ? BigDecimal.ZERO : new BigDecimal(score * 5).divide(new BigDecimal(totalNum * 9), 1, BigDecimal.ROUND_HALF_UP);
	}
	
	@JsonIgnore
	@Override
	public Integer calcSatisfaction() { // 受評人滿意度
		if (satisfaction != null) {
			try {
				return Integer.parseInt(satisfaction.getReply());
			}
			catch (NumberFormatException e) {
				// Ignore it
			}
		}
		return null;
	}
	
	@JsonIgnore
	@Override
	public Integer calcSatisfaction2() { // 評量人滿意度
		if (satisfaction2 != null) {
			try {
				return Integer.parseInt(satisfaction2.getReply());
			}
			catch (NumberFormatException e) {
				// Ignore it
			}
		}
		return null;
	}
	
	public List<MedMilestoneElement> getSecEvalItems() {
		return secEvalItems;
	}
	public void setSecEvalItems(List<MedMilestoneElement> secEvalItems) {
		this.secEvalItems = secEvalItems;
	}
	public Boolean getSatisfactionOnOff() {
		return satisfactionOnOff;
	}
	public void setSatisfactionOnOff(Boolean satisfactionOnOff) {
		this.satisfactionOnOff = satisfactionOnOff;
	}
	public MedMilestoneElement getSatisfaction() {
		return satisfaction;
	}
	public void setSatisfaction(MedMilestoneElement satisfaction) {
		this.satisfaction = satisfaction;
	}
	public MedMilestoneElement getSatisfaction2() {
		return satisfaction2;
	}
	public void setSatisfaction2(MedMilestoneElement satisfaction2) {
		this.satisfaction2 = satisfaction2;
	}
	
	@JsonIgnore
	@Override
	public List<EduMedFormReportBean> getReportBeanList(){
		ArrayList<EduMedFormReportBean> retList = new ArrayList<EduMedFormReportBean>();
		
		for(MedMilestoneElement elem : getSecEvalItems()) {
			
			elem.setLevel1(replaceNewLineForReport(elem.getLevel1()));
			elem.setLevel2(replaceNewLineForReport(elem.getLevel2()));
			elem.setLevel3(replaceNewLineForReport(elem.getLevel3()));
			elem.setLevel4(replaceNewLineForReport(elem.getLevel4()));
			elem.setLevel5(replaceNewLineForReport(elem.getLevel5()));
			
			retList.add(new EduMedFormReportBean(elem));
		}
		return retList;
	}
	
	@JsonIgnore
	private String replaceNewLineForReport(String inputStr) {
		inputStr = inputStr.replaceAll("<br>", "\n");
		inputStr = inputStr.replaceAll("<BR>", "\n");
		return inputStr;
	}
	
	@JsonIgnore
	@Override
	public void addExcelColsTitle(Row row) throws Exception {
		int colIndex = row.getLastCellNum();
		for (MedMilestoneElement elm : secEvalItems) {
			row.createCell(colIndex).setCellValue(elm.getValue());
			colIndex++;
		}
		if (satisfactionOnOff) {
			row.createCell(colIndex).setCellValue(satisfaction.getValue());
			colIndex++;
			row.createCell(colIndex).setCellValue(satisfaction2.getValue());
			colIndex++;
		}
	}
	
	@JsonIgnore
	@Override
	public void addExcelCells(Row row, int colIndex, CellStyle intCellStyle, CellStyle floatCellStyle) throws Exception {
		for (MedMilestoneElement elm : secEvalItems) {
			ExcelUtil.addCell2Row(row, colIndex, elm.getReply(), intCellStyle, floatCellStyle);
			colIndex++;
		}
		if (satisfactionOnOff) {
			ExcelUtil.addCell2Row(row, colIndex, satisfaction.getReply(), intCellStyle, floatCellStyle);
			colIndex++;
			ExcelUtil.addCell2Row(row, colIndex, satisfaction2.getReply(), intCellStyle, floatCellStyle);
			colIndex++;
		}
	}
	
	@JsonIgnore
	@Override
	public boolean hasForReview() {
		if (satisfactionOnOff != null && satisfactionOnOff) {
			return true;
		}
		return false;
	}
}
