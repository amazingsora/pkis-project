package com.tradevan.aporg.repository.impl;

import org.springframework.stereotype.Repository;

import com.tradevan.apcommon.persistence.JpaGenericRepository;
import com.tradevan.aporg.model.SubJobType;
import com.tradevan.aporg.repository.SubJobTypeRepository;

/**
 * Title: JpaSubJobTypeRepository<br>
 * Description: <br>
 * Company: Tradevan Co.<br>
 * 
 * @author Neil Chen
 * @version 1.0
 */
@Repository
public class JpaSubJobTypeRepository extends JpaGenericRepository<SubJobType, Long> implements SubJobTypeRepository {
	
}
