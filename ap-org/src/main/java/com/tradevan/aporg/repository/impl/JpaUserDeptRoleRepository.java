package com.tradevan.aporg.repository.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.tradevan.apcommon.persistence.JpaGenericRepository;
import com.tradevan.apcommon.util.StringUtil;
import com.tradevan.aporg.model.UserDeptRole;
import com.tradevan.aporg.repository.UserDeptRoleRepository;

/**
 * Title: JpaUserDeptRoleRepository<br>
 * Description: <br>
 * Company: Tradevan Co.<br>
 * 
 * @author Neil Chen
 * @version 1.0
 */
@Repository
public class JpaUserDeptRoleRepository extends JpaGenericRepository<UserDeptRole, Long> implements UserDeptRoleRepository {

	@Override
	public List<Map<String, Object>> findNotInDeptRoleUsers(String deptId, String roleId, String deptId2, String userId, String userName) {
		List<String> sqlParams = new ArrayList<String>();
		sqlParams.add(deptId);
		sqlParams.add(roleId);
		String appendSql = "";
		if (StringUtil.isNotBlank(deptId2)) {
			appendSql += " and u.deptId = ? ";
			sqlParams.add(deptId2);
		}
		if (StringUtil.isNotBlank(userId)) {
			appendSql += " and u.userId like ? ";
			sqlParams.add(userId + "%");
		}
		if (StringUtil.isNotBlank(userName)) {
			appendSql += " and u.userName like ? ";
			sqlParams.add("%" + userName + "%");
		}
		
		return findListMapBySQL("SELECT u.userId, u.userId + '(' + u.userName + ')' AS name FROM UserProf u WHERE " +
				"NOT EXISTS(SELECT udr.serNo FROM UserDeptRole udr WHERE udr.userId = u.userId AND udr.deptId = ? AND udr.roleId = ?)" +
				appendSql, sqlParams.toArray());
	}

	@Override
	public List<Map<String, Object>> findInDeptRoleUsers(String deptId, String roleId) {
		
		return findListMapBySQL("SELECT u.userId, u.userId + '(' + u.userName + ')' AS name FROM UserProf u WHERE " +
				"EXISTS(SELECT udr.serNo FROM UserDeptRole udr WHERE udr.userId = u.userId AND udr.deptId = ? AND udr.roleId = ?)",
				deptId, roleId);
	}
	
}
