package com.tradevan.pkis.backend.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tradevan.mapper.pkis.dao.StatisticalReportMapper;
import com.tradevan.mapper.pkis.model.StatisticalReport;
import com.tradevan.pkis.backend.batch.ImportStatisticalReportBatch;
import com.tradevan.pkis.backend.config.DefaultService;
import com.tradevan.pkis.backend.utils.Const;
import com.tradevan.xauthframework.common.utils.MapUtils;

@Service("ImportStatisticalReportService")
@Transactional(rollbackFor = Exception.class)
public class ImportStatisticalReportService extends DefaultService {

	public static String LABEL_MAIN = ImportStatisticalReportBatch.LABEL_MAIN;
		
	@Autowired
	StatisticalReportMapper statisticalReportMapper;

	public List<StatisticalReport> getDataList() {
		// 獲取統計報表資訊
		List<StatisticalReport> srlist = new LinkedList<StatisticalReport>();
		QueryWrapper <StatisticalReport> queryWrapper = new QueryWrapper<StatisticalReport>();
		queryWrapper.isNull("DOWNLOADPATH");
		srlist = statisticalReportMapper.selectList(queryWrapper);
		return srlist;
	}
	
	/**
	 * 建立報表
	 * @void
	 */
	public void createStatisticalReport(List<Map<String, Object>> Datalist) throws Exception {
		Map<String, Object> datalastmap = Datalist.get(Datalist.size() - 1);
		String module = MapUtils.getString(datalastmap, "module");
		if (module == null) {
			module = "NSC";
		}
		String srt = MapUtils.getString(datalastmap, "srt");
		String rptid = MapUtils.getString(datalastmap, "rptid");
		String timeFrame = MapUtils.getString(datalastmap, "timeFrame");
		String date = MapUtils.getString(datalastmap, "date");
		String createuser = MapUtils.getString(datalastmap, "createuser");
		StringBuffer StatisticalReportCName = new StringBuffer();
		String moduleCName = module.equals("SC") ? "制式" : "非制式";
		StringBuffer fileName = new StringBuffer();

		try {
			FileInputStream fis = null;
			// 取得模板
			String modelPath = Const.STATISTICALREPORT_FIELD_MODELPATH;
			Map <String, String> modelMap = getStatisticalReportModel(modelPath);
			if (srt.equals("CD")) {
				fis = getModelFile("CD",modelMap,modelPath);
				StatisticalReportCName.append(moduleCName);
				StatisticalReportCName.append("電子合約案件資料");
				fileName.append(rptid).append("電子合約案件資料").append("(").append(moduleCName).append(")");
			
			}else if (srt.equals("NTS")) {
				fis = getModelFile("NTS",modelMap,modelPath);
				StatisticalReportCName.append("非制式合約類型比例表");
				fileName.append(rptid).append("非制式合約類型比例表");
				
			}else if (module.equals("SC") && srt.equals("RS")) {
				fis = getModelFile("RS_SC",modelMap,modelPath);
				StatisticalReportCName.append("制式審核進度表");
				fileName.append(rptid).append("制式審核進度表").append("(").append(moduleCName).append(")");
			
			}else if (module.equals("NSC") && srt.equals("RS")) {
				fis = getModelFile("RS_NSC",modelMap,modelPath);				
				StatisticalReportCName.append("非制式審核進度表");
				fileName.append(rptid).append("非制式審核進度表").append("(").append(moduleCName).append(")");
			
			}else if(srt.equals("ACD")) {
				fis = getModelFile("ACD",modelMap,modelPath);
				StatisticalReportCName.append(moduleCName);
				StatisticalReportCName.append("代理簽審合約案件資料");
				fileName.append(rptid).append("代理簽審合約案件資料").append("(").append(moduleCName).append(")");
			
			}
			if(fis != null) {
				XSSFWorkbook workBook = new XSSFWorkbook(fis);
				// 搜尋第一個sheet
				XSSFSheet sheet = workBook.getSheet("StatisticalReport");
				// 取得ROW
				Row row = sheet.getRow(0);
				// 取得cell
				Cell cell = row.getCell(0);
				// 設定cell裡的值
				// 設定報表種類
				cell.setCellValue(StatisticalReportCName.toString());
				// 設定報表區間
				row = sheet.getRow(1);
				row.createCell(1).setCellValue(timeFrame);
				row.createCell(7).setCellValue(date);
				// 設定製表人
				row = sheet.getRow(2);
				row.createCell(7).setCellValue(createuser);
				// 插入個別報表
				createSRTExcel(module, srt, sheet, Datalist);
				FileOutputStream fileOut;
				String svaePath = Const.STATISTICALREPORT_FIELD_SAVEPATH;
				File file = new File(svaePath);
				// 如果資料夾不存在則建立
				if (!file.exists() && !file.isDirectory()) {
					file.mkdir();
				}
				String downloadPath = svaePath + "/" + fileName.toString() + ".xlsx";
				fileOut = new FileOutputStream(svaePath + "/" + fileName.toString() + ".xlsx");
				// 把workBook寫進FileOutputStream
				workBook.write(fileOut);
				// 關閉FileOutputStream
				fileOut.close();
				fis.close();
				// 下載位置寫入資料庫
				QueryWrapper<StatisticalReport> queryWrapper = new QueryWrapper<StatisticalReport>();
				queryWrapper.eq("RPTID", rptid);
				List<StatisticalReport> list = statisticalReportMapper.selectList(queryWrapper);

				if (list.size() > 0) {
					StatisticalReport statisticalReport = list.get(0);
					statisticalReport.setDownloadpath(downloadPath);
					statisticalReport.setUpdateuser("System");
					statisticalReport.setUpdatedate(new Date());
					statisticalReportMapper.update(statisticalReport, queryWrapper);
				}
			}else {
				logger.error("查無此模板 :: SRT_"+srt+"_Model");
				throw new Exception("查無此模板 :: SRT_"+srt+"_Model");
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 建立EXCEL
	 * @void
	 */
	public void createSRTExcel(String module, String srt, XSSFSheet sheet, List<Map<String, Object>> Datalist) {
		if (srt.equals("CD")) {
			int count = 7;
			// 插入內容
			for (int i = 0; i < Datalist.size() - 1; i++) {
				Row row = sheet.createRow(count);
				Cell cell = row.createCell(0);
				cell.setCellValue(MapUtils.getString(Datalist.get(i), "承辦人員"));
				row.createCell(1).setCellValue(MapUtils.getString(Datalist.get(i), "合約模式"));
				row.createCell(2).setCellValue(MapUtils.getString(Datalist.get(i), "合約編碼"));
				row.createCell(3).setCellValue(MapUtils.getString(Datalist.get(i), "合約名稱"));
				row.createCell(4).setCellValue(MapUtils.getString(Datalist.get(i), "送審日期"));
				row.createCell(5).setCellValue(MapUtils.getString(Datalist.get(i), "Flowstatus"));
				row.createCell(6).setCellValue(MapUtils.getString(Datalist.get(i), "狀態"));
				count++;
			}
		}

		else if (srt.equals("NTS")) {
			//設定總計位置
			int count = 6;
			// 插入內容
			for (int i = 0; i < Datalist.size(); i++) {
				 Row row = sheet.createRow(count);
				 Cell cell = row.createCell(0);
//				if(MapUtils.getString(Datalist.get(i), "ContractType")!=null){
//					cell.setCellValue(MapUtils.getString(Datalist.get(i), "ContractType"));
//					Double mun = MapUtils.getDouble(Datalist.get(i), "count");
//					row.createCell(1).setCellValue(mun);
//				}
//				else continue;
				if(MapUtils.getDouble(Datalist.get(i), "count") != null) {
					if (!MapUtils.getString(Datalist.get(i), "ContractType").equals("總計")) {
						cell.setCellValue(MapUtils.getString(Datalist.get(i), "ContractType"));
						Double mun = MapUtils.getDouble(Datalist.get(i), "count");
						row.createCell(1).setCellValue(mun);
						count++;
					}
					else {
						row = sheet.getRow(2);
						Double mun = MapUtils.getDouble(Datalist.get(i), "count");
						row.createCell(1).setCellValue(mun);
						
					}
				}
			}
		} else if (srt.equals("RS") && module.equals("SC")) {
			int count = 7;
			// 插入內容
			for (int i = 0; i < Datalist.size() - 1; i++) {
				Row row = sheet.createRow(count);
				Cell cell = row.createCell(0);
				cell.setCellValue(MapUtils.getString(Datalist.get(i), "承辦人員"));
				row.createCell(1).setCellValue(MapUtils.getString(Datalist.get(i), "合約模式"));
				row.createCell(2).setCellValue(MapUtils.getDoubleValue(Datalist.get(i), "Count"));
				row.createCell(3).setCellValue(MapUtils.getString(Datalist.get(i), "todoEdit"));
				row.createCell(4).setCellValue(MapUtils.getString(Datalist.get(i), "Supplier"));
				row.createCell(5).setCellValue(MapUtils.getString(Datalist.get(i), "TotalReview"));
				row.createCell(6).setCellValue(MapUtils.getString(Datalist.get(i), "Director"));
				row.createCell(7).setCellValue(MapUtils.getString(Datalist.get(i), "logisticalDirector"));
				row.createCell(8).setCellValue(MapUtils.getString(Datalist.get(i), "StoreManager"));
				row.createCell(9).setCellValue(MapUtils.getString(Datalist.get(i), "Officer"));
				row.createCell(10).setCellValue(MapUtils.getString(Datalist.get(i), "待法務簽章"));
				row.createCell(11).setCellValue(MapUtils.getString(Datalist.get(i), "todoreject"));
				row.createCell(12).setCellValue(MapUtils.getString(Datalist.get(i), "todoArchive"));
				count++;
			}
		} else if (srt.equals("RS") && module.equals("NSC")) {
			int count = 7;
			// 插入內容
			for (int i = 0; i < Datalist.size() - 1; i++) {
				Row row = sheet.createRow(count);
				Cell cell = row.createCell(0);
				cell.setCellValue(MapUtils.getString(Datalist.get(i), "承辦人員"));
				row.createCell(1).setCellValue(MapUtils.getString(Datalist.get(i), "合約模式"));
				row.createCell(2).setCellValue(MapUtils.getDoubleValue(Datalist.get(i), "Count"));
				row.createCell(3).setCellValue(MapUtils.getString(Datalist.get(i), "todoEdit"));
				row.createCell(4).setCellValue(MapUtils.getString(Datalist.get(i), "法務審核中"));
				row.createCell(5).setCellValue(MapUtils.getString(Datalist.get(i), "Supplier"));
				row.createCell(6).setCellValue(MapUtils.getString(Datalist.get(i), "TotalReview"));
				row.createCell(7).setCellValue(MapUtils.getString(Datalist.get(i), "店經理/單位主管"));
				row.createCell(8).setCellValue(MapUtils.getString(Datalist.get(i), "OSD"));
				row.createCell(9).setCellValue(MapUtils.getString(Datalist.get(i), "法律顧問"));
				row.createCell(10).setCellValue(MapUtils.getString(Datalist.get(i), "法務長"));
				row.createCell(11).setCellValue(MapUtils.getString(Datalist.get(i), "非商品採購經理"));
				row.createCell(12).setCellValue(MapUtils.getString(Datalist.get(i), "資產部經理"));
				row.createCell(13).setCellValue(MapUtils.getString(Datalist.get(i), "IT經理"));
				row.createCell(14).setCellValue(MapUtils.getString(Datalist.get(i), "人力資源總監"));
				row.createCell(15).setCellValue(MapUtils.getString(Datalist.get(i), "組織系統供應鏈總監"));
				row.createCell(16).setCellValue(MapUtils.getString(Datalist.get(i), "便利購暨資產總監"));
				row.createCell(17).setCellValue(MapUtils.getString(Datalist.get(i), "財務總監"));
				row.createCell(18).setCellValue(MapUtils.getString(Datalist.get(i), "總經理"));
				row.createCell(19).setCellValue(MapUtils.getString(Datalist.get(i), "待法務簽章"));
				row.createCell(20).setCellValue(MapUtils.getString(Datalist.get(i), "todoreject"));
				row.createCell(21).setCellValue(MapUtils.getString(Datalist.get(i), "todoArchive"));

				count++;
			}
		}else if(srt.equals("ACD")){
			int count = 7;
			// 插入內容
			for (int i = 0; i < Datalist.size() - 1; i++) {
				Row row = sheet.createRow(count);
				Cell cell = row.createCell(0);
				cell.setCellValue(MapUtils.getString(Datalist.get(i), "合約編碼"));
				row.createCell(1).setCellValue(MapUtils.getString(Datalist.get(i), "合約模式"));
				row.createCell(2).setCellValue(MapUtils.getString(Datalist.get(i), "合約名稱"));
				row.createCell(3).setCellValue(MapUtils.getString(Datalist.get(i), "供應商統編"));
				row.createCell(4).setCellValue(MapUtils.getString(Datalist.get(i), "供應商中文名稱"));
				row.createCell(5).setCellValue(MapUtils.getString(Datalist.get(i), "送審日期"));
				row.createCell(6).setCellValue(MapUtils.getString(Datalist.get(i), "合約狀態"));
				row.createCell(7).setCellValue(MapUtils.getString(Datalist.get(i), "承辦人員"));
				row.createCell(8).setCellValue(MapUtils.getString(Datalist.get(i), "被代理人"));
				row.createCell(9).setCellValue(MapUtils.getString(Datalist.get(i), "代理簽審關卡"));
				row.createCell(10).setCellValue(MapUtils.getString(Datalist.get(i), "代理簽審動作(審查結果)"));
				row.createCell(11).setCellValue(MapUtils.getString(Datalist.get(i), "代理簽審時間(送出時間)"));

				count++;
			}
		}
	}
	
	/**
	 * 轉換百分比格式
	 * @return
	 */
	public  String convertPercent(int fraction , int denominator ) { 
		String percent = "";
		double baiy = fraction  * 1.0; 
		double baiz = denominator  * 1.0; 
		double fen = baiy / baiz; 

		DecimalFormat df1 = new DecimalFormat("##.##");
		percent = df1.format(fen)+"%"; 
		
		return percent; 
		} 
	/**
	 * 取得報表模組
	 * @return
	 */
	public Map <String,String>  getStatisticalReportModel(String filePath) {
		Map <String,String> map = new HashMap<String, String> ();
	    File file = new File(filePath);
	    if(file.exists()){
	    	File[] files = file.listFiles();
	  		for(int i = 0; i<files.length; i++) {
	  			if (!files[i].isDirectory()) {
  					if(files[i].getName().indexOf("Model") > -1) {
  						if(files[i].getName().indexOf("RS") > -1) {
  							//取得格式RS_SC,RS_NSC
  							String key = files[i].getName().split("_")[1] + "_" + files[i].getName().split("_")[3].split("\\.")[0];
  		  	  				map.put(key, files[i].getName());
  						}else {
  							String key = files[i].getName().split("_")[1];
  	  						map.put(key, files[i].getName());
  						}
  						
  					}
	  			}
	  		}
	    }
	    return map;
	}
	/**
	 * 取得報表模版資料流
	 * @return
	 */
	public FileInputStream getModelFile(String model , Map<String ,String> modelMap, String modelPath) throws FileNotFoundException {
		FileInputStream fis = null;
		String modelName = modelMap.get(model);
		if(StringUtils.isNoneBlank(modelName)) {
			if(new File(modelPath +modelName).exists()) {
				fis = new FileInputStream(modelPath +modelName);
			}
		}
		return fis;
	} 
}
