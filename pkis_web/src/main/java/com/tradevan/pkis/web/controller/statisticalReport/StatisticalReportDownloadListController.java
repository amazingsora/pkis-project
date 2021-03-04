package com.tradevan.pkis.web.controller.statisticalReport;

import java.io.File;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.tradevan.pkis.web.service.statisticalReport.StatisticalReportDownloadService;
import com.tradevan.xauthframework.web.controller.DefaultController;

/**
 * 作 業 代 碼 ：StatisticalReportDownloadListController<br>
 * 作 業 名 稱 ：報表統計下載<br>
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
@RequestMapping("/statisticalReport/downloadList")
public class StatisticalReportDownloadListController extends DefaultController {

	@Autowired
	StatisticalReportDownloadService statisticalReportDownloadService;

	/**
	 * 初始化頁面
	 * 
	 * @return
	 */
	@RequestMapping("/")
	public ModelAndView init() {
		ModelAndView mv = new ModelAndView("/statisticalReport/statisticalReportDownloadLsit");
		return mv;
	}

	/**
	 * 分頁查詢
	 * 
	 * @param request
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/query")
	public @ResponseBody Object query(HttpServletRequest request, @RequestParam Map<String, Object> params)
			throws Exception {
		return statisticalReportDownloadService.selectStatisticalReportDownloadList(params);
	}

	/**
	 * 檔案下載
	 * 
	 * @param request
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/download")
	public void downloadFile(HttpServletRequest request, HttpServletResponse resonse) throws Exception {
		String rptId = request.getParameter("rptId");
		String downloadPath = statisticalReportDownloadService.getDownloadPath(rptId);
		if (StringUtils.isNotBlank(downloadPath)) {
			File file = new File(downloadPath);
			this.downloadFile(request, resonse, file);
		}
	}
}
