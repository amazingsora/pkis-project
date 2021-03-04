package com.tradevan.handyflow.repository.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.tradevan.apcommon.persistence.JpaGenericRepository;
import com.tradevan.handyflow.model.form.FlowStepLink;
import com.tradevan.handyflow.repository.FlowStepLinkRepository;

/**
 * Title: JpaFlowStepLinkRepository<br>
 * Description: <br>
 * Company: Tradevan Co.<br>
 * 
 * @author Neil Chen
 * @version 1.0
 */
@Repository("jpaFlowStepLinkRepositoryOra")
public class JpaFlowStepLinkRepositoryOra extends JpaGenericRepository<FlowStepLink, Long> implements FlowStepLinkRepository {

	@Override
	public List<Map<String, Object>> findFlowStepLinks(Map<String, Object> params) {
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT ROW_NUMBER() OVER (order by l.serNo) AS rowsNum ");
		sql.append(" , l.serNo ");
		sql.append(" , l.flowId ");
		sql.append(" , l.stepId ");
		sql.append(" , l.toStepId ");
		sql.append(" , l.action ");
		sql.append(" , CASE WHEN l.isConcurrent = '1' THEN 'Y' ELSE 'N' END AS concurrent ");
		sql.append(" , l.linkName ");
		sql.append(" , l.linkDesc ");
		sql.append(" , l.dispOrd ");
		sql.append(" , l.createUserId ");
		sql.append(" , TO_CHAR (l.createTime, 'yyyy/mm/dd HH24:MI') AS createTime ");
		sql.append(" , l.updateUserId ");
		sql.append(" , TO_CHAR (l.updateTime, 'yyyy/mm/dd HH24:MI') AS updateTime ");
		sql.append(" , CASE ");
		sql.append(" WHEN s.stepType = 'C' THEN to_char(l.toStepId || '(' || s.stepName || ')') ");
		sql.append(" WHEN s.stepType = 'P' ");
		sql.append(" THEN (SELECT to_char(wmsys.wm_concat(sf.subFlowId||'('||sf.subFlowName||')')) ");
		sql.append(" 	FROM FlowStep s2, FlowStepLink l2, SubFlowConf sf ");
		sql.append(" 	WHERE s2.flowId = l.flowId and s2.stepId = l.toStepId and s2.flowId = l2.flowId and s2.stepId = l2.stepId and l2.flowId = sf.flowId and l2.toStepId = sf.subFlowId) ");
		sql.append(" WHEN INSTR(l.toStepId, 'Sub') != 0 AND INSTR(l.toStepId, 'Finish') != 0 ");
		sql.append(" THEN (SELECT to_char(wmsys.wm_concat(s2.stepId||'('||s2.stepName||')')) ");
		sql.append(" 	FROM SubFlowConf sf, FlowStep s2 ");
		sql.append(" 	WHERE l.flowId = sf.flowId AND l.stepId LIKE sf.subFlowId + '%' AND s2.flowId = l.flowId AND s2.stepId = sf.finishTo ) "); 
		sql.append(" WHEN INSTR(l.toStepId, 'Sub') != 0 AND INSTR(l.toStepId, 'Return') != 0 ");
		sql.append(" THEN (SELECT to_char(wmsys.wm_concat(s2.stepId||'('||s2.stepName||')')) ");
		sql.append(" 	FROM SubFlowConf sf, FlowStep s2 ");
		sql.append(" 	WHERE l.flowId = sf.flowId AND l.stepId LIKE sf.subFlowId + '%' AND s2.flowId = l.flowId AND s2.stepId = sf.returnTo) ");		
		sql.append(" ELSE (SELECT to_char(wmsys.wm_concat(sf.subFlowId||'('||sf.subFlowName||')')) ");
		sql.append(" 	FROM SubFlowConf sf where sf.flowId = l.flowId and sf.subFlowId = l.toStepId ) ");
		sql.append(" END toStepIdAndName ");	
		sql.append(" FROM FlowStepLink l LEFT JOIN FlowStep s on l.flowId = s.flowId AND l.toStepId = s.stepId  ");		
		sql.append(" WHERE l.flowId = :flowId AND l.stepId = :stepId ");
		return findListMapBySQL_map(sql.toString(), params);
	}

	@Override
	public Integer findMaxLinkDispOrd(String flowId, String stepId) {
		StringBuffer sql = new StringBuffer();
		sql.append(" select Max(dispOrd) as dispOrd "); 	 
		sql.append(" from FlowStepLink "); 	 
		sql.append(" where flowId = ? "); 	 
		sql.append(" and stepId = ? "); 	
		List<Map<String, Object>> list = findListMapBySQL(sql.toString(), flowId, stepId);
		
		Integer maxDispOrd = 0;
		Map<String, Object> map = list.get(0);
		if(map.get("dispOrd") != null) {
			maxDispOrd = Integer.parseInt(map.get("dispOrd").toString()) + 10;
		}else {
			maxDispOrd = 10;
		}
		return maxDispOrd;
	}

	@Override
	public List<Map<String, Object>> findLinksForSubFlow(Map<String, Object> params) {
		StringBuffer sql = new StringBuffer();
		sql.append(" select l.linkName ");
		sql.append(" , l.action ");
		sql.append(" , l.toStepId ");
		sql.append(" , l.linkDesc ");
		sql.append(" , l.dispOrd ");
		sql.append(" , (select sf.subFlowId || '(' || sf.subFlowName || ')' from SubFlowConf sf where sf.flowId = l.flowId and sf.subFlowId = l.toStepId) AS toStepIdAndName ");
		sql.append(" , CASE WHEN l.isConcurrent = '1' THEN 'Y' ELSE 'N' END AS concurrent ");
		sql.append(" from FlowStepLink l ");
		sql.append(" where l.flowId = :flowId and l.stepId = :stepId ");
		return findListMapBySQL_map(sql.toString(), params);
	}
	
}
