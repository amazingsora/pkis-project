package com.tradevan.pkis.web.aspect;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.tradevan.mapper.xauth.dao.XauthMapper;
import com.tradevan.xauthframework.core.security.UserContext;
import com.tradevan.xauthframework.core.security.UserInfo;

@Aspect
@Component
public class ControllerBreadCrumbAspect {
	
	@Value("${xauth.appId}")
	private String APP_ID;

	@Autowired
	XauthMapper xauthMapper;
	
	@Autowired
	private UserContext userContext;
	
	@Autowired
	private HttpServletRequest request;
	
	@Around("( execution(* com.tradevan..*.controller..*..*(..)) ) && @annotation(requestMapping)")
	public Object aroundMethod(ProceedingJoinPoint joinpoint, RequestMapping requestMapping) throws Throwable {
		UserInfo userInfo = userContext.getCurrentUser();
		Object obj = joinpoint.proceed();
		if (userInfo != null && userInfo.isSso()) {
			return obj;
		}
		if (obj instanceof ModelAndView) {
			ModelAndView mv = (ModelAndView) obj;
			String uri = request.getRequestURI();
			if (StringUtils.isNotBlank(uri)) {
				String[] uriArray = uri.split("/");
				if (uriArray.length > 3) {				
					String menuUrl = "/" + uriArray[2] + "/" + uriArray[3] + "/";
					Map<String, Object> params = new HashMap<String, Object>();
					params.put("appId", APP_ID);
					params.put("menuUrl", menuUrl);
					List<Map<String, Object>> list = xauthMapper.selectMenuTreeDescByUrl(params);
					if (list != null && list.size() > 0) {
						String breadStr = "";
						String menuName = "";
						for (int i = 0; i < list.size() - 1; i++) {
							if (i == 0) {
								menuName = list.get(i).get("TEXT").toString();
							}
							breadStr = "/" + list.get(i).get("TEXT").toString() + breadStr;
						}
						mv.addObject("bread", breadStr);
						mv.addObject("menuName", menuName);
					}
				}			
			}
			return mv;
		}
		else {
			return obj;
		}		
	}
	
}
