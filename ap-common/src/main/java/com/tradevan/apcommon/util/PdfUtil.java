package com.tradevan.apcommon.util;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfCopy;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfWriter;

public class PdfUtil {

	private static Logger logger = LoggerFactory.getLogger(PdfUtil.class);

	public static void mergePdfFiles(String[] files, String savePathName) {
		FileOutputStream fout = null;
		try {
			fout = new FileOutputStream(savePathName);
			Document document = new Document(new PdfReader(files[0]).getPageSize(1));
			PdfCopy copy = new PdfCopy(document, fout);
			document.open();

			for (int i = 0; i < files.length; i++) {
				PdfReader reader = new PdfReader(files[i]);
				int n = reader.getNumberOfPages();

				for (int j = 1; j <= n; j++) {
					document.newPage();
					PdfImportedPage page = copy.getImportedPage(reader, j);
					copy.addPage(page);
				}
			}

			document.close();
		}
		catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		finally {
			if (fout != null) {
				try { fout.close(); } catch (IOException e) { logger.error(e.getMessage(), e); }
			}
		}
	}

	public static void mergePdfFiles(List<String> fileNameList, String savePathName) {
		FileOutputStream fout = null;
		try {
			fout = new FileOutputStream(savePathName);
			Document document = new Document(new PdfReader(fileNameList.get(0)).getPageSize(1));
			PdfCopy copy = new PdfCopy(document, fout);
			document.open();

			boolean hasPage = false;
			for (int i = 0; i < fileNameList.size(); i++) {
				PdfReader reader = new PdfReader(fileNameList.get(i));
				int n = reader.getNumberOfPages();

				for (int j = 1; j <= n; j++) {
					hasPage = true;
					document.newPage();
					PdfImportedPage page = copy.getImportedPage(reader, j);
					copy.addPage(page);
				}
			}

			if (hasPage) {
				document.close();
			}
		}
		catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		finally {
			if (fout != null) {
				try { fout.close(); } catch (IOException e) { logger.error(e.getMessage(), e); }
			}
		}
	}

	public static void encryptPdf(String srcFilePathName, String destFilePathName, String userPassword, String ownerPassword) {
		FileOutputStream fout = null;
		try {
			fout = new FileOutputStream(destFilePathName);
			PdfReader reader = new PdfReader(srcFilePathName);
			PdfStamper stamper = new PdfStamper(reader, fout);
			stamper.setEncryption(userPassword.getBytes(), ownerPassword.getBytes(), PdfWriter.ALLOW_PRINTING, PdfWriter.ENCRYPTION_AES_128 | PdfWriter.DO_NOT_ENCRYPT_METADATA);
			stamper.close();
			reader.close();
		}
		catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		finally {
			if (fout != null) {
				try { fout.close(); } catch (IOException e) { logger.error(e.getMessage(), e); }
			}
		}
	}
}
