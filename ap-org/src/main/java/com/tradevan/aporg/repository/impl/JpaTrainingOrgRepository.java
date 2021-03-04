package com.tradevan.aporg.repository.impl;

import java.util.Map;

import org.springframework.stereotype.Repository;

import com.tradevan.apcommon.bean.PageResult;
import com.tradevan.apcommon.persistence.JpaGenericRepository;
import com.tradevan.aporg.model.TrainingOrg;
import com.tradevan.aporg.repository.TrainingOrgRepository;

/**
 * Title: JpaTrainingOrgRepository<br>
 * Description: <br>
 * Company: Tradevan Co.<br>
 * 
 * @author Neil Chen
 * @version 1.0
 */
@Repository
public class JpaTrainingOrgRepository extends JpaGenericRepository<TrainingOrg, Long> implements TrainingOrgRepository {
	
	@Override
	public PageResult fetchTrainingOrg(Map<String, Object> params, int page, int pageSize) {
		StringBuilder sql = new StringBuilder();
		sql.append(" select serNo, trainingOrgId, trainingOrgName ");
		sql.append("   from TrainingOrg ");
		sql.append("  where 1 = 1 ");
		if (params.containsKey("trainingOrgName")) {
			sql.append("    and trainingOrgName like :trainingOrgName ");
		}
		
		return findListMapPageResultBySQL_map(sql.toString(), page, pageSize, params);
	}
}
