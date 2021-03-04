package com.tradevan.handyflow.repository.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.tradevan.apcommon.persistence.JpaGenericRepository;
import com.tradevan.handyflow.model.form.FlowConf;
import com.tradevan.handyflow.repository.FlowConfRepository;

/**
 * Title: JpaFlowConfRepository<br>
 * Description: <br>
 * Company: Tradevan Co.<br>
 * 
 * @author Neil Chen
 * @version 1.0
 */
@Repository("jpaFlowConfRepositoryOra")
public class JpaFlowConfRepositoryOra extends JpaGenericRepository<FlowConf, Long> implements FlowConfRepository {

	@Override
	public List<Map<String, Object>> findFlowConfs(Map<String, Object> params) {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT ROW_NUMBER() OVER (order by serno) AS rowsNum ");
		sql.append(" , serNo ");
		sql.append(" , flowId ");
		sql.append(" , flowName ");
		sql.append(" , flowVersion ");
		sql.append(" , flowDesc ");
		sql.append(" , flowAdminId ");
		sql.append(" , (SELECT sysName FROM SysProf WHERE SysProf.sysId = FlowConf.sysId) as system ");
		sql.append(" , createUserId ");
		sql.append(" , TO_CHAR (createTime, 'yyyy/mm/dd HH24:MI') AS createTime  ");
		sql.append(" , updateUserId ");
		sql.append(" , TO_CHAR (updateTime, 'yyyy/mm/dd HH24:MI') AS updateTime  ");
		sql.append(" FROM FlowConf ");
		if (params.containsKey("sysId")) {
			sql.append("WHERE sysId in ('ALL', :sysId) ");
		}
		else {
			sql.append("WHERE sysId = 'ALL' ");
		}
		if (params.containsKey("flowType")) {
			if ("COMM".equalsIgnoreCase((String) params.get("flowType"))) {
				sql.append("AND flowId NOT LIKE 'SIMP-%' ");
			}
			else if ("SIMP".equalsIgnoreCase((String) params.get("flowType"))) {
				sql.append("AND flowId LIKE 'SIMP-%' ");
			}
			params.remove("flowType");
		}
		return findListMapBySQL_map(sql.toString(), params);
	}

	@Override
	public String getMaxFlowIdSerial(String prefixVal, String dateFmtVal, int serialLen) {
		int len = prefixVal.length() + dateFmtVal.length();
		
		String serial = (String) getByJPQL(
				"select max(substring(f.flowId, " + (len+1) + ", " + serialLen + ")) from FlowConf f where f.flowId like ?", 
				prefixVal + dateFmtVal + "%");
		
		return serial;
	}
	
}
