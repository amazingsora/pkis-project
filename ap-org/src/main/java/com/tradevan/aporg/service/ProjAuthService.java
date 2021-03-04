package com.tradevan.aporg.service;

import java.util.List;
import java.util.Map;

import com.tradevan.apcommon.bean.CreateUserDto;
import com.tradevan.apcommon.bean.PageResult;
import com.tradevan.apcommon.bean.UpdateUserDto;
import com.tradevan.apcommon.exception.ApException;
import com.tradevan.aporg.bean.AuthDto;
import com.tradevan.aporg.bean.AuthProfQuery;
import com.tradevan.aporg.bean.ProjAuthQuery;
import com.tradevan.aporg.bean.ProjDto;
import com.tradevan.aporg.bean.RoleDto;
import com.tradevan.aporg.model.AuthProf;
import com.tradevan.aporg.model.ProjProf;

/**
 * Title: ProjAuthService<br>
 * Description: <br>
 * Company: Tradevan Co.<br>
 * 
 * @author Neil Chen
 * @version 1.1
 */
public interface ProjAuthService {

	String genProjId(String prefix, String dateFmt, int serialLen);
	
	String genRschProjId(String year, String organCode, String projType, String hostType, int serialLen);
	
	String genEduCourseId(String prefix, String dateFmt, int serialLen);
	
	String genEduClassId(String courseId);
	
	String genCoprApplyNo(String prefix, String dateFmt, int serialLen, String sequenceName);
	
	String genBulletinNo(String dateFmt, int serialLen);
	
	void addProj(ProjDto projDto, CreateUserDto createUserDto);
	
	void updProj(ProjDto projDto, UpdateUserDto updateUserDto);
	
	ProjDto getProjById(Long id);
	
	boolean delProjById(Long id);
	
	ProjProf saveProj(ProjProf proj);
	
	ProjProf fetchProjByProjId(String projId);

	Map<String, List<String>> findUserProjRoles(String userId, String sysId);
	
	Map<String, List<String>> findProjRoleUsers(String projId);
	
	void addAuth(AuthDto authDto, CreateUserDto createUserDto);
	
	void updAuth(AuthDto authDto, UpdateUserDto updateUserDto);
	
	AuthDto getAuthById(Long id);
	
	boolean delAuthById(Long id);
	
	AuthProf saveAuth(AuthProf auth);
	
	AuthProf fetchAuthByAuthId(String authId);

	Map<String, List<String>> findUserProjAuths(String userId, String sysId);
	
	Map<String, List<String>> findProjAuthUsers(String projId);
	
	/**
	 * 2018/03/26 Added by Sephiro : 專案計劃權限管理 - 查詢grid資料
	 */
	PageResult fetchProjs(ProjAuthQuery query);
	
	/**
	 * 2018/03/26 Added by Sephiro : 權限別管理 - 查詢grid資料
	 */
	PageResult fetchAuthProfs(AuthProfQuery query);
	
	/**
	 * 2018/03/26 Added by Sephiro : 專案計劃權限設定 - 權限別維護 - 查詢同一計劃下所有權限別
	 */
	List<Map<String, Object>> listProjAuth(String projId);
	
	/**
	 * 2018/03/27 Added by Sephiro : 專案計劃權限設定- 權限別設定 - 找尋未被指定到此計劃的權限別
	 */
	List<Map<String, Object>> fetchAuthsNotInProjAuth(String projId);

	/**
	 * 2018/03/27 Added by Sephiro : 專案計劃權限設定- 權限別設定 - 找尋已被指定到此計劃的權限別
	 */
	List<Map<String, Object>> fetchAuthsInProjAuth(String projId);	

	/**
	 * 2018/03/27 Added by Sephiro : 專案計劃權限設定- 權限別設定 - 回寫權限別給專案計劃
	 */
	void addOrDelProjAuths(String projId, String[] asAry);

	/**
	 * 2018/03/28 Added by Sephiro ：專案計劃權限設定- 使用者權限別設定 - 找尋同一計劃下，未被指定此權限的使用者帳號
	 */
	List<Map<String, Object>> fetchUsersNotInUserProjAuth(String projId, String authId);

	/**
	 * 2018/03/28 Added by Sephiro ：專案計劃權限設定- 使用者權限別設定 - 找尋同一計劃下，已被指定此權限的使用者帳號
	 */
	List<Map<String, Object>> fetchUsersInUserProjAuth(String projId, String authId);	

	/**
	 * 2018/03/28 Added by Sephiro : 專案計劃權限設定- 使用者權限別設定 - 回寫權限別給使用者帳號
	 */
	void addOrDelUserProjAuths(String projId, String authId, String[] asAry, CreateUserDto createUserDto);

	/**
	 * 2018/03/29 Added by Sephiro : 專案計劃權限設定- 角色權限維護 - 查詢同一計劃下所有權限別
	 */
	List<Map<String, Object>> listProjRole(String projId);
	
	/**
	 * 2018/03/29 Added by Sephiro : 專案計劃權限設定- 角色設定 - 找尋同一計劃下，未被指定的角色
	 */
	List<Map<String, Object>> fetchRolesNotInProjRole(String sysId, String projId);

	/**
	 * 2018/03/29 Added by Sephiro : 專案計劃權限設定- 角色設定 - 找尋同一計劃下，已被指定的角色
	 */
	List<Map<String, Object>> fetchRolesInProjRole(String sysId, String projId);
	
	/**
	 * 2018/03/29 Added by Sephiro : 專案計劃權限設定- 角色設定 - 回寫角色給專案計劃
	 */
	void addOrDelProjRoles(String sysId, String projId, String[] asAry);

	/**
	 * 2018/03/29 Added by Sephiro ：專案計劃權限設定- 使用者角色設定 - 找尋同一計劃下，未被指定此角色的使用者帳號
	 */
	List<Map<String, Object>> fetchUsersNotInUserProjRole(String projId, String roleId);

	/**
	 * 2018/03/29 Added by Sephiro ：專案計劃權限設定- 使用者角色設定 - 找尋同一計劃下，已被指定此角色的使用者帳號
	 */
	List<Map<String, Object>> fetchUsersInUserProjRole(String projId, String roleId);	
	
	RoleDto getRoleById(Long id);

	/**
	 * 2018/03/29 Added by Sephiro : 專案計劃權限設定- 使用者角色設定 - 回寫角色給使用者帳號
	 */
	void addOrDelUserProjRoles(String projId, String roleId, String[] asAry, CreateUserDto createUserDto);
	
	/**
	 * 2018/03/30 Added by Sephiro : 專案計劃權限管理- 刪除
	 */
	boolean deleteProjById(Long id) throws ApException;

	/**
	 * 2018/03/30 Added by Sephiro : 權限別設定- 刪除
	 */
	boolean deleteAuthById(Long id) throws ApException;
}
