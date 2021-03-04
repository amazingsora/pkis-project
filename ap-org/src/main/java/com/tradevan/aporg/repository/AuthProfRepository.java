package com.tradevan.aporg.repository;

import java.util.List;
import java.util.Map;

import com.tradevan.apcommon.bean.PageResult;
import com.tradevan.apcommon.persistence.GenericRepository;
import com.tradevan.aporg.model.AuthProf;

/**
 * Title: AuthProfRepository<br>
 * Description: <br>
 * Company: Tradevan Co.<br>
 * 
 * @author Neil Chen
 * @version 1.0
 */
public interface AuthProfRepository extends GenericRepository<AuthProf, Long> {

	PageResult findAuthProfs(Map<String, Object> params, Integer page, Integer pageSize);
	
	List<Map<String, Object>> findAuthsNotInProjAuth(String projId);

	List<Map<String, Object>> findAuthsInProjAuth(String projId);
	
}
