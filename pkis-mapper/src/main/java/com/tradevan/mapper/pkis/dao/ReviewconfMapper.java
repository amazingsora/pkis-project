package com.tradevan.mapper.pkis.dao;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tradevan.mapper.pkis.model.Reviewconf;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author system
 * @since 2020-07-30
 */
public interface ReviewconfMapper extends BaseMapper<Reviewconf> {

	List<Map<String, Object>> selectContractReviewList(Map<String, Object> params);
	
}
