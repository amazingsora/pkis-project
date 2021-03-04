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

import com.tradevan.pkis.backend.service.ImportShortagepenaltyService;
import com.tradevan.pkis.backend.utils.Const;

public class ImportShortagepenaltyBatch extends CommonBatch {

	private ImportShortagepenaltyService service = new ImportShortagepenaltyService();
	
	public static void main(String[] args) throws Exception {
		ImportShortagepenaltyBatch main = new ImportShortagepenaltyBatch();
		String arg = "";
		if(args.length > 0) {
			arg = args[0];
		}
		main.init(ImportShortagepenaltyBatch.class);
		main.runLoop(arg, main.getClass().getSimpleName());
	}
	
	private void init() throws Exception {
		service = (ImportShortagepenaltyService) ctx.getBean("ImportShortagepenaltyService");
	}
	
	@Override
	protected String process(String arg) throws Exception {
		List<String> errorMessageList = new ArrayList<String>(); // 記錄錯誤筆數原因
		String errorMsg = "";
		try {
			this.init();
			service.save(Const.INFORMATION_FIELD_DATAPATH + "SHORTAGEPENALTY.xlsx", errorMessageList);
			errorMsg = createTxt(errorMessageList);
		} catch(Exception e) {
			e.printStackTrace();
			logger.error("ERROR : " + e, e);
		}
		
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
				File file = new File(fileName + "importShortagepenaltyBatch_" + sdf.format(new Date()) + ".txt");
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
