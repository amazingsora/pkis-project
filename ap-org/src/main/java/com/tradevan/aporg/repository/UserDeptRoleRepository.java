package com.tradevan.aporg.repository;

import java.util.List;
import java.util.Map;

import com.tradevan.apcommon.persistence.GenericRepository;
import com.tradevan.aporg.model.UserDeptRole;

/**
 * Title: UserDeptRoleRepository<br>
 * Description: <br>
 * Company: Tradevan Co.<br>
 * 
 * @author Neil Chen
 * @version 1.0
 */
public interface UserDeptRoleRepository extends GenericRepository<UserDeptRole, Long> {

	List<Map<String, Object>> findNotInDeptRoleUsers(String deptId, String roleId, String deptId2, String userId, String userName);
	
	List<Map<String, Object>> findInDeptRoleUsers(String deptId, String roleId);
	
}
