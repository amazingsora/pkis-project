package com.tradevan.aporg.service;

import java.util.List;
import java.util.Map;

import com.tradevan.apcommon.bean.CreateUserDto;
import com.tradevan.apcommon.bean.NameValuePair;
import com.tradevan.apcommon.bean.PageResult;
import com.tradevan.apcommon.bean.UpdateUserDto;
import com.tradevan.apcommon.exception.ApException;
import com.tradevan.aporg.bean.AgentConfQuery;
import com.tradevan.aporg.bean.AgentDto;
import com.tradevan.aporg.bean.JobRankDto;
import com.tradevan.aporg.bean.JobTypeDto;
import com.tradevan.aporg.bean.RoleDeptQuery;
import com.tradevan.aporg.bean.RoleDto;
import com.tradevan.aporg.bean.RoleQuery;
import com.tradevan.aporg.bean.UserDto;
import com.tradevan.aporg.bean.UserQuery;
import com.tradevan.aporg.model.AgentProf;
import com.tradevan.aporg.model.DeptProf;
import com.tradevan.aporg.model.RoleProf;
import com.tradevan.aporg.model.UserDeptRole;
import com.tradevan.aporg.model.UserProf;

/**
 * Title: OrgService<br>
 * Description: <br>
 * Company: Tradevan Co.<br>
 * 
 * @author Neil Chen
 * @version 1.6.5
 */
public interface OrgService {

	void addUser(UserDto userDto, CreateUserDto createUserDto);
	
	void updUser(UserDto userDto, UpdateUserDto updateUserDto);
	
	void updUserPwd(UserDto userDto, UpdateUserDto updateUserDto);
	
	String genUserId();
	
	String genPassword();
	
	void addRole(RoleDto roleDto, CreateUserDto createUserDto);
	
	void updRole(RoleDto roleDto, UpdateUserDto updateUserDto);
	
	UserDto getUserById(Long id);
	
	RoleDto getRoleById(Long id);
	
	UserDto getUserByUserId(String userId);
	
	RoleDto getRoleByRoleId(String roleId);
	
	boolean delUserById(Long id);
	
	boolean delRoleById(Long id) throws ApException;
	
	DeptProf saveDept(DeptProf dept);
	
	UserProf saveUser(UserProf user);
	
	RoleProf saveRole(RoleProf role);
	
	AgentProf saveAgent(AgentProf agent);
	
	DeptProf fetchDeptByDeptId(String deptId);
	
	UserProf fetchUserByUserId(String userId);

	RoleProf fetchRoleByRoleId(String roleId);
	
	List<AgentProf> fetchActiveAgentsByUserId(String userId);
	
	List<NameValuePair> fetchAllSystems();
	
	List<NameValuePair> fetchAllDepts(boolean isEnabled);
	
	List<NameValuePair> fetchAllRoles(boolean isEnabled);
	
	PageResult fetchUsers(UserQuery query);
	
	PageResult fetchRoles(RoleQuery query);

	PageResult fetchRoleDepts(RoleDeptQuery query);
	
	List<String> findUserDeptIdList(String userId);

	List<String> findUserRoleIdList(String sysId, String userId);
	
	List<NameValuePair> findBeRepresentedUsers(String userId);
	
	List<String> findProjRoleUserIdList(String projId, String roleId);

	//2018/03/22 Added by Sephiro : 找尋同一系統別下，未被指定此角色的帳號
	List<Map<String, Object>> fetchNotAssignedUsersByConditions(String roleId, String sysId, String deptId, String userId, String userName);
	
	//2018/03/22 Added by Sephiro : 找尋同一系統別下，所有已被指定此角色的帳號
	List<Map<String, Object>> fetchAssignedUsersByConditions(String roleId, String sysId);
	
	/**
	 * 2018/03/22 Added by Sephiro : 回寫角色給帳號
	 */
	void saveRoleByUser(String roleId, String sysId, String[] asAry);
	
	List<String> fetchUserEmailsByRole(RoleProf role);
	
	List<UserProf> fetchUsersByProjIdAndRoleId(String projId, String roleId);
	
	List<UserDeptRole> fetchUserDeptRoles(String userId);
	
	List<UserProf> fetchUsersInUserDeptRole(String deptId, String roleId);
	
	Integer getMaxDeptLevelByRole(RoleProf role);
	
	public Map<String, List<String>> findUserDeptRoles(String userId);

	void updateUserDeptRoles(String deptId, String roleId, List<String> userIds, CreateUserDto createUserDto);

	PageResult fetchAgentConfs(AgentConfQuery query);

	AgentDto getAgentById(Long id);

	void addAgent(AgentDto dto, CreateUserDto createUserDto);

	void updAgent(AgentDto dto, UpdateUserDto updateUserDto);
	
	void updAgentStatusToN(Long id);

	String getDeptIdByUserId(String userId);
	
	String getUpDeptIdByDeptId(String deptId);

	boolean checkUserByUserId(String userId);
	
	boolean checkUserByUserIdAndStatus(String userId, String status);
	
	boolean checkUserDeptRoleByDeptIdAndRoleId(String deptId, String roleId);
	
	JobRankDto getJobRankById(Long id);
	
	JobTypeDto getJobTypeById(Long id);
	
	List<String> fetchEmailByRoleId(String roleId);
}
