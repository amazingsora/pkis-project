package com.tradevan.aporg.service.impl;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.tradevan.apcommon.bean.CommonMsgCode;
import com.tradevan.apcommon.bean.CreateUserDto;
import com.tradevan.apcommon.bean.NameValuePair;
import com.tradevan.apcommon.bean.PageResult;
import com.tradevan.apcommon.bean.UpdateUserDto;
import com.tradevan.apcommon.exception.ApException;
import com.tradevan.apcommon.service.I18nMsgService;
import com.tradevan.apcommon.util.BeanUtil;
import com.tradevan.apcommon.util.StringUtil;
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
import com.tradevan.aporg.model.JobRank;
import com.tradevan.aporg.model.JobType;
import com.tradevan.aporg.model.RoleProf;
import com.tradevan.aporg.model.UserDeptRole;
import com.tradevan.aporg.model.UserProf;
import com.tradevan.aporg.repository.AgentProfRepository;
import com.tradevan.aporg.repository.DeptProfRepository;
import com.tradevan.aporg.repository.JobRankRepository;
import com.tradevan.aporg.repository.JobTypeRepository;
import com.tradevan.aporg.repository.RoleProfRepository;
import com.tradevan.aporg.repository.SysProfRepository;
import com.tradevan.aporg.repository.UserDeptRoleRepository;
import com.tradevan.aporg.repository.UserProfRepository;
import com.tradevan.aporg.repository.UserProjRoleRepository;
import com.tradevan.aporg.service.OrgService;

/**
 * Title: OrgServiceImpl<br>
 * Description: <br>
 * Company: Tradevan Co.<br>
 * 
 * @author Neil Chen
 * @version 1.6.5
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class OrgServiceImpl implements OrgService {

	@Autowired
	protected I18nMsgService i18nMsgService;
	
	@Autowired
	private SysProfRepository sysRepository;
	
	@Autowired
	private DeptProfRepository deptRepository;
	
	@Autowired
	private UserProfRepository userRepository;
	
	@Autowired
	private RoleProfRepository roleRepository;
	
	@Autowired
	private AgentProfRepository agentRepository;
	
	@Autowired
	private UserProjRoleRepository userProjRoleRepository;
	
	@Autowired
	private UserDeptRoleRepository userDeptRoleRepository;
	
	@Autowired
	private JobRankRepository jobRankRepository;
	
	@Autowired
	private JobTypeRepository jobTypeRepository;
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void addUser(UserDto userDto, CreateUserDto createUserDto) {
		UserProf user = new UserProf(userDto, createUserDto);
		linkRelations(userDto, user);
		userRepository.save(user);
	}
	
	private void linkRelations(UserDto userDto, UserProf user) {
		if (StringUtils.isNotBlank(userDto.getDeptId())) {
			user.setDept(deptRepository.getEntityByProperty("deptId", userDto.getDeptId()));
		}
		List<String> roleIds = userDto.getRoleIds();
		if (roleIds != null && roleIds.size() > 0) {
			user.setRoles(new HashSet<RoleProf>(roleRepository.findEntityListInValueList("roleId", userDto.getRoleIds(), null, null, null)));
		}
		else {
			user.setRoles(null);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void updUser(UserDto userDto, UpdateUserDto updateUserDto) {
		UserProf user = userRepository.getEntityById(userDto.getId());
		BeanUtil.copyPropertiesIgnoreNull(userDto, user, "id", "createUserId", "createTime");
		if (! UserDto.FAKE_PASSWORD.equals(userDto.getPwd())) {
			user.setPwdHash(DigestUtils.sha1Hex(userDto.getPwd()));
		}
		BeanUtil.copyProperties(updateUserDto, user);
		linkRelations(userDto, user);
		userRepository.save(user);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void updUserPwd(UserDto userDto, UpdateUserDto updateUserDto) {
		UserProf user = userRepository.getEntityById(userDto.getId());
		//BeanUtil.copyPropertiesIgnoreNull(userDto, user, "id", "createUserId", "createTime");
		
		if (! UserDto.FAKE_PASSWORD.equals(userDto.getPwd())) {
			user.setPwdHash(DigestUtils.sha1Hex(userDto.getPwd()));
		}
		BeanUtil.copyProperties(updateUserDto, user);
		//linkRelations(userDto, user);
		userRepository.save(user);
	}
	
	@Override
	public String genUserId() {
		Long seq = userRepository.getNextUserIdSeq();
		String serial = seq.toString();
		StringBuilder buf = new StringBuilder();
		int len = 10 - serial.length();
		for (int x = 0; x < len; x++) {
			buf.append("0");
		}
		buf.append(serial);
		
		return "OA" + buf.toString();
	}
	
	@Override
	public String genPassword() {
	    List<Integer> list = new ArrayList<>();
	    StringBuilder sb = new StringBuilder();
	    
		// Add ASCII numbers of characters commonly acceptable in passwords
        for (int i = 33; i < 127; i++) {
        	list.add(i);
        }

        // Remove characters /, \, and " as they're not commonly accepted
        list.remove(new Integer(34));
        list.remove(new Integer(47));
        list.remove(new Integer(92));

        // Randomise over the ASCII numbers and append respective character values into a StringBuilder
        for (int i = 0; i < 10; i++) {
            int randInt = list.get(new SecureRandom().nextInt(91));
            sb.append((char) randInt);
        }

		return sb.toString();
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void addRole(RoleDto roleDto, CreateUserDto createUserDto) {
		RoleProf role = new RoleProf(roleDto, createUserDto);
		role.applyDefault(roleDto.getDeptRole());
		roleRepository.save(role);
	}

	@Override
	public void updRole(RoleDto roleDto, UpdateUserDto updateUserDto) {
		RoleProf role = roleRepository.getEntityById(roleDto.getId());
		BeanUtil.copyPropertiesIgnoreNull(roleDto, role, "id", "createUserId", "createTime");
		role.applyDefault(roleDto.getDeptRole());
		BeanUtil.copyProperties(updateUserDto, role);
		roleRepository.save(role);
	}

	@Override
	public UserDto getUserById(Long id) {
		UserProf user = userRepository.getEntityById(id);
		if (user != null) {
			UserDto userDto = new UserDto(user);
			return userDto;
		}
		return null;
	}

	@Override
	public RoleDto getRoleById(Long id) {
		RoleProf role = roleRepository.getEntityById(id);
		if (role != null) {
			RoleDto roleDto = new RoleDto(role);
			return roleDto;
		}
		return null;
	}
	
	@Override
	public UserDto getUserByUserId(String userId) {
		UserProf user = userRepository.getEntityByProperty("userId", userId);
		if (user != null) {
			UserDto userDto = new UserDto(user);
			return userDto;
		}
		return null;
	}
	
	@Override
	public RoleDto getRoleByRoleId(String roleId) {
		RoleProf role = roleRepository.getEntityByProperty("roleId", roleId);
		if (role != null) {
			RoleDto roleDto = new RoleDto(role);
			return roleDto;
		}
		return null;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean delUserById(Long id) {
		return userRepository.delete(id);
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean delRoleById(Long id) throws ApException {
		RoleProf role = roleRepository.getEntityById(id);
		if (role != null && userRepository.isRoleInUse(role)) {
			throw new ApException(CommonMsgCode.CODE_E_CANNOT_BE_DELETED, i18nMsgService.getText(CommonMsgCode.MSG_E_CANNOT_BE_DELETED));
		}
		else {
			return roleRepository.delete(id);
		}
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public DeptProf saveDept(DeptProf dept) {
		return deptRepository.save(dept);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public UserProf saveUser(UserProf user) {
		return userRepository.save(user);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public RoleProf saveRole(RoleProf role) {
		return roleRepository.save(role);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public AgentProf saveAgent(AgentProf agent) {
		return agentRepository.save(agent);
	}
	
	@Override
	public DeptProf fetchDeptByDeptId(String deptId) {
		return deptRepository.getEntityByProperty("deptId", deptId);
	}
	
	@Override
	public UserProf fetchUserByUserId(String userId) {
		return userRepository.getUserByUserId(userId);
	}
	
	@Override
	public RoleProf fetchRoleByRoleId(String roleId) {
		return roleRepository.getEntityByProperty("roleId", roleId);
	}

	@Override
	public List<AgentProf> fetchActiveAgentsByUserId(String userId) {
		return agentRepository.findActiveAgentRecords(userId);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<NameValuePair> fetchAllSystems() {
		List<NameValuePair> rtnList = new ArrayList<NameValuePair>();
		for (Object[] sys : (List<Object[]>) sysRepository.findByProperty(new String[] { "sysId", "name" }, null, null, "sysId")) {
			rtnList.add(new NameValuePair((String) sys[1], (String) sys[0]));
		}
		return rtnList;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public List<NameValuePair> fetchAllDepts(boolean isEnabled) {
		List<NameValuePair> rtnList = new ArrayList<NameValuePair>();
		for (Object[] dept : (List<Object[]>) deptRepository.findByProperty(new String[] { "deptId", "name", "level" }, isEnabled ? "status" : null, isEnabled ? "Y" : null, "deptId")) {
			Integer level = (Integer) dept[2];
			if (level == null || level > 0) {
				rtnList.add(new NameValuePair(dept[0] + " (" + dept[1] + ")", (String) dept[0]));
			}
		}
		return rtnList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<NameValuePair> fetchAllRoles(boolean isEnabled) {
		List<NameValuePair> rtnList = new ArrayList<NameValuePair>();
		for (Object[] role : (List<Object[]>) roleRepository.findByProperty(new String[] { "roleId", "name" }, isEnabled ? "status" : null, isEnabled ? "Y" : null, "roleId")) {
			rtnList.add(new NameValuePair(role[0] + " (" + role[1] + ")", (String) role[0]));
		}
		return rtnList;
	}

	@Override
	public PageResult fetchUsers(UserQuery query) {
		Map<String, Object> params = new HashMap<String, Object>();
		if (!"ALL".equalsIgnoreCase(query.getSysId())) {
			params.put("sysId", query.getSysId());
		}
		if (StringUtils.isNotBlank(query.getUserId())) {
			params.put("userId", "%" + query.getUserId() + "%");
		}
		if (StringUtils.isNotBlank(query.getName())) {
			params.put("name", "%" + query.getName() + "%");
		}
		if (query.getIsSuperAdmin() != null) {
			params.put("isSuperAdmin", query.getIsSuperAdmin());
		}
		return userRepository.findUsers(params, query.getPage(), query.getPageSize());
	}
	
	@Override
	public PageResult fetchRoles(RoleQuery query) {
		Map<String, Object> params = new HashMap<String, Object>();
		if (!"ALL".equalsIgnoreCase(query.getSysId())) {
			params.put("sysId", query.getSysId());
		}
		if (StringUtils.isNotBlank(query.getRoleId())) {
			params.put("roleId", query.getRoleId());
		}
		if (StringUtils.isNotBlank(query.getName())) {
			params.put("name", "%" + query.getName() + "%");
		}
		if (query.getIsSuperAdmin() != null) {
			params.put("isSuperAdmin", query.getIsSuperAdmin());
		}
		return roleRepository.findRoles(params, query.getPage(), query.getPageSize());
	}

	@Override
	public PageResult fetchRoleDepts(RoleDeptQuery query) {
		Map<String, Object> params = new HashMap<String, Object>();
		if (!"ALL".equalsIgnoreCase(query.getSysId())) {
			params.put("sysId", query.getSysId());
		}
		if (StringUtils.isNotBlank(query.getUpDeptId())) {
			params.put("upDeptId", query.getUpDeptId());
		}
		if (StringUtils.isNotBlank(query.getDeptId())) {
			params.put("deptId", query.getDeptId());
		}
		if (StringUtils.isNotBlank(query.getRoleId())) {
			params.put("roleId", query.getRoleId());
		}
		if (StringUtils.isNotBlank(query.getUserId())) {
			params.put("userId", query.getUserId());
		}
		if (query.getIsSuperAdmin() != null) {
			params.put("isSuperAdmin", query.getIsSuperAdmin());
		}
		return roleRepository.findRoleDepts(params, query.getPage(), query.getPageSize());
	}
	
	@Override
	public List<String> findUserDeptIdList(String userId) {
		return userRepository.findUserDeptIds(userId);
	}

	@Override
	public List<String> findUserRoleIdList(String sysId, String userId) {
		return userRepository.findUserRoleIds(sysId, userId);
	}

	@Override
	public List<NameValuePair> findBeRepresentedUsers(String userId) {
		List<NameValuePair> rtnList = new ArrayList<NameValuePair>();
		String lastUserId = "";
		for (AgentProf agent : agentRepository.findActiveAgentRecords(userId)) {
			UserProf user = agent.getUser();
			if (! user.getUserId().equals(lastUserId)) {
				rtnList.add(new NameValuePair(user.getName(), user.getUserId()));
			}
			lastUserId = user.getUserId();
		}
		return rtnList;
	}

	@Override
	public List<String> findProjRoleUserIdList(String projId, String roleId) {
		List<String> rtnList = new ArrayList<String>();
		for (Object userId : userProjRoleRepository.findByProperties(new String[] { "userId" }, 
				new String[] { "projId", "roleId" }, new String[] { projId, roleId }, new String[] { "userId" })) {
			rtnList.add((String) userId);
		}
		return rtnList;
	}
	
	/**
	 * 2018/03/22 Added by Sephiro : 找尋同一系統別下，未被指定此角色的帳號
	 */
	@Override
	public List<Map<String, Object>> fetchNotAssignedUsersByConditions(String roleId, String sysId, 
			String deptId, String userId, String userName){
		Map<String, Object> params = new HashMap<String, Object>();
		if(StringUtils.isNotBlank(roleId)) {
			params.put("roleId", roleId);
		}
		if(StringUtils.isNotBlank(sysId)) {
			params.put("sysId", sysId);
		}
		if (StringUtil.isNotBlank(deptId)) {
			params.put("deptId", deptId);
		}
		if (StringUtil.isNotBlank(userId)) {
			params.put("userId", userId + "%");
		}
		if (StringUtil.isNotBlank(userName)) {
			params.put("userName", "%" + userName + "%");
		}
		return userRepository.findNotAssignedUsersByConditions(params);
	}
	
	/**
	 * 2018/03/22 Added by Sephiro : 找尋同一系統別下，所有已被指定此角色的帳號
	 */
	@Override
	public List<Map<String, Object>> fetchAssignedUsersByConditions(String roleId, String sysId){
		Map<String, Object> params = new HashMap<String, Object>();
		if(StringUtils.isNotBlank(roleId)) {
			params.put("roleId", roleId);
		}
		if(StringUtils.isNotBlank(sysId)) {
			params.put("sysId", sysId);
		}
		return userRepository.findAssignedUsersByConditions(params);
	}

	/**
	 * 2018/03/22 Added by Sephiro : 回寫角色給帳號
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void saveRoleByUser(String roleId, String sysId, String[] asAry) {
		//roleId -> roleProf
		RoleProf role = roleRepository.getEntityByProperty("roleId", roleId);
		//ori list
		List<String> oriUserList = new ArrayList<String>();
		List<Map<String, Object>> oriList = fetchAssignedUsersByConditions(roleId, sysId);
        for (int x = 0; x < oriList.size(); x++){
        	Map<String, Object> oriMap = (Map<String, Object>) oriList.get(x);
        	oriUserList.add((String)oriMap.get("userId"));
        }
        //assign list
        List<String> assignUserList = new ArrayList<String>();
        if(asAry!= null && asAry.length > 0) {
        	for(String assignUserId : asAry) {
        		assignUserList.add(assignUserId);
        	}
        }
        //compare two list 
        if((oriUserList.size() == 0 || oriUserList == null) 
        		&& (assignUserList.size() == 0 || assignUserList == null)) { 
        	//沒有任何帳號要被新增此角色====>不處理
        	return;
        }else if((oriUserList.size() == 0 || oriUserList == null) && assignUserList.size() > 0) {
        	for(String assignUserId2 : assignUserList){
        		addRoleByUser(assignUserId2, role);
        	}        	
        }else if(oriUserList.size() > 0 && (assignUserList.size() == 0 || assignUserList == null)) {
        	for(String oriUserId : oriUserList){
    			deleteRoleByUser(oriUserId, role);
        	}         	
        }else {
        	//1.assign list有，但ori list沒有====>要把角色加到這帳號裡
        	for(String assignUserId2 : assignUserList){
        		if(!oriUserList.contains(assignUserId2)){
        			addRoleByUser(assignUserId2, role);
        		}
        	}
        	//2.ori list有，但assign list沒有====>要把角色從這帳號移除
        	for(String oriUserId : oriUserList){
        		if(!assignUserList.contains(oriUserId)){
        			deleteRoleByUser(oriUserId, role);
        		}
        	}          
        }
	}
	
	private void addRoleByUser(String assignUserId2, RoleProf role) {
		UserProf userProf = userRepository.getEntityByProperty("userId", assignUserId2);
		Set<RoleProf> roleSet = userProf.getRoles();
		roleSet.add(role);
		userProf.setRoles(roleSet);
		userRepository.update(userProf);
	}
	
	private void deleteRoleByUser(String oriUserId, RoleProf role) {
		UserProf userProf = userRepository.getEntityByProperty("userId", oriUserId);
		Set<RoleProf> roleSet = userProf.getRoles();
		for (Iterator<RoleProf> it = roleSet.iterator(); it.hasNext();) {
			RoleProf roleProf = it.next();
		    if (StringUtils.equals(role.getRoleId(), roleProf.getRoleId())) {
		        it.remove();
		    }
		}    		
		userProf.setRoles(roleSet);
		userRepository.update(userProf);		
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<String> fetchUserEmailsByRole(RoleProf role) {
		return userRepository.findByPropertyAndMemberOf(new String[] { "email" }, "status", "Y", "roles", role, null);
	}
	
	@Override
	public List<UserProf> fetchUsersByProjIdAndRoleId(String projId, String roleId) {
		return userRepository.findUsersBy(projId, roleId);
	}

	@Override
	public List<UserDeptRole> fetchUserDeptRoles(String userId) {
		return userDeptRoleRepository.findEntityListByProperty("userId", userId, "deptId");
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<UserProf> fetchUsersInUserDeptRole(String deptId, String roleId) {
		return userDeptRoleRepository.findByProperties(new String[] { "userProf" }, new String[] { "deptId", "roleId" }, new Object[] { deptId, roleId }, new String[] { "userId" });
	}

	@Override
	public Integer getMaxDeptLevelByRole(RoleProf role) {
		return (Integer) deptRepository.getByPropertyAndMemberOf(new String[] { "MAX(", "level", ")" }, null, null, "roles", role);
	}
	
	@Override
	public Map<String, List<String>> findUserDeptRoles(String userId) {
		Map<String, List<String>> map = new HashMap<String, List<String>>();
		List<String> list = null;
		String lastDeptId = "";
		for (UserDeptRole userDeptRole : userDeptRoleRepository.findEntityListByProperty("userId", userId, "deptId")) {
			String deptId = userDeptRole.getDeptId();
			if (list == null || !lastDeptId.equals(deptId)) {
				list = new ArrayList<String>();
				map.put(deptId, list);
			}
			list.add(userDeptRole.getRoleId());
			lastDeptId = deptId;
		}
		return map;
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void updateUserDeptRoles(String deptId, String roleId, List<String> userIds, CreateUserDto createUserDto) {
		List<String> origUserIds = userDeptRoleRepository.findByProperties(new String[] { "userId" }, new String[] { "deptId", "roleId" }, new Object[] { deptId, roleId }, null);
		for (String origUserId : origUserIds) {
			if (! userIds.contains(origUserId)) {
				userDeptRoleRepository.deleteByProperties(new String[] { "userId", "deptId", "roleId" }, new Object[] { origUserId, deptId, roleId });
			}
		}
		
		for (String userId : userIds) {
			if (! origUserIds.contains(userId)) {
				userDeptRoleRepository.save(new UserDeptRole(userId, deptId, roleId, createUserDto));
			}
		}
	}

	@Override
	public PageResult fetchAgentConfs(AgentConfQuery query) {
		Map<String, Object> params = new HashMap<String, Object>();
		if (!"ALL".equalsIgnoreCase(query.getSysId())) {
			params.put("sysId", query.getSysId());
		}
		if (StringUtils.isNotBlank(query.getUserId())) {
			params.put("userId", query.getUserId());
		}
		if (StringUtils.isNotBlank(query.getAgentUserId())) {
			params.put("agentUserId", query.getAgentUserId());
		}
		if (StringUtils.isNotBlank(query.getAgentDate())) {
			params.put("agentDate", query.getAgentDate());
		}
		return agentRepository.findAgentConfs(params, query.getPage(), query.getPageSize());
	}

	@Override
	public AgentDto getAgentById(Long id) {
		AgentProf agent = agentRepository.getEntityById(id);
		if (agent != null) {
			AgentDto agentDto = new AgentDto(agent);
			return agentDto;
		}
		return null;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void addAgent(AgentDto dto, CreateUserDto createUserDto) {
		AgentProf agent = new AgentProf(dto, createUserDto);
		agent.applyFormat(dto);
		agent.setStatus("Y");
		agentRepository.save(agent);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void updAgent(AgentDto dto, UpdateUserDto updateUserDto) {
		AgentProf agent = agentRepository.getEntityById(dto.getId());
		BeanUtil.copyPropertiesIgnoreNull(dto, agent, "id", "createUserId", "createTime");
		agent.applyFormat(dto);
		BeanUtil.copyProperties(updateUserDto, agent);
		agentRepository.save(agent);
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void updAgentStatusToN(Long id) {
		AgentProf agent = agentRepository.getEntityById(id);
		agent.setStatus("N");
	}
	
	@Override
	public String getDeptIdByUserId(String userId) {
		return (String) userRepository.getByProperty(new String[] { "deptId" }, "userId", userId);
	}
	
	@Override
	public String getUpDeptIdByDeptId(String deptId) {
		return (String) deptRepository.getByProperty(new String[] { "upDeptId" }, "deptId", deptId);
	}
	
	@Override
	public boolean checkUserByUserId(String userId) {
		return userRepository.countByProperty("userId", userId) > 0 ? true : false;
	}
	
	@Override
	public boolean checkUserByUserIdAndStatus(String userId, String status) {
		return userRepository.countByProperties(new String[] { "userId", "status" }, new Object[] { userId, status }) > 0 ? true : false;
	}
	
	@Override
	public boolean checkUserDeptRoleByDeptIdAndRoleId(String deptId, String roleId) {
		return userDeptRoleRepository.countByProperties(new String[] { "deptId", "roleId" }, new Object[] { deptId, roleId }) > 0 ? true : false;
	}
	
	@Override
	public JobRankDto getJobRankById(Long id) {
		JobRank jobRank = jobRankRepository.getEntityById(id);
		if (jobRank != null) {
			JobRankDto dto = new JobRankDto(jobRank);
			return dto;
		}
		return null;
	}
	
	@Override
	public JobTypeDto getJobTypeById(Long id) {
		JobType jobType = jobTypeRepository.getEntityById(id);
		if (jobType != null) {
			JobTypeDto dto = new JobTypeDto(jobType);
			return dto;
		}
		return null;
	}
	
	@Override
	public List<String> fetchEmailByRoleId(String roleId) {
		return userRepository.fetchEmailByRoleId(roleId);
	}
}
