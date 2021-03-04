package com.tradevan.pkis.web.controller.xauth; 
 
import org.apache.commons.lang3.StringUtils; 
import org.springframework.beans.factory.annotation.Autowired; 
import org.springframework.web.bind.annotation.PathVariable; 
import org.springframework.web.bind.annotation.RequestMapping; 
import org.springframework.web.bind.annotation.RestController; 
import org.springframework.web.servlet.ModelAndView;

import com.tradevan.pkis.web.service.xauth.SettingPwdService;
import com.tradevan.xauthframework.common.bean.ProcessResult; 
import com.tradevan.xauthframework.web.controller.DefaultController; 
 
@RestController 
@RequestMapping("/resetPassword") 
public class ResetPwdController extends DefaultController { 
	 
	@Autowired 
	SettingPwdService settingPwdService; 
	 
	/** 
	 * 初始化頁面 
	 * @return 
	 */ 
	@RequestMapping("/{emailToken}") 
	public ModelAndView init(@PathVariable String emailToken) { 
		logger.info("ResetPwdController#start"); 
		logger.info("emailToken == " + emailToken); 
		ModelAndView mv = new ModelAndView("/reset_pwd"); 
		ProcessResult checkResetTokenResult = settingPwdService.checkResetToken(emailToken); 
		if(StringUtils.equals(checkResetTokenResult.getStatus(), ProcessResult.OK)) { 
			mv.addObject("emailToken", emailToken); 
		} else { 
			mv.addObject("tokenFail", "Y"); 
			mv.addObject("status", checkResetTokenResult.getStatus()); 
			mv.addObject("msg", checkResetTokenResult.getMessages()); 
		} 
		return mv; 
	} 
} 