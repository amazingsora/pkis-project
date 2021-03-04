package com.tradevan.pkis.web.xss;

import java.util.Map;

import javax.servlet.DispatcherType;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.common.collect.Maps;

@Configuration
public class XssFilterConfiguration {
	
	@Value("${xss.enabled}")
	private String XSS_ENABLED;
	
	@Value("${xss.excludes}")
	private String XSS_EXCLUDES;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Bean
    public FilterRegistrationBean xssFilterRegistrationBean() {
		FilterRegistrationBean initXssFilterBean = new FilterRegistrationBean();
		if (XSS_ENABLED.equalsIgnoreCase("Y")) {
			initXssFilterBean.setFilter(new XSSFilter());
		}
		else {
			initXssFilterBean.setFilter(new DefaultFilter());
		}
        initXssFilterBean.setOrder(1);
        initXssFilterBean.setEnabled(true);
        initXssFilterBean.addUrlPatterns("/*");
        initXssFilterBean.setDispatcherTypes(DispatcherType.REQUEST);
        Map<String, String> initParameters = Maps.newHashMap();
        initParameters.put("excludes", XSS_EXCLUDES);
        initXssFilterBean.setInitParameters(initParameters);      
        return initXssFilterBean;
    }
	
}
