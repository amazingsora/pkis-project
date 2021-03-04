package com.tradevan.mapper.contract.dao;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

public interface ContractMapper extends BaseMapper<Object> {
	
	List<Map<String, Object>> selectContractList(Map<String, Object> params);
	
	List<Map<String, Object>> selectContracts(Map<String, Object> params);
	
	List<Map<String, Object>> selectPerConTeplate(Map<String, Object> params);
	
	List<Map<String, Object>> selectRoleAndName(Map<String, Object> params);
	
	List<Map<String, Object>> selectflowid(Map<String, Object> params);
	
	List<Map<String, Object>> selectContractReport(Map<String, Object> params);
	
	List<Map<String, Object>> selectSysCode(Map<String, Object> params);
		
	List<Map<String, Object>> selectContractMaster(Map<String, Object> params);
			
	List<Map<String, Object>> selectDraftConList(Map<String, Object> params);
	
	List<Map<String, Object>> selectDocstate(Map<String, Object> params);
	
	List<Map<String, Object>> selectContractMasterByTask1(Map<String, Object> params);
	
	List<Map<String, Object>> selectACD(Map<String, Object> params);

}
