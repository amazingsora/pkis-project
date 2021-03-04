package com.tradevan.mapper.pkis.dao;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tradevan.mapper.pkis.model.Codelist;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author system
 * @since 2020-07-22
 */
public interface CodelistMapper extends BaseMapper<Codelist> {

	List<Map<String, Object>> insertCodeList(Codelist codeList);
	
	List<Map<String, Object>> searchSupplierCode(Map<String, Object> params);
	
	List<Map<String, Object>> updataSupplierCodeList(Map<String, Object> params);

}
