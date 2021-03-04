package com.tradevan.pkis.web.controller.xauth;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.google.gson.Gson;
import com.tradevan.mapper.pkis.model.Reviewconf;
import com.tradevan.pkis.web.service.xauth.XauthService;
import com.tradevan.xauthframework.common.bean.ProcessResult;
import com.tradevan.xauthframework.common.utils.ConvertUtils;
import com.tradevan.xauthframework.common.utils.JsonUtils;
import com.tradevan.xauthframework.web.controller.DefaultController;

/**
 * 簽審管理
 * @author Yuan
 *
 */
@RestController
@RequestMapping("/xauth/reviewPer")
public class XauthReviewPerController extends DefaultController {

	@Autowired
	XauthService xauthService;
	
	/**
	 * 初始化頁面
	 * @return
	 */
	@RequestMapping("/")
	public ModelAndView init() {
		return new ModelAndView("/xauth/xauth_review_per_list");
	}
	
	/**
	 * 分頁查詢
	 * @param request
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/query")
	public @ResponseBody Object query(HttpServletRequest request, @RequestParam Map<String, Object> params) throws Exception {
		
		return xauthService.searchSystemparam(params);
	}
	
	/**
	 * 詳細資料頁面
	 * @param request
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/data")
	public ModelAndView toDetailView(HttpServletRequest request, @RequestParam Map<String, Object> params) throws Exception {
		ModelAndView mv = new ModelAndView("/xauth/xauth_review_per_data");
		Map<String, Object> data = new HashMap<String, Object>();
		List<Map<String, Object>> idenList = new ArrayList<Map<String, Object>>();
		if(params != null && params.size() > 0) {
			String jsonData = MapUtils.getString(params, "keyData");
			if(StringUtils.isNotBlank(jsonData)) {
				Map<String, Object> keyData = new Gson().fromJson(jsonData, HashMap.class);
				logger.info("keyData === " + keyData);
				List<Map<String, Object>> datas = xauthService.getSystemparamBySerno(keyData);
				if (datas != null && datas.size() > 0) {				
					data = ConvertUtils.Map2Camel(datas.get(0));
					idenList = xauthService.getSystemparamIdens((String)data.get("idenids"));
				}
				logger.info("data == " + data);
				mv.addObject("data", data);
				mv.addObject("idenList", new Gson().toJson(idenList));
			}
		}
		
		return mv;
	}
	
	/**
	 * 新增資料
	 * @param request
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/insert")
	public @ResponseBody Object insert(HttpServletRequest request, @RequestParam Map<String, Object> params) throws Exception {
		String jsonData = MapUtils.getString(params, "paramsData");
		Map<String, Object> paramsData = (Map<String, Object>) JsonUtils.json2Object(jsonData, Map.class, false);
		logger.info("paramsData == " + paramsData);
		ProcessResult result = xauthService.insertSystemparam(paramsData);
		
		return result;
	}
	
	/**
	 * 修改資料
	 * @param request
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/update")
	public @ResponseBody Object update(HttpServletRequest request, @RequestParam Map<String, Object> params) throws Exception {
		String jsonData = MapUtils.getString(params, "paramsData");
		Map<String, Object> paramsData = (Map<String, Object>) JsonUtils.json2Object(jsonData, Map.class, false);
		logger.info("paramsData == " + paramsData);
		ProcessResult result = xauthService.updateSystemparam(paramsData);
		
		return result;
	}
	
	/**
	 * 刪除資料
	 * @param request
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/delete")
	public @ResponseBody Object delete(HttpServletRequest request, @RequestParam Map<String, Object> params) throws Exception {
		String jsonData = MapUtils.getString(params, "paramsData");
		Map<String, Object> paramsData = (Map<String, Object>) JsonUtils.json2Object(jsonData, Map.class, false);
		ProcessResult result = xauthService.deleteSystemparam(paramsData);
		
		return result;
	}
	
	/**
	 * 取得ActionType用途 之下拉選單
	 * @param request
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/getActionTypeList")
	public @ResponseBody Object getActionTypeList(HttpServletRequest request, @RequestParam Map<String, Object> params) throws Exception {
		
		String contractmodel = MapUtils.getString(params, "contractmodel");
		List<Map<String, Object>> getActionTypeList = xauthService.getActionTypeList(contractmodel);
		ProcessResult result = new ProcessResult();
		result.addResult("dataList", getActionTypeList);
		
		return result;
	}
	
	/**
	 * 取得flow 之下拉選單
	 * @param request
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/getFlowList")
	public @ResponseBody Object getFlowList(HttpServletRequest request, @RequestParam Map<String, Object> params) throws Exception {
		
		String contractmodel = MapUtils.getString(params, "contractmodel");
		List<Reviewconf> getFlowList = xauthService.getFlowList(contractmodel);
		ProcessResult result = new ProcessResult();
		result.addResult("dataList", getFlowList);
		return result;
	}
	
	/**
	 * 取得被簽審部門
	 * @param request
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/getBeSignDept")
	public @ResponseBody Object getBeSignDept(HttpServletRequest request, @RequestParam Map<String, Object> params) throws Exception {
		ProcessResult result = new ProcessResult();
		List<Map<String, Object>> deptList = xauthService.getBeSignDept(MapUtils.getString(params, "idenIdSelected"));
		result.addResult("deptList", deptList);
		
		return result;
	}
}
