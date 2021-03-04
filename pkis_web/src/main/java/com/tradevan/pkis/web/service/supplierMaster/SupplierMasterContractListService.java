package com.tradevan.pkis.web.service.supplierMaster;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tradevan.handyflow.bean.DocStateBean;
import com.tradevan.handyflow.service.FlowQueryService;
import com.tradevan.mapper.pkis.dao.ContractmasterMapper;
import com.tradevan.mapper.pkis.dao.DocstateMapper;
import com.tradevan.mapper.pkis.dao.SuppliermasterMapper;
import com.tradevan.mapper.pkis.model.Contractmaster;
import com.tradevan.mapper.pkis.model.Docstate;
import com.tradevan.mapper.pkis.model.Suppliermaster;
import com.tradevan.mapper.xauth.dao.XauthSysCodeMapper;
import com.tradevan.mapper.xauth.model.XauthSysCode;
import com.tradevan.pkis.web.service.common.CommonService;
import com.tradevan.pkis.web.util.ElasticSearchUtil;
import com.tradevan.xauthframework.common.utils.JsonUtils;
import com.tradevan.xauthframework.core.common.LocaleMessage;
import com.tradevan.xauthframework.core.security.UserInfo;
import com.tradevan.xauthframework.dao.bean.GridResult;
import com.tradevan.xauthframework.web.service.DefaultService;

@Service("SupplierMasterContractListService")
@Transactional(rollbackFor = Exception.class)
public class SupplierMasterContractListService extends DefaultService {
	
	@Autowired
	SuppliermasterMapper suppliermasterMapper;
	
	@Autowired
	ContractmasterMapper contractmasterMapper;
	
	@Autowired
	XauthSysCodeMapper xauthSysCodeMapper;
	
	@Autowired
	DocstateMapper docstateMapper;
	
	@Autowired
	private FlowQueryService flowQueryService;
	
	@Autowired
	CommonService commonService;

	/**
	 * 合約查詢
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public GridResult selectContractList(Map<String, Object> params) throws Exception {
		logger.info("params == " + params);
		
		GridResult gridResult = null;
		UserInfo userInfo = userContext.getCurrentUser();
		
		//供應商只能看到自己的合約流程
		QueryWrapper<Suppliermaster> suppliermasterQueryWrapper = new QueryWrapper<Suppliermaster>();
		suppliermasterQueryWrapper.eq("SUPPLIERID", userInfo.getUserId());
		List<Suppliermaster> suppliermasterList = suppliermasterMapper.selectList(suppliermasterQueryWrapper);
		Suppliermaster suppliermaster = suppliermasterList.get(0);
		
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		StringBuffer esSql = new StringBuffer();
		String beginColumnName = "data.docdetail.resultdata";
		String year = MapUtils.getString(params, "year");
		String module = MapUtils.getString(params, "module");
		String moduleName = MapUtils.getString(params, "moduleName");
		String supplierId = suppliermaster.getSupplierid();
		String indexName = "";
		String nowTaskId = "";
		String nowUserId = userInfo.getUserId();
		String flowStatus = "";
		
		List<XauthSysCode> deptCodeList = getDeptSysCode(); // 課別資料
		
		if(StringUtils.isNotBlank(year) && StringUtils.isNotBlank(module)) {
			// 組Elasticsearch條件
			indexName = year + "_" + module + "_qryrec_doc";
			esSql.append(" where " + beginColumnName + "." + LocaleMessage.getMsg("contract.field.year") + "=" + year);
			esSql.append("   and " + beginColumnName + "." + LocaleMessage.getMsg("contract.field.module") + "=" + "'" + moduleName + "'");
			if(StringUtils.isNotBlank(supplierId)) {
				esSql.append("and " + beginColumnName + "." + LocaleMessage.getMsg("suppliter.field.supplierid") + "=" + "'" + supplierId + "'");
			}
			// 至Elasticsearch依條件查詢
			List<String> resultJson = ElasticSearchUtil.searchForES(indexName, beginColumnName, esSql.toString());
			if(resultJson != null && resultJson.size() > 0) {
				// 將查詢後的json放入grid中
				for(String json : resultJson) {
					Map<String, Object> map = JsonUtils.json2Object(json, Map.class);
					List<Map<String, Object>> dataMapList = (List<Map<String, Object>>) map.get("data");
					for(Map<String, Object> docdetailMap : dataMapList) {
						List<Map<String, Object>> docdetailMapList = (List<Map<String, Object>>) docdetailMap.get("docdetail");
						for(Map<String, Object> resultdataMap : docdetailMapList) {
							Map<String, Object> resultMap = (Map<String, Object>) resultdataMap.get("resultdata");
							resultMap.put("isNowUser", "N");
							resultMap.put("indexName", indexName);
							String contractNo = MapUtils.getString(resultMap, "合約編碼");
							// 取得課別資料中文名稱
							for(XauthSysCode deptCode : deptCodeList) {
								if(StringUtils.equals(deptCode.getCode(), MapUtils.getString(resultMap, LocaleMessage.getMsg("contract.field.section")))) {
									resultMap.put("deptCName", deptCode.getCname());
									resultMap.put("deptNo", deptCode.getCode());
								}
							}
							
							String dbNowTaskId = "";
							// 從oracle DB DOCSTATE 取得flow 狀態
							QueryWrapper<Docstate> docstateWrapper = new QueryWrapper<Docstate>();
							docstateWrapper.eq("APPLYNO", contractNo);
							List<Docstate> docstateList = docstateMapper.selectList(docstateWrapper);
							if(docstateList.size() > 0) {
								dbNowTaskId = docstateList.get(0).getNowtaskid();
								flowStatus = docstateList.get(0).getFlowstatus();
								if(StringUtils.equals(flowStatus, "PROCESS")) {
									flowStatus = "審核中";
								}else if(StringUtils.equals(flowStatus, "END")) {
									flowStatus = "歸檔";
								}else{
									flowStatus = "作廢";
								}
								resultMap.put(LocaleMessage.getMsg("contract.field.docstatus"), flowStatus);
							}
							
							//判斷使用者是否為合約當前簽審人員
							Contractmaster contractmaster = commonService.getContractmasterData(contractNo);
							if(contractmaster != null) {
								String flowId = contractmaster.getFlowid();
								resultMap.put("flowId", flowId);
								// Act & Assert
								List<DocStateBean> list = flowQueryService.fetchToDoListBy(nowUserId, flowId, APP_ID, true, false);
								for(DocStateBean docStateBean : list) {
									//判斷是否刪檔
									nowTaskId = docStateBean.getNowTaskId();
									if(StringUtils.equals(docStateBean.getApplyNo(), contractNo)) {
										resultMap.put("isNowUser", "Y");
									}
								}
							}
							
							// 轉換送審日期格式
							SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
							String updtime = MapUtils.getString(resultMap, LocaleMessage.getMsg("contract.field.updtime"));
							resultMap.put(LocaleMessage.getMsg("contract.field.updtime"), sdf.format(sdf.parse(updtime)));
							
							//供應商合約查詢塞選 不可看到 編輯中合約以及前幾合約關卡
							if(!StringUtils.equals(userInfo.getUserCname(), MapUtils.getString(resultMap, LocaleMessage.getMsg("contract.field.contractor")))) {
								if(StringUtils.isNoneBlank(dbNowTaskId) && !StringUtils.equals("Task1", dbNowTaskId)) {
									if(StringUtils.equals(dbNowTaskId, nowTaskId) || !StringUtils.equals("Task2", dbNowTaskId)) {
										resultList.add(resultMap);
									}
								}
							} 
						}
					}
				}
			}
		}
		
		// 排序及分頁
		gridResult = this.grid(params);
		ElasticSearchUtil.sortPagination(gridResult, resultList, params);
		
		return gridResult;
	}
	
	/**
	 * 課別
	 * @return
	 * @throws Exception
	 */
	public List<XauthSysCode> getDeptSysCode() throws Exception{
		QueryWrapper<XauthSysCode> queryWrapper = new QueryWrapper<XauthSysCode>();
		queryWrapper.eq("APP_ID", "APPKIS");
		queryWrapper.eq("GP", "DEPT_CODE");
		List<XauthSysCode> xauthSysCodeList = xauthSysCodeMapper.selectList(queryWrapper);
		
		
//		List<XauthSysCode> xauthSysCodeList = commonService.getSysCodeDatasByGp("DEPT_CODE");
//		logger.info("xauthSysCodeList ==="+xauthSysCodeList);
		return xauthSysCodeList;
	}
}
