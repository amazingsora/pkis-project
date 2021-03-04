package com.tradevan.pkis.web.controller.xauth;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.tradevan.pkis.web.service.contract.ContractService;
import com.tradevan.pkis.web.service.supplierMaster.SupplierMasterService;
import com.tradevan.pkis.web.service.xauth.XauthService;
import com.tradevan.pkis.web.service.xauth.XauthTransferService;
import com.tradevan.xauthframework.web.controller.DefaultController;

/**
 * 作 業 代 碼 ：XAUTH_TRANSFER<br>
 * 作 業 名 稱 ：合約資料移轉<br>
 * 程 式 代 號 ：XauthTransferController<br>
 * 描             述 ：<br>
 *【 資 料 來 源】  ：<br>
 *【 輸 出 報 表】  ：<br>
 *【 異 動 紀 錄】  ：<br>
 * @version  : 1.0.0 2020/11/19<P>
 */

@RestController
@RequestMapping("/xauth/transfer")
public class XauthTransferController extends DefaultController {
	@Autowired
	SupplierMasterService supplierMasterService;
	
	@Autowired
	XauthService xauthService;
	
	@Autowired
	ContractService contractService;
	
	@Autowired
	XauthTransferService xauthTransferService;
	
	/**
	 * 初始化頁面
	 * @return
	 */
	@RequestMapping("/")
	public ModelAndView init(RedirectAttributes redirectAttributes) {		
		return new ModelAndView("/xauth/xauth_transfer");
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
		return xauthTransferService.selectTransferList(params);
	}
	
	/**
	 * 取得所有部門
	 * 
	 * @return
	 */
	@RequestMapping("/getAllSection")
	public @ResponseBody Object getAllSection(){
		return xauthTransferService.getAllSection();
	}
	
	/**
	 * 取得角色id
	 * 
	 * @param params
	 * @return
	 */
	@RequestMapping("/getUserid")
	public @ResponseBody Object getUserid(@RequestParam Map<String, Object> params){
		return xauthService.getuserid(params);
	}
	
	/**
	 * 移轉合約
	 * 
	 * @param request
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/setUndertaker")
	public @ResponseBody Object setUndertaker(@RequestParam Map<String, Object> params) throws Exception{
		return xauthTransferService.setUndertaker(params);
	}

}
