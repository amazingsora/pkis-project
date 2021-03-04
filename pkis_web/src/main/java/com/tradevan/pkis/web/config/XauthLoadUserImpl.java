package com.tradevan.pkis.web.config;

import java.util.Calendar;
import java.util.Date;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Component;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tradevan.mapper.pkis.model.UserInfoExt;
import com.tradevan.mapper.xauth.model.XauthUsers;
import com.tradevan.xauthframework.core.security.IXauthLoadUserProvider;
import com.tradevan.xauthframework.web.service.DefaultService;

/**
 * 作 業 代 碼 ：XauthLoadUserImpl<br>
 * 作 業 名 稱 ：取得使用者資訊<br>
 * 程 式 代 號 ：XauthLoadUserImpl<br>
 * 描             述 ：自訂取得登入使用者資料的方式<br>
 * 公             司 ：Tradevan Co.<br><br>
 *【 資 料 來 源】  ：<br>
 *【 輸 出 報 表】  ：<br>
 *【 異 動 紀 錄】  ：<br>
 * @author   : penny<br>
 * @version  : 1.0.0 2020/3/13<P>
 */
@Component
public class XauthLoadUserImpl extends DefaultService implements IXauthLoadUserProvider {

	@Override
	public XauthUsers getXauthUser(String idenId, String userId) throws Exception {
		XauthUsers xauthUsers = new XauthUsers();
		QueryWrapper<XauthUsers> wrapper = new QueryWrapper<XauthUsers>();
		wrapper.eq("APP_ID", APP_ID);
		wrapper.eq("USER_ID", userId);
		xauthUsers = xauthUsers.selectOne(wrapper);
		if (xauthUsers != null) {
			UserInfoExt userInfoExt = new UserInfoExt();
			QueryWrapper<UserInfoExt> userInfoExtWrapper = new QueryWrapper<UserInfoExt>();
			userInfoExtWrapper.eq("APP_ID", APP_ID);
			userInfoExtWrapper.eq("USER_ID", userId);
			userInfoExt = userInfoExt.selectOne(userInfoExtWrapper);
			if (userInfoExt != null) {
				Date currentDate = new Date(); 
				if (currentDate.before(userInfoExt.getBgnDate())) {
					throw new BadCredentialsException("message.user.error.period");
				}
				else {
					Date endDate = userInfoExt.getEndDate();
					if (endDate != null) {
						Calendar calendar = Calendar.getInstance();
						calendar.setTime(endDate);
						calendar.set(Calendar.HOUR, 23);
						calendar.set(Calendar.MINUTE, 59);
						calendar.set(Calendar.SECOND, 59);
						if (currentDate.after(calendar.getTime())) {
							throw new BadCredentialsException("message.user.error.period");
						}
					}
				}
			}
		}
		return xauthUsers;
	}

}
