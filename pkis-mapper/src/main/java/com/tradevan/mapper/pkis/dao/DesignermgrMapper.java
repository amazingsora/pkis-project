package com.tradevan.mapper.pkis.dao;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tradevan.mapper.pkis.model.Designermgr;

public interface DesignermgrMapper extends BaseMapper<Designermgr> {
	
	public int insertDesignermgr(Designermgr designermgr);
	
	List<Map<String, Object>> searchDesignermgr(Map<String, Object> params);
}
