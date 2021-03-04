package com.tradevan.aporg.repository;

import java.util.List;
import java.util.Map;

import com.tradevan.apcommon.bean.PageResult;
import com.tradevan.apcommon.persistence.GenericRepository;
import com.tradevan.aporg.model.RoleProf;

/**
 * Title: RoleRepository<br>
 * Description: <br>
 * Company: Tradevan Co.<br>
 * 
 * @author Neil Chen
 * @version 1.1
 */
public interface RoleProfRepository extends GenericRepository<RoleProf, Long> {

	PageResult findRoles(Map<String, Object> params, Integer page, Integer pageSize);

	List<Map<String, Object>> findRolesNotInProjRole(String sysId, String projId);

	List<Map<String, Object>> findRolesInProjRole(String sysId, String projId);
	
	List<Map<String, Object>> findRolesNotInRoleIds(String sysId, String roleIds, String deptId, int mode);
	
	List<Map<String, Object>> findRolesInRoleIds(String sysId, String roleIds, String deptId, int mode);
	
	Integer countDeptRole(String[] asRoleAry);
	
	PageResult findRoleDepts(Map<String, Object> params, Integer page, Integer pageSize);
	
}
