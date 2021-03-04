package com.tradevan.pkis.backend.service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.crypto.SealedObject;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.tradevan.mapper.pkis.dao.CodelistMapper;
import com.tradevan.mapper.pkis.dao.SuppliermasterMapper;
import com.tradevan.mapper.pkis.dao.UserInfoExtMapper;
import com.tradevan.mapper.pkis.model.Codelist;
import com.tradevan.mapper.pkis.model.Suppliermaster;
import com.tradevan.mapper.pkis.model.UserInfoExt;
import com.tradevan.mapper.xauth.dao.XauthMapper;
import com.tradevan.mapper.xauth.dao.XauthRoleUserMapper;
import com.tradevan.mapper.xauth.dao.XauthUsersMapper;
import com.tradevan.mapper.xauth.model.XauthRoleUser;
import com.tradevan.mapper.xauth.model.XauthUsers;
import com.tradevan.xauthframework.common.bean.ProcessResult;
import com.tradevan.xauthframework.common.utils.ConvertUtils;
import com.tradevan.xauthframework.common.utils.SensitiveUtils;

@Service("ImportSupplierMasterService")
@Transactional(rollbackFor=Exception.class)
public class ImportSupplierMasterService{
	
	@Autowired
	CodelistMapper codelistMapper;
	
	@Autowired
	SuppliermasterMapper suppliermasterMapper;
	
	@Autowired
	XauthMapper xauthMapper;
	
	@Autowired
	XauthUsersMapper xauthUsersMapper;
	
	@Autowired
	XauthRoleUserMapper xauthRoleUserMapper;
	
	@Autowired
	UserInfoExtMapper userInfoExtMapper;
	
	private static String SYS_USER = "SYSTEM";
	
	private static String APP_ID = "APPKIS";
	
	private static String IDEN_ID = "999999999";
	
	private static int NSC_DATA_LENGTH = 22;
	
	private static int SC_DATA_LENGTH = 9;
	
	public ProcessResult saveSCSuppliermaster(String data) throws Exception{
		ProcessResult result = new ProcessResult();
		Date now = new Timestamp(new Date().getTime());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
		Suppliermaster suppliermaster = new Suppliermaster();
		String years = sdf.format(now);
		String pCode = "";
		
		if(StringUtils.isBlank(data)) {
			return null;
		}
		String[] datas = data.split("\\|", -1);
		byte[] bytes = {-17, -69, -65}; // 檔案多餘byte過濾
		datas[0] = datas[0].replaceAll(new String(bytes), "");
		
		if(datas.length != SC_DATA_LENGTH) {
			result.setStatus(ProcessResult.NG);
			result.addMessage("### 欄位應為 " + SC_DATA_LENGTH + " 此筆資料為 " + datas.length + " 個欄位 ###");
			return result;
		}
		
		//檢查供應商資料是否重複
		QueryWrapper<Suppliermaster> queryWrapper = new QueryWrapper<Suppliermaster>();
		queryWrapper.eq("SUPPLIERGUI", datas[1]);
		queryWrapper.eq("SUPPLIERCODE", datas[0].substring(2, datas[0].length()));
		queryWrapper.eq("DEPTNO", datas[0].substring(0, 2));
		List<Suppliermaster> supplierList = suppliermasterMapper.selectList(queryWrapper);
		
		if(supplierList.size() > 0) {
			suppliermaster = supplierList.get(0);
			// 判斷更新時機 : 上次為系統更新
			if(StringUtils.isBlank(suppliermaster.getUpdateuser()) || StringUtils.equals("SYSTEM", suppliermaster.getUpdateuser())) {
				suppliermaster.setIdenid(IDEN_ID);
				suppliermaster.setEnabled("Y");
				suppliermaster.setDeptno(datas[0].substring(0, 2));
				suppliermaster.setSuppliercode(datas[0].substring(2, datas[0].length()));
				suppliermaster.setSuppliergui(datas[1]);
				suppliermaster.setSuppliercname(datas[2]);
				suppliermaster.setSupplierename(datas[3]);
				//負責人註解
//				suppliermaster.setPicuser(datas[4]);
				suppliermaster.setSuppliercaddr(datas[5]);
				suppliermaster.setSuppliereaddr(datas[6]);
				suppliermaster.setSupplieretel(datas[7]);
				suppliermaster.setUpdateuser(SYS_USER);
				suppliermaster.setUpdatedate(now);
				
				UpdateWrapper<Suppliermaster> updateWrapper = new UpdateWrapper<Suppliermaster>();
				updateWrapper.eq("SUPPLIERID", supplierList.get(0).getSupplierid());
				updateWrapper.eq("SUPPLIERGUI", datas[1]);
				updateWrapper.eq("SUPPLIERCODE", datas[0].substring(2, datas[0].length()));
				updateWrapper.eq("DEPTNO", datas[0].substring(0, 2));
				
				//更新供應商主檔
				suppliermasterMapper.update(suppliermaster, updateWrapper);
			}

		}else {
			//供應商流水號
			String code = getNumCode(years, datas[0].substring(0, 2), datas[0].substring(2, datas[0].length()));
			//供應商代碼(格式 SP + yyyy + 流水號)  -> 2020/7/22編碼規則改為 SP + 部門 + 供應商廠編 + 流水碼(2碼)-如無部門則捨去部門
			suppliermaster.setSupplierid("SP" + datas[0] + code);
			
			suppliermaster.setIdenid(IDEN_ID);
			suppliermaster.setEnabled("Y");
			suppliermaster.setDeptno(datas[0].substring(0, 2));
			suppliermaster.setSuppliercode(datas[0].substring(2, datas[0].length()));
			suppliermaster.setSuppliergui(datas[1]);
			suppliermaster.setSuppliercname(datas[2]);
			suppliermaster.setSupplierename(datas[3]);
			suppliermaster.setPicuser(datas[4]);
			suppliermaster.setSuppliercaddr(datas[5]);
			suppliermaster.setSuppliereaddr(datas[6]);
			suppliermaster.setSupplieretel(datas[7]);
			suppliermaster.setCreateuser(SYS_USER);
			suppliermaster.setCreatedate(now);
			//新增供應商主檔
			suppliermasterMapper.insert(suppliermaster);
		}
		
		pCode = suppliermaster.getSupplierid();
		String userId = suppliermaster.getSupplierid();
		XauthUsers xauthUsers = new XauthUsers();
		xauthUsers.setAppId(APP_ID);
		xauthUsers.setIdenId(IDEN_ID);
		xauthUsers.setUserId(userId);
	
		//判斷使用者是否存在
		if(getUser(xauthUsers) == null) {
			insertUser(suppliermaster, pCode);
		} else {
			updateUser(suppliermaster);
		}
		return result;
	}
	
	public ProcessResult saveNSCSuppliermaster(String data) throws Exception{
		ProcessResult result = new ProcessResult();
		Date now = new Timestamp(new Date().getTime());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
		Suppliermaster suppliermaster = new Suppliermaster();
		String years = sdf.format(now);
		String pCode = "";
		
		if(StringUtils.isBlank(data)) {
			return null;
		}
		String[] datas = data.split("\\|", -1);
		byte[] bytes = {-17, -69, -65}; // 檔案多餘byte過濾
		datas[0] = datas[0].replaceAll(new String(bytes), "");
		
		if(datas.length != NSC_DATA_LENGTH) {
			result.setStatus(ProcessResult.NG);
			result.addMessage("### 欄位應為 " + NSC_DATA_LENGTH + " 此筆資料為 " + datas.length + " 個欄位 ###");
			return result;
		}
		
		if(StringUtils.isBlank(datas[1])) {
			result.setStatus(ProcessResult.NG);
			result.addMessage("### 廠商編號為空值 ###");
			return result;
		}else {
			if(datas[1].matches("[^a-zA-Z_0-9]")) {
				result.setStatus(ProcessResult.NG);
				result.addMessage("### 廠商編號必須為數字加英文 ###");
				return result;
			}
		}
		
		//檢查供應商資料是否重複
		QueryWrapper<Suppliermaster> queryWrapper = new QueryWrapper<Suppliermaster>();
		queryWrapper.eq("SUPPLIERGUI", datas[2]);
		queryWrapper.eq("SUPPLIERCODE", datas[1]);
		queryWrapper.isNull("DEPTNO");
		List<Suppliermaster> supplierList = suppliermasterMapper.selectList(queryWrapper);
		
		if(supplierList.size() > 0) {
			suppliermaster = supplierList.get(0);
			// 判斷更新時機 : 上次為系統更新
			if(StringUtils.isBlank(suppliermaster.getUpdateuser()) || StringUtils.equals("SYSTEM", suppliermaster.getUpdateuser())) {
				suppliermaster.setIdenid(IDEN_ID);
				suppliermaster.setEnabled("Y");
				suppliermaster.setSuppliercode(datas[1]);
				suppliermaster.setSuppliergui(datas[2]);
				suppliermaster.setSuppliercname(datas[3]);
				//負責人註解不更新
//				suppliermaster.setPicuser(datas[11]);
				suppliermaster.setSuppliercaddr(datas[19]);
				suppliermaster.setSupplieretel(datas[5]);
				suppliermaster.setUpdateuser(SYS_USER);
				suppliermaster.setUpdatedate(now);
				
				UpdateWrapper<Suppliermaster> updateWrapper = new UpdateWrapper<Suppliermaster>();
				updateWrapper.eq("SUPPLIERGUI", datas[2]);
				updateWrapper.eq("SUPPLIERCODE", datas[1]);
				updateWrapper.isNull("DEPTNO");
				
				//更新供應商主檔
				suppliermasterMapper.update(suppliermaster, updateWrapper);
			}
		}else {
			//供應商流水號
			String code = getNumCode(years, null, datas[1]);
			//供應商代碼(格式 SP + yyyy + 流水號)  -> 2020/7/22編碼規則改為 SP + 部門 + 供應商廠編 + 流水碼(2碼)-如無部門則捨去部門
			suppliermaster.setSupplierid("SP" + datas[1] + code);
			
			suppliermaster.setIdenid(IDEN_ID);
			suppliermaster.setEnabled("Y");
			suppliermaster.setSuppliercode(datas[1]);
			suppliermaster.setSuppliergui(datas[2]);
			suppliermaster.setSuppliercname(datas[3]);
			suppliermaster.setPicuser(datas[11]);
			suppliermaster.setSuppliercaddr(datas[19]);
			suppliermaster.setSupplieretel(datas[5]);
			suppliermaster.setCreateuser(SYS_USER);
			suppliermaster.setCreatedate(now);
			//新增供應商主檔
			suppliermasterMapper.insert(suppliermaster);
		}
		
		pCode = suppliermaster.getSupplierid();
		String userId = suppliermaster.getSupplierid();
		XauthUsers xauthUsers = new XauthUsers();
		xauthUsers.setAppId(APP_ID);
		xauthUsers.setIdenId(IDEN_ID);
		xauthUsers.setUserId(userId);
		
		//判斷使用者是否存在
		if(getUser(xauthUsers) == null) {
			insertUser(suppliermaster, pCode);
		} else {
			updateUser(suppliermaster);
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
		Codelist codelist = null;
		
		QueryWrapper<Codelist> queryWrapper = new QueryWrapper<Codelist>();
		
		queryWrapper.eq("SYS", "SEQUENCE");
		queryWrapper.eq("CLASSTYPE", "SP");
		queryWrapper.eq("YEARS", years);
		queryWrapper.eq("SUPPLIERCODE", suppliercode);
		if(StringUtils.isNotBlank(deptno)) {
			queryWrapper.eq("DEPTNO", deptno);
		}else {
			queryWrapper.isNull("DEPTNO");
		}
		
		List<Codelist> codeList = codelistMapper.selectList(queryWrapper);
		
		if(codeList.size() == 0) {
			codelist = new Codelist();
			resultString = "01";
			codelist.setSys("SEQUENCE");
			codelist.setClasstype("SP");
			codelist.setYears(years);
			codelist.setAnumber(2);
			codelist.setNote("序號");
			if(deptno != null) {
				codelist.setDeptno(deptno);
			}
			codelist.setSuppliercode(suppliercode);
			codelistMapper.insert(codelist);
		}else {
			codelist = codeList.get(0);
			Integer anumber = codelist.getAnumber();
			resultString = StringUtils.leftPad("" + anumber, 2, "0");
			codelist.setAnumber(anumber + 1);
			codelistMapper.update(codelist, queryWrapper);
		}
		
		return resultString;
	}
	
	/**
	 * 新增使用者資料
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public 	ProcessResult insertUser(Suppliermaster suppliermaster, String pCode) throws Exception {
		ProcessResult result = new ProcessResult();
		Date now = new Timestamp(new Date().getTime());
		
		if (suppliermaster != null) {
			String userId = suppliermaster.getSupplierid();
			
			if(StringUtils.isBlank(userId)) {
				result.setStatus(ProcessResult.NG);
				result.addMessage("產生供應商編號發生錯誤");
				return result;
			}
			
			XauthUsers xauthUsers = new XauthUsers();
			xauthUsers.setAppId(APP_ID);
			xauthUsers.setIdenId(IDEN_ID);
			xauthUsers.setUserId(userId);
			
			if (StringUtils.isNotBlank(pCode)) {
				SensitiveUtils sens = new SensitiveUtils();
				SealedObject sealObject = sens.encrypt(pCode, "00000000" + "admin");
				xauthUsers.setPwdhash(pCode);
				xauthUsers.setUserPw(new BCryptPasswordEncoder().encode(sens.decrypt(sealObject, "00000000" + "admin").toString()));
			}else {
				result.setStatus(ProcessResult.NG);
				result.addMessage("密碼欄位不得為空");
				return result;
			}
			
			xauthUsers.setEnabled("1");
			xauthUsers.setEmail(suppliermaster.getSupplieremail());
			xauthUsers.setUserCname(suppliermaster.getSuppliercname());
			xauthUsers.setUserType("02");
			xauthUsers.setIsFirst("1");
			xauthUsers.setCreUser(SYS_USER);
			xauthUsers.setCreDate(now);
			xauthUsersMapper.insert(xauthUsers);
			
			XauthRoleUser xauthRoleUser = new XauthRoleUser();;
			xauthRoleUser.setAppId(APP_ID);
			xauthRoleUser.setIdenId(IDEN_ID);
			xauthRoleUser.setRoleId("ROLE_SP");
			xauthRoleUser.setUserId(userId);
			xauthRoleUser.setCreUser(SYS_USER);
			xauthRoleUser.setCreDate(now);				
			xauthRoleUserMapper.insert(xauthRoleUser);
			
			QueryWrapper<UserInfoExt> queryWrapper = new QueryWrapper<UserInfoExt>();
			queryWrapper.eq("APP_ID", APP_ID);
			queryWrapper.eq("IDEN_ID", IDEN_ID);
			queryWrapper.eq("USER_ID", userId);			
			UserInfoExt userInfoExt = userInfoExtMapper.selectOne(queryWrapper);
			
			if (userInfoExt != null) {
				userInfoExt.setUpdUser(SYS_USER);
				userInfoExt.setUpdDate(now);				
				userInfoExtMapper.update(userInfoExt, queryWrapper);
			} else {
				userInfoExt = new UserInfoExt();
				userInfoExt.setAppId(APP_ID);
				userInfoExt.setIdenId(IDEN_ID);
				userInfoExt.setUserId(userId);
				userInfoExt.setBgnDate(now);			
				userInfoExt.setCreUser(SYS_USER);
				userInfoExt.setCreDate(now);				
				userInfoExtMapper.insert(userInfoExt);
			}
			result.setStatus(ProcessResult.OK);
		}else {
			result.setStatus(ProcessResult.NG);
			result.addMessage("供應商資料發生錯誤");
			return result;
		}
			
		return result;
	}
	
	/**
	 * 更新使用者資料
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public ProcessResult updateUser(Suppliermaster suppliermaster) throws Exception {
		ProcessResult result = new ProcessResult();
		Date now = new Timestamp(new Date().getTime());
		
		if (suppliermaster != null) {
			
			String userId = suppliermaster.getSupplierid();
			
			XauthUsers xauthUsers = new XauthUsers();
			xauthUsers.setUserCname(suppliermaster.getSuppliercname());
			xauthUsers.setUpdUser(SYS_USER);			
			xauthUsers.setUpdDate(now);
			
			QueryWrapper<XauthUsers> usersWrapper = new QueryWrapper<XauthUsers>();
			usersWrapper.eq("APP_ID", APP_ID);
			usersWrapper.eq("IDEN_ID", IDEN_ID);
			usersWrapper.eq("USER_ID", userId);
			xauthUsersMapper.update(xauthUsers, usersWrapper);
			
			// 刪除該使用者所有角色
			QueryWrapper<XauthRoleUser> roleUserWrapper = new QueryWrapper<XauthRoleUser>();
			roleUserWrapper.eq("APP_ID", APP_ID);
			roleUserWrapper.eq("IDEN_ID", IDEN_ID);
			roleUserWrapper.eq("USER_ID", userId);			
			xauthRoleUserMapper.delete(roleUserWrapper);
		
			// 新增所選角色
			XauthRoleUser xauthRoleUser = new XauthRoleUser();
			xauthRoleUser.setAppId(APP_ID);
			xauthRoleUser.setIdenId(IDEN_ID);
			xauthRoleUser.setRoleId("ROLE_SP");
			xauthRoleUser.setUserId(userId);
			xauthRoleUser.setCreUser(SYS_USER);
			xauthRoleUser.setCreDate(now);				
			xauthRoleUserMapper.insert(xauthRoleUser);
			
			QueryWrapper<UserInfoExt> userInfoExtWrapper = new QueryWrapper<UserInfoExt>();
			userInfoExtWrapper.eq("APP_ID", APP_ID);
			userInfoExtWrapper.eq("IDEN_ID", IDEN_ID);
			userInfoExtWrapper.eq("USER_ID", userId);			
			UserInfoExt userInfoExt = userInfoExtMapper.selectOne(userInfoExtWrapper);
			
			if (userInfoExt != null) {
				userInfoExt.setUpdUser(SYS_USER);
				userInfoExt.setUpdDate(now);				
				userInfoExtMapper.update(userInfoExt, userInfoExtWrapper);
			} else {
				userInfoExt = new UserInfoExt();
				userInfoExt.setAppId(APP_ID);
				userInfoExt.setIdenId(IDEN_ID);
				userInfoExt.setUserId(userId);
				userInfoExt.setBgnDate(now);			
				userInfoExt.setCreUser(SYS_USER);
				userInfoExt.setCreDate(now);				
				userInfoExtMapper.insert(userInfoExt);
			}
			result.setStatus(ProcessResult.OK);
		} else {
			result.setStatus(ProcessResult.NG);
			result.addMessage("供應商資料發生錯誤");
			return result;
		}
		
		return result;
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
	
}
