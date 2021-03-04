package com.tradevan.mapper.pkis.dao;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tradevan.mapper.pkis.model.Suppliermaster;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author system
 * @since 2020-07-22
 */
public interface SuppliermasterMapper extends BaseMapper<Suppliermaster> {

	List<Map<String, Object>> selectSupplierList(Map<String, Object> params);
	
	List<Map<String, Object>> selectSuppliercodeList(Map<String, Object> params);
	
}
