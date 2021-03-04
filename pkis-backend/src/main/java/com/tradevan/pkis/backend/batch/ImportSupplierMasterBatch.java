package com.tradevan.pkis.backend.batch;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import com.tradevan.pkis.backend.service.ImportSupplierMasterService;
import com.tradevan.pkis.backend.utils.Const;
import com.tradevan.xauthframework.common.bean.ProcessResult;

public class ImportSupplierMasterBatch extends CommonBatch {
	
	private ImportSupplierMasterService service = new ImportSupplierMasterService();
	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		ImportSupplierMasterBatch main = new ImportSupplierMasterBatch();
		String arg = "";
		if (args.length > 0) {
			arg = args[0];
		}
		main.init(ImportSupplierMasterBatch.class);
		main.runLoop(arg, main.getClass().getSimpleName());
	}
	
	private void init() throws Exception {
		service = (ImportSupplierMasterService) ctx.getBean("ImportSupplierMasterService");
	}
	
	@Override
	protected String process(String arg) throws Exception {
		logger.info("START");
		this.init();
		String module = "";
		String filePath = "";
		List<String> suppliermasterTxtList = new ArrayList<String>();
		List<String> errorMessageList = new ArrayList<String>();
		Map <String,String> modulemap = new HashMap<String, String>();
		String errorMsgResult = "";
		
		try {
			Map<String,List<String>> contractmap=new HashMap<String,List<String>>();
			// init
//			//下載Sftp至本地
//			SftpUtil.get(Const.SFTP_SERVER_FILE,Const.SFTP_LOCAL_SUPPLIERMASTER_FILE);
			modulemap = getFileList(Const.SFTP_LOCAL_SUPPLIERMASTER_FILE);
			//檔案讀取
			for (String key : modulemap.keySet()) {
				filePath = Const.SFTP_LOCAL_SUPPLIERMASTER_FILE + modulemap.get(key) ;
				logger.info("有效檔案路徑 ===" + filePath);
				if(checkFile(filePath)) {
					FileInputStream fileStream = new FileInputStream(new File(filePath));
					suppliermasterTxtList = IOUtils.readLines((fileStream),"UTF-8");
					if(suppliermasterTxtList != null) {
						contractmap.put(key, suppliermasterTxtList );
					}
					fileStream.close();
				}
				else {
					logger.info(key + "檔案不存在");
				}
			}		
			if(contractmap.size()>0){
				for (String key : contractmap.keySet()) {
					 suppliermasterTxtList = contractmap.get(key);
					 module = key;
					 //NSC匯入檔案
					 if(key.equals("NSC")) {
						 logger.info("開始匯入非制式供應商資料");
						 int size = suppliermasterTxtList.size() - 1;
						 for(int i = 8 ; i < size ; i++) {
							 try {
								 if((i % 100) == 0) {
									 logger.info("正匯入第"+i+"筆資料");
								 }
								if(StringUtils.isBlank(suppliermasterTxtList.get(i))) {
									continue;
								}else {
									i++;
									ProcessResult processResult = service.saveNSCSuppliermaster(suppliermasterTxtList.get(i));
									if(StringUtils.equals(processResult.getStatus(), ProcessResult.NG)) {
										for(String errorMsg : processResult.getMessages()){
											errorMessageList.add(errorMsg);
										}
										errorMessageList.add(" ### Error Data ###  " + suppliermasterTxtList.get(i));
									}
								}
							}catch(Exception e){
								logger.info("第" + i + "筆失敗，原因為" + e.getMessage());
								errorMessageList.add("供應商資料第" + i + "筆失敗，原因為" + e.getMessage());
								errorMessageList.add(" ### Error Data ###  " + suppliermasterTxtList.get(i));
							}
						}
				 }
				 //SC匯入檔案
				 else {
					 logger.info("開始匯入制式供應商資料 ");
					 int i = 0;
						for(String data : suppliermasterTxtList) {
							if((i % 100) == 0 && i != 0) {
								logger.info("正匯入第"+i+"筆資料");
							}
							try {    
								if(StringUtils.isBlank(data)) {
									continue;
								}else {
									i++;
									ProcessResult processResult = service.saveSCSuppliermaster(data);
									if(StringUtils.equals(processResult.getStatus(), ProcessResult.NG)) {
										for(String errorMsg : processResult.getMessages()){
											errorMessageList.add(errorMsg);
										}
										errorMessageList.add(" ### Error Data ###  " + suppliermasterTxtList.get(i));
									}
								}
							}catch(Exception e){
								logger.info("第" + i + "筆失敗，原因為" + e.getMessage());
								errorMessageList.add("供應商資料第" + i + "筆失敗，原因為" + e.getMessage());
								errorMessageList.add(" ### Error Data ###  " + data);
							}
						}
				 	}
				}	
			}
			else {
				logger.info("無有效檔案或無新資料");
			}
			deleteFileBySupplierMaster(Const.SFTP_LOCAL_SUPPLIERMASTER_FILE);
		}catch(Exception e) {
			logger.error(e, e);
			errorMessageList.add(e.getMessage());
		}finally {
			logger.info("###### SupplierMaster"+module+"ErrorTask END ######");
		}
		errorMsgResult = createTxt(errorMessageList, "SupplierMaster" + module + "ErrorTask_" + new SimpleDateFormat("yyyyMMdd").format(new Date()) + ".txt");
		
		return errorMsgResult;
	}
	public String createTxt(List<String> errorMessageList, String fileName) {
		String filePath = Const.SFTP_LOCAL_FILE;
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
				//java.nio.file.Files;
				Files.createDirectories(path);
				File file = new File(filePath + fileName);
				file.createNewFile();
				  
				FileWriter writer = new FileWriter(file);
				writer.write(errorMsg);
				writer.flush();
				writer.close();
			}
		} catch (IOException e) {
		  logger.error("Failed to create directory!" + e.getMessage());
		}
		
		return errorMsg;
	}
	
	public boolean checkFile(String filePath) {
		File file = new File(filePath);
		return file.exists();
	}
	
	public Map <String,String>  getFileList(String filePath) throws IOException {
		long filesetSCtime = 0;
		long filesetNCtime = 0;
		Map <String, String> modulemap = new HashMap<String, String>();
	    File file = new File(filePath);
	    if(file.exists()){
	    	File[] files = file.listFiles();
	  		for(int i = 0; i < files.length; i++) {
	  			if (!files[i].isDirectory()) {
	  				//取得建立時間
	  				Path path = Paths.get(filePath+files[i].getName());
	  				BasicFileAttributes attrs = Files.readAttributes(path, BasicFileAttributes.class);
	  				long filedate = attrs.creationTime().toMillis();
	  			
	  				if(files[i].getName().indexOf("vendor") > -1) {
	  		  			if(filedate > filesetSCtime) {
		  					modulemap.put("SC", files[i].getName());
		  					filesetSCtime = filedate;
	  		  			}
	  				}
	  				else  if(files[i].getName().indexOf("MSOVN") > -1) {
	  		  			if(filedate > filesetNCtime) {
			  				modulemap.put("NSC", files[i].getName());
			  				filesetNCtime = filedate;
	  		  			}
	  				}
	  			}
	  		}
	    }
		return modulemap;
	}
	
	//刪除所有供應商匯入檔案
	public void  deleteFileBySupplierMaster(String filePath) {
	    File file = new File(filePath);
	    if(file.exists()){
	    	File[] files = file.listFiles();
	  		for(int i = 0; i<files.length; i++) {
	  			if (!files[i].isDirectory()) {
  					if(files[i].getName().indexOf("vendor") > -1 || files[i].getName().indexOf("MSOVN") > -1) {
  						files[i].delete();
  					}
	  			}
	  		}
	    }	
	}
}
