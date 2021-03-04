package com.tradevan.apcommon.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ZipFileUtil {
	private static Logger logger = LoggerFactory.getLogger(ZipFileUtil.class);
	
	public static void zipFile(final File[] files, final File targetZipFile) throws IOException {
		try {
			FileOutputStream fos = new FileOutputStream(targetZipFile);
			ZipOutputStream zos = new ZipOutputStream(fos);
			
			byte[] buffer = new byte[128];
			
			for (int i = 0; i < files.length; i++) {
				
				File currentFile = files[i];
				
				if (!currentFile.isDirectory()) {
					ZipEntry entry = new ZipEntry(currentFile.getName());
					FileInputStream fis = new FileInputStream(currentFile);
					zos.putNextEntry(entry);
					int read = 0;
					while ((read = fis.read(buffer)) != -1) {
						zos.write(buffer, 0, read);
					}
					zos.closeEntry();
					fis.close();
				}
			}
			
			zos.close();
			fos.close();
		} catch (FileNotFoundException e) {
			logger.error("File not found : " + e);
		}
	}
	
	public static void zipFile(List<String> fileList, File targetZipFile) throws IOException {
		try {
			FileOutputStream fos = new FileOutputStream(targetZipFile);
			ZipOutputStream zos = new ZipOutputStream(fos);
			
			byte[] buffer = new byte[128];
			
			for(String fileName : fileList)	{
				File currentFile = new File(fileName);
				
				if (!currentFile.isDirectory()) {
					ZipEntry entry = new ZipEntry(currentFile.getName());
					FileInputStream fis = new FileInputStream(currentFile);
					zos.putNextEntry(entry);
					int read = 0;
					while ((read = fis.read(buffer)) != -1) {
						zos.write(buffer, 0, read);
					}
					zos.closeEntry();
					fis.close();
				}
			}
			
			zos.close();
			fos.close();
		} catch (FileNotFoundException e) {
			logger.error("File not found : " + e);
		}
	}
}
