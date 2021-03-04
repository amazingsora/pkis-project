package com.tradevan.pkis.backend.batch;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;

import com.tradevan.mapper.pkis.model.Batchparamset;
import com.tradevan.pkis.backend.config.DefaultBatch;
import com.tradevan.pkis.backend.service.CommonService;

public abstract class CommonBatch extends DefaultBatch {
	
	protected Log logger = LogFactory.getLog(getClass());
	
	protected static ApplicationContext ctx;
	
	protected CommonService service = new CommonService();
	
	protected void init(Class<?> c) throws Exception {
		ctx = SpringApplication.run(c);
		service = (CommonService) ctx.getBean("CommonService");
	}
	
	/**
	 * 批次迴圈
	 * @param arg
	 * @param className
	 * @throws Exception
	 */
	protected void runLoop(String arg, String className) throws Exception {
		logger.info("Batch Name : " + className);
		Batchparamset batchData = service.getBatchData(className);
		if(batchData == null) {
			logger.error("查無Batch Name : " + className + "設定檔資料");
		} else {
			String msg = "";
			while(StringUtils.equals("1", batchData.getStatus())) {
				msg = process(arg);
				service.updateBatchData(className, msg);
				service.sendErrorMail(className, msg);
				logger.info("Batch Name : " + className + ", 時間 : " + new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()));
				TimeUnit.SECONDS.sleep(batchData.getSleepsec());
				batchData = service.getBatchData(className);
			}
		}
		logger.info("end");
	}
	
	/**
	 * batch內容
	 * @param arg
	 * @throws Exception
	 */
	protected abstract String process(String arg) throws Exception;
	
}
