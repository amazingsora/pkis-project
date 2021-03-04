package com.tradevan.handyflow.repository;

import com.tradevan.apcommon.persistence.GenericRepository;
import com.tradevan.handyflow.model.form.SubFlowConf;

/**
 * Title: FlowStepRepository<br>
 * Description: <br>
 * Company: Tradevan Co.<br>
 * 
 * @author Neil Chen
 * @version 1.0
 */
public interface SubFlowConfRepository extends GenericRepository<SubFlowConf, Long> {

	String findMaxSubFlowId(String flowId);
	
}
