package com.tradevan.pkis.web.controller.contract;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/contract/insert")
public class ContractInsertController {
	
	@RequestMapping("/")
	public @ResponseBody Object insert(HttpServletRequest request, @RequestParam Map<String, Object> params)throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		ModelAndView mv = new ModelAndView("/contract/contract_insert");
		mv.addObject("creDateDesc", sdf.format(new Date()));
		return mv;
	}

}
