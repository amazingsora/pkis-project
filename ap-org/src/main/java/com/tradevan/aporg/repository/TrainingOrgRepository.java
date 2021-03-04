package com.tradevan.aporg.repository;

import java.util.Map;

import com.tradevan.apcommon.bean.PageResult;
import com.tradevan.apcommon.persistence.GenericRepository;
import com.tradevan.aporg.model.TrainingOrg;

/**
 * Title: TrainingOrgRepository<br>
 * Description: <br>
 * Company: Tradevan Co.<br>
 * 
 * @author Neil Chen
 * @version 1.0
 */
public interface TrainingOrgRepository extends GenericRepository<TrainingOrg, Long> {
	
	PageResult fetchTrainingOrg(Map<String, Object> params, int page, int pageSize);
}
