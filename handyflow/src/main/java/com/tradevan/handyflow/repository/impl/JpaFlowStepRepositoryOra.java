package com.tradevan.handyflow.repository.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.tradevan.apcommon.persistence.JpaGenericRepository;
import com.tradevan.handyflow.model.form.FlowStep;
import com.tradevan.handyflow.repository.FlowStepRepository;

/**
 * Title: JpaFlowStepRepository<br>
 * Description: <br>
 * Company: Tradevan Co.<br>
 * 
 * @author Neil Chen
 * @version 1.0.1
 */
@Repository("jpaFlowStepRepositoryOra")
public class JpaFlowStepRepositoryOra extends JpaGenericRepository<FlowStep, Long> implements FlowStepRepository {

	@Override
	public List<Map<String, Object>> findFlowSteps(Map<String, Object> params) {
		StringBuffer sql = new StringBuffer();
		sql.append(" select ROW_NUMBER() OVER (order by serno) AS rowsNum ");
		sql.append(" , serNo ");
		sql.append(" , CASE WHEN stepType = 'C' THEN '一般步驟' ELSE '平行會簽' END AS stepType ");
		sql.append(" , flowId ");
		sql.append(" , stepId ");
		sql.append(" , stepName ");
		sql.append(" , stepDesc ");
		sql.append(" , dispOrd ");
		sql.append(" , deptId ");
		sql.append(" , roleIds ");
		sql.append(" , userIds ");
		sql.append(" , sameUserAs ");
		sql.append(" , isReviewDeptRole ");
		sql.append(" , CASE WHEN isReviewDeptRole = '1' THEN '是' ELSE '否' END AS isReviewDeptRoleStr ");
		sql.append(" , isProjRole ");
		sql.append(" , CASE WHEN isProjRole = '1' THEN '是' ELSE '否' END AS isProjRoleStr ");
		sql.append(" , parallelPassCount "); 
		sql.append(" , createUserId ");
		sql.append(" , TO_CHAR (createTime, 'yyyy/mm/dd HH24:MI') AS createTime  ");
		sql.append(" , updateUserId ");
		sql.append(" , TO_CHAR (updateTime, 'yyyy/mm/dd HH24:MI') AS updateTime  ");
		sql.append(" from FlowStep ");
		sql.append(" where flowId = :flowId ");
		if(params.containsKey("subFlowId")) { //子流程設定頁面
			sql.append(" and subFlowId = :subFlowId ");
		} else { //關卡設定頁面
			sql.append(" and subFlowId is null ");
		}
		if(!params.containsKey("subFlowId")) params.remove("subFlowId");
		return findListMapBySQL_map(sql.toString(), params);
	}

	@Override
	public List<Map<String, Object>> findAllSameUserAs(String flowId, Integer dispOrd) {
		StringBuffer sql = new StringBuffer();
		List<Object> params = new ArrayList<Object>();
		sql.append(" select stepId as value, stepId + '(' + stepName + ')' as name ");
		sql.append(" from FlowStep ");
		sql.append(" where flowId = ? ");
		//2018/05/04 Sephiro : 同關卡X使用者鎖定在一般流程裡的step
		sql.append(" and subFlowId is null "); 
		sql.append(" and stepType <> 'P' ");
		params.add(flowId);
		if(dispOrd != null) {
			sql.append(" and dispOrd < ? ");
			params.add(dispOrd);
		} 
		return findListMapBySQL(sql.toString(), params.toArray());
	}

	@Override
	public String findMaxStepId(String flowId, String subFlowId) {
		StringBuffer sql = new StringBuffer();
		List<Object> params = new ArrayList<Object>();
		sql.append(" select MAX(stepId) AS stepId from FlowStep ");
		sql.append(" where flowId = ? ");
		params.add(flowId);
		if(subFlowId != null) {
			sql.append(" and subFlowId = ? ");
			params.add(subFlowId);
		} 
		
		String maxStepId = "";
		List<Map<String, Object>> list = findListMapBySQL(sql.toString(), params.toArray());
		Map<String, Object> map = list.get(0);
		if(subFlowId != null) { //子流程設定頁面
			if(map.get("stepId") != null) {
				String stepId = (String)map.get("stepId");
				String[] stepAry = stepId.split("Task");
				maxStepId = subFlowId + "Task" + (Integer.parseInt(stepAry[1]) +1);
			}else {
				maxStepId = subFlowId + "Task1";
			}			
		}else { //關卡設定頁面
			if(map.get("stepId") != null) { 
				String stepId = (String)map.get("stepId");
				String[] stepAry = stepId.split("Task");
				maxStepId = "Task" + (Integer.parseInt(stepAry[1]) +1);
			}else {
				maxStepId = "Task1";
			}
		}
		return maxStepId;
	}
	
	@Override
	public Integer findMaxStepDispOrd(String flowId, String subFlowId) {
		StringBuffer sql = new StringBuffer();
		List<Object> params = new ArrayList<Object>();
		sql.append(" select MAX(dispOrd) AS dispOrd from FlowStep ");
		sql.append(" where flowId = ? ");
		params.add(flowId);
		if(subFlowId != null) {
			sql.append(" and subFlowId = ? ");
			params.add(subFlowId);
		} 
		
		Integer maxDispOrd = 0;
		List<Map<String, Object>> list = findListMapBySQL(sql.toString(), params.toArray());
		Map<String, Object> map = list.get(0);
		if(map.get("dispOrd") != null) {
			maxDispOrd = Integer.parseInt(map.get("dispOrd").toString()) + 10;
		}else {
			maxDispOrd = 10;
		}
		return maxDispOrd;
	}

	@Override
	public List<Map<String, Object>> listFlowStep(String flowId, String stepId, String subFlowId, boolean isFinSelect) {
		StringBuffer sql = new StringBuffer();
		List<Object> params = new ArrayList<Object>();
		sql.append(" select stepId as value, stepName as name ");
		sql.append(" from FlowStep ");
		sql.append(" where flowId = ? ");
		params.add(flowId);
		if(isFinSelect) { //新增/編輯子流程時，完成/退回的下拉單
			sql.append(" and stepType <> 'P' "); //排除平行會簽
			sql.append(" and subFlowId is null "); //排除子流程關卡
		}else {
			sql.append(" and stepId <> ? ");
			params.add(stepId);
			if(subFlowId != null) { //子流程關卡新建連結，裡面下一關卡的下拉單
				sql.append(" and subFlowId is not null ");
			}else { //一般關卡的link
				sql.append(" and subFlowId is null ");
			}
		}
		return findListMapBySQL(sql.toString(), params.toArray());
	}

	@Override
	public List<Map<String, Object>> findFlowStepsNotBySubFlow(String flowId) {
		StringBuffer sql = new StringBuffer();
		sql.append(" select * ");
		sql.append(" from flowstep "); 
		sql.append(" where flowId = ? and subFlowId is null ");
		sql.append(" order by dispOrd ");		
		return findListMapBySQL(sql.toString(), flowId);
	}

	@Override
	public List<Map<String, Object>> findFlowStepsBySubFlow(String flowId, String subFlowId) {
		StringBuffer sql = new StringBuffer();
		sql.append(" select * ");
		sql.append(" from flowstep "); 
		sql.append(" where flowId = ? and subFlowId = ? ");
		sql.append(" order by dispOrd ");		
		return findListMapBySQL(sql.toString(), flowId, subFlowId);
	}

	@Override
	public List<Map<String, Object>> findSimpleFlowSteps(String flowId) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT s.serNo ");
		sql.append(",s.stepId ");
		sql.append(",s.stepName ");
		sql.append(",s.stepDesc ");
		sql.append(",s.deptId ");
		sql.append(",s.roleIds ");
		sql.append(",s.isReviewDeptRole ");
		sql.append(",CASE WHEN s.isReviewDeptRole = '1' THEN '是' ELSE '否' END AS isReviewDeptRoleStr ");
		sql.append(",s.userIds ");
		sql.append(",s.sameUserAs ");
		sql.append(",s.extension ");
		sql.append(",s.dispOrd ");
		sql.append(" FROM FlowStep s ");
		sql.append(" WHERE s.flowId = ? ");
		sql.append(" ORDER BY s.dispOrd ");
		
		return findListMapBySQL(sql.toString(), flowId);
	}
	
}
