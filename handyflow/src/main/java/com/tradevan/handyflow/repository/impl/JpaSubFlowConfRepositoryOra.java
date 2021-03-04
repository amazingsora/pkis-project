package com.tradevan.handyflow.repository.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.tradevan.apcommon.persistence.JpaGenericRepository;
import com.tradevan.handyflow.model.form.SubFlowConf;
import com.tradevan.handyflow.repository.SubFlowConfRepository;

@Repository("jpaSubFlowConfRepositoryOra")
public class JpaSubFlowConfRepositoryOra extends JpaGenericRepository<SubFlowConf, Long> implements SubFlowConfRepository {

	@Override
	public String findMaxSubFlowId(String flowId) {
		String maxSubFlowId = "";
		StringBuffer sql = new StringBuffer(" select MAX(subFlowId) AS subFlowId from SubFlowConf where flowId = ? ");
		List<Map<String, Object>> list = findListMapBySQL(sql.toString(), flowId);
		Map<String, Object> map = list.get(0);
		if(map.get("subFlowId") != null) {
			String subFlowId = (String)map.get("subFlowId");
			String[] subAry = subFlowId.split("Sub");
			maxSubFlowId = "Sub" + (Integer.parseInt(subAry[1]) +1);
		}else {
			maxSubFlowId = "Sub1";
		}
		return maxSubFlowId;
	}

}
