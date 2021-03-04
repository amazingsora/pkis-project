package com.tradevan.handyflow.repository;

import java.util.List;
import java.util.Map;

import com.tradevan.apcommon.persistence.GenericRepository;
import com.tradevan.handyflow.model.form.FormConf;

/**
 * Title: FormConfRepository<br>
 * Description: <br>
 * Company: Tradevan Co.<br>
 * 
 * @author Neil Chen
 * @version 1.0
 */
public interface FormConfRepository extends GenericRepository<FormConf, Long> {

	List<Map<String, Object>> findFormConfs(Map<String, Object> params);
	
}
