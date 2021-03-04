package com.tradevan.pkis.web.service.supplierMaster;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.crypto.SealedObject;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tradevan.mapper.contract.dao.ContractMapper;
import com.tradevan.mapper.pkis.dao.CodelistMapper;
import com.tradevan.mapper.pkis.dao.SuppliermasterMapper;
import com.tradevan.mapper.pkis.dao.UserInfoExtMapper;
import com.tradevan.mapper.pkis.model.Codelist;
import com.tradevan.mapper.pkis.model.Suppliermaster;
import com.tradevan.mapper.pkis.model.UserInfoExt;
import com.tradevan.mapper.xauth.dao.XauthMapper;
import com.tradevan.mapper.xauth.dao.XauthRoleUserMapper;
import com.tradevan.mapper.xauth.dao.XauthUsersMapper;
import com.tradevan.mapper.xauth.dao.XauthUsersPwdHistoryMapper;
import com.tradevan.mapper.xauth.model.XauthRoleUser;
import com.tradevan.mapper.xauth.model.XauthUsers;
import com.tradevan.mapper.xauth.model.XauthUsersPwdHistory;
import com.tradevan.xauthframework.common.bean.ProcessResult;
import com.tradevan.xauthframework.common.utils.ConvertUtils;
import com.tradevan.xauthframework.common.utils.MapUtils;
import com.tradevan.xauthframework.common.utils.SensitiveUtils;
import com.tradevan.xauthframework.core.enums.MSG_KEY;
import com.tradevan.xauthframework.core.enums.USER_TYPE;
import com.tradevan.xauthframework.core.security.UserInfo;
import com.tradevan.xauthframework.core.service.CheckPwdService;
import com.tradevan.xauthframework.dao.bean.GridResult;
import com.tradevan.xauthframework.web.service.DefaultService;

@Service("SupplierMasterService")
@Transactional(rollbackFor = Exception.class)
public class SupplierMasterService extends DefaultService {
	
	@Autowired
	CheckPwdService checkPwdService;
	
	@Autowired
	XauthMapper xauthMapper;
	
	@Autowired
	CodelistMapper codelistMapper;
	
	@Autowired
	XauthUsersMapper xauthUsersMapper;
	
	@Autowired
	UserInfoExtMapper userInfoExtMapper;
	
	@Autowired
	XauthRoleUserMapper xauthRoleUserMapper;
	
	@Autowired
	SuppliermasterMapper suppliermasterMapper;
	
	@Autowired
	XauthUsersPwdHistoryMapper xauthUsersPwdHistoryMapper;
	
	@Autowired
	ContractMapper contractMapper;
	
	/**
	 * 供應商查詢
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public GridResult selectSupplierList(Map<String, Object> params) throws Exception {
		GridResult gridResult = this.grid(params);	
		
		Map<String, Object> objParams = new HashMap<String, Object>();
		objParams.put("suppliergui", MapUtils.getString(params, "suppliergui"));
		objParams.put("suppliercname", MapUtils.getString(params, "suppliercname"));
		objParams.put("suppliercode", MapUtils.getString(params, "suppliercode"));
		commonDao.query(gridResult, "com.tradevan.mapper.pkis.dao.SuppliermasterMapper.selectSupplierList", objParams);		
		return gridResult;
	}
	
	/**
	 * 供應商新增 ＆＆ 使用者新增
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public ProcessResult insertSupplierData(Map<String, Object> params) throws Exception {
		Date now = new Timestamp(new Date().getTime());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
		Suppliermaster suppliermaster = new Suppliermaster();
		ProcessResult result = new ProcessResult();
		
		//Check Input Value
		ProcessResult inputProcessResult = checkInput(params);
		if(StringUtils.equals(ProcessResult.NG, inputProcessResult.getStatus())) {
			return inputProcessResult;
		}
		
		String pCode = MapUtils.getString(params, "password");
		String supplierename = MapUtils.getString(params, "supplierename");
		String supplieretel = MapUtils.getString(params, "supplieretel");
		String picuser = MapUtils.getString(params, "picuser");
		String supplieremail = MapUtils.getString(params, "supplieremail");
		String suppliercname = MapUtils.getString(params, "suppliercname");
		String suppliercode = MapUtils.getString(params, "suppliercode");
		String suppliereaddr = MapUtils.getString(params, "suppliereaddr");
		String contacruser = MapUtils.getString(params, "contacruser");
		String suppliergui = MapUtils.getString(params, "suppliergui");
		String suppliercaddr = MapUtils.getString(params, "suppliercaddr");
		String createuser = MapUtils.getString(params, "createuser");
		String suppliertype = "";//MapUtils.getString(params, "suppliertype");
		String deptno = MapUtils.getString(params, "deptno");
		String years = sdf.format(now);
		String idenId = "999999999";
		
		//檢查供應商資料是否重複
		QueryWrapper<Suppliermaster> queryWrapper = new QueryWrapper<Suppliermaster>();
		queryWrapper.eq("SUPPLIERGUI", suppliergui);
		queryWrapper.eq("SUPPLIERCODE", suppliercode);
		if(StringUtils.isNotBlank(deptno)) {
			queryWrapper.eq("DEPTNO", deptno);
		} else {
			queryWrapper.isNull("DEPTNO");
		}
		List<Suppliermaster> supplierList = suppliermasterMapper.selectList(queryWrapper);
		
		if(supplierList.size() > 0) {
			result.setStatus(ProcessResult.NG);
			result.addMessage("資料已存在");
			return result;
		}
		//供應商流水號
		String code = getNumCode(years, deptno, suppliercode);
		
		//供應商代碼(格式 SP + yyyy + 流水號)  -> 2020/7/22編碼規則改為 SP + 部門 + 供應商廠編 + 流水碼(2碼)-如無部門則捨去部門
		if(StringUtils.isBlank(deptno)) {
			suppliermaster.setSupplierid("SP" + suppliercode + code);
		} else {
			suppliermaster.setSupplierid("SP" + deptno + suppliercode + code);
		}
		suppliermaster.setIdenid(idenId);
		suppliermaster.setSuppliercode(suppliercode);
		suppliermaster.setSuppliergui(suppliergui);
		suppliermaster.setSuppliertype(suppliertype);
		suppliermaster.setSuppliercname(suppliercname);
		suppliermaster.setSupplierename(supplierename);
		suppliermaster.setSuppliercaddr(suppliercaddr);
		suppliermaster.setSuppliereaddr(suppliereaddr);
		suppliermaster.setPicuser(picuser);
		suppliermaster.setSupplieretel(supplieretel);
		suppliermaster.setContacruser(contacruser);
		suppliermaster.setSupplieremail(supplieremail);
		suppliermaster.setCreateuser(StringUtils.isBlank(createuser) ? getCreOrUpdUser(null) : createuser);
		suppliermaster.setCreatedate(now);
		suppliermaster.setEnabled("Y");
		suppliermaster.setDeptno(deptno);
		if(StringUtils.isBlank(pCode)){
			pCode = suppliermaster.getSupplierid();
		}
		idenId = suppliermaster.getIdenid();
		String userId = suppliermaster.getSupplierid();
		XauthUsers xauthUsers = new XauthUsers();
		xauthUsers.setAppId(APP_ID);
		xauthUsers.setIdenId(idenId);
		xauthUsers.setUserId(userId);
		
		//判斷使用者是否存在
		if(getUser(xauthUsers) == null) {
			result = insertUser(suppliermaster, pCode);
			if(StringUtils.equals(ProcessResult.OK, result.getStatus())) {
				//新增供應商主檔
				suppliermasterMapper.insert(suppliermaster);
				// 新增或更新codelist
				insertOrUpdCodelist(code, years, deptno, suppliercode);
			}
		} else {
			result.setStatus(ProcessResult.NG);
			result.addMessage("資料已存在");
		}
		
		if(StringUtils.equals(ProcessResult.OK, result.getStatus())) {
			result.addMessage("，供應商代碼為：" + suppliermaster.getSupplierid());
		}
		
		return result;
	}
	
	/**
	 * 供應商更新 ＆＆ 使用者更新
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public ProcessResult updateSupplierData(Map<String, Object> params) throws Exception {
		Date now = new Timestamp(new Date().getTime());
		Suppliermaster suppliermaster = new Suppliermaster();
		ProcessResult result = new ProcessResult();
		String pCode = MapUtils.getString(params, "password");
		String supplierid = MapUtils.getString(params, "supplierid");
		String supplierename = MapUtils.getEmptyString(params, "supplierename");
		String supplieretel = MapUtils.getEmptyString(params, "supplieretel");
		String picuser = MapUtils.getEmptyString(params, "picuser");
		String supplieremail = MapUtils.getEmptyString(params, "supplieremail");
		String suppliercname = MapUtils.getEmptyString(params, "suppliercname");
		String suppliercode = MapUtils.getString(params, "suppliercode");
		String suppliereaddr = MapUtils.getEmptyString(params, "suppliereaddr");
		String contacruser = MapUtils.getEmptyString(params, "contacruser");
		String suppliergui = MapUtils.getString(params, "suppliergui");
		String suppliercaddr = MapUtils.getEmptyString(params, "suppliercaddr");
		String suppliertype = MapUtils.getString(params, "suppliertype");
		String enabled = MapUtils.getString(params, "enabled");
		
		//Check Input Value
		ProcessResult inputProcessResult = checkInput(params);
		if(StringUtils.equals(ProcessResult.NG, inputProcessResult.getStatus())) {
			return inputProcessResult;
		} else if(StringUtils.isBlank(supplierid)) {
			inputProcessResult.setStatus(ProcessResult.NG);
			inputProcessResult.addMessage("供應商代碼，不得為空");
			return inputProcessResult;
		}
		
		suppliermaster.setSupplierid(supplierid);
		suppliermaster.setIdenid("999999999");
		suppliermaster.setSuppliercode(suppliercode);
		suppliermaster.setSuppliergui(suppliergui);
		suppliermaster.setSuppliertype(suppliertype);
		suppliermaster.setSuppliercname(suppliercname);
		suppliermaster.setSupplierename(supplierename);
		suppliermaster.setSuppliercaddr(suppliercaddr);
		suppliermaster.setSuppliereaddr(suppliereaddr);
		suppliermaster.setPicuser(picuser);
		suppliermaster.setSupplieretel(supplieretel);
		suppliermaster.setContacruser(contacruser);
		suppliermaster.setSupplieremail(supplieremail);
		suppliermaster.setUpdateuser(getCreOrUpdUser(null));
		suppliermaster.setUpdatedate(now);
		suppliermaster.setEnabled(enabled);
		
		String idenId = suppliermaster.getIdenid();
		String userId = suppliermaster.getSupplierid();
		XauthUsers xauthUsers = new XauthUsers();
		xauthUsers.setAppId(APP_ID);
		xauthUsers.setIdenId(idenId);
		xauthUsers.setUserId(userId);
		
		//判斷使用者是否存在
		if(getUser(xauthUsers) == null) {
			result.setStatus(ProcessResult.NG);
			result.addMessage("查無使用者");
		} else {
			result = updateUser(suppliermaster, pCode);
			if(StringUtils.equals(ProcessResult.OK, result.getStatus())) {
				//SupplierMaster Where 
				QueryWrapper<Suppliermaster> queryWrapper = new QueryWrapper<Suppliermaster>();
				queryWrapper.eq("IDENID", suppliermaster.getIdenid());
				queryWrapper.eq("SUPPLIERID", suppliermaster.getSupplierid());
				queryWrapper.eq("SUPPLIERGUI", suppliermaster.getSuppliergui());
				queryWrapper.eq("SUPPLIERCODE", suppliermaster.getSuppliercode());
				suppliermasterMapper.update(suppliermaster, queryWrapper);
			}
		}
		
		return result;
	}
	
	/**
	 * 供應商啟用/停用 ＆＆ 使用者啟用/停用
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public ProcessResult bgnOrStopSupplierData(Map<String, Object> params) throws Exception {
		Date now = new Timestamp(new Date().getTime());
		Suppliermaster suppliermaster = new Suppliermaster();
		ProcessResult result = new ProcessResult();
		
		String supplierid = MapUtils.getString(params, "supplierid");
		String supplierename = MapUtils.getString(params, "supplierename");
		String supplieretel = MapUtils.getString(params, "supplieretel");
		String picuser = MapUtils.getString(params, "picuser");
		String supplieremail = MapUtils.getString(params, "supplieremail");
		String suppliercname = MapUtils.getString(params, "suppliercname");
		String suppliercode = MapUtils.getString(params, "suppliercode");
		String suppliereaddr = MapUtils.getString(params, "suppliereaddr");
		String contacruser = MapUtils.getString(params, "contacruser");
		String suppliergui = MapUtils.getString(params, "suppliergui");
		String suppliercaddr = MapUtils.getString(params, "suppliercaddr");
		String suppliertype = MapUtils.getString(params, "suppliertype");
		String enabled = MapUtils.getString(params, "enabled");
		
		suppliermaster.setSupplierid(supplierid);
		suppliermaster.setIdenid("999999999");
		suppliermaster.setSuppliercode(suppliercode);
		suppliermaster.setSuppliergui(suppliergui);
		suppliermaster.setSuppliertype(suppliertype);
		suppliermaster.setSuppliercname(suppliercname);
		suppliermaster.setSupplierename(supplierename);
		suppliermaster.setSuppliercaddr(suppliercaddr);
		suppliermaster.setSuppliereaddr(suppliereaddr);
		suppliermaster.setPicuser(picuser);
		suppliermaster.setSupplieretel(supplieretel);
		suppliermaster.setContacruser(contacruser);
		suppliermaster.setSupplieremail(supplieremail);
		suppliermaster.setUpdateuser(getCreOrUpdUser(null));
		suppliermaster.setUpdatedate(now);
		
		if(StringUtils.equals("N", enabled)) {
			suppliermaster.setEnabled("Y");
		}else {
			suppliermaster.setEnabled("N");
		}
		
		String idenId = suppliermaster.getIdenid();
		String userId = suppliermaster.getSupplierid();
		XauthUsers xauthUsers = new XauthUsers();
		xauthUsers.setAppId(APP_ID);
		xauthUsers.setIdenId(idenId);
		xauthUsers.setUserId(userId);
		result = bgnOrStopUser(xauthUsers, enabled);
		if(StringUtils.equals(ProcessResult.OK, result.getStatus())) {
			QueryWrapper<Suppliermaster> queryWrapper = new QueryWrapper<Suppliermaster>();
			queryWrapper.eq("IDENID", suppliermaster.getIdenid());
			queryWrapper.eq("SUPPLIERID", suppliermaster.getSupplierid());
			queryWrapper.eq("SUPPLIERGUI", suppliermaster.getSuppliergui());
			queryWrapper.eq("SUPPLIERCODE", suppliermaster.getSuppliercode());
			
			suppliermasterMapper.update(suppliermaster, queryWrapper);
		}
		
		return result;
	}
	
	/**
	 * 供應商流水號
	 * @param years 系統年(yyyy)
	 * @param idenId
	 * @param suppliercode
	 * @return 01 (兩碼)
	 * @throws Exception
	 */
	public String getNumCode(String years, String deptno, String suppliercode) throws Exception{
		String resultString = "";
		GridResult gridResult = new GridResult();
		Map<String, Object> objParams = new HashMap<String, Object>();
		if(StringUtils.isNotBlank(deptno)) {
			objParams.put("deptno", deptno);
		}
		objParams.put("suppliercode", suppliercode);
		commonDao.query(gridResult, "com.tradevan.mapper.pkis.dao.CodelistMapper.searchSupplierCode", objParams);
		
		if(gridResult.getRows().size() == 0) {
			resultString = "01";
		} else {
			Integer anumber = ((BigDecimal)gridResult.getRows().get(0).get("anumber")).intValue();
			resultString = StringUtils.leftPad("" + anumber, 2, "0");
		}
		
		return resultString;
	}
	
	/**
	 * 新增或更新供應商代碼codelist
	 * @param numCode
	 * @param years
	 * @param deptno
	 * @param suppliercode
	 * @throws Exception
	 */
	public void insertOrUpdCodelist(String code, String years, String deptno, String suppliercode) throws Exception {
		GridResult gridResult = new GridResult();
		Codelist codelist = new Codelist();
		Map<String, Object> objParams = new HashMap<String, Object>();
		int anumber = Integer.parseInt(code);
		
		if(anumber > 1) {
			objParams.put("anumber", anumber + 1);
			objParams.put("years", years);
			commonDao.query(gridResult, "com.tradevan.mapper.pkis.dao.CodelistMapper.updataSupplierCodeList", objParams);
		} else {
			codelist.setSys("SEQUENCE");
			codelist.setClasstype("SP");
			codelist.setYears(years);
			codelist.setAnumber(2);
			codelist.setNote("序號");
			codelist.setDeptno(deptno == null ? "" : deptno);
			codelist.setSuppliercode(suppliercode);
			commonDao.query(gridResult, "com.tradevan.mapper.pkis.dao.CodelistMapper.insertCodeList", codelist);
		}
	}

	/**
	 * 新增使用者資料
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public ProcessResult insertUser(Suppliermaster suppliermaster, String pCode) throws Exception {
		Date now = new Timestamp(new Date().getTime());
		UserInfo userInfo = userContext.getCurrentUser();
		ProcessResult result = new ProcessResult();
		
		if (suppliermaster != null) {
			
			if (userInfo != null && userInfo.getUserType().equals(USER_TYPE.USER.getCode())) {
				result.setStatus(ProcessResult.NG);
				result.addMessage("您無權限新增使用者資料");
				return result;
			}
			
			String idenId = suppliermaster.getIdenid();
			String userId = suppliermaster.getSupplierid();
			String cryptIdenId = "";
			String crypyUserId = "";
			
			XauthUsers xauthUsers = new XauthUsers();
			xauthUsers.setAppId(APP_ID);
			xauthUsers.setIdenId(idenId);
			xauthUsers.setUserId(userId);
			
			if (StringUtils.isNotBlank(pCode)) {
				if(userInfo == null) {
					cryptIdenId = "00000000";
					crypyUserId = "admin";
				} else {
					cryptIdenId = userInfo.getIdenId();
					crypyUserId = userInfo.getUserId();
				}
				SensitiveUtils sens = new SensitiveUtils();
				SealedObject sealObject = sens.encrypt(pCode, cryptIdenId + crypyUserId);
				ProcessResult r = checkPwdService.checkPwd(idenId, userId, sens.decrypt(sealObject, cryptIdenId + crypyUserId).toString());
				
				if (r.getStatus().equals(ProcessResult.NG)) {
					result.setStatus(ProcessResult.NG);
					result.addMessage(r.getMessages().get(0));
					return result;
				}
				
				xauthUsers.setPwdhash(pCode);
				xauthUsers.setUserPw(new BCryptPasswordEncoder().encode(sens.decrypt(sealObject, cryptIdenId + crypyUserId).toString()));
			} else {
				result.setStatus(ProcessResult.NG);
				result.addMessage("請輸入密碼");
				return result;
			}
			
			xauthUsers.setEnabled("1");
			xauthUsers.setEmail(suppliermaster.getSupplieremail());
			xauthUsers.setUserCname(suppliermaster.getSuppliercname());
			xauthUsers.setUserType("02");
			xauthUsers.setIsFirst("1");
			xauthUsers.setCreUser(suppliermaster.getCreateuser());
			xauthUsers.setCreDate(now);			
			xauthUsersMapper.insert(xauthUsers);
			
			XauthRoleUser xauthRoleUser = new XauthRoleUser();;
			xauthRoleUser.setAppId(APP_ID);
			xauthRoleUser.setIdenId(idenId);
			xauthRoleUser.setRoleId("ROLE_SP");
			xauthRoleUser.setUserId(userId);
			xauthRoleUser.setCreUser(suppliermaster.getCreateuser());
			xauthRoleUser.setCreDate(now);				
			xauthRoleUserMapper.insert(xauthRoleUser);
			
			if(userInfo != null) {
				insUserPdHis(idenId, userId, xauthUsers.getUserPw());
			}
			
			QueryWrapper<UserInfoExt> queryWrapper = new QueryWrapper<UserInfoExt>();
			queryWrapper.eq("APP_ID", APP_ID);
			queryWrapper.eq("IDEN_ID", idenId);
			queryWrapper.eq("USER_ID", userId);			
			UserInfoExt userInfoExt = userInfoExtMapper.selectOne(queryWrapper);
			
			if (userInfoExt != null) {
				userInfoExt.setUpdUser(suppliermaster.getCreateuser());
				userInfoExt.setUpdDate(now);				
				userInfoExtMapper.update(userInfoExt, queryWrapper);
			} else {
				userInfoExt = new UserInfoExt();
				userInfoExt.setAppId(APP_ID);
				userInfoExt.setIdenId(idenId);
				userInfoExt.setUserId(userId);
				userInfoExt.setBgnDate(now);			
				userInfoExt.setCreUser(suppliermaster.getCreateuser());
				userInfoExt.setCreDate(now);				
				userInfoExtMapper.insert(userInfoExt);
			}
						
			result.setStatus(ProcessResult.OK);
			result.addMessage(MSG_KEY.INSERT_OK.getMessage());
		} else {
			result.setStatus(ProcessResult.NG);
			result.addMessage(MSG_KEY.PARAMS_EMPTY.getMessage());
		}
		
		return result;
	}
	
	/**
	 * 更新使用者資料
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public ProcessResult updateUser(Suppliermaster suppliermaster, String pCode) throws Exception {
		Date now = new Timestamp(new Date().getTime());
		UserInfo userInfo = userContext.getCurrentUser();
		ProcessResult result = new ProcessResult();
		
		if (suppliermaster != null) {
			
			String idenId = suppliermaster.getIdenid();
			String userId = suppliermaster.getSupplierid();
			
			XauthUsers xauthUsers = new XauthUsers();
			xauthUsers.setAppId(APP_ID);
			xauthUsers.setIdenId(idenId);
			xauthUsers.setUserId(userId);
			Map<String, Object> data = getUser(xauthUsers);
			
			if (data == null) {
				result.setStatus(ProcessResult.NG);
				result.addMessage(MSG_KEY.QUERY_EMPTY.getMessage());
				return result;
			}
			
			if (userInfo.getUserType().equals(USER_TYPE.USER.getCode())) {
				if (!userInfo.getUserId().equals(userId)) {
					result.setStatus(ProcessResult.NG);
					result.addMessage("您無權限修改使用者資料");
					return result;
				}
			} else if (userInfo.getUserType().equals(USER_TYPE.ADMIN.getCode())) {
				// 如果登入者與要異動的帳號為同公司，且要異動的帳號是管理者身份，且欲將帳號改為使用者
				if (userInfo.getIdenId().equals(idenId) 
						&& MapUtils.getString(data, "userType").equals(USER_TYPE.ADMIN.getCode()) 
						&& MapUtils.getString(data, "userType").equals(USER_TYPE.USER.getCode())) {
					result.setStatus(ProcessResult.NG);
					result.addMessage("您無權限修改此管理員帳號為使用者");
					return result;
				}
			}
			
			if (StringUtils.isNotBlank(pCode)) {
				SensitiveUtils sens = new SensitiveUtils();
				SealedObject sealObject = sens.encrypt(pCode, userInfo.getIdenId() + userInfo.getUserId());
				ProcessResult r = checkPwdService.checkPwd(idenId, userId, sens.decrypt(sealObject, userInfo.getIdenId() + userInfo.getUserId()).toString());
				if (r.getStatus().equals(ProcessResult.NG)) {
					result.setStatus(ProcessResult.NG);
					result.addMessage(r.getMessages().get(0));
					return result;
				}
				xauthUsers.setUserPw(new BCryptPasswordEncoder().encode(sens.decrypt(sealObject, userInfo.getIdenId() + userInfo.getUserId()).toString()));
			}
			
			xauthUsers.setEnabled("1");
			xauthUsers.setEmail(suppliermaster.getSupplieremail());
			xauthUsers.setUserCname(suppliermaster.getSuppliercname());
			xauthUsers.setUserType("02");
			String setIsFirst = "0";
			if(userInfo.getUserId().equals("admin")) {
				setIsFirst="1";
			}
			xauthUsers.setIsFirst(setIsFirst);
			xauthUsers.setUpdUser(getCreOrUpdUser(null));			
			xauthUsers.setUpdDate(now);
			
			QueryWrapper<XauthUsers> usersWrapper = new QueryWrapper<XauthUsers>();
			usersWrapper.eq("APP_ID", APP_ID);
			usersWrapper.eq("IDEN_ID", idenId);
			usersWrapper.eq("USER_ID", userId);			
			xauthUsersMapper.update(xauthUsers, usersWrapper);
			
			// 刪除該使用者所有角色
			QueryWrapper<XauthRoleUser> roleUserWrapper = new QueryWrapper<XauthRoleUser>();
			roleUserWrapper.eq("APP_ID", APP_ID);
			roleUserWrapper.eq("IDEN_ID", idenId);
			roleUserWrapper.eq("USER_ID", userId);			
			xauthRoleUserMapper.delete(roleUserWrapper);
		
			// 新增所選角色
			XauthRoleUser xauthRoleUser = new XauthRoleUser();
			xauthRoleUser.setAppId(APP_ID);
			xauthRoleUser.setIdenId(idenId);
			xauthRoleUser.setRoleId("ROLE_SP");
			xauthRoleUser.setUserId(userId);
			xauthRoleUser.setCreUser(getCreOrUpdUser(null));
			xauthRoleUser.setCreDate(now);				
			xauthRoleUserMapper.insert(xauthRoleUser);
			
			if (StringUtils.isNotBlank(pCode)) {
				insUserPdHis(idenId, userId, xauthUsers.getUserPw());
			}
						
			QueryWrapper<UserInfoExt> userInfoExtWrapper = new QueryWrapper<UserInfoExt>();
			userInfoExtWrapper.eq("APP_ID", APP_ID);
			userInfoExtWrapper.eq("IDEN_ID", idenId);
			userInfoExtWrapper.eq("USER_ID", userId);			
			UserInfoExt userInfoExt = userInfoExtMapper.selectOne(userInfoExtWrapper);
			
			if (userInfoExt != null) {				
				userInfoExt.setUpdUser(getCreOrUpdUser(null));
				userInfoExt.setUpdDate(now);				
				userInfoExtMapper.update(userInfoExt, userInfoExtWrapper);
			} else {
				userInfoExt = new UserInfoExt();
				userInfoExt.setAppId(APP_ID);
				userInfoExt.setIdenId(idenId);
				userInfoExt.setUserId(userId);
				userInfoExt.setBgnDate(now);			
				userInfoExt.setCreUser(getCreOrUpdUser(null));
				userInfoExt.setCreDate(now);				
				userInfoExtMapper.insert(userInfoExt);
			}
			
			result.setStatus(ProcessResult.OK);
			result.addMessage(MSG_KEY.UPDATE_OK.getMessage());
		} else {
			result.setStatus(ProcessResult.NG);
			result.addMessage(MSG_KEY.PARAMS_EMPTY.getMessage());
		}
		
		return result;
	}
	
	/**
	 * 供應商啟用/停用 ＆＆ 使用者啟用/停用
	 * @param xauthUsers
	 * @param isStop
	 * @return
	 * @throws Exception
	 */
	public ProcessResult bgnOrStopUser(XauthUsers xauthUsers, String enabled) throws Exception {
		Date now = new Timestamp(new Date().getTime());
		ProcessResult result = new ProcessResult();
		Map<String, Object> data = new HashMap<String, Object>();
		
		if(xauthUsers != null) {
			String idenId = xauthUsers.getIdenId();
			String userId = xauthUsers.getUserId();
			
			data = getUser(xauthUsers);
			if (data == null) {
				result.setStatus(ProcessResult.NG);
				result.addMessage(MSG_KEY.QUERY_EMPTY.getMessage());
				return result;
			}
			
			xauthUsers.setUserPw((String)data.get("USER_PW"));
			if(StringUtils.equals("Y", enabled)) {
				xauthUsers.setEnabled("0");
			}else {
				xauthUsers.setEnabled("1");
			}
			
			xauthUsers.setEmail((String)data.get("EMAIL"));
			xauthUsers.setUserCname((String)data.get("USER_CNAME"));
			xauthUsers.setUserType((String)data.get("USER_TYPE"));
			xauthUsers.setIsFirst((String)data.get("IS_FIRST"));
			xauthUsers.setUpdUser(getCreOrUpdUser(null));			
			xauthUsers.setUpdDate(now);
			
			QueryWrapper<XauthUsers> usersWrapper = new QueryWrapper<XauthUsers>();
			usersWrapper.eq("APP_ID", APP_ID);
			usersWrapper.eq("IDEN_ID", idenId);
			usersWrapper.eq("USER_ID", userId);			
			xauthUsersMapper.update(xauthUsers, usersWrapper);
			
			QueryWrapper<UserInfoExt> userInfoExtWrapper = new QueryWrapper<UserInfoExt>();
			userInfoExtWrapper.eq("APP_ID", APP_ID);
			userInfoExtWrapper.eq("IDEN_ID", idenId);
			userInfoExtWrapper.eq("USER_ID", userId);			
			
			UserInfoExt userInfoExt = userInfoExtMapper.selectOne(userInfoExtWrapper);
			userInfoExt.setUpdUser(getCreOrUpdUser(null));
			userInfoExt.setUpdDate(now);
			
			if(StringUtils.equals("Y", enabled)) {
				userInfoExt.setEndDate(now);
				result.setStatus(ProcessResult.OK);
				result.addMessage("停用成功");
			} else {
				userInfoExt.setEndDate(null);
				result.setStatus(ProcessResult.OK);
				result.addMessage("啟用成功");
			}
			
			userInfoExtMapper.update(userInfoExt, userInfoExtWrapper);
		}
		
		return result;
	}
	
	/**
	 * 刪除使用者資料
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public ProcessResult deleteUser(Suppliermaster suppliermaster) throws Exception {
		ProcessResult result = new ProcessResult();
		
		if (suppliermaster != null) {
			String idenId = suppliermaster.getIdenid();
			String userId = suppliermaster.getSuppliercode();
						
			QueryWrapper<XauthUsers> usersWrapper = new QueryWrapper<XauthUsers>();
			usersWrapper.eq("APP_ID", APP_ID);
			usersWrapper.eq("IDEN_ID", idenId);
			usersWrapper.eq("USER_ID", userId);			
			xauthUsersMapper.delete(usersWrapper);
			
			QueryWrapper<XauthRoleUser> roleUserWrapper = new QueryWrapper<XauthRoleUser>();
			roleUserWrapper.eq("APP_ID", APP_ID);
			roleUserWrapper.eq("IDEN_ID", idenId);
			roleUserWrapper.eq("USER_ID", userId);			
			xauthRoleUserMapper.delete(roleUserWrapper);
						
			QueryWrapper<XauthUsersPwdHistory> usersPwdHistoryWrapper = new QueryWrapper<XauthUsersPwdHistory>();
			usersPwdHistoryWrapper.eq("APP_ID", APP_ID);
			usersPwdHistoryWrapper.eq("IDEN_ID", idenId);
			usersPwdHistoryWrapper.eq("USER_ID", userId);			
			xauthUsersPwdHistoryMapper.delete(usersPwdHistoryWrapper);
			
			QueryWrapper<UserInfoExt> userInfoExtWrapper = new QueryWrapper<UserInfoExt>();
			userInfoExtWrapper.eq("APP_ID", APP_ID);
			userInfoExtWrapper.eq("IDEN_ID", idenId);
			userInfoExtWrapper.eq("USER_ID", userId);			
			userInfoExtMapper.delete(userInfoExtWrapper);
			
			result.setStatus(ProcessResult.OK);
			result.addMessage(MSG_KEY.DELETE_OK.getMessage());
		} else {
			result.setStatus(ProcessResult.NG);
			result.addMessage(MSG_KEY.PARAMS_EMPTY.getMessage());
		}
		
		return result;
	}
	
	/**
	 * 新增使用者密碼歷史資料
	 * @param idenId
	 * @param userId
	 * @param p
	 * @throws Exception
	 */
	private void insUserPdHis(String idenId, String userId, String p) throws Exception {
		Date now = new Timestamp(new Date().getTime());
		XauthUsersPwdHistory xauthUsersPwdHistory = new XauthUsersPwdHistory();
		xauthUsersPwdHistory.setAppId(APP_ID);
		xauthUsersPwdHistory.setIdenId(idenId);
		xauthUsersPwdHistory.setUserId(userId);
		xauthUsersPwdHistory.setUserPw(p);
		xauthUsersPwdHistory.setCreUser(getCreOrUpdUser(null));
		xauthUsersPwdHistory.setCreDate(now);		
		xauthUsersPwdHistoryMapper.insert(xauthUsersPwdHistory);
	}
	
	/**
	 * 取得使用者單筆資料
	 * @param xauthUsers
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> getUser(XauthUsers xauthUsers) throws Exception {		
		if (xauthUsers != null) {
			List<Map<String, Object>> dataList = xauthMapper.searchUser(ConvertUtils.Object2Map(xauthUsers));
			if (dataList != null && dataList.size() > 0) {		
				dataList = ConvertUtils.Map2Camel(dataList);
				return dataList.get(0);
			}
		}
		
		return null;
	}
	
	/**
	 * Check Input Value
	 * @param params
	 * @return
	 */
	public ProcessResult checkInput(Map<String, Object> params) {
		ProcessResult processResult = new ProcessResult();
		processResult.setStatus(ProcessResult.OK);
		
		String suppliercname = MapUtils.getString(params, "suppliercname");
		String suppliergui = MapUtils.getString(params, "suppliergui");
		String suppliercode = MapUtils.getString(params, "suppliercode");
		
//		String supplierename = MapUtils.getString(params, "supplierename");
//		String supplieretel = MapUtils.getString(params, "supplieretel");
//		String picuser = MapUtils.getString(params, "picuser");
//		String supplieremail = MapUtils.getString(params, "supplieremail");
//		String suppliercode = MapUtils.getString(params, "suppliercode");
//		String suppliereaddr = MapUtils.getString(params, "suppliereaddr");
//		String contacruser = MapUtils.getString(params, "contacruser");
//		String suppliercaddr = MapUtils.getString(params, "suppliercaddr");
//		String suppliertype = MapUtils.getString(params, "suppliertype");
		
		if(StringUtils.isBlank(suppliergui)) {
			processResult.setStatus(ProcessResult.NG);
			processResult.addMessage("供應商統編，不得為空");
		} else if(StringUtils.isBlank(suppliercode)) {
			processResult.setStatus(ProcessResult.NG);
			processResult.addMessage("供應商廠編，不得為空");
		} else if(StringUtils.isBlank(suppliercname)) {
			processResult.setStatus(ProcessResult.NG);
			processResult.addMessage("供應商中文名稱，不得為空");
		}
		
		return processResult;
	}
	
	/**
	 * 取得供應商資訊
	 * 
	 * @param keyData
	 * @return
	 */
	public Suppliermaster getUpdataData(Map<String, Object> keyData) {
		
		Suppliermaster result = null;
		String supplierid = MapUtils.getString(keyData, "supplierid");
		QueryWrapper<Suppliermaster> queryWrapper = new QueryWrapper<Suppliermaster>();
		queryWrapper.eq("SUPPLIERID", supplierid);
		List<Suppliermaster> supplierMasterList = suppliermasterMapper.selectList(queryWrapper);
		if(supplierMasterList.size() > 0) {
			result = supplierMasterList.get(0);
		}
		
		return result;
	}
	
}
