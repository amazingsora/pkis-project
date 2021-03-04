package com.tradevan.pkis.web.task;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tradevan.mapper.pkis.dao.NegoentryMapper;
import com.tradevan.mapper.pkis.model.Negoentry;
import com.tradevan.xauthframework.web.service.DefaultService;

@Service("ExportExcelFileService")
@Transactional(rollbackFor = Exception.class)
public class ExportExcelFile extends DefaultService {
	
	@Autowired
	NegoentryMapper negoentryMapper;

//	public static void main(String[] args) {
//		
////		String file = "D:\\家樂福電子合約\\00D_Nego-Linda-2020-to Alex.xlsx";
//		ExportExcelFile exportExcelFile = new ExportExcelFile();
//		exportExcelFile.exportExcel();
//	}
	
	// 需寄在某個controller下執行
	// @Autowired
	// ExportExcelFile exportExcelFile;
	// exportExcelFile.process();
	public boolean process() {
		
//		String file = "D:\\家樂福電子合約\\00D_Nego-Linda-2020-to Alex.xlsx";
		String file = "D:\\家樂福電子合約\\毛利資料\\00D_Nego-Linda-2020-to Alex_0921.xlsx";
		boolean excelCellList = readExcel(file);
		
		return excelCellList;
	}
	
	public boolean readExcel(String file) {
		
		boolean isSuccess = false;
		InputStream input = null;
		XSSFWorkbook workbook = null;
		XSSFSheet sheet = null;
		int sheetCount = 0;
		XSSFRow row = null;
		Iterator<Row> rows = null;
		List<XSSFRow> rowList = new ArrayList<XSSFRow>();
		Negoentry negoentry = null;
		XSSFCell cell = null;
		Iterator<Cell> cells = null;
		String type = "";
		List<Negoentry> negoentryList = new ArrayList<Negoentry>();
		
		try {
			input = new BufferedInputStream(new FileInputStream(file));
			workbook = new XSSFWorkbook(input);
			sheetCount = workbook.getNumberOfSheets();
			logger.info("sheetCount == " + sheetCount);
			for(int i = 0 ; i < sheetCount ; i ++) {
				type = String.valueOf(i);
				sheet = workbook.getSheetAt(i);
				rows = sheet.rowIterator();
				rows.next(); // 從標題下一行讀資料
				while(rows.hasNext()) {
					row = (XSSFRow) rows.next();
					rowList.add(row);
					cells = row.cellIterator();
					negoentry = new Negoentry();
					while(cells.hasNext()) {
						try {
							cell = (XSSFCell) cells.next();
							negoentry = insertExcelToDb(cell, type, negoentry);
						} catch(Exception e) {
							e.printStackTrace();
							logger.info("錯誤為 : " + e.getMessage());
						}
					}
					negoentryList.add(negoentry);
				}
			}
			int i = 1;
			for(Negoentry negoentryData: negoentryList) {
				try {
					negoentryMapper.insert(negoentryData);
				} catch(Exception e) {
					e.printStackTrace();
					logger.info("寫入DB，第" + i + "筆錯誤為 : " + e.getMessage());
				}
				i = i + 1;
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return isSuccess;
	}
	
	public Negoentry insertExcelToDb(XSSFCell cell, String type, Negoentry negoentry) {
		
		
		int columnIndex = cell.getColumnIndex();
		switch(columnIndex) {
		case 0 :
			String years = cell.getStringCellValue();
			logger.info("years == " + years);
			negoentry.setYears(years);
			break;
		case 1:
			String deptno = cell.getStringCellValue();
			logger.info("deptno == " + deptno);
			negoentry.setDeptno(deptno);
			break;
		case 2:
			String supplierCode = cell.getStringCellValue();
			logger.info("supplierCode == " + supplierCode);
			negoentry.setSuppliercode(supplierCode);
			break;
		case 3:
			String flow = cell.getStringCellValue();
			negoentry.setFlow(flow);
			break;
		case 4:
			double hypcost = getRoundingModeUpVal(cell);
			negoentry.setHypcost(hypcost);
			break;
		case 5:
			double supcost = getRoundingModeUpVal(cell);
			negoentry.setSupcost(supcost);
			break;
		case 6:
			double kmcost = getRoundingModeUpVal(cell);
			negoentry.setKmcost(kmcost);
			break;
		case 7:
			double natcost = getRoundingModeUpVal(cell);
			negoentry.setNatcost(natcost);
			break;
		case 8:
			double hypmargin = getRoundingModeUpVal(cell);
			negoentry.setHypmargin(hypmargin);
			break;
		case 9:
			double supmargin = getRoundingModeUpVal(cell);
			negoentry.setSupmargin(supmargin);
			break;
		case 10:
			double kmmargin = getRoundingModeUpVal(cell);
			negoentry.setKmmargin(kmmargin);
			break;
		case 11:
			double natmargin = getRoundingModeUpVal(cell);
			negoentry.setNatmargin(natmargin);
			break;
		case 12:
			double hypmarginactual = getRoundingModeUpVal(cell);
			negoentry.setHypmarginactual(hypmarginactual);
			break;
		case 13:
			double supmarginactual = getRoundingModeUpVal(cell);
			negoentry.setSupmarginactual(supmarginactual);
			break;
		case 14:
			double kmmarginactual = getRoundingModeUpVal(cell);
			negoentry.setKmmarginactual(kmmarginactual);
			break;
		case 15:
			double natmarginactual = getRoundingModeUpVal(cell);
			negoentry.setNatmarginactual(natmarginactual);
			break;
		case 16:
			double extra = cell.getNumericCellValue();
			negoentry.setExtra(extra);
			break;
		case 17:
			double extrapc = getRoundingModeUpVal(cell);
			negoentry.setExtrapc(extrapc);
			break;
		case 18:
			double extraactual = getRoundingModeUpVal(cell);
			negoentry.setExtraactual(extraactual);
			break;
		}
		negoentry.setType(type);	
		return negoentry;
	}
	
	public double getRoundingModeUpVal(XSSFCell cell) {
		
		double numericCellValue = (cell.getNumericCellValue() * 100);
		BigDecimal bg = new BigDecimal(numericCellValue).setScale(2, BigDecimal.ROUND_HALF_UP);
		numericCellValue = bg.doubleValue();
		
		return numericCellValue;
	}
}
