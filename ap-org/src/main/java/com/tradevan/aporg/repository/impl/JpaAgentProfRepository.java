package com.tradevan.aporg.repository.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.tradevan.apcommon.bean.PageResult;
import com.tradevan.apcommon.persistence.JpaGenericRepository;
import com.tradevan.aporg.model.AgentProf;
import com.tradevan.aporg.repository.AgentProfRepository;

/**
 * Title: JpaAgentRepository<br>
 * Description: <br>
 * Company: Tradevan Co.<br>
 * 
 * @author Neil Chen
 * @version 1.1
 */
@Repository
public class JpaAgentProfRepository extends JpaGenericRepository<AgentProf, Long> implements AgentProfRepository {
	
	@Override
	public List<AgentProf> findActiveAgentRecords(String agentUserId) {
		
		String jpql = "select a from AgentProf a where a.agent.userId = ?1 and ?2 between a.begDate and a.endDate and a.status = 'Y' and a.user.status = 'Y' order by a.user.userId, a.begDate";
		
		return findEntityListByJPQL(jpql, agentUserId, new Date());
	}

	@Override
	public PageResult findAgentConfs(Map<String, Object> params, Integer page, Integer pageSize) {
		StringBuilder sql = new StringBuilder(
				"select ROW_NUMBER() OVER (order by a.userId, a.begDate desc, a.endDate desc) AS rownum, a.serNo "
			);
		sql.append(",u1.userId + '(' + u1.userName + ')' as userName");
		sql.append(",u2.userId + '(' + u2.userName + ')' as agentUserName");
		sql.append(",CONVERT(char(10), a.begDate, 111) + '-' + CONVERT(char(5), a.begDate, 108) begDate ");
		sql.append(",CONVERT(char(10), a.endDate, 111) + '-' + CONVERT(char(5), a.endDate, 108) endDate ");
		sql.append("from AgentProf a join UserProf u1 on a.userId = u1.userId join UserProf u2 on a.agentUserId = u2.userId where 1 = 1 ");
		
		if (params.containsKey("sysId")) {
			sql.append("and u1.sysId in ('ALL', :sysId) ");
		}
		else {
			sql.append("and u1.sysId = 'ALL' ");
		}
		if (params.containsKey("userId")) {
			sql.append("and a.userId = :userId ");
		}
		if (params.containsKey("agentUserId")) {
			sql.append("and a.agentUserId = :agentUserId ");
		}
		if (params.containsKey("agentDate")) {
			sql.append("and CONVERT(datetime, :agentDate) between a.begDate-1 and a.endDate ");
		}
		
		sql.append("and a.status = 'Y' ");
		
		return findListMapPageResultBySQL_map(sql.toString(), page, pageSize, params);
	}
}
