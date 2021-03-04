package com.tradevan.pkis.backend.service;

import java.io.File;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.tradevan.mapper.pkis.dao.StatisticalReportMapper;
import com.tradevan.mapper.pkis.model.StatisticalReport;
import com.tradevan.pkis.backend.config.DefaultService;

@Service("DelStatisticalReportService")
@Transactional(rollbackFor = Exception.class)
public class DelStatisticalReportService extends DefaultService {

	public Log logger = LogFactory.getLog(getClass());
	
	@Autowired
	StatisticalReportMapper statisticalReportMapper;
	
	public void delStatisticalReport(String rptId, String downloadPath) throws Exception {
		File file = null;
		
		// 刪除資料夾
		try {
			if(StringUtils.isNotBlank(downloadPath)) {
				file = new File(downloadPath);
				if(file.exists()) {
					file.delete();
					// 刪除DB資料
					statisticalReportMapper.deleteById(rptId);
					logger.info("已刪除檔案 : " + downloadPath);
				} else {
					logger.error("查無" + rptId + "的檔案路徑 : " + downloadPath + "，故無法刪除");
					throw new Exception("查無" + rptId + "的檔案路徑 : " + downloadPath + "，故無法刪除");
				}
			}
		} catch(Exception e) {
			logger.error("刪除此筆資料失敗 : " + rptId + " ，原因為 : " + e, e);
			throw new Exception(e);
		}
	
	}
	
	public List<StatisticalReport> getStatisticalReportDatas() {
		QueryWrapper<StatisticalReport> statisticalReportWrapper = new QueryWrapper<StatisticalReport>();
		statisticalReportWrapper.isNotNull("DOWNLOADPATH");
		
		return statisticalReportMapper.selectList(statisticalReportWrapper);
	}
}
