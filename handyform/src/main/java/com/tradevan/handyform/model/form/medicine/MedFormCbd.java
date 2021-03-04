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
import com.tradevan.handyform.enums.MedCbdType;
import com.tradevan.handyform.model.form.medicine.elem.MedCbdElement;

@JsonInclude(Include.NON_NULL)
public class MedFormCbd extends BaseMedForm {
	private List<MedCbdElement> patient;
	private List<MedCbdElement> secEvalItems;
	private Boolean secTimingOnOff;
	private List<MedCbdElement> secTiming;
	private Boolean secCommentOnOff;
	private List<MedCbdElement> secComment;
	private Boolean satisfactionOnOff;
	private MedCbdElement satisfaction;
	private MedCbdElement satisfaction2;
	
	@JsonIgnore
	@Override
	public BigDecimal calcScore() { // 受評人分數
		int totalNum = 0;
		int score = 0;
		for (MedCbdElement elm : secEvalItems) {
			if (elm.getReply() != null && MedCbdType.ques06Point.name().equals(elm.getType().name()) && !"0".equals(elm.getReply())) {
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
	public String getEvalSubject() { // 評量主題
		if (patient != null && patient.size() > 4) {
			return patient.get(4).getReply();
		}
		return null;
	}
	
	public List<MedCbdElement> getPatient() {
		return patient;
	}
	public void setPatient(List<MedCbdElement> patient) {
		this.patient = patient;
	}
	public List<MedCbdElement> getSecEvalItems() {
		return secEvalItems;
	}
	public void setSecEvalItems(List<MedCbdElement> secEvalItems) {
		this.secEvalItems = secEvalItems;
	}
	public Boolean getSecTimingOnOff() {
		return secTimingOnOff;
	}
	public void setSecTimingOnOff(Boolean secTimingOnOff) {
		this.secTimingOnOff = secTimingOnOff;
	}
	public List<MedCbdElement> getSecTiming() {
		return secTiming;
	}
	public void setSecTiming(List<MedCbdElement> secTiming) {
		this.secTiming = secTiming;
	}
	public Boolean getSecCommentOnOff() {
		return secCommentOnOff;
	}
	public void setSecCommentOnOff(Boolean secCommentOnOff) {
		this.secCommentOnOff = secCommentOnOff;
	}
	public List<MedCbdElement> getSecComment() {
		return secComment;
	}
	public void setSecComment(List<MedCbdElement> secComment) {
		this.secComment = secComment;
	}
	public Boolean getSatisfactionOnOff() {
		return satisfactionOnOff;
	}
	public void setSatisfactionOnOff(Boolean satisfactionOnOff) {
		this.satisfactionOnOff = satisfactionOnOff;
	}
	public MedCbdElement getSatisfaction() {
		return satisfaction;
	}
	public void setSatisfaction(MedCbdElement satisfaction) {
		this.satisfaction = satisfaction;
	}
	public MedCbdElement getSatisfaction2() {
		return satisfaction2;
	}
	public void setSatisfaction2(MedCbdElement satisfaction2) {
		this.satisfaction2 = satisfaction2;
	}
	
	@JsonIgnore
	@Override
	public List<EduMedFormReportBean> getReportBeanList(){
		ArrayList<EduMedFormReportBean> retList = new ArrayList<EduMedFormReportBean>();
		
		for(MedCbdElement elem : getSecEvalItems()) {
			EduMedFormReportBean bean = new EduMedFormReportBean(elem.getType().name(),elem.getValue(),elem.getReply());
			retList.add(bean);
		}
		return retList;
	}
	
	@JsonIgnore
	@Override
	public void addExcelColsTitle(Row row) throws Exception {
		int colIndex = row.getLastCellNum();
		for (MedCbdElement elm : patient) {
			row.createCell(colIndex).setCellValue(elm.getValue());
			colIndex++;
		}
		for (MedCbdElement elm : secEvalItems) {
			row.createCell(colIndex).setCellValue(elm.getValue());
			colIndex++;
		}
		if (secTimingOnOff) {
			for (MedCbdElement elm : secTiming) {
				row.createCell(colIndex).setCellValue(elm.getValue());
				colIndex++;
			}
		}
		if (secCommentOnOff) {
			for (MedCbdElement elm : secComment) {
				row.createCell(colIndex).setCellValue(elm.getValue());
				colIndex++;
			}
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
		for (MedCbdElement elm : patient) {
			ExcelUtil.addCell2Row(row, colIndex, elm.getReply(), intCellStyle, floatCellStyle);
			colIndex++;
		}
		for (MedCbdElement elm : secEvalItems) {
			ExcelUtil.addCell2Row(row, colIndex, elm.getReply(), intCellStyle, floatCellStyle);
			colIndex++;
		}
		if (secTimingOnOff) {
			for (MedCbdElement elm : secTiming) {
				ExcelUtil.addCell2Row(row, colIndex, elm.getReply(), intCellStyle, floatCellStyle);
				colIndex++;
			}
		}
		if (secCommentOnOff) {
			for (MedCbdElement elm : secComment) {
				ExcelUtil.addCell2Row(row, colIndex, elm.getReply(), intCellStyle, floatCellStyle);
				colIndex++;
			}
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
