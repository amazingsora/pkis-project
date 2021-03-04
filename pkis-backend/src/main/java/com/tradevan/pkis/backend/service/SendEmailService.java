package com.tradevan.pkis.backend.service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.tradevan.mapper.pkis.dao.EmailqueueMapper;
import com.tradevan.mapper.pkis.model.Emailqueue;
import com.tradevan.pkis.backend.config.DefaultService;
import com.tradevan.pkis.backend.utils.Const;

@Service("SendEmailService")
@Transactional(rollbackFor=Exception.class)
public class SendEmailService extends DefaultService {

	@Autowired
	EmailqueueMapper emailqueueMapper;
	
	@Autowired	
	private JavaMailSender javaMailSender;

	private static String SYS_USER = "SYSTEM";
	
	public String sendEmailSpring() throws Exception {
		MimeMessage mail = javaMailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(mail, true);
		List<String> errorMessageList = new ArrayList<String>();
		Emailqueue data = null;
		String errorMsg = "";
		
		QueryWrapper<Emailqueue> queryWrapper = new QueryWrapper<Emailqueue>();
		queryWrapper.eq("STATUS", "N");
		List<Emailqueue> emailqueueList = emailqueueMapper.selectList(queryWrapper);
		logger.info("SPRING_MAIL_SENDER == " + Const.SPRING_MAIL_SENDER);
		
		int i = 0;
		for(Emailqueue emailqueue : emailqueueList) {
			try {
				i++;
				helper.setTo(emailqueue.getMailto());
				helper.setFrom(Const.SPRING_MAIL_SENDER);
				helper.setSubject(emailqueue.getSubject());
				helper.setText(emailqueue.getContent(),	true);
				javaMailSender.send(mail);
				
				emailqueue.setStatus("Y");
				emailqueue.setUpdatetime(new Timestamp(new Date().getTime()));
				emailqueue.setUpdateuserid(SYS_USER);
				
				UpdateWrapper<Emailqueue> updateWrapper = new UpdateWrapper<Emailqueue>();
				updateWrapper.eq("SERNO", emailqueue.getSerno());
				emailqueueMapper.update(emailqueue, updateWrapper);
			}catch(Exception e) {
				e.printStackTrace();
				logger.error("第" + i + "筆失敗，原因為" + e.getMessage());
				errorMessageList.add("供應商資料第" + i + "筆失敗，原因為" + e.getMessage());
				errorMessageList.add(" ### Error Data ###  " + data);
				continue;
			}
		}
		errorMsg = createTxt(errorMessageList, "SendEmailBatchErrorTask.txt");
		
		return errorMsg;
	}
	
	public String createTxt(List<String> errorMessageList, String fileName) {
		String filePath = Const.INFORMATION_FIELD_DATAPATH;
		String errorMsg = "";
		
		try {
			if(errorMessageList.size() > 0) {
				Path path = Paths.get(fileName);
				StringBuffer msg = new StringBuffer();
				
				for(String errMsg : errorMessageList) {
					msg.append(errMsg);
				    msg.append("\n");
				}
				errorMsg = msg.toString();
				//java.nio.file.Files;
				Files.createDirectories(path);
				File file = new File(filePath + fileName);
				file.createNewFile();
				  
				FileWriter writer = new FileWriter(file);
				writer.write(errorMsg);
				writer.flush();
				writer.close();
			}
		} catch (IOException e) {
		  logger.error("Failed to create directory!" + e.getMessage());
		}
		
		return errorMsg;
	}
}
