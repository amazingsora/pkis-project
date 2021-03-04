package com.tradevan.aporg.repository.impl;

import org.springframework.stereotype.Repository;

import com.tradevan.apcommon.persistence.JpaGenericRepository;
import com.tradevan.aporg.model.SysProf;
import com.tradevan.aporg.repository.SysProfRepository;

/**
 * Title: JpaSysRepository<br>
 * Description: <br>
 * Company: Tradevan Co.<br>
 * 
 * @author Neil Chen
 * @version 1.0
 */
@Repository
public class JpaSysProfRepository extends JpaGenericRepository<SysProf, String> implements SysProfRepository {

}
