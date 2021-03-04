package com.tradevan.pkis.test.jpa;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.tradevan.jpa.entity.XauthUsers;
import com.tradevan.jpa.entity.XauthUsersKey;
import com.tradevan.jpa.repository.XauthUsersRepository;
import com.tradevan.pkis.web.PKISApplication;
import com.tradevan.xauthframework.common.utils.JsonUtils;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = PKISApplication.class)
public class JpaTestJunit {

	@Autowired
	private XauthUsersRepository xauthUsersRepository;
	
	@Test
	public void test() throws Exception {
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
