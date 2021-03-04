package com.tradevan.pkis.web.controller.supplierMaster;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.tradevan.mapper.pkis.model.Suppliermaster;
import com.tradevan.pkis.web.service.supplierMaster.SupplierMasterService;
import com.tradevan.xauthframework.common.utils.JsonUtils;
import com.tradevan.xauthframework.core.security.UserInfo;
import com.tradevan.xauthframework.web.controller.DefaultController;

/**
 * 作 業 代 碼 ：SUPPERMASTER<br>
 * 作 業 名 稱 ：供應基本資料維護<br>
 * 程 式 代 號 ：SupplierMasterMaintenanceController<br>
 * 描             述 ：<br>
 * 公             司 ：<br><br>
 *【 資 料 來 源】  ：<br>
 *【 輸 出 報 表】  ：<br>
 *【 異 動 紀 錄】  ：<br>
 * @version  : 1.0.0 2020/7/7<P>
 */
@RestController
@RequestMapping("/supplier/maintenance")
public class SupplierMasterMaintenanceController extends DefaultController {
	
	@Autowired
	SupplierMasterService supplierMasterService;
	
	/**
	 * 供應商基本資料維護初始化頁面
	 * @return
	 */
	@RequestMapping("/")
	public ModelAndView init() {
		ModelAndView mv = new ModelAndView("/supplierMaster/supplier_maintenance", "data", new HashMap<String, Object>());
		UserInfo userInfo = userContext.getCurrentUser();
		Map<String, Object> keyMap = new HashMap <String, Object>();
		keyMap.put("supplierid", userInfo.getUserId());
		
		Suppliermaster suppliermaster = supplierMasterService.getUpdataData(keyMap);
		mv.addObject("data", suppliermaster);

		return  mv;
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
	
	
	
	
}
