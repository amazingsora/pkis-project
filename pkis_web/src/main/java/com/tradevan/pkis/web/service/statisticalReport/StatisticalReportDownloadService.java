package com.tradevan.pkis.web.service.statisticalReport;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tradevan.mapper.pkis.dao.StatisticalReportMapper;
import com.tradevan.mapper.pkis.model.StatisticalReport;
import com.tradevan.mapper.xauth.dao.XauthSysCodeMapper;
import com.tradevan.mapper.xauth.dao.XauthUsersMapper;
import com.tradevan.mapper.xauth.model.XauthSysCode;
import com.tradevan.mapper.xauth.model.XauthUsers;
import com.tradevan.pkis.web.service.common.CommonService;
import com.tradevan.pkis.web.util.ElasticSearchUtil;
import com.tradevan.xauthframework.common.utils.JsonUtils;
import com.tradevan.xauthframework.core.security.UserInfo;
import com.tradevan.xauthframework.dao.bean.GridResult;
import com.tradevan.xauthframework.web.service.DefaultService;

@Service("StatisticalReportDownloadService")
@Transactional(rollbackFor = Exception.class)

public class StatisticalReportDownloadService extends DefaultService {

	@Autowired
	StatisticalReportMapper statisticalReportMapper;
	
	@Autowired
	XauthUsersMapper xauthUsersMapper;
	
	@Autowired
	XauthSysCodeMapper xauthSysCodeMapper;
	
	@Autowired
	CommonService commonService;
	
	/**
	 * 查詢統計報表下載清單
	 * @param params
	 * @return
	 */
	public GridResult selectStatisticalReportDownloadList(Map<String, Object> params) throws Exception {
		GridResult gridResult = null;
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		// 查詢資料庫條件
		UserInfo userInfo = userContext.getCurrentUser();
		String user = userInfo.getUsername();
		String module = MapUtils.getString(params, "module");
		String statisticalReportType = MapUtils.getString(params, "statisticalReportType");
		String statisticalReportTypeName = MapUtils.getString(params, "statisticalReportTypeName");
		QueryWrapper<StatisticalReport> queryWrapper = new QueryWrapper<StatisticalReport>();

		if (!user.equals("admin")) {
			queryWrapper.eq("CREATEUSER", getCreOrUpdUser(null));
		}
		queryWrapper.like("RPTID", statisticalReportType);
		queryWrapper.orderByDesc("CREATEDATE");
		List<StatisticalReport> sRlist = statisticalReportMapper.selectList(queryWrapper);
		// 過濾合約模式
		if (sRlist.size() > 0) {
			sRlist = sRlist.stream().filter(item -> {
				Boolean ismodule;
				try {
					ismodule = JsonUtils.jsonArray2MapList(item.getJson())
							.get(JsonUtils.jsonArray2MapList(item.getJson()).size() - 1).get("module").equals(module);
				} catch (Exception e) {
					e.printStackTrace();
					return false;
				}
				return ismodule;
			}).collect(Collectors.toList());

			// 遍歷前端grid
			sRlist.forEach(item -> {
				
				Map<String, Object> resultMap = new LinkedHashMap<String, Object>();
				// 獲得檔案名稱
				String rptCname = "";
				try {
					rptCname = item.getRptid() + statisticalReportTypeName + "("
							+ getSysCode("CONTRACT_MODE_CODE", module) + ")";
				} catch (Exception e) {
					e.printStackTrace();
				}

				resultMap.put("DownloadPath", item.getDownloadpath());
				resultMap.put("RPTID", item.getRptid());
				resultMap.put("RPTCName", rptCname);
				// 報表建立人中文名
				String userid = item.getCreateuser().split(":")[1];
				QueryWrapper<XauthUsers> userqueryWrapper = new QueryWrapper<XauthUsers>();
				userqueryWrapper.eq("USER_ID", userid);
				List<XauthUsers> userNameList = xauthUsersMapper.selectList(userqueryWrapper);
				resultMap.put("CREATEUSER", userNameList.get(0).getUserCname());
				Date date = item.getCreatedate();
				resultMap.put("CREATEDATE", new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(date));
				resultList.add(resultMap);
			});
		}

		// 排序及分頁
		gridResult = this.grid(params);
		ElasticSearchUtil.sortPagination(gridResult, resultList, params);

		return gridResult;
	}
	
	/**
	 * 取得序號
	 * @param gp
	 * @param code
	 * @return
	 * @throws Exception 
	 */
	private String getSysCode(String gp, String code) throws Exception {
		String result = "";
		XauthSysCode xauthSysCode = commonService.getSysCodeData(gp, code);
		if(xauthSysCode != null) {
			result = xauthSysCode.getCname();
		}
		return result;
	}
	
	public String getDownloadPath(String rptId) {
		String result = "";
		QueryWrapper<StatisticalReport> statisticalReportWrapper = new QueryWrapper<StatisticalReport>();
		statisticalReportWrapper.eq("RPTID", rptId);
		List<StatisticalReport> statisticalReports = statisticalReportMapper.selectList(statisticalReportWrapper);
		if(statisticalReports != null && statisticalReports.size() > 0) {
			result = statisticalReports.get(0).getDownloadpath();
		}
		
		return result;
	}
}
