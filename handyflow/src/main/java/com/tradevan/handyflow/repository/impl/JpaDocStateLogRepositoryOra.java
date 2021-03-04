package com.tradevan.handyflow.repository.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.tradevan.apcommon.persistence.JpaGenericRepository;
import com.tradevan.handyflow.model.form.DocState;
import com.tradevan.handyflow.model.form.DocStateLog;
import com.tradevan.handyflow.repository.DocStateLogRepository;

/**
 * Title: JpaDocStateLogRepository<br>
 * Description: <br>
 * Company: Tradevan Co.<br>
 * 
 * @author Neil Chen
 * @version 1.2.1
 */
@Repository("jpaDocStateLogRepositoryOra")
public class JpaDocStateLogRepositoryOra extends JpaGenericRepository<DocStateLog, Long> implements DocStateLogRepository {
	
	@Override
	public List<DocStateLog> findListByFormIdAndApplyNo(String formId, String applyNo, boolean sortDesc) {
		
		String jpql = "select g from DocStateLog g join g.formConf f where f.formId = ? and g.applyNo = ? " + 
				"order by g.id " + (sortDesc ? "desc" : "");
		
		return findEntityListByJPQL(jpql, formId, applyNo);
	}
	
	@Override
	public List<Map<String, Object>> findDocStateLogListMap(Long docStateId, Long upDocStateId, Long... subDocStateIds) {
		List<Long> params = new ArrayList<Long>();
		params.add(docStateId);
		params.add(upDocStateId);
		
		StringBuilder sql = new StringBuilder(
				"select l.taskName, l.taskDesc, l.linkName, TO_CHAR (l.createTime, 'yyyy/mm/dd HH24:MI') sendDate, l.memo, l.toTaskId, l.userId, u.userName, l.agentId, null as candidates "); 
		sql.append("from DocStateLog l left join UserProf u on l.userId = u.userId "); 
		sql.append("where l.sourceSerNo in (?,?");
		for (Long id : subDocStateIds) {
			sql.append(",?");
			params.add(id);
		}
		sql.append(") and l.taskType in ('FlowTask', 'FlowParallel') and l.linkName <> 'Claim' "); 
		sql.append("order by l.serNo ");
		return findListMapBySQL(sql.toString(), params.toArray());
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<String> findDocStateLogMemos(String formId, String applyNo, String taskId) {
		StringBuilder sql = new StringBuilder("select l.memo "); 
		sql.append("from DocStateLog l "); 
		sql.append("where l.formId = ? ");
		sql.append("and l.applyNo = ? ");
		sql.append("and l.fromTaskId = ? ");
		sql.append("and l.taskType = 'FlowTask' and l.linkName <> 'Claim' "); 
		sql.append("order by l.serNo ");
		return findBySQL(sql.toString(), formId, applyNo, taskId);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<String> findDocStateLogMemos(String formId, String applyNo) {
		StringBuilder sql = new StringBuilder("select l.memo "); 
		sql.append("from DocStateLog l "); 
		sql.append("where ");
		if (formId != null) {
			sql.append("l.formId = ? and ");
		}
		sql.append("l.applyNo = ? ");
		sql.append("and l.taskType = 'FlowTask' and l.linkName <> 'Claim' "); 
		sql.append("order by l.serNo desc");
		return formId == null ? findBySQL(sql.toString(), applyNo) : findBySQL(sql.toString(), formId, applyNo);
	}
	
	@Override
	public long countDocStateLogsBy(DocState docState, List<String> taskIds) {
		
		return countByPropertyAndInValueList("docState", docState, "fromTaskId", taskIds);
	}

	@Override
	public List<Map<String, Object>> fetchDocStateLogs(Map<String, Object> params) {
		StringBuilder sql = new StringBuilder();
		sql.append("select ROW_NUMBER() OVER (order by f.projId) AS rowsnum  ");
		sql.append(", f.projId ");
		sql.append(", f.applyNo ");
		sql.append(", c.formName ");
		sql.append(", l.linkName ");
		sql.append(", TO_CHAR(l.createTime,'yyyy/mm/dd') as createTime  ");
		sql.append(", l.taskName ");
		sql.append(", j.jobTitleName ");
		sql.append(", l.userId||'/'||u.userName as userName  ");
		sql.append(", l.taskDesc ");
		sql.append(", nvl(l2.rcvDate, TO_CHAR (l.createTime, 'yyyy/mm/dd')) rcvDate  ");
		sql.append(", TO_CHAR (l.createTime, 'yyyy/mm/dd') sendDate ");
		sql.append(", l.memo ");
		sql.append(", case p.status ");
		sql.append("	when 'T' then '暫存' ");
		sql.append("	when 'P' then '申請審查中' ");
		sql.append(" 	when 'E' then '執行中' ");
		sql.append(" 	when 'O' then '結案中' ");
		sql.append("	when 'C' then '已結案' ");
		sql.append("	when 'S' then '已終止' ");
		sql.append("  	end as status ");
		sql.append("  from RschProjForm f ");
		sql.append("  left join DocState d on f.applyNo=d.applyNo ");
		sql.append("  left join DocStateLog l on d.serNo=l.sourceSerNo ");
		sql.append("  left join (select TO_CHAR (max(l2.createTime), 'yyyy/mm/dd') as rcvDate, max(sourceserno) sourceserno	, max(applyNo) applyNo, max(toTaskId) toTaskId ");												 
		sql.append(" 	           from DocStateLog l2  ");	
	    sql.append("              group by applyNo, toTaskId ");									  
		sql.append(" 	        ) l2 on l2.sourceSerNo = l.sourceSerNo and l2.applyNo=l.applyNo and l2.toTaskId=l.fromTaskId ");
		sql.append("  left join FormConf c on d.formId=c.formId ");
		sql.append("  left join FlowConf w on l.flowId=w.flowId ");
		sql.append("  left join UserProf u on l.userId=u.userId ");
		sql.append("  left join JobTitle j on u.jobTitleSerNo=j.serNo ");
		sql.append("  left join RschProj p on f.sourceSerNo = p.serNo ");
		sql.append(" where 1=1 ");
		sql.append(" and l.linkName <> 'claim' ");
		if (params.containsKey("sourceSerNo")) {
			sql.append("and f.sourceSerNo in (:sourceSerNo) ");
		}
		
		return findListMapBySQL_map(sql.toString(), params);
	}
	/**
	 * 案件管理 - 匯出excel：找出IRB所有審查紀錄
	 */
	@Override
	public List<Map<String, Object>> findIrbDocStateLogs(Map<String, Object> params) {
		StringBuilder sql = new StringBuilder();
		sql.append(" select p.irbNumber "); //案件編號
		sql.append(" , l.formId "); //申請項目
		sql.append(" , l.toTaskId ");
		sql.append(" , ( ");
		sql.append(" select * from (select TO_CHAR (dl.createTime, 'yyyy/mm/dd') as submitDate  ");
		sql.append(" from DocStateLog dl ");
		sql.append(" where dl.applyNo = l.applyNo ");
		sql.append(" and dl.toTaskId = 'Task2' and dl.flowStatus = 'DRAFT' ");
		sql.append(" order by dl.serNo ");
		sql.append(" ) as submitDate "); //送審日期
		sql.append(" , l.taskDesc "); //審查關卡
		sql.append(" , j.jobTitleName "); //審查人員職稱
		sql.append(" , u.userName "); //審查人員
		sql.append(" , l.linkName "); //審查狀況
		sql.append("  , TO_CHAR (l.createTime, 'yyyy/mm/dd HH24:MI') sendDate   "); //收件時間
		sql.append(" , '' as rcvDate "); //送件時間
		sql.append(" , NVL(l.memo, '') as memo  "); //審查意見
		sql.append(" from DocStateLog l ");
		sql.append(" left join IrbProjForm pm on pm.applyNo = l.applyNo ");
		sql.append(" left join (select serNo, projId, irbNumber from IrbProj where serNo in (:sourceSerNo)) p on pm.projId = p.projId ");
		sql.append(" left join UserProf u on l.userId = u.userId ");
		sql.append(" left join JobTitle j on j.serNo = u.jobTitleSerNo ");
		sql.append(" where l.taskType in ('FlowTask', 'FlowParallel') ");
		sql.append(" and pm.applyStatus <> 'D' ");
		sql.append(" and l.linkName <> 'Claim' ");
		sql.append(" and l.formId in(:formIds) ");
		sql.append(" and p.serNo in (:sourceSerNo) ");
		sql.append(" order by l.projId, l.serNo ");
		return findListMapBySQL_map(sql.toString(), params);
	}
	
}
