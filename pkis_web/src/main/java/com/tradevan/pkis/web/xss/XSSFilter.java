package com.tradevan.pkis.web.xss;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class XSSFilter implements Filter {
	
	Logger logger = LogManager.getLogger(XSSFilter.class);
	
	public List<String> excludes = new ArrayList<>();
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		String ex = filterConfig.getInitParameter("excludes");
		if (StringUtils.isNotBlank(ex)) {
			String[] url = ex.split(",");
			for (int i = 0; url != null && i < url.length; i++) {
				excludes.add(url[i]);
			}
		}
		logger.info("xss exclude url:" + excludes);
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;
  		if (handleExcludeURL(req, resp)) {
  			chain.doFilter(request, response);
  			return;
  		}
  		chain.doFilter(new XssHttpServletRequestWrapper((HttpServletRequest) request), response);
	}

	private boolean handleExcludeURL(HttpServletRequest request, HttpServletResponse response) {		 
		if (excludes == null || excludes.isEmpty()) {
			return false;
		}
 
		String url = request.getServletPath();
		for (String pattern : excludes) {
			Pattern p = Pattern.compile("^" + pattern);
			Matcher m = p.matcher(url);
			if (m.find()) {
				return true;
			}
		}
		return false;
	}
}
