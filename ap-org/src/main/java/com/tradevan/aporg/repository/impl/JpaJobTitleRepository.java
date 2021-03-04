package com.tradevan.aporg.repository.impl;

import java.util.Map;

import org.springframework.stereotype.Repository;

import com.tradevan.apcommon.bean.PageResult;
import com.tradevan.apcommon.persistence.JpaGenericRepository;
import com.tradevan.aporg.model.JobTitle;
import com.tradevan.aporg.repository.JobTitleRepository;

/**
 * Title: JpaJobTitleRepository<br>
 * Description: <br>
 * Company: Tradevan Co.<br>
 * 
 * @author Neil Chen
 * @version 1.0
 */
@Repository
public class JpaJobTitleRepository extends JpaGenericRepository<JobTitle, Long> implements JobTitleRepository {
	
	@Override
	public PageResult fetchJob(Map<String, Object> params, Integer page, Integer pageSize) {
		StringBuilder sql = new StringBuilder();
		sql.append("select ROW_NUMBER() OVER (order by serno) AS rownum, ");
		sql.append("jobTitleId, ");
		sql.append("jobTitleName ");
		sql.append("from JobTitle ");
		sql.append("where status = 'Y' ");
		return findListMapPageResultBySQL_map(sql.toString(), page, pageSize, params);
	}
	
}
