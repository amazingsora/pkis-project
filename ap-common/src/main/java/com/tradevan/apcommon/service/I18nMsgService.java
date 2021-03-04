package com.tradevan.apcommon.service;

/**
 * Title: I18nMsgService<br>
 * Description: <br>
 * Company: Tradevan Co.<br>
 * 
 * @author Neil Chen
 * @version 1.0
 */
public interface I18nMsgService {

	String getText(String i18nCode, Object... args);
	
}
