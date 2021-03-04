package com.tradevan.pkis.backend.batch;

import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.tradevan.mapper.pkis.model.StatisticalReport;
import com.tradevan.pkis.backend.service.DelStatisticalReportService;
import com.tradevan.pkis.backend.utils.Const;

public class DelStatisticalReportBatch extends CommonBatch {

	public Log logger = LogFactory.getLog(getClass());
	
	private DelStatisticalReportService service = new DelStatisticalReportService();
	
	public static void main(String[] args) throws Exception {
		DelStatisticalReportBatch main = new DelStatisticalReportBatch();
		String arg = "";
		if (args.length > 0) {
			arg = args[0];
		}
		main.init(DelStatisticalReportBatch.class);
		main.runLoop(arg, main.getClass().getSimpleName());
	}
	
	private void init() throws Exception {
		service = (DelStatisticalReportService) ctx.getBean("DelStatisticalReportService");
	}
	
	@Override
	protected String process(String arg) throws Exception {
		this.init();
		List<String> errorMessageList = new ArrayList<String>(); // 記錄錯誤筆數原因
		String errorMsg = "";
		
		List<StatisticalReport>  statisticalReportDatas = service.getStatisticalReportDatas();
		int delFileCnt = 0;
		for(StatisticalReport data : statisticalReportDatas) {
			try {
				service.delStatisticalReport(data.getRptid(), data.getDownloadpath());
				delFileCnt ++;
			} catch(Exception e) {
				errorMessageList.add(e.getMessage());
			}
		}
		logger.info("共刪除 " + delFileCnt + " 筆資料");
		errorMsg = createTxt(errorMessageList);
		
		return errorMsg;
	}
	
	public String createTxt(List<String> errorMessageList) {
		String fileName = Const.SFTP_LOCAL_FILE;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String errorMsg = "";
		try {
			if(errorMessageList.size() > 0) {
				Path path = Paths.get(fileName);
				StringBuffer msg = new StringBuffer();
				
				for(String errMsg : errorMessageList) {
					msg.append(errMsg);
					msg.append("\n");
				}
				errorMsg = msg.toString();
				Files.createDirectories(path);
				File file = new File(fileName + "delStatisticalReportBatch_" + sdf.format(new Date()) + ".txt");
				file.createNewFile();
				
				FileWriter writer = new FileWriter(file);
				writer.write(errorMsg);
				writer.close();
			}
		} catch(Exception e) {
			logger.error("Failed to create directory!" + e.getMessage());
		}
		
		return errorMsg;
	}
}
