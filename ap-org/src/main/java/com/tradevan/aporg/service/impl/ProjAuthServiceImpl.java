package com.tradevan.aporg.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.tradevan.apcommon.bean.CommonMsgCode;
import com.tradevan.apcommon.bean.CreateUserDto;
import com.tradevan.apcommon.bean.PageResult;
import com.tradevan.apcommon.bean.UpdateUserDto;
import com.tradevan.apcommon.exception.ApException;
import com.tradevan.apcommon.service.I18nMsgService;
import com.tradevan.aporg.bean.AuthDto;
import com.tradevan.aporg.bean.AuthProfQuery;
import com.tradevan.aporg.bean.ProjAuthQuery;
import com.tradevan.aporg.bean.ProjDto;
import com.tradevan.aporg.bean.RoleDto;
import com.tradevan.aporg.model.AuthProf;
import com.tradevan.aporg.model.ProjProf;
import com.tradevan.aporg.model.RoleProf;
import com.tradevan.aporg.model.UserProjAuth;
import com.tradevan.aporg.model.UserProjRole;
import com.tradevan.aporg.repository.AuthProfRepository;
import com.tradevan.aporg.repository.ProjProfRepository;
import com.tradevan.aporg.repository.RoleProfRepository;
import com.tradevan.aporg.repository.UserProjAuthRepository;
import com.tradevan.aporg.repository.UserProjRoleRepository;
import com.tradevan.aporg.service.ProjAuthService;

/**
 * Title: ProjAuthServiceImpl<br>
 * Description: <br>
 * Company: Tradevan Co.<br>
 * 
 * @author Neil Chen
 * @version 1.1
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class ProjAuthServiceImpl implements ProjAuthService {

	@Autowired
	private ProjProfRepository projRepository;
	
	@Autowired
	private UserProjRoleRepository userProjRoleRepository;
	
	@Autowired
	private RoleProfRepository roleRepository;
	
	@Autowired
	private AuthProfRepository authRepository;
	
	@Autowired
	private UserProjAuthRepository userProjAuthRepository;
	
	@Autowired
	private I18nMsgService i18nMsgService;
	
	@Override
	public String genProjId(String prefix, String dateFmt, int serialLen) {
		String prefixVal = (prefix != null) ? prefix : "";
		String dateFmtVal = (dateFmt != null) ? (new SimpleDateFormat(dateFmt).format(new Date())) : "";
		
		String maxSerial = projRepository.getMaxProjIdSerial(prefixVal, dateFmtVal, serialLen);
		
		String serial = String.valueOf((maxSerial != null) ? Integer.parseInt(maxSerial) + 1 : 1);
		
		if (serial.length() > serialLen) {
			new IllegalStateException("Serial overflows. (projId:" + prefixVal + dateFmtVal + serial + ")");
		}
		
		StringBuilder buf = new StringBuilder();
		int len = serialLen - serial.length();
		for (int x = 0; x < len; x++) {
			buf.append("0");
		}
		buf.append(serial);
		
		return prefixVal + dateFmtVal + buf.toString();
	}
	
	@Override
	public String genRschProjId(String year, String organCode, String projType, String hostType, int serialLen) {
		String maxSerial = projRepository.getMaxRschProjIdSerial(year, organCode, projType, hostType, serialLen);
		
		String serial = String.valueOf((maxSerial != null) ? Integer.parseInt(maxSerial) + 1 : 1);
		
		if (serial.length() > serialLen) {
			new IllegalStateException("Serial overflows. (projId:" + year + organCode + projType + hostType + serial + ")");
		}
		
		StringBuilder buf = new StringBuilder();
		int len = serialLen - serial.length();
		for (int x = 0; x < len; x++) {
			buf.append("0");
		}
		buf.append(serial);
		
		return year + organCode + projType + hostType + buf.toString();
	}
	
	@Override
	public String genEduCourseId(String prefix, String dateFmt, int serialLen) {
		String prefixVal = (prefix != null) ? prefix : "";
		String dateFmtVal = (dateFmt != null) ? (new SimpleDateFormat(dateFmt).format(new Date())) : "";
		
		String maxSerial = projRepository.getMaxEduCourseIdSerial(prefixVal, dateFmtVal, serialLen);
		
		String serial = String.valueOf((maxSerial != null) ? Integer.parseInt(maxSerial) + 1 : 1);
		
		if (serial.length() > serialLen) {
			new IllegalStateException("Serial overflows. (courseId:" + prefixVal + dateFmtVal + serial + ")");
		}
		
		StringBuilder buf = new StringBuilder();
		int len = serialLen - serial.length();
		for (int x = 0; x < len; x++) {
			buf.append("0");
		}
		buf.append(serial);
		
		return prefixVal + dateFmtVal + buf.toString();
	}
	
	@Override
	public String genEduClassId(String courseId) {
		String maxSerial = projRepository.getMaxEduClassIdSerial(courseId);
		
		String serial = String.valueOf((maxSerial != null) ? Character.toString((char) ((int)maxSerial.charAt(0) + 1)) : "A");
		
		return courseId + "-" + serial;
	}
	
	@Override
	public String genCoprApplyNo(String prefix, String dateFmt, int serialLen, String sequenceName) {
		String prefixVal = (prefix != null) ? prefix : "";
		String dateFmtVal = (dateFmt != null) ? (new SimpleDateFormat(dateFmt).format(new Date())) : "";
		
		String serial = projRepository.getMaxCoprApplyNoSerial(sequenceName);
		
		if (serial.length() > serialLen) {
			new IllegalStateException("Serial overflows. (applyNo:" + dateFmtVal + prefixVal + serial + ")");
		}
		
		StringBuilder buf = new StringBuilder();
		int len = serialLen - serial.length();
		for (int x = 0; x < len; x++) {
			buf.append("0");
		}
		buf.append(serial);
		
		return dateFmtVal + prefixVal + buf.toString();
	}
	
	@Override
	public String genBulletinNo(String dateFmt, int serialLen) {
		String dateFmtVal = (dateFmt != null) ? (new SimpleDateFormat(dateFmt).format(new Date())) : "";
		
		String maxSerial = projRepository.getMaxBulletinNo(dateFmtVal, serialLen);
		
		String serial = String.valueOf((maxSerial != null) ? Integer.parseInt(maxSerial) + 1 : 1);
		
		if (serial.length() > serialLen) {
			new IllegalStateException("Serial overflows. (bulletinNo:" + dateFmtVal + serial + ")");
		}
		
		StringBuilder buf = new StringBuilder();
		int len = serialLen - serial.length();
		for (int x = 0; x < len; x++) {
			buf.append("0");
		}
		buf.append(serial);
		
		return dateFmtVal + buf.toString();
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void addProj(ProjDto projDto, CreateUserDto createUserDto) {
		ProjProf proj = new ProjProf(projDto, createUserDto);
		linkRelations(projDto, proj);
		projRepository.save(proj);
	}
	
	private void linkRelations(ProjDto projDto, ProjProf proj) {
		List<String> roleIds = projDto.getRoleIds();
		if (roleIds != null && roleIds.size() > 0) {
			//proj.setRoles(new HashSet<RoleProf>(roleRepository.findEntityListInValueList("roleId", projDto.getRoleIds(), null, null, null)));
		}
		else {
			//proj.setRoles(null);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void updProj(ProjDto projDto, UpdateUserDto updateUserDto) {
		ProjProf proj = projRepository.getEntityById(projDto.getId());
		BeanUtils.copyProperties(projDto, proj, "id", "createUserId", "createTime");
		BeanUtils.copyProperties(updateUserDto, proj);
		linkRelations(projDto, proj);
		projRepository.save(proj);
	}

	@Override
	public ProjDto getProjById(Long id) {
		ProjProf proj = projRepository.getEntityById(id);
		if (proj != null) {
			ProjDto projDto = new ProjDto(proj);
			return projDto;
		}
		return null;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean delProjById(Long id) {
		return projRepository.delete(id);
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public ProjProf saveProj(ProjProf proj) {
		return projRepository.save(proj);
	}
	
	@Override
	public ProjProf fetchProjByProjId(String projId) {
		return projRepository.getEntityByProperty("projId", projId);
	}

	@Override
	public Map<String, List<String>> findUserProjRoles(String userId, String sysId) {
		Map<String, List<String>> map = new HashMap<String, List<String>>();
		List<String> list = null;
		String lastProjId = "";
		for (UserProjRole userProjRole : userProjRoleRepository.findEntityListByProperty("userId", userId, "projId")) {
			String projId = userProjRole.getProjId();
			if (StringUtils.isNotBlank(sysId)) {
				ProjProf proj = fetchProjByProjId(projId);
				if (proj != null && StringUtils.isNotBlank(proj.getSysId()) && !sysId.equalsIgnoreCase(proj.getSysId())) {
					continue;
				}
			}
			
			if (list == null || !lastProjId.equals(projId)) {
				list = new ArrayList<String>();
				map.put(projId, list);
			}
			list.add(userProjRole.getRoleId());
			lastProjId = projId;
		}
		return map;
	}

	@Override
	public Map<String, List<String>> findProjRoleUsers(String projId) {
		Map<String, List<String>> map = new HashMap<String, List<String>>();
		List<String> list = null;
		String lastRoleId = "";
		for (UserProjRole userProjRole : userProjRoleRepository.findEntityListByProperty("projId", projId, "roleId")) {
			String roleId = userProjRole.getRoleId();
			if (list == null || !lastRoleId.equals(roleId)) {
				list = new ArrayList<String>();
				map.put(roleId, list);
			}
			list.add(userProjRole.getUserId());
			lastRoleId = roleId;
		}
		return map;
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void addAuth(AuthDto authDto, CreateUserDto createUserDto) {
		AuthProf auth = new AuthProf(authDto, createUserDto);
		authRepository.save(auth);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void updAuth(AuthDto authDto, UpdateUserDto updateUserDto) {
		AuthProf auth = authRepository.getEntityById(authDto.getId());
		BeanUtils.copyProperties(authDto, auth, "id", "createUserId", "createTime");
		BeanUtils.copyProperties(updateUserDto, auth);
		authRepository.save(auth);
	}

	@Override
	public AuthDto getAuthById(Long id) {
		AuthProf auth = authRepository.getEntityById(id);
		if (auth != null) {
			AuthDto authDto = new AuthDto(auth);
			return authDto;
		}
		return null;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean delAuthById(Long id) {
		return authRepository.delete(id);
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public AuthProf saveAuth(AuthProf auth) {
		return authRepository.save(auth);
	}
	
	@Override
	public AuthProf fetchAuthByAuthId(String authId) {
		return authRepository.getEntityByProperty("authId", authId);
	}

	@Override
	public Map<String, List<String>> findUserProjAuths(String userId, String sysId) {
		Map<String, List<String>> map = new HashMap<String, List<String>>();
		List<String> list = null;
		String lastProjId = "";
		for (UserProjAuth userProjAuth : userProjAuthRepository.findEntityListByProperty("userId", userId, "projId")) {
			String projId = userProjAuth.getProjId();
			if (StringUtils.isNotBlank(sysId)) {
				ProjProf proj = fetchProjByProjId(projId);
				if (proj != null && StringUtils.isNotBlank(proj.getSysId()) && !sysId.equalsIgnoreCase(proj.getSysId())) {
					continue;
				}
			}
			
			if (list == null || !lastProjId.equals(projId)) {
				list = new ArrayList<String>();
				map.put(projId, list);
			}
			list.add(userProjAuth.getAuthId());
			lastProjId = projId;
		}
		return map;
	}

	@Override
	public Map<String, List<String>> findProjAuthUsers(String projId) {
		Map<String, List<String>> map = new HashMap<String, List<String>>();
		List<String> list = null;
		String lastAuthId = "";
		for (UserProjAuth userProjAuth : userProjAuthRepository.findEntityListByProperty("projId", projId, "authId")) {
			String authId = userProjAuth.getAuthId();
			if (list == null || !lastAuthId.equals(authId)) {
				list = new ArrayList<String>();
				map.put(authId, list);
			}
			list.add(userProjAuth.getUserId());
			lastAuthId = authId;
		}
		return map;
	}
	
	/**
	 * 2018/03/26 Added by Sephiro : 專案計劃權限管理 - 查詢grid資料
	 */
	@Override
	public PageResult fetchProjs(ProjAuthQuery query) {
		Map<String, Object> params = new HashMap<String, Object>();
		//系統別
		if(StringUtils.isNotBlank(query.getSysId()) && !StringUtils.equals(query.getSysId(), "ALL")) {
			params.put("sysId", query.getSysId()); //系統別
		}
		//計劃代號
		if(StringUtils.isNotBlank(query.getProjId())) {
			params.put("projId", query.getProjId());
		}
		//計劃名稱
		if(StringUtils.isNotBlank(query.getName())) {
			params.put("name", "%" + query.getName() +"%");
		}
		return projRepository.findProjs(params, query.getPage(), query.getPageSize());
	}

	@Override
	public PageResult fetchAuthProfs(AuthProfQuery query) {
		Map<String, Object> params = new HashMap<String, Object>();
		//系統別
		if(StringUtils.isNotBlank(query.getSysId()) && !StringUtils.equals(query.getSysId(), "ALL")) {
			params.put("sysId", query.getSysId()); 
		}
		//權限別代號
		if(StringUtils.isNotBlank(query.getAuthId())) {
			params.put("authId", query.getAuthId());
		}
		//權限別名稱
		if(StringUtils.isNotBlank(query.getName())) {
			params.put("name", "%" + query.getName() +"%");
		}
		return authRepository.findAuthProfs(params, query.getPage(), query.getPageSize());
	}

	@Override
	public List<Map<String, Object>> listProjAuth(String projId) {
		return projRepository.listProjAuth(projId);
	}

	@Override
	public List<Map<String, Object>> fetchAuthsNotInProjAuth(String projId) {
		return authRepository.findAuthsNotInProjAuth(projId);
	}

	@Override
	public List<Map<String, Object>> fetchAuthsInProjAuth(String projId) {
		return authRepository.findAuthsInProjAuth(projId);
	}

	/**
	 * 2018/03/27 Added by Sephiro : 回寫權限別給專案計劃
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void addOrDelProjAuths(String projId, String[] asAry) {
		ProjProf projProf = projRepository.getEntityByProperty("projId", projId);
		//ori list
		List<String> oriAuthProfList = new ArrayList<String>();
		List<Map<String, Object>> oriList = fetchAuthsInProjAuth(projId);
        for (int x = 0; x < oriList.size(); x++){
        	Map<String, Object> oriMap = (Map<String, Object>) oriList.get(x);
        	oriAuthProfList.add((String)oriMap.get("value"));
        }
        //assign list
        List<String> assignAuthProfList = new ArrayList<String>();
        if(asAry!= null && asAry.length > 0) {
        	for(String assignAuthId : asAry) {
        		assignAuthProfList.add(assignAuthId);
        	}
        }
        //compare two list 
        if((oriAuthProfList.size() == 0 || oriAuthProfList == null) 
        		&& (assignAuthProfList.size() == 0 || assignAuthProfList == null)) { 
        	//沒有任何權限別要被新增此專案計劃====>不處理
        	return;
        }else if((oriAuthProfList.size() == 0 || oriAuthProfList == null) && assignAuthProfList.size() > 0) {
        	for(String assignAuthId2 : assignAuthProfList){
        		addProjAuth(assignAuthId2, projProf);
        	}        	
        }else if(oriAuthProfList.size() > 0 && (assignAuthProfList.size() == 0 || assignAuthProfList == null)) {
        	for(String oriAuthId : oriAuthProfList){
        		delProjAuth(oriAuthId, projProf);
        	}    
        }else {
        	//1.assign list有，但ori list沒有====>要把角色加到這帳號裡
        	for(String assignAuthId2 : assignAuthProfList){
        		if(!oriAuthProfList.contains(assignAuthId2)){
        			addProjAuth(assignAuthId2, projProf);
        		}
        	}
        	//2.ori list有，但assign list沒有====>要把角色從這帳號移除
        	for(String oriAuthId : oriAuthProfList){
        		if(!assignAuthProfList.contains(oriAuthId)){
        			delProjAuth(oriAuthId, projProf);
        		}
        	}     
        }
        projRepository.update(projProf);
	}
	
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	private void addProjAuth(String assignAuthId2, ProjProf projProf) {
		AuthProf authProf = authRepository.getEntityByProperty("authId", assignAuthId2);
		Set<AuthProf> authProfSet = projProf.getAuths();
		authProfSet.add(authProf);
		projProf.setAuths(authProfSet);
	}
	
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	private void delProjAuth(String oriAuthId, ProjProf projProf) {
		Set<AuthProf> authProfSet = projProf.getAuths();
		for (Iterator<AuthProf> it = authProfSet.iterator(); it.hasNext();) {
			AuthProf ap = it.next();
		    if (StringUtils.equals(ap.getAuthId(), oriAuthId)) {
		        it.remove();
		    }
		}    		
		//此計劃的權限被刪除時，一併刪除已被指定此權限的使用者帳號
		List<UserProjAuth> upaList = userProjAuthRepository.findEntityListByProperties(new String[] {"projId", "authId"}, new String[] {projProf.getProjId(), oriAuthId}, null, null, null);
		if(upaList.size() > 0 && null != upaList) {
			for(UserProjAuth upa : upaList) {
				userProjAuthRepository.delete(upa);
			}
		}
		projProf.setAuths(authProfSet);
	}

	@Override
	public List<Map<String, Object>> fetchUsersNotInUserProjAuth(String projId, String authId) {
		return userProjAuthRepository.findUsersNotInUserProjAuth(projId, authId);
	}

	@Override
	public List<Map<String, Object>> fetchUsersInUserProjAuth(String projId, String authId) {
		return userProjAuthRepository.findUsersInUserProjAuth(projId, authId);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void addOrDelUserProjAuths(String projId, String authId, String[] asAry, CreateUserDto createUserDto) {
		//ori list
		List<String> oriUserList = new ArrayList<String>();
		List<Map<String, Object>> oriList = fetchUsersInUserProjAuth(projId, authId);
        for (int x = 0; x < oriList.size(); x++){
        	Map<String, Object> oriMap = (Map<String, Object>) oriList.get(x);
        	oriUserList.add((String)oriMap.get("value"));
        }
        //assign list
        List<String> assignedUserList = new ArrayList<String>();
        if(asAry!= null && asAry.length > 0) {
        	for(String assignedUserId : asAry) {
        		assignedUserList.add(assignedUserId);
        	}
        }
        //compare two list 
        if((oriUserList.size() == 0 || oriUserList == null) 
        		&& (assignedUserList.size() == 0 || assignedUserList == null)) { 
        	//沒有任何權限別要被新增此使用者角色====>不處理
        	return;
        }else if((oriUserList.size() == 0 || oriUserList == null) && assignedUserList.size() > 0) {
        	for(String assignedUserId2 : assignedUserList){
        		addUserProjAuth(assignedUserId2, projId, authId, createUserDto);
        	}        	
        }else if(oriUserList.size() > 0 && (assignedUserList.size() == 0 || assignedUserList == null)) {
        	for(String oriUserId : oriUserList){
        		delUserProjAuth(oriUserId, projId, authId);
        	}    
        }else {
        	//1.assign list有，但ori list沒有====>要把角色加到這帳號裡
        	for(String assignedUserId2 : assignedUserList){
        		if(!oriUserList.contains(assignedUserId2)){
        			addUserProjAuth(assignedUserId2, projId, authId, createUserDto);
        		}
        	}
        	//2.ori list有，但assign list沒有====>要把角色從這帳號移除
        	for(String oriUserId : oriUserList){
        		if(!assignedUserList.contains(oriUserId)){
        			delUserProjAuth(oriUserId, projId, authId);
        		}
        	}     
        }
	}

	private void addUserProjAuth(String assignedUserId2, String projId, String authId, CreateUserDto createUserDto) {
		UserProjAuth upa = new UserProjAuth(assignedUserId2, projId, authId, createUserDto);
		userProjAuthRepository.save(upa);
	}
	
	private void delUserProjAuth(String oriUserId, String projId, String authId) {
		userProjAuthRepository.deleteByProperties(new String[] {"userId", "projId", "authId"}, new String[] {oriUserId, projId, authId});
	}

	@Override
	public List<Map<String, Object>> listProjRole(String projId) {
		return projRepository.listProjRole(projId);
	}

	@Override
	public List<Map<String, Object>> fetchRolesNotInProjRole(String sysId, String projId) {
		return roleRepository.findRolesNotInProjRole(sysId, projId);
	}

	@Override
	public List<Map<String, Object>> fetchRolesInProjRole(String sysId, String projId) {
		return roleRepository.findRolesInProjRole(sysId, projId);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void addOrDelProjRoles(String sysId, String projId, String[] asAry) {
		//找出proj
		ProjProf proj = projRepository.getEntityByProperty("projId", projId);
		//ori list
		List<String> oriRoleList = new ArrayList<String>();
		List<Map<String, Object>> oriList = fetchRolesInProjRole(sysId, projId);
        for (int x = 0; x < oriList.size(); x++){
        	Map<String, Object> oriMap = (Map<String, Object>) oriList.get(x);
        	oriRoleList.add((String)oriMap.get("value"));
        }
        //assign list
        List<String> assignedRoleList = new ArrayList<String>();
        if(asAry!= null && asAry.length > 0) {
        	for(String assignedRoleId : asAry) {
        		assignedRoleList.add(assignedRoleId);
        	}
        }
        //compare two list 
        if((oriRoleList.size() == 0 || oriRoleList == null) 
        		&& (assignedRoleList.size() == 0 || assignedRoleList == null)) { 
        	//沒有任何角色要被新增到這計劃====>不處理
        	return;
        }else if((oriRoleList.size() == 0 || oriRoleList == null) && assignedRoleList.size() > 0) {
        	for(String assignedRoleId2 : assignedRoleList){
        		addProjRole(assignedRoleId2, proj);
        	}        	
        }else if(oriRoleList.size() > 0 && (assignedRoleList.size() == 0 || assignedRoleList == null)) {
        	for(String oriRoleId : oriRoleList){
        		delProjRole(oriRoleId, proj);
        	}    
        }else {
        	//1.assign list有，但ori list沒有====>要把角色加到這帳號裡
        	for(String assignedRoleId2 : assignedRoleList){
        		if(!oriRoleList.contains(assignedRoleId2)){
        			addProjRole(assignedRoleId2, proj);
        		}
        	}
        	//2.ori list有，但assign list沒有====>要把角色從這帳號移除
        	for(String oriRoleId : oriRoleList){
        		if(!assignedRoleList.contains(oriRoleId)){
        			delProjRole(oriRoleId, proj);
        		}
        	}     
        }		
        projRepository.update(proj);
	}

	private void addProjRole(String assignedRoleId2, ProjProf proj) {
		//1. 拉出roleSet
		//Set<RoleProf> roleSet = proj.getRoles();
		//2. 從roleId -> roleProf
		//RoleProf role = roleRepository.getEntityByProperty("roleId", assignedRoleId2);
		//3. add roleProf to roleSet
		//roleSet.add(role);
		//4. set roleSet to proj
		//proj.setRoles(roleSet);
	}
	
	private void delProjRole(String oriRoleId, ProjProf proj) {
		//1. 拉出roleSet
		//Set<RoleProf> roleSet = proj.getRoles();
		//2. for loop roleSet, if roleSet.roleId = oriRoleId -> delete
		//for (Iterator<RoleProf> it = roleSet.iterator(); it.hasNext();) {
		//	RoleProf role = it.next();
		//    if (StringUtils.equals(role.getRoleId(), oriRoleId)) {
		//        it.remove();
		//    }
		//}  		
		//3. set roleSet to proj
		//proj.setRoles(roleSet);
		//4. 以roleId+projId找出List<userProjRole>, delete List
		List<UserProjRole> uprList = userProjRoleRepository.findEntityListByProperties(new String[] {"projId", "roleId"}, new String[] {proj.getProjId(), oriRoleId}, null, null, null);
		if(uprList.size() > 0 && null != uprList) {
			for(UserProjRole upr : uprList) {
				userProjRoleRepository.delete(upr);
			}
		}
	}

	@Override
	public List<Map<String, Object>> fetchUsersNotInUserProjRole(String projId, String roleId) {
		return userProjRoleRepository.findUsersNotInUserProjRole(projId, roleId);
	}

	@Override
	public List<Map<String, Object>> fetchUsersInUserProjRole(String projId, String roleId) {
		return userProjRoleRepository.findUsersInUserProjRole(projId, roleId);
	}

	@Override
	public RoleDto getRoleById(Long id) {
		RoleProf role = roleRepository.getEntityById(id);
		if(role != null) {
			RoleDto roleDto = new RoleDto(role);
			return roleDto;
		}
		return null;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void addOrDelUserProjRoles(String projId, String roleId, String[] asAry, CreateUserDto createUserDto) {
		//ori list
		List<String> oriUserList = new ArrayList<String>();
		List<Map<String, Object>> oriList = fetchUsersInUserProjRole(projId, roleId);
        for (int x = 0; x < oriList.size(); x++){
        	Map<String, Object> oriMap = (Map<String, Object>) oriList.get(x);
        	oriUserList.add((String)oriMap.get("value"));
        }
        //assign list
        List<String> assignedUserList = new ArrayList<String>();
        if(asAry!= null && asAry.length > 0) {
        	for(String assignedUserId : asAry) {
        		assignedUserList.add(assignedUserId);
        	}
        }
        //compare two list 
        if((oriUserList.size() == 0 || oriUserList == null) 
        		&& (assignedUserList.size() == 0 || assignedUserList == null)) { 
        	//沒有任何角色要被新增此使用者角色====>不處理
        	return;
        }else if((oriUserList.size() == 0 || oriUserList == null) && assignedUserList.size() > 0) {
        	for(String assignedUserId2 : assignedUserList){
        		addUserProjRole(assignedUserId2, projId, roleId, createUserDto);
        	}        	
        }else if(oriUserList.size() > 0 && (assignedUserList.size() == 0 || assignedUserList == null)) {
        	for(String oriUserId : oriUserList){
        		delUserProjRole(oriUserId, projId, roleId);
        	}    
        }else {
        	//1.assign list有，但ori list沒有====>要把角色加到這帳號裡
        	for(String assignedUserId2 : assignedUserList){
        		if(!oriUserList.contains(assignedUserId2)){
        			addUserProjRole(assignedUserId2, projId, roleId, createUserDto);
        		}
        	}
        	//2.ori list有，但assign list沒有====>要把角色從這帳號移除
        	for(String oriUserId : oriUserList){
        		if(!assignedUserList.contains(oriUserId)){
        			delUserProjRole(oriUserId, projId, roleId);
        		}
        	}     
        }		
	}
	
	private void addUserProjRole(String assignedUserId2, String projId, String roleId, CreateUserDto createUserDto) {
		UserProjRole upr = new UserProjRole(assignedUserId2, projId, roleId, createUserDto);
		userProjRoleRepository.save(upr);
	}
	
	private void delUserProjRole(String oriUserId, String projId, String roleId) {
		userProjRoleRepository.deleteByProperties(new String[] {"userId", "projId", "roleId"}, new String[] {oriUserId, projId, roleId});
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean deleteProjById(Long id) throws ApException {
		ProjProf proj = projRepository.getEntityById(id);
		if (proj != null && isProjInUse(proj)) {
			throw new ApException(CommonMsgCode.CODE_E_CANNOT_BE_DELETED, i18nMsgService.getText(CommonMsgCode.MSG_E_CANNOT_BE_DELETED));
		}
		else {
			return projRepository.delete(id);
		}
	}
	
	private boolean isProjInUse(ProjProf proj) {
		int countUpr = userProjRoleRepository.findEntityListByProperty("projId", proj.getProjId(), null).size();
		int countUpa = userProjAuthRepository.findEntityListByProperty("projId", proj.getProjId(), null).size();
		return ((countUpr+countUpa)>0) ? true : false;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean deleteAuthById(Long id) throws ApException {
		AuthProf auth = authRepository.getEntityById(id);
		if (auth != null && isAuthInUse(auth)) {
			throw new ApException(CommonMsgCode.CODE_E_CANNOT_BE_DELETED, i18nMsgService.getText(CommonMsgCode.MSG_E_CANNOT_BE_DELETED));
		}
		else {
			return authRepository.delete(id);
		}
	}

	private boolean isAuthInUse(AuthProf auth) {
		int countUpa = userProjAuthRepository.findEntityListByProperty("authId", auth.getAuthId(), null).size();
		return countUpa>0 ? true : false;
	}
}
