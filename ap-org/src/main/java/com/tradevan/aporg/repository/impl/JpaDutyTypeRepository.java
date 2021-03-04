package com.tradevan.aporg.repository.impl;

import org.springframework.stereotype.Repository;

import com.tradevan.apcommon.persistence.JpaGenericRepository;
import com.tradevan.aporg.model.DutyType;
import com.tradevan.aporg.repository.DutyTypeRepository;

/**
 * Title: JpaDutyTypeRepository<br>
 * Description: <br>
 * Company: Tradevan Co.<br>
 * 
 * @author Neil Chen
 * @version 1.0
 */
@Repository
public class JpaDutyTypeRepository extends JpaGenericRepository<DutyType, Long> implements DutyTypeRepository {
	
}
