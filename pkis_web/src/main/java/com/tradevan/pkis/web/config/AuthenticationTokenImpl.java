package com.tradevan.pkis.web.config;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import com.tradevan.xauthframework.common.utils.JsonUtils;
import com.tradevan.xauthframework.common.utils.httpclient.HttpClient;
import com.tradevan.xauthframework.core.bean.SsoResult;
import com.tradevan.xauthframework.core.security.IXauthAuthenticationToken;

/**
 * 作 業 代 碼 ：AuthenticationTokenImpl<br>
 * 作 業 名 稱 ：SSO TOKEN驗證<br>
 * 程 式 代 號 ：AuthenticationTokenImpl<br>
 * 描             述 ：若需SSO介接，需實作此類別，用來驗證token並匹配本系統的登入資訊<br>
 * 公             司 ：Tradevan Co.<br><br>
 *【 資 料 來 源】  ：<br>
 *【 輸 出 報 表】  ：<br>
 *【 異 動 紀 錄】  ：<br>
 * @author   : penny<br>
 * @version  : 1.0.0 2020/3/16<P>
 */
@Component
public class AuthenticationTokenImpl implements IXauthAuthenticationToken {
	
	Logger logger = LogManager.getLogger(AuthenticationTokenImpl.class);

	/**
	 * 驗證token
	 * @param token token值
	 * @param params 其他request參數
	 */
	@SuppressWarnings("unchecked")
	@Override
	public SsoResult checkToken(String token, Map<String, Object> params) throws IOException {
		SsoResult ssoResult = new SsoResult();
		ssoResult.setStatus(SsoResult.OK);
		try {
			// 驗證token
			Map<String, Object> mapParams = new HashMap<String, Object>();
			mapParams.put("token", token);
			if (MapUtils.isNotEmpty(params)) {
				mapParams.putAll(params);
			}
			Map<String, Object> returnData = api("http://localhost:8082/REST/api/check", mapParams);
			String returnCode = MapUtils.getString(returnData, "returnCode");
			String returnMsg = MapUtils.getString(returnData, "returnMsg");
			if (returnCode.equals("S0000")) {
				Map<String, Object> data = MapUtils.getMap(returnData, "data");		
				// 取得新token
				String newToken = MapUtils.getString(data, "token");
				// 自訂判斷邏輯，用來匹配本系統的登入資訊
				ssoResult.setIdenId("00000000");
				ssoResult.setUserId("admin");
				ssoResult.setToken(newToken);
				
//				params = new HashMap<String, Object>();
//				params.put("token", newToken);
//				returnData = api("http://localhost:8082/REST/api/getUser", params);
//				returnCode = MapUtils.getString(returnData, "returnCode");
//				if (returnCode.equals("S0000")) {
//					Map<String, Object> userData = MapUtils.getMap(returnData, "data");					
//					ssoResult.setIdenId("00000000");
//					ssoResult.setUserId("admin");
//					ssoResult.setToken(newToken);
//					ssoResult.setResult(userData);
//				}
//				else {
//					ssoResult.setStatus(SsoResult.NG);
//				}
				
			}
			else {
				ssoResult.setStatus(SsoResult.NG);
				ssoResult.addMessage(returnMsg);
			}			
		}
		catch (Exception e) {
			e.printStackTrace();
			logger.error(e, e);
			ssoResult.setStatus(SsoResult.EX);
			ssoResult.addMessage("系統發生錯誤");
		}
		
		return ssoResult;
	}

	public Map<String, Object> api(String url, Map<String, Object> params) throws Exception {
		Map<String, Object> headers = new HashMap<String, Object>();
		headers.put("Content-Type", "application/json");
		HttpClient client = new HttpClient(url, 300000, 300000);			
		String result = client.post(headers, JsonUtils.obj2json(params));									
		logger.info("result => " + result);
		Map<String, Object> returnData = JsonUtils.json2Object(result, Map.class);
		return returnData;
	}
}
