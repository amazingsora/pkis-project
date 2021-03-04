package com.tradevan.handyform.model.form.medicine;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.tradevan.apcommon.util.ExcelUtil;
import com.tradevan.apcommon.util.StringUtil;
import com.tradevan.handyform.model.form.medicine.elem.MedCaseLogElement;

import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@JsonInclude(Include.NON_NULL)
public class MedFormCaseLog extends BaseMedForm {
	private List<MedCaseLogElement> secStudyItems;
	private List<MedCaseLogElement> caseInfo;
	private List<MedCaseLogElement> secLogColumns;
	private List<MedCaseLogElement> secEvalColumns;
	
	public List<MedCaseLogElement> getSecStudyItems() {
		return secStudyItems;
	}
	public void setSecStudyItems(List<MedCaseLogElement> secStudyItems) {
		this.secStudyItems = secStudyItems;
	}
	public List<MedCaseLogElement> getCaseInfo() {
		return caseInfo;
	}
	public void setCaseInfo(List<MedCaseLogElement> caseInfo) {
		this.caseInfo = caseInfo;
	}
	public List<MedCaseLogElement> getSecLogColumns() {
		return secLogColumns;
	}
	public void setSecLogColumns(List<MedCaseLogElement> secLogColumns) {
		this.secLogColumns = secLogColumns;
	}
	public List<MedCaseLogElement> getSecEvalColumns() {
		return secEvalColumns;
	}
	public void setSecEvalColumns(List<MedCaseLogElement> secEvalColumns) {
		this.secEvalColumns = secEvalColumns;
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
	public List<EduMedFormReportBean> getReportBeanList(List<Map<String, Object>> caseLogItemDocCounts){
		ArrayList<EduMedFormReportBean> masterList = new ArrayList<EduMedFormReportBean>();
		
		EduMedFormReportBean masterData = new EduMedFormReportBean();	
		masterData.setMaster1DS(new JRBeanCollectionDataSource(genSummaryDS(caseLogItemDocCounts)));//學習項目資料
		masterData.setDetailDS(new JRBeanCollectionDataSource(genDetailDS()));//紀錄填寫資料
		
		masterList.add((masterData));
		return masterList;
	}
	
	@JsonIgnore
	private List<EduMedFormReportBean> genSummaryDS(List<Map<String, Object>> caseLogItemDocCounts){
		ArrayList<EduMedFormReportBean> retDetailList = new ArrayList<EduMedFormReportBean>();
		
		for(MedCaseLogElement detailElem : getSecStudyItems()) {
			
			EduMedFormReportBean reportBean = new EduMedFormReportBean();
			reportBean.setId(detailElem.getId()+"");
			reportBean.setValue(detailElem.getValue());
			reportBean.setCaseNum(detailElem.getCaseNum()+"");
			reportBean.setReply2(parseCaseLogItemDoneNum(caseLogItemDocCounts, detailElem.getId()) + "/" + detailElem.getCaseNum());//進度
			
			retDetailList.add(reportBean);
		}
		return retDetailList;
	}
	
	private String parseCaseLogItemDoneNum(List<Map<String, Object>> caseLogItemDocCounts, Integer elemId) {
		if (caseLogItemDocCounts != null) {
			for (Map<String, Object> entry : caseLogItemDocCounts) {
				Short studyItemId = (Short) entry.get("studyItemId");
				if (studyItemId != null && elemId != null && studyItemId.intValue() == elemId.intValue()) {
					return String.valueOf(entry.get("doneNum"));
				}
			}
		}
		return "0";
	}
	
	@JsonIgnore
	public List<EduMedFormReportBean> genDetailDS(){
		ArrayList<EduMedFormReportBean> retDetailList = new ArrayList<EduMedFormReportBean>();
		
		retDetailList.addAll(genDetailData(getCaseInfo(), true));
		retDetailList.addAll(genDetailData(getSecLogColumns(), false));
		retDetailList.addAll(genDetailData(getSecEvalColumns(), false));
		return retDetailList;
	}
	
	@JsonIgnore
	private List<EduMedFormReportBean> genDetailData(List<MedCaseLogElement> dataList, boolean isOnlyRequired){
		ArrayList<EduMedFormReportBean> retDetailList = new ArrayList<EduMedFormReportBean>();
		
		for(MedCaseLogElement detailElem : dataList) {
			if (isOnlyRequired == false || (isOnlyRequired == true && detailElem.getRequired())) {
				EduMedFormReportBean reportBean = new EduMedFormReportBean();
				
				reportBean.setType(detailElem.getType().name());
				reportBean.setValue(detailElem.getValue());
				reportBean.setReply(detailElem.getReply());
				genOptionsStr(reportBean,detailElem);				
				
				retDetailList.add(reportBean);
			}
		}
		
		return retDetailList;
	}
	
	
	@JsonIgnore
	private void genOptionsStr(EduMedFormReportBean reportBean,MedCaseLogElement detailElem) {
		if ("quesCheckVal".equalsIgnoreCase(detailElem.getType().name()) || "quesRadioVal".equalsIgnoreCase(detailElem.getType().name()) || 
				"quesComboVal".equalsIgnoreCase(detailElem.getType().name())) {
			reportBean.setOptionBlankStr(genRadioOptionsStr(detailElem.getOptions(),detailElem.getType().name(),null));
			reportBean.setOptionStr(genRadioOptionsStr(detailElem.getOptions(),detailElem.getType().name(),detailElem.getReply()));
		}
	}
		
	@JsonIgnore
	private String genRadioOptionsStr(List<String> optionsList, String type, String reply) {
		if ("quesCheckVal".equalsIgnoreCase(type)) {
			return genRadioOptionsStrInternal(optionsList, reply, CHECK, CHECK_BLANK);
		}else {
			return genRadioOptionsStrInternal(optionsList, reply, RADIO, RADIO_BLANK);
		}
	}
	
	
	@JsonIgnore
	private String genRadioOptionsStrInternal(List<String> optionsList , String reply,String checkStr, String blankStr) {
		StringBuffer optionStr = new StringBuffer();
		for(String option : optionsList) {
			if (! StringUtil.isBlank2(option)) {
				String replyOpt = (option.equals(reply) || StringUtil.isValuesContains(reply, option)) ? checkStr: blankStr;
				optionStr.append(replyOpt + " " + option+"  ");
			}
		}
		return optionStr.toString();
	}
	
	@JsonIgnore
	public MedCaseLogElement getReplyStudyItem() {
		if (CollectionUtils.isEmpty(secStudyItems) ) {
			return null;
		}else {
			for(MedCaseLogElement detailElem : secStudyItems) {
				if (StringUtil.isNotBlank(detailElem.getReply())) {
					return detailElem;
				}
			}
			return null;
		}
	}
	
	@JsonIgnore
	@Override
	public void addExcelColsTitle(Row row) throws Exception {
		int colIndex = row.getLastCellNum();
		row.createCell(colIndex).setCellValue("學習項目");
		colIndex++;
		for (MedCaseLogElement elm : caseInfo) {
			if (elm.getRequired()) {
				row.createCell(colIndex).setCellValue(elm.getValue());
				colIndex++;
			}
		}
		for (MedCaseLogElement elm : secLogColumns) {
			row.createCell(colIndex).setCellValue(elm.getValue());
			colIndex++;
		}
		for (MedCaseLogElement elm : secEvalColumns) {
			row.createCell(colIndex).setCellValue(elm.getValue());
			colIndex++;
		}
	}
	
	@JsonIgnore
	@Override
	public void addExcelCells(Row row, int colIndex, CellStyle intCellStyle, CellStyle floatCellStyle) throws Exception {
		for (MedCaseLogElement elm : secStudyItems) {
			if (StringUtil.isNotBlank(elm.getReply())) {
				ExcelUtil.addCell2Row(row, colIndex, elm.getValue(), intCellStyle, floatCellStyle);
				colIndex++;
				break;
			}
		}
		for (MedCaseLogElement elm : caseInfo) {
			if (elm.getRequired()) {
				ExcelUtil.addCell2Row(row, colIndex, elm.getReply(), intCellStyle, floatCellStyle);
				colIndex++;
			}
		}
		for (MedCaseLogElement elm : secLogColumns) {
			ExcelUtil.addCell2Row(row, colIndex, elm.getReply(), intCellStyle, floatCellStyle);
			colIndex++;
		}
		for (MedCaseLogElement elm : secEvalColumns) {
			ExcelUtil.addCell2Row(row, colIndex, elm.getReply(), intCellStyle, floatCellStyle);
			colIndex++;
		}
	}
	
	@JsonIgnore
	@Override
	public boolean hasForReview() {
		if (secEvalColumns != null && secEvalColumns.size() > 0) {
			return true;
		}
		return false;
	}
}
