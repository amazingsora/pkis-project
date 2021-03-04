package com.tradevan.pkis.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tradevan.jpa.entity.XauthUsers;
import com.tradevan.jpa.entity.XauthUsersKey;
import com.tradevan.jpa.repository.XauthUsersRepository;
import com.tradevan.mapper.xauth.dao.XauthDeptMapper;
import com.tradevan.mapper.xauth.model.XauthDept;
import com.tradevan.pkis.backend.config.DefaultService;
import com.tradevan.xauthframework.common.utils.JsonUtils;

//@Service("SampleService")
public class SampleService extends DefaultService {
	
	@Autowired
	XauthDeptMapper xauthDeptMapper;
	
	@Autowired
	XauthUsersRepository xauthUsersRepository;
	
	public void process() throws Exception {
		logger.info("===== process begin =====");
		XauthDept xauthDept = new XauthDept();
		QueryWrapper<XauthDept> wrapper = new QueryWrapper<XauthDept>();
		wrapper.eq("APP_ID", APP_ID);
		wrapper.eq("IDEN_ID", "00000000");
		xauthDept= xauthDept.selectOne(wrapper);
		logger.info(JsonUtils.obj2json(xauthDept));
		
		XauthUsersKey key = new XauthUsersKey();
		key.setAppId(APP_ID);
		key.setIdenId("00000000");
		key.setUserId("admin");		
		XauthUsers users = xauthUsersRepository.getOne(key);
		if (users != null) {
			logger.info(JsonUtils.obj2json(users));			
		}
		else {
			logger.info("no data");
		}
	}

}
