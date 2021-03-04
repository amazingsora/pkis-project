package com.tradevan.pkis.web.controller.supplierMaster;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;
import com.tradevan.mapper.pkis.model.Flowstep;
import com.tradevan.pkis.web.service.contract.ContractService;
import com.tradevan.pkis.web.service.flow.FlowSetService;
import com.tradevan.pkis.web.service.supplierMaster.SupplierMasterContractListService;
import com.tradevan.xauthframework.common.bean.ProcessResult;
import com.tradevan.xauthframework.common.utils.JsonUtils;
import com.tradevan.xauthframework.web.bean.FileBean;
import com.tradevan.xauthframework.web.controller.DefaultController;

/**
 * 作 業 代 碼 ：CONTRACT<br>
 * 作 業 名 稱 ：供應合約查詢<br>
 * 程 式 代 號 ：ContractListController<br>
 * 描             述 ：<br>
 * 公             司 ：Tradevan Co.<br><br>
 *【 資 料 來 源】  ：<br>
 *【 輸 出 報 表】  ：<br>
 *【 異 動 紀 錄】  ：<br>
 * @author   : bruce<br>
 * @version  : 1.0.0 2020/4/16<P>
 */
@RestController
@RequestMapping("/supplier/contract")
public class SupplierMasterContractListController extends DefaultController {

	@Autowired
	SupplierMasterContractListService supplierMasterContractListService;
	
	@Autowired
	ContractService contractService;
	
	@Autowired 
	FlowSetService flowSetService;
	
	/**
	 * 初始化頁面
	 * @return
	 */
	@RequestMapping("/")
	public ModelAndView init() {
		return new ModelAndView("/supplierMaster/supplier_contract_list");		
	}
	
	/**
	 * 合約查詢Grid
	 * @param request
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/query")
	public @ResponseBody Object query(HttpServletRequest request, @RequestParam Map<String, Object> params) throws Exception {
		return supplierMasterContractListService.selectContractList(params);
	}
	
	/**
	 * 詳細資料頁面
	 * @param request
	 * @param params
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/view")
	public @ResponseBody Object view(HttpServletRequest request, @RequestParam Map<String, Object> params) throws Exception {
		ModelAndView mv = new ModelAndView("/supplierMaster/supplier_contract_edit", "data", new HashMap<String, Object>());
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
	 * 編輯合約
	 * @return
	 */
	@RequestMapping("/editView")
	public @ResponseBody Object editView(HttpServletRequest request, @RequestParam Map<String, Object> params) {
		ModelAndView mv = new ModelAndView("/supplierMaster/supplier_contract_edit", "data", new HashMap<String, Object>());
		logger.info("Flow Data == " + params.toString());
		params.put("currentUserCname", userContext.getCurrentUser().getUserCname());
		params.put("idenId", userContext.getCurrentUser().getIdenId());
		mv.addObject("data", params);
		return mv;
	}
	
	/**
	 * 撈 JSON 資料
	 * @param request
	 * @param params
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/detail")
	public @ResponseBody Object detail(HttpServletRequest request, @RequestBody Map<String, Object> params) throws Exception {
		String jsonData = MapUtils.getString(params, "data");
		Map<String, Object> keyData = new Gson().fromJson(jsonData, HashMap.class);
		logger.info("Json Params == " + jsonData);
		return contractService.getJsonByEs(keyData);
	}
	
	/**
	 * 取得關卡流程Flowstep
	 * @param request
	 * @param params
	 * @return
	 */
	@RequestMapping("/getFlowstep")
	public @ResponseBody Object getFlowstep(HttpServletRequest request, @RequestBody Map<String, Object> params) {
		
		ProcessResult result = new ProcessResult();
		List<Flowstep> flowstepList = flowSetService.getFlowstepList(MapUtils.getString(params, "contractNo"));
		if(flowstepList.size() > 0) {
			result.setStatus(ProcessResult.OK);
			result.addResult("flowstepList", flowstepList);
		} else {
			result.setStatus(ProcessResult.NG);
			result.addMessage("關卡流程圖示產生失敗");
		}
		
		return result;
	}
	
	/**
	 * 編輯頁面 按鈕處理
	 * @param request
	 * @param params
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping("/updata")
	public @ResponseBody Object updata(HttpServletRequest request, @RequestBody String paramsStr) throws Exception{
		paramsStr = StringEscapeUtils.unescapeHtml3(paramsStr);
		Map<String, Object> params = JsonUtils.json2Object(paramsStr, Map.class);
		Map<String, Object> result = null;
		String type = MapUtils.getString(params, "type");
		String indexName = MapUtils.getString(params, "indexName");
		String contractNo = MapUtils.getString(params, "contractNo");
		try {
			logger.info(type);
			if(type == null) {
				type = "暫存";
			}
			logger.info(type);
			if("送出,駁回,審核,核准".indexOf(type) > -1) {
				result = contractService.flowDeal(params, type, contractNo, true);
			}else if("暫存,作廢".indexOf(type) > -1){
				result = contractService.updataValue(params, type, indexName, contractNo);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		logger.info("Response == " + result.toString());
		
		return result;
	}
	
	/**
	 * 合約編輯 檔案格式下拉選單
	 * @param request
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/getCode")
	public @ResponseBody Object getCode(HttpServletRequest request, @RequestBody Map<String, Object> params) throws Exception {
		String jsonData = MapUtils.getString(params, "data");
		Map<String, Object> data = new Gson().fromJson(jsonData, HashMap.class);
		logger.info("Json Params == " + jsonData);
		return contractService.getSysCode(data);
	}
	
	/**
	 * 合約編輯Grid Query
	 * @param request
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/fixQuery")
	public @ResponseBody Object fixQuery(HttpServletRequest request, @RequestBody Map<String, Object> params) throws Exception{
		return contractService.fixQuery(params);
	}
	
	/**
	 * 刪除附件上傳檔案
	 * @param request
	 * @param params
	 * @return
	 */
	@RequestMapping("/deleteFile")
	public @ResponseBody Object deleteFile(HttpServletRequest request, @RequestBody Map<String, Object> params) throws Exception {
		
		ProcessResult result = new ProcessResult();
		String id = MapUtils.getString(params, "id");
		String dataId = MapUtils.getString(params, "dataid");
		result = contractService.deleteFile(id, dataId);
		
		return result;
	}
	
	/**
	 * 取得檔案需下載的路徑
	 * @param request
	 * @param params
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/preDownloadGetFileSerNo")
	public @ResponseBody String preDownloadGetFileSerNo(HttpServletRequest request, @RequestBody Map<String, Object> params, HttpServletResponse response) throws Exception {
		logger.info("downloadFile#start");
		String id = MapUtils.getString(params, "id");
		String dataId = MapUtils.getString(params, "dataid");
		String fileName = contractService.getFileSerNo(id, dataId);
	    
		return fileName;
	}
	
	/**
	 * 下載檔案
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
	 * 查詢SHORTAGEPENALTY表格
	 * @throws datalist
	 */
	@RequestMapping("/getShortagepenalty")
	public @ResponseBody Object getShortagepenalty(@RequestBody Map<String, Object> params) throws Exception {

		return contractService.selectShortagepenalty(params);
		
	}
	
	/**
	 * 	回傳檔案位置byte陣列
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
