package com.tradevan.aporg.repository;

import java.util.List;
import java.util.Map;

import com.tradevan.apcommon.persistence.GenericRepository;
import com.tradevan.aporg.model.UserProjAuth;

/**
 * Title: UserProjAuthRepository<br>
 * Description: <br>
 * Company: Tradevan Co.<br>
 * 
 * @author Neil Chen
 * @version 1.0
 */
public interface UserProjAuthRepository extends GenericRepository<UserProjAuth, Long> {

	List<Map<String, Object>> findUsersNotInUserProjAuth(String projId, String authId);
	
	List<Map<String, Object>> findUsersInUserProjAuth(String projId, String authId);
	
	Map<String, Object> findUserInUserProjAuth(Map<String, Object> params);
	
	List<UserProjAuth> getUserProjAuth(Map<String, Object> params);
	
}
