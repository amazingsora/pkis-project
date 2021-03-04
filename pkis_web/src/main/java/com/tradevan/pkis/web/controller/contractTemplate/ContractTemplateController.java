package com.tradevan.pkis.web.controller.contractTemplate;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;
import com.tradevan.pkis.web.service.contract.ContractService;
import com.tradevan.pkis.web.service.contractTemplate.ContractTemplateService;
import com.tradevan.xauthframework.common.bean.ProcessResult;
import com.tradevan.xauthframework.common.utils.JsonUtils;
import com.tradevan.xauthframework.web.controller.DefaultController;

/**
 * 作 業 代 碼 ：CONTRACT_Template<br>
 * 作 業 名 稱 ：合約查詢與上傳<br>
 * 程 式 代 號 ：ContractTemplateController<br>
 * 描             述 ：<br>
 * 公             司 ：Tradevan Co.<br><br>
 *【 資 料 來 源】  ：<br>
 *【 輸 出 報 表】  ：<br>
 *【 異 動 紀 錄】  ：<br>
 * @author   : jason tsai<br>
 * @version  : 1.0.0 2020/6/17<P>
 */
@RestController
@RequestMapping("/contractTemplate/manage")
public class ContractTemplateController extends DefaultController {
	
	@Autowired
	ContractTemplateService contractTemplateService;
	
	@Autowired
	ContractService contractService;
	
	/**
	 * 初始化頁面
	 */
	@RequestMapping("/")
	public ModelAndView init(){
		return new ModelAndView("/contractTemplate/contractTemplate_manage");
	}
	
	/**
	 * 分頁查詢
	 */
	@RequestMapping("/query")
	public @ResponseBody Object query(HttpServletRequest request, @RequestParam Map<String, Object> params) throws Exception{
		return contractTemplateService.searchDeptPage(params);
	}
	
	/**
	 * 上傳頁面
	 */
	@RequestMapping("/toUpload")
	public ModelAndView toUpload(HttpServletRequest request) throws Exception {
		return new ModelAndView("/contractTemplate/contractTemplate_upload");
	}
	
	@RequestMapping("/upload")
	public @ResponseBody Object upload(HttpServletRequest request, @RequestParam Map<String, Object> params, @RequestParam("file") MultipartFile multipartFile) throws Exception {
		ProcessResult result = new ProcessResult();
		//取得檔案清單
		String fileName = multipartFile.getOriginalFilename();
		String fileUrl = "";
		String latestVersion = "";
		String[] splitFile = fileName.split("-");
		String lastStr = StringUtils.defaultIfEmpty(splitFile[2], "");
		String extend = "";
		String disp = "";
		
		if(StringUtils.isNotBlank(lastStr) && lastStr.indexOf(".") > 0){
			extend = lastStr.split("\\.")[0];
			disp = lastStr.split("\\.")[1];
		}
		
		params.put("fullpath", fileName);
		params.put("extend", extend);
		params.put("disp", disp);
		latestVersion = contractTemplateService.getLatestVersion(params);
		fileUrl = contractTemplateService.getExcelPath(MapUtils.getString(params, "year") + MapUtils.getString(params, "module") + "_" + latestVersion);
		
		File file = new File(fileUrl);
		if(!file.exists()){
			file.mkdirs();
		}
		
		file = new File(fileUrl + "/" + fileName);
		multipartFile.transferTo(file);
		
		params.put("latestVersion", latestVersion);
		ProcessResult uploadResult = contractTemplateService.upload(file, params);
		result.setStatus(uploadResult.getStatus());
		result.setMessages(uploadResult.getMessages());
		
		if(StringUtils.equals(result.getStatus(), ProcessResult.NG)) {
			//刪除上傳的檔案
			if(file.exists()){
				file.delete();
			}
		}
		
		return result;
	}
	
	/**
	 * 發佈
	 */
	@RequestMapping("/release")
	public @ResponseBody Object release(HttpServletRequest request, @RequestParam Map<String, Object> params) throws Exception {
		String docver = MapUtils.getString(params, "docver");
		ProcessResult result = contractTemplateService.release(docver);
		return result;
	}
	
	/***
	 * 停用
	 */
	@RequestMapping("/stop")
	public @ResponseBody Object stop(HttpServletRequest request, @RequestParam Map<String, Object> params) throws Exception {
		String docver = MapUtils.getString(params, "docver");
		ProcessResult result = contractTemplateService.stop(docver);
		return result;
	}
	
	/**
	 * 下載xls檔案
	 * @param request
	 * @param resonse
	 * @throws Exception
	 */
	@RequestMapping("/downloadXls")
	public void downloadXls(HttpServletRequest request, HttpServletResponse resonse) throws Exception {
		String docver = request.getParameter("docver");
		String downloadPath = contractTemplateService.getDownloadXlsPath(docver);
		File file = new File(downloadPath);
		
		if(file.exists()) {
			this.downloadFile(request, resonse, file);
		} else {
			logger.error("下載失敗，檔案不存在");
			throw new Exception("下載失敗，請聯絡系統管理員");
		}
    }
	
	/**
	 * 下載pdf檔案
	 * @param request
	 * @param resonse
	 * @throws Exception
	 */
	@RequestMapping("/downloadPdf")
	public void downloadPdf(HttpServletRequest request, HttpServletResponse resonse) throws Exception {
		String fileName = request.getParameter("fileName");
		String downloadPath = contractTemplateService.getPdfPath(fileName + ".pdf");
		File file = new File(downloadPath);
		
		if(file.exists()) {
			this.downloadFile(request, resonse, file);
			FileUtils.forceDelete(file);
		} else {
			logger.error("下載失敗，檔案不存在");
			throw new Exception("下載失敗，請聯絡系統管理員");
		}
	}
	
	/**
	 * 下載範本
	 * @param request
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/preDownload")
	public @ResponseBody String preDownload(HttpServletRequest request, @RequestBody String paramsStr) throws Exception {
		paramsStr = StringEscapeUtils.unescapeHtml3(paramsStr);
		Map<String, Object> params = JsonUtils.json2Object(paramsStr, Map.class);
		String json = new Gson().toJson(params.get("data"), Map.class);
		
		return contractTemplateService.createPdf(json);
	}
	
	/**
	 * 下載PDF檔
	 * 
	 * @param request
	 * @param resonse
	 * @throws Exception
	 */
	@RequestMapping("/downloadpreview")
	public void downloadpreview(HttpServletRequest request, HttpServletResponse resonse) throws Exception {
		String fileName = request.getParameter("fileName");
		String path = contractTemplateService.getPdfPath(fileName + ".pdf"); // 組檔案路徑
		File file = new File(path);
		
		if (file.exists()) {
			this.downloadFile(request, resonse, file);
			FileUtils.forceDelete(file);
		} else {
			logger.error("下載失敗，檔案不存在");
			throw new Exception("下載失敗，請聯絡系統管理員");
		}
	}
	
	/**
	 * 版本比對按鈕導頁
	 * @param request
	 * @param params
	 * @throws Exception
	 */
	@RequestMapping("/toPdfVerCompare")
	public ModelAndView toPdfVerCompare(HttpServletRequest request, @RequestParam Map<String, Object> params) throws Exception {
		ModelAndView mv = new ModelAndView("/contractTemplate/contractTemplate_verCompare");
		mv.addObject("data", contractTemplateService.getContractTemplateData(MapUtils.getString(params, "docver")));
		
		return mv;
	}
	
	/**
	 * 版本比對grid查詢
	 * @param rquest
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/queryPdfVerCompare")
	public @ResponseBody Object queryPdfVerCompare(HttpServletRequest rquest, @RequestParam Map<String, Object> params) throws Exception {
		
		return contractTemplateService.pdfVerCompareQuery(params);
	}
	
	/**
	 * 版本比對
	 * @param rquest
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/pdfVerCompare")
	public @ResponseBody Object pdfVerCompare(HttpServletRequest rquest, @RequestParam Map<String, Object> params) throws Exception {
		ProcessResult result = new ProcessResult();
		result = contractTemplateService.pdfVerCompare(params);
		
		return result;
	}
	
	/**
	 * 編輯合約範本
	 * @param request
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/editView")
	public @ResponseBody Object editView(HttpServletRequest request, @RequestParam Map<String, Object> params) throws Exception {
		ModelAndView mv = new ModelAndView("/contractTemplate/contractTemplate_edit", "data", new HashMap<String, Object>());
		if(params.containsKey("keyData")) {
			String jsonData = MapUtils.getString(params, "keyData");
			params = JsonUtils.json2Object(jsonData, Map.class, false);
		}
		logger.info("params == " + params);
		params.put("dataType", "0.前言");
		mv.addObject("data", params);
		return mv;
	}
	
	/**
	 * 撈json資料
	 * @param request
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/detail")
	public @ResponseBody Object detail(HttpServletRequest request, @RequestBody Map<String, Object> params) throws Exception {
		String jsonData = MapUtils.getString(params, "data");
		Map<String, Object> keyData = new Gson().fromJson(jsonData, HashMap.class);
		logger.info("keyData == " + keyData);
		
		return contractTemplateService.getDetailData(keyData);
	}
}
