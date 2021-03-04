package com.tradevan.pkis.web.controller.xauth; 
 
import java.util.Map; 
 
import javax.servlet.http.HttpServletRequest; 
 
import org.springframework.beans.factory.annotation.Autowired; 
import org.springframework.web.bind.annotation.RequestMapping; 
import org.springframework.web.bind.annotation.RequestParam; 
import org.springframework.web.bind.annotation.ResponseBody; 
import org.springframework.web.bind.annotation.RestController; 
import org.springframework.web.servlet.ModelAndView;

import com.tradevan.pkis.web.service.xauth.SettingPwdService;
import com.tradevan.xauthframework.common.bean.ProcessResult; 
import com.tradevan.xauthframework.web.controller.DefaultController; 
 
@RestController 
@RequestMapping("/changeForgetPwd") 
public class ForgetPwdController extends DefaultController { 
	 
	@Autowired 
	SettingPwdService settingPwdService; 
 
	/** 
	 * 初始化頁面 
	 * @return 
	 */ 
	@RequestMapping("/") 
	public ModelAndView init() { 
		return new ModelAndView("/forget_pwd"); 
	} 
	 
	@RequestMapping("/update") 
	public @ResponseBody Object update(HttpServletRequest request, @RequestParam Map<String, Object> params) throws Exception { 
		ModelAndView mv = new ModelAndView("/forget_pwd"); 
		ProcessResult updatePwdPr = settingPwdService.updatePwd(params); 
		mv.addObject("result", updatePwdPr); 
		return mv; 
	} 
} 