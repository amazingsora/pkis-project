package com.tradevan.pkis.web.ssoController;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.tradevan.xauthframework.web.controller.DefaultController;

@RestController
@RequestMapping("/ssoList")
public class SsoTokenController extends DefaultController {

//	@RequestMapping("/")
//	public String ssoApi() throws Exception {
//		logger.info("SsoController#start");
//		String json = "{\"identity \":\"tvPcrc\",\"identityPw\":\"vSSOPcrc!@#$\",\"lgcAccount\":\"\"}";
//		Cryptology cryptology = new Cryptology();
//		String test = cryptology.encrypt(json);
//		
//		return test;
//	}
	
	@RequestMapping("/")
	public ModelAndView ssoList(HttpServletRequest request, @RequestParam Map<String, Object> params) throws Exception {
		logger.info("ssoList#start");
		
		return new ModelAndView("/sso_list");
	}
}
