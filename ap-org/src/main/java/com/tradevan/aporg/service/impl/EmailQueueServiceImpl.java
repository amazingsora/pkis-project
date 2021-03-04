package com.tradevan.aporg.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.tradevan.apcommon.bean.CreateUserDto;
import com.tradevan.aporg.enums.MailCategory;
import com.tradevan.aporg.model.EmailQueue;
import com.tradevan.aporg.repository.EmailQueueRepository;
import com.tradevan.aporg.service.EmailQueueService;

/**
 * Title: EmailQueueServiceImpl<br>
 * Description: <br>
 * Company: Tradevan Co.<br>
 * 
 * @author Neil Chen
 * @version 1.3
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class EmailQueueServiceImpl implements EmailQueueService {

	@Autowired
	private EmailQueueRepository emailQueueRepository;
	
	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false, rollbackFor = Exception.class)
	public long add2EmailQueue(String sysId, MailCategory category, String mailTo, String subject, String body, String createUserId) {
		return add2EmailQueue(sysId, category, mailTo, subject, body, null, createUserId);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false, rollbackFor = Exception.class)
	public long add2EmailQueue(String sysId, MailCategory category, String mailTo, String subject, String body, CreateUserDto createUserDto) {
		return add2EmailQueue(sysId, category, mailTo, subject, body, null, createUserDto);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false, rollbackFor = Exception.class)
	public long add2EmailQueue(String sysId, MailCategory category, String mailTo, String mailToCc, String mailToBcc, String subject, String body, CreateUserDto createUserDto) {
		return add2EmailQueue(sysId, category, mailTo, mailToCc, mailToBcc, subject, body, null, createUserDto);
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false, rollbackFor = Exception.class)
	public long add2EmailQueue(String sysId, MailCategory category, String mailTo, String subject, String body, String attachment, String createUserId) {
		EmailQueue emailQueue = new EmailQueue(sysId, category, mailTo, subject, body, createUserId);
		emailQueue.setAttachment(attachment);
		emailQueue = emailQueueRepository.save(emailQueue);
		return emailQueue.getId();
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false, rollbackFor = Exception.class)
	public long add2EmailQueue(String sysId, MailCategory category, String mailTo, String subject, String body, String attachment, CreateUserDto createUserDto) {
		EmailQueue emailQueue = new EmailQueue(sysId, category, mailTo, subject, body, createUserDto);
		emailQueue.setAttachment(attachment);
		emailQueue = emailQueueRepository.save(emailQueue);
		return emailQueue.getId();
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false, rollbackFor = Exception.class)
	public long add2EmailQueue(String sysId, MailCategory category, String mailTo, String mailToCc, String mailToBcc, String subject, String body, String attachment, CreateUserDto createUserDto) {
		EmailQueue emailQueue = new EmailQueue(sysId, category, mailTo, subject, body, createUserDto);
		emailQueue.setMailToCc(mailToCc);
		emailQueue.setMailToBcc(mailToBcc);
		emailQueue.setAttachment(attachment);
		emailQueue = emailQueueRepository.save(emailQueue);
		return emailQueue.getId();
	}

}
