package com.tradevan.handyflow.repository;

import java.util.List;
import java.util.Map;

import com.tradevan.apcommon.persistence.GenericRepository;
import com.tradevan.handyflow.model.form.DocState;
import com.tradevan.handyflow.model.form.DocStateLog;

/**
 * Title: DocStateLogRepository<br>
 * Description: <br>
 * Company: Tradevan Co.<br>
 * 
 * @author Neil Chen
 * @version 1.2
 */
public interface DocStateLogRepository extends GenericRepository<DocStateLog, Long> {

	List<DocStateLog> findListByFormIdAndApplyNo(String formId, String applyNo, boolean sortDesc);

	List<Map<String, Object>> findDocStateLogListMap(Long docStateId, Long upDocStateId, Long... subDocStateIds);

	List<String> findDocStateLogMemos(String formId, String applyNo, String taskId);
	
	List<String> findDocStateLogMemos(String formId, String applyNo);
	
	long countDocStateLogsBy(DocState docState, List<String> taskIds);
	
	List<Map<String, Object>> fetchDocStateLogs(Map<String, Object> params);
	/**
	 * 案件管理 - 匯出excel：找出IRB所有審查紀錄
	 */
	List<Map<String, Object>> findIrbDocStateLogs(Map<String, Object> params);
	
}
