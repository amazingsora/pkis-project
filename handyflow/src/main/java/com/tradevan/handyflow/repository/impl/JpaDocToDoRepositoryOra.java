package com.tradevan.handyflow.repository.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.tradevan.apcommon.persistence.JpaGenericRepository;
import com.tradevan.apcommon.util.StringUtil;
import com.tradevan.handyflow.model.form.DocToDo;
import com.tradevan.handyflow.repository.DocToDoRepository;

/**
 * Title: JpaDocToDoRepository<br>
 * Description: <br>
 * Company: Tradevan Co.<br>
 * 
 * @author Neil Chen
 * @version 1.1.1
 */
@Repository
public class JpaDocToDoRepositoryOra extends JpaGenericRepository<DocToDo, Long> implements DocToDoRepository {
	
	@Override
	public String getMaxToDoNoSerial(String formId, String prefixVal, String dateFmtVal, int serialLen) {
		
		int len = prefixVal.length() + dateFmtVal.length();
		
		String serial = (String) getByJPQL(
				"select max(substring(t.toDoNo, " + (len+1) + ", " + serialLen + ")) from DocToDo t join t.formConf f where f.formId = ? and t.toDoNo like ? and t.serialNo = 0", 
				formId, prefixVal + dateFmtVal + "%");
		
		return serial;
	}
	
	@Override
	public List<DocToDo> findOwnedListByUser(String userId, String formId, String sysId, boolean sortDesc) {
		
		String jpql = "select t from DocToDo t join fetch t.formConf left join fetch t.createUser where t.userId = :userId and t.status = 'W' " + 
				((formId != null) ? "and t.formId = :formId " : "") + (StringUtil.isNotBlank(sysId) ? "and t.sysId in ('ALL', :sysId) " : "") +
				"order by t.formId, t.toDoNo " + (sortDesc ? "desc" : "");
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userId", userId);
		if (formId != null) {
			params.put("formId", formId);
		}
		if (StringUtil.isNotBlank(sysId)) {
			params.put("sysId", sysId);
		}
		return findEntityListByJPQL_map(jpql, params);
	}
	
	@Override
	public int updateStatusByProjIdAndSysId(String projId,String sysId,String newStatus) {
		String sql = "UPDATE DocToDo SET oldStatus = status, status = ? WHERE sysId = ? AND projId = ? ";
		return updateBySQL(sql,newStatus,sysId,projId);
	}
	
	@Override
	public int restoreStatusByProjIdAndSysId(String projId,String sysId) {
		String sql = "UPDATE docToDo SET status = oldStatus WHERE sysId = ? AND projId = ? AND oldStatus IS NOT NULL";
		return updateBySQL(sql,sysId,projId);
	}
	
	@Override
	public int updateStatusByProjIdAndSysIdAndStatus(String projId,String sysId,String status,String newStatus) {
		String sql = "UPDATE DocToDo SET oldStatus = status, status = ? WHERE sysId = ? AND projId = ? AND status = ? ";
		return updateBySQL(sql,newStatus,sysId,projId,status);
	}
}
