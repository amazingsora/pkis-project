package com.tradevan.pkis.web.controller.sample;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.tradevan.xauthframework.web.controller.DefaultController;

@RestController
@RequestMapping("/sample/thymeleaf")
public class ThymeleafController extends DefaultController {

	/**
	 * 初始化頁面
	 * @return
	 */
	@RequestMapping("/")
	public ModelAndView init() {
		return new ModelAndView("thymeleaf/login");
	}
}
