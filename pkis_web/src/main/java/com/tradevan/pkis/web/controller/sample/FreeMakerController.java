package com.tradevan.pkis.web.controller.sample;

import java.io.StringWriter;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.RequestContext;

import com.tradevan.xauthframework.common.bean.ProcessResult;
import com.tradevan.xauthframework.common.utils.MapUtils;
import com.tradevan.xauthframework.web.controller.DefaultController;

import freemarker.template.Configuration;
import freemarker.template.Template;

@RestController
@RequestMapping("/sample/freemaker")
public class FreeMakerController extends DefaultController {

	@Autowired(required=false)
    Configuration cfg;
	
	/**
	 * 初始化頁面
	 * @return
	 */
	@RequestMapping("/")
	public ModelAndView init() {
		return new ModelAndView("/sample/freemaker");
	}
	
	@RequestMapping("/tmp")
	public @ResponseBody Object tmp(HttpServletRequest request,  @RequestParam Map<String, Object> params) throws Exception {
		StringWriter stringWriter = new StringWriter();
		String tmpCode = MapUtils.getString(params, "tmp");
		String template = "/sample/";
		if (tmpCode.equals("01")) {
			template += "t1.ftl";
		}
		else if (tmpCode.equals("02")) {
			template += "t2.ftl";
		}
		Template temp = cfg.getTemplate(template);		
		params.put("springMacroRequestContext", new RequestContext(request, null, null, null));
		temp.process(params, stringWriter);			
		ProcessResult result = new ProcessResult();
		result.setStatus(ProcessResult.OK);
		result.setObject(stringWriter.toString());
		return result;
	}
}
