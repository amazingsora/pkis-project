package com.tradevan.handyform.model.form.medicine;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
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
import com.tradevan.handyform.model.form.medicine.elem.MedCustOptionElm;

import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@JsonInclude(Include.NON_NULL)
public class MedFormResidentAssess extends BaseCustMedForm {
	private List<MedCustMasterElm> masterList;

	public List<MedCustMasterElm> getMasterList() {
		return masterList;
	}
	public void setMasterList(List<MedCustMasterElm> masterList) {
		this.masterList = masterList;
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
	public BigDecimal sumCustFormScore() { // Cust表單-[初核]加總分數欄位(SF)給合計欄位(TF)
		return sumCustFormScoreInternal(masterList, 1);
	}
	
	@JsonIgnore
	@Override
	public BigDecimal sumCustFormScore2() { // Cust表單-[覆核]加總分數欄位(SF)給合計欄位(TF)
		return sumCustFormScoreInternal(masterList, 2);
	}
	
	@JsonIgnore
	@Override
	public BigDecimal calcScore() { // 受評人分數
		return calcScoreInternal(masterList, 2, null, null);
	}
	
	@JsonIgnore
	@Override
	public BigDecimal calcScore2() { // 受評人分數(初核)
		return calcScoreInternal(masterList, 1, null, null);
	}
	
	@JsonIgnore
	@Override
	public Integer calcSatisfaction() { // 受評人滿意度
		return calcSatisfactionInternal(masterList);
	}
	
	@JsonIgnore
	@Override
	public Integer calcSatisfaction2() { // 評量人滿意度
		return calcSatisfaction2Internal(masterList);
	}
	
	@JsonIgnore
	@Override
	public List<EduMedFormReportBean> getReportBeanList(){
		ArrayList<EduMedFormReportBean> masterList = new ArrayList<EduMedFormReportBean>();
		
		for(MedCustMasterElm master : getMasterList()) {
			EduMedFormReportBean masterData = new EduMedFormReportBean();
			masterData.setTitle(master.getTitle());
			masterData.setSubTitle(master.getSubTitle());
			
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
			reportBean.setType(detailElem.getType().name());
			reportBean.setIsEval(detailElem.getIsEval());
			
			if (detailElem.getIsEval() == null || !detailElem.getIsEval()) {
				reportBean.setTopicStr(detailElem.getTopic());
				reportBean.setReply(detailElem.getReply());
				reportBean.setMemo(detailElem.getMemo());
				
				genOptionsStr(reportBean,detailElem);				
			
			}else {//多項評量項目
				
				if (CollectionUtils.isNotEmpty(detailElem.getEvalItems())) {
					reportBean.setEvalItemDS(new JRBeanCollectionDataSource(genEvilItemDS(detailElem)));
				}else {
					reportBean.setEvalItemDS(null);
				}
			}
			retDetailList.add(reportBean);
		}
		return retDetailList;
	}
	
	
	@JsonIgnore
	private void genOptionsStr(EduMedFormReportBean reportBean,MedCustDetailElm detailElem) {
		if (("radio".equalsIgnoreCase(detailElem.getType().name())) || ("check".equalsIgnoreCase(detailElem.getType().name())) || 
			("combo".equalsIgnoreCase(detailElem.getType().name())))  {
			
			reportBean.setOptionBlankStr(genRadioOptionsStr(detailElem.getOptions(), detailElem.getType().name(), null));
			reportBean.setOptionStr(genRadioOptionsStr(detailElem.getOptions(), detailElem.getType().name(), detailElem.getReply()));
		}
	}
	
	@JsonIgnore
	private List<EduMedFormReportBean> genEvilItemDS(MedCustDetailElm detailElem){
		ArrayList<EduMedFormReportBean> retEvalItemList = new ArrayList<EduMedFormReportBean>();
		
		for(MedCustEvalElm evalItem : detailElem.getEvalItems()) {
			EduMedFormReportBean evalItemData = new EduMedFormReportBean();
			evalItemData.setTopic(detailElem.getTopic());
			evalItemData.setTopicStr(evalItem.getValue());
			evalItemData.setReply(evalItem.getReply());
			evalItemData.setReply2(evalItem.getReply2());
			evalItemData.setOptionBlankStr(genRadioOptionsStr(detailElem.getOptions(),"radio", null));
			evalItemData.setOptionStr(genRadioOptionsStr(detailElem.getOptions(), "radio", evalItem.getReply()));
			retEvalItemList.add(evalItemData);
		}
		return retEvalItemList;
	}
		
	@JsonIgnore
	private String genRadioOptionsStr(List<MedCustOptionElm> optionsList , String type , String reply) {
		if ("check".equalsIgnoreCase(type)) {
			return genRadioOptionsStrInternal(optionsList, reply , CHECK, CHECK_BLANK);
		}else {
			return genRadioOptionsStrInternal(optionsList, reply , RADIO, RADIO_BLANK);
		}
	}
	
	@JsonIgnore
	private String genRadioOptionsStrInternal(List<MedCustOptionElm> optionsList , String reply,String checkStr, String blankStr) {
		StringBuffer optionStr = new StringBuffer();
		for(MedCustOptionElm option : optionsList) {
			if (! StringUtil.isBlank2(option.getValue())) {
				String replyOpt = (option.getValue().equals(reply) || StringUtil.isValuesContains(reply, option.getValue())) ? checkStr: blankStr;
				optionStr.append(replyOpt + " " + option.getValue()+"  ");
			}
		}
		return optionStr.toString();
	}
	
	@JsonIgnore
	@Override
	public void addExcelColsTitle(Row row) throws Exception {
		int colIndex = row.getLastCellNum();
		for (MedCustMasterElm master : masterList) {
			row.createCell(colIndex).setCellValue("主標題");
			colIndex++;
			row.createCell(colIndex).setCellValue("次標題");
			colIndex++;
			if (master.getIsEval() != null && master.getIsEval()) {
				for (MedCustDetailElm detail : master.getDetailList()) {
					if (detail.getIsEval() != null && detail.getIsEval()) {
						for (MedCustEvalElm elm : detail.getEvalItems()) {
							row.createCell(colIndex).setCellValue("(初核)" + elm.getValue());
							colIndex++;
							row.createCell(colIndex).setCellValue("(複核)" + elm.getValue());
							colIndex++;
						}
					}
					else {
						row.createCell(colIndex).setCellValue("(初核)" + detail.getTopic());
						colIndex++;
						row.createCell(colIndex).setCellValue("(複核)" + detail.getTopic());
						colIndex++;
					}
				}
			}
			else {
				for (MedCustDetailElm detail : master.getDetailList()) {
					if (detail.getIsEval() != null && detail.getIsEval()) {
						for (MedCustEvalElm elm : detail.getEvalItems()) {
							row.createCell(colIndex).setCellValue(elm.getValue());
							colIndex++;
						}
					}
					else {
						row.createCell(colIndex).setCellValue(detail.getTopic());
						colIndex++;
					}
				}
			}
		}
	}
	
	@JsonIgnore
	@Override
	public void addExcelCells(Row row, int colIndex, CellStyle intCellStyle, CellStyle floatCellStyle) throws Exception {
		for (MedCustMasterElm master : masterList) {
			row.createCell(colIndex).setCellValue(master.getTitle());
			colIndex++;
			row.createCell(colIndex).setCellValue(master.getSubTitle());
			colIndex++;
			if (master.getIsEval() != null && master.getIsEval()) {
				for (MedCustDetailElm detail : master.getDetailList()) {
					if (detail.getIsEval() != null && detail.getIsEval()) {
						for (MedCustEvalElm eval : detail.getEvalItems()) {
							ExcelUtil.addCell2Row(row, colIndex, eval.getReply(), intCellStyle, floatCellStyle);
							colIndex++;
							ExcelUtil.addCell2Row(row, colIndex, eval.getReply2(), intCellStyle, floatCellStyle);
							colIndex++;
						}
					}
					else {
						ExcelUtil.addCell2Row(row, colIndex, detail.getReply(), intCellStyle, floatCellStyle);
						colIndex++;
						ExcelUtil.addCell2Row(row, colIndex, detail.getReply2(), intCellStyle, floatCellStyle);
						colIndex++;
					}
				}
			}
			else {
				for (MedCustDetailElm detail : master.getDetailList()) {
					if (detail.getIsEval() != null && detail.getIsEval()) {
						for (MedCustEvalElm eval : detail.getEvalItems()) {
							ExcelUtil.addCell2Row(row, colIndex, eval.getReply(), intCellStyle, floatCellStyle);
							colIndex++;
						}
					}
					else {
						ExcelUtil.addCell2Row(row, colIndex, detail.getReply(), intCellStyle, floatCellStyle);
						colIndex++;
					}
				}
			}
		}
	}
	
	@JsonIgnore
	@Override
	public boolean hasForReview() {
		for (MedCustMasterElm master : masterList) {
			for (MedCustDetailElm detail : master.getDetailList()) {
				if (detail.getForReview() != null && detail.getForReview()) {
					return true;
				}
			}
		}
		return false;
	}
}
