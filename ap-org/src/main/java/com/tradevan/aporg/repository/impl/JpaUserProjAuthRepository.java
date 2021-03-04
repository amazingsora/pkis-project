package com.tradevan.aporg.repository.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.tradevan.apcommon.persistence.JpaGenericRepository;
import com.tradevan.aporg.model.UserProjAuth;
import com.tradevan.aporg.repository.UserProjAuthRepository;

/**
 * Title: JpaUserProjAuthRepository<br>
 * Description: <br>
 * Company: Tradevan Co.<br>
 * 
 * @author Neil Chen
 * @version 1.0
 */
@Repository
public class JpaUserProjAuthRepository extends JpaGenericRepository<UserProjAuth, Long> implements UserProjAuthRepository {

	@Override
	public List<Map<String, Object>> findUsersNotInUserProjAuth(String projId, String authId) {
		StringBuffer sql = new StringBuffer();
		sql.append(" select up.userId + '(' + up.userName + ')' as name, up.userId as value");
		sql.append(" from UserProf up ");
		sql.append(" where NOT EXISTS( ");
		sql.append(" select upa.userId ");
		sql.append(" from UserProjAuth upa ");
		sql.append(" where upa.projId = ? ");
		sql.append(" and upa.authId = ? ");
		sql.append(" and upa.userId = up.userId ");
		sql.append(" ) ");		
		return findListMapBySQL(sql.toString(), projId, authId);
	}

	@Override
	public List<Map<String, Object>> findUsersInUserProjAuth(String projId, String authId) {
		StringBuffer sql = new StringBuffer();
		sql.append(" select up.userId + '(' + up.userName + ')' as name, up.userId as value");
		sql.append(" from UserProf up ");
		sql.append(" where up.userId IN( ");
		sql.append(" select upa.userId ");
		sql.append(" from UserProjAuth upa ");
		sql.append(" where upa.projId = ? ");
		sql.append(" and upa.authId = ? ");
		sql.append(" and upa.userId = up.userId ");
		sql.append(" ) ");		
		return findListMapBySQL(sql.toString(), projId, authId);
	}
	
	@Override
	public Map<String, Object> findUserInUserProjAuth(Map<String, Object> params) {
		
		StringBuilder sql = new StringBuilder();
		sql.append(" select serNo");
		sql.append(" from UserProjAuth");
		sql.append(" where 1=1");
		if (params.containsKey("projId")) {
			sql.append("and projId = :projId ");
		}
		if (params.containsKey("userId")) {
			sql.append("and userId = :userId ");
		}
		
		return getMapBySQL_map(sql.toString(), params);		
		
	}
	
	@Override
	public List<UserProjAuth> getUserProjAuth(Map<String, Object> params){
		StringBuilder sql = new StringBuilder();
		sql.append("select * from UserProjAuth where serNo is not null "); 
		if(params.containsKey("projId")) {
			sql.append("and projId = :projId ");
		}
		if(params.containsKey("userId")) {
			sql.append("and userId = :userId ");
		}
		return findEntityListBySQL_map(sql.toString(), params);
	}
	
}
