package com.tradevan.aporg.repository;

import java.util.List;
import java.util.Map;

import com.tradevan.apcommon.bean.PageResult;
import com.tradevan.apcommon.persistence.GenericRepository;
import com.tradevan.aporg.model.RoleProf;
import com.tradevan.aporg.model.UserProf;

/**
 * Title: UserRepository<br>
 * Description: <br>
 * Company: Tradevan Co.<br>
 * 
 * @author Neil Chen
 * @version 1.4.2
 */
public interface UserProfRepository extends GenericRepository<UserProf, Long> {

	PageResult findUsers(Map<String, Object> params, Integer page, Integer pageSize);
	
	List<Map<String, Object>> findUsers(Map<String, Object> params);
	
	boolean isRoleInUse(RoleProf role);

	UserProf getUserByUserId(String userId);
	
	List<String> findUserDeptIds(String userId);
	
	List<String> findUserRoleIds(String sysId, String userId);
	
	List<Map<String, Object>> findNotAssignedUsersByConditions(Map<String, Object> params);
	
	List<Map<String, Object>> findAssignedUsersByConditions(Map<String, Object> params);
	
	List<Map<String, Object>> findUsersNotInUserIds(String userIds);
	
	List<Map<String, Object>> findUsersInUserIds(String userIds);
	
	Long getNextUserIdSeq();
	
	List<UserProf> findUsersBy(String projId, String roleId);
	
	PageResult fetchUserProfs(Map<String, Object> params, Integer page, Integer pageSize);
	
	PageResult fetchActiveUserProfs(Map<String, Object> params, Integer page, Integer pageSize);

	Map<String, Object> getUserProf(Map<String, Object> params);
	
	Map<String, Object> getUsePIrProf(Map<String, Object> params);
	
	PageResult fetchPreEmpStat(Map<String, Object> params, Integer page, Integer pageSize);
	
	List<UserProf> fetchUserprof(String sysId, String deptId, String userId);
	
	UserProf getUserprof(String sysId, String userId);

	List<String> findUserEmailByRoleId(String roleEduGrp);
	
	Map<String, Object> findPIUserInfo(Map<String, Object> params);
	
	List<String> fetchEmailByRoleId(String roleId);
	
}
