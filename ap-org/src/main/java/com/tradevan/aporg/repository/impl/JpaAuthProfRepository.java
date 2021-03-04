package com.tradevan.aporg.repository.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.tradevan.apcommon.bean.PageResult;
import com.tradevan.apcommon.persistence.JpaGenericRepository;
import com.tradevan.aporg.model.AuthProf;
import com.tradevan.aporg.repository.AuthProfRepository;

/**
 * Title: JpaAuthProfRepository<br>
 * Description: <br>
 * Company: Tradevan Co.<br>
 * 
 * @author Neil Chen
 * @version 1.0
 */
@Repository
public class JpaAuthProfRepository extends JpaGenericRepository<AuthProf, Long> implements AuthProfRepository {

	@Override
	public PageResult findAuthProfs(Map<String, Object> params, Integer page, Integer pageSize) {
		StringBuffer sql = new StringBuffer();
		sql.append(" select ROW_NUMBER() OVER (order by a.sysId, a.authId) AS rownum ");	
		sql.append(" , a.serNo ");	
		sql.append(" , s.sysName as system ");	
		sql.append(" , a.authId ");	
		sql.append(" , a.authName as name ");	
		sql.append(" , a.sysId ");	
		sql.append(" , a.status ");	
		sql.append(" from AuthProf a left join SysProf s on a.sysId = s.sysId ");	 
		sql.append(" where 1 = 1 ");		
		if(params.containsKey("sysId")) sql.append(" and a.sysId in ('ALL', :sysId) "); else sql.append(" and a.sysId = 'ALL' ");
		if(params.containsKey("authId")) sql.append(" and a.authId = :authId ");
		if(params.containsKey("name")) sql.append(" and a.authName like :name ");
		return findListMapPageResultBySQL_map(sql.toString(), page, pageSize, params);
	}

	@Override
	public List<Map<String, Object>> findAuthsNotInProjAuth(String projId) {
		StringBuffer sql = new StringBuffer();
		sql.append(" select a.authName as name, a.authId as value");	
		sql.append(" from AuthProf a ");	
		sql.append(" where not exists( ");	
		sql.append(" select pa.authId ");	
		sql.append(" from ProjAuth pa ");	
		sql.append(" where pa.authId = a.authId ");	
		sql.append(" and pa.projId = ? ");			
		sql.append(" ) ");			
		return findListMapBySQL(sql.toString(), projId);
	}

	@Override
	public List<Map<String, Object>> findAuthsInProjAuth(String projId) {
		StringBuffer sql = new StringBuffer();
		sql.append(" select a.authName as name, a.authId as value");	
		sql.append(" from AuthProf a ");	
		sql.append(" where a.authId IN( ");	
		sql.append(" select pa.authId ");	
		sql.append(" from ProjAuth pa ");	
		sql.append(" where pa.authId = a.authId ");	
		sql.append(" and pa.projId = ? ");	
		sql.append(" ) ");			
		return findListMapBySQL(sql.toString(), projId);
	}
	
}
