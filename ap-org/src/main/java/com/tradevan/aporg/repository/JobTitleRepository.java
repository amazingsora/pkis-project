package com.tradevan.aporg.repository;

import java.util.Map;

import com.tradevan.apcommon.bean.PageResult;
import com.tradevan.apcommon.persistence.GenericRepository;
import com.tradevan.aporg.model.JobTitle;

/**
 * Title: JobTitleRepository<br>
 * Description: <br>
 * Company: Tradevan Co.<br>
 * 
 * @author Neil Chen
 * @version 1.0
 */
public interface JobTitleRepository extends GenericRepository<JobTitle, Long> {
	
	PageResult fetchJob(Map<String, Object> params, Integer page, Integer pageSize);
	
}
