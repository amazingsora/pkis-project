package com.tradevan.aporg.repository.impl;

import org.springframework.stereotype.Repository;

import com.tradevan.apcommon.persistence.JpaGenericRepository;
import com.tradevan.aporg.model.JobType;
import com.tradevan.aporg.repository.JobTypeRepository;

/**
 * Title: JpaJobTypeRepository<br>
 * Description: <br>
 * Company: Tradevan Co.<br>
 * 
 * @author Neil Chen
 * @version 1.0
 */
@Repository
public class JpaJobTypeRepository extends JpaGenericRepository<JobType, Long> implements JobTypeRepository {
	
}
