package com.tradevan.aporg.repository;

import java.util.Map;

import com.tradevan.apcommon.bean.PageResult;
import com.tradevan.apcommon.persistence.GenericRepository;
import com.tradevan.aporg.model.EmailQueue;

public interface EmailQueueRepository extends GenericRepository<EmailQueue, Long> {
	
	PageResult fetchEmailQueue(Map<String, Object> params, Integer page, Integer pageSize);
}
