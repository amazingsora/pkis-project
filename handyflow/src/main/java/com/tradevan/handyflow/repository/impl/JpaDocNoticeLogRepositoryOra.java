package com.tradevan.handyflow.repository.impl;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.tradevan.apcommon.persistence.JpaGenericRepository;
import com.tradevan.handyflow.model.form.DocNoticeLog;
import com.tradevan.handyflow.repository.DocNoticeLogRepository;

/**
 * Title: JpaDocNoticeLogRepository<br>
 * Description: <br>
 * Company: Tradevan Co.<br>
 * 
 * @author Neil Chen
 * @version 1.0.3
 */
@Repository("jpaDocNoticeLogRepositoryOra")
public class JpaDocNoticeLogRepositoryOra extends JpaGenericRepository<DocNoticeLog, Long> implements DocNoticeLogRepository {

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public int updateStatus2Y(String userId, String sysId) {
		return updateBySQL("UPDATE DocNoticeLog SET status = 'Y', updateTime = SYSTIMESTAMP WHERE userId = ? AND sysId = ? AND status = 'N'", userId, sysId);
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public int updateStatusWithDocToDoId(String status, String sysId, Long docToDoId) {
		return updateBySQL("UPDATE DocNoticeLog SET status = ?, updateTime = SYSTIMESTAMP WHERE sysId = ? AND "
				+ "applyNo = (SELECT toDoNo FROM DocToDo Where serNo = ?) AND serialNo = (SELECT serialNo FROM DocToDo Where serNo = ?)"
				, status, sysId, docToDoId, docToDoId);
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public int updateStatus(String status, String sysId, String applyNo, Integer serialNo) {
		String sql = "UPDATE DocNoticeLog SET status = ?, updateTime = SYSTIMESTAMP WHERE sysId = ? AND applyNo = ? "
				+ ((serialNo != null) ? "AND serialNo = ? " : "") + "AND status <> ?";
		if (serialNo != null) {
			return updateBySQL(sql, status, sysId, applyNo, serialNo, status);
		}
		else {
			return updateBySQL(sql, status, sysId, applyNo, status);
		}
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public List fetchNeedNoticeLogs() {
		return findBySQL("SELECT DISTINCT g.sysId, u.email FROM DocNoticeLog g JOIN  UserProf u ON g.userId = u.userId WHERE g.status = 'N' AND u.status = 'Y' AND u.email IS NOT NULL");
	}

}
