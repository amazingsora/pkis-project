package com.tradevan.apcommon.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRCsvExporter;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRRtfExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleWriterExporterOutput;
import net.sf.jasperreports.export.SimpleXlsReportConfiguration;
import net.sf.jasperreports.export.WriterExporterOutput;

/**
 * Title: JasperReportUtil<br>
 * Description: <br>
 * Company: Tradevan Co.<br>
 * 
 * @author Neil Chen
 * @version 1.0
 */
public class JasperReportUtil {
	
	public static void outputPDFFile( 
			Connection conn, 
			List<JasperPrint> jprints,
			String filePath, 
			String fileName, 
			boolean connClose) throws IOException, JRException, SQLException {
		
		BufferedOutputStream bos = null;
		try {
			mkdirsAndSetExecutable(filePath);
			bos = new BufferedOutputStream(new FileOutputStream(new File(filePath, fileName)));
			
			JRPdfExporter exporterPdf = new JRPdfExporter();
			exporterPdf.setExporterInput(SimpleExporterInput.getInstance(jprints));
			exporterPdf.setExporterOutput(new SimpleOutputStreamExporterOutput(bos));
			
			exporterPdf.exportReport();
		}
		finally {
			if (connClose == true && conn != null && !conn.isClosed()) {
				conn.close();
			}
			if (bos != null) {
				bos.close();
			}
		}
	}
	
	public static void outputRTFFile(
			Connection conn, 
			List<JasperPrint> jprints, 
			String filePath, 
			String fileName, 
			boolean connClose) throws IOException, JRException, SQLException {
		
		BufferedOutputStream bos = null;
		try {
			mkdirsAndSetExecutable(filePath);
			bos = new BufferedOutputStream(new FileOutputStream(new File(filePath, fileName)));
			
			JRRtfExporter exporterRtf = new JRRtfExporter();
			exporterRtf.setExporterInput(SimpleExporterInput.getInstance(jprints));
			exporterRtf.setExporterOutput(new SimpleWriterExporterOutput(bos));
			
			exporterRtf.exportReport();
		}
		finally {
			if (connClose == true && conn != null && !conn.isClosed()) {
				conn.close();
			}
			if (bos != null) {
				bos.close();
			}
		}
	}
	
	public static void outputExcelFile(
			Connection conn, 
			List<JasperPrint> jprints, 
			String filePath, 
			String fileName, 
			boolean connClose) throws IOException, JRException, SQLException {

		BufferedOutputStream bos = null;
		try {
			mkdirsAndSetExecutable(filePath);
			bos = new BufferedOutputStream(new FileOutputStream(new File(filePath, fileName)));
			
			JRXlsExporter exporterXLS = new JRXlsExporter();
			exporterXLS.setExporterInput(SimpleExporterInput.getInstance(jprints));
			exporterXLS.setExporterOutput(new SimpleOutputStreamExporterOutput(bos));
			
			SimpleXlsReportConfiguration xlsReportConfiguration = new SimpleXlsReportConfiguration();
			xlsReportConfiguration.setOnePagePerSheet(false);
	        xlsReportConfiguration.setDetectCellType(true);
	        xlsReportConfiguration.setRemoveEmptySpaceBetweenRows(true);
	        xlsReportConfiguration.setRemoveEmptySpaceBetweenColumns(true);
	        
	        exporterXLS.setConfiguration(xlsReportConfiguration);
	    	
	    	exporterXLS.exportReport();
		}
		finally {
			if (connClose == true && conn != null && !conn.isClosed()) {
				conn.close();
			}
			if (bos != null) {
				bos.close();
			}
		}
	}
	
	public static void outputCsvFile(
			Connection conn, 
			List<JasperPrint> jprints, 
			String filePath, 
			String fileName, 
			boolean connClose) throws IOException, JRException, SQLException {

		BufferedOutputStream bos = null;
		try {
			mkdirsAndSetExecutable(filePath);
			bos = new BufferedOutputStream(new FileOutputStream(new File(filePath, fileName)));
			
			JRCsvExporter exportCSV = new JRCsvExporter();
			
			WriterExporterOutput reportOutput;
			reportOutput = new SimpleWriterExporterOutput(bos, "MS950");
			
			exportCSV.setExporterInput(SimpleExporterInput.getInstance(jprints));
			exportCSV.setExporterOutput(reportOutput);
			
			exportCSV.exportReport();
		}
		finally {
			if (connClose == true && conn != null && !conn.isClosed()) {
				conn.close();
			}
			if (bos != null) {
				bos.close();
			}
		}
	}
	
	public static void mkdirsAndSetExecutable(String path) {
		File dir = new File(path);
		if (! dir.exists()) {
			dir.mkdirs();
			dir.setExecutable(true, false);
			dir.setReadable(true, false);
			File parentDir = dir.getParentFile();
			for (int x = 0; x < 4; x++) {
				if (parentDir != null && parentDir.isDirectory()) {
					parentDir.setExecutable(true, false);
					parentDir.setReadable(true, false);
					
					parentDir = parentDir.getParentFile();
				}
				else {
					break;
				}
			}
		}
	}
	
}
