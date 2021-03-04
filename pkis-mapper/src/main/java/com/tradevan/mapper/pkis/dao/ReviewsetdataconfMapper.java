package com.tradevan.mapper.pkis.dao;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tradevan.mapper.pkis.model.Reviewsetdataconf;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author system
 * @since 2020-07-29
 */
public interface ReviewsetdataconfMapper extends BaseMapper<Reviewsetdataconf> {

	List<Map<String, Object>> selectReviewsetdataconf(Map<String, Object> params);
	
	List<Map<String, Object>> selectFlowData(Map<String, Object> params);
}
