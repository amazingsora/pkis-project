package com.tradevan.handyform.model.form.medicine;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.tradevan.apcommon.util.ExcelUtil;
import com.tradevan.apcommon.util.ImageUtil;
import com.tradevan.apcommon.util.StringUtil;
import com.tradevan.handyform.enums.MedCustDetailType;
import com.tradevan.handyform.model.form.medicine.elem.MedCustDetailElm;
import com.tradevan.handyform.model.form.medicine.elem.MedCustEvalElm;
import com.tradevan.handyform.model.form.medicine.elem.MedCustMasterElm;
import com.tradevan.handyform.model.form.medicine.elem.MedCustOptionElm;

import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@JsonInclude(Include.NON_NULL)
public class MedFormCustomSingle extends BaseCustMedForm {
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
	public BigDecimal sumCustFormScore() { // Cust表單-加總分數欄位(SF)給合計欄位(TF)
		return sumCustFormScoreInternal(masterList, 1);
	}
	
	@JsonIgnore
	@Override
	public BigDecimal calcScore() { // 受評人分數
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
	public String getPassStatus() { // Cust表單結果-通過狀態
		return getPassStatusInternal(masterList, 1, null, null);
	}
	
	@JsonIgnore
	public BigDecimal calcEvalScore(Integer masterId, Integer detailId) { // Cust表單結果-多項評量分數
		return calcScoreInternal(masterList, 1, masterId, detailId, true);
	}
	
	@JsonIgnore
	public String getEvalPassStatus(Integer masterId, Integer detailId) { // Cust表單結果-多項評量通過狀態
		return getPassStatusInternal(masterList, 1, masterId, detailId, true);
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
			masterList.add(masterData);
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
				if ( !"familyTree".equalsIgnoreCase(detailElem.getType().name()) ) {
					genOptionsStr(reportBean,detailElem);
				}else {
					//familyTree 須特殊處理
					reportBean.setFamilyTreeImage(genFamilyTreeData(detailElem));
				}
			
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
			
			reportBean.setOptionBlankStr(genRadioOptionsStr(detailElem.getOptions(), detailElem.getType().name(), null, null, null));
			reportBean.setOptionStr(genRadioOptionsStr(detailElem.getOptions(), detailElem.getType().name(), detailElem.getReply(), detailElem.getMemo(), null));
		}
	}
	
	@JsonIgnore
	private List<EduMedFormReportBean> genEvilItemDS(MedCustDetailElm detailElem){
		ArrayList<EduMedFormReportBean> retEvalItemList = new ArrayList<EduMedFormReportBean>();
		
		for(MedCustEvalElm evalItem : detailElem.getEvalItems()) {
			EduMedFormReportBean evalItemData = new EduMedFormReportBean();
			
			evalItemData.setTopicStr(evalItem.getValue());
			if (evalItem.getIsNoScoring() == null || !evalItem.getIsNoScoring()) {
				evalItemData.setReply(evalItem.getReply());
				evalItemData.setOptionBlankStr(genRadioOptionsStr(detailElem.getOptions(),"radio", null, null, null));
				evalItemData.setOptionStr(genRadioOptionsStr(detailElem.getOptions(),"radio", evalItem.getReply(), evalItem.getMemo(), evalItem.getMemoMap()));
			}
			retEvalItemList.add(evalItemData);
		}
		return retEvalItemList;
	}
	
	@JsonIgnore
	private String genRadioOptionsStr(List<MedCustOptionElm> optionsList, String type, String reply, String memo, Map<String, String> memoMap) {
		if ("check".equalsIgnoreCase(type)) {
			return genRadioOptionsStrInternal(optionsList, reply, memo, memoMap, CHECK, CHECK_BLANK);
		}else {
			return genRadioOptionsStrInternal(optionsList, reply, memo, memoMap, RADIO, RADIO_BLANK);
		}
	}
	
	
	@JsonIgnore
	private String genRadioOptionsStrInternal(List<MedCustOptionElm> optionsList , String reply, String memo, Map<String, String> memoMap, String checkStr, String blankStr) {
		StringBuffer optionStr = new StringBuffer();
		for(MedCustOptionElm option : optionsList) {
			if (! StringUtil.isBlank2(option.getValue())) {
				String replyOpt = (option.getValue().equals(reply) || StringUtil.isValuesContains(reply, option.getValue())) ? checkStr: blankStr;
				optionStr.append(replyOpt + " " + option.getValue());
				if (option.getWithOthers() != null && option.getWithOthers()) {
					if (memoMap != null && memoMap.get(option.getValue()) != null) {
						optionStr.append("：" + memoMap.get(option.getValue()) + " ");
					}
					else if (StringUtil.isNotBlank(option.getMemo())) {
						optionStr.append("：" + option.getMemo() + " ");
					}
					else if (memo != null) {
						optionStr.append("：" + memo + " ");
					}
					else {
						optionStr.append("：          ");
					}
				}
				else {
					optionStr.append(" ");
				}
				optionStr.append(" ");
			}	
		}
		return optionStr.toString();
	}
	
	private Image genFamilyTreeData(MedCustDetailElm detailElem) {
		if (StringUtils.isBlank(detailElem.getReply())) {
			return null;
		}
		try {
			BufferedImage image = ImageUtil.convertDataUrlToImage(ImageUtil.decompress(detailElem.getReply()));
			
			return image.getScaledInstance(300, 300,Image.SCALE_DEFAULT);
		}catch(Exception e) {
			return null;
		}
		
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
			for (MedCustDetailElm detail : master.getDetailList()) {
				if (detail.getIsEval() != null && detail.getIsEval()) {
					for (MedCustEvalElm elm : detail.getEvalItems()) {
						row.createCell(colIndex).setCellValue(elm.getValue());
						colIndex++;
					}
				}
				else if (detail.getType() != MedCustDetailType.familyTree) {
					row.createCell(colIndex).setCellValue(detail.getTopic());
					colIndex++;
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
			for (MedCustDetailElm detail : master.getDetailList()) {
				if (detail.getIsEval() != null && detail.getIsEval()) {
					for (MedCustEvalElm eval : detail.getEvalItems()) {
						String reply = eval.getReply();
						if (StringUtil.isNotBlank(reply) && detail.getOptions() != null) {
							for (MedCustOptionElm option : detail.getOptions()) {
								if (option.getWithOthers() != null && option.getWithOthers()) {
									if (option.getValue().equals(reply)) {
										reply += ("：" + eval.getMemo());
										break;
									}
									else if (StringUtil.isValuesContains(reply, option.getValue())) {
										int idx = reply.indexOf(option.getValue()) + option.getValue().length();
										reply = reply.substring(0, idx) + "：" + eval.getMemo() + reply.substring(idx);
									}
								}
							}
						}
						ExcelUtil.addCell2Row(row, colIndex, reply, intCellStyle, floatCellStyle);
						colIndex++;
					}
				}
				else if (detail.getType() != MedCustDetailType.familyTree) {
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
