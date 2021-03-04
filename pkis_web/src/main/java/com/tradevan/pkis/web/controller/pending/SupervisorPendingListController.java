package com.tradevan.pkis.web.controller.pending;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;
import com.tradevan.pkis.web.service.contract.ContractService;
import com.tradevan.pkis.web.service.pending.SupervisorPendingService;
import com.tradevan.xauthframework.common.bean.ProcessResult;
import com.tradevan.xauthframework.web.controller.DefaultController;

/**
 * 作 業 代 碼 ：CONTRACT<br>
 * 作 業 名 稱 ：待辦事項<br>
 * 程 式 代 號 ：ContractListController<br>
 * 描             述 ：<br>
 * 公             司 ：Tradevan Co.<br><br>
 *【 資 料 來 源】  ：<br>
 *【 輸 出 報 表】  ：<br>
 *【 異 動 紀 錄】  ：<br>
 * @author   : bruce<br>
 * @version  : 1.0.0 2020/4/15<P>
 */
@RestController
@RequestMapping("/pending/supervisor")
public class SupervisorPendingListController extends DefaultController {
	
	@Autowired
	SupervisorPendingService supervisorPendingService;
	
	@Autowired
	ContractService contractService;

	/**
	 * 初始化頁面
	 * @return
	 */
	@RequestMapping("/")
	public ModelAndView init() {
		return new ModelAndView("/pending/supervisor_pending_list");		
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping("/view")
	public @ResponseBody Object view(HttpServletRequest request, @RequestParam Map<String, Object> params) throws Exception {
		ModelAndView mv = new ModelAndView("/pending/supervisor_pending_edit", "data", new HashMap<String, Object>());
		try {
			if (params != null && params.size() > 0) {
				String keyData = MapUtils.getString(params, "keyData");
				if (StringUtils.isNotBlank(keyData)) {
					Map<String, Object> keyMap = new Gson().fromJson(keyData, HashMap.class);
					Map<String, Object> data = supervisorPendingService.getFlowVeiwData(keyMap);
					mv.addObject("data", data);
				}
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
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
	public @ResponseBody Object detail(HttpServletRequest request, @RequestBody Map<String, Object> params) throws Exception {
		Map<String, Object> result = null;
		try {
			if (params != null && params.size() > 0) {
				String jsonData = MapUtils.getString(params, "data");
				if (StringUtils.isNotBlank(jsonData)) {
					Map<String, Object> keyData = new Gson().fromJson(jsonData, HashMap.class);
					logger.info("Json Params == " + jsonData);
					contractService.getJsonByEs(keyData);
				}
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 代辦清單Grid
	 * @param request
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/toDoList")
	public @ResponseBody Object toDoList(HttpServletRequest request, @RequestParam Map<String, Object> params) throws Exception {
		return supervisorPendingService.selectToDoList(params);
	}
	
	/**
	 * 主管批次簽核
	 * @param request
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/batchSign")
	public @ResponseBody Object batchSign(HttpServletRequest request, @RequestBody List<String> dataList) throws Exception {
		Map<String, Object> result = null;
		try {
			
			result = supervisorPendingService.batchSign(dataList);
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	/**
	 * 取得檢核結果下拉選單
	 * @param request
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/getCheckResultList")
	public @ResponseBody Object getCheckResultList(HttpServletRequest request, @RequestParam Map<String, Object> params) throws Exception {
		ProcessResult result = new ProcessResult();
		result.addResult("checkResultList", supervisorPendingService.getCheckResultList(null));
		
		return result;
	}
	
	/**
	 * 主管批次駁回
	 * @param request
	 * @param dataList
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/btnReject")
	public @ResponseBody Object btnReject(HttpServletRequest request, @RequestBody List<String> dataList) throws Exception {
		Map<String, Object> result = new HashMap<String, Object>();

		try {
			result = supervisorPendingService.btnReject(dataList);
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
}
