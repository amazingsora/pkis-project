package com.tradevan.handyflow.repository;

import java.util.List;
import java.util.Map;

import com.tradevan.apcommon.persistence.GenericRepository;
import com.tradevan.handyflow.model.form.FlowConf;

/**
 * Title: FlowConfRepository<br>
 * Description: <br>
 * Company: Tradevan Co.<br>
 * 
 * @author Neil Chen
 * @version 1.0
 */
public interface FlowConfRepository extends GenericRepository<FlowConf, Long> {

	List<Map<String, Object>> findFlowConfs(Map<String, Object> params);

	String getMaxFlowIdSerial(String prefixVal, String dateFmtVal, int serialLen);
	
}
