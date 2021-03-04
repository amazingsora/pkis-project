package com.tradevan.pkis.backend.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Properties;
import java.util.Vector;

import org.apache.commons.io.IOUtils;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.ChannelSftp.LsEntry;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

public class SftpUtil {
	
	public static void get(String serverFile, String localFile) {
		ChannelSftp sftp = null;
		Channel channel = null;
		Session sshSession = null ;
		try {
			JSch jsch = new JSch();
			System.out.println(Const.SFTP_LOGIN_NAME+"___----___"+Const.SFTP_IP);
			sshSession = jsch.getSession(Const.SFTP_LOGIN_NAME, Const.SFTP_IP, Const.SFTP_PORT);
			sshSession.setPassword(Const.SFTP_PASSWORD);
			sshSession.setConfig("StrictHostKeyChecking", "no");
			sshSession.connect();
			System.out.println("連線成功");
			channel = sshSession.openChannel("sftp");
			channel.connect();
			sftp = (ChannelSftp) channel;
			File file = new File(localFile);
			if (file.isDirectory()) {
				sftp.cd(serverFile);
				Vector filelist = sftp.ls(serverFile);
				for (int i = 0; i < filelist.size(); i++) {
					LsEntry entry = (LsEntry) filelist.get(i);
					if(!entry.getAttrs().isDir()) {
						//InputStream is = sftp.get(entry.getFilename());
						sftp.get(entry.getFilename(), localFile + "/" + entry.getFilename());
						//IOUtils.copy(is, new FileOutputStream(localFile + "/" + entry.getFilename()));
						System.out.println("下載檔案成功：" + entry.getFilename());
						sftp.rm(entry.getFilename());
						System.out.println("刪除檔案：" + entry.getFilename());

					}
				}
			} else {
				InputStream is = sftp.get(serverFile);
				IOUtils.copy(is, new FileOutputStream(localFile));
			}
		} catch (Exception e) {
			System.out.println("取得資料失敗：" + e.getMessage());
			e.printStackTrace();
		} finally {
			channel.disconnect();
			sftp.disconnect();
			sshSession.disconnect();
			System.out.println("關閉連線");
		}
	}
	public static void get(String serverFile, String localFile,String model) {
		ChannelSftp sftp = null;
		Channel channel = null;
		Session sshSession = null ;
		try {
			JSch jsch = new JSch();
			System.out.println(Const.SFTP_LOGIN_NAME+"___----___"+Const.SFTP_IP);
			sshSession = jsch.getSession(Const.SFTP_LOGIN_NAME, Const.SFTP_IP, Const.SFTP_PORT);
			sshSession.setPassword(Const.SFTP_PASSWORD);
			sshSession.setConfig("StrictHostKeyChecking", "no");
			sshSession.connect();
			System.out.println("連線成功");
			channel = sshSession.openChannel("sftp");
			channel.connect();
			sftp = (ChannelSftp) channel;
			File file = new File(localFile);
			if (file.isDirectory()) {
				sftp.cd(serverFile);
				Vector filelist = sftp.ls(serverFile);
				for (int i = 0; i < filelist.size(); i++) {
					LsEntry entry = (LsEntry) filelist.get(i);
					if(!entry.getAttrs().isDir()) {
						//InputStream is = sftp.get(entry.getFilename());
						if(selectmodel(model,entry.getFilename())) {
							sftp.get(entry.getFilename(), localFile + "/" + entry.getFilename());
							//IOUtils.copy(is, new FileOutputStream(localFile + "/" + entry.getFilename()));
							System.out.println("下載檔案成功：" + entry.getFilename());
							sftp.rm(entry.getFilename());
							System.out.println("刪除檔案：" + entry.getFilename());
						}
					}
				}
			} else {
				InputStream is = sftp.get(serverFile);
				IOUtils.copy(is, new FileOutputStream(localFile));
			}
		} catch (Exception e) {
			System.out.println("取得資料失敗：" + e.getMessage());
			e.printStackTrace();
		} finally {
			channel.disconnect();
			sftp.disconnect();
			sshSession.disconnect();
			System.out.println("關閉連線");
		}
	}
	public static boolean selectmodel(String model,String filename) {
		boolean result=false ;
		model=model.toUpperCase();
		if(model.equals("CSV")) {
			if(filename.indexOf("emp")>-1||filename.indexOf("org")>-1||filename.indexOf("TITLE")>-1) {
				result = true;
			}
		}
		return result;
	}
}
