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
import com.tradevan.handyform.enums.MedMiniCexType;
import com.tradevan.handyform.model.form.medicine.elem.MedCustDetailElm;
import com.tradevan.handyform.model.form.medicine.elem.MedCustMasterElm;
import com.tradevan.handyform.model.form.medicine.elem.MedMiniCexElement;

@JsonInclude(Include.NON_NULL)
public class MedFormMiniCex extends BaseMedForm {
	private MedMiniCexElement place;
	private List<MedMiniCexElement> secPatient;
	private List<MedMiniCexElement> secDiagnosis;
	private MedMiniCexElement phase;
	private List<MedMiniCexElement> secEvaluation;
	private List<MedMiniCexElement> secEvalItems;
	private MedMiniCexElement result;
	private List<MedMiniCexElement> secTiming;
	private List<MedMiniCexElement> secComment;
	private Boolean satisfactionOnOff;
	private MedMiniCexElement satisfaction;
	private MedMiniCexElement satisfaction2;
	
	@JsonIgnore
	@Override
	public BigDecimal calcScore() { // 受評人分數
		int totalNum = 0;
		int score = 0;
		for (MedMiniCexElement elm : secEvalItems) {
			if (elm.getReply() != null && MedMiniCexType.ques09Point.name().equals(elm.getType().name()) && !"0".equals(elm.getReply())) {
				try {
					score += Integer.parseInt(elm.getReply());
				}
				catch (NumberFormatException e) {
					// Ignore it
				}
				totalNum++;
			}
		}
		return (totalNum == 0) ? BigDecimal.ZERO : new BigDecimal(score * 100).divide(new BigDecimal(totalNum * 9), 1, BigDecimal.ROUND_HALF_UP);
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
		if (secEvaluation != null && secEvaluation.size() > 0) {
			return secEvaluation.get(0).getReply();
		}
		return null;
	}
	
	public MedMiniCexElement getPlace() {
		return place;
	}
	public void setPlace(MedMiniCexElement place) {
		this.place = place;
	}
	public List<MedMiniCexElement> getSecPatient() {
		return secPatient;
	}
	public void setSecPatient(List<MedMiniCexElement> secPatient) {
		this.secPatient = secPatient;
	}
	public List<MedMiniCexElement> getSecDiagnosis() {
		return secDiagnosis;
	}
	public void setSecDiagnosis(List<MedMiniCexElement> secDiagnosis) {
		this.secDiagnosis = secDiagnosis;
	}
	public MedMiniCexElement getPhase() {
		return phase;
	}
	public void setPhase(MedMiniCexElement phase) {
		this.phase = phase;
	}
	public List<MedMiniCexElement> getSecEvaluation() {
		return secEvaluation;
	}
	public void setSecEvaluation(List<MedMiniCexElement> secEvaluation) {
		this.secEvaluation = secEvaluation;
	}
	public List<MedMiniCexElement> getSecEvalItems() {
		return secEvalItems;
	}
	public void setSecEvalItems(List<MedMiniCexElement> secEvalItems) {
		this.secEvalItems = secEvalItems;
	}
	public MedMiniCexElement getResult() {
		return result;
	}
	public void setResult(MedMiniCexElement result) {
		this.result = result;
	}
	public List<MedMiniCexElement> getSecTiming() {
		return secTiming;
	}
	public void setSecTiming(List<MedMiniCexElement> secTiming) {
		this.secTiming = secTiming;
	}
	public List<MedMiniCexElement> getSecComment() {
		return secComment;
	}
	public void setSecComment(List<MedMiniCexElement> secComment) {
		this.secComment = secComment;
	}
	public Boolean getSatisfactionOnOff() {
		return satisfactionOnOff;
	}
	public void setSatisfactionOnOff(Boolean satisfactionOnOff) {
		this.satisfactionOnOff = satisfactionOnOff;
	}
	public MedMiniCexElement getSatisfaction() {
		return satisfaction;
	}
	public void setSatisfaction(MedMiniCexElement satisfaction) {
		this.satisfaction = satisfaction;
	}
	public MedMiniCexElement getSatisfaction2() {
		return satisfaction2;
	}
	public void setSatisfaction2(MedMiniCexElement satisfaction2) {
		this.satisfaction2 = satisfaction2;
	}
	
	@JsonIgnore
	@Override
	public List<EduMedFormReportBean> getReportBeanList(){
		ArrayList<EduMedFormReportBean> retList = new ArrayList<EduMedFormReportBean>();
		
		for(MedMiniCexElement elem : getSecEvalItems()) {
			EduMedFormReportBean bean = new EduMedFormReportBean(elem.getType().name(),elem.getValue(),elem.getReply());
			retList.add(bean);
		}
		return retList;
	}
	
	@JsonIgnore
	@Override
	public void addExcelColsTitle(Row row) throws Exception {
		int colIndex = row.getLastCellNum();
		row.createCell(colIndex).setCellValue(place.getValue());
		colIndex++;
		for (MedMiniCexElement elm : secPatient) {
			row.createCell(colIndex).setCellValue(elm.getValue());
			colIndex++;
		}
		for (MedMiniCexElement elm : secDiagnosis) {
			row.createCell(colIndex).setCellValue(elm.getValue());
			colIndex++;
		}
		row.createCell(colIndex).setCellValue(phase.getValue());
		colIndex++;
		for (MedMiniCexElement elm : secEvaluation) {
			row.createCell(colIndex).setCellValue(elm.getValue());
			colIndex++;
		}
		for (MedMiniCexElement elm : secEvalItems) {
			row.createCell(colIndex).setCellValue(elm.getValue());
			colIndex++;
		}
		row.createCell(colIndex).setCellValue(result.getValue());
		colIndex++;
		for (MedMiniCexElement elm : secTiming) {
			row.createCell(colIndex).setCellValue(elm.getValue());
			colIndex++;
		}
		for (MedMiniCexElement elm : secComment) {
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
		ExcelUtil.addCell2Row(row, colIndex, place.getReply() + (place.getOther() != null ? "(" + place.getOther() + ")" : ""), intCellStyle, floatCellStyle);
		colIndex++;
		for (MedMiniCexElement elm : secPatient) {
			ExcelUtil.addCell2Row(row, colIndex, elm.getReply(), intCellStyle, floatCellStyle);
			colIndex++;
		}
		for (MedMiniCexElement elm : secDiagnosis) {
			ExcelUtil.addCell2Row(row, colIndex, elm.getReply(), intCellStyle, floatCellStyle);;
			colIndex++;
		}
		ExcelUtil.addCell2Row(row, colIndex, phase.getReply() + (phase.getOther() != null ? "(" + phase.getOther() + ")" : ""), intCellStyle, floatCellStyle);
		colIndex++;
		for (MedMiniCexElement elm : secEvaluation) {
			ExcelUtil.addCell2Row(row, colIndex, elm.getReply(), intCellStyle, floatCellStyle);
			colIndex++;
		}
		for (MedMiniCexElement elm : secEvalItems) {
			ExcelUtil.addCell2Row(row, colIndex, elm.getReply(), intCellStyle, floatCellStyle);
			colIndex++;
		}
		ExcelUtil.addCell2Row(row, colIndex, result.getReply(), intCellStyle, floatCellStyle);
		colIndex++;
		for (MedMiniCexElement elm : secTiming) {
			ExcelUtil.addCell2Row(row, colIndex, elm.getReply(), intCellStyle, floatCellStyle);
			colIndex++;
		}
		for (MedMiniCexElement elm : secComment) {
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
