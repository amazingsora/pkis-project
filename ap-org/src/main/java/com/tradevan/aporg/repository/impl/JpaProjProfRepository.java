package com.tradevan.aporg.repository.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.tradevan.apcommon.bean.PageResult;
import com.tradevan.apcommon.persistence.JpaGenericRepository;
import com.tradevan.aporg.model.ProjProf;
import com.tradevan.aporg.repository.ProjProfRepository;

/**
 * Title: JpaProjProfRepository<br>
 * Description: <br>
 * Company: Tradevan Co.<br>
 * 
 * @author Neil Chen
 * @version 1.1
 */
@Repository
public class JpaProjProfRepository extends JpaGenericRepository<ProjProf, Long> implements ProjProfRepository {

	@Override
	public String getMaxProjIdSerial(String prefixVal, String dateFmtVal, int serialLen) {
		
		int len = prefixVal.length() + dateFmtVal.length();
		
		String serial = (String) getByJPQL(
				"select max(substring(p.projId, " + (len+1) + ", " + serialLen + ")) from ProjProf p where p.projId like ? ", 
				prefixVal + dateFmtVal + "%");
		
		return serial;
	}
	
	@Override
	public String getMaxRschProjIdSerial(String year, String organCode, String projType, String hostType, int serialLen) {
		
		int len = year.length() + organCode.length() + projType.length() + hostType.length();
		
		String serial = (String) getByJPQL(
				"select max(substring(p.projId, " + (len+1) + ", " + serialLen + ")) from ProjProf p where p.projId like ? ", 
				year + organCode + projType + hostType + "%");
		
		return serial;
	}
	
	@Override
	public String getMaxEduCourseIdSerial(String prefixVal, String dateFmtVal, int serialLen) {
		
		int len = prefixVal.length() + dateFmtVal.length();
		
		String serial = (String) getByJPQL(
				"select max(substring(p.courseId, " + (len+1) + ", " + serialLen + ")) from EduCourse p where p.courseId like ? ", 
				prefixVal + dateFmtVal + "%");
		
		return serial;
	}
	
	@Override
	public String getMaxEduClassIdSerial(String courseId) {
		
		String serial = (String) getBySQL(
				"select max(substring(p.classId, " + (courseId.length()+2) + ", " + 1 + ")) from EduCourseClass p where p.courseId = ? ", 
				courseId);
		
		return serial;
	}
	
	@Override
	public String getMaxCoprApplyNoSerial(String sequenceName) {
		
		String serial = String.valueOf(getBySQL("SELECT NEXT VALUE FOR " + sequenceName));
		
		return serial;
	}
	
	@Override
	public String getMaxBulletinNo(String dateFmtVal, int serialLen) {
		
		int len = dateFmtVal.length();
		
		String serial = (String) getByJPQL(
				"select max(substring(p.bulletinNo, " + (len+1) + ", " + serialLen + ")) from Bulletin p where p.bulletinNo like ? ", 
				dateFmtVal + "%");
		
		return serial;
	}
	
	@Override
	public PageResult findProjs(Map<String, Object> params, Integer page, Integer pageSize) {
		StringBuffer sql = new StringBuffer();
		sql.append(" select ROW_NUMBER() OVER (order by p.sysId, p.projId) AS rownum ");
		sql.append(" , p.serNo ");
		sql.append(" , s.sysName as system ");
		sql.append(" , p.projId ");
		sql.append(" , p.projName as name ");
		sql.append(" , p.sysId ");
		sql.append(" , p.status ");
		sql.append(" from ProjProf p ");
		sql.append(" left join SysProf s on p.sysId = s.sysId "); 
		sql.append(" where 1 = 1 ");		
		if(params.containsKey("sysId")) sql.append(" and p.sysId in ('ALL', :sysId) "); else sql.append(" and p.sysId = 'ALL' ");
		if(params.containsKey("projId")) sql.append(" and p.projId = :projId ");
		if(params.containsKey("name")) sql.append(" and p.projName like :name ");
		return findListMapPageResultBySQL_map(sql.toString(), page, pageSize, params);
	}

	@Override
	public List<Map<String, Object>> listProjAuth(String projId) {
		StringBuffer sql = new StringBuffer();
		sql.append(" select t.* ");
		sql.append(" , substring(t.userList, 1, LEN(t.userList)-1) as userName ");
		sql.append(" from ");
		sql.append(" (select pa.projId ");  
		sql.append(" , pa.authId ");  
		sql.append(" , a.serNo ");  
		sql.append(" , a.authName as name ");
		sql.append(" , a.sysId ");  
		sql.append(" , a.status ");  
		sql.append(" , s.sysName as system "); 
		sql.append(" , (select CAST(u.userName AS VARCHAR) + ',' "); 
		sql.append(" from UserProjAuth as upa ");
		sql.append(" left join UserProf u on u.userId = upa.userId "); 
		sql.append(" where upa.projId = ? ");
		sql.append(" and upa.authId = a.authId  ");
		sql.append(" FOR XML PATH( '') ");
		sql.append(" ) as userList ");
		sql.append(" from ProjAuth pa left join AuthProf a on a.authId = pa.authId "); 
		sql.append(" left join SysProf s on s.sysId = a.sysId ");  
		sql.append(" where pa.projId = ? ) t ");
		return findListMapBySQL(sql.toString(), projId, projId);
	}

	@Override
	public List<Map<String, Object>> listProjRole(String projId) {
		StringBuffer sql = new StringBuffer();
		sql.append(" select t.* ");	
		sql.append(" , substring(t.userList, 1, LEN(t.userList)-1) as userName ");	 
		sql.append(" from ");	
		sql.append(" (select pr.projId ");	  
		sql.append(" , pr.roleId ");	  
		sql.append(" , r.serNo ");	  
		sql.append(" , r.roleName as name ");	
		sql.append(" , r.sysId ");	  
		sql.append(" , r.status ");	  
		sql.append(" , s.sysName as system ");	 
		sql.append(" , (select CAST(u.userName AS VARCHAR) + ',' ");	 
		sql.append(" from UserProjRole as upr ");	
		sql.append(" left join UserProf u on u.userId = upr.userId ");	 
		sql.append(" where upr.projId = ? ");	
		sql.append(" and upr.roleId = r.roleId ");	 
		sql.append(" FOR XML PATH( '') ");	
		sql.append(" ) as userList ");	
		sql.append(" from ProjRole pr ");	 
		sql.append(" left join RoleProf r on r.roleId = pr.roleId ");	
		sql.append(" left join SysProf s on s.sysId = r.sysId ");	
		sql.append(" where pr.projId = ? ) t ");		
		return findListMapBySQL(sql.toString(), projId, projId);
	}
	
}
