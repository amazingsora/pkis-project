package com.tradevan.handyform.model.form.medicine;

import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.tradevan.apcommon.util.ExcelUtil;
import com.tradevan.apcommon.util.StringUtil;
import com.tradevan.handyform.enums.MedMeetingType;
import com.tradevan.handyform.model.form.medicine.elem.MedMeetingElement;

@JsonInclude(Include.NON_NULL)
public class MedFormMeeting extends BaseMedForm {
	private MedMeetingElement meetingDate;
	private MedMeetingElement meetingPlace;
	private MedMeetingElement meetingHost;
	private MedMeetingElement meetingReporter;
	private MedMeetingElement meeingDesc;
	private List<MedMeetingElement> meetingUsers;
	private Boolean isShare;
	private List<MedMeetingElement> secNonScoring;
	
	public MedMeetingElement getMeetingDate() {
		return meetingDate;
	}
	public void setMeetingDate(MedMeetingElement meetingDate) {
		this.meetingDate = meetingDate;
	}
	public MedMeetingElement getMeetingPlace() {
		return meetingPlace;
	}
	public void setMeetingPlace(MedMeetingElement meetingPlace) {
		this.meetingPlace = meetingPlace;
	}
	public MedMeetingElement getMeetingHost() {
		return meetingHost;
	}
	public void setMeetingHost(MedMeetingElement meetingHost) {
		this.meetingHost = meetingHost;
	}
	public MedMeetingElement getMeetingReporter() {
		return meetingReporter;
	}
	public void setMeetingReporter(MedMeetingElement meetingReporter) {
		this.meetingReporter = meetingReporter;
	}
	public MedMeetingElement getMeeingDesc() {
		return meeingDesc;
	}
	public void setMeeingDesc(MedMeetingElement meeingDesc) {
		this.meeingDesc = meeingDesc;
	}
	public List<MedMeetingElement> getMeetingUsers() {
		return meetingUsers;
	}
	public void setMeetingUsers(List<MedMeetingElement> meetingUsers) {
		this.meetingUsers = meetingUsers;
	}
	public Boolean getIsShare() {
		return isShare;
	}
	public void setIsShare(Boolean isShare) {
		this.isShare = isShare;
	}
	public List<MedMeetingElement> getSecNonScoring() {
		return secNonScoring;
	}
	public void setSecNonScoring(List<MedMeetingElement> secNonScoring) {
		this.secNonScoring = secNonScoring;
	}
	
	@JsonIgnore
	private final String RADIO = "●";
	@JsonIgnore
	private final String RADIO_BLANK = "○";
	@JsonIgnore
	private final String CHECK = "■";
	@JsonIgnore
	private final String CHECK_BLANK = "□";
	
	@JsonIgnore
	@Override
	public List<EduMedFormReportBean> getReportBeanList(){
		ArrayList<EduMedFormReportBean> retList = new ArrayList<EduMedFormReportBean>();
		
		for(MedMeetingElement elem : secNonScoring) {
			
			EduMedFormReportBean reportBean = new EduMedFormReportBean();
			reportBean.setValue(elem.getValue());
			reportBean.setType(elem.getType().name());
			
			reportBean.setReply(elem.getReply());
			genOptionsStr(reportBean,elem);				
			
			retList.add(reportBean);
		}
		return retList;
	}
	
	@JsonIgnore
	private void genOptionsStr(EduMedFormReportBean reportBean,MedMeetingElement detailElem) {
		if (("quesRadioVal".equalsIgnoreCase(detailElem.getType().name())) || ("quesCheckVal".equalsIgnoreCase(detailElem.getType().name())) || 
			("quesComboVal".equalsIgnoreCase(detailElem.getType().name())))  {
			
			reportBean.setOptionBlankStr(genRadioOptionsStr(detailElem.getOptions(), detailElem.getType().name(),null));
			reportBean.setOptionStr(genRadioOptionsStr(detailElem.getOptions(), detailElem.getType().name(), detailElem.getReply()));
		}
	}
	
	@JsonIgnore
	private String genRadioOptionsStr(List<String> optionsList , String type , String reply) {
		if ("quesRadioVal".equalsIgnoreCase(type)) {
			return genRadioOptionsStrInternal(null, reply , RADIO, RADIO_BLANK);
		}
		else if ("quesCheckVal".equalsIgnoreCase(type)) {
			return genRadioOptionsStrInternal(optionsList, reply , CHECK, CHECK_BLANK);
		}
		else {
			return genRadioOptionsStrInternal(optionsList, reply , RADIO, RADIO_BLANK);
		}
	}
	
	@JsonIgnore
	private String genRadioOptionsStrInternal(List<String> optionsList , String reply,String checkStr, String blankStr) {
		StringBuffer optionStr = new StringBuffer();
		if (optionsList == null) { // quesRadioVal
			optionStr.append("Y".equalsIgnoreCase(reply) ? checkStr : blankStr).append(" 是 ").append("N".equalsIgnoreCase(reply) ? checkStr : blankStr).append(" 否 ");
		}
		else {
			for (int i = 0; i < optionsList.size(); i++) {
				String idx = String.valueOf(i);
				String option = optionsList.get(i);
				if (! StringUtil.isBlank2(option)) {
					String replyOpt = (idx.equals(reply) || StringUtil.isValuesContains(reply, idx)) ? checkStr: blankStr;
					optionStr.append(replyOpt + " " + option+"  ");
				}
			}
		}
		return optionStr.toString();
	}
	
	@JsonIgnore
	@Override
	public void addExcelColsTitle(Row row) throws Exception {
		int colIndex = row.getLastCellNum();
		row.createCell(colIndex).setCellValue(meetingDate.getValue());
		colIndex++;
		row.createCell(colIndex).setCellValue(meetingPlace.getValue());
		colIndex++;
		row.createCell(colIndex).setCellValue(meetingHost.getValue());
		colIndex++;
		row.createCell(colIndex).setCellValue(meetingReporter.getValue());
		colIndex++;
		row.createCell(colIndex).setCellValue(meeingDesc.getValue());
		colIndex++;
		row.createCell(colIndex).setCellValue("出席人員");
		colIndex++;
		row.createCell(colIndex).setCellValue("共同記錄");
		colIndex++;
		for (MedMeetingElement elm : secNonScoring) {
			row.createCell(colIndex).setCellValue(elm.getValue());
			colIndex++;
		}
	}
	
	@JsonIgnore
	@Override
	public void addExcelCells(Row row, int colIndex, CellStyle intCellStyle, CellStyle floatCellStyle) throws Exception {
		ExcelUtil.addCell2Row(row, colIndex, meetingDate.getReply(), intCellStyle, floatCellStyle);
		colIndex++;
		ExcelUtil.addCell2Row(row, colIndex, meetingPlace.getReply(), intCellStyle, floatCellStyle);
		colIndex++;
		ExcelUtil.addCell2Row(row, colIndex, meetingHost.getReply(), intCellStyle, floatCellStyle);
		colIndex++;
		ExcelUtil.addCell2Row(row, colIndex, meetingReporter.getReply(), intCellStyle, floatCellStyle);
		colIndex++;
		ExcelUtil.addCell2Row(row, colIndex, meeingDesc.getReply(), intCellStyle, floatCellStyle);
		colIndex++;
		StringBuilder buf = new StringBuilder();
		for (MedMeetingElement elm : meetingUsers) {
			buf.append(elm.getReply()).append(",");
		}
		ExcelUtil.addCell2Row(row, colIndex, buf.toString(), intCellStyle, floatCellStyle);
		colIndex++;
		ExcelUtil.addCell2Row(row, colIndex, isShare, intCellStyle, floatCellStyle);
		colIndex++;
		for (MedMeetingElement elm : secNonScoring) {
			String reply = "";
			if (elm.getType() == MedMeetingType.quesComboVal || elm.getType() == MedMeetingType.quesCheckVal) {
				for (int i = 0; i < elm.getOptions().size(); i++) {
					if (String.valueOf(i).equals(elm.getReply())) {
						if (elm.getType() == MedMeetingType.quesCheckVal) {
							reply += reply.length() != 0 ? "," : "";
							reply += elm.getOptions().get(i);
						}
						else {
							reply = elm.getOptions().get(i);
							break;
						}
					}
				}
			}
			else {
				reply = elm.getReply();
			}
			ExcelUtil.addCell2Row(row, colIndex, reply, intCellStyle, floatCellStyle);
			colIndex++;
		}
	}
}
