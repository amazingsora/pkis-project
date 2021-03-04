package com.tradevan.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

public interface SequenceMapper extends BaseMapper<Object> {

	Long selectBatchparamsetSeq();
}
