package com.tradevan.pkis.junit.mapper;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tradevan.jpa.entity.XauthUsers;
import com.tradevan.jpa.entity.XauthUsersKey;
import com.tradevan.jpa.repository.XauthUsersRepository;
import com.tradevan.mapper.xauth.dao.XauthDeptMapper;
import com.tradevan.mapper.xauth.model.XauthDept;
import com.tradevan.pkis.junit.DefaultJunit;
import com.tradevan.xauthframework.common.utils.JsonUtils;


public class MapperTest extends DefaultJunit {
	
	@Autowired
	XauthDeptMapper xauthDeptMapper;
	
	@Autowired
	XauthUsersRepository xauthUsersRepository;

	@Test
	@Rollback(true)
	public void test() throws Exception {
		QueryWrapper<XauthDept> xauthDeptWrapper = new QueryWrapper<XauthDept>();
		xauthDeptWrapper.eq("APP_ID", "APPKIS");
		xauthDeptWrapper.eq("IDEN_ID", "00000000");
		XauthDept xauthDept = xauthDeptMapper.selectOne(xauthDeptWrapper);
		System.out.println(xauthDept);
		
		XauthUsersKey key = new XauthUsersKey();
		key.setAppId("APPKIS");
		key.setIdenId("00000000");
		key.setUserId("admin");		
		XauthUsers users = xauthUsersRepository.getOne(key);
		if (users != null) {
			System.out.println(JsonUtils.obj2json(users));			
		}
		else {
			System.out.println("no data");
		}
	}
	
}
