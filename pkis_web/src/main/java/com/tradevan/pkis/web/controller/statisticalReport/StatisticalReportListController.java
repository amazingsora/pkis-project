package com.tradevan.pkis.web.controller.statisticalReport;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.tradevan.pkis.web.service.statisticalReport.StatisticalReportService;
import com.tradevan.xauthframework.web.controller.DefaultController;

/**
 * 作 業 代 碼 ：statisticalReport<br>
 * 作 業 名 稱 ：報表統計<br>
 * 程 式 代 號 ：StatisticalReportController<br>
 * 描 述 ：<br>
 * 公 司 ：Tradevan Co.<br>
 * <br>
 * 【 資 料 來 源】 ：<br>
 * 【 輸 出 報 表】 ：<br>
 * 【 異 動 紀 錄】 ：<br>
 * 
 * @author : <br>
 * @version :
 *          <P>
 */
@RestController
@RequestMapping("/statisticalReport/list")
public class StatisticalReportListController extends DefaultController {
	@Autowired
	StatisticalReportService statisticalReportService;

	/**
	 * 初始化頁面_統計報表資料
	 * 
	 * @return
	 */
	@RequestMapping("/")
	public ModelAndView init(HttpServletRequest request, @RequestParam Map<String, Object> params) {
		logger.info("傳入參數:" + params);
		ModelAndView mv = new ModelAndView("/statisticalReport/statisticalReportLsit");

		@SuppressWarnings("unchecked")
		Map<String, Object> resultinitdata = new HashedMap();
		resultinitdata.put("srt", MapUtils.getString(params, "statisticalReportType"));
		resultinitdata.put("module", MapUtils.getString(params, "hidemodule"));
		resultinitdata.put("year", MapUtils.getString(params, "hideyear"));

		if (resultinitdata.get("srt") != null && resultinitdata.get("srt").equals("CD")) {
			resultinitdata.put("SRT", "CD");
		} else if ((resultinitdata.get("srt") != null && resultinitdata.get("module") != null)
				&& resultinitdata.get("srt").equals("RS")) {
			resultinitdata.put("SRT", "RS");

		} else if (resultinitdata.get("srt") != null && resultinitdata.get("srt").equals("RS")) {
			resultinitdata.put("SRT", "RS");

		} else if (resultinitdata.get("srt") != null && resultinitdata.get("srt").equals("NTS")) {
			resultinitdata.put("SRT", "NTS");

		} else if (resultinitdata.get("srt") != null && resultinitdata.get("srt").equals("ACD")) {
			resultinitdata.put("SRT", "ACD");
		}
		mv.addObject("resultinitdata", resultinitdata);
		return mv;
	}

	/**
	 * 合約查詢Grid
	 * 
	 * @param request
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/query")
	public @ResponseBody Object query(HttpServletRequest request, @RequestParam Map<String, Object> params)
			throws Exception {
		System.out.println("傳入參數:" + params);
		logger.info("傳入參數:" + params);
		String statisticalReportType = MapUtils.getString(params, "statisticalReportType");
		if (statisticalReportType.equals("CD")) {
			return statisticalReportService.selectCDtList(params);
		} else if (statisticalReportType.equals("NTS")) {

			return statisticalReportService.selectNTSList(params);
		} else if (statisticalReportType.equals("RS")) {

			return statisticalReportService.selectRSList(params);
		} else if (statisticalReportType.equals("ACD")) {

			return statisticalReportService.selectACDList(params);
		}
		return null;
	}

	/**
	 * 寫入報表
	 * 
	 * @param request
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/insertData")
	public @ResponseBody Object insertData(HttpServletRequest request, @RequestParam Map<String, Object> params)
			throws Exception {
		return statisticalReportService.insertStatisticalReportDate(params);
	}

}
