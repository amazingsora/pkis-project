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
import com.tradevan.handyform.enums.MedDopsType;
import com.tradevan.handyform.model.form.medicine.elem.MedDopsElement;

@JsonInclude(Include.NON_NULL)
public class MedFormDops extends BaseMedForm {
	private MedDopsElement place;
	private List<MedDopsElement> patient;
	private List<MedDopsElement> techInfo;
	private List<MedDopsElement> operInfo;
	private List<MedDopsElement> secEvalItems;
	private Boolean secTimingOnOff;
	private List<MedDopsElement> secTiming;
	private Boolean secCommentOnOff;
	private List<MedDopsElement> secComment;
	private Boolean satisfactionOnOff;
	private MedDopsElement satisfaction;
	private MedDopsElement satisfaction2;
	
	@JsonIgnore
	@Override
	public BigDecimal calcScore() { // 受評人分數
		int totalNum = 0;
		int score = 0;
		for (MedDopsElement elm : secEvalItems) {
			if (elm.getReply() != null && MedDopsType.ques06Point.name().equals(elm.getType().name()) && !"0".equals(elm.getReply())) {
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
		if (techInfo != null && techInfo.size() > 0) {
			return techInfo.get(0).getReply();
		}
		return null;
	}
	
	public MedDopsElement getPlace() {
		return place;
	}
	public void setPlace(MedDopsElement place) {
		this.place = place;
	}
	public List<MedDopsElement> getPatient() {
		return patient;
	}
	public void setPatient(List<MedDopsElement> patient) {
		this.patient = patient;
	}
	public List<MedDopsElement> getTechInfo() {
		return techInfo;
	}
	public void setTechInfo(List<MedDopsElement> techInfo) {
		this.techInfo = techInfo;
	}
	public List<MedDopsElement> getOperInfo() {
		return operInfo;
	}
	public void setOperInfo(List<MedDopsElement> operInfo) {
		this.operInfo = operInfo;
	}
	public List<MedDopsElement> getSecEvalItems() {
		return secEvalItems;
	}
	public void setSecEvalItems(List<MedDopsElement> secEvalItems) {
		this.secEvalItems = secEvalItems;
	}
	public Boolean getSecTimingOnOff() {
		return secTimingOnOff;
	}
	public void setSecTimingOnOff(Boolean secTimingOnOff) {
		this.secTimingOnOff = secTimingOnOff;
	}
	public List<MedDopsElement> getSecTiming() {
		return secTiming;
	}
	public void setSecTiming(List<MedDopsElement> secTiming) {
		this.secTiming = secTiming;
	}
	public Boolean getSecCommentOnOff() {
		return secCommentOnOff;
	}
	public void setSecCommentOnOff(Boolean secCommentOnOff) {
		this.secCommentOnOff = secCommentOnOff;
	}
	public List<MedDopsElement> getSecComment() {
		return secComment;
	}
	public void setSecComment(List<MedDopsElement> secComment) {
		this.secComment = secComment;
	}
	public Boolean getSatisfactionOnOff() {
		return satisfactionOnOff;
	}
	public void setSatisfactionOnOff(Boolean satisfactionOnOff) {
		this.satisfactionOnOff = satisfactionOnOff;
	}
	public MedDopsElement getSatisfaction() {
		return satisfaction;
	}
	public void setSatisfaction(MedDopsElement satisfaction) {
		this.satisfaction = satisfaction;
	}
	public MedDopsElement getSatisfaction2() {
		return satisfaction2;
	}
	public void setSatisfaction2(MedDopsElement satisfaction2) {
		this.satisfaction2 = satisfaction2;
	}
	
	@JsonIgnore
	@Override
	public List<EduMedFormReportBean> getReportBeanList(){
		ArrayList<EduMedFormReportBean> retList = new ArrayList<EduMedFormReportBean>();
		
		for(MedDopsElement elem : getSecEvalItems()) {
			EduMedFormReportBean bean = new EduMedFormReportBean(elem.getType().name(),elem.getValue(),elem.getReply());
			retList.add(bean);
		}
		return retList;
	}
	
	@JsonIgnore
	@Override
	public void addExcelColsTitle(Row row) throws Exception {
		int colIndex = row.getLastCellNum();
		if (place != null) {
			row.createCell(colIndex).setCellValue(place.getValue());
			colIndex++;
		}
		for (MedDopsElement elm : patient) {
			row.createCell(colIndex).setCellValue(elm.getValue());
			colIndex++;
		}
		for (MedDopsElement elm : techInfo) {
			row.createCell(colIndex).setCellValue(elm.getValue());
			colIndex++;
		}
		for (MedDopsElement elm : operInfo) {
			row.createCell(colIndex).setCellValue(elm.getValue());
			colIndex++;
		}
		for (MedDopsElement elm : secEvalItems) {
			row.createCell(colIndex).setCellValue(elm.getValue());
			colIndex++;
		}
		if (secTimingOnOff) {
			for (MedDopsElement elm : secTiming) {
				row.createCell(colIndex).setCellValue(elm.getValue());
				colIndex++;
			}
		}
		if (secCommentOnOff) {
			for (MedDopsElement elm : secComment) {
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
		if (place != null) {
			ExcelUtil.addCell2Row(row, colIndex, place.getReply() + (place.getOther() != null ? "(" + place.getOther() + ")" : ""), intCellStyle, floatCellStyle);
			colIndex++;
		}
		for (MedDopsElement elm : patient) {
			ExcelUtil.addCell2Row(row, colIndex, elm.getReply(), intCellStyle, floatCellStyle);
			colIndex++;
		}
		for (MedDopsElement elm : techInfo) {
			ExcelUtil.addCell2Row(row, colIndex, elm.getReply(), intCellStyle, floatCellStyle);
			colIndex++;
		}
		for (MedDopsElement elm : operInfo) {
			ExcelUtil.addCell2Row(row, colIndex, elm.getReply(), intCellStyle, floatCellStyle);
			colIndex++;
		}
		for (MedDopsElement elm : secEvalItems) {
			ExcelUtil.addCell2Row(row, colIndex, elm.getReply(), intCellStyle, floatCellStyle);
			colIndex++;
		}
		if (secTimingOnOff) {
			for (MedDopsElement elm : secTiming) {
				ExcelUtil.addCell2Row(row, colIndex, elm.getReply(), intCellStyle, floatCellStyle);
				colIndex++;
			}
		}
		if (secCommentOnOff) {
			for (MedDopsElement elm : secComment) {
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
