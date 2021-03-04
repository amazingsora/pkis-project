package com.tradevan.pkis.web.controller.perContractTemplate;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;
import com.tradevan.pkis.web.service.perContractTemplate.PerContractTemplateService;
import com.tradevan.xauthframework.common.utils.JsonUtils;
import com.tradevan.xauthframework.web.controller.DefaultController;

/**
 * 作 業 代 碼 ：CONTRACT<br>
 * 作 業 名 稱 ：個人範本查詢<br>
 * 程 式 代 號 ：PerContractTemplateInsertController<br>
 * 描             述 ：<br>
 * 公             司 ：Tradevan Co.<br><br>
 *【 資 料 來 源】  ：<br>
 *【 輸 出 報 表】  ：<br>
 *【 異 動 紀 錄】  ：<br>
 * @author   : bruce<br>
 * @version  : 1.0.0 2020/10/6<P>
 */
@RestController
@RequestMapping("/perContractTemplate/insert")
public class PerContractTemplateInsertController extends DefaultController {
	
	@Autowired
	PerContractTemplateService perContractTemplateService;
	
	/**
	 * 初始化頁面
	 * @return
	 */
	@RequestMapping("/")
	public ModelAndView init() {
		return new ModelAndView("/perContractTemplate/perContractTemplate_data");
	}
	
	/**
	 * 個人範本新增畫面
	 * @param request
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/insertData")
	public @ResponseBody Object insertData(HttpServletRequest request, @RequestParam Map<String, Object> params) throws Exception {
		String jsonData = MapUtils.getString(params, "paramsData");
		Map<String, Object> paramsData = JsonUtils.json2Object(jsonData, Map.class, false);
		logger.info("paramsDataMap == " + paramsData);
		
		return perContractTemplateService.insertData(paramsData);
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
		ModelAndView mv = new ModelAndView("/perContractTemplate/perContractTemplate_edit", "data", new HashMap<String, Object>());
		if(params.containsKey("keyData")) {
			String jsonData = MapUtils.getString(params, "keyData");
			params = JsonUtils.json2Object(jsonData, Map.class, false);
		}
		logger.info("params == " + params);
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
		
		return perContractTemplateService.getDetailData(keyData);
	}
	
	/**
	 * 編輯合約頁面
	 * @param request
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/updata")
	public @ResponseBody Object updata(HttpServletRequest request, @RequestBody String paramsStr) throws Exception {
		paramsStr = StringEscapeUtils.unescapeHtml3(paramsStr);
		logger.info("updata#params === " + paramsStr);
		Map<String, Object> params = JsonUtils.json2Object(paramsStr, Map.class);
		
		return perContractTemplateService.saveTempJson(params);
	}

}
