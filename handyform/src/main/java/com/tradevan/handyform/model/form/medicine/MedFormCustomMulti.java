package com.tradevan.handyform.model.form.medicine;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.tradevan.apcommon.util.StringUtil;
import com.tradevan.handyform.model.form.medicine.elem.MedCustDetailElm;
import com.tradevan.handyform.model.form.medicine.elem.MedCustMasterElm;
import com.tradevan.handyform.model.form.medicine.elem.MedCustOptionElm;

import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@JsonInclude(Include.NON_NULL)
public class MedFormCustomMulti extends BaseCustMedForm {
	private MedCustMasterElm master1;
	
	// 多筆維護區塊欄位定義
	private MedCustMasterElm master2;
	
	// 多筆維護區塊 子文件 資料
	private List<MedCustMasterElm> master2List = new ArrayList<MedCustMasterElm>();
	
	private List<MedCustMasterElm> master3toN;
	
	public MedCustMasterElm getMaster1() {
		return master1;
	}
	public void setMaster1(MedCustMasterElm master1) {
		this.master1 = master1;
	}
	public MedCustMasterElm getMaster2() {
		return master2;
	}
	public void setMaster2(MedCustMasterElm master2) {
		this.master2 = master2;
	}
	@JsonIgnore
	public List<MedCustMasterElm> getMaster2List() {
		return master2List;
	}
	@JsonIgnore
	public void addToMaster2List(MedCustMasterElm master2) {
		this.master2List.add(master2);
	}
	@JsonIgnore
	public void addToMaster2List(List<MedCustMasterElm> master2) {
		if (master2 != null) {
			this.master2List.addAll(master2);
		}
	}
	
	public List<MedCustMasterElm> getMaster3toN() {
		return master3toN;
	}
	public void setMaster3toN(List<MedCustMasterElm> master3toN) {
		this.master3toN = master3toN;
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
		ArrayList<EduMedFormReportBean> masterList = new ArrayList<EduMedFormReportBean>();
		
		EduMedFormReportBean reportBean = new EduMedFormReportBean();
		reportBean.setMaster1DS(new JRBeanCollectionDataSource(genMaster1Data()));
		reportBean.setMaster2DS(new JRBeanCollectionDataSource(genMaster2Data()));
		reportBean.setMaster3ToNDS(new JRBeanCollectionDataSource(genMaster3Data()));
		masterList.add(reportBean);
		return masterList;
	}
	
	private List<EduMedFormReportBean> genMaster1Data() {
		
		ArrayList<EduMedFormReportBean> retList = new ArrayList<EduMedFormReportBean>();
		
		EduMedFormReportBean master1Data = new EduMedFormReportBean();
		master1Data.setId(master1.getId()+"");
		master1Data.setTitle(master1.getTitle());
		master1Data.setSubTitle(master1.getSubTitle());		
		master1Data.setDetailDS(new JRBeanCollectionDataSource(genDetailDS(master1.getDetailList())));
		retList.add(master1Data);
		return retList;
	}
	
	private List<EduMedFormReportBean> genMaster2Data() {
		ArrayList<EduMedFormReportBean> retList = new ArrayList<EduMedFormReportBean>();
		
		for(MedCustMasterElm masterElem : getMaster2List()) {
			EduMedFormReportBean master2Data = new EduMedFormReportBean();
			master2Data.setId(master2.getId()+"");
			master2Data.setTitle(masterElem.getTitle());
			master2Data.setSubTitle(master2.getSubTitle());	
			master2Data.setDetailDS(new JRBeanCollectionDataSource(genDetailDS(masterElem.getDetailList())));
			master2Data.setFlow(masterElem.getFlow()); // 子表單流程
			master2Data.setComments(masterElem.getComments()); // 子表單各關卡審核意見
			retList.add(master2Data);
		}
		return retList;
	}
	
	private List<EduMedFormReportBean> genMaster3Data() {
		ArrayList<EduMedFormReportBean> masterList = new ArrayList<EduMedFormReportBean>();
		if (master3toN != null) {
			for(MedCustMasterElm master : master3toN) {
				EduMedFormReportBean masterData = new EduMedFormReportBean();
				masterData.setId(master.getId()+"");
				masterData.setTitle(master.getTitle());
				masterData.setSubTitle(master.getSubTitle());
				
				masterData.setDetailDS(new JRBeanCollectionDataSource(genDetailDS(master.getDetailList())));
				masterList.add(masterData);
			}
		}
		return masterList;
	}
	
	@JsonIgnore
	private List<EduMedFormReportBean> genDetailDS(List<MedCustDetailElm> detailList){
		ArrayList<EduMedFormReportBean> retDetailList = new ArrayList<EduMedFormReportBean>();
		
		for(MedCustDetailElm detailElem : detailList) {
			EduMedFormReportBean reportBean = new EduMedFormReportBean();
			reportBean.setType(detailElem.getType().name());
			
			reportBean.setTopic(detailElem.getTopic());
			reportBean.setReply(detailElem.getReply());
				
			genOptionsStr(reportBean,detailElem);
			retDetailList.add(reportBean);
		}
		return retDetailList;
	}
	
	@JsonIgnore
	private void genOptionsStr(EduMedFormReportBean reportBean,MedCustDetailElm detailElem) {
		if (("radio".equalsIgnoreCase(detailElem.getType().name())) || ("check".equalsIgnoreCase(detailElem.getType().name())) || 
			("combo".equalsIgnoreCase(detailElem.getType().name())))  {
			
			reportBean.setOptionBlankStr(genRadioOptionsStr(detailElem.getOptions(), detailElem.getType().name(), null, null, null));
			reportBean.setOptionStr(genRadioOptionsStr(detailElem.getOptions(), detailElem.getType().name(),detailElem.getReply(), detailElem.getMemo(), null));
		}
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
	
	@JsonIgnore
	private List<EduMedFormReportBean> genDetailDSForMaster2(MedCustMasterElm masterElem){

		ArrayList<EduMedFormReportBean> retDetailList = new ArrayList<EduMedFormReportBean>();
		
		EduMedFormReportBean reportBean = new EduMedFormReportBean();
		
		reportBean.setDetailDS(new JRBeanCollectionDataSource(genDetailDS(masterElem.getDetailList())));
		retDetailList.add(reportBean);
			
		return retDetailList;
	}
	
	@JsonIgnore
	@Override
	public boolean hasForReview() {
		if (master2 != null) {
			for (MedCustDetailElm detail : master2.getDetailList()) {
				if (detail.getForReview() != null && detail.getForReview()) {
					return true;
				}
			}
		}
		return false;
	}
}
