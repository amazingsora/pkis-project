package com.tradevan.pkis.web.controller.xauth;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;
import com.tradevan.mapper.pkis.model.Reviewconf;
import com.tradevan.mapper.pkis.model.Reviewsetdata;
import com.tradevan.mapper.xauth.model.XauthSysCode;
import com.tradevan.pkis.web.service.xauth.XauthContractReviewService;
import com.tradevan.xauthframework.common.bean.ProcessResult;
import com.tradevan.xauthframework.common.utils.JsonUtils;
import com.tradevan.xauthframework.web.controller.DefaultController;

/**
 * 作 業 代 碼 ：CONTRACT<br>
 * 作 業 名 稱 ：流程查詢<br>
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
@RequestMapping("/xauth/contractReview")
public class XauthContractReviewController extends DefaultController {
	
	@Autowired
	XauthContractReviewService xauthContractReviewService;

	/**
	 * 初始化頁面
	 * @return
	 */
	@RequestMapping("/")
	public ModelAndView init() {
		return new ModelAndView("/xauth/xauth_contract_review_list");
	}
	
	@RequestMapping("/query")
	public @ResponseBody Object query(HttpServletRequest request, @RequestParam Map<String, Object> params) throws Exception {
		return xauthContractReviewService.selectContractReviewGridList(params);
	}
	
	/**
	 * 詳細資料頁面 Insert ＆＆ Edit View
	 * @param request
	 * @param params
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/data")
	public ModelAndView data(HttpServletRequest request, @RequestParam Map<String, Object> params) throws Exception{
		ModelAndView mv = new ModelAndView("/xauth/xauth_contract_review_data");
		
		if (params != null && params.size() > 0) {
			String jsonData = MapUtils.getString(params, "keyData");
			if (StringUtils.isNotBlank(jsonData)) {
				Map<String, Object> keyData = new Gson().fromJson(jsonData, HashMap.class);
				List<Reviewconf> reviewconfList = xauthContractReviewService.selectContractReviewList(keyData);
				List<Map<String, Object>> reviewsetdataconfList = xauthContractReviewService.selectReviewsetdataconfList(keyData);
				
				//取得該筆編輯資料
				if(reviewconfList.size() == 1) {
					Reviewconf uiData = reviewconfList.get(0);
					mv.addObject("data", uiData);
				}
				// 取得checkbox資料
				mv.addObject("reviewsetdataconfList", reviewsetdataconfList);
			}
		}
		return mv;
	}
	
	/**
	 * 新增資料
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/insertData")
	public @ResponseBody Object insertData(HttpServletRequest request, @RequestParam Map<String, Object> params) throws Exception{
		String jsonData = MapUtils.getString(params, "paramsData");
		logger.info("jsonData == " + jsonData);
		Map<String, Object> paramsData = (Map<String, Object>)JsonUtils.json2Object(jsonData, Map.class, false);
		
		return xauthContractReviewService.insertContractReviewData(paramsData);
	}
	
	/**
	 * 更新資料
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/updateData")
	public @ResponseBody Object updateData(HttpServletRequest request, @RequestParam Map<String, Object> params) throws Exception{
		String jsonData = MapUtils.getString(params, "paramsData");
		Map<String, Object> paramsData = (Map<String, Object>)JsonUtils.json2Object(jsonData, Map.class, false);
		return xauthContractReviewService.updateContractReviewData(paramsData);
	}
	
	/**
	 * 啟用/停用資料
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/bgnAndStop")
	public @ResponseBody Object bgnAndStop(HttpServletRequest request, @RequestParam Map<String, Object> params) throws Exception{
		String jsonData = MapUtils.getString(params, "paramsData");
		Map<String, Object> paramsData = (Map<String, Object>)JsonUtils.json2Object(jsonData, Map.class, false);
		return xauthContractReviewService.bgnOrStopSupplierData(paramsData);
	}
	
	/**
	 * 下拉式選單
	 * @return
	 */
	@RequestMapping("/getFlowList")
	public @ResponseBody Object getFlowList(HttpServletRequest request, @RequestParam Map<String, Object> params) throws Exception{
		String contractmodel = MapUtils.getString(params, "contractmodel");
		List<XauthSysCode> flowList = xauthContractReviewService.getFlowList(contractmodel);
		ProcessResult result = new ProcessResult();
		result.addResult("dataList", flowList);
		return result;
	}
	
	/**
	 * 取得流程checkbox
	 * @param request
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/getReviewsetdata")
	public @ResponseBody Object getReviewsetdata(HttpServletRequest request, @RequestParam Map<String, Object> params) throws Exception {
		List<Reviewsetdata> reviewsetdataList = xauthContractReviewService.getReviewsetdata();
		ProcessResult result = new ProcessResult();
		result.addResult("dataList", reviewsetdataList);
		
		return result;
	}
	
}
