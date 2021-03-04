package com.tradevan.mapper.xauth.dao;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tradevan.mapper.xauth.model.XauthDept;
import com.tradevan.mapper.xauth.model.XauthIdenMenu;
import com.tradevan.mapper.xauth.model.XauthIpGrant;
import com.tradevan.mapper.xauth.model.XauthRoleMenu;
import com.tradevan.mapper.xauth.model.XauthRoleUser;
import com.tradevan.mapper.xauth.model.XauthUsers;
import com.tradevan.mapper.xauth.model.XauthUsersToken;

public interface XauthMapper extends BaseMapper<Object> {
	
	List<Map<String, Object>> searchApplication(Map<String, Object> params);
	
	List<Map<String, Object>> selectMenu(Map<String, Object> params);
	
	List<Map<String, Object>> selectMenuTree(String appId);
	
	List<Map<String, Object>> selectMenuTreeDescByUrl(Map<String, Object> params);

	List<Map<String, Object>> initMenuByUser(Map<String, Object> params);
	
	List<Map<String, Object>> initUserRole(Map<String, Object> params);
	
	List<XauthRoleUser> selectRoleUser(XauthRoleUser xauthRoleUser);
	
	List<Map<String, Object>> selectMenuMain(Map<String, Object> params);
	
	List<Map<String, Object>> searchMenuMain(Map<String, Object> params);
	
	int insertMenu(Map<String, Object> params);
	
	int updateMenu(Map<String, Object> params);
	
	List<Map<String, Object>> selectUser(Map<String, Object> params);
	
	List<Map<String, Object>> searchUser(Map<String, Object> params);
	
	List<Map<String, Object>> selectUserRole(XauthUsers xauthUsers);
	
	List<Map<String, Object>> searchUserRole(Map<String, Object> params);
	
	List<Map<String, Object>> selectUserRoleAdmin(XauthUsers xauthUsers);
	
	List<Map<String, Object>> selectDeptTree(Map<String, Object> params);
	
	List<Map<String, Object>> selectDeptTreeRoot(XauthDept xauthDept);
	
	List<Map<String, Object>> selectDeptTreeDesc(XauthDept xauthDept);
	
	List<Map<String, Object>> selectDept(Map<String, Object> params);
	
	List<Map<String, Object>> searchDept(Map<String, Object> params);
	
	List<Map<String, Object>> getuserlist(Map<String, Object> params);

	
	int insertDept(XauthDept xauthDept);
	
	int updateDept(Map<String, Object> params);
	
	int updateDeptParent(Map<String, Object> params);
	
	int updateDeptParentSeq(Map<String, Object> params);
	
	List<Map<String, Object>> selectRole(Map<String, Object> params);
	
	List<Map<String, Object>> searchRole(Map<String, Object> params);
	
	List<Map<String, Object>> selectIdenMenuTree(XauthIdenMenu xauthIdenMenu);
	
	List<Map<String, Object>> selectIdenTree(XauthIdenMenu xauthIdenMenu);
	
	List<Map<String, Object>> selectRoleMenuTree(XauthRoleMenu xauthRoleMenu);
	
	List<XauthIpGrant> selectIpGrantValid(XauthIpGrant xauthIpGrant);
	
	List<Map<String, Object>> selectIpGrant(Map<String, Object> params);
	
	List<Map<String, Object>> searchIpGrant(Map<String, Object> params);
	
	List<Map<String, Object>> searchSysCode(Map<String, Object> params);
	
	List<Map<String, Object>> selectSysCode(Map<String, Object> params);
	
	List<XauthUsersToken> selectUersToken(XauthUsersToken xauthUsersToken);
	
	List<Map<String, Object>> searchRoleAgentUser(Map<String, Object> params);
	
	List<Map<String, Object>> searchSystemparam(Map<String, Object> params);
	
	List<Map<String, Object>> selectChildDept(Map<String, Object> params);
	
	List<Map<String, Object>> selectBatch(Map<String, Object> params);
	
}
