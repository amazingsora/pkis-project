package com.tradevan.pkis.web.config;

import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.tradevan.xauthframework.core.common.LocaleMessage;
import com.tradevan.xauthframework.core.enums.MSG_KEY;

@ControllerAdvice
public class WebExceptionHandler {

	private Logger logger = LogManager.getLogger(getClass());	
	
	@Value("${msg.show.error}")
	private String MSG_SHOW_ERROR;
	
	@ExceptionHandler({ Exception.class })
	public @ResponseBody Object handleException(HttpServletRequest request, HttpServletResponse response, Exception e) {
		logger.error(e);
		ModelAndView mv = new ModelAndView();
		String httpState = getHttpState(request);		
		String msg = parseException(e);
		if (isAjax(request)) {			
            try {
            	PrintWriter writer = response.getWriter();
            	response.setCharacterEncoding("utf-8");
            	writer.write(msg);
            	writer.flush();
            }
            catch (Exception ex) {
            	ex.printStackTrace();
            }
            return null;
		}
		else {
			if (MSG_SHOW_ERROR.equalsIgnoreCase("Y")) {
				msg = getExceptionMessage(e);
			}
			if (StringUtils.isNotBlank(httpState)) {
				msg += "(" + httpState + ")";
			}
			mv.addObject("msg", "系統發生錯誤:" + msg);
			mv.setViewName("/msg/sys_msg");
		}
		return mv;
	}
	
	public String parseException(Exception e) {
		if (e instanceof org.springframework.dao.DuplicateKeyException) {
        	return MSG_KEY.DATA_DUP.getMessage();
		}
		return LocaleMessage.getMsg("message.system.error");
	}
	
	private String getExceptionMessage(Throwable e) {
        String message = "";
        
        if (e instanceof org.springframework.dao.DuplicateKeyException) {
        	return MSG_KEY.DATA_DUP.getMessage();
		}
        
        while (e != null) {
            message += e.getMessage() + "\n";
            e = e.getCause();
        }
        return message;
    }
	
	private boolean isAjax(HttpServletRequest request) {
        return "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));
    }
	
	private String getHttpState(HttpServletRequest request) {
		Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
		if (status != null) {
			Integer statusCode = Integer.valueOf(status.toString());
			for (HttpStatus s : HttpStatus.values()) {
				if (statusCode.equals(s.value())) {
					return s.toString();
				}
			}
		}
		return null;
	}
}
