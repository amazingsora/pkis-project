package com.tradevan.pkis.web.controller.perContractTemplate;

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

import com.tradevan.mapper.xauth.model.XauthSysCode;
import com.tradevan.pkis.web.service.perContractTemplate.PerContractTemplateService;
import com.tradevan.xauthframework.common.bean.ProcessResult;
import com.tradevan.xauthframework.common.utils.JsonUtils;
import com.tradevan.xauthframework.web.controller.DefaultController;

/**
 * 作 業 代 碼 ：CONTRACT<br>
 * 作 業 名 稱 ：個人範本查詢<br>
 * 程 式 代 號 ：PerContractTemplateController<br>
 * 描             述 ：<br>
 * 公             司 ：Tradevan Co.<br><br>
 *【 資 料 來 源】  ：<br>
 *【 輸 出 報 表】  ：<br>
 *【 異 動 紀 錄】  ：<br>
 * @author   : bruce<br>
 * @version  : 1.0.0 2020/10/6<P>
 */
@RestController
@RequestMapping("/percontractTemplate/list")
public class PerContractTemplateController extends DefaultController {
	
	@Autowired
	PerContractTemplateService perContractTemplateService;
	
	/**
	 * 初始化頁面
	 * @return
	 */
	@RequestMapping("/")
	public ModelAndView init() {
		return new ModelAndView("/perContractTemplate/perContractTemplate_list");
	}
	
	/**
	 * 查詢
	 * @param request
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/query")
	public @ResponseBody Object query(HttpServletRequest request, @RequestParam Map<String, Object> params) throws Exception {
		logger.info("query#start");
		return perContractTemplateService.queryPercontracttemplate(params);
	}
	
	/**
	 * 設定啟用
	 * @param request
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/setBeginVal")
	public @ResponseBody Object setBeginVal(HttpServletRequest request, @RequestParam Map<String, Object> params) throws Exception {
		String jsonData = MapUtils.getString(params, "paramsData");
		Map<String, Object> paramsData = JsonUtils.json2Object(jsonData, Map.class, false);
		
		return perContractTemplateService.setBeginVal(paramsData);
	}
	
	/**
	 * 設定停用
	 * @param request
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/setStopVal")
	public @ResponseBody Object setStopVal(HttpServletRequest request, @RequestParam Map<String, Object> params) throws Exception {
		String jsonData = MapUtils.getString(params, "paramsData");
		Map<String, Object> paramsData = JsonUtils.json2Object(jsonData, Map.class, false);
		
		return perContractTemplateService.setStopVal(paramsData);
	}
	/**
	 * 個人範本合約模式
	 * @param request
	 * @param json
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/getTemplatemoudle")
	public @ResponseBody Object getmoudle() throws Exception {
		List<XauthSysCode> moudlelist = perContractTemplateService.getTemplatemoudle();
		ProcessResult result = new ProcessResult();
		result.addResult("dataList", moudlelist);
		return result;
	}

}
