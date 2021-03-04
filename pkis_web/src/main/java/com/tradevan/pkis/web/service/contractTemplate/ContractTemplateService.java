package com.tradevan.pkis.web.service.contractTemplate;

import java.io.File;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.ReadContext;
import com.tradevan.mapper.pkis.dao.ContractmasterMapper;
import com.tradevan.mapper.pkis.dao.DesignermgrMapper;
import com.tradevan.mapper.pkis.dao.PercontracttemplateMapper;
import com.tradevan.mapper.pkis.model.Contractmaster;
import com.tradevan.mapper.pkis.model.Designermgr;
import com.tradevan.mapper.pkis.model.Percontracttemplate;
import com.tradevan.mapper.xauth.dao.XauthMapper;
import com.tradevan.mapper.xauth.dao.XauthSysCodeMapper;
import com.tradevan.mapper.xauth.model.XauthSysCode;
import com.tradevan.pkis.web.service.contract.ContractCompareService;
import com.tradevan.pkis.web.service.contract.ContractReportService;
import com.tradevan.pkis.web.util.ElasticSearchUtil;
import com.tradevan.pkis.web.util.JsonUtil;
import com.tradevan.pkis.web.util.SendApiUtil;
import com.tradevan.xauthframework.common.bean.ProcessResult;
import com.tradevan.xauthframework.common.utils.JsonUtils;
import com.tradevan.xauthframework.core.common.LocaleMessage;
import com.tradevan.xauthframework.core.utils.XauthPropUtils;
import com.tradevan.xauthframework.dao.bean.GridResult;
import com.tradevan.xauthframework.web.service.DefaultService;

@Service("ContractTemplateService")
@Transactional(rollbackFor=Exception.class)
public class ContractTemplateService extends DefaultService {
	@Autowired
	XauthSysCodeMapper xauthSysCodeMapper;
	
	@Autowired
	XauthMapper xauthMapper;
	
	@Autowired
	DesignermgrMapper designermgrMapper;
	
	@Autowired
	PercontracttemplateMapper percontracttemplateMapper;
	
	@Autowired
	ElasticSearchUtil elasticSearchUtil;
	
	@Autowired
	SendApiUtil sendApiUtil;
	
	@Autowired
	ContractReportService contractReportService;
	
	@Autowired
	ContractCompareService contractCompareService;
	
	@Autowired
	ContractmasterMapper contractmasterMapper;
	
	private static String EXCLEFILE_URL = XauthPropUtils.getKey("upload.files.excelurl");
	
	private static String PDFFILE_URL = XauthPropUtils.getKey("download.files.pdfurl");
	
	Map<String, String> netApiMap = new HashMap<String, String>();
	
	public GridResult searchDeptPage(Map<String, Object> params) throws Exception {
		GridResult gridResult = this.grid(params);		
		Map<String, Object> objParams = new HashMap<String, Object>();
		String module = MapUtils.getString(params, "module");
		String docstatus = MapUtils.getString(params, "docstatus");
		
		objParams.put("module", module);
		objParams.put("docstatus", docstatus);
		objParams.put("sortColumnName", "YEAR, DISP, CAST(VERSION AS FLOAT) DESC");
		commonDao.query(gridResult, "com.tradevan.mapper.pkis.dao.DesignermgrMapper.searchDesignermgr", objParams);
		
		return gridResult;
	}
	
	public ProcessResult upload(File file, Map<String, Object> params) throws Exception {
		ProcessResult result = new ProcessResult();
		
		//寫入ElasticSearch & Oracle
		ProcessResult insertResult = insert(file, params);
		if(StringUtils.equals(insertResult.getStatus(), ProcessResult.NG)) {
			result.setStatus(insertResult.getStatus());
			result.setMessages(insertResult.getMessages());
		} else {
			result.setStatus(insertResult.getStatus());
			result.addMessage("檔案上傳:成功");
		}
		
		return result;
	}
	
	private ProcessResult insert(File file, Map<String, Object> params) throws Exception {
		ProcessResult result = new ProcessResult();
		result.setStatus(ProcessResult.NG);
		result.addMessage("檔案上傳:失敗");
//		String docver = UUID.randomUUID().toString();
		String docver = "";
		String netUrl = XauthPropUtils.getKey("net.api.url");
		Map<String, Object> paramMap = getParamMap(file, params);
		
		ProcessResult checkIsBasicData = checkIsBasicData(paramMap);
		if(StringUtils.equals(checkIsBasicData.getStatus(), ProcessResult.NG)) {
			result.setStatus(checkIsBasicData.getStatus());
			result.setMessages(checkIsBasicData.getMessages());
			return result;
		}
		
		//呼叫.net API
		String json = sendApiUtil.post(netUrl, netApiMap, file);
		docver = getDocver(json);
		
		// 將DESIGNERMGR的基本資料json塞入新上傳的json資料中
		json = setDesignermgrJson(json, paramMap, docver);
		
		if(StringUtils.isNotBlank(json)){
			//寫入ElasticSearch
	        //insertDesignermgrForES(paramMap, json);
			//寫入Oracle
			insertDesignermgr(paramMap, json, docver);
			
			result.setStatus(ProcessResult.OK);
		} else {
			result.setStatus(ProcessResult.NG);
			result.addMessage("查無合約基本資料範本");
		}
			
		return result;
	}
	
	public void insertDesignermgr(Map<String, Object> params, String json, String docver) throws Exception {
		Date updtime = (Date) MapUtils.getObject(params, "updtime");
//		Date efftime = (Date) MapUtils.getObject(params, "efftime");
//		Date prdtime = (Date) MapUtils.getObject(params, "prdtime");
//		Date droptime = (Date) MapUtils.getObject(params, "droptime");
		String module = MapUtils.getString(params, "module");
		String kind = MapUtils.getString(params, "kind");
		String spec = MapUtils.getString(params, "spec");
		String extend = MapUtils.getString(params, "extend");
//		String version = MapUtils.getString(params, "version");
		String doccode = MapUtils.getString(params, "doccode");
		String disp = MapUtils.getString(params, "disp");
		String shortdisp = MapUtils.getString(params, "shortdisp");
		String fullpath = MapUtils.getString(params, "fullpath");
		String errmessage = MapUtils.getString(params, "errmessage");
		String year = MapUtils.getString(params, "year");
		// 取得目前最新版本
		String latestVersion = MapUtils.getString(params, "latestVersion");
		
		// 先刪除同版本的再新增
		QueryWrapper<Designermgr> designermgrWrapper = new QueryWrapper<Designermgr>();
		designermgrWrapper.eq("DOCSTATUS", "00");
		designermgrWrapper.eq("MODULE", module);
		designermgrWrapper.eq("DISP", disp);
		designermgrWrapper.eq("YEAR", year);
		designermgrWrapper.eq("VERSION", latestVersion);
		List<Designermgr> designermgrList = designermgrMapper.selectList(designermgrWrapper);
		if(designermgrList != null && designermgrList.size() > 0) {
			designermgrMapper.delete(designermgrWrapper);
		}
		
		Designermgr designermgr = new Designermgr();
		designermgr.setDocver(docver);
		designermgr.setUpdtime(updtime);
//		designermgr.setEfftime(efftime);
//		designermgr.setPrdtime(prdtime);
//		designermgr.setDroptime(droptime);
		designermgr.setDocstatus("00");
		designermgr.setModule(module);
		designermgr.setKind(kind);
		designermgr.setSpec(spec);
		designermgr.setExtend(extend);
		designermgr.setVersion(latestVersion);
		designermgr.setDoccode(doccode);
		designermgr.setDisp(disp);
		designermgr.setShortdisp(shortdisp);
		designermgr.setFullpath(fullpath);
		designermgr.setErrmessage(errmessage);
		designermgr.setJson(json);
		designermgr.setYear(year);
		designermgr.setCreateuser(getCreOrUpdUser(null));
		designermgrMapper.insertDesignermgr(designermgr);
	}
	
	/**
	 * 取得最新版本號
	 * @param params
	 */
	public String getLatestVersion(Map<String, Object> params) throws Exception {
		Map<String, Object> conditionMap = new HashMap<String, Object>();
		conditionMap.put("module", MapUtils.getString(params, "module"));
//		conditionMap.put("fullpath", MapUtils.getString(params, "fullpath"));
		conditionMap.put("disp", MapUtils.getString(params, "disp"));
		conditionMap.put("year", MapUtils.getString(params, "year"));
		List<Designermgr> list = designermgrMapper.selectByMap(conditionMap);
		
		BigDecimal bigVersion = new BigDecimal(0);
		for(Designermgr entity : list){
			String dbVersion = entity.getVersion();
			//取得目前最新版本
			if(StringUtils.isNotBlank(dbVersion)){
				BigDecimal tempVersion = new BigDecimal(dbVersion);
				if(tempVersion.compareTo(bigVersion) >= 0){
					bigVersion = tempVersion;
				}
			}
		}
		
		//最新版本只能到.9，例如：0.9、1.9...
		String tempVersion = String.valueOf(bigVersion.floatValue());
		if(tempVersion.indexOf(".") > 0){
			//取小數第一位
			tempVersion = tempVersion.substring(tempVersion.indexOf(".") + 1);
			if(Integer.valueOf(tempVersion) != 9){
				bigVersion = bigVersion.add(new BigDecimal(0.1));
			}
		}else{
			bigVersion = bigVersion.add(new BigDecimal(0.1));
		}

		return String.valueOf(bigVersion.floatValue());
	}
	
	@SuppressWarnings("static-access")
	private void insertDesignermgrForES(Map<String, Object> params, String json) throws Exception{
		String ip = XauthPropUtils.getKey("es.ip");
		String port = XauthPropUtils.getKey("es.port");
		String year = MapUtils.getString(params, "year");
		String spec = MapUtils.getString(params, "spec");
		String contractNo = MapUtils.getString(params, "contractNo");
		String indexName = year + "_" + spec.toLowerCase() + "_qryrec_doc";	//index只能小寫
			
		//檢查連線
		boolean isConnect = elasticSearchUtil.initESClinet(ip, port);
		if(isConnect){ 
			//index是否存在；存在->寫入，不存在->新增
			if(!elasticSearchUtil.existsIndex(indexName)){
				//建立index
				elasticSearchUtil.createIndex(indexName);
				//寫入資料
				Map<String, Object> insertIndexMap = elasticSearchUtil.insertIndex(indexName, contractNo, json);
				//刪除當筆資料
				elasticSearchUtil.deleteES(indexName);
			}
		}
		
		//關閉連線
		elasticSearchUtil.closeESClient();
	}
	
	private Map<String, Object> getParamMap(File file, Map<String, Object> params) throws Exception{
		Map<String, Object> paramMap = new HashMap<String, Object>();
		
		//檔案名稱拆解
		String fileName = file.getName();
		String[] splitFile = fileName.split("-");
		Date updtime = new Date();
		String module = MapUtils.getString(params, "module");
		String year = MapUtils.getString(params, "year");
		String kind = StringUtils.defaultIfEmpty(splitFile[0], "");
		String spec = StringUtils.defaultIfEmpty(splitFile[1], "");
		String extend = MapUtils.getString(params, "extend");;
		String doccode = fileName.substring(0, fileName.indexOf("."));
		String disp = MapUtils.getString(params, "disp");;
		String shortdisp = StringUtils.defaultIfEmpty(splitFile[0], "");
		String fullpath = fileName;
//		String lastStr = StringUtils.defaultIfEmpty(splitFile[2], "");
		String latestVersion = MapUtils.getString(params, "latestVersion");
		
//		if(StringUtils.isNotBlank(lastStr) && lastStr.indexOf(".") > 0){
//			extend = lastStr.split("\\.")[0];
//			disp = lastStr.split("\\.")[1];
//		}
		
//		paramMap.put("docver", docver);
		paramMap.put("updtime", updtime);
		paramMap.put("efftime", updtime);
		paramMap.put("prdtime", updtime);
		paramMap.put("droptime", updtime);
		paramMap.put("docstatus", "");
		paramMap.put("module", module);
		paramMap.put("kind", kind);
		paramMap.put("spec", spec);
		paramMap.put("extend", extend);
		paramMap.put("version", "0.1");
		paramMap.put("doccode", doccode);
		paramMap.put("disp", disp);
		paramMap.put("shortdisp", shortdisp);
		paramMap.put("fullpath", fullpath);
		paramMap.put("errmessage", "No Error");
		paramMap.put("json", "");
		paramMap.put("year", year);
		paramMap.put("latestVersion", latestVersion);
		
		//呼叫.net api使用
		netApiMap.put("token", "11111");
		netApiMap.put("userId", "admin");
		netApiMap.put("module", module);
		netApiMap.put("kind", kind);
		netApiMap.put("spec", spec);
		netApiMap.put("extend", extend);
		netApiMap.put("func", kind);
		netApiMap.put("doccode", doccode);
		
		return paramMap;
	}
	
	/**
	 * 發佈合約範本
	 * @param docver
	 * @return
	 */
	public ProcessResult release(String docver) throws Exception {
		ProcessResult result = new ProcessResult();
		String verString = "";
		Date now = new Date(); 
		String year = "";
		String model = "";
		String fullpath = "";
		String oldPath = "";
		String newFolderPath = "";
		StringBuffer esSql = null;
		Map<String, Object> resultMap = null;
		
		Designermgr designermgr = designermgrMapper.selectById(docver);
		if(designermgr == null) {
			result.setStatus(ProcessResult.NG);
			result.addMessage("發佈失敗，查無此範本");
			return result;
		} else {
			year = designermgr.getYear();
			model = designermgr.getModule();
			fullpath = designermgr.getFullpath();
		}
		
		//作廢尚未送出的合約
		//取得EL欄位
		QueryWrapper<Contractmaster> queryWrapper=new QueryWrapper<Contractmaster>();
		queryWrapper.eq("CONTRACTMODEL", model);
		queryWrapper.isNull("SENDDATE");
		List<Contractmaster> expiredData = contractmasterMapper.selectList(queryWrapper);
		//放置indexName
		Set<String> docverlist = new LinkedHashSet<String>();
		//將Contractmaster內已作廢的單子更新SendDate
		for(Contractmaster contract:expiredData) {
			queryWrapper = new QueryWrapper<Contractmaster>();
			queryWrapper.eq("DATAID",contract.getDataid());
			contract.setSendDate(new Timestamp(new Date().getTime()));
			contractmasterMapper.update(contract, queryWrapper);
			docverlist.add(contract.getIndexname());

		}
		
		for(String indexName : docverlist) {
			esSql = new StringBuffer();
			// 組Elasticsearch條件
			esSql.append(" where data.docver is not '" + docver + "' and todo in ('暫存', '新建') ");
			List<String> resultJson = ElasticSearchUtil.searchForES(indexName, "*", esSql.toString());
			for(String jsonData : resultJson) {
				ReadContext readContext = JsonPath.parse(jsonData);
				List<Map<String, Object>> resultdataList = readContext.read("$..resultdata");
				String contractNo = "";
				if(resultdataList.size() > 0) {
					resultMap = resultdataList.get(0);
					contractNo = MapUtils.getString(resultMap, LocaleMessage.getMsg("contract.field.no"));
				}
				jsonData = JsonUtil.jsonSetValueByKey(jsonData, "todo", "作廢");
				//合約主檔ES
				ElasticSearchUtil.updateById(contractNo, indexName, jsonData);
				//關閉連線
				elasticSearchUtil.closeESClient();
			}
		}
		
		if(StringUtils.isNotBlank(designermgr.getVersion())) {
			double version = Double.valueOf(designermgr.getVersion()) + 1.0;
			verString = StringUtils.substring(String.valueOf(version), 0, String.valueOf(version).indexOf(".") + 1) + "0";
			// 複製檔案到新版本
			oldPath = getExcelPath(year + model + "_" + designermgr.getVersion() + "/" + fullpath);
			newFolderPath = getExcelPath(year + model + "_" + verString);
			File newFileFolder = new File(newFolderPath);
			if(!newFileFolder.exists()){
				newFileFolder.mkdirs();
			}
			FileUtils.copyFile(new File(oldPath), new File(newFolderPath + "/" + fullpath));
			designermgr.setVersion(verString);
		}
		
		designermgr.setPrdtime(now);
		designermgr.setDocstatus("01");
		designermgrMapper.updateById(designermgr);
		
		// 停用合約範本舊版本
		QueryWrapper<Designermgr> designermgrWrapper = new QueryWrapper<Designermgr>();
		designermgrWrapper.eq("MODULE", designermgr.getModule());
		designermgrWrapper.ne("DOCVER", docver);
		designermgrWrapper.ne("DOCSTATUS", "99");
		if((StringUtils.equals("SC", designermgr.getModule()) && !StringUtils.equals("基本資料", designermgr.getDisp())) 
				|| StringUtils.equals("NSC", designermgr.getModule())) {
			designermgrWrapper.eq("DISP", designermgr.getDisp());
		}
		List<Designermgr> designermgrList = designermgrMapper.selectList(designermgrWrapper);
		if(designermgrList != null && designermgrList.size() > 0) {
			//停用舊版本
			Designermgr designermgrData = new Designermgr();
			designermgrData.setDroptime(now);
			designermgrData.setDocstatus("99");
			designermgrMapper.update(designermgrData, designermgrWrapper);
			for(Designermgr data : designermgrList){
				// 停用個人合約範本舊版本
				QueryWrapper<Percontracttemplate> percontemplateWrapper = new QueryWrapper<Percontracttemplate>();
				percontemplateWrapper.eq("DOCVER", data.getDocver());
				List<Percontracttemplate> percontracttemplateList = percontracttemplateMapper.selectList(percontemplateWrapper);
				if(percontracttemplateList != null && percontracttemplateList.size() > 0) {
					for(Percontracttemplate perConTemplate : percontracttemplateList) {
						perConTemplate.setDroptime(now);
						perConTemplate.setUpdatedate(now);
						perConTemplate.setUpdateuser(getCreOrUpdUser(null));
						percontracttemplateMapper.updateById(perConTemplate);
					}
				}
			}
		}
		
		result.setStatus(ProcessResult.OK);
		result.addMessage("發佈成功");
			
		return result;
	}
	
	/**
	 * 停用合約範本
	 * @param docver
	 * @return
	 */
	public ProcessResult stop(String docver){
		ProcessResult result = new ProcessResult();
		Date now = new Date(); 
		
		try{
			Designermgr designermgr = designermgrMapper.selectById(docver);
			designermgr.setDroptime(new Date());
			designermgr.setDocstatus("99");
			designermgrMapper.updateById(designermgr);
			
			// 停用個人合約範本
			QueryWrapper<Percontracttemplate> percontemplateWrapper = new QueryWrapper<Percontracttemplate>();
			percontemplateWrapper.eq("DOCVER", docver);
			List<Percontracttemplate> percontracttemplateList = percontracttemplateMapper.selectList(percontemplateWrapper);
			if(percontracttemplateList != null && percontracttemplateList.size() > 0) {
				for(Percontracttemplate perConTemplate : percontracttemplateList) {
					perConTemplate.setDroptime(now);
					perConTemplate.setUpdatedate(now);
					perConTemplate.setUpdateuser(getCreOrUpdUser(null));
					percontracttemplateMapper.updateById(perConTemplate);
				}
			}
			
			result.setStatus(ProcessResult.OK);
			result.addMessage("停用成功");
			
		}catch(Exception e){
			System.out.println("stop Exception:" + e);
		}
		
		return result;
	}
	
	/**
	 * 將DESIGNERMGR的基本資料塞入新上傳的json資料中
	 * @param json
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public String setDesignermgrJson(String json, Map<String, Object> params, String docver) throws Exception {
		String basicJson = "";
		String result = "";
		String module = MapUtils.getString(params, "module");
		String disp = MapUtils.getString(params, "disp");
		Map<String, Object> basicjsonMap = new HashMap<String, Object>();
		Map<String, Object> resultJsonMap = new HashMap<String, Object>();
		List<Map<String, Object>> basicDataList = null;
		List<Map<String, Object>> resultJsonList = null;
		List<Map<String, Object>> basicTopButtonDatas = null;
		List<Map<String, Object>> contractTopButtonDatas = null;
		
		if(StringUtils.equals(module, "SC") && !StringUtils.equals(disp, "基本資料")) {
			// 從DESIGNERMGR查詢出基本資料的json
			QueryWrapper<Designermgr> queryWrapper = new QueryWrapper<Designermgr>();
			queryWrapper.eq("MODULE", module);
			queryWrapper.eq("DISP", "基本資料");
//			queryWrapper.eq("YEAR", MapUtils.getString(params, "year"));
			queryWrapper.isNull("DROPTIME");
			queryWrapper.orderByDesc("PRDTIME");
			List<Designermgr> dataList = designermgrMapper.selectList(queryWrapper);
			if(dataList.size() > 0) {
				// 將DESIGNERMGR中的json轉map
				basicJson = dataList.get(0).getJson();
				basicjsonMap = JsonUtils.json2Object(basicJson, Map.class);
				basicDataList = (List<Map<String, Object>>) basicjsonMap.get("data");
				// update docver
				for(Map<String, Object> basicMap : basicDataList) {
					basicMap.put("docver", docver);
				}
				
				// 將上傳檔案的json轉map
				resultJsonMap = JsonUtils.json2Object(json, Map.class);
				resultJsonList = (List<Map<String, Object>>) resultJsonMap.get("data");
				basicTopButtonDatas = getTopButtonData(basicDataList);
				contractTopButtonDatas = getTopButtonData(resultJsonList);
				
				// 將兩者資料合併
				for(Map<String, Object> resultMap : resultJsonList) {
					basicDataList.add(resultMap);
				}
				resultJsonMap.put("data", basicDataList);
				if(basicTopButtonDatas.isEmpty() || contractTopButtonDatas.isEmpty()) {
					result = "";
				} else {
					resultJsonMap = setTopButtonJson(basicTopButtonDatas, contractTopButtonDatas, resultJsonMap);
					result = JsonUtils.obj2json(resultJsonMap);
				}
			} else {
				result = "";
			}
		} else {
			result = json;
		}
		
		return result;
	}
	
	/**
	 * 重組resultJsonMap的json
	 * @param basicTopButtonDatas
	 * @param contractTopButtonDatas
	 * @param resultJsonMap
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> setTopButtonJson(List<Map<String, Object>> basicTopButtonDatas, List<Map<String, Object>> contractTopButtonDatas, Map<String, Object> resultJsonMap) throws Exception {
		List<Map<String, Object>> dataList = (List<Map<String, Object>>) resultJsonMap.get("data");
		List<Map<String, Object>> sourceDocdetails = null;
		List<Map<String, Object>> docDetails = null;
		List<Map<String, Object>> topButtonDatas = new ArrayList<Map<String, Object>>();
		topButtonDatas.addAll(basicTopButtonDatas);
		topButtonDatas.addAll(contractTopButtonDatas);
		boolean isPutTopButtonDatas = false;
		
		for(Map<String, Object> data : dataList) {
			sourceDocdetails = (List<Map<String, Object>>) data.get("docdetail");
			docDetails = new ArrayList<Map<String, Object>>();
			isPutTopButtonDatas = false;
			for(Map<String, Object> sourceDocdetail : sourceDocdetails) {
				if(isTopButton(sourceDocdetail)) {
					if(!isPutTopButtonDatas) {
						docDetails.addAll(topButtonDatas);
						isPutTopButtonDatas = true;
					}
				} else {
					docDetails.add(sourceDocdetail);
				}
			}
			data.put("docdetail", docDetails);
		}
		
		return resultJsonMap;
	}
	
	/**
	 * 取得TopButton
	 * @param datas
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getTopButtonData(List<Map<String, Object>> datas) throws Exception {
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> sourceDocdetails = null;
		for(Map<String, Object> data : datas) {
			sourceDocdetails = (List<Map<String, Object>>) data.get("docdetail");
			for(Map<String, Object> sourceDocdetail : sourceDocdetails) {
				if(isTopButton(sourceDocdetail)) {
					result.add(sourceDocdetail);
				}
			}
			break;
		}
		
		return result;
	}
	
	/**
	 * 判斷是否為TopButton
	 * @param data
	 * @return
	 */
	private boolean isTopButton(Map<String, Object> data) {
		boolean result = false;
		if(StringUtils.equals("TopButton", MapUtils.getString(data, "_ref")) 
				&& StringUtils.isNotBlank(MapUtils.getString(data, "_js"))) {
			result = true;
		}
		
		return result;
	}
	
	/**
	 * 檢核 新增合約範本
	 * @param params
	 * @return
	 */
	public ProcessResult checkIsBasicData(Map<String, Object> params) throws Exception {
		
		ProcessResult result = new ProcessResult();
		String module = MapUtils.getString(params, "module");
//		String year = MapUtils.getString(params, "year");
		String disp = MapUtils.getString(params, "disp");
		String spec = MapUtils.getString(params, "spec");
		List<Designermgr> designermgrList = null;
		List<XauthSysCode> sysCodeList = null;
		result.setStatus(ProcessResult.OK);
		
		if(!StringUtils.equals(module, spec)) {
			result.setStatus(ProcessResult.NG);
			result.addMessage("合約模式與檔名不符");
			return result;
		}
		
		if(StringUtils.equals(module, "SC") && !StringUtils.equals(disp, "基本資料")) {
			// 檢核檔名不可為為設定在設定檔中之名稱
			QueryWrapper<XauthSysCode> sysCodeWrapper = new QueryWrapper<XauthSysCode>();
			sysCodeWrapper.eq("GP", "SC_FLOW_TEMPLATE");
			sysCodeWrapper.eq("CNAME", disp);
			sysCodeList = xauthSysCodeMapper.selectList(sysCodeWrapper);
			if(sysCodeList == null || sysCodeList.size() == 0) {
				result.setStatus(ProcessResult.NG);
				result.addMessage("設計書名稱不符");
				return result;
			}
			
			// 檢核 制式合約 該年度 基本資料未上傳與未發佈，不得上傳其他EXCEL
			QueryWrapper<Designermgr> queryWrapper = new QueryWrapper<Designermgr>();
			queryWrapper.eq("MODULE", module);
//			queryWrapper.eq("YEAR", year);
			queryWrapper.eq("DISP", "基本資料");
			queryWrapper.orderByDesc("UPDTIME");
			designermgrList = designermgrMapper.selectList(queryWrapper);
			if(designermgrList.size() > 0) {
				// 檢核 基本資料中 最新上傳是否有發佈
				if(designermgrList.get(0).getPrdtime() == null && designermgrList.get(0).getDroptime() == null) {
					result.setStatus(ProcessResult.NG);
					result.addMessage("基本資料未發佈不得上傳其他設計書");
				} else {
					result.setStatus(ProcessResult.OK);
				}
			} else {
				result.setStatus(ProcessResult.NG);
				result.addMessage("基本資料未上傳不得上傳其他設計書");
			}
		}
		
		return result;
	}
	
	/**
	 * 組下載Xls路徑
	 * @param docver
	 * @return
	 * @throws Exception
	 */
	public String getDownloadXlsPath(String docver) throws Exception {
		String result = "";
		Designermgr designermgr = null;
		if(StringUtils.isNotBlank(docver)) {
			designermgr = getDesignermgrData(docver);
			result = getExcelPath(designermgr.getYear() + designermgr.getModule() + "_" + designermgr.getVersion() + "/" + designermgr.getFullpath());
		}
		
		return result;
	}	
	
	/**取得下載路徑
	 * 取得下載pdf路徑
	 * @param request
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public String createPdf(String json) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String fileName = "SC_contract_" + sdf.format(new Date());
		contractReportService.createScPdf(PDFFILE_URL, fileName + ".pdf", json);
		
		return fileName;
	}
	
	public String getDocver(String json) throws Exception {
		String result = "";
		ReadContext readContext = JsonPath.parse(json);
		List<String> docverList = readContext.read("$..docver");
		if(docverList != null && docverList.size() > 0) {
			result = docverList.get(0);
		}
		
		return result;
	}
	
	/**
	 * 取得版本比對源頭資訊
	 * @param docver
	 * @return
	 */
	public Map<String, Object> getContractTemplateData(String docver) throws Exception {
		Map<String, Object> result = null;
		Map<String, Object> objParams = new HashMap<String, Object>();
		objParams.put("docver", docver);
		List<Map<String, Object>> designermgrs = designermgrMapper.searchDesignermgr(objParams);
		logger.info("designermgrs.size() == " + designermgrs.size());
		if(designermgrs != null && designermgrs.size() > 0) {
			result = designermgrs.get(0);
		}
		
		return result;
	}
	
	/**
	 * 查詢版本比對編輯中版本
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public GridResult pdfVerCompareQuery(Map<String, Object> params) throws Exception {
		GridResult gridResult = this.grid(params);
		Map<String, Object> objParams = new HashMap<String, Object>();
		String module = MapUtils.getString(params, "modulecode");
		String disp = MapUtils.getString(params, "disp");
		
		objParams.put("module", module);
		objParams.put("disp", disp);
		objParams.put("docstatus", "00");
		objParams.put("sortColumnName", "CAST(VERSION AS FLOAT)");
		commonDao.query(gridResult, "com.tradevan.mapper.pkis.dao.DesignermgrMapper.searchDesignermgr", objParams);
		
		return gridResult;
	}
	
	/**
	 * 版本比對
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public ProcessResult pdfVerCompare(Map<String, Object> params) throws Exception {
		ProcessResult result = new ProcessResult();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String docverSource = MapUtils.getString(params, "docverSource");  // 源頭docver(版本.0)
		String docver = MapUtils.getString(params, "docver");  // 被選取要比對之docver
		String jsonSource = "";
		String json = "";
		String fileName = "SC_contract_compare_" + sdf.format(new Date());
		Designermgr dataSource = null;
		Designermgr data = null;
		
		if(StringUtils.isBlank(docver)) {
			result.setStatus(ProcessResult.NG);
			result.addMessage("請選擇版本比對項目");
			return result;
		}
		
		dataSource = getDesignermgrData(docverSource);
		data = getDesignermgrData(docver);
		if(dataSource != null && data != null) {
			jsonSource = dataSource.getJson();
			json = data.getJson();
			contractCompareService.build(jsonSource, json, PDFFILE_URL, fileName + ".pdf");
			result.setStatus(ProcessResult.OK);
			result.addResult("fileName", fileName);
		} else {
			result.setStatus(ProcessResult.NG);
			result.addMessage("查無資料");
		}
		
		return result;
	}
	
	/**
	 * 組設定檔pdf下載路徑
	 * @return
	 */
	public String getPdfPath(String fileName) {
		return PDFFILE_URL + fileName;
	}
	
	/**
	 * 組設定檔excel下載路徑
	 * @param fileName
	 * @return
	 */
	public String getExcelPath(String fileName) {
		return EXCLEFILE_URL + fileName;
	}
	
	/**
	 * 取得Designermgr資訊
	 * @param docver
	 * @return
	 */
	public Designermgr getDesignermgrData(String docver) {
		Designermgr result = null;
		QueryWrapper<Designermgr> queryWrapper = new QueryWrapper<Designermgr>();
		queryWrapper.eq("DOCVER", docver);
		List<Designermgr> designermgrList = designermgrMapper.selectList(queryWrapper);
		if(designermgrList.size() > 0) {
			result = designermgrList.get(0);
		}
		
		return result;
	}
	
	/**
	 * 取得json資料
	 * @param request
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> getDetailData(Map<String, Object> params) throws Exception {
		logger.info("getDetailData params == " + params);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		String docver = MapUtils.getString(params, "docver");
		String json = "";
		Designermgr designermgr  = null;
		
		if(StringUtils.isNotBlank(docver)) {
			designermgr = getDesignermgrData(docver);
			if(designermgr != null) {
				json = designermgr.getJson();
				json = insertToJson(json);
				json = JsonUtil.jsonSetValueByKey(json, "todo", "新建");
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
			resultMap.put("message", "查無合約範本");
			return resultMap;
		}
		
		return resultMap;
	}
	
	@SuppressWarnings("unchecked")
	public String insertToJson(String json) throws Exception {
		String result = "";
		Map<String, Object> paramsData = JsonUtils.json2Object(json, Map.class, false);
		Map<String, Object> detailMap = new HashMap<String, Object>();
		Map<String, Object> resultDataMap = new HashMap<String, Object>();	
		
		for(int i = 0 ; i < 7 ; i ++) {
			if(i == 6) {
				detailMap.put("Shortagepenalty_" + i, "");
			}else {
				detailMap.put("Shortagepenalty_" + i, "||");
			}
		}
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
		
		return result;
	}
}
