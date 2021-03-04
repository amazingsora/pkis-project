package com.tradevan.aporg.service;

import com.tradevan.apcommon.bean.CreateUserDto;
import com.tradevan.aporg.enums.MailCategory;

/**
 * Title: EmailQueueService<br>
 * Description: <br>
 * Company: Tradevan Co.<br>
 * 
 * @author Neil Chen
 * @version 1.3
 */
public interface EmailQueueService {

	long add2EmailQueue(String sysId, MailCategory category, String mailTo, String subject, String body, String createUserId);
	
	long add2EmailQueue(String sysId, MailCategory category, String mailTo, String subject, String body, CreateUserDto createUserDto);
	
	long add2EmailQueue(String sysId, MailCategory category, String mailTo, String mailToCc, String mailToBcc, String subject, String body, CreateUserDto createUserDto);
	
	long add2EmailQueue(String sysId, MailCategory category, String mailTo, String subject, String body, String attachment, String createUserId);
	
	long add2EmailQueue(String sysId, MailCategory category, String mailTo, String subject, String body, String attachment, CreateUserDto createUserDto);
	
	long add2EmailQueue(String sysId, MailCategory category, String mailTo, String mailToCc, String mailToBcc, String subject, String body, String attachment, CreateUserDto createUserDto);
	
}
