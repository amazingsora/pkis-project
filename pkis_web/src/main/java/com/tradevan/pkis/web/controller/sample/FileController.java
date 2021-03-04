package com.tradevan.pkis.web.controller.sample;

import java.io.File;
import java.text.MessageFormat;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.tradevan.xauthframework.common.bean.ProcessResult;
import com.tradevan.xauthframework.common.utils.MapUtils;
import com.tradevan.xauthframework.common.utils.StrUtils;
import com.tradevan.xauthframework.web.bean.FileBean;
import com.tradevan.xauthframework.web.controller.DefaultController;

@RestController
@RequestMapping("/sample/file")
public class FileController extends DefaultController {

	/**
	 * 初始化頁面
	 * @return
	 */
	@RequestMapping("/")
	public ModelAndView init() {
		return new ModelAndView("/sample/file");
	}
	
	@RequestMapping("/upload")
	public @ResponseBody Object upload(HttpServletRequest request, @RequestParam Map<String, Object> params) throws Exception {
		ProcessResult result = new ProcessResult();
		result.setStatus(ProcessResult.NG);
		String txt = MapUtils.getString(params, "txt");
		if (StringUtils.isNotBlank(txt)) {
			result.addMessage(params.get("txt").toString());
		}	
		
		List<FileBean> fileBeanList = this.getFileBeanList(request);
		if (fileBeanList != null && fileBeanList.size() > 0) {
			for (FileBean fileBean : fileBeanList) {				
				String message = MessageFormat.format("fileName:{0}, fileSize:{1}, fileExt:{2}", fileBean.getFileName(), StrUtils.readableFileSize(fileBean.getFileSize()), fileBean.getFileExt());
				result.addMessage(message);
			}
			result.setStatus(ProcessResult.OK);	
		}
		return result;
	}

	@RequestMapping("/download")
	public void downloadFile(HttpServletRequest request, HttpServletResponse resonse) throws Exception {
		File file = new File("src/testFile/testPDF.pdf");
		this.downloadFile(request, resonse, file);
    }
	
}
