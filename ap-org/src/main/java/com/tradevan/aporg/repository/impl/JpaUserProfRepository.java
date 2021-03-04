package com.tradevan.aporg.repository.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import com.tradevan.apcommon.bean.PageResult;
import com.tradevan.apcommon.persistence.JpaGenericRepository;
import com.tradevan.aporg.model.RoleProf;
import com.tradevan.aporg.model.UserProf;
import com.tradevan.aporg.repository.UserProfRepository;

/**
 * Title: JpaUserRepository<br>
 * Description: <br>
 * Company: Tradevan Co.<br>
 * 
 * @author Neil Chen
 * @version 1.4.2
 */
@Repository
public class JpaUserProfRepository extends JpaGenericRepository<UserProf, Long> implements UserProfRepository {

	@Value("${sql.selectAppend}")
	protected String sqlSelectAppend;
	
	@Override
	public PageResult findUsers(Map<String, Object> params, Integer page, Integer pageSize) {
		StringBuilder sql = new StringBuilder(
				"select ROW_NUMBER() OVER (order by u.sysId, u.userId) AS rownum, u.serno, s.sysName as system, u.userId, u.userName as name, u.deptId, u.status, u.email, u.onBoardDate, u.leaveDate "
			);
		sql.append("from UserProf u left join SysProf s on u.sysId = s.sysId where 1 = 1 ");
		
		if (params.containsKey("sysId")) {
			sql.append("and u.sysId in ('ALL', :sysId) ");
		}
		else {
			sql.append("and u.sysId = 'ALL' ");
		}
		if (params.containsKey("userId")) {
			sql.append("and u.userId like :userId ");
		}
		if (params.containsKey("name")) {
			sql.append("and u.userName like :name ");
		}
		if (params.containsKey("division")) {
			sql.append("and u.deptId = :division ");
		} 
		else {
			if (params.containsKey("department")) {
				sql.append("and u.deptId = :department ");
			}
		}
		if (params.containsKey("isSuperAdmin")) {
			params.remove("isSuperAdmin");
		}
		else {
			sql.append("and userId <> 'admin' ");
		}
		return findListMapPageResultBySQL_map(sql.toString(), page, pageSize, params);
	}

	@Override
	public List<Map<String, Object>> findUsers(Map<String, Object> params) {
		StringBuffer sql = new StringBuffer();
		
		sql.append("SELECT u.userId ");//-- 帳號
		sql.append("      ,u.userName ");//-- 名稱
		sql.append("  FROM UserProf u ");//
		sql.append(" WHERE 1=1 ");
		if (params.containsKey("deptId")) {
			sql.append("   AND u.deptId = :deptId ");//-- Optional查詢條件(部門單位)
		}
		if (params.containsKey("trainingOrgSerNo")) {
			sql.append("   AND u.trainingOrgSerNo = :trainingOrgSerNo ");//-- Optional查詢條件(送訓機構)
		}
		if (params.containsKey("jobTypeSerNo")) {
			sql.append("   AND u.jobTypeSerNo = :jobTypeSerNo ");//-- Optional查詢條件(職類)
		}
		if (params.containsKey("jobRankSerNo")) {
			sql.append("   AND u.jobRankSerNo = :jobRankSerNo ");//-- Optional查詢條件(職級)
		}
		if (params.containsKey("jobTitleSerNo")) {
			sql.append("   AND u.jobTitleSerNo = :jobTitleSerNo ");//-- Optional查詢條件(職稱)
		}
		if (params.containsKey("userId")) {
			sql.append("   AND u.userId IN (:userId) ");//-- Optional查詢條件(員工編號)
		}
		if (params.containsKey("userName")) {
			sql.append("   AND u.userName = :userName ");//-- Optional查詢條件(姓名)
		}
		if (params.containsKey("beginDate")) {
			sql.append("   AND u.onBoardDate BETWEEN CONVERT(datetime, :beginDate) AND CONVERT(datetime, :endDate) ");//-- Optional查詢條件(到職日期)
		}
		//issue 369：鎖定只查在職人員/離職人員(by Sephiro)
		if(params.containsKey("status")) {
			sql.append("   AND u.status = :status ");
		}
		//issue 372：鎖定只查院內/院外人員(by Sephiro)
		if(params.containsKey("userType")) {
			sql.append("   AND u.userType = :userType ");
		}		
		sql.append(" ORDER BY u.userId ");//	
		
		return findListMapBySQL_map(sql.toString(), params);
	}
	
	@Override
	public boolean isRoleInUse(RoleProf role) {
		return convert2Long(getByJPQL("select count(*) from UserProf u where ? member of u.roles", role)) > 0 ? true : false;
	}

	@Override
	public UserProf getUserByUserId(String userId) {
		return getEntityBySQL("select * from XAUTH_USERS where user_Id = ? " + sqlSelectAppend, userId);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<String> findUserDeptIds(String userId) {
		List<String> rtnList = new ArrayList<String>();
		String mainDeptId = (String) getBySQL("select deptId from UserProf where userId = ?", userId);
		if (mainDeptId != null) {
			rtnList.add(mainDeptId);
		}
		rtnList.addAll(
			findBySQL(
				"select ud.deptId from UserDept ud inner join DeptProf d on ud.deptId = d.deptId where ud.userId = ? and d.status = 'Y'", userId)
		);
		return rtnList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<String> findUserRoleIds(String sysId, String userId) {
		return findBySQL(
				"select ur.roleId from UserRole ur inner join RoleProf r on ur.roleId = r.roleId where ur.userId = ? and r.sysId in ('ALL', ?) and r.status = 'Y'", 
				userId, sysId);
	}
	
	@Override
	public List<Map<String, Object>> findNotAssignedUsersByConditions(Map<String, Object> params){
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT uf.userId + '(' + uf.userName + ')' as name, uf.userId ");
		sql.append(" FROM UserProf uf ");
		sql.append(" WHERE uf.status = 'Y' AND NOT EXISTS( ");
		sql.append(" SELECT ur.userId from UserRole ur ");
		sql.append(" WHERE uf.userId = ur.userId "); 
		if(params.containsKey("roleId")) sql.append(" AND ur.roleId = :roleId "); 
		if(params.containsKey("sysId")) sql.append(" AND uf.sysId in ('ALL', :sysId) "); else sql.append(" AND uf.sysId = 'ALL' ");
		sql.append(" ) "); 
		if(params.containsKey("sysId")) sql.append(" AND uf.sysId in ('ALL', :sysId) "); else sql.append(" AND uf.sysId = 'ALL' ");
		if(params.containsKey("deptId")) sql.append(" AND uf.deptId = :deptId ");
		if(params.containsKey("userId")) sql.append(" AND uf.userId like :userId ");
		if(params.containsKey("userName")) sql.append(" AND uf.userName like :userName ");
		sql.append(" order by uf.userType, uf.userId ");
		return findListMapBySQL_map(sql.toString(), params);
	}
	
	@Override
	public List<Map<String, Object>> findAssignedUsersByConditions(Map<String, Object> params) {
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT uf.userId + '(' + uf.userName + ')' as name, uf.userId  FROM UserProf uf ");  
		sql.append(" WHERE uf.status = 'Y' AND uf.userId IN( ");  
		sql.append(" SELECT ur.userId "); 
		sql.append(" from UserRole ur ");  
		sql.append(" WHERE uf.userId = ur.userId ");  
		if(params.containsKey("roleId")) sql.append(" AND ur.roleId = :roleId "); 
		if(params.containsKey("sysId")) sql.append(" AND uf.sysId in ('ALL', :sysId) "); else sql.append(" AND uf.sysId = 'ALL' ");
		sql.append(" ) ");    
		if(params.containsKey("sysId")) sql.append(" AND uf.sysId in ('ALL', :sysId) "); else sql.append(" AND uf.sysId = 'ALL' ");
		sql.append(" order by uf.userType, uf.userId ");
		return findListMapBySQL_map(sql.toString(), params);
	}

	@Override
	public List<Map<String, Object>> findUsersNotInUserIds(String userIds) {
		StringBuffer sql = new StringBuffer();
		sql.append(" select userId as value, userId + '(' + userName  + ')' as name ");
		sql.append(" from UserProf ");
		
		sql.append(" where userId <> 'admin' and status = 'Y' ");
		
		List<Object> params = new ArrayList<Object>();
		if(userIds != null) {
			String[] userIdAry = userIds.split(",");
			if(userIdAry.length > 0) {
				sql.append(" and userId not in(");
				for(int i=0; i<userIdAry.length; i++) {
					if(i != userIdAry.length-1) { 
						sql.append("?,");
					}else { //最後一個後面不用逗號
						sql.append("?");
					}
					params.add(userIdAry[i]);
				}
				sql.append(")");
			}
		}
		sql.append(" order by userType, userId ");
		return findListMapBySQL(sql.toString(), params.toArray());
	}

	@Override
	public List<Map<String, Object>> findUsersInUserIds(String userIds) {
		StringBuffer sql = new StringBuffer();
		sql.append(" select u.userId as value, u.userId + '(' + u.userName  + ')' as name ");
		sql.append(" ,u.userId as userId, u.userName as userName, u.deptId as deptId, j.jobTitleName as jobTitle ");
		sql.append(" ,u.email as email, u.phone as phone, u.ext as ext, u.mobileNo as mobileNo ");
		sql.append(" from UserProf u left join JobTitle j on u.jobTitleSerNo = j.serNo ");
		
		sql.append(" where u.userId <> 'admin' ");
		
		List<Object> params = new ArrayList<Object>();
		if(userIds != null) {
			String[] userIdAry = userIds.split(",");
			if(userIdAry.length > 0) {
				sql.append(" and u.userId in(");
				for(int i=0; i<userIdAry.length; i++) {
					if(i != userIdAry.length-1) { 
						sql.append("?,");
					}else { //最後一個後面不用逗號
						sql.append("?");
					}
					params.add(userIdAry[i]);
				}
				sql.append(")");
			}
		}
		sql.append(" order by u.userType, u.userId ");
		return findListMapBySQL(sql.toString(), params.toArray());
	}
	
	@Override
	public Long getNextUserIdSeq() {
		return convert2Long(getBySQL("SELECT NEXT VALUE FOR Seq_UserId"));
	}
	
	@Override
	public List<UserProf> findUsersBy(String projId, String roleId) {
		String sql = "select u.* from UserProf u where u.userId IN (select upr.userId from UserProjRole upr where upr.projId = ? and upr.roleId = ? and upr.userId = u.userId)"; 		
		return findEntityListBySQL(sql.toString(), projId, roleId);
	}
	
	@Override
	public PageResult fetchUserProfs(Map<String, Object> params, Integer page, Integer pageSize) {
		StringBuffer sql = new StringBuffer();
		sql.append("select ROW_NUMBER() OVER (order by u.userId) AS rownum ");
	    sql.append(", u.userId ");
	    sql.append(", u.userName ");
	    sql.append(", case d.level ");
		sql.append("    when '1' then d.deptName ");
		sql.append("    when '2' then (select deptName from DeptProf where deptId = d.upDeptId) ");
		sql.append("  end as upDeptName ");
	    sql.append(", d.deptName ");
	    sql.append(", j.jobTitleName ");
	    sql.append(", r.jobRankName ");
	    sql.append(", isnull(i.email, u.email) as email ");
	    sql.append(", u.mobileNo ");
		sql.append("	, convert(varchar, convert(date,u.onBoardDate), 111) as onBoardDate ");
		sql.append("	, convert(varchar, convert(date,u.leaveDate), 111) as leaveDate ");
		//issue 325：為了預設臨床教師畫面新增的欄位(by Sephiro)
		sql.append("	, u.serNo ");
		sql.append(", u.deptId ");
	    sql.append("  from UserProf u ");
	    sql.append("  left join Userinfo i on u.userId = i.userId ");
	    sql.append("  left join DeptProf d on u.deptId = d.deptId ");
	    sql.append("  left join JobRank r on u.jobRankSerNo = r.serNo ");
	    sql.append("  left join JobTitle j on u.jobTitleSerNo = j.serNo ");
	    sql.append("  left join JobType y on u.jobTypeSerNo = y.serNo ");
	    sql.append(" where 1 = 1 ");
		if (params.containsKey("sysId")) {
			sql.append("and u.sysId in ('ALL', :sysId) ");
		}
		else {
			sql.append("and u.sysId = 'ALL' ");
		}
		if (params.containsKey("userType")) {
			sql.append("and u.userType = :userType ");
		}
		if (params.containsKey("status")) {
			sql.append("and u.status in ('Y', :status) ");
		}
		if (params.containsKey("userId")) {
			sql.append("and u.userId like :userId ");
		}
		if (params.containsKey("name")) {
			sql.append("and u.userName like :name ");
		}
		if (params.containsKey("jobTitleSerNo")) {
			sql.append("and j.serNo = :jobTitleSerNo ");
		}
		if (params.containsKey("deptId")) {
			sql.append("and u.deptId = :deptId ");
		}
		else {
			if (params.containsKey("upDeptId")) {
				sql.append("and u.deptId = :upDeptId ");
			}
		}
		if (params.containsKey("idCardNo")) {
			sql.append("and u.idCardNo = :idCardNo ");
		}
		if (params.containsKey("userState")) {
			sql.append("and u.userState = :userState ");
		}		
		if(params.containsKey("jobRankId")) {
			sql.append("  and (r.jobRankId like 'V%' or r.jobRankId like 'F%' or r.jobRankId like 'R%' or jobRankId = :jobRankId ");
			sql.append("   or j.jobTitleName like '%住院%' or j.jobTitleName like '%臨床%' or j.jobTitleName like '%主治%') ");
		}
		
	    return findListMapPageResultBySQL_map(sql.toString(), page, pageSize, params);
	}
	
	@Override
	public PageResult fetchActiveUserProfs(Map<String, Object> params, Integer page, Integer pageSize) {
		StringBuffer sql = new StringBuffer();
		sql.append("select ROW_NUMBER() OVER (order by u.userId) AS rownum ");
	    sql.append(", u.userId ");
	    sql.append(", u.userName ");
	    sql.append(", case d.level ");
		sql.append("    when '1' then d.deptName ");
		sql.append("    when '2' then (select deptName from DeptProf where deptId = d.upDeptId) ");
		sql.append("  end as upDeptName ");
	    sql.append(", d.deptName ");
	    sql.append(", j.jobTitleName ");
	    sql.append(", r.jobRankName ");
	    sql.append(", u.deptId as deptId ");
		sql.append(", u.jobTitleSerNo as jobTitleSerNo ");
	    sql.append(", isnull(i.email, u.email) as email ");
	    sql.append(", u.mobileNo ");
	    sql.append(", convert(varchar(10),convert(datetime, u.onBoardDate), 111) as onBoardDate ");
	    sql.append(", convert(varchar(10),convert(datetime, u.leaveDate), 111) as leaveDate ");
	    sql.append("  from UserProf u ");
	    sql.append("  left join Userinfo i on u.userId = i.userId ");
	    sql.append("  left join DeptProf d on u.deptId = d.deptId ");
	    sql.append("  left join JobRank r on u.jobRankSerNo = r.serNo ");
	    sql.append("  left join JobTitle j on u.jobTitleSerNo = j.serNo ");
	    sql.append("  left join JobType y on u.jobTypeSerNo = y.serNo ");
	    sql.append(" where u.userState = :userState  and usertype = :userType ");
		if (params.containsKey("sysId")) {
			sql.append("and u.sysId in ('ALL', :sysId) ");
		}
		else {
			sql.append("and u.sysId = 'ALL' ");
		}
		if (params.containsKey("userId")) {
			sql.append("and u.userId like :userId ");
		}
		if (params.containsKey("name")) {
			sql.append("and u.userName like :name ");
		}
		if (params.containsKey("jobTitleSerNo")) {
			sql.append("and j.serNo = :jobTitleSerNo ");
		}
		if (params.containsKey("deptId")) {
			sql.append("and u.deptId = :deptId ");
		}
		else {
			if (params.containsKey("upDeptId")) {
				sql.append("and u.deptId = :upDeptId ");
			}
		}
		if (params.containsKey("idCardNo")) {
			sql.append("and u.idCardNo = :idCardNo ");
		}

		if(params.containsKey("jobRankId")) {
			sql.append("  and (r.jobRankId like 'V%' or r.jobRankId like 'F%' or r.jobRankId like 'R%' or jobRankId = :jobRankId) ");
		}
		
	    return findListMapPageResultBySQL_map(sql.toString(), page, pageSize, params);
	}
	
	public Map<String, Object> getUserProf(Map<String, Object> params) {
		StringBuffer sql = new StringBuffer();
		sql.append("select i.serNo ");
		sql.append(", u.userId ");
		sql.append(", u.userName ");
		sql.append(", u.engName ");
		sql.append(", u.idCardNo ");
		sql.append(", case when ISNULL(u.birthday,'')<>'' then left(u.birthday,4)+'/'+substring(u.birthday,5,2)+'/'+right(u.birthday,2) else u.birthday end as birthday ");
		sql.append(", case u.gender when 'M' then '男' when 'F' then '女' else u.gender end as gender ");
		sql.append(", d.deptId ");
		sql.append(", case d.level ");
		sql.append("    when '1' then d.deptName ");
		sql.append("    when '2' then (select deptName from DeptProf where deptId = d.upDeptId) ");
		sql.append("  end as upDeptName ");
		sql.append(", d.deptName ");
		sql.append(", j.serNo as jobTitleSerNo ");
		sql.append(", j.jobTitleName ");
		sql.append(", j.jobTitleId ");
		sql.append(", r.serNo as jobRankSerNo ");
		sql.append(", r.jobRankName ");
		sql.append(", r.jobRankId ");
		sql.append(", t.jobTypeId ");
		sql.append(", u.email ");
		sql.append(", i.email as new_email ");
		sql.append(", i.mobileNo as new_mobileNo ");
		sql.append(", i.phone as new_phone ");
		sql.append(", i.ext as new_ext ");
//		sql.append(", i.email as otherEmail ");
		sql.append(", u.mobileNo ");
		sql.append(", u.ext ");
		sql.append(", u.phone ");
		
		sql.append(", i.email as otherEmail ");
		sql.append(", i.phone as otherPhone ");
		sql.append(", i.ext as otherExt ");
		sql.append(", i.mobileNo as otherMobileNo ");
		
		sql.append(", convert(varchar(10),convert(datetime, u.onBoardDate), 111) as onBoardDate");
		sql.append(", convert(varchar(10),convert(datetime, u.leaveDate), 111) as leaveDate");
		sql.append(", case when convert(varchar, u.preEmpDate, 111) = '1911/01/01' then '--' else convert(varchar, u.preEmpDate, 111) end as preEmpDate ");
		sql.append(", convert(varchar, u.createTime, 120) as createTime ");
		sql.append("  from UserProf u ");
		sql.append("  left join Userinfo i on u.userId = i.userId ");
		sql.append("  left join DeptProf d on u.deptId = d.deptId ");
		sql.append("  left join JobRank r on u.jobRankSerNo = r.serNo ");
		sql.append("  left join JobTitle j on u.jobTitleSerNo = j.serNo ");
		sql.append("  left join JobType t on u.jobTypeSerNo = t.serNo ");
		sql.append(" where 1 = 1 ");
		if (params.containsKey("sysId")) {
			sql.append("and u.sysId in ('ALL', :sysId) ");
		}
		else {
			sql.append("and u.sysId = 'ALL' ");
		}
		if (params.containsKey("userId")) {
			sql.append("and u.userId = :userId ");
		}
		if (params.containsKey("idCardNo")) {
			sql.append("and u.idCardNo = :idCardNo ");
		}
		return getMapBySQL_map(sql.toString(), params);
	}
	
	public Map<String, Object> getUsePIrProf(Map<String, Object> params) {
		StringBuffer sql = new StringBuffer();
		sql.append("select i.serNo ");
		sql.append(", u.userId ");
		sql.append(", u.userName ");
		sql.append(", u.engName ");
		sql.append(", udjt.deptId ");
		sql.append(", case d.level ");
		sql.append("    when '1' then d.deptName ");
		sql.append("    when '2' then (select deptName from DeptProf where deptId = d.upDeptId) ");
		sql.append("  end as upDeptName ");
		sql.append(", d.deptName ");
		sql.append(", udjt.serNo as jobTitleSerNo ");
		sql.append(", udjt.jobTitleName ");
		sql.append(", udjt.jobTitleId ");
		sql.append(", r.serNo as jobRankSerNo ");
		sql.append(", r.jobRankName ");
		sql.append(", r.jobRankId ");
		sql.append(", t.jobTypeId ");
		sql.append(", u.email ");
		sql.append(", i.email as new_email ");
		sql.append(", i.mobileNo as new_mobileNo ");
		sql.append(", i.phone as new_phone ");
		sql.append(", i.ext as new_ext ");
//		sql.append(", i.email as otherEmail ");
		sql.append(", u.mobileNo ");
		sql.append(", u.ext ");
		sql.append(", u.phone ");
		
		sql.append(", i.otherEmail ");
		sql.append(", i.otherPhone ");
		sql.append(", i.otherExt ");
		sql.append(", i.otherMobileNo ");
		
		sql.append(", convert(varchar(10),convert(datetime, u.onBoardDate), 111) as onBoardDate");
		sql.append(", convert(varchar(10),convert(datetime, u.leaveDate), 111) as leaveDate");
		sql.append(", convert(varchar, u.createTime, 120) as createTime ");
		sql.append("  from UserProf u ");
		sql.append("  left join Userinfo i on u.userId = i.userId ");
		sql.append("  left join DeptProf d on u.deptId = d.deptId ");
		sql.append("  left join JobRank r on u.jobRankSerNo = r.serNo ");
		sql.append("  left join UserDeptJobTitle udjt on u.userId = udjt.userId ");
		sql.append("  left join JobType t on u.jobTypeSerNo = t.serNo ");
		sql.append(" where udjt.deptId = :deptId ");
		if (params.containsKey("sysId")) {
			sql.append("and u.sysId in ('ALL', :sysId) ");
		}
		else {
			sql.append("and u.sysId = 'ALL' ");
		}
		if (params.containsKey("userId")) {
			sql.append("and u.userId = :userId ");
		}
		if (params.containsKey("idCardNo")) {
			sql.append("and u.idCardNo = :idCardNo ");
		}
		return getMapBySQL_map(sql.toString(), params);
	}

	@Override
	public PageResult fetchPreEmpStat(Map<String, Object> params, Integer page, Integer pageSize) {
		StringBuffer sql = new StringBuffer();
		sql.append(" select ROW_NUMBER() over (order by u.onBoardDate, d1.deptName, ISNULL(u.userId, u.idCardNo)) as rownum ");
		sql.append(" 	  , u.serNo ");
		sql.append(" 	  , convert(varchar(10),convert(datetime, u.onBoardDate), 111) as onBoardDate ");
		sql.append(" 	  , u.sendEmail ");
		sql.append(" 	  , p.paramName ");
		sql.append(" 	  , s.subJobTypeName ");
		sql.append(" 	  , ISNULL(u.userId, u.idCardNo) as userId ");
		//issue 351：查詢結果清單，身份証號/員工編號 欄位，如為職前帳號，直接顯示身份証字號(by Sephiro)
		sql.append(" 	  , case when userId like 'E-%' then idCardNo else ISNULL(u.userId, u.idCardNo) end as userIdNo ");
		sql.append(" 	  , u.userName ");
		sql.append(" 	  , case d1.level when '1' then d1.deptName else d2.deptName end as upDeptName ");
		sql.append(" 	  , d1.deptName ");
		sql.append(" 	  , j.jobTitleName ");
		sql.append(" 	  , ISNULL(case when convert(varchar,u.icHalfRealDate,111) = '1911/01/01' then '--' else convert(varchar,u.icHalfRealDate,111) end, '') as icHalfRealDate ");
		sql.append(" 	  , ISNULL(case when convert(varchar,u.ic2HrDate,111) = '1911/01/01' then '--' else convert(varchar,u.ic2HrDate,111) end, '') as ic2HrDate ");
		sql.append(" 	  , ISNULL(case when convert(varchar,u.ic6HrDate,111) = '1911/01/01' then '--' else convert(varchar,u.ic6HrDate,111) end, '') as ic6HrDate ");
		//issuee 462：增加「職前網路課程完成日期」欄位(by Sephiro)
		sql.append(" 	  , ISNULL(case when convert(varchar,u.preEmpDate,111) = '1911/01/01' then '--' else convert(varchar,u.preEmpDate,111) end, '') as preEmpDate ");
		sql.append(" 	  , case ");
		sql.append(" 	  	  when convert(varchar,u.ic6HrDate,111) = '1911/01/01' then '--' ");
		sql.append(" 	  	  when datediff(month, u.onBoardDate, u.ic6HrDate) <= 6 then 'Y' ");
		sql.append(" 	  	  else 'N' ");
		sql.append(" 	  	end as halfYear ");
		sql.append("   from UserProf u ");
		sql.append("   left join EduParam p on u.preEmpCode=p.name1 and p.groupId='PRE_EMP_COURSE' ");
		sql.append("   left join subJobType s on u.subJobTypeSerNo=s.serNo ");
		sql.append("   left join DeptProf d1 on u.deptId=d1.deptId ");
		sql.append("   left join DeptProf d2 on d1.upDeptId=d2.deptId ");
		sql.append("   left join JobTitle j on u.jobTitleSerNo=j.serNo ");
		sql.append("  where 1 = 1 and u.userId not like 'C-%' and u.userId not like 'R-%' ");
		
		if (params.containsKey("isAdmin")) {
			params.remove("isAdmin");
		} else {
			sql.append(" AND (u.userId = :loginUserId ");
			sql.append("  OR EXISTS (SELECT udr.serNo FROM UserDeptRole udr WHERE udr.userId = '").append(params.get("loginUserId")).append("' AND udr.deptId in (d1.deptId, d1.upDeptId) AND udr.roleId IN ('LEADER', 'supervisor', 'JOB_MGR')) ) ");
		}
		
		if (params.containsKey("startTime") && params.containsKey("endTime")) {
			sql.append(" and convert(datetime,u.onBoardDate,111) between convert(datetime,:startTime,111) and convert(datetime,:endTime,111) ");
		}
		
		if (params.containsKey("inputUserId")) {
			sql.append(" and u.userId like :inputUserId ");
		}
		
		if (params.containsKey("idCardNo")) {
			sql.append(" and u.idCardNo like :idCardNo ");
		}
		
		if (params.containsKey("deptId")) { //--單位
	    	sql.append(" and u.deptId = :deptId ");
	    }
		
		if (params.containsKey("upDeptId")) { //--部門
			sql.append(" and (u.deptId = :upDeptId or d1.upDeptId = :upDeptId) ");
		}
		
		if (params.containsKey("userId")) { //--員工編號
	    	 sql.append("  and (u.userId = :userId) ");
	    }
		
		if (params.containsKey("onBoard") && params.containsKey("resigned")) {
			sql.append(" and u.status in (:onBoard, :resigned) ");
		}
		else if (params.containsKey("onBoard")) {
			sql.append(" and u.status = :onBoard ");
		}
		else if (params.containsKey("resigned")) {
			sql.append(" and u.status = :resigned ");
		}
		
		if (params.containsKey("preEmpY") && params.containsKey("preEmpN")) {
//			sql.append(" and 'Y' = :preEmpY and 'N' = :preEmpN ");
			params.remove("preEmpY");
			params.remove("preEmpN");
		}
		else if (params.containsKey("preEmpY")) {
			params.remove("preEmpY");
			sql.append(" and ISNULL(u.preEmpDate, '')<>'' ");
		}
		else if (params.containsKey("preEmpN")) {
			params.remove("preEmpN");
			sql.append(" and ISNULL(u.preEmpDate, '')='' ");
		}
		
		if (params.containsKey("realY") && params.containsKey("realN")) {
//			sql.append(" and 'Y' = :realY and 'N' = :realN ");
			params.remove("realY");
			params.remove("realN");
		}
		else if (params.containsKey("realY")) {
			params.remove("realY");
			sql.append(" and ISNULL(u.icHalfRealDate, '')<>'' ");
		}
		else if (params.containsKey("realN")) {
			params.remove("realN");
			sql.append(" and ISNULL(u.icHalfRealDate, '')='' ");
		}
		
		if (params.containsKey("icEmpY") && params.containsKey("icEmpN")) {
//			sql.append(" and 'Y' = :icEmpY and 'N' = :icEmpN ");
			params.remove("icEmpY");
			params.remove("icEmpN");
		}
		else if (params.containsKey("icEmpY")) {
			params.remove("icEmpY");
			sql.append(" and ISNULL(u.ic2HrDate, '')<>'' ");
		}
		else if (params.containsKey("icEmpN")) {
			params.remove("icEmpN");
			sql.append(" and ISNULL(u.ic2HrDate, '')='' ");
		}
		
		if (params.containsKey("sensingY") && params.containsKey("sensingN")) {
//			sql.append(" and 'Y' = :sensingY and 'N' = :sensingN ");
			params.remove("sensingY");
			params.remove("sensingN");
		}
		else if (params.containsKey("sensingY")) {
			params.remove("sensingY");
			sql.append(" and ISNULL(u.ic6HrDate, '')<>'' ");
		}
		else if (params.containsKey("sensingN")) {
			params.remove("sensingN");
			sql.append(" and ISNULL(u.ic6HrDate, '')='' ");
		}
		
		return findListMapPageResultBySQL_map(sql.toString(), page, pageSize, params);
	}
	
	@Override
	public List<UserProf> fetchUserprof(String sysId, String deptId, String userId) {
		if(StringUtils.isNotBlank(deptId)) {
			StringBuilder sql = new StringBuilder();
			sql.append(" select u from UserProf u where userState = 'A' and status = 'Y' and sysId in ('ALL', ?) and deptId = ? ");
			return findEntityListByJPQL(sql.toString(), sysId, deptId);			
		}
		else if(StringUtils.isNotBlank(userId)) {
			StringBuilder sql = new StringBuilder();
			sql.append(" select u from UserProf u where userState = 'A' and status = 'Y' and sysId in ('ALL', ?) and userId = ? ");
			return findEntityListByJPQL(sql.toString(), sysId, userId);						
		}
		return null;
	}
	
	@Override
	public UserProf getUserprof(String sysId, String userId) {
		StringBuilder sql = new StringBuilder();
		sql.append(" select u from UserProf u where status = 'Y' and sysId in ('ALL', ?) and userId = ? ");
		return getEntityByJPQL(sql.toString(), sysId, userId);			
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<String> findUserEmailByRoleId(String roleId) {
		StringBuilder sql = new StringBuilder();
		sql.append("select u.email from UserProf u where userState = 'A' and status = 'Y' ");
		sql.append("and exists (select * from UserRole ur where ur.userId = u.userId and ur.roleId = ?) ");
		return findBySQL(sql.toString(), roleId);
	}
	
	@Override
	public Map<String, Object> findPIUserInfo(Map<String, Object> params){
		StringBuilder sql = new StringBuilder();
		sql.append("select pr.userId, u.userName ");
		sql.append("from UserProjRole pr ");
		sql.append("left join UserProf u on pr.userId = u.userId ");
		sql.append("where pr.projId = :projId and pr.roleId = 'PI'");
		return getMapBySQL_map(sql.toString(), params);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<String> fetchEmailByRoleId(String roleId) {
		StringBuilder sql = new StringBuilder();
		sql.append("select ISNULL(i.email, u.email) as email ");
		sql.append("  from UserProf u ");
		sql.append("  left join UserInfo i on u.userId=i.userId ");
		sql.append("  left join UserRole r on u.userId=r.userId ");
		sql.append(" where r.roleId = ? ");
		sql.append("   and ISNULL(ISNULL(i.email, u.email),'')<>'' ");
		return findBySQL(sql.toString(), roleId);
	}
}
