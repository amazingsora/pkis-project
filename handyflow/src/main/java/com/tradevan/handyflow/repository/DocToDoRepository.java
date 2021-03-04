package com.tradevan.handyflow.repository;

import java.util.List;

import com.tradevan.apcommon.persistence.GenericRepository;
import com.tradevan.handyflow.model.form.DocToDo;

/**
 * Title: DocToDoRepository<br>
 * Description: <br>
 * Company: Tradevan Co.<br>
 * 
 * @author Neil Chen
 * @version 1.1
 */
public interface DocToDoRepository extends GenericRepository<DocToDo, Long> {

	String getMaxToDoNoSerial(String formId, String prefixVal, String dateFmtVal, int serialLen);
	
	List<DocToDo> findOwnedListByUser(String userId, String formId, String sysId, boolean sortDesc);

	int updateStatusByProjIdAndSysId(String projId,String sysId,String newStatus);

	int restoreStatusByProjIdAndSysId(String projId, String sysId);

	int updateStatusByProjIdAndSysIdAndStatus(String projId, String sysId, String status, String newStatus);
}
