package com.tradevan.aporg.repository.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.tradevan.apcommon.persistence.JpaGenericRepository;
import com.tradevan.aporg.model.UserProjRole;
import com.tradevan.aporg.repository.UserProjRoleRepository;

/**
 * Title: JpaUserProjRoleRepository<br>
 * Description: <br>
 * Company: Tradevan Co.<br>
 * 
 * @author Neil Chen
 * @version 1.0
 */
@Repository
public class JpaUserProjRoleRepository extends JpaGenericRepository<UserProjRole, Long> implements UserProjRoleRepository {

	@Override
	public List<Map<String, Object>> findUsersNotInUserProjRole(String projId, String roleId) {
		StringBuffer sql = new StringBuffer();
		sql.append(" select up.userId + '(' + up.userName + ')' as name, up.userId as value ");
		sql.append(" from UserProf up "); 
		sql.append(" where NOT EXISTS( "); 
		sql.append(" select upr.userId "); 
		sql.append(" from UserProjRole upr ");
		sql.append(" where upr.projId = ? "); 
		sql.append(" and upr.roleId = ? "); 
		sql.append(" and upr.userId = up.userId ");
		sql.append(" ) "); 		
		return findListMapBySQL(sql.toString(), projId, roleId);
	}

	@Override
	public List<Map<String, Object>> findUsersInUserProjRole(String projId, String roleId) {
		StringBuffer sql = new StringBuffer();
		sql.append(" select up.userId + '(' + up.userName + ')' as name, up.userId as value ");
		sql.append(" from UserProf up "); 
		sql.append(" where up.userId IN( "); 
		sql.append(" select upr.userId "); 
		sql.append(" from UserProjRole upr ");
		sql.append(" where upr.projId = ? "); 
		sql.append(" and upr.roleId = ? "); 
		sql.append(" and upr.userId = up.userId "); 
		sql.append(" ) "); 		
		return findListMapBySQL(sql.toString(), projId, roleId);
	}
	
	@Override
	public Map<String, Object> findUserInUserProjRole(Map<String, Object> params) {
		StringBuilder sql = new StringBuilder();
		sql.append(" select serNo");
		sql.append(" from UserProjRole");
		sql.append(" where 1=1");
		if (params.containsKey("projId")) {
			sql.append("and projId = :projId ");
		}
		if (params.containsKey("userId")) {
			sql.append("and userId = :userId ");
		}
		
		return getMapBySQL_map(sql.toString(), params);		
	}
}
