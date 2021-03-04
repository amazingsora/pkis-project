package com.tradevan.apcommon.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import com.tradevan.apcommon.service.I18nMsgService;

/**
 * Title: I18nMsgServiceImpl<br>
 * Description: <br>
 * Company: Tradevan Co.<br>
 * 
 * @author Neil Chen
 * @version 1.0
 */
@Service
public class I18nMsgServiceImpl implements I18nMsgService {

	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private MessageSource messageSource;

	@Override
	public String getText(String i18nCode, Object... args) {
		try {
			return messageSource.getMessage(i18nCode, args, LocaleContextHolder.getLocale());
		}
		catch (Exception e) {
			logger.error(e.getMessage());
			return i18nCode;
		}
	}

}
