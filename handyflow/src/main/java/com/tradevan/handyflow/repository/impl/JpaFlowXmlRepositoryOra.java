package com.tradevan.handyflow.repository.impl;

import org.springframework.stereotype.Repository;

import com.tradevan.apcommon.persistence.JpaGenericRepository;
import com.tradevan.handyflow.model.form.FlowXml;
import com.tradevan.handyflow.repository.FlowXmlRepository;

/**
 * Title: JpaFlowXmlRepository<br>
 * Description: <br>
 * Company: Tradevan Co.<br>
 * 
 * @author Neil Chen
 * @version 1.0
 */
@Repository("jpaFlowXmlRepositoryOra")
public class JpaFlowXmlRepositoryOra extends JpaGenericRepository<FlowXml, Long> implements FlowXmlRepository {
	
}
