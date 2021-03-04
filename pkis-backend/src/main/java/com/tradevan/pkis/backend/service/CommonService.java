package com.tradevan.pkis.backend.service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.tradevan.mapper.pkis.dao.BatchparamsetMapper;
import com.tradevan.mapper.pkis.dao.EmailqueueMapper;
import com.tradevan.mapper.pkis.model.Batchparamset;
import com.tradevan.mapper.pkis.model.Emailqueue;
import com.tradevan.pkis.backend.config.DefaultService;
import com.tradevan.pkis.backend.utils.Const;

@Service("CommonService")
@Transactional(rollbackFor = Exception.class)
public class CommonService extends DefaultService {
	
	@Autowired
	BatchparamsetMapper batchparamsetMapper;
	
	@Autowired
	EmailqueueMapper emailqueueMapper;

	/**
	 * 取得批次設定檔資料
	 * @param className
	 * @return
	 * @throws Exception
	 */
	public Batchparamset getBatchData(String className) throws Exception {
		Batchparamset result = null;
		QueryWrapper<Batchparamset> batchparamsetWrapper = new QueryWrapper<Batchparamset>();
		batchparamsetWrapper.eq("BATCHNAME", className);
		List<Batchparamset> batchparamsets = batchparamsetMapper.selectList(batchparamsetWrapper);
		if(batchparamsets != null && batchparamsets.size() > 0) {
			result = batchparamsets.get(0);
		} else {
			logger.error("查無Batch Name : " + className + "設定檔資料");
		}
		
		return result;
	}
	
	public void updateBatchData(String className, String msg) throws Exception {
		Batchparamset batchData = getBatchData(className);
		if(batchData != null) { 
			QueryWrapper<Batchparamset> batchparamsetWrapper = new QueryWrapper<Batchparamset>();
			batchparamsetWrapper.eq("BATCHNAME", className);
			if(StringUtils.isNotBlank(msg)) {
				batchData.setLasterrordate(new Date());
				batchData.setLasterrormsg(msg);
			}
			batchData.setUpdateuser("SYSTEM");
			batchData.setUpdatedate(new Date());
			batchparamsetMapper.update(batchData, batchparamsetWrapper);
		}
	}
	
	public void sendErrorMail(String className, String msg) {
		Emailqueue mail = null;
		String mailMembers = Const.ERRORMSG_MAIL_ADDR;
		StringBuffer content = new StringBuffer();
		
		if(StringUtils.isBlank(msg)) {
			logger.info("批次完成，無錯誤訊息");
			return;
		}
		
		if(StringUtils.isNotBlank(mailMembers)) {
			content.append("執行批次發生錯誤，詳細內容如下：<BR>");
			content.append("批次名稱：" + className + "<BR>");
			content.append("錯誤內容：<BR>");
			content.append(msg);
			for(String mailMember : StringUtils.split(mailMembers, ",")) {
				mail = new Emailqueue();
				mail.setSysid(APP_ID);
				mail.setCategory("BATCH");
				mail.setMailto(mailMember);
				mail.setSubject("批次名稱：" + className + " 發生錯誤通知");
				mail.setContent(content.toString());
				mail.setSysmemo(className);
				mail.setStatus("N");
				mail.setPriority("1");
				mail.setCreateuserid("SYSTEM");
				mail.setCreatetime(new Timestamp(new Date().getTime()));
				mail.setUpdateuserid("SYSTEM");
				mail.setUpdatetime(new Timestamp(new Date().getTime()));
				emailqueueMapper.insert(mail);
			}
		} else {
			logger.info("批次完成，有錯誤訊息但未在設定檔設定收件者!");
		}
	}
}
