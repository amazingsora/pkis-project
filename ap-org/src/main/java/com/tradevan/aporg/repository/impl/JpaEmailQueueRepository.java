package com.tradevan.aporg.repository.impl;

import java.util.Map;

import org.springframework.stereotype.Repository;

import com.tradevan.apcommon.bean.PageResult;
import com.tradevan.apcommon.persistence.JpaGenericRepository;
import com.tradevan.aporg.model.EmailQueue;
import com.tradevan.aporg.repository.EmailQueueRepository;

@Repository
public class JpaEmailQueueRepository extends JpaGenericRepository<EmailQueue, Long> implements EmailQueueRepository {
	
	@Override
	public PageResult fetchEmailQueue(Map<String, Object> params, Integer page, Integer pageSize) {
		StringBuilder sql = new StringBuilder();
		sql.append(" select serNo ");
		sql.append("      , convert(varchar,createTime, 111) + ' '+convert(varchar,createTime, 108) as sendTime ");
		sql.append("      , mailTo ");
		sql.append("      , subject ");
		sql.append("   from EmailQueue ");
		sql.append("  where 1 = 1 ");
		
		if (params.containsKey("sysId")) {
			sql.append(" and sysId in ('ALL', :sysId) ");
		}
		if (params.containsKey("begDate")) {
			sql.append(" and convert(varchar, createTime, 111) >= :begDate ");
		}
		if (params.containsKey("endDate")) {
			sql.append(" and convert(varchar, createTime, 111) <= :endDate ");
		}
		if (params.containsKey("mailTo")) {
			sql.append(" and mailTo like :mailTo ");
		}
		if (params.containsKey("subject")) {
			sql.append(" and subject like :subject ");
		}
		    
		return findListMapPageResultBySQL_map(sql.toString(), page, pageSize, params);
	}
}
