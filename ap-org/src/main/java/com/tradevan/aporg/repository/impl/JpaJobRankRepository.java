package com.tradevan.aporg.repository.impl;

import org.springframework.stereotype.Repository;

import com.tradevan.apcommon.persistence.JpaGenericRepository;
import com.tradevan.aporg.model.JobRank;
import com.tradevan.aporg.repository.JobRankRepository;

/**
 * Title: JpaJobRankRepository<br>
 * Description: <br>
 * Company: Tradevan Co.<br>
 * 
 * @author Neil Chen
 * @version 1.0
 */
@Repository
public class JpaJobRankRepository extends JpaGenericRepository<JobRank, Long> implements JobRankRepository {
	
}
