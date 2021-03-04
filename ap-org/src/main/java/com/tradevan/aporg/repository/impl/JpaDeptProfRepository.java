package com.tradevan.aporg.repository.impl;

import java.util.Map;

import org.springframework.stereotype.Repository;

import com.tradevan.apcommon.bean.PageResult;
import com.tradevan.apcommon.persistence.JpaGenericRepository;
import com.tradevan.aporg.model.DeptProf;
import com.tradevan.aporg.repository.DeptProfRepository;

/**
 * Title: JpaDeptRepository<br>
 * Description: <br>
 * Company: Tradevan Co.<br>
 * 
 * @author Neil Chen
 * @version 1.0
 */
@Repository
public class JpaDeptProfRepository extends JpaGenericRepository<DeptProf, Long> implements DeptProfRepository {
	
	@Override
	public PageResult fetchDept(Map<String, Object> params, Integer page, Integer pageSize) {
		StringBuilder sql = new StringBuilder();
		sql.append("select ROW_NUMBER() OVER (order by serno) AS rownum, ");
		sql.append("deptId, ");
		sql.append("deptName ");
		sql.append("from DeptProf ");
		sql.append("where status = 'Y' ");
		return findListMapPageResultBySQL_map(sql.toString(), page, pageSize, params);
	}
	
}
