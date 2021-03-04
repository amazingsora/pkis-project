package com.tradevan.pkis.backend.utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelUtil {
	
	public static Log log = LogFactory.getLog(ExcelUtil.class);
	
	public static List<Map<Integer, String>> read(String filePath, int sheetIndex, int rowIndexStart, int cellIndexStart, int cellIndexEnd) throws Exception {
		List<Map<Integer, String>> result = new ArrayList<Map<Integer, String>>();
		Map<Integer, String> dataMap = null;
		InputStream input = null;
		XSSFWorkbook workbook = null;
		XSSFSheet sheet = null;
		XSSFRow row = null;
		
		try {
			File file = new File(filePath);
			if(!file.exists()) {
				throw new Exception(filePath + " 檔案不存在!");
			}
			
			input = new BufferedInputStream(new FileInputStream(filePath));
			workbook = new XSSFWorkbook(input);
			sheet = workbook.getSheetAt(sheetIndex);
			if(sheet == null) {
				throw new Exception("sheet is null");
			}
			log.info("【" + filePath + " sheet num 】" + sheet.getPhysicalNumberOfRows());
			for(int i = rowIndexStart ; i < sheet.getPhysicalNumberOfRows() ; i ++) {
				row = sheet.getRow(i);
				dataMap = new HashMap<Integer, String>();
				for(int j = cellIndexStart ; j <= cellIndexEnd ; j ++) {
					dataMap.put(j, getCell(row, j));
				}
				result.add(dataMap);
			}
			
			log.info("result.size : " + result.size());
			
		} catch(Exception e) {
			e.printStackTrace();
			log.error(e);
			throw new Exception("讀取檔案錯誤 : " + e);
		} finally {
			if(workbook != null) {
				workbook.close();
			}
		}
		
		return result;
	}
	
	/**
	 * 讀取Excel內的Cell欄位值，一律先將儲存格式設定為字串後再讀取
	 * @param xssfRow
	 * @param columnIndex
	 * @return
	 */
	private static String getCell(XSSFRow xssfRow, int cellIndex) {
		XSSFCell cell = xssfRow.getCell(cellIndex);
	 
		if(cell != null) {
			if(cell.getCellTypeEnum() == CellType.STRING) {
				return StringUtils.trimToEmpty(cell.getStringCellValue());
			} else if(cell.getCellTypeEnum() == CellType.NUMERIC) {
				BigDecimal bigDecimal = new BigDecimal(String.valueOf(cell.getNumericCellValue()));
				return bigDecimal.toPlainString();
			} else {
				cell.setCellType(CellType.STRING);
				return StringUtils.trimToEmpty(cell.getStringCellValue());
			}
		
		} else {
			return "";
		}
	}
	
}
