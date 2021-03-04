package com.tradevan.pkis.web.controller.supplierMaster;

import java.util.HashMap;
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
import com.tradevan.mapper.pkis.model.Suppliermaster;
import com.tradevan.pkis.web.service.supplierMaster.SupplierMasterService;
import com.tradevan.xauthframework.common.utils.JsonUtils;
import com.tradevan.xauthframework.dao.bean.GridResult;
import com.tradevan.xauthframework.web.controller.DefaultController;

/**
 * 作 業 代 碼 ：SUPPERMASTER<br>
 * 作 業 名 稱 ：供應基本資料<br>
 * 程 式 代 號 ：SupplierMasterController<br>
 * 描             述 ：<br>
 * 公             司 ：<br><br>
 *【 資 料 來 源】  ：<br>
 *【 輸 出 報 表】  ：<br>
 *【 異 動 紀 錄】  ：<br>
 * @author   : bruce<br>
 * @version  : 1.0.0 2020/7/7<P>
 */
@RestController
@RequestMapping("/supplier/master")
public class SupplierMasterController extends DefaultController{

	@Autowired
	SupplierMasterService supplierMasterService;
	
	/**
	 * 初始化頁面
	 * @return
	 */
	@RequestMapping("/")
	public ModelAndView init() {
		return new ModelAndView("/supplierMaster/supplier_list");
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
		ModelAndView mv = new ModelAndView("/supplierMaster/supplier_data", "data", new HashMap<String, Object>());
		
		if (params != null && params.size() > 0) {
			String jsonData = MapUtils.getString(params, "keyData");
			if (StringUtils.isNotBlank(jsonData)) {
				Map<String, Object> keyData = new Gson().fromJson(jsonData, HashMap.class);
				Suppliermaster suppliermaster = supplierMasterService.getUpdataData(keyData);
				//取得該筆編輯資料
				mv.addObject("data", suppliermaster);
			}
		}
		return mv;
	}
	
	/**
	 * 查詢資料
	 * @return
	 */
	@RequestMapping("/query")
	public @ResponseBody Object query(HttpServletRequest request, @RequestParam Map<String, Object> params) throws Exception{
		return supplierMasterService.selectSupplierList(params);
	}
	
	/**
	 * 新增資料
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/insertData")
	public @ResponseBody Object insertData(HttpServletRequest request, @RequestParam Map<String, Object> params) throws Exception{
		String jsonData = MapUtils.getString(params, "paramsData");
		Map<String, Object> paramsData = (Map<String, Object>)JsonUtils.json2Object(jsonData, Map.class, false);
		return supplierMasterService.insertSupplierData(paramsData);
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
		return supplierMasterService.updateSupplierData(paramsData);
	}
	
	/**
	 * 啟用/停用資料
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/bgnAndStop")
	public @ResponseBody Object stopData(HttpServletRequest request, @RequestParam Map<String, Object> params) throws Exception{
		String jsonData = MapUtils.getString(params, "paramsData");
		Map<String, Object> paramsData = (Map<String, Object>)JsonUtils.json2Object(jsonData, Map.class, false);
		return supplierMasterService.bgnOrStopSupplierData(paramsData);
	}
}
