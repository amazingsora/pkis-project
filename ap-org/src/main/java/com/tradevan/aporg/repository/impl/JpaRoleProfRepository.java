package com.tradevan.aporg.repository.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.tradevan.apcommon.bean.PageResult;
import com.tradevan.apcommon.persistence.JpaGenericRepository;
import com.tradevan.aporg.model.RoleProf;
import com.tradevan.aporg.repository.RoleProfRepository;

/**
 * Title: JpaRoleRepository<br>
 * Description: <br>
 * Company: Tradevan Co.<br>
 * 
 * @author Neil Chen
 * @version 1.1
 */
@Repository
public class JpaRoleProfRepository extends JpaGenericRepository<RoleProf, Long> implements RoleProfRepository {

	@Override
	public PageResult findRoles(Map<String, Object> params, Integer page, Integer pageSize) {
		StringBuilder sql = new StringBuilder(
				"select ROW_NUMBER() OVER (order by r.sysId, r.roleId) AS rownum, r.serno, s.sysName as system, r.category, r.roleId, r.roleName as name, r.status, CASE WHEN isDeptRole='1' THEN '是' ELSE '否' END AS isDeptRole "
			);
		sql.append("from RoleProf r left join SysProf s on r.sysId = s.sysId where 1 = 1 ");
		
		if (params.containsKey("sysId")) {
			if (params.containsKey("isSuperAdmin")) {
				sql.append("and r.sysId in ('ALL', :sysId) ");
			}
			else {
				sql.append("and r.sysId in (:sysId) ");
			}
		}
		else {
			sql.append("and r.sysId = 'ALL' ");
		}
		if (params.containsKey("roleId")) {
			sql.append("and r.roleId = :roleId ");
		}
		if (params.containsKey("name")) {
			sql.append("and r.roleName like :name ");
		}
		if (params.containsKey("isSuperAdmin")) {
			params.remove("isSuperAdmin");
		}
		else {
			sql.append("and (category is NULL or category = '' or category in ('NonFlow')) ");
		}
		return findListMapPageResultBySQL_map(sql.toString(), page, pageSize, params);
	}

	@Override
	public List<Map<String, Object>> findRolesNotInProjRole(String sysId, String projId) {
		StringBuffer sql = new StringBuffer();
		sql.append(" select r.roleId + '(' + r.roleName + ')' as name, r.roleId as value ");
		sql.append(" from RoleProf r ");
		sql.append(" where sysId in('ALL', ?) ");
		sql.append(" and not exists( ");
		sql.append(" select pr.roleId ");
		sql.append(" from ProjRole pr ");
		sql.append(" where pr.roleId = r.roleId ");
		sql.append(" and pr.projId = ? ");
		sql.append(" ) ");		
		sql.append(" and r.isDeptRole = 0 "); //2018/04/10 Sephiro : 只限非部門角色才能加入專案
		sql.append(" and r.category = 'MemType' ");
		sql.append(" and r.status = 'Y' ");
		return findListMapBySQL(sql.toString(), sysId, projId);
	}

	@Override
	public List<Map<String, Object>> findRolesInProjRole(String sysId, String projId) {
		StringBuffer sql = new StringBuffer();
		sql.append(" select r.roleId + '(' + r.roleName + ')' as name, r.roleId as value ");	
		sql.append(" from RoleProf r ");
		sql.append(" where sysId in('ALL', ?) ");
		sql.append(" and r.roleId IN( ");	
		sql.append(" select pr.roleId ");	
		sql.append(" from ProjRole pr ");	
		sql.append(" where pr.roleId = r.roleId ");	
		sql.append(" and pr.projId = ? ");
		sql.append(" ) ");		
		sql.append(" and r.isDeptRole = 0 "); //2018/04/10 Sephiro : 只限非部門角色才能加入專案
		sql.append(" and r.category = 'MemType' ");
		sql.append(" and r.status = 'Y' ");
		return findListMapBySQL(sql.toString(), sysId, projId);
	}

	@Override
	public List<Map<String, Object>> findRolesNotInRoleIds(String sysId, String roleIds, String deptId, int mode) {
		StringBuffer sql = new StringBuffer();
		sql.append(" select roleId as value, IIF(isDeptRole = '1', '*', '') + roleId + '(' + roleName  + ')' as name ");
		sql.append(" from RoleProf r ");
		
		List<Object> params = new ArrayList<Object>();
		
		sql.append(" where sysId in('ALL', ?) ");
		params.add(sysId);
		
		if (mode == 1) { // (部門)階層角色(AuthFlow)
			sql.append(" and category = 'AuthFlow' ");
		}
		else if (mode == 2) { // (部門)組織角色(OrgType)及職類管理員
			sql.append(" and category in ('OrgType', 'JobMgr') ");
			if (deptId != null) {
				sql.append(" and exists(select dr.roleId from DeptRole dr where dr.roleId = r.roleId and dr.deptId = ? ) ");
				params.add(deptId);
			}
		}
		else if (mode == 3) { // 專案角色(MemType)
			sql.append(" and category = 'MemType' ");
		}
		else {
			sql.append(" and (category is null or category = '' or category not in ('AuthFlow','OrgType','JobMgr','MemType')) ");
		}
		sql.append(" and (category is null or category = '' or category not in ('NonFlow','SuperAdmin','Sample')) ");
		
		if(roleIds != null) {
			String[] roleIdAry = roleIds.split(",");
			if(roleIdAry.length > 0) {
				sql.append(" and roleId not in(");
				for(int i=0; i<roleIdAry.length; i++) {
					if(i != roleIdAry.length-1) { 
						sql.append("?,");
					}else { //最後一個後面不用逗號
						sql.append("?");
					}
					params.add(roleIdAry[i]);
				}
				sql.append(")");
			}
		}
		sql.append(" and status = 'Y' ");
		return findListMapBySQL(sql.toString(), params.toArray());
	}

	@Override
	public List<Map<String, Object>> findRolesInRoleIds(String sysId, String roleIds, String deptId, int mode) {
		StringBuffer sql = new StringBuffer();
		sql.append(" select roleId as value, IIF(isDeptRole = '1', '*', '') + roleId + '(' + roleName  + ')' as name ");
		sql.append(" from RoleProf r ");
		
		List<Object> params = new ArrayList<Object>();
		
		sql.append(" where sysId in('ALL', ?) ");
		params.add(sysId);
		
		if (mode == 1) { // (部門)階層角色(AuthFlow)
			sql.append(" and category = 'AuthFlow' ");
		}
		else if (mode == 2) { // (部門)組織角色(OrgType)及職類管理員
			sql.append(" and category in ('OrgType', 'JobMgr') ");
			if (deptId != null) {
				sql.append(" and exists(select dr.roleId from DeptRole dr where dr.roleId = r.roleId and dr.deptId = ? ) ");
				params.add(deptId);
			}
		}
		else if (mode == 3) { // 專案角色(MemType)
			sql.append(" and category = 'MemType' ");
		}
		else {
			sql.append(" and (category is null or category = '' or category not in ('AuthFlow','OrgType','JobMgr','MemType')) ");
		}
		sql.append(" and (category is null or category = '' or category not in ('NonFlow','SuperAdmin','Sample')) ");
		
		if(roleIds != null) {
			String[] roleIdAry = roleIds.split(",");
			if(roleIdAry.length > 0) {
				sql.append(" and roleId in(");
				for(int i=0; i<roleIdAry.length; i++) {
					if(i != roleIdAry.length-1) { 
						sql.append("?,");
					}else { //最後一個後面不用逗號
						sql.append("?");
					}
					params.add(roleIdAry[i]);
				}
				sql.append(")");
			}
		}
		sql.append(" and status = 'Y' ");
		return findListMapBySQL(sql.toString(), params.toArray());
	}

	@Override
	public Integer countDeptRole(String[] asRoleAry) {
		StringBuffer sql = new StringBuffer();
		sql.append(" select count(*) as count from RoleProf where 1=1 ");
		List<Object> params = new ArrayList<Object>();
		if(asRoleAry.length > 0) {
			sql.append(" and roleId in(");
			for(int i=0; i<asRoleAry.length; i++) {
				if(i != asRoleAry.length-1) { 
					sql.append("?,");
				}else { //最後一個後面不用逗號
					sql.append("?");
				}
				params.add(asRoleAry[i]);
			}
			sql.append(")");
		}		
		sql.append(" and isDeptRole = '1' ");
		List<Map<String, Object>> list = findListMapBySQL(sql.toString(), params.toArray());
		Map<String, Object> map = list.get(0);
		Integer countDeptRole = (Integer)map.get("count");
		return countDeptRole;
	}
	
	@Override
	public PageResult findRoleDepts(Map<String, Object> params, Integer page, Integer pageSize) {
		StringBuilder sql = new StringBuilder(
				"select ROW_NUMBER() OVER (order by dr.deptId, dr.roleId) AS rownum, dr.deptId, dr.roleId, d.deptName, r.roleName "
			);
		sql.append("from DeptRole dr join DeptProf d on dr.deptId = d.deptId join RoleProf r on dr.roleId = r.roleId where 1 = 1 ");
		
		if (params.containsKey("sysId")) {
			sql.append("and r.sysId in ('ALL', :sysId) ");
		}
		else {
			sql.append("and r.sysId = 'ALL' ");
		}
		if (params.containsKey("upDeptId")) {
			sql.append("and (d.upDeptId = :upDeptId or d.deptId = :upDeptId) ");
		}
		if (params.containsKey("deptId")) {
			sql.append("and dr.deptId = :deptId ");
		}
		if (params.containsKey("roleId")) {
			sql.append("and dr.roleId = :roleId ");
		}
		if (params.containsKey("userId")) {
			sql.append("and exists(select udr.serNo from UserDeptRole udr where udr.userId = :userId and udr.deptId = dr.deptId and udr.roleId = dr.roleId) ");
		}
		if (params.containsKey("isSuperAdmin")) {
			params.remove("isSuperAdmin");
		}
		else {
			sql.append("and (r.category in ('JobMgr')) ");
		}
		return findListMapPageResultBySQL_map(sql.toString(), page, pageSize, params);
	}
	
}
