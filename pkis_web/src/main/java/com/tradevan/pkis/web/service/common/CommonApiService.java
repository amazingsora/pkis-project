package com.tradevan.pkis.web.service.common;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tradevan.mapper.pkis.dao.SsotokenMapper;
import com.tradevan.mapper.pkis.dao.SuppliermasterMapper;
import com.tradevan.mapper.pkis.model.Ssotoken;
import com.tradevan.mapper.pkis.model.Suppliermaster;
import com.tradevan.pkis.web.bean.apiRequest.GetSupplierIdDataRequest;
import com.tradevan.pkis.web.bean.apiRequest.GetSupplierIdRequest;
import com.tradevan.pkis.web.bean.apiRequest.SsoRequest;
import com.tradevan.pkis.web.bean.apiResponse.GetSupplierIdDataResponse;
import com.tradevan.pkis.web.bean.apiResponse.GetSupplierIdResponse;
import com.tradevan.pkis.web.bean.apiResponse.SsoDataResponse;
import com.tradevan.pkis.web.bean.apiResponse.SsoResponse;
import com.tradevan.pkis.web.service.supplierMaster.SupplierMasterService;
import com.tradevan.xauthframework.common.bean.ProcessResult;
import com.tradevan.xauthframework.common.utils.JsonUtils;
import com.tradevan.xauthframework.web.service.DefaultService;

@Service("CommonApiService")
@Transactional(rollbackFor = Exception.class)
public class CommonApiService extends DefaultService {
	
	@Autowired
	SuppliermasterMapper suppliermasterMapper;
	
	@Autowired
	SupplierMasterService supplierMasterService;
	
	@Autowired
	UserDetailsService userDetailsServiceImpl;
	
	@Autowired
	SsotokenMapper ssotokenMapper;

	/**
	 * 取供應商帳號API執行程序
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public GetSupplierIdResponse execute(GetSupplierIdRequest request) throws Exception {
		GetSupplierIdResponse response = new GetSupplierIdResponse();
		List<GetSupplierIdDataResponse> responseDataList = new ArrayList<GetSupplierIdDataResponse>();
		GetSupplierIdDataResponse responseData = null;
		List<GetSupplierIdDataRequest> requestDataList = request.getData();
		String supplierGUI = "";
		String supplierCode = "";
		boolean requestIsBlank = false;
		int i = 0;
		
		try {
			if(requestDataList == null || requestDataList.size() == 0) {
				throw new Exception("未傳任何資料");
			}
			for(GetSupplierIdDataRequest data : requestDataList) {
				responseData = new GetSupplierIdDataResponse();
				supplierGUI = data.getSupplierGUI();
				supplierCode = data.getSupplierCode();
				responseData.setSupplierGUI(supplierGUI);
				responseData.setSupplierCode(supplierCode);
				if(checkRequestIsBlank(supplierGUI, supplierCode)) {
					responseData.setSupplierID("");
					requestIsBlank = true;
				} else {
					ProcessResult getSupplierIdResult = getSupplierId(supplierGUI, supplierCode);
					if(StringUtils.equals(ProcessResult.OK, getSupplierIdResult.getStatus())) {
						responseData.setSupplierID(MapUtils.getString(getSupplierIdResult.getResult(), "supplierid"));
						i ++;
					} else {
						throw new Exception("新增供應商資料發生錯誤");
					}
				}
				responseDataList.add(responseData);
			}
			
			if(requestDataList.size() > 1 && requestIsBlank && i > 0) {
				response.setReturnData(responseDataList);
				response.setReturnCode("001");
				response.setReturnMsg("部分交易成功");
				logger.info("有部分資料未提供supplierGUI或supplierCode資訊，資料筆數共：" + requestDataList.size() + "筆, 成功筆數：" + i + "筆");
			} else if(requestIsBlank) {
				throw new Exception("未提供supplierGUI或supplierCode資訊");
			} else {
				response.setReturnData(responseDataList);
				response.setReturnCode("000");
				response.setReturnMsg("交易成功");
			}
		} catch(Exception e) {
			logger.error(e, e);
			throw new Exception(e.getMessage());
		}
		
		return response;
	}
	
	/**
	 * 檢核參數是否為空
	 * @param supplierGUI
	 * @param supplierCode
	 * @return
	 */
	private boolean checkRequestIsBlank(String supplierGUI, String supplierCode) throws Exception {
		boolean requestIsBlank = false;
		
		if(StringUtils.isBlank(supplierGUI)) {
			requestIsBlank = true;
			return requestIsBlank;
		}
		
		if(StringUtils.isBlank(supplierCode)) {
			requestIsBlank = true;
			return requestIsBlank;
		}
		
		return requestIsBlank;
	}
	
	/**
	 * 根據參數取得供應商帳號SUPPLIERID
	 * @param supplierGUI
	 * @param supplierCode
	 * @return
	 */
	private ProcessResult getSupplierId(String supplierGUI, String supplierCode) throws Exception {
		ProcessResult result = new ProcessResult();
		String deptno = "";
		QueryWrapper<Suppliermaster> suppliermasterWrapper = new QueryWrapper<Suppliermaster>();
		Suppliermaster suppliermaster = null;
		if(supplierCode.length() > 5) {
			deptno = StringUtils.substring(supplierCode, 0, 2); // 100004
			supplierCode = StringUtils.substring(supplierCode, 2);
			suppliermasterWrapper.eq("DEPTNO", deptno); 
			suppliermasterWrapper.eq("SUPPLIERCODE", supplierCode);
		} else {
			suppliermasterWrapper.isNull("DEPTNO");
			suppliermasterWrapper.eq("SUPPLIERCODE", supplierCode);
		}
		suppliermasterWrapper.eq("SUPPLIERGUI", supplierGUI);
		List<Suppliermaster> suppliermasters = suppliermasterMapper.selectList(suppliermasterWrapper);
		if(suppliermasters != null && suppliermasters.size() > 0) {
			suppliermaster = suppliermasters.get(0);
			result.setStatus(ProcessResult.OK);
			result.addResult("supplierid", suppliermaster.getSupplierid());
		} else {
			result = insertSupplierData(supplierGUI, supplierCode, deptno);
			if(StringUtils.equals(ProcessResult.OK, result.getStatus())) {
				result.addResult("supplierid", StringUtils.split(result.getMessages().get(1), "：")[1]);
			}
		}
		
		return result;
	}
	
	/**
	 * 新增供應商資料
	 * @param supplierGUI
	 * @param supplierCode
	 * @param deptno
	 * @return
	 * @throws Exception
	 */
	private ProcessResult insertSupplierData(String supplierGUI, String supplierCode, String deptno) throws Exception {
		ProcessResult result = null;
		Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("suppliercname", supplierGUI + deptno + supplierCode);
		dataMap.put("suppliercode", supplierCode);
		dataMap.put("suppliergui", supplierGUI);
		dataMap.put("deptno", deptno);
		dataMap.put("createuser", "SYSTEM");
		result = supplierMasterService.insertSupplierData(dataMap);
		logger.info("getMessages == " + result.getMessages());
		
		return result;
	}
	
	/**
	 * 多筆supplierid查詢多筆供應商資料
	 * @param spids
	 * @return
	 */
	public List<Map<String, Object>> getSupplierData(List<String> spids) throws Exception {
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		Map<String, Object> data = null;
		QueryWrapper<Suppliermaster> suppliermasterWrapper = null;
		List<Suppliermaster> suppliermasterList = null;
		Suppliermaster suppliermaster = null;
		for(String spid : spids) {
			data = new HashMap<String, Object>();
			suppliermasterWrapper = new QueryWrapper<Suppliermaster>();
			suppliermasterWrapper.eq("SUPPLIERID", spid);
			suppliermasterWrapper.eq("ENABLED", "Y");
			suppliermasterList = suppliermasterMapper.selectList(suppliermasterWrapper);
			if(suppliermasterList != null && suppliermasterList.size() > 0) {
				suppliermaster = suppliermasterList.get(0);
				data.put("supplierId", suppliermaster.getSupplierid());
				data.put("supplierData", suppliermaster.getSuppliercode() + "-" + suppliermaster.getSuppliergui() + "-" + suppliermaster.getSuppliercname());
				result.add(data);
			}
		}
		
		return result;
	}
	
	/**
	 * sso取token執行程序
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public SsoResponse ssoExecute(SsoRequest request) throws Exception {
		SsoResponse response = new SsoResponse();
		SsoDataResponse responseData = new SsoDataResponse();
		List<String> spids = null;
		List<Map<String, Object>> supplierDatas = null;
		Map<String, Object> ssoData = new HashMap<String, Object>();
		String token = "";
		
		try {
			spids = request.getSPID();
			if(spids == null || spids.size() == 0) {
				throw new Exception("request未傳送SPID資料");
			} else {
				supplierDatas = getSupplierData(spids);
				if(supplierDatas == null || supplierDatas.size() == 0) {
					throw new Exception("request有傳送SPID資料，但供應商主檔查無此筆帳號");
				} else {
					token = String.valueOf(UUID.randomUUID());
					ssoData.put("token", token);
					ssoData.put("spidJson", JsonUtils.obj2json(spids));
					ssoData.put("spidCount", spids.size());
					ssoData.put("lgcUserid", request.getUSERID());
					insOrUpdSsotoken(ssoData);
					responseData.setTOKEN(token);
					response.setRETURNDATA(responseData);
					response.setRETURNCODE("000");
					response.setRETURNMSG("交易成功");
				}
			}
		} catch(Exception e) {
			logger.error(e, e);
			throw new Exception(e.getMessage());
		}
		
		return response;
	}
	
	/**
	 * 異動SSOTOKEN資料表
	 * @param ssoData
	 * @throws Exception
	 */
	private void insOrUpdSsotoken(Map<String, Object> ssoData) throws Exception {
		QueryWrapper<Ssotoken> ssotokenWrapper = new QueryWrapper<Ssotoken>();
		Date currentDate = new Timestamp(new Date().getTime());
		String token = MapUtils.getString(ssoData, "token");
		ssotokenWrapper.eq("TOKEN", token);
		List<Ssotoken> dataList = ssotokenMapper.selectList(ssotokenWrapper);
		Ssotoken data = null;
		if(dataList != null && dataList.size() > 0) {
			data = dataList.get(0);
			data.setUpdateuser("SYSTEM");
			data.setUpdatedate(currentDate);
			ssotokenMapper.update(data, ssotokenWrapper);
		} else {
			data = new Ssotoken();
			data.setSpidjson(MapUtils.getString(ssoData, "spidJson"));
			data.setToken(token);
			data.setSpidcount(MapUtils.getLong(ssoData, "spidCount"));
			data.setLgcuserid(MapUtils.getString(ssoData, "lgcUserid"));
			data.setCreateuser("SYSTEM");
			data.setCreatedate(currentDate);
			ssotokenMapper.insert(data);
		}
	}
	
	/**
	 * 用token至SSOTOKEN資料表取單筆資料
	 * @param token
	 * @return
	 */
	public Ssotoken getSsotokenData(String token) {
		Ssotoken result = null;
		QueryWrapper<Ssotoken> ssotokenWrapper = new QueryWrapper<Ssotoken>();
		ssotokenWrapper.eq("TOKEN", token);
		List<Ssotoken> dataList = ssotokenMapper.selectList(ssotokenWrapper);
		if(dataList != null && dataList.size() > 0) {
			result = dataList.get(0);
		}
		
		return result;
	}
	
	/**
	 * 執行自動登入
	 * @param request
	 * @param userId
	 * @throws Exception
	 */
	public void autoLogin(HttpServletRequest request, String userId, String token) throws Exception {
		Map<String, Object> tokenMap = new HashMap<String, Object>();
		tokenMap.put("token", token);
		UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(userId);
		Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(authentication);
		request.getSession().setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, SecurityContextHolder.getContext());
		insOrUpdSsotoken(tokenMap);
	}
	
	/**
	 * request轉物件
	 * @param request
	 * @return
	 */
	public SsoRequest getSsoRequest(HttpServletRequest request) throws Exception {
		SsoRequest result = null;
		String data = request.getParameter("data");
		String json = new String(Base64.getDecoder().decode(data));
		logger.info("json == " + json);
		result = JsonUtils.json2Object(json, SsoRequest.class);
		
		return result;
	}
}
