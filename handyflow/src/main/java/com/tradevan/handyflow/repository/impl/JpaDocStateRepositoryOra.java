package com.tradevan.handyflow.repository.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.tradevan.apcommon.persistence.JpaGenericRepository;
import com.tradevan.apcommon.util.StringUtil;
import com.tradevan.handyflow.model.form.DocState;
import com.tradevan.handyflow.repository.DocStateRepository;

/**
 * Title: JpaDocStateRepository<br>
 * Description: <br>
 * Company: Tradevan Co.<br>
 * 
 * @author Neil Chen
 * @version 1.6.1
 */
@Repository
public class JpaDocStateRepositoryOra extends JpaGenericRepository<DocState, Long> implements DocStateRepository {
	
	@Override
	public String getMaxApplyNoSerial(String prefixVal, String dateFmtVal, int serialLen) {
		
		int len = prefixVal.length() + dateFmtVal.length();
		
		String serial = (String) getByJPQL(
				"select max(substring(s.applyNo, " + (len+1) + ", " + serialLen + ")) from DocState s where s.applyNo like ?1 and s.serialNo = 0", 
				prefixVal + dateFmtVal + "%");
		
		return serial;
	}
	
	@Override
	public String getMaxApplyNoSerial(String formId, String prefixVal, String dateFmtVal, int serialLen) {
		
		int len = prefixVal.length() + dateFmtVal.length();
		
		String serial = (String) getByJPQL(
				"select max(substring(s.applyNo, " + (len+1) + ", " + serialLen + ")) from DocState s join s.formConf f where f.formId = ?1 and s.applyNo like ?2 and s.serialNo = 0", 
				formId, prefixVal + dateFmtVal + "%");
		
		return serial;
	}
	
	@Override
	public List<DocState> findOwnedListByUser(String userId, String formId, String sysId, boolean sortDesc) {
		
		String jpql = "select s from DocState s join fetch s.formConf join fetch s.applicant where s.flowStatus in (:statusDraft,:statusProcess) and s.nowUserId = :nowUserId " + 
				((formId != null) ? "and s.formId = :formId " : "") + (StringUtil.isNotBlank(sysId) ? "and s.sysId in ('ALL', :sysId) " : "") +
				"order by s.formId, s.applyNo " + (sortDesc ? "desc" : "");
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("statusDraft", DocState.FLOW_STATUS_DRAFT);
		params.put("statusProcess", DocState.FLOW_STATUS_PROCESS);
		params.put("nowUserId", userId);
		if (formId != null) {
			params.put("formId", formId);
		}
		if (StringUtil.isNotBlank(sysId)) {
			params.put("sysId", sysId);
		}
		return findEntityListByJPQL_map(jpql, params);
	}
	
	@Override
	public List<DocState> findNoOwnerListByDeptAndRole(String deptId, String roleId, String formId, String sysId, boolean sortDesc) {
		
		String jpql = "select s from DocState s join fetch s.formConf join fetch s.applicant where s.flowStatus in (:statusDraft,:statusProcess) and s.nowUserId is null " +
				"and s.taskDeptId = :taskDeptId " +
				"and (s.taskRoleIds = :taskRoleIds1 or s.taskRoleIds like :taskRoleIds2 or s.taskRoleIds like :taskRoleIds3 or s.taskRoleIds like :taskRoleIds4) " +
				((formId != null) ? "and s.formId = :formId " : "") + (StringUtil.isNotBlank(sysId) ? "and s.sysId in ('ALL', :sysId) " : "") +
				"order by s.formId, s.applyNo " + (sortDesc ? "desc" : "");
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("statusDraft", DocState.FLOW_STATUS_DRAFT);
		params.put("statusProcess", DocState.FLOW_STATUS_PROCESS);
		params.put("taskDeptId", deptId);
		params.put("taskRoleIds1", roleId);
		params.put("taskRoleIds2", roleId + ",%");
		params.put("taskRoleIds3", "%," + roleId + ",%");
		params.put("taskRoleIds4", "%," + roleId);
		if (formId != null) {
			params.put("formId", formId);
		}
		if (StringUtil.isNotBlank(sysId)) {
			params.put("sysId", sysId);
		}
		return findEntityListByJPQL_map(jpql, params);
	}
	
	@Override
	public List<DocState> findNoOwnerListByApplicantDeptRole(String deptId, String roleId, String formId, String sysId, boolean sortDesc, boolean isSubDept) {
		
		String jpql = "select s from DocState s join fetch s.formConf join fetch s.applicant where s.isReviewDeptRole = 'N' " +
				"and s.flowStatus in (:statusDraft,:statusProcess) and s.nowUserId is null and s.taskDeptId is null " +
				"and (s.applicant.deptId = :deptId or s.subFlowDeptId = :deptId) " +
				"and (s.taskRoleIds = :taskRoleIds1 or s.taskRoleIds like :taskRoleIds2 or s.taskRoleIds like :taskRoleIds3 or s.taskRoleIds like :taskRoleIds4) " +
				((formId != null) ? "and s.formId = :formId " : "") + (StringUtil.isNotBlank(sysId) ? "and s.sysId in ('ALL', :sysId) " : "") +
				(isSubDept ? "and not exists (select u from UserDeptRole u where u.deptId = :deptId and u.roleId = :roleId) " : "") +
				"order by s.formId, s.applyNo " + (sortDesc ? "desc" : "");
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("statusDraft", DocState.FLOW_STATUS_DRAFT);
		params.put("statusProcess", DocState.FLOW_STATUS_PROCESS);
		params.put("deptId", deptId);
		params.put("taskRoleIds1", roleId);
		params.put("taskRoleIds2", roleId + ",%");
		params.put("taskRoleIds3", "%," + roleId + ",%");
		params.put("taskRoleIds4", "%," + roleId);
		if (formId != null) {
			params.put("formId", formId);
		}
		if (StringUtil.isNotBlank(sysId)) {
			params.put("sysId", sysId);
		}
		if (isSubDept) {
			params.put("roleId", roleId);
		}
		return findEntityListByJPQL_map(jpql, params);
	}
	
	@Override
	public List<DocState> findNoOwnerListByReviewDeptRole(String deptId, String roleId, String formId, String sysId, boolean sortDesc, boolean isSubDept) {
		
		String jpql = "select s from DocState s join fetch s.formConf join fetch s.applicant where s.isReviewDeptRole = 'Y' " +
				"and s.flowStatus in (:statusDraft,:statusProcess) and s.nowUserId is null and s.taskDeptId is null " +
				"and s.lastReviewDeptId = :lastReviewDeptId " +
				"and (s.taskRoleIds = :taskRoleIds1 or s.taskRoleIds like :taskRoleIds2 or s.taskRoleIds like :taskRoleIds3 or s.taskRoleIds like :taskRoleIds4) " +
				((formId != null) ? "and s.formId = :formId " : "") + (StringUtil.isNotBlank(sysId) ? "and s.sysId in ('ALL', :sysId) " : "") +
				(isSubDept ? "and not exists (select u from UserDeptRole u where u.deptId = :deptId and u.roleId = :roleId) " : "") +
				"order by s.formId, s.applyNo " + (sortDesc ? "desc" : "");
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("statusDraft", DocState.FLOW_STATUS_DRAFT);
		params.put("statusProcess", DocState.FLOW_STATUS_PROCESS);
		params.put("lastReviewDeptId", deptId);
		params.put("taskRoleIds1", roleId);
		params.put("taskRoleIds2", roleId + ",%");
		params.put("taskRoleIds3", "%," + roleId + ",%");
		params.put("taskRoleIds4", "%," + roleId);
		if (formId != null) {
			params.put("formId", formId);
		}
		if (StringUtil.isNotBlank(sysId)) {
			params.put("sysId", sysId);
		}
		if (isSubDept) {
			params.put("deptId", deptId);
			params.put("roleId", roleId);
		}
		return findEntityListByJPQL_map(jpql, params);
	}
	
	@Override
	public List<DocState> findNoOwnerListByRole(String roleId, String formId, String sysId, boolean sortDesc) {
		
		String jpql = "select s from DocState s join fetch s.formConf join fetch s.applicant where s.isProjRole = 'N' " +
				"and s.flowStatus in (:statusDraft,:statusProcess) and s.nowUserId is null and s.taskDeptId is null " +
				"and (s.taskRoleIds = :taskRoleIds1 or s.taskRoleIds like :taskRoleIds2 or s.taskRoleIds like :taskRoleIds3 or s.taskRoleIds like :taskRoleIds4) " + 
				((formId != null) ? "and s.formId = :formId " : "") + (StringUtil.isNotBlank(sysId) ? "and s.sysId in ('ALL', :sysId) " : "") +
				"order by s.formId, s.applyNo " + (sortDesc ? "desc" : "");
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("statusDraft", DocState.FLOW_STATUS_DRAFT);
		params.put("statusProcess", DocState.FLOW_STATUS_PROCESS);
		params.put("taskRoleIds1", roleId);
		params.put("taskRoleIds2", roleId + ",%");
		params.put("taskRoleIds3", "%," + roleId + ",%");
		params.put("taskRoleIds4", "%," + roleId);
		if (formId != null) {
			params.put("formId", formId);
		}
		if (StringUtil.isNotBlank(sysId)) {
			params.put("sysId", sysId);
		}
		return findEntityListByJPQL_map(jpql, params);
	}
	
	@Override
	public List<DocState> findNoOwnerListByUserProjRole(String userId, String formId, String sysId, boolean sortDesc) {
		
		String sql = "select distinct s.* from DocState s, UserProjRole r where r.userId = ? and s.projId = r.projId and s.isProjRole = 'Y' and s.flowStatus in (?,?) " +
			"and s.nowUserId is null and (s.taskRoleIds = r.roleId or s.taskRoleIds like r.roleId+',%' or s.taskRoleIds like '%,'+r.roleId+',%' or s.taskRoleIds like '%,'+r.roleId) " + 
			((formId != null) ? "and s.formId = ? " : "") + (StringUtil.isNotBlank(sysId) ? "and s.sysId in ('ALL', ?) " : "") +
			"order by s.formId, s.applyNo " + (sortDesc ? "desc" : "");
		
		List<Object> params = new ArrayList<Object>();
		params.add(userId);
		params.add(DocState.FLOW_STATUS_DRAFT);
		params.add(DocState.FLOW_STATUS_PROCESS);
		if (formId != null) {
			params.add(formId);
		}
		if (StringUtil.isNotBlank(sysId)) {
			params.add(sysId);
		}
		return findEntityListBySQL(sql, params.toArray());
	}
	
	@Override
	public List<DocState> findNoOwnerListByDept(String deptId, String formId, String sysId, boolean sortDesc) {
		
		String jpql = "select s from DocState s join fetch s.formConf join fetch s.applicant " +
				"where s.flowStatus in (:statusDraft,:statusProcess) and s.nowUserId is null and s.taskRoleIds is null and s.taskDeptId = :taskDeptId " + 
				((formId != null) ? "and s.formId = :formId " : "") + (StringUtil.isNotBlank(sysId) ? "and s.sysId in ('ALL', :sysId) " : "") +
				"order by s.formId, s.applyNo " + (sortDesc ? "desc" : "");
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("statusDraft", DocState.FLOW_STATUS_DRAFT);
		params.put("statusProcess", DocState.FLOW_STATUS_PROCESS);
		params.put("taskDeptId", deptId);
		if (formId != null) {
			params.put("formId", formId);
		}
		if (StringUtil.isNotBlank(sysId)) {
			params.put("sysId", sysId);
		}
		return findEntityListByJPQL_map(jpql, params);
	}
	
	@Override
	public List<DocState> findNoOwnerListByUser(String userId, String formId, String sysId, boolean sortDesc) {
		
		String jpql = "select s from DocState s join fetch s.formConf join fetch s.applicant " +
				"where s.flowStatus in (:statusDraft,:statusProcess) and s.nowUserId is null " +
				"and (s.taskUserIds = :taskUserIds1 or s.taskUserIds like :taskUserIds2 or s.taskUserIds like :taskUserIds3 or s.taskUserIds like :taskUserIds4) " + 
				((formId != null) ? "and s.formId = :formId " : "") + (StringUtil.isNotBlank(sysId) ? "and s.sysId in ('ALL', :sysId) " : "") +
				"order by s.formId, s.applyNo " + (sortDesc ? "desc" : "");
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("statusDraft", DocState.FLOW_STATUS_DRAFT);
		params.put("statusProcess", DocState.FLOW_STATUS_PROCESS);
		params.put("taskUserIds1", userId);
		params.put("taskUserIds2", userId + ",%");
		params.put("taskUserIds3", "%," + userId + ",%");
		params.put("taskUserIds4", "%," + userId);
		if (formId != null) {
			params.put("formId", formId);
		}
		if (StringUtil.isNotBlank(sysId)) {
			params.put("sysId", sysId);
		}
		return findEntityListByJPQL_map(jpql, params);
	}
	
	@Override
	public int getMaxSerialNo(DocState upDocState) {
		
		Integer serial = (Integer) getByJPQL("select max(s.serialNo) from DocState s where s.upDocState = ?", upDocState);
		
		return serial == null ? 0 : serial;
	}

	@Override
	public DocState getDocState(String formId, String applyNo, Integer serialNo) {
		return getEntityByProperties(new String[] { "formConf.formId", "applyNo", "serialNo" }, new Object[] { formId, applyNo, serialNo });
	}

	/**
	 * IRB：查詢審查委員的待辦清單，已停留 2 天未處理完成的待辦
	 */		
	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> findCmToDos() {
		StringBuffer sql = new StringBuffer();
		sql.append(" select up.email, fc.formName ");
		sql.append(" from DocState d "); 
		sql.append(" left join FormConf fc on fc.formId = d.formId ");
		sql.append(" left join UserProf up on up.userId = d.nowUserId ");
		sql.append(" where d.sysId = 'IRB' and d.flowStatus <> 'CANCEL' and d.nowUserId IS NOT NULL ");
		sql.append(" and SYSTIMESTAMP - d.updateTime > 3   ");		
		return (List<Object[]>)findBySQL(sql.toString());
	}
	
	/**
	 * IRB：偏差報告若是退回複審，並一直停留在申請人或主持人身上，找出最後更新時間====>要跟系統日比較
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> findDNUpdateTimes() {
		StringBuffer sql = new StringBuffer();
		sql.append("select p.serNo ");
		sql.append(" 	 , CASE WHEN DATEDIFF(DAY, DATEADD(DAY, 7, d.updateTime), GETDATE()) = 0 then 'Y' else 'N' end as isAfter7Days ");
		sql.append(" 	 , CASE WHEN DATEDIFF(DAY, DATEADD(DAY, 9, d.updateTime), GETDATE()) = 0 then 'Y' else 'N' end as isAfter9Days ");
		sql.append(" 	 , CASE WHEN DATEDIFF(DAY, DATEADD(DAY, 12, d.updateTime), GETDATE()) = 0 then 'Y' else 'N' end as isAfter12Days ");
		sql.append(" 	 , CASE WHEN DATEDIFF(DAY, DATEADD(DAY, 13, d.updateTime), GETDATE()) = 0 then 'Y' else 'N' end as isAfter13Days ");
		sql.append("  from DocState d ");
		sql.append("  left join IrbProjForm pm on pm.applyNo = d.applyNo ");
		sql.append("  left join IrbProj p on p.projId = d.projId and p.status = 'E' ");
		sql.append(" where d.sysId = 'IRB' ");
		sql.append("   and d.flowStatus <> 'CANCEL' ");
		sql.append("   and pm.returnTimes is not null ");
		sql.append("   and d.nowTaskId in ('Task1', 'Task2') ");
		sql.append("   and pm.formId in ('IrbDeviaNotiFlow', 'IrbDeviaNotiForm') ");
		return (List<Object[]>)findBySQL(sql.toString());
	}
	
}
