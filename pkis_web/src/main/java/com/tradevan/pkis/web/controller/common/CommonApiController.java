package com.tradevan.pkis.web.controller.common;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.tradevan.mapper.pkis.model.Ssotoken;
import com.tradevan.pkis.web.bean.apiRequest.GetSupplierIdRequest;
import com.tradevan.pkis.web.bean.apiResponse.GetSupplierIdResponse;
import com.tradevan.pkis.web.bean.apiResponse.SsoResponse;
import com.tradevan.pkis.web.service.common.CommonApiService;
import com.tradevan.xauthframework.common.utils.JsonUtils;
import com.tradevan.xauthframework.web.controller.DefaultController;

/**
 * 作 業 代 碼 ：CommonApiController<br>
 * 作 業 名 稱 ：共用api<br>
 * 程 式 代 號 ：CommonApiController<br>
 * 描             述 ：<br>
 * 公             司 ：Tradevan Co.<br><br>
 *【 資 料 來 源】  ：<br>
 *【 輸 出 報 表】  ：<br>
 *【 異 動 紀 錄】  ：<br>
 * @author   : Yuan<br>
 * @version  : 1.0.0 2021/1/19<P>
 */
@RestController
@RequestMapping("/common/api")
public class CommonApiController extends DefaultController {
	
	@Autowired
	CommonApiService commonApiService;
	
	@Autowired
	UserDetailsService userDetailsServiceImpl;
	
	/**
	 * 取供應商帳號
	 * @param request
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/getSupplierId")
	public @ResponseBody GetSupplierIdResponse getSupplierId(@RequestBody GetSupplierIdRequest request) {
		GetSupplierIdResponse response = new GetSupplierIdResponse();
		try{
			logger.info("REQUEST：" + JsonUtils.obj2json(request));
			response = commonApiService.execute(request);
		} catch(Exception e) {
			response.setReturnCode("999");
			response.setReturnMsg("交易失敗");
		}
		
		return response;
	}
	
	/**
	 * sso取token
	 * @param request
	 * @return
	 */
	@RequestMapping("/sso")
	public @ResponseBody SsoResponse sso(HttpServletRequest request) {
		SsoResponse response = new SsoResponse();
		try {
			commonApiService.getSsoRequest(request);
			response = commonApiService.ssoExecute(commonApiService.getSsoRequest(request));
		} catch(Exception e) {
			response.setRETURNCODE("999");
			response.setRETURNMSG("交易失敗");
		}
		
		return response;
	}
	
	/**
	 * sso導頁
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/sso/page")
	public ModelAndView ssoPage(HttpServletRequest request) throws Exception {
		ModelAndView mv = null;
		String token = request.getParameter("TOKEN");
		List<Map<String, Object>> suppliermasterDatas = null;
		Ssotoken ssotoken = commonApiService.getSsotokenData(token);
		List<String> spids = null;
		
		if(ssotoken != null) {
			spids = JsonUtils.json2Object(ssotoken.getSpidjson(), List.class);
			if(ssotoken.getSpidcount() > 1) { // 多筆供應商帳號
				mv = new ModelAndView("/sso_list");
				suppliermasterDatas = commonApiService.getSupplierData(spids);
				mv.addObject("suppliermasterDatas", suppliermasterDatas);
				mv.addObject("token", token);
				logger.info("多筆供應商帳號，導頁至 : " + mv.getViewName());
			} else { // 單筆供應商帳號
				mv = new ModelAndView("redirect:/main/index");
				commonApiService.autoLogin(request, spids.get(0), token);
				logger.info("登入成功，導頁至 : " + mv.getViewName());
			}
		}
		
		return mv;
	}
	
	/**
	 * 從多帳號畫面登入
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/sso/login")
	public ModelAndView ssoLogin(HttpServletRequest request) throws Exception {
		ModelAndView mv = new ModelAndView("redirect:/main/index");
		String supplierid = request.getParameter("supplierid");
		String token = request.getParameter("token");
		commonApiService.autoLogin(request, supplierid, token);
		logger.info("登入成功，導頁至 : " + mv.getViewName());
		
		return mv;	
	}
}
