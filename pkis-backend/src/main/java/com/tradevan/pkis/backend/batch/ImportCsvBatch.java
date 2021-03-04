package com.tradevan.pkis.backend.batch;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
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

import org.apache.commons.lang3.StringUtils;

import com.tradevan.pkis.backend.bean.XauthUsersBean;
import com.tradevan.pkis.backend.service.ImportCsvBatchService;
import com.tradevan.pkis.backend.utils.Const;
import com.tradevan.xauthframework.common.utils.MapUtils;

public class ImportCsvBatch extends CommonBatch {

	public static String LABEL_MAIN = "【csv排程作業】";
	
	private ImportCsvBatchService service = new ImportCsvBatchService();
	
	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		ImportCsvBatch main = new ImportCsvBatch();
		String arg = "";
		if(args.length > 0) {
			arg = args[0];
		}
		main.init(ImportCsvBatch.class);
		main.runLoop(arg, main.getClass().getSimpleName());
	}
	
	private void init() throws Exception {
		logger.info(LABEL_MAIN + "初始化");
		service = (ImportCsvBatchService) ctx.getBean("ImportCsvBatchService");
		//下載SFTP檔案至本地
//		SftpUtil.get(Const.SFTP_SERVER_FILE,Const.SFTP_LOCAL_CSV_FILE,"CSV");

	}
	
	@Override
	protected String process(String arg) throws Exception {
		logger.info(LABEL_MAIN + "START");
		logger.info("arg == " + arg);
        long startTimeTotal = 0;
        long endTimeTotal = 0;
        double spendTimeTotal = 0L; // 方法花費多少時間 (單位:秒)
        
        long startTimeDept = 0;
        long endTimeDept = 0;
        double spendTimeDept = 0L; // 方法花費多少時間 (單位:秒)
        
        long startTimeEmp = 0;
        long endTimeEmp = 0;
        double spendTimeEmp = 0L; // 方法花費多少時間 (單位:秒)
        Map<String, String> xauthDeptMap = new HashMap<String, String>(); // 部門資料
        Map<String, String> xauthRolesMap = new HashMap<String, String>(); // 角色資料
        List<XauthUsersBean> xauthUsers = new ArrayList<XauthUsersBean>(); // 人員資料
        List<String> errorMessageList = new ArrayList<String>(); // 記錄錯誤筆數原因
        int showLogPer = 100;
        String errorMsg = "";
		
		try {
			
			this.init();
			startTimeTotal = System.currentTimeMillis();
//			tablemap = getFileList(Const.SFTP_LOCAL_SUPPLIERMASTER_FILE);
//			for(String key:tablemap.keySet()) {
//				String filePath=Const.SFTP_LOCAL_SUPPLIERMASTER_FILE +tablemap.get(key) ;
//				if(checkFile(filePath)) {
//					logger.info("讀取 : "+tablemap.get(key));
//					if(key.toLowerCase().indexOf("emp") > -1) {
//						xauthUsers = service.readUserCsvFile(filePath , arg);
//					}
//					else if(key.toLowerCase().indexOf("org") > -1) {
//						xauthDeptMap = service.readDeptCsvFile(filePath);
//					}
//					else if(key.toLowerCase().indexOf("title") > -1) {
//						xauthRolesMap = service.readRoleCsvFile(filePath);
//					}
//				}
//				else logger.info("該檔案不存在 : "+tablemap.get(key));
//			}
			//測試
			xauthDeptMap = service.readDeptCsvFile(Const.SFTP_LOCAL_FILE + "\\20210225\\" + "org_20210114.csv");
			xauthRolesMap = service.readRoleCsvFile(Const.SFTP_LOCAL_FILE + "\\20210225\\" +  "TITLE_20210114.CSV");
			xauthUsers = service.readUserCsvFile(Const.SFTP_LOCAL_FILE + "\\20210225\\" +  "emp_20210114.csv", arg);
//			xauthDeptMap = service.readDeptCsvFile(Const.SFTP_LOCAL_FILE + "\\20200831\\temp\\" + "org_20200831.csv");
//			xauthRolesMap = service.readRoleCsvFile(Const.SFTP_LOCAL_FILE + "\\20200831\\temp\\" +  "TITLE_20200830.CSV");
//			xauthUsers = service.readUserCsvFile(Const.SFTP_LOCAL_FILE + "\\20200831\\temp\\" +  "emp_20200831.csv", arg);
			
			int j = 1;
			startTimeDept = System.currentTimeMillis();
			if(xauthDeptMap != null && xauthDeptMap.size() > 0) {
				for(String key : xauthDeptMap.keySet()) {
					String[] deptData = null;
					String[] keyArray = null;
					
					try {
						deptData = MapUtils.getString(xauthDeptMap, key).split(",");
						keyArray = key.split("_");
						service.saveDept(keyArray[0], deptData);
						
						BigDecimal tmp = new BigDecimal(j);
						if(((tmp.divideAndRemainder(new BigDecimal(showLogPer)))[1]).intValue() == 0) {
							logger.info("儲存筆數：" + j);
						}
					} catch(Exception e) {
	//					e.printStackTrace();
						logger.info("第" + keyArray[1] + "筆錯誤，原因為 : " + e.getMessage());
						errorMessageList.add("部門資料第" + keyArray[1] + "筆錯誤，原因為 : " + e.getMessage());
					}
					if((j % 100) == 0) {
						logger.info("處理第" + j + "筆");
					}
					j ++;
				}
				endTimeDept = System.currentTimeMillis() ;
				spendTimeDept = (double)(endTimeDept - startTimeDept) / 1000 ;
				logger.info(LABEL_MAIN + "部門花費時間：" + spendTimeDept + " 秒");
				
			}
			
			else logger.info("該檔案不存在或無須更新");

			if(xauthUsers.size() > 0 && xauthDeptMap.size() >0 &&xauthRolesMap.size() > 0) {
				int i = 1;
				startTimeEmp = System.currentTimeMillis();
				for(XauthUsersBean entity : xauthUsers) {
					try {
						if(StringUtils.equals(arg, "init")) {
							if(StringUtils.equals(entity.getOnJob(), "Y")) {
								service.save(i, entity, xauthDeptMap, xauthRolesMap, errorMessageList);
							}
						} else {
							service.save(i, entity, xauthDeptMap, xauthRolesMap, errorMessageList);
						}
						
						BigDecimal tmp = new BigDecimal(i);
						if(((tmp.divideAndRemainder(new BigDecimal(showLogPer)))[1]).intValue() == 0) {
							logger.info("儲存筆數：" + i);
						}
					} catch(Exception e) {
						e.printStackTrace();
					}
					if((i % 100) == 0) {
						logger.info("處理第" + i + "筆");
					}
					i ++;
				}
				endTimeEmp = System.currentTimeMillis() ;
				spendTimeEmp = (double)(endTimeEmp - startTimeEmp) / 1000 ;
				logger.info(LABEL_MAIN + "人員花費時間：" + spendTimeEmp + " 秒");
				
				// 更新XauthUsers enabled狀態
				service.updXauthUsers(errorMessageList);
			}
			else logger.info("該檔案不存在或無須更新");
			
//			deleteFileByCsv(Const.SFTP_LOCAL_CSV_FILE);
		} catch(Exception e) {
			logger.error(e, e);
			errorMessageList.add(e.getMessage());
		} finally {
			endTimeTotal = System.currentTimeMillis() ;
			spendTimeTotal = (double)(endTimeTotal - startTimeTotal) / 1000 ;
			logger.info(LABEL_MAIN + "END");
		}
		errorMsg = createTxt(errorMessageList);
		// 總花費時間
		logger.info(LABEL_MAIN + "總花費時間：" + spendTimeTotal + " 秒");
		
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
				File file = new File(fileName + "importCsvTask_" + sdf.format(new Date()) + ".txt");
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
	public boolean checkFile(String filePath) {
	      File file = new File(filePath);
	      return file.exists();
	   
	}
	
	public Map <String,String>  getFileList(String filePath) throws IOException {
		//取得檔名List
		Map <String,String> modulemap = new HashMap<String, String>();
	      File file = new File(filePath);
	      SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
	      String nowtime = sdf.format(new Date());
	      if(file.exists()){
	    	
	  			File[] files = file.listFiles();
	  			for(int i=0; i < files.length; i++) {
	  				if (!files[i].isDirectory()) {
	  					//取得建立時間
	  					Path path = Paths.get(filePath + files[i].getName());
	  					BasicFileAttributes attrs = Files.readAttributes(path, BasicFileAttributes.class);
	  					String filedate = sdf.format(attrs.creationTime().toMillis());
	  					if(nowtime.equals(filedate)) {
	  						if(files[i].getName().indexOf("emp")>-1) {
	  							modulemap.put("emp", files[i].getName());
	  						}
	  						else  if(files[i].getName().indexOf("org")>-1) {
	  							modulemap.put("org", files[i].getName());
	  						}
	  						else  if(files[i].getName().indexOf("TITLE")>-1) {
	  							modulemap.put("title", files[i].getName());
	  						}
	  					}
	  					else	logger.info("該檔案 ::" + files[i].getName()+"無須更新");
	  				}
	  			}
	      }
			return modulemap;
	}
	
	//刪除所有匯入檔案
	public void  deleteFileByCsv(String filePath) {
	    File file = new File(filePath);
	    if(file.exists()){
	    	File[] files = file.listFiles();
	  		for(int i = 0; i < files.length; i++) {
	  			if (!files[i].isDirectory()) {
  					if(files[i].getName().indexOf("emp") > -1 || files[i].getName().indexOf("org") > -1 || files[i].getName().indexOf("TITLE") > -1) {
  						files[i].delete();
    				}
	  			}
	  		}
	    }	
	}
	
}
