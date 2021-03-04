package com.tradevan.pkis.web.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tradevan.mapper.pkis.dao.DeptInfoExtMapper;
import com.tradevan.mapper.pkis.dao.UserInfoExtMapper;
import com.tradevan.mapper.pkis.model.DeptInfoExt;
import com.tradevan.mapper.pkis.model.UserInfoExt;
import com.tradevan.mapper.xauth.dao.XauthDeptMapper;
import com.tradevan.pkis.web.bean.UserData;
import com.tradevan.xauthframework.core.security.IXauthUserDetail;

/**
 * 作 業 代 碼 ：XauthUserDetailImpl<br>
 * 作 業 名 稱 ：取得使用者擴充資訊<br>
 * 程 式 代 號 ：XauthUserDetailImpl<br>
 * 描             述 ：自訂取得登入使用者擴充資料<br>
 * 公             司 ：Tradevan Co.<br><br>
 *【 資 料 來 源】  ：<br>
 *【 輸 出 報 表】  ：<br>
 *【 異 動 紀 錄】  ：<br>
 * @author   : penny<br>
 * @version  : 1.0.0 2020/3/16<P>
 */
@Component
public class XauthUserDetailImpl implements IXauthUserDetail {
	
	@Autowired
	XauthDeptMapper xauthDeptMapper;
	
	@Autowired
	DeptInfoExtMapper deptInfoExtMapper;
	
	@Autowired
	UserInfoExtMapper userInfoExtMapper;

	@Override
	public Object setObject(String appId, String idenId, String userId) throws Exception {
		UserData userData = new UserData();
		QueryWrapper<DeptInfoExt> deptInfoExtWrapper = new QueryWrapper<DeptInfoExt>();
		deptInfoExtWrapper.eq("APP_ID", appId);
		deptInfoExtWrapper.eq("IDEN_ID", idenId);
		DeptInfoExt deptInfoExt = deptInfoExtMapper.selectOne(deptInfoExtWrapper);
		if (deptInfoExt != null) {
			userData.setIdenType(deptInfoExt.getIdenType());
		}
				
		QueryWrapper<UserInfoExt> userInfoExtWrapper = new QueryWrapper<UserInfoExt>();
		userInfoExtWrapper.eq("APP_ID", appId);
		userInfoExtWrapper.eq("IDEN_ID", idenId);
		userInfoExtWrapper.eq("USER_ID", userId);
		UserInfoExt userInfoExt = userInfoExtMapper.selectOne(userInfoExtWrapper);
		if (userInfoExt != null) {
			userData.setBgnDate(userInfoExt.getBgnDate());
			userData.setEndDate(userInfoExt.getEndDate());
		}
		return userData;
	}

}
