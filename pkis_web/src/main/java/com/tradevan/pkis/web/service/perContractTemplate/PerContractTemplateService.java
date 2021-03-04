package com.tradevan.pkis.web.service.perContractTemplate;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.gson.Gson;
import com.tradevan.mapper.pkis.dao.CodelistMapper;
import com.tradevan.mapper.pkis.dao.DesignermgrMapper;
import com.tradevan.mapper.pkis.dao.PercontracttemplateMapper;
import com.tradevan.mapper.pkis.model.Codelist;
import com.tradevan.mapper.pkis.model.Designermgr;
import com.tradevan.mapper.pkis.model.Percontracttemplate;
import com.tradevan.mapper.xauth.dao.XauthSysCodeMapper;
import com.tradevan.mapper.xauth.model.XauthSysCode;
import com.tradevan.pkis.web.service.common.CommonService;
import com.tradevan.pkis.web.util.JsonUtil;
import com.tradevan.xauthframework.common.bean.ProcessResult;
import com.tradevan.xauthframework.common.utils.JsonUtils;
import com.tradevan.xauthframework.core.common.LocaleMessage;
import com.tradevan.xauthframework.core.enums.MSG_KEY;
import com.tradevan.xauthframework.core.enums.USER_TYPE;
import com.tradevan.xauthframework.core.security.UserInfo;
import com.tradevan.xauthframework.dao.bean.GridResult;
import com.tradevan.xauthframework.web.service.DefaultService;

@Service("PerContractTemplateService")
@Transactional(rollbackFor = Exception.class)
public class PerContractTemplateService extends DefaultService {
	
	@Autowired
	PercontracttemplateMapper percontracttemplateMapper;
	
	@Autowired
	DesignermgrMapper designermgrMapper;
	
	@Autowired
	CodelistMapper codelistMapper;
	
	@Autowired
	XauthSysCodeMapper xauthSysCodeMapper;
	
	@Autowired
	CommonService commonService;

	/**
	 * 新增資料
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public ProcessResult insertData(Map<String, Object> params) throws Exception {
		ProcessResult result = new ProcessResult();
		Percontracttemplate percontracttemplate = null;
		String module = MapUtils.getString(params, "module");
		String disp = MapUtils.getString(params, "disp");
		String dispName = MapUtils.getString(params, "dispName");
		String templatename = MapUtils.getString(params, "templatename");
		String enabled = MapUtils.getString(params, "enabled");
		Designermgr designermgr = null;
		Date ts = new Timestamp(new Date().getTime());
		String dataId = getTemplateDataId();
		String year = "";
		String version = "";
		String json = "";
		
		if(params != null && params.size() > 0) {
			ProcessResult checkRs = checkInsertData(params);
			if(StringUtils.equals(checkRs.getStatus(), ProcessResult.NG)) {
				result.setStatus(checkRs.getStatus());
				result.setMessages(checkRs.getMessages());
				return result;
			}
			// 從DESIGNERMGR取資料
			designermgr = getTemplateData(module, dispName);
			if(designermgr != null && StringUtils.isNotBlank(designermgr.getJson())) {
				year = designermgr.getYear();
				version = designermgr.getVersion();
				json = designermgr.getJson();
				// 塞入resultdata
				json = setPerResultData(json, year, version, params);
				json = JsonUtil.jsonSetValueByKey(json, "todo", "新建");
				json = setDefaultValue(json);
				
				// 寫入資料庫PERCONTRACTTEMPLATE
				percontracttemplate = new Percontracttemplate();
				percontracttemplate.setDataid(dataId);
				percontracttemplate.setFlowid(disp);
				percontracttemplate.setJson(json);
				percontracttemplate.setModule(module);
				percontracttemplate.setTemplatename(templatename);
				percontracttemplate.setPrdtime(StringUtils.equals("1", enabled) ? ts : null);
				percontracttemplate.setDocver(designermgr.getDocver());
				percontracttemplate.setCreateuser(getCreOrUpdUser(null));
				percontracttemplate.setCreatedate(ts);
				percontracttemplate.setUpdateuser(getCreOrUpdUser(null));
				percontracttemplate.setUpdatedate(ts);
				percontracttemplateMapper.insert(percontracttemplate);
				
				result.setStatus(ProcessResult.OK);
				result.addMessage(MSG_KEY.INSERT_OK.getMessage());
				result.addResult("templateDataId", dataId);
				result.addResult("dataType", "0.前言");
			} else {
				result.setStatus(ProcessResult.NG);
				result.addMessage("請先建立合約範本");
			}
			
		} else {
			result.setStatus(ProcessResult.NG);
			result.addMessage(MSG_KEY.PARAMS_EMPTY.getMessage());
		}
		
		return result;
	}
	
	public String setDefaultValue(String json) {
		json = JsonUtil.setDefaultValue(json);
		return json;
	}
	
	/**
	 * 檢核新增個人範本新增欄位
	 * @param params
	 * @return
	 */
	public ProcessResult checkInsertData(Map<String, Object> params) {
		ProcessResult result = new ProcessResult();
		result.setStatus(ProcessResult.NG);
		try {
			String module = MapUtils.getString(params, "module");
			String disp = MapUtils.getString(params, "disp");
			
			if(StringUtils.isBlank(module)) {
				result.addMessage("請選擇合約模式");
				return result;
			}
			
			if(StringUtils.isBlank(disp)) {
				result.addMessage("請選擇合約範本");
				return result;
			}
			
		} catch(Exception e) {
			logger.error("checkInsertData exception",e , e);
			result.addMessage(MSG_KEY.PARAMS_EMPTY.getMessage());
			return result;
		}
		result.setStatus(ProcessResult.OK);
		return result;
	}
	
	/**
	 * 從DESIGNERMGR取資料
	 * @param module
	 * @param dispName
	 * @return
	 */
	public Designermgr getTemplateData(String module, String dispName) throws Exception {
		Designermgr result = null;
		QueryWrapper<Designermgr> designermgrWrapper = new QueryWrapper<Designermgr>();
		designermgrWrapper.eq("MODULE", module);
		if(StringUtils.equals("SC", module)) {
			designermgrWrapper.eq("DISP", dispName);
		}
		designermgrWrapper.isNull("DROPTIME");
		designermgrWrapper.isNotNull("PRDTIME");
		designermgrWrapper.orderByDesc("PRDTIME");
		List<Designermgr> designermgrList = designermgrMapper.selectList(designermgrWrapper);
		if(designermgrList != null && designermgrList.size() > 0) {
			result = designermgrList.get(0);
		}
		
		return result;
	}
	
	/**
	 * 組個人範本編號
	 * @param year
	 * @return
	 * @throws Exception
	 */
	public String getTemplateDataId()  throws Exception {
		String result = "";
		String perDataId = "TLOGR";
		String year = new SimpleDateFormat("yyyy").format(new Date());
		
		Codelist codelist = null;
		QueryWrapper<Codelist> codelistWrapper = new QueryWrapper<Codelist>();
		codelistWrapper.eq("CLASSTYPE", perDataId);
		codelistWrapper.eq("YEARS", year);
		List<Codelist> codes = codelistMapper.selectList(codelistWrapper);
		if(codes != null && codes.size() > 0) {
			codelist = codes.get(0);
			codelist.setAnumber(codelist.getAnumber() + 1);
			codelistMapper.update(codelist, codelistWrapper);
		} else {
			codelist = new Codelist();
			codelist.setSys("SEQUENCE");
			codelist.setClasstype(perDataId);
			codelist.setYears(year);
			codelist.setAnumber(1);
			codelist.setNote("序號");
			codelistMapper.insert(codelist);
		}
		result = perDataId + year + StringUtils.leftPad(String.valueOf(codelist.getAnumber()), 4, "0");
		
		return result;
	}
	
	/**
	 * 取得個人合約範本內容
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> getDetailData(Map<String, Object> params) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		String templateDataId = MapUtils.getString(params, "templateDataId");
		String json = "";
		QueryWrapper<Percontracttemplate> templateWrapper = new QueryWrapper<Percontracttemplate>();
		Percontracttemplate percontracttemplate = null;
		
		if(StringUtils.isNotBlank(templateDataId)) {
			templateWrapper.eq("DATAID", templateDataId);
			List<Percontracttemplate> templateList = percontracttemplateMapper.selectList(templateWrapper);
			if(templateList != null && templateList.size() > 0) {
				percontracttemplate = templateList.get(0);
				json = percontracttemplate.getJson();
				if(StringUtils.isBlank(json)) {
					jsonMap.put("data", "");
					resultMap.put("rtnCode", -1);
					resultMap.put("jsonData", "");
					resultMap.put("message", "查無合約範本");
					return resultMap;
				} else {
					jsonMap.put("data", json);
					resultMap.put("rtnCode", 0);
					resultMap.put("jsonData", jsonMap);
					resultMap.put("message", "建立範本合約成功");
				}
			} else {
				jsonMap.put("data", "");
				resultMap.put("rtnCode", -1);
				resultMap.put("jsonData", "");
				resultMap.put("message", "查無合約範本");
				return resultMap;
			}
		} else {
			jsonMap.put("data", "");
			resultMap.put("rtnCode", -1);
			resultMap.put("jsonData", "");
			resultMap.put("message", "請先新建個人合約範本");
			return resultMap;
		}
		
		return resultMap;
	}
	
	/**
	 * 儲存修改後合約範本內容
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> saveTempJson(Map<String, Object> params) throws Exception {
		Map<String, Object> result = new HashMap<String, Object>();
		String templateDataId = MapUtils.getString(params, "templateDataId");
		String json = new Gson().toJson(params.get("data"), Map.class);
		String type = MapUtils.getString(params, "type");
		QueryWrapper<Percontracttemplate> templateWrapper = new QueryWrapper<Percontracttemplate>();
		Percontracttemplate percontracttemplate = null;
		Date ts = new Timestamp(new Date().getTime());
		
		if(StringUtils.isNotBlank(templateDataId)) {
			templateWrapper.eq("DATAID", templateDataId);
			List<Percontracttemplate> templateList = percontracttemplateMapper.selectList(templateWrapper);
			if(templateList != null && templateList.size() > 0) {
				json = JsonUtil.jsonSetValueByKey(json, "todo", type);
				percontracttemplate = templateList.get(0);
				if(StringUtils.isNotBlank(json)) {
					percontracttemplate.setJson(json);
					percontracttemplate.setUpdateuser(getCreOrUpdUser(null));
					percontracttemplate.setUpdatedate(ts);
					percontracttemplateMapper.update(percontracttemplate, templateWrapper);
					
				} else {
					result.put("rtnCode", -1);
					result.put("message", "查無資料，請聯絡系統管理員");
					return result;
				}
			} else {
				result.put("rtnCode", -1);
				result.put("message", "查無此合約範本");
				return result;
			}
		}
		result.put("rtnCode", 0);
		result.put("message", "暫存成功!");
		result.put("model", "perConTemplate");
		
		return result;
	}
	
	/**
	 * 查詢個人合約範本
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public GridResult queryPercontracttemplate(Map<String, Object> params) throws Exception {
		GridResult result = this.grid(params);
		String module = MapUtils.getString(params, "module");
		String disp = MapUtils.getString(params, "disp");
		UserInfo userInfo = userContext.getCurrentUser();
		Map<String, Object> objParams = new HashMap<String, Object>();
		objParams.put("module", module);
		objParams.put("flowid", disp);
		if(!StringUtils.equals(userInfo.getUserType(), USER_TYPE.SYS_ADMIN.getCode())) {
			objParams.put("createuser", userInfo.getIdenId() + ":" + userInfo.getUserId());
		}
		commonDao.query(result, "com.tradevan.mapper.contract.dao.ContractMapper.selectPerConTeplate", objParams);
		
		return result;
	}
	
	/**
	 * 取得編輯單一資料
	 * @param params
	 * @return
	 */
	public Percontracttemplate getPerConTemplateData(String templateDataId) throws Exception {
		Percontracttemplate result = null;
		QueryWrapper<Percontracttemplate> templateWrapper = new QueryWrapper<Percontracttemplate>();
		templateWrapper.eq("DATAID", templateDataId);
		List<Percontracttemplate> dataList = percontracttemplateMapper.selectList(templateWrapper);
		if(dataList != null && dataList.size() > 0) {
			result = dataList.get(0);
		}
		
		return result;
	}
	
	/**
	 * 設定啟用
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public ProcessResult setBeginVal(Map<String, Object> params) throws Exception {
		ProcessResult result = new ProcessResult();
		Percontracttemplate percontracttemplate = null;
		String templateDataId = MapUtils.getString(params, "templateDataId");
		Date ts = new Timestamp(new Date().getTime());
		if(params != null && params.size() > 0) {
			percontracttemplate = getPerConTemplateData(templateDataId);
			if(percontracttemplate != null) {
				percontracttemplate.setPrdtime(ts);
				percontracttemplate.setUpdateuser(getCreOrUpdUser(null));
				percontracttemplate.setUpdatedate(ts);
				percontracttemplateMapper.updateById(percontracttemplate);
				
				result.setStatus(ProcessResult.OK);
				result.addMessage(MSG_KEY.UPDATE_OK.getMessage());
			}
		} else {
			result.setStatus(ProcessResult.NG);
			result.addMessage(MSG_KEY.PARAMS_EMPTY.getMessage());
			return result;
		}
		
		return result;
	}
	
	/**
	 * 設定停用
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public ProcessResult setStopVal(Map<String, Object> params) throws Exception {
		ProcessResult result = new ProcessResult();
		Percontracttemplate percontracttemplate = null;
		String templateDataId = MapUtils.getString(params, "templateDataId");
		Date ts = new Timestamp(new Date().getTime());
		if(params != null && params.size() > 0) {
			percontracttemplate = getPerConTemplateData(templateDataId);
			if(percontracttemplate != null) {
				percontracttemplate.setDroptime(ts);
				percontracttemplate.setUpdateuser(getCreOrUpdUser(null));
				percontracttemplate.setUpdatedate(ts);
				percontracttemplateMapper.updateById(percontracttemplate);
				
				result.setStatus(ProcessResult.OK);
				result.addMessage(MSG_KEY.UPDATE_OK.getMessage());
			}
		} else {
			result.setStatus(ProcessResult.NG);
			result.addMessage(MSG_KEY.PARAMS_EMPTY.getMessage());
			return result;
		}
		
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public String setPerResultData(String json, String year, String version, Map<String, Object> params) throws Exception {
		String result = "";
		Map<String, Object> paramsData = JsonUtils.json2Object(json, Map.class, false);
		Map<String, Object> detailMap = new HashMap<String, Object>();
		Map<String, Object> resultDataMap = new HashMap<String, Object>();	
		String module = MapUtils.getString(params, "module");
		String dispName = MapUtils.getString(params, "dispName");
		String moduleName = "";

		XauthSysCode xauthSysCode = commonService.getSysCodeData("CONTRACT_MODE_CODE", module);
		if(xauthSysCode != null ) {
			moduleName = xauthSysCode.getCname();
			detailMap.put(LocaleMessage.getMsg("contract.field.templateyear"), year);
			detailMap.put(LocaleMessage.getMsg("contract.field.module"), moduleName);
			detailMap.put(LocaleMessage.getMsg("contract.field.type"), dispName);
			detailMap.put(LocaleMessage.getMsg("contract.field.version"), version);
			
			resultDataMap.put("segmentation", "");
			resultDataMap.put("data", "");
			resultDataMap.put("resultdata", detailMap);
			// 將新增頁面json塞入資料庫取得的json
			List<Map<String, Object>> dataList = (List<Map<String, Object>>) paramsData.get("data");
			for (Map<String, Object> dataMap : dataList) {
				if (dataMap.get("datatype").equals("基本資料")) {
					List<Map<String, Object>> docdetailList = (List<Map<String, Object>>) dataMap.get("docdetail");
					docdetailList.add(resultDataMap);
				}
			}
			result = JsonUtils.obj2json(paramsData);
		} else {
			logger.info("查無設定檔CONTRACT_TYPE資料");
		}
		
		return result;
	}
	public List<XauthSysCode> getTemplatemoudle() throws Exception {
		List<XauthSysCode> dataList = commonService.getSysCodeDatasByGp("CONTRACT_MODE_CODE");
		for(int i = 0; i < dataList.size(); i++) {
			if(dataList.get(i).getCode().equals("NSC")) {
				logger.info("移除" + dataList.get(i));
				dataList.remove(i);
			}
		}
		return dataList;	
	}
}
