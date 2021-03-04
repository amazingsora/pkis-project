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
import com.tradevan.handyform.enums.MedEval360Type;
import com.tradevan.handyform.model.form.medicine.elem.MedEval360Element;

@JsonInclude(Include.NON_NULL)
public class MedFormEval360 extends BaseMedForm {
	private MedEval360Element period;
	private List<MedEval360Element> secTarget;
	private List<MedEval360Element> secEvalItems;
	private Boolean satisfactionOnOff;
	private MedEval360Element satisfaction;
	private MedEval360Element satisfaction2;
	
	@JsonIgnore
	@Override
	public BigDecimal calcScore() { // 受評人分數
		int totalNum = 0;
		int score = 0;
		for (MedEval360Element elm : secEvalItems) {
			if (elm.getReply() != null && MedEval360Type.ques06Point.name().equals(elm.getType().name()) && !"0".equals(elm.getReply())) {
				try {
					score += Integer.parseInt(elm.getReply());
				}
				catch (NumberFormatException e) {
					// Ignore it
				}
				totalNum++;
			}
		}
		return (totalNum == 0) ? BigDecimal.ZERO : new BigDecimal(score * 100).divide(new BigDecimal(totalNum * 6), 1, BigDecimal.ROUND_HALF_UP);
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
	
	@JsonIgnore
	@Override
	public List<EduMedFormReportBean> getReportBeanList(){
		ArrayList<EduMedFormReportBean> retList = new ArrayList<EduMedFormReportBean>();
		
		for(MedEval360Element elem : getSecEvalItems()) {
			EduMedFormReportBean bean = new EduMedFormReportBean(elem.getType().name(),elem.getValue(),elem.getReply());
			retList.add(bean);
		}
		return retList;
	}
	
	public MedEval360Element getPeriod() {
		return period;
	}
	public void setPeriod(MedEval360Element period) {
		this.period = period;
	}
	public List<MedEval360Element> getSecTarget() {
		return secTarget;
	}
	public void setSecTarget(List<MedEval360Element> secTarget) {
		this.secTarget = secTarget;
	}
	public List<MedEval360Element> getSecEvalItems() {
		return secEvalItems;
	}
	public void setSecEvalItems(List<MedEval360Element> secEvalItems) {
		this.secEvalItems = secEvalItems;
	}
	public Boolean getSatisfactionOnOff() {
		return satisfactionOnOff;
	}
	public void setSatisfactionOnOff(Boolean satisfactionOnOff) {
		this.satisfactionOnOff = satisfactionOnOff;
	}
	public MedEval360Element getSatisfaction() {
		return satisfaction;
	}
	public void setSatisfaction(MedEval360Element satisfaction) {
		this.satisfaction = satisfaction;
	}
	public MedEval360Element getSatisfaction2() {
		return satisfaction2;
	}
	public void setSatisfaction2(MedEval360Element satisfaction2) {
		this.satisfaction2 = satisfaction2;
	}
	
	@JsonIgnore
	@Override
	public void addExcelColsTitle(Row row) throws Exception {
		int colIndex = row.getLastCellNum();
		row.createCell(colIndex).setCellValue(period.getValue() + "(起)");
		colIndex++;
		row.createCell(colIndex).setCellValue(period.getValue() + "(迄)");
		colIndex++;
		for (MedEval360Element elm : secTarget) {
			row.createCell(colIndex).setCellValue(elm.getValue());
			colIndex++;
		}
		for (MedEval360Element elm : secEvalItems) {
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
		ExcelUtil.addCell2Row(row, colIndex, period.getBegin(), intCellStyle, floatCellStyle);
		colIndex++;
		ExcelUtil.addCell2Row(row, colIndex, period.getEnd(), intCellStyle, floatCellStyle);
		colIndex++;
		for (MedEval360Element elm : secTarget) {
			ExcelUtil.addCell2Row(row, colIndex, elm.getReply(), intCellStyle, floatCellStyle);
			colIndex++;
		}
		for (MedEval360Element elm : secEvalItems) {
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
