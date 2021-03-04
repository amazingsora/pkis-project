package com.tradevan.handyflow.repository;

import java.util.List;

import com.tradevan.apcommon.persistence.GenericRepository;
import com.tradevan.handyflow.model.form.DocState;

/**
 * Title: DocStateRepository<br>
 * Description: <br>
 * Company: Tradevan Co.<br>
 * 
 * @author Neil Chen
 * @version 1.6.1
 */
public interface DocStateRepository extends GenericRepository<DocState, Long> {

	String getMaxApplyNoSerial(String prefixVal, String dateFmtVal, int serialLen);
	
	String getMaxApplyNoSerial(String formId, String prefixVal, String dateFmtVal, int serialLen);
	
	List<DocState> findOwnedListByUser(String userId, String formId, String sysId, boolean sortDesc);

	List<DocState> findNoOwnerListByDeptAndRole(String deptId, String roleId, String formId, String sysId, boolean sortDesc);
	
	List<DocState> findNoOwnerListByApplicantDeptRole(String deptId, String roleId, String formId, String sysId, boolean sortDesc, boolean isSubDept);
	
	List<DocState> findNoOwnerListByReviewDeptRole(String deptId, String roleId, String formId, String sysId, boolean sortDesc, boolean isSubDept);
	
	List<DocState> findNoOwnerListByRole(String roleId, String formId, String sysId, boolean sortDesc);
	
	List<DocState> findNoOwnerListByUserProjRole(String userId, String formId, String sysId, boolean sortDesc);
	
	List<DocState> findNoOwnerListByDept(String deptId, String formId, String sysId, boolean sortDesc);

	List<DocState> findNoOwnerListByUser(String userId, String formId, String sysId, boolean sortDesc);

	int getMaxSerialNo(DocState upDocState);
	
	DocState getDocState(String formId, String applyNo, Integer serialNo);
	/**
	 * IRB：查詢審查委員的待辦清單，已停留 2 天未處理完成的待辦
	 */	
	List<Object[]> findCmToDos();
	/**
	 * IRB：偏差報告若是退回複審，並一直停留在申請人或主持人身上，找出最後更新時間====>要跟系統日比較
	 */
	List<Object[]> findDNUpdateTimes();
	
}
