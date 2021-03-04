package com.tradevan.pkis.backend.service;

import java.io.File;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tradevan.apcommon.util.DateUtil;
import com.tradevan.aporg.enums.UserType;
import com.tradevan.mapper.pkis.dao.DeptInfoExtMapper;
import com.tradevan.mapper.pkis.dao.UserInfoExtMapper;
import com.tradevan.mapper.pkis.model.DeptInfoExt;
import com.tradevan.mapper.pkis.model.UserInfoExt;
import com.tradevan.mapper.xauth.dao.XauthDeptMapper;
import com.tradevan.mapper.xauth.dao.XauthIdenMenuMapper;
import com.tradevan.mapper.xauth.dao.XauthRoleAgentUserMapper;
import com.tradevan.mapper.xauth.dao.XauthRoleMapper;
import com.tradevan.mapper.xauth.dao.XauthRoleMenuMapper;
import com.tradevan.mapper.xauth.dao.XauthRoleUserMapper;
import com.tradevan.mapper.xauth.dao.XauthUsersMapper;
import com.tradevan.mapper.xauth.model.XauthDept;
import com.tradevan.mapper.xauth.model.XauthIdenMenu;
import com.tradevan.mapper.xauth.model.XauthRole;
import com.tradevan.mapper.xauth.model.XauthRoleAgentUser;
import com.tradevan.mapper.xauth.model.XauthRoleMenu;
import com.tradevan.mapper.xauth.model.XauthRoleUser;
import com.tradevan.mapper.xauth.model.XauthUsers;
import com.tradevan.pkis.backend.batch.ImportCsvBatch;
import com.tradevan.pkis.backend.bean.XauthUsersBean;
import com.tradevan.pkis.backend.config.DefaultService;
import com.tradevan.xauthframework.common.utils.DateUtils;
import com.tradevan.xauthframework.common.utils.MapUtils;
import com.tradevan.xauthframework.common.utils.SensitiveUtils;

@Service("ImportCsvBatchService")
@Transactional(rollbackFor=Exception.class)
public class ImportCsvBatchService extends DefaultService {
	
	public static String LABEL_MAIN = ImportCsvBatch.LABEL_MAIN;
	
	/* 是否要過濾掉 部門CSV 重複資料 */
	public boolean checkCsvRepeatDept = false;
	
	/* 是否要過濾掉 角色CSV 重複資料 */
	public boolean checkCsvRepeatRole = false;
	
	@Autowired
	XauthUsersMapper xauthUsersMapper;
	
	@Autowired
	XauthRoleMapper xauthRoleMapper;
	
	@Autowired
	XauthRoleUserMapper xauthRoleUserMapper;
	
	@Autowired
	UserInfoExtMapper userInfoExtMapper;
	
	@Autowired
	DeptInfoExtMapper deptInfoExtMapper;
	
	@Autowired
	XauthDeptMapper xauthDeptMapper;
	
	@Autowired
	XauthIdenMenuMapper xauthIdenMenuMapper;
	
	@Autowired
	XauthRoleMenuMapper xauthRoleMenuMapper;
	
	@Autowired
	XauthRoleAgentUserMapper xauthRoleAgentUserMapper;
	
	@Value("${xauth.appId}")
	private String appId;
	
	public void save(int i, XauthUsersBean xauthUsersbean, Map<String, String> xauthDeptMap, Map<String, String> xauthRolesMap, List<String> errorMessageList) throws Exception {
		String label = LABEL_MAIN + "【人員角色資料存檔】【" + xauthUsersbean.getUserId() + "】";
		logger.debug(label + "START");
		
		XauthUsers entity = new XauthUsers();
		entity.setAppId(xauthUsersbean.getAppId()); 
		entity.setIdenId(xauthUsersbean.getIdenId());
		entity.setUserId(xauthUsersbean.getUserId());
		entity.setUserCname(xauthUsersbean.getUserCname());
		entity.setPwdhash(xauthUsersbean.getpHash()); 
		entity.setUserPw(xauthUsersbean.getUserPw());
		entity.setEmail(xauthUsersbean.getEmail());
		entity.setUserType(xauthUsersbean.getUserType()); // 02:使用者
		entity.setIsFirst(xauthUsersbean.getIsFirst());
		entity.setEnabled(xauthUsersbean.getEnabled());
		entity.setCreUser(xauthUsersbean.getCreUser()); 
		entity.setCreDate(xauthUsersbean.getCreDate());
		entity.setUpdUser(xauthUsersbean.getUpdUser()); 
		entity.setUpdDate(xauthUsersbean.getUpdDate());
		
		// ------------------------------------------------------------------------------
		//     XAUTH_USER , PKEY = APP_ID + IDEN_ID + USER_ID
		// ------------------------------------------------------------------------------
		try {
			QueryWrapper<XauthUsers> entityWrapper = new QueryWrapper<XauthUsers>();
			entityWrapper.eq("APP_ID", entity.getAppId());
//			entityWrapper.eq("IDEN_ID", entity.getIdenId());
			entityWrapper.eq("USER_ID", entity.getUserId());
			
			if(xauthUsersMapper.selectCount(entityWrapper).intValue() > 0) {
				XauthUsers entityUpd = xauthUsersMapper.selectList(entityWrapper).get(0);
				// 部門有改變，刪掉當筆代理人與被代理人資料
				if(!StringUtils.equals(entityUpd.getIdenId(), entity.getIdenId())) {
					delAgentUser(entity.getUserId());
				}
				entityUpd.setIdenId(entity.getIdenId());
				entityUpd.setUserCname(entity.getUserCname());
				entityUpd.setEmail(entity.getEmail());
				entityUpd.setUserType(entity.getUserType()); // 02:使用者
				entityUpd.setEnabled(entity.getEnabled());
				entityUpd.setUpdUser(entity.getUpdUser()); 
				entityUpd.setUpdDate(entity.getUpdDate());
				xauthUsersMapper.update(entityUpd, entityWrapper);
				logger.debug(label + "更新 XAUTH_USER");
			} else {
				xauthUsersMapper.insert(entity);
				logger.debug(label + "新增 XAUTH_USER");
			}
		} catch(Exception e) {
			errorMessageList.add("人員資料第" + i + "筆，儲存XAUTH_USERS錯誤，原因為 : " + e.getMessage());
			throw new Exception(e);
		}
		// ------------------------------------------------------------------------------
		//     USER_INFO_EXT , PKEY = APP_ID + IDEN_ID + USER_ID
		// ------------------------------------------------------------------------------
		try {
			QueryWrapper<UserInfoExt> userInfoExtWrapper = new QueryWrapper<UserInfoExt>();
			userInfoExtWrapper.eq("APP_ID", entity.getAppId());
//			userInfoExtWrapper.eq("IDEN_ID", entity.getIdenId());
			userInfoExtWrapper.eq("USER_ID", entity.getUserId());
			
			UserInfoExt userInfoExt = new UserInfoExt();
			userInfoExt.setAppId(entity.getAppId());
			userInfoExt.setIdenId(entity.getIdenId());
			userInfoExt.setUserId(entity.getUserId());
			userInfoExt.setBgnDate(DateUtils.converStr2Date(DateUtil.formatDate(new Date(), DateUtil.FMT_YYYY_MM_DD), "yyyy/MM/dd"));			
			userInfoExt.setEndDate(DateUtils.converStr2Date(MapUtils.getEmptyString(new HashMap<String,String>(), "endDate"), "yyyy/MM/dd"));
			userInfoExt.setCreUser(entity.getCreUser());
			userInfoExt.setCreDate(entity.getCreDate());
			userInfoExt.setUpdUser(entity.getUpdUser());
			userInfoExt.setUpdDate(entity.getUpdDate());
			
			if(userInfoExtMapper.selectCount(userInfoExtWrapper) > 0) {
				UserInfoExt userInfoExtUpd = userInfoExtMapper.selectList(userInfoExtWrapper).get(0);
				userInfoExtUpd.setIdenId(userInfoExt.getIdenId());
				userInfoExtUpd.setBgnDate(userInfoExt.getBgnDate());			
				userInfoExtUpd.setEndDate(userInfoExt.getEndDate());
				userInfoExtUpd.setUpdUser(userInfoExt.getUpdUser());
				userInfoExtUpd.setUpdDate(userInfoExt.getUpdDate());
				userInfoExtMapper.update(userInfoExtUpd, userInfoExtWrapper);
				logger.debug(label + "更新 USER_INFO_EXT");
			} else {
				userInfoExtMapper.insert(userInfoExt);
				logger.debug(label + "新增 USER_INFO_EXT");
			}
		} catch(Exception e) {
			errorMessageList.add("人員資料第" + i + "筆，儲存USER_INFO_EXT錯誤，原因為 : " + e.getMessage());
			throw new Exception(e);
		}

		
		// ------------------------------------------------------------------------------
		//     XAUTH_ROLE , PKEY = APP_ID + IDEN_ID + ROLE_ID
		// ------------------------------------------------------------------------------
		String[] lineArray = null;
		String roleCount = "";
		String roleCName = "";
		
		try {
			if(xauthRolesMap.containsKey(xauthUsersbean.getRoleId())) {
				// 表示人員的角色id存在於TITLE的CSV裡
				String roleId = "ROLE_" + xauthUsersbean.getRoleId();	
				lineArray = MapUtils.getString(xauthRolesMap, xauthUsersbean.getRoleId()).split(",");
				roleCount = lineArray[0];
				roleCName = lineArray[1];
				
				QueryWrapper<XauthRole> xauthRoleWrapper = new QueryWrapper<XauthRole>();
				xauthRoleWrapper.eq("APP_ID", entity.getAppId());
				xauthRoleWrapper.eq("IDEN_ID", entity.getIdenId());
				xauthRoleWrapper.eq("ROLE_ID", roleId);
				
				XauthRole xauthRole = new XauthRole();
				xauthRole.setAppId(entity.getAppId());
				xauthRole.setIdenId(entity.getIdenId());
				xauthRole.setRoleId(roleId);
				xauthRole.setRoleCname(roleCName); // 因為line第一個資料為筆數，故取資料需再加一
//				xauthRole.setLevel(0);
//				xauthRole.setSysid(entity.getAppId());
//				xauthRole.setIsdeptrole(0);
//				xauthRole.setStatus("1");
				xauthRole.setCreUser(entity.getCreUser());
				xauthRole.setCreDate(entity.getCreDate());
				xauthRole.setUpdUser(entity.getUpdUser());
				xauthRole.setUpdDate(entity.getUpdDate());
				
				if(xauthRoleMapper.selectCount(xauthRoleWrapper) > 0) {
					XauthRole xauthRoleUpd = xauthRoleMapper.selectList(xauthRoleWrapper).get(0);
					xauthRoleUpd.setRoleCname(xauthRole.getRoleCname()); // 因為line第一個資料為筆數，故取資料需再加一
					xauthRoleUpd.setUpdUser(xauthRole.getUpdUser());
					xauthRoleUpd.setUpdDate(xauthRole.getUpdDate());
					xauthRoleMapper.update(xauthRoleUpd, xauthRoleWrapper);
					logger.debug(label + "更新 XAUTH_ROLE");
				} else {
					xauthRoleMapper.insert(xauthRole);
					logger.debug(label + "新增 XAUTH_ROLE");
				}
			} else {
				throw new Exception(label + "emp CSV 的 ROLE_ID，並未存在 TITLE CSV 裡");
			}
		} catch(Exception e) {
			errorMessageList.add("角色資料第" + roleCount + "筆，儲存XAUTH_ROLE錯誤，原因為 : " + e.getMessage());
			throw new Exception(e);
		}

		// ------------------------------------------------------------------------------
		//     XAUTH_ROLE_USER , PKEY = APP_ID + IDEN_ID + ROLE_ID + USER_ID
		// ------------------------------------------------------------------------------
		try {
			String roleId = "ROLE_" + xauthUsersbean.getRoleId();
			
			// 先刪除此部門及人員的資料
			QueryWrapper<XauthRoleUser> xauthRoleUserWrapper = new QueryWrapper<XauthRoleUser>();
			xauthRoleUserWrapper.eq("APP_ID", entity.getAppId());
//			xauthRoleUserWrapper.eq("IDEN_ID", entity.getIdenId());
			xauthRoleUserWrapper.eq("USER_ID", entity.getUserId());
			List<XauthRoleUser> roleUserList = xauthRoleUserMapper.selectList(xauthRoleUserWrapper);
			if(roleUserList != null && roleUserList.size() > 0) {
				if(!StringUtils.equals(roleUserList.get(0).getRoleId(), roleId)) {
					updAgentUser(entity.getUserId(), entity.getIdenId(), roleId);
				}
				xauthRoleUserMapper.delete(xauthRoleUserWrapper);
				logger.debug(label + "刪除 XAUTH_ROLE_USER，條件為 : APP_ID + USER_ID");
			}
			
			XauthRoleUser xauthRoleUser = new XauthRoleUser();
			xauthRoleUser.setAppId(entity.getAppId());
			xauthRoleUser.setIdenId(entity.getIdenId());
			xauthRoleUser.setRoleId(roleId);
			xauthRoleUser.setUserId(entity.getUserId());
			xauthRoleUser.setCreUser(entity.getCreUser());
			xauthRoleUser.setCreDate(entity.getCreDate());
			xauthRoleUser.setUpdUser(entity.getUpdUser());
			xauthRoleUser.setUpdDate(entity.getUpdDate());
			
			xauthRoleUserMapper.insert(xauthRoleUser);
			logger.debug(label + "新增 XAUTH_ROLE_USER");
			
		} catch(Exception e) {
			errorMessageList.add("人員資料第" + i + "筆，儲存XAUTH_ROLE_USER錯誤，原因為 : " + e.getMessage());
			throw new Exception(e);
		}
		
		// ------------------------------------------------------------------------------
		//     XAUTH_ROLE_MENU , PKEY = APP_ID + IDEN_ID + ROLE_ID
		// ------------------------------------------------------------------------------
		try {
			String roleId = "ROLE_" + xauthUsersbean.getRoleId();
			String[] roleMenuId = {"ROOT", "#", "CRC001", "CRC00101", "CRC00103", "CRC00107", "CRC00108", "XAUTH", "XAUTH_ROLE_AGENT_USER"};
			XauthRoleMenu xauthRoleMenu = null;
			QueryWrapper<XauthRoleMenu> xauthRoleMenuWrapper = new QueryWrapper<XauthRoleMenu>();
			xauthRoleMenuWrapper.eq("APP_ID", entity.getAppId());
			xauthRoleMenuWrapper.eq("IDEN_ID", entity.getIdenId());
			xauthRoleMenuWrapper.eq("ROLE_ID", roleId);
//			xauthRoleMenuWrapper.eq("MENU_ID", menuId);
			List<XauthRoleMenu> xauthRoleMenuList = xauthRoleMenuMapper.selectList(xauthRoleMenuWrapper);
			if(xauthRoleMenuList.size() <= 0) {
				for(String menuId : roleMenuId) {
					xauthRoleMenu = new XauthRoleMenu();
					xauthRoleMenu.setAppId(entity.getAppId());
					xauthRoleMenu.setIdenId(entity.getIdenId());
					xauthRoleMenu.setRoleId(roleId);
					xauthRoleMenu.setMenuId(menuId);
					xauthRoleMenu.setCreUser(entity.getCreUser());
					xauthRoleMenu.setCreDate(entity.getCreDate());
					xauthRoleMenu.setUpdUser(entity.getUpdUser());
					xauthRoleMenu.setUpdDate(entity.getUpdDate());
					xauthRoleMenuMapper.insert(xauthRoleMenu);
				}
			}
			
		} catch(Exception e) {
			errorMessageList.add("角色資料第" + roleCount + "筆，儲存XAUTH_ROLE_MENU錯誤，原因為 : " + e.getMessage());
			throw new Exception(e);
		}

		logger.debug(label + "END");
	}
	
	public void saveDept(String key, String[] deptData) throws Exception {
		String label = LABEL_MAIN + "【部門資料存檔】";
		logger.debug(label + "START");
		Date currentDate = new Timestamp(new Date().getTime());
		String creOrUpdUser = "SYSTEM"; 
		String[] idenMenuId = {"ROOT", "CRC001", "CRC00101", "CRC00103", "CRC00107", "CRC00108", "XAUTH", "XAUTH_ROLE_AGENT_USER"};

		// PKEY : 0H2H0M9Z, line : ﻿TSR,HT,H2H,,,0H2H0M9Z,HT -Hsin Tien StoreSecurity-Caddie boys,0H2H0M9V,0H2H0M9V,S25,F000010055
		
		// ------------------------------------------------------------------------------
		//     XAUTH_DEPT
		// ------------------------------------------------------------------------------
		QueryWrapper<XauthDept> deptWrapper = null;
		QueryWrapper<DeptInfoExt> deptInfoExtWrapper = null;
		QueryWrapper<XauthIdenMenu> idenMenuWrapper = null;
		XauthDept xauthDept = null;
		DeptInfoExt deptInfoExt = null;
		XauthIdenMenu xauthIdenMenu = null;
		
		deptWrapper = new QueryWrapper<XauthDept>();
		deptWrapper.eq("APP_ID", APP_ID);
		deptWrapper.eq("IDEN_ID", key);
		List<XauthDept> xauthDeptList = xauthDeptMapper.selectList(deptWrapper);
		if(xauthDeptList.size() > 0) {
			xauthDept = xauthDeptList.get(0);
			xauthDept.setUpdUser(creOrUpdUser);
			xauthDept.setUpdDate(currentDate);
			xauthDept.setCname(deptData[6]);
			xauthDept.setParentId(StringUtils.equals(key, "0000984") ? "00000000" : deptData[7]); 
			xauthDeptMapper.update(xauthDept, deptWrapper);
		} else {
			xauthDept = new XauthDept();
			xauthDept.setAppId(APP_ID);
			xauthDept.setIdenId(key);
			xauthDept.setCreUser(creOrUpdUser);
			xauthDept.setCreDate(currentDate);
			xauthDept.setUpdUser(creOrUpdUser);
			xauthDept.setUpdDate(currentDate);
			xauthDept.setCname(deptData[6]);
			xauthDept.setEnabled("1");
			xauthDept.setParentId(StringUtils.equals(key, "0000984") ? "00000000" : deptData[7]);
			xauthDept.setParentSeq(0);
			xauthDept.setSeqNo(0);
			xauthDeptMapper.insert(xauthDept);
		}
		
		// ------------------------------------------------------------------------------
		//     DEPT_INFO_EXT
		// ------------------------------------------------------------------------------
		
		deptInfoExtWrapper = new QueryWrapper<DeptInfoExt>();
		deptInfoExtWrapper.eq("APP_ID", APP_ID);
		deptInfoExtWrapper.eq("IDEN_ID", key);
		List<DeptInfoExt> deptInfoExtDb = deptInfoExtMapper.selectList(deptInfoExtWrapper);
		if(deptInfoExtDb.size() > 0) {
			deptInfoExt = deptInfoExtDb.get(0);
			deptInfoExt.setIdenType("01");
			deptInfoExt.setUpdUser(creOrUpdUser);
			deptInfoExt.setUpdDate(currentDate);
			if(deptData.length == 11) {
				deptInfoExt.setDirectManager(deptData[10]);
			}
			deptInfoExtMapper.update(deptInfoExt, deptInfoExtWrapper);
		} else {
			deptInfoExt = new DeptInfoExt();
			deptInfoExt.setAppId(APP_ID);
			deptInfoExt.setIdenId(key);
			deptInfoExt.setIdenType("01");
			deptInfoExt.setCreUser(creOrUpdUser);
			deptInfoExt.setCreDate(currentDate);
			deptInfoExt.setUpdUser(creOrUpdUser);
			deptInfoExt.setUpdDate(currentDate);
			if(deptData.length == 11) {
				deptInfoExt.setDirectManager(deptData[10]);
			}
			deptInfoExtMapper.insert(deptInfoExt);
		}
		
		// ------------------------------------------------------------------------------
		//     XAUTH_IDEN_MENU
		// ------------------------------------------------------------------------------
		idenMenuWrapper = new QueryWrapper<XauthIdenMenu>();
		idenMenuWrapper.eq("APP_ID", APP_ID);
		idenMenuWrapper.eq("IDEN_ID", key);
//		idenMenuWrapper.eq("MENU_ID", menuId);
		List<XauthIdenMenu> xauthIdenMenuList = xauthIdenMenuMapper.selectList(idenMenuWrapper);
		if(xauthIdenMenuList.size() <= 0) {
			for(String menuId : idenMenuId) {
				xauthIdenMenu = new XauthIdenMenu();
				xauthIdenMenu.setAppId(APP_ID);
				xauthIdenMenu.setIdenId(key);
				xauthIdenMenu.setMenuId(menuId);
				xauthIdenMenu.setCreUser(creOrUpdUser);
				xauthIdenMenu.setCreDate(currentDate);
				xauthIdenMenu.setUpdUser(creOrUpdUser);
				xauthIdenMenu.setUpdDate(currentDate);
				xauthIdenMenuMapper.insert(xauthIdenMenu);
			}
		}
	}
	
	/**
	 * 解析部門CSV資料
	 * @return
	 * @throws Exception
	 */
	public Map<String, String> readDeptCsvFile(String file) throws Exception {
		String label = LABEL_MAIN + "【匯入部門資料】";
		logger.debug(label + "START");
		
        long startTime = 0; // 計算花費秒數
        long endTime = 0; // 計算花費秒數
        double spendTime = 0L; // 計算花費秒數 (單位:秒)
		int count = 0; // 總筆數
		int repeatCount = 0; // 重複筆數
		String PKEY = "";
		String line = "";
		String[] lineArray = null;
		File csvFile = new File(file);
		LineIterator lineIterator = null;
		List<String> checkPK = null;
		List<String> repeatPK = new ArrayList<String>(); // 重複PKEY
		List<String> notRepeatPK = new ArrayList<String>(); // 未重複PKEY
		List<String> maxArrayCount = new ArrayList<String>(); // 最大陣列長度
		Map<String, String> result = new HashMap<String, String>(); // 回傳結果：部門資料
		
		try {
			startTime = System.currentTimeMillis();
			
			lineIterator = FileUtils.lineIterator(csvFile, "UTF-8");
			
			while(lineIterator.hasNext()) {
				line = lineIterator.nextLine();
				lineArray = line.split(",");
				
				if(!maxArrayCount.contains((lineArray.length)+"")) {
					maxArrayCount.add((lineArray.length)+"");
				}
				
				PKEY = lineArray[5];
				
				if(checkCsvRepeatDept) {
					checkPK = new ArrayList<String>();
					checkPK.add(PKEY);
				}
				
				if(checkCsvRepeatDept && notRepeatPK.containsAll(checkPK)) {
					repeatCount++;
					if(!repeatPK.containsAll(checkPK)) {
						repeatPK.add(PKEY);
					}
				} else {
					if(checkCsvRepeatDept) {
						notRepeatPK.add(PKEY);
					}
					result.put(PKEY + "_" + (count + 1), line); // 若有重複PKEY，資料後蓋前
//					logger.info(label + "PKEY : " + PKEY + ", line : " + line);
				}
				count++;
			}
		} catch(Exception e) {
			logger.error(label + "ERROR：" + e.getMessage());
			e.printStackTrace();
		} finally {
            endTime = System.currentTimeMillis() ;
			spendTime = (double)(endTime - startTime) / 1000 ;
			
			if(lineIterator != null) {
				lineIterator.close();
			}
			
			logger.info(label + "-------------------------------------------------");
			logger.info(label + "    匯入部門 CSV 統計結果");
			logger.info(label + "-------------------------------------------------");
			logger.info(label + "是否要檢查資料重複：" + checkCsvRepeatDept);
			logger.info(label + "花費時間：" + spendTime + " 秒");
			
			if(checkCsvRepeatDept) {
				logger.info(label + "未重複筆數：" + result.size());
				logger.info(label + "已重複筆數：" + repeatCount);
				logger.info(label + "總筆數：" + count);
			} else {
				logger.info(label + "總筆數：" + result.size());
			}
			
			if(checkCsvRepeatDept) {
				for(String tmp : repeatPK) {
					logger.info(label + "已重複PKEY：" + tmp);
				}
			}
			
			for(String tmp : maxArrayCount) {
				logger.info(label + "最大陣列長度：" + tmp);
			}
			logger.debug(label + "END");
		}
		return result;
	}
	
	/**
	 * 解析角色CSV資料
	 * @return
	 * @throws Exception
	 */
	public Map<String, String> readRoleCsvFile(String file) throws Exception {
		String label = LABEL_MAIN + "【匯入角色資料】";
		logger.debug(label + "START");
		
        long startTime = 0; // 計算花費秒數
        long endTime = 0; // 計算花費秒數
        double spendTime = 0L; // 計算花費秒數 (單位:秒)
		int count = 0; // 總筆數
		int repeatCount = 0; // 重複筆數
		String PKEY = "";
		String line = "";
		String[] lineArray = null;
		File csvFile = new File(file);
		LineIterator lineIterator = null;
		List<String> checkPK = null;
		List<String> repeatPK = new ArrayList<String>(); // 重複PKEY
		List<String> notRepeatPK = new ArrayList<String>(); // 未重複PKEY
		List<String> maxArrayCount = new ArrayList<String>(); // 最大陣列長度
		Map<String, String> result = new HashMap<String, String>(); // 回傳結果：角色資料
		
		try {
			startTime = System.currentTimeMillis();
			
			lineIterator = FileUtils.lineIterator(csvFile, "Big5");
			while(lineIterator.hasNext()) {
				line = lineIterator.nextLine();
				lineArray = line.split(",");
				
				if(!maxArrayCount.contains((lineArray.length)+"")) {
					maxArrayCount.add((lineArray.length)+"");
				}
				
				PKEY = filter(lineArray[2]);//.toUpperCase(); // ROLE_ID需轉大寫
				
				if(checkCsvRepeatRole) {
					checkPK = new ArrayList<String>();
					checkPK.add(PKEY);
				}
				
				if(checkCsvRepeatRole && notRepeatPK.containsAll(checkPK)) {
					repeatCount++;
					if(!repeatPK.containsAll(checkPK)) {
						repeatPK.add(PKEY);
					}
				} else {
					if(checkCsvRepeatRole) {
						notRepeatPK.add(PKEY);
					}
					result.put(PKEY, (count + 1) + "," + line); // 若有重複PKEY，資料後蓋前
//					logger.info(label + "PKEY : " + PKEY + ", line : " + line);
				}
				count++;
			}
		} catch(Exception e) {
			logger.error(label + "ERROR：" + e.getMessage());
			e.printStackTrace();
		} finally {
            endTime = System.currentTimeMillis() ;
			spendTime = (double)(endTime - startTime) / 1000 ;
			
			if(lineIterator != null) {
				lineIterator.close();
			}
			
			logger.info(label + "-------------------------------------------------");
			logger.info(label + "    匯入角色 CSV 統計結果");
			logger.info(label + "-------------------------------------------------");
			logger.info(label + "是否要檢查資料重複：" + checkCsvRepeatRole);
			logger.info(label + "花費時間：" + spendTime + " 秒");
			
			if(checkCsvRepeatRole) {
				logger.info(label + "未重複筆數：" + result.size());
				logger.info(label + "已重複筆數：" + repeatCount);
				logger.info(label + "總筆數：" + count);
			} else {
				logger.info(label + "總筆數：" + result.size());
			}
			
			if(checkCsvRepeatRole) {
				for(String tmp : repeatPK) {
					logger.info(label + "已重複PKEY：" + tmp);
				}
			}
			
			for(String tmp : maxArrayCount) {
				logger.info(label + "最大陣列長度：" + tmp);
			}
			logger.debug(label + "END");
		}
		return result;
	}
	
	/**
	 * 解析人員CSV資料
	 * @return
	 * @throws Exception
	 */
	public List<XauthUsersBean> readUserCsvFile(String file, String arg) throws Exception {
		String label = LABEL_MAIN + "【匯入人員資料】";
		logger.debug(label + "START");
		
		int showLogPer = 50;
        long startTime = 0;
        long endTime = 0;
        double spendTime = 0L; // 方法花費多少時間 (單位:秒)
		String IDEN_ID = "";
		String USER_ID = "";
		String ROLE_ID = "";
		String USER_CNAME = "";
		String EMAIL = "";
		String line = "";
		String onJob = "";
		String[] lineArray = null;
		File csvFile = new File(file);
		LineIterator lineIterator = null;
		XauthUsersBean xauthUsers = null;
		BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(); // 與密碼加密有關
		SensitiveUtils sensitiveUtils = new SensitiveUtils(); // 與密碼加密有關
		List<String> maxArrayCount = new ArrayList<String>(); // 最大陣列長度
		List<XauthUsersBean> result = new ArrayList<XauthUsersBean>(); // 回傳結果：人員資料
		
		try {
			startTime = System.currentTimeMillis();
			
			lineIterator = FileUtils.lineIterator(csvFile, "UTF-8");
			
			int i = 0;
			while(lineIterator.hasNext()) {
				line = lineIterator.nextLine();
				lineArray = line.split(",");
				
				if(!maxArrayCount.contains((lineArray.length)+"")) {
					maxArrayCount.add((lineArray.length)+"");
				}
				
				// -------------------------------------------------
				// 資料準備 START
				// -------------------------------------------------
				if(lineArray.length > 0) {
					USER_ID = filter(lineArray[0]);
				}
				
				if(lineArray.length > 1) {
					USER_CNAME = lineArray[1];
				}
				
				if(lineArray.length > 3) {
					ROLE_ID = lineArray[3];//.toUpperCase();
				}
				
				if(lineArray.length > 9) {
					IDEN_ID = lineArray[9];
				}
				
				if(lineArray.length > 12) {
					onJob = lineArray[12];
				}
				
				if(lineArray.length > 13) {
					EMAIL = lineArray[13];
				}

				if(StringUtils.equals(arg, "init") && !StringUtils.equals(onJob, "Y")) {
					continue;
				}
				xauthUsers = new XauthUsersBean();
				xauthUsers.setAppId(appId); 
				xauthUsers.setIdenId(IDEN_ID);
				xauthUsers.setUserId(USER_ID);
				xauthUsers.setUserCname(USER_CNAME);
				xauthUsers.setRoleId(ROLE_ID);
				xauthUsers.setpHash("Test!@#$"); 
				
				xauthUsers.setUserPw(bCryptPasswordEncoder.encode(sensitiveUtils.decrypt(sensitiveUtils.encrypt("Test!@#$", USER_ID), USER_ID).toString()));
//				xauthUsers.setUserPw("1234");
				
				xauthUsers.setEmail(EMAIL);
				xauthUsers.setUserType(UserType.APPKIS02.getCode()); // 02:使用者
				xauthUsers.setIsFirst("1");
				if(!StringUtils.equals(onJob, "Y")) {
					xauthUsers.setEnabled("0");
				} else {
					xauthUsers.setEnabled("1");
				}
				xauthUsers.setCreUser("SYSTEM"); 
				xauthUsers.setCreDate(new Date());
				xauthUsers.setUpdUser("SYSTEM"); 
				xauthUsers.setUpdDate(new Date());
				xauthUsers.setOnJob(onJob);
				// -------------------------------------------------
				// 資料準備 END
				// -------------------------------------------------
				
				result.add(xauthUsers);
				i++;
				
				BigDecimal tmp = new BigDecimal(i);
				if(((tmp.divideAndRemainder(new BigDecimal(showLogPer)))[1]).intValue() == 0) {
					logger.info(label + "解析筆數：" + i);
				}
			}
		} catch(Exception e) {
			logger.error(label + "ERROR：" + e.getMessage());
			throw new Exception(label + "ERROR：" + e.getMessage());
		} finally {
            endTime = System.currentTimeMillis() ;
			spendTime = (double)(endTime - startTime) / 1000 ;
			
			if(lineIterator != null) {
				lineIterator.close();
			}
			logger.debug(label + "END");
		}
		
		logger.info(label + "-------------------------------------------------");
		logger.info(label + "    匯入人員 CSV 統計結果");
		logger.info(label + "-------------------------------------------------");
		logger.info(label + "花費時間：" + spendTime + " 秒");
		logger.info(label + "總筆數：" + result.size());
		
		for(String tmp : maxArrayCount) {
			logger.info(label + "最大陣列長度：" + tmp);
		}
		return result;
	}
	
	/**
	 * 過濾檔案多餘byte
	 * @param val
	 * @return
	 * @throws Exception
	 */
	private static String filter(String val) throws Exception {
		byte[] bytes = {-17, -69, -65}; // 過濾檔案多餘byte
		return val.replaceAll(new String(bytes), "");
	}
	
	/**
	 * 依情況刪除或更新XAUTH_ROLE_AGENT_USER資料
	 * @param userId
	 * @param beforeIdenId
	 * @param afterIdenId
	 * @param beforeRoleId
	 * @param afterRoleId
	 */
	private void delAgentUser(String userId) {
		QueryWrapper<XauthRoleAgentUser> roleAgentUserWrapper = new QueryWrapper<XauthRoleAgentUser>();
		roleAgentUserWrapper.eq("APP_ID", appId);
		roleAgentUserWrapper.and(wrapper -> wrapper.eq("USER_ID", userId).or().eq("AGENT_USER_ID", userId));
		xauthRoleAgentUserMapper.delete(roleAgentUserWrapper);
	}
	
	/**
	 * 更新XAUTH_ROLE_AGENT_USER的ROLE_ID
	 * @param userId
	 * @param idenId
	 * @param beforeRoleId
	 * @param afterRoleId
	 */
	private void updAgentUser(String userId, String idenId, String afterRoleId) {
		List<XauthRoleAgentUser> roleAgentUserList = null;
		XauthRoleAgentUser roleAgentUser = null;
		QueryWrapper<XauthRoleAgentUser> roleAgentUserWrapper = new QueryWrapper<XauthRoleAgentUser>();
		
		roleAgentUserWrapper.eq("APP_ID", appId);
		roleAgentUserWrapper.eq("USER_ID", userId);
		roleAgentUserWrapper.eq("IDEN_ID", idenId);
		roleAgentUserList = xauthRoleAgentUserMapper.selectList(roleAgentUserWrapper);
		if(roleAgentUserList != null && roleAgentUserList.size() > 0) {
			roleAgentUser = roleAgentUserList.get(0);
			roleAgentUser.setRoleId(afterRoleId);
			roleAgentUser.setUpdDate(new Date());
			roleAgentUser.setUpdUser("SYSTEM");
			xauthRoleAgentUserMapper.update(roleAgentUser, roleAgentUserWrapper);
		}
	}
	
	/**
	 * 更新XauthUsers enabled欄位
	 * @throws Exception
	 */
	public void updXauthUsers(List<String> errorMessageList) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		QueryWrapper<XauthUsers> usersWrapper = new QueryWrapper<XauthUsers>();
		usersWrapper.eq("APP_ID", appId);
		usersWrapper.eq("ENABLED", "1");
		usersWrapper.ne("TRUNC(UPD_DATE)", sdf.parse(sdf.format(new Date())));
		usersWrapper.ne("IDEN_ID", "999999999");
		usersWrapper.ne("IDEN_ID", "00000000");
		List<XauthUsers> usersList = xauthUsersMapper.selectList(usersWrapper);
		
		int i = 0;
		for(XauthUsers user : usersList) {
			try {
				usersWrapper = new QueryWrapper<XauthUsers>();
				usersWrapper.eq("IDEN_ID", user.getIdenId());
				usersWrapper.eq("USER_ID", user.getUserId());
				user.setEnabled("0");
				user.setUpdDate(new Date());
				user.setUpdUser("SYSTEM");
				xauthUsersMapper.update(user, usersWrapper);
				logger.info("已更新離職人員資料：(IDEN_ID，USER_ID) : (" + user.getIdenId() + "," + user.getUserId() + ")");
				i++;
			} catch(Exception e) {
				logger.info("更新XauthUsers enabled欄位發生錯誤 (IDEN_ID，USER_ID)：(" + user.getIdenId() + "," + user.getUserId() + ")，原因為 : " + e.getMessage());
				errorMessageList.add("更新XauthUsers enabled欄位發生錯誤 (IDEN_ID，USER_ID)：(" + user.getIdenId() + "," + user.getUserId() + ")，原因為 : " + e.getMessage());
//				throw new Exception(e);
			}
		}
		logger.info("更新離職人員資料共" + i + "筆");
	}
}
