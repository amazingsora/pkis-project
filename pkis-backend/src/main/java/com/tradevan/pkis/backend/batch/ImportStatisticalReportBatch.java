package com.tradevan.pkis.backend.batch;

import java.io.File;
import java.io.FileWriter;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.tradevan.mapper.pkis.model.StatisticalReport;
import com.tradevan.pkis.backend.service.ImportStatisticalReportService;
import com.tradevan.pkis.backend.utils.Const;
import com.tradevan.xauthframework.common.utils.JsonUtils;

public class ImportStatisticalReportBatch extends CommonBatch {
	
	public static String LABEL_MAIN = "【報表統計排程作業】";
	private ImportStatisticalReportService service=new ImportStatisticalReportService();

	public static void main(String[] args) throws Exception {
		ImportStatisticalReportBatch main = new ImportStatisticalReportBatch();
		String arg = "";
		if (args.length > 0) {
			arg = args[0];
		}
		main.init(ImportStatisticalReportBatch.class);
		main.runLoop(arg, main.getClass().getSimpleName());
	}

	private void init() throws Exception {
		logger.info(LABEL_MAIN + "初始化");
		service = (ImportStatisticalReportService) ctx.getBean("ImportStatisticalReportService");
	}
	
	@Override
	protected String process(String arg) throws Exception {
		this.init();
		logger.info(LABEL_MAIN + "START");
		logger.info("arg == " + arg);
        long startTimeTotal = 0;
        long endTimeTotal = 0;
        double spendTimeTotal = 0L; // 方法花費多少時間 (單位:秒)
    	List<StatisticalReport> srlist = service.getDataList();
        List<String> errorMessageList = new ArrayList<String>(); // 記錄錯誤筆數原因
        int showLogPer = 100;
        String errorMsg = "";
        
        if(srlist.size() > 0 ) {
		try {
			startTimeTotal = System.currentTimeMillis();
			
			srlist.forEach(item -> {
				int j = 1;
				String keyArray = item.getRptid().substring(item.getRptid().length() -6,item.getRptid().length());   //截取筆數
				try {
					List <Map<String,Object>> datalist = JsonUtils.jsonArray2MapList(item.getJson());
					service.createStatisticalReport(datalist);

					BigDecimal tmp = new BigDecimal(j);
					if(((tmp.divideAndRemainder(new BigDecimal(showLogPer)))[1]).intValue() == 0) {
						logger.info("儲存筆數：" + j);
					}
				} catch(Exception e) {
//					e.printStackTrace();
					logger.info("第" + keyArray + "筆錯誤，原因為 : " + e.getMessage());
					errorMessageList.add("統計報表第" + keyArray + "筆錯誤，原因為 : " + e.getMessage());
				}
				j ++;	
			});
			errorMsg = createTxt(errorMessageList);
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			endTimeTotal = System.currentTimeMillis() ;
			spendTimeTotal = (double)(endTimeTotal - startTimeTotal) / 1000 ;
			logger.info(LABEL_MAIN + "END");
		}
		}
		// 總花費時間
        logger.info(LABEL_MAIN + "總花費時間：" + spendTimeTotal + " 秒");
		
		return errorMsg;
	}
	
	public String createTxt(List<String> errorMessageList ) {
		String fileName = Const.STATISTICALREPORT_FIELD_SAVEPATH + "//";
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
				File file = new File(fileName + "importStatisticalReportTask_ " + sdf.format(new Date()) + ".txt");
				file.createNewFile();
				  
				FileWriter writer = new FileWriter(file);
				writer.write(errorMsg);
				writer.close();
			}
		} catch (Exception e) {
			logger.error("Failed to create directory!" + e.getMessage());
		}
		
		return errorMsg;
	}
}
