package com.tradevan.aporg.repository;

import java.util.List;
import java.util.Map;

import com.tradevan.apcommon.persistence.GenericRepository;
import com.tradevan.aporg.model.UserProjRole;

/**
 * Title: UserProjRoleRepository<br>
 * Description: <br>
 * Company: Tradevan Co.<br>
 * 
 * @author Neil Chen
 * @version 1.0
 */
public interface UserProjRoleRepository extends GenericRepository<UserProjRole, Long> {

	List<Map<String, Object>> findUsersNotInUserProjRole(String projId, String roleId);
	
	List<Map<String, Object>> findUsersInUserProjRole(String projId, String roleId);
	
	Map<String, Object> findUserInUserProjRole(Map<String, Object> params);
}
