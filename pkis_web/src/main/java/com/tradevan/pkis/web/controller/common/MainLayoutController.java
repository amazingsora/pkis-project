package com.tradevan.pkis.web.controller.common;

import java.io.File;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.tradevan.pkis.web.service.common.MainLayoutService;
import com.tradevan.xauthframework.common.bean.ProcessResult;
import com.tradevan.xauthframework.web.controller.DefaultController;

/**
 * 作 業 代 碼 ：MAIN_LAYOUT<br>
 * 作 業 名 稱 ：nav bar管理<br>
 * 程 式 代 號 ：MainLayoutController<br>
 * 描             述 ：<br>
 * 公             司 ：Tradevan Co.<br><br>
 *【 資 料 來 源】  ：<br>
 *【 輸 出 報 表】  ：<br>
 *【 異 動 紀 錄】  ：<br>
 * @author   : Yuan<br>
 * @version  : 1.0.0 2020/12/14<P>
 */
@RestController
@RequestMapping("/main/layout")
public class MainLayoutController extends DefaultController {
	
	@Autowired
	MainLayoutService mainLayoutService;
	
	@RequestMapping("/getFiles")
	public @ResponseBody Object getFiles(HttpServletRequest request, @RequestParam Map<String, Object> params) throws Exception {
		return mainLayoutService.getFiles();
	}
	
	@RequestMapping("/download")
	public void downloadFile(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String fileNameKey = request.getParameter("fileNameKey");
		String filePath = mainLayoutService.getFilePath(fileNameKey);
		File file = new File(filePath);
		this.downloadFile(request, response, file);
    }
}
