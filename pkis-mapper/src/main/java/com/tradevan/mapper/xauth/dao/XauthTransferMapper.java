package com.tradevan.mapper.xauth.dao;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

public interface XauthTransferMapper extends BaseMapper<Object> {
	
	List<Map<String, Object>> selectallContract(Map<String, Object> params);
	
	List<Map<String, Object>> searchtransferUserId(Map<String, Object> params);
	
	List<Map<String, Object>> selectlastTransfer(Map<String, Object> params);		
}
