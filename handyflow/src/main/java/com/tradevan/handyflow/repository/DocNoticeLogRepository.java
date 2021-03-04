package com.tradevan.handyflow.repository;

import java.util.List;

import com.tradevan.apcommon.persistence.GenericRepository;
import com.tradevan.handyflow.model.form.DocNoticeLog;

/**
 * Title: DocNoticeLogRepository<br>
 * Description: <br>
 * Company: Tradevan Co.<br>
 * 
 * @author Neil Chen
 * @version 1.0.3
 */
public interface DocNoticeLogRepository extends GenericRepository<DocNoticeLog, Long> {

	int updateStatus2Y(String userId, String sysId);

	int updateStatusWithDocToDoId(String status, String sysId, Long docToDoId);
	
	int updateStatus(String status, String sysId, String applyNo, Integer serialNo);
	
	@SuppressWarnings("rawtypes")
	List fetchNeedNoticeLogs();
	
}
