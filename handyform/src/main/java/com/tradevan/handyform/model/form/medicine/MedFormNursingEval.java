package com.tradevan.handyform.model.form.medicine;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.tradevan.apcommon.util.ExcelUtil;
import com.tradevan.apcommon.util.StringUtil;
import com.tradevan.handyform.model.form.medicine.elem.MedCustDetailElm;
import com.tradevan.handyform.model.form.medicine.elem.MedCustEvalElm;
import com.tradevan.handyform.model.form.medicine.elem.MedCustMasterElm;
import com.tradevan.handyform.model.form.medicine.elem.MedNursingElement;

import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@JsonInclude(Include.NON_NULL)
public class MedFormNursingEval extends BaseCustMedForm {
	private MedNursingElement trainingType;
	private MedNursingElement nurseStatus;
	private MedNursingElement teacher;
	private List<MedCustMasterElm> masterList;

	public MedNursingElement getTrainingType() {
		return trainingType;
	}
	public void setTrainingType(MedNursingElement trainingType) {
		this.trainingType = trainingType;
	}
	public MedNursingElement getNurseStatus() {
		return nurseStatus;
	}
	public void setNurseStatus(MedNursingElement nurseStatus) {
		this.nurseStatus = nurseStatus;
	}
	public MedNursingElement getTeacher() {
		return teacher;
	}
	public void setTeacher(MedNursingElement teacher) {
		this.teacher = teacher;
	}
	public List<MedCustMasterElm> getMasterList() {
		return masterList;
	}
	public void setMasterList(List<MedCustMasterElm> masterList) {
		this.masterList = masterList;
	}
	
	@JsonIgnore
	@Override
	public String getPassStatus() { // Cust表單結果-通過狀態
		int totalCnt = getTotalCount();
		int passCnt = getPassCount();
		return totalCnt > 0 && totalCnt == passCnt ? "Y" : "N";
	}
	
	@JsonIgnore
	@Override
	public int getTotalCount() { // 總項目
		int totalCnt = 0;
		for (MedCustMasterElm master : masterList) {
			totalCnt+=master.getDetailList().size();
		}
		return totalCnt;
	}
	
	@JsonIgnore
	@Override
	public int getPassCount() { // 通過項目
		int passCnt = 0;
		for (MedCustMasterElm master : masterList) {
			for (MedCustDetailElm detail : master.getDetailList()) {
				String reply3 = detail.getReply3();
				String reply4 = detail.getReply4();
				if ("Y".equals(reply3) || "N".equals(reply3) || "Y".equals(reply4) || "N".equals(reply4)) { // Y:通過/N:不需考核
					passCnt++;
				}
			}
		}
		return passCnt;
	}
	
	@JsonIgnore
	@Override
	public List<EduMedFormReportBean> getReportBeanList(){
		ArrayList<EduMedFormReportBean> masterList = new ArrayList<EduMedFormReportBean>();
		
		for(MedCustMasterElm master : getMasterList()) {
			EduMedFormReportBean masterData = new EduMedFormReportBean();
			masterData.setId(master.getId()+"");
			masterData.setTitle(master.getTitle());
			masterData.setSubTitle(master.getSubTitle());
			
			masterData.setNeedAdvisingStr(master.getNursingEvalNeedAdvising());
			masterData.setEvalAdvisedStr(master.getNursingEvalAdvised());
			masterData.setEvalDoneStr(master.getNursingEvalDone());
			masterData.setDetailDS(new JRBeanCollectionDataSource(genDetailDS(master.getDetailList())));
			masterList.add((masterData));
		}
		return masterList;
	}
	
	@JsonIgnore
	private List<EduMedFormReportBean> genDetailDS(List<MedCustDetailElm> detailList){
		ArrayList<EduMedFormReportBean> retDetailList = new ArrayList<EduMedFormReportBean>();
		
		for(MedCustDetailElm detailElem : detailList) {
			EduMedFormReportBean reportBean = new EduMedFormReportBean();
						
			reportBean.setTopic(detailElem.getTopic());
			reportBean.setReply(getReplyStr(detailElem.getReply()));
			reportBean.setReply2(getReply2Str(detailElem));
			reportBean.setReply3(getReply3Str(detailElem));
			reportBean.setReply4(getReply4Str(detailElem));
			reportBean.setMemo(detailElem.getMemo());
			
			retDetailList.add(reportBean);
		}
		return retDetailList;
	}
	
	@JsonIgnore
	private String getReplyStr(String reply) {
		if (StringUtils.isNotBlank(reply) && "Y".equalsIgnoreCase(reply)) {
			return "需要指導";
		}else {
			return "";
		}
	}
	
	@JsonIgnore
	private String getReply2Str(MedCustDetailElm detailElem) {
		return getReplyStrInternal(detailElem.getReply2(), detailElem.getReply2a(), detailElem.getReply2b(), detailElem.getReply2c(), true);
	}
	
	@JsonIgnore
	private String getReply3Str(MedCustDetailElm detailElem) {
		return getReplyStrInternal(detailElem.getReply3(), detailElem.getReply3a(), detailElem.getReply3b(), detailElem.getReply3c(), true);
	}
	
	@JsonIgnore
	private String getReply4Str(MedCustDetailElm detailElem) {
		return getReplyStrInternal(detailElem.getReply4(), detailElem.getReply4a(), detailElem.getReply4b(), detailElem.getReply4c(), true);
	}
	
	@JsonIgnore
	private String getReplyStrInternal(String reply, String replyA, String replyB, String replyC, boolean newLine) {
		if (StringUtils.isBlank(reply)) {
			return null;
		}else {
			if ("Y".equals(reply)) {//通過
				return genReplyStr("通過", replyA, replyB, replyC, newLine);
			}else if ("R".equals(reply)) {//部分了解
				return genReplyStr("部分了解", replyA, replyB, replyC, newLine);
			}else if ("X".equals(reply)) {//不了解
				return genReplyStr("不了解", replyA, replyB, replyC, newLine);
			}else if ("N".equals(reply)) {//不需考核
				return genReplyStr("不需考核", replyA, replyB, replyC, newLine);
			}else{
				return null;
			}
		}
	}
	
	@JsonIgnore
	private String genReplyStr(String replyStr, String replyA, String replyB, String replyC, boolean newLine) {
		StringBuffer retStr = new StringBuffer();
		
		retStr.append(replyStr + (newLine ? "\n" : "/"));
		retStr.append((!"".equals(replaceNull(replyA))) ? replaceNull(replyA) + (newLine ? "\n" : "") : "/");
		retStr.append(replaceNull(replyB) + (newLine ? "\n" : "/"));
		retStr.append(replaceNull(replyC));
		
		return retStr.toString();
	}
	
	@JsonIgnore
	private String replaceNull(String str) {
		if (StringUtil.isBlank(str)) {
			return "";
		}else {
			return str;
		}
	}
	
	@JsonIgnore
	@Override
	public void addExcelColsTitle(Row row) throws Exception {
		int colIndex = row.getLastCellNum();
		if (trainingType != null) {
			row.createCell(colIndex).setCellValue(trainingType.getValue());
			colIndex++;
		}
		if (nurseStatus != null) {
			row.createCell(colIndex).setCellValue(nurseStatus.getValue());
			colIndex++;
		}
		if (teacher != null) {
			row.createCell(colIndex).setCellValue(teacher.getValue());
			colIndex++;
		}
		for (MedCustMasterElm master : masterList) {
			row.createCell(colIndex).setCellValue("訓練項目");
			colIndex++;
			for (int x = 0; x < master.getDetailList().size(); x++) {
				row.createCell(colIndex).setCellValue("目標");
				colIndex++;
				row.createCell(colIndex).setCellValue("學員自評");
				colIndex++;
				row.createCell(colIndex).setCellValue("指導狀態");
				colIndex++;
				row.createCell(colIndex).setCellValue("初評");
				colIndex++;
				row.createCell(colIndex).setCellValue("複評");
				colIndex++;
				row.createCell(colIndex).setCellValue("備註");
				colIndex++;
			}
		}
	}
	
	@JsonIgnore
	@Override
	public void addExcelCells(Row row, int colIndex, CellStyle intCellStyle, CellStyle floatCellStyle) throws Exception {
		if (trainingType != null) {
			ExcelUtil.addCell2Row(row, colIndex, trainingType.getReply(), intCellStyle, floatCellStyle);
			colIndex++;
		}
		if (nurseStatus != null) {
			ExcelUtil.addCell2Row(row, colIndex, nurseStatus.getReply(), intCellStyle, floatCellStyle);
			colIndex++;
		}
		if (teacher != null) {
			ExcelUtil.addCell2Row(row, colIndex, teacher.getReply() + "：" + teacher.getOther(), intCellStyle, floatCellStyle);
			colIndex++;
		}
		for (MedCustMasterElm master : masterList) {
			row.createCell(colIndex).setCellValue(master.getTitle());
			colIndex++;
			for (MedCustDetailElm detail : master.getDetailList()) {
				ExcelUtil.addCell2Row(row, colIndex, detail.getTopic(), intCellStyle, floatCellStyle);
				colIndex++;
				ExcelUtil.addCell2Row(row, colIndex, getReplyStr(detail.getReply()), intCellStyle, floatCellStyle);
				colIndex++;
				ExcelUtil.addCell2Row(row, colIndex, "Y".equals(detail.getReply2()) ? detail.getReply2a() + "/" + detail.getReply2b() + "/" + detail.getReply2c() : "", intCellStyle, floatCellStyle);
				colIndex++;
				ExcelUtil.addCell2Row(row, colIndex, getReplyStrInternal(detail.getReply3(), detail.getReply3a(), detail.getReply3b(), detail.getReply3c(), false), intCellStyle, floatCellStyle);
				colIndex++;
				ExcelUtil.addCell2Row(row, colIndex, getReplyStrInternal(detail.getReply4(), detail.getReply4a(), detail.getReply4b(), detail.getReply4c(), false), intCellStyle, floatCellStyle);
				colIndex++;
				ExcelUtil.addCell2Row(row, colIndex, detail.getMemo(), intCellStyle, floatCellStyle);
				colIndex++;
			}
		}
	}
}
