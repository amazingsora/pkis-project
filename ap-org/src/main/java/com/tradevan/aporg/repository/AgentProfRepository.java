package com.tradevan.aporg.repository;

import java.util.List;
import java.util.Map;

import com.tradevan.apcommon.bean.PageResult;
import com.tradevan.apcommon.persistence.GenericRepository;
import com.tradevan.aporg.model.AgentProf;

/**
 * Title: AgentRepository<br>
 * Description: <br>
 * Company: Tradevan Co.<br>
 * 
 * @author Neil Chen
 * @version 1.1
 */
public interface AgentProfRepository extends GenericRepository<AgentProf, Long> {

	List<AgentProf> findActiveAgentRecords(String agentUserId);

	PageResult findAgentConfs(Map<String, Object> params, Integer page, Integer pageSize);
}
