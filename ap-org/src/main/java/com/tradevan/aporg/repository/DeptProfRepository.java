package com.tradevan.aporg.repository;

import java.util.Map;

import com.tradevan.apcommon.bean.PageResult;
import com.tradevan.apcommon.persistence.GenericRepository;
import com.tradevan.aporg.model.DeptProf;

/**
 * Title: DeptRepository<br>
 * Description: <br>
 * Company: Tradevan Co.<br>
 * 
 * @author Neil Chen
 * @version 1.0
 */
public interface DeptProfRepository extends GenericRepository<DeptProf, Long> {
	
	PageResult fetchDept(Map<String, Object> params, Integer page, Integer pageSize);

}
