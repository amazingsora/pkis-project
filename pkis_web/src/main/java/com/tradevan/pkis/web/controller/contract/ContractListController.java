package com.tradevan.pkis.web.controller.contract;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
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
import com.tradevan.mapper.pkis.model.Reviewconf;
import com.tradevan.pkis.web.service.common.CommonService;
import com.tradevan.pkis.web.service.contract.ContractService;
import com.tradevan.pkis.web.service.flow.FlowSetService;
import com.tradevan.xauthframework.common.bean.ProcessResult;
import com.tradevan.xauthframework.common.utils.JsonUtils;
import com.tradevan.xauthframework.core.utils.XauthPropUtils;
import com.tradevan.xauthframework.web.bean.FileBean;
import com.tradevan.xauthframework.web.controller.DefaultController;

/**
 * 作 業 代 碼 ：CONTRACT<br>
 * 作 業 名 稱 ：合約查詢<br>
 * 程 式 代 號 ：ContractListController<br>
 * 描 述 ：<br>
 * 公 司 ：Tradevan Co.<br>
 * <br>
 * 【 資 料 來 源】 ：<br>
 * 【 輸 出 報 表】 ：<br>
 * 【 異 動 紀 錄】 ：<br>
 * 
 * @author : bruce<br>
 * @version : 1.0.0 2020/4/16
 *          <P>
 */
@RestController
@RequestMapping("/contract/list")
public class ContractListController extends DefaultController {
	@Autowired
	ContractService contractService;

	@Autowired
	FlowSetService flowSetService;
	
	@Autowired
	CommonService commonService;
	
	private static String DOWMLOAD_PDF_URL = XauthPropUtils.getKey("download.files.pdfurl");

	/**
	 * 初始化頁面
	 * 
	 * @return
	 */
	@RequestMapping("/")
	public ModelAndView init() {
		return new ModelAndView("/contract/contract_list");
	}

	/**
	 * 合約查詢Grid
	 * 
	 * @param request
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/query")
	public @ResponseBody Object query(HttpServletRequest request, @RequestParam Map<String, Object> params)throws Exception {
		return contractService.selectContractList(params);
	}

	/**
	 * 代辦清單Grid
	 * 
	 * @param request
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/toDoList")
	public @ResponseBody Object toDoList(HttpServletRequest request, @RequestParam Map<String, Object> params)throws Exception {
		return contractService.selectToDoList(params);
	}

	/**
	 * 下拉式選單
	 * 
	 * @param request
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/getFlowList")
	public @ResponseBody Object getFlowList(HttpServletRequest request, @RequestParam Map<String, Object> params)throws Exception {
		String contractmodel = MapUtils.getString(params, "module");
		List<Reviewconf> flowList = contractService.getFlowList(contractmodel);
		ProcessResult result = new ProcessResult();
		result.addResult("dataList", flowList);
		return result;
	}

	/**
	 * 下拉式選單
	 * 
	 * @param request
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/getSuppliermaster")
	public @ResponseBody Object getSuppliermaster(HttpServletRequest request, @RequestParam Map<String, Object> params)throws Exception {
		String section = MapUtils.getString(params, "section");
		String module = MapUtils.getString(params, "module");
		List<Map<String, Object>> suppliermasterList = contractService.getSuppliermaster(section, module);
		ProcessResult result = new ProcessResult();
		result.addResult("dataList", suppliermasterList);
		return result;
	}

	/**
	 * 部門階層
	 * 
	 * @param request
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/getDept")
	public @ResponseBody Object getDept(HttpServletRequest request, @RequestParam Map<String, Object> params) {
		List<String> flowList = contractService.getDept();
		ProcessResult result = new ProcessResult();
		result.setStatus(ProcessResult.OK);
		result.addResult("data", flowList);
		return result;
	}

	/**
	 * 詳細資料頁面
	 * 
	 * @param request
	 * @param params
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/view")
	public @ResponseBody Object view(HttpServletRequest request, @RequestParam Map<String, Object> params)throws Exception {
		ModelAndView mv = new ModelAndView("/contract/contract_edit", "data", new HashMap<String, Object>());
		logger.info("Params Data == " + params.toString());
		if (params != null && params.size() > 0) {
			String jsonData = MapUtils.getString(params, "keyData");
			if (StringUtils.isNotBlank(jsonData)) {
				Map<String, Object> data = new HashMap<String, Object>();
				data = contractService.getFlowValue(params);
				logger.info("Flow Data == " + data.toString());
				mv.addObject("data", data);
			}
		}
		return mv;
	}

	/**
	 * 新增頁 導頁
	 * 
	 * @param request
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/insert")
	public @ResponseBody Object insert(HttpServletRequest request, @RequestParam Map<String, Object> params)throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		ModelAndView mv = new ModelAndView("/contract/contract_insert");
		mv.addObject("creDateDesc", sdf.format(new Date()));

		return mv;
	}

	/**
	 * 新增資料 && 開啟合約流程 >>>>>>>> Flow
	 * 
	 * @param request
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/insertData")
	public @ResponseBody Object insertData(HttpServletRequest request, @RequestParam Map<String, Object> params)throws Exception {
		logger.info("傳入參數insertData"+params);
		String jsonData = MapUtils.getString(params, "paramsData");
		Map<String, Object> paramsData = JsonUtils.json2Object(jsonData, Map.class, false);
		return contractService.insertContractData(paramsData);
	}

	/**
	 * 編輯合約
	 * 
	 * @return
	 */
	@RequestMapping("/editView")
	public @ResponseBody Object editView(HttpServletRequest request, @RequestParam Map<String, Object> params) {
		ModelAndView mv = new ModelAndView("/contract/contract_edit", "data", new HashMap<String, Object>());
		logger.info("editView傳入參數 ==="+params);
		params.put("currentUserCname", userContext.getCurrentUser().getUserCname());
		params.put("currentUserid", userContext.getCurrentUser().getUserId());
		params.put("idenId", userContext.getCurrentUser().getIdenId());
		mv.addObject("data", params);
		return mv;
	}

	/**
	 * 撈 JSON 資料
	 * 
	 * @param request
	 * @param params
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/detail")
	public @ResponseBody Object detail(HttpServletRequest request, @RequestBody Map<String, Object> params)throws Exception {
		String jsonData = MapUtils.getString(params, "data");
		Map<String, Object> keyData = new Gson().fromJson(jsonData, HashMap.class);
		logger.info("Json Params == " + jsonData);
		return contractService.getJsonByEs(keyData);
	}

	/**
	 * 合約編輯 檔案格式下拉選單
	 * 
	 * @param request
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/getCode")
	public @ResponseBody Object getCode(HttpServletRequest request, @RequestBody Map<String, Object> params)throws Exception {
		String jsonData = MapUtils.getString(params, "data");
		Map<String, Object> data = new Gson().fromJson(jsonData, HashMap.class);
		logger.info("Json Params == " + jsonData);
		return contractService.getSysCode(data);
	}

	/**
	 * 編輯頁面 按鈕處理
	 * 
	 * @param request
	 * @param params
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping("/updata")
	public @ResponseBody Object updata(HttpServletRequest request, @RequestBody String paramsStr) throws Exception {
		paramsStr = StringEscapeUtils.unescapeHtml3(paramsStr);
		logger.info("傳入參數=== " + paramsStr);
		Map<String, Object> params = JsonUtils.json2Object(paramsStr, Map.class);
		Map<String, Object> result = null;
		String type = MapUtils.getString(params, "type").replaceAll("[a-zA-Z]", "");
		String indexName = MapUtils.getString(params, "indexName");
		String contractNo = MapUtils.getString(params, "contractNo");
		try {
			if (type == null) {
				type = "暫存";
			}
			if ("送出,駁回,審核,核准".indexOf(type) > -1) {
				result = contractService.flowDeal(params, type, contractNo, false);
			} else if ("暫存,作廢".indexOf(type) > -1) {
				result = contractService.updataValue(params, type, indexName, contractNo);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result;
	}
	
	/**
	 * 合約編輯Grid Query
	 * 
	 * @param request
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/fixQuery")
	public @ResponseBody Object fixQuery(HttpServletRequest request, @RequestBody Map<String, Object> params)throws Exception {
		return contractService.fixQuery(params);
	}

	/**
	 * 檔案上傳
	 * 
	 * @param request
	 * @param json
	 * @param files
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/upload")
	public @ResponseBody Object upload(HttpServletRequest request, @RequestParam("data") String json,@RequestParam("file") MultipartFile files) throws Exception {
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> jsonParams = null;
		ProcessResult process = null;

		if (StringUtils.isNotBlank(json)) {
			jsonParams = new Gson().fromJson(json, HashMap.class);
			logger.info("Json Params == " + jsonParams.toString());

			if (files != null && jsonParams != null) {
				process = contractService.upload(files, jsonParams);

				if (process != null && StringUtils.equals(process.getStatus(), ProcessResult.OK)) {
					result.put("rtnCode", 0);
				} else {
					result.put("rtnCode", -1);
				}

				result.put("message", process.getMessages());
			} else {
				result.put("rtnCode", -1);
				result.put("message", "檔案不得為空");
				return result;
			}
		} else {
			result.put("rtnCode", -1);
			result.put("message", "檔案上傳發生錯誤");
			return result;
		}

		return result;
	}

	/**
	 * 取得關卡流程Flowstep
	 * 
	 * @param request
	 * @param params
	 * @return
	 */
	@RequestMapping("/getFlowstep")
	public @ResponseBody Object getFlowstep(HttpServletRequest request, @RequestBody Map<String, Object> params) {

		ProcessResult result =flowSetService.getFlowstepName(MapUtils.getString(params, "contractNo"));

		return result;
	}

	@RequestMapping("/getApproval")
	public @ResponseBody Object getNegoentry(HttpServletRequest request, @RequestBody Map<String, Object> params) {
		logger.info("獲取參數 ==="+params);
		ProcessResult result = new ProcessResult();
		String taskname = MapUtils.getString(params, "taskName");
		String usercname = MapUtils.getString(params, "usercname");
		String section = MapUtils.getString(params, "section");
		String suppliercode = MapUtils.getString(params, "suppliercode");
		String contractYear = MapUtils.getString(params, "contractYear");
		String contractorAgentUserId=MapUtils.getString(params, "contractorAgentUserId");
		Map <String,String> checkmap =new HashMap<String, String>();
		checkmap.put("taskName", taskname);
		checkmap.put("contractorAgentUserId", contractorAgentUserId);
		checkmap.put("usercname", usercname);

		String getNegoentryHtml = contractService.getNegoentryHtml(section, suppliercode,contractYear,checkmap);
		if (StringUtils.isNotBlank(getNegoentryHtml)) {
			result.setStatus(ProcessResult.OK);
			result.addResult("getNegoentryHtml", getNegoentryHtml);
		} else {
			result.setStatus(ProcessResult.NG);
			result.addMessage("取得小卡資料失敗");
		}
		return result;
	}

	/**
	 * 刪除附件上傳檔案
	 * 
	 * @param request
	 * @param params
	 * @return
	 */
	@RequestMapping("/deleteFile")
	public @ResponseBody Object deleteFile(HttpServletRequest request, @RequestBody Map<String, Object> params)throws Exception {

		ProcessResult result = new ProcessResult();
		String id = MapUtils.getString(params, "id");
		String dataId = MapUtils.getString(params, "dataid");
		result = contractService.deleteFile(id, dataId);

		return result;
	}

	/**
	 * 取得檔案需下載的路徑
	 * 
	 * @param request
	 * @param params
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/preDownloadGetFileSerNo")
	public @ResponseBody String preDownloadGetFileSerNo(HttpServletRequest request, @RequestBody Map<String, Object> params,HttpServletResponse response) throws Exception {
		logger.info("downloadFile#start");
		String id = MapUtils.getString(params, "id");
		String dataId = MapUtils.getString(params, "dataid");
		String fileName = contractService.getFileSerNo(id, dataId);

		return fileName;
	}
	
	/**
	 * 下載檔案
	 * 
	 * @param request
	 * @param resonse
	 * @throws Exception
	 */
	@RequestMapping("/download")
	public void downloadFile(HttpServletRequest request, HttpServletResponse resonse) throws Exception {
		String fileName = "";
		String serNo = request.getParameter("serno");
		if (StringUtils.isNotBlank(serNo)) {
			fileName = contractService.getDownloadFile(serNo);
			File file = new File(fileName);
			this.downloadFile(request, resonse, file);
		}
	}
	/**
	 * 查詢供應商ID生成下拉式選單
	 * 
	 * @param request
	 * @String suppliercode
	 * @throws Exception
	 */
	@RequestMapping("/getSupplieridList")
	public @ResponseBody Object getSupplieridList(HttpServletRequest request, @RequestParam String suppliercode) {
		ProcessResult result = new ProcessResult();
		result.addResult("supplierCodeList", contractService.supplierIdGetList(suppliercode.toUpperCase()));

		return result;
	}
	
	/**
	 * 查詢供應商名稱生成下拉式選單
	 * 
	 * @param request
	 * @String suppliername
	 * @throws Exception
	 */
	@RequestMapping("/getSuppliernameList")
	public @ResponseBody Object getSuppliernameList(HttpServletRequest request, @RequestParam String suppliername) {
		ProcessResult result = new ProcessResult();

		result.addResult("supplierCodeList", contractService.supplierNameGetList(suppliername.toUpperCase()));

		return result;
	}
	/**
	 * 查詢供應商GUI生成下拉式選單
	 * 
	 * @param request
	 * @String GUIId
	 * @throws Exception
	 */
	@RequestMapping("/getSupplierGUIList")
	public @ResponseBody Object getSupplierGUIList(HttpServletRequest request, @RequestParam String GUIId) {

		ProcessResult result = new ProcessResult();
		result.addResult("supplierGUIList", contractService.supplierGUIGetList(GUIId.toUpperCase()));

		return result;
	}
	
	/**
	 * 取得PDF路徑
	 * 
	 * @param request
	 * @param resonse
	 * @throws Exception
	 */
	@RequestMapping("/preDownload")
	public @ResponseBody Object preDownload(HttpServletRequest request, @RequestBody String paramsStr) throws Exception {
		paramsStr = StringEscapeUtils.unescapeHtml3(paramsStr);
		Map<String, Object> params = JsonUtils.json2Object(paramsStr, Map.class);
		Map<String, Object> result = contractService.previewpath(params);
		return result;
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
		String dataid = request.getParameter("dataid");
		String path = "";
		String dirpath = "";
		if (StringUtils.isNotBlank(dataid)) {
			path = DOWMLOAD_PDF_URL + dataid; // 組檔案路徑
			dirpath = path;
			path += "/" + dataid + "_1.pdf";
			File file = new File(path);
			if (file.exists()) {
				this.downloadFile(request, resonse, file);
			} else {
				logger.info("檔案不存在無法下載");
			}
			logger.info("dirpath ==="+dirpath);
			// 連同資料夾刪除
			if(new File(dirpath).exists()) {
				FileUtils.forceDelete(new File(dirpath));

			}
		}
	}

	/**
	 * 取得範本種類下拉選單
	 * 
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/getTemplateTypeList")
	public @ResponseBody Object getTemplateTypeList(HttpServletRequest request, @RequestParam Map<String, Object> params) throws Exception {
		List<Map<String, Object>> dataList = contractService.getTemplateTypeList(params);
		ProcessResult result = new ProcessResult();
		result.addResult("dataList", dataList);
		
		return result;
	}
	
	/**
	 * 取得個人範本名稱下拉選單
	 * 
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/getPerConTemplateList")
	public @ResponseBody Object getPerConTemplateList(HttpServletRequest request, @RequestParam Map<String, Object> params) throws Exception {
		List<Map<String, Object>> dataList = contractService.getPerConTemplateList(params);
		ProcessResult result = new ProcessResult();
		result.addResult("dataList", dataList);
		
		return result;
	}
	
	/**
	 * 查詢SHORTAGEPENALTY表格
	 * 
	 * @param params
	 * @throws Exception
	 */
	@RequestMapping("/getShortagepenalty")
	public @ResponseBody Object getShortagepenalty(@RequestBody Map<String, Object> params) throws Exception {

		return contractService.selectShortagepenalty(params);
		
	}
	
	/**
	 * 確認是否有前次合約紀錄
	 * 
	 * @param request
	 * @param params
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping("/checkBeforeContract")
	public @ResponseBody Object checkBeforeContract(HttpServletRequest request, @RequestParam Map<String, Object> params) throws Exception {
		String jsonData = MapUtils.getString(params, "paramsData");
		Map<String, Object> paramsData = JsonUtils.json2Object(jsonData, Map.class, false);
		return contractService.getBeforeContract(paramsData);
	}
	
	/**
	 * 	回傳檔案位置byte陣列
	 * 
	 * @param request
	 * @param params
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping("/getSignFilebyte")
	public @ResponseBody Object getSignFilebyte(HttpServletRequest request, @RequestParam("data") String json, @RequestParam Map<String, Object> params) throws Exception {
		byte[] fileUpload1 = null;
		byte[] fileUpload2 = null;
		ProcessResult result = new ProcessResult();
		List<FileBean> fileBeanList = this.getFileBeanList(request);
		if (fileBeanList != null && fileBeanList.size() > 0 && params != null) {
			fileUpload1 = fileBeanList.get(0).getFileContent();
			fileUpload2 = fileBeanList.get(1).getFileContent();
			result =  contractService.getSignFilebyte(json, params, fileUpload1, fileUpload2);
		}
		
		return result;
	}
	
	/**
	 * 檔案落檔及存DB
	 * 
	 * @param request
	 * @param json
	 * @param params
	 * @return
	 * @throws Exception
	 */
//	@RequestMapping("/saveSignFile")
//	public @ResponseBody Object saveSignFile(HttpServletRequest request, @RequestParam("data") String json, @RequestParam Map<String, Object> params) throws Exception {
//		ProcessResult result = contractService.saveSignFile(json, params);
//		
//		return result;
//	}
	@RequestMapping("/saveSignFile")
	public @ResponseBody Object saveSignFile(HttpServletRequest request, @RequestBody Map<String, Object> params) throws Exception {
		String json = MapUtils.getString(params, "data");
		ProcessResult result = contractService.saveSignFile(json, params);
		
		return result;
	}
}
