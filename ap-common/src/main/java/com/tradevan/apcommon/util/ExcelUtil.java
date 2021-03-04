package com.tradevan.apcommon.util;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Title: JsonUtil<br>
 * Description: <br>
 * Company: Tradevan Co.<br>
 * 
 * @author Neil Chen
 * @version 1.0.1
 */
public class ExcelUtil {
	protected static Logger logger = LoggerFactory.getLogger(ExcelUtil.class);

	/**
	 * 自訂格式：for 數字
	 */
	public static void addCell2Row(Row row, int colIndex, Object data, CellStyle intCellStyle, CellStyle floatCellStyle) throws Exception {
    	if (data != null) {
    		Class<? extends Object> clazz = data.getClass();
    		Cell cell = row.createCell(colIndex);
    		if (clazz.equals(Integer.class)) {
    			cell.setCellType(CellType.NUMERIC);
    			cell.setCellStyle(intCellStyle);
    			cell.setCellValue((Integer) data);
    		}
    		else if (clazz.equals(Short.class)) {
    			cell.setCellType(CellType.NUMERIC);
    			cell.setCellStyle(intCellStyle);
    			cell.setCellValue(((Short) data).doubleValue());
    		}
    		else if (clazz.equals(Long.class)) {
    			cell.setCellType(CellType.NUMERIC);
    			cell.setCellStyle(intCellStyle);
    			cell.setCellValue((Long) data);
    		}
    		else if (clazz.equals(BigInteger.class)){ 
    			cell.setCellType(CellType.NUMERIC);
    			cell.setCellStyle(intCellStyle);
    			cell.setCellValue(((BigInteger) data).longValue());
    		}
    		else if (clazz.equals(BigDecimal.class)) {
    			cell.setCellType(CellType.NUMERIC);
    			cell.setCellStyle(floatCellStyle);
    			cell.setCellValue(((BigDecimal) data).doubleValue());
    		}
    		else if (clazz.equals(Boolean.class)) {
    			cell.setCellType(CellType.STRING);
    			cell.setCellValue(((Boolean) data) ? "Y" : "N");
    		}
    		else {
    			cell.setCellType(CellType.STRING);
    			row.createCell(colIndex).setCellValue((String) data);
    		}
    	}
    }
	
	/**
	 * 自訂格式：for 字串
	 */
	public static void addCell2Row(Row row, int colIndex, Object data, CellStyle stringCellStyle) throws Exception {
    	if (data != null) {
    		Cell cell = row.createCell(colIndex);
    		cell.setCellStyle(stringCellStyle);
    		cell.setCellValue((String) data);
    	}
    }
	
}
