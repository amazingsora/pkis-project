package com.tradevan.pkis.web.service.xauth; 
 
import java.security.SecureRandom; 
import java.sql.Timestamp; 
import java.util.Base64; 
import java.util.Calendar; 
import java.util.Date; 
import java.util.List; 
import java.util.Map; 
 
import javax.servlet.ServletContext; 
 
import org.apache.commons.lang.StringUtils; 
import org.springframework.beans.factory.annotation.Autowired; 
import org.springframework.core.env.Environment; 
import org.springframework.stereotype.Service; 
import org.springframework.transaction.annotation.Transactional; 
 
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tradevan.mapper.pkis.dao.EmailqueueMapper;
import com.tradevan.mapper.pkis.model.Emailqueue;
import com.tradevan.mapper.xauth.dao.XauthUsersMapper; 
import com.tradevan.mapper.xauth.model.XauthUsers; 
import com.tradevan.xauthframework.common.bean.ProcessResult; 
import com.tradevan.xauthframework.common.utils.MapUtils; 
import com.tradevan.xauthframework.core.enums.MSG_KEY; 
import com.tradevan.xauthframework.core.utils.XauthPropUtils; 
import com.tradevan.xauthframework.web.service.DefaultService; 
 
@Service("SettingPwdService") 
@Transactional(rollbackFor=Exception.class) 
public class SettingPwdService extends DefaultService { 
	 
	@Autowired 
	EmailqueueMapper emailqueueMapper; 
	 
	@Autowired 
	XauthUsersMapper xauthUsersMapper; 
	 
	@Autowired 
	private Environment env; 
	 
	@Autowired 
    private ServletContext servletContext; 
	 
	/** 
	 * 忘記密碼 
	 * @param params 
	 * @return 
	 * @throws Exception 
	 */ 
	public ProcessResult updatePwd(Map<String, Object> params) throws Exception { 
		logger.info("params == " + params); 
		ProcessResult result = new ProcessResult(); 
		Emailqueue emailqueue = null; 
		XauthUsers xauthUsers = null; 
		String userId = MapUtils.getString(params, "userId"); 
		String email = MapUtils.getString(params, "email"); 
		String token = generateNewToken(); 
		int tokenExpireHour = Integer.valueOf(XauthPropUtils.getKey("user.forgetpw.email.expire")); 
		Calendar cal = Calendar.getInstance(); 
		Date now = cal.getTime(); 
		cal.setTime(now); 
		cal.add(Calendar.HOUR, tokenExpireHour); 
		 
		if (params != null && params.size() > 0) { 
			ProcessResult rs = checkForgetPwdData(params); 
			if (StringUtils.equals(rs.getStatus(), ProcessResult.NG)) { 
				result.setStatus(rs.getStatus()); 
				result.setMessages(rs.getMessages()); 
				return result; 
			} 
			QueryWrapper<XauthUsers> usersWrapper = new QueryWrapper<XauthUsers>(); 
			usersWrapper.eq("APP_ID", APP_ID); 
			usersWrapper.eq("USER_ID", userId); 
			usersWrapper.eq("EMAIL", email); 
			List<XauthUsers> xauthUserList = xauthUsersMapper.selectList(usersWrapper); 
			if(xauthUserList.size() > 0) { 
				xauthUsers = xauthUserList.get(0); 
				emailqueue = new Emailqueue(); 
				emailqueue.setCategory("RESET"); 
				emailqueue.setContent(getForgetPewContent(token, xauthUsers.getUserCname())); 
				emailqueue.setCreatetime(new Timestamp(new Date().getTime())); 
				emailqueue.setCreateuserid("SYSTEM"); 
				emailqueue.setMailto(email); 
				emailqueue.setPriority("1"); 
				emailqueue.setStatus("N"); 
				emailqueue.setSubject("[Notification] E-Contract -請重設密碼"); 
				emailqueue.setSysid(APP_ID); 
				emailqueue.setSysmemo(userId); 
				emailqueue.setUpdatetime(new Timestamp(new Date().getTime())); 
				emailqueue.setUpdateuserid("SYSTEM"); 
				emailqueueMapper.insert(emailqueue); 
				 
				xauthUsers.setEmailToken(token); 
				xauthUsers.setEmailExpire(cal.getTime()); 
				logger.info("getEmailExpire == " + xauthUsers.getEmailExpire()); 
				xauthUsers.setUpdDate(new Timestamp(new Date().getTime())); 
				xauthUsers.setUpdUser("SYSTEM"); 
				xauthUsersMapper.update(xauthUsers, usersWrapper); 
				result.setStatus(ProcessResult.OK); 
				result.addMessage("已成功發送，請至您登記的郵箱確認信件，並依指示步驟完成密碼重設。"); 
			} else { 
				result.setStatus(ProcessResult.NG); 
				result.addMessage("請聯絡系統管理員"); 
			} 
		} 
		 
		return result; 
	} 
	 
	/** 
	 * 忘記密碼欄位檢核 
	 * @param params 
	 * @return 
	 */ 
	private ProcessResult checkForgetPwdData(Map<String, Object> params) { 
		ProcessResult result = new ProcessResult(); 
		result.setStatus(ProcessResult.NG); 
		try { 
			if (StringUtils.isBlank(MapUtils.getString(params, "userId"))) { 
				result.addMessage("請輸入使用者帳號"); 
				return result; 
			} 
			 
			if (StringUtils.isBlank(MapUtils.getString(params, "email"))) { 
				result.addMessage("請輸入電子郵件"); 
				return result; 
			}		 
		} 
		catch (Exception e) { 
			e.printStackTrace(); 
			result.addMessage(MSG_KEY.PARAMS_EMPTY.getMessage()); 
			return result; 
		} 
		result.setStatus(ProcessResult.OK); 
		return result; 
	} 
	 
	public String generateNewToken() { 
		SecureRandom secureRandom = new SecureRandom();  
		Base64.Encoder base64Encoder = Base64.getUrlEncoder(); 
	    byte[] randomBytes = new byte[64]; 
	    secureRandom.nextBytes(randomBytes); 
	     
	    return base64Encoder.encodeToString(randomBytes); 
	} 
	 
	public String getForgetPewContent(String token, String userCname) { 
		String apiUrl = env.getProperty("spring.domain"); 
		String contectPath = servletContext.getContextPath(); 
		String url = apiUrl + contectPath + "/resetPassword/" + token; 
		StringBuffer content = new StringBuffer(); 
		content.append(userCname + " 您好：忘記密碼申請<BR>"); 
		content.append("<a href='" + url + "' target='_blank'>請按此重新設定密碼</a>"); 
		 
		return content.toString(); 
	} 
	 
	public ProcessResult checkResetToken(String token) { 
		ProcessResult result = new ProcessResult(); 
		result.setStatus(ProcessResult.NG); 
		XauthUsers xauthUsers = null; 
		Calendar cal = Calendar.getInstance(); 
		QueryWrapper<XauthUsers> usersWrapper = new QueryWrapper<XauthUsers>(); 
		usersWrapper.eq("APP_ID", APP_ID); 
		usersWrapper.eq("EMAIL_TOKEN", token); 
		List<XauthUsers> xauthUsersList = xauthUsersMapper.selectList(usersWrapper); 
		if(xauthUsersList != null && xauthUsersList.size() > 0) { 
			xauthUsers = xauthUsersList.get(0); 
			if(cal.getTime().before(xauthUsers.getEmailExpire())) { 
				result.setStatus(ProcessResult.OK); 
			} else { 
				result.addMessage("驗證超過效期，請重新進行忘記密碼申請"); 
			} 
		} else { 
			result.addMessage("驗證失敗，請重新進行忘記密碼申請"); 
		} 
		 
		return result; 
	} 
} 