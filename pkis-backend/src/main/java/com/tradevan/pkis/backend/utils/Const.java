package com.tradevan.pkis.backend.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Const {
	
	
	public static String SFTP_LOGIN_NAME;

	public static String SFTP_IP;

	public static int SFTP_PORT;

	public static String SFTP_PASSWORD;
	
	public static String SFTP_SERVER_FILE;
	
	public static String SFTP_LOCAL_FILE;
	
	public static String SFTP_LOCAL_SUPPLIERMASTER_FILE;
	
	public static String SFTP_LOCAL_CSV_FILE;
	
	public static String SPRING_MAIL_SENDER;
	
	public static String STATISTICALREPORT_FIELD_SAVEPATH;
	
	public static String STATISTICALREPORT_FIELD_MODELPATH;
	
	public static String INFORMATION_FIELD_DATAPATH;
	
	public static String ERRORMSG_MAIL_ADDR;
	

	public  String getINFORMATION_FIELD_DATAPATH() {
		return INFORMATION_FIELD_DATAPATH;
	}
	
	@Value("${information.field.dataPath}")
	public  void setINFORMATION_FIELD_DATAPATH(String iNFORMATION_FIELD_DATAPATH) {
		Const.INFORMATION_FIELD_DATAPATH = iNFORMATION_FIELD_DATAPATH;
	}
	
	public  String getSTATISTICALREPORT_FIELD_SAVEPATH() {
		return STATISTICALREPORT_FIELD_SAVEPATH;
	}
	@Value("${statisticalreport.field.savePath}")
	public  void setSTATISTICALREPORT_FIELD_SAVEPATH(String sTATISTICALREPORT_FIELD_SAVEPATH) {
		Const.STATISTICALREPORT_FIELD_SAVEPATH = sTATISTICALREPORT_FIELD_SAVEPATH;
	}
	
	public  String getSTATISTICALREPORT_FIELD_MODELPATH() {
		return STATISTICALREPORT_FIELD_MODELPATH;
	}
	@Value("${statisticalreport.field.modelPath}")
	public  void setSTATISTICALREPORT_FIELD_MODELPATH(String sTATISTICALREPORT_FIELD_MODELPATH) {
		Const.STATISTICALREPORT_FIELD_MODELPATH = sTATISTICALREPORT_FIELD_MODELPATH;
	}

	public String getSFTP_LOGIN_NAME() {
		return SFTP_LOGIN_NAME;
	}

	@Value("${sftp.login.name}")
	public void setSFTP_LOGIN_NAME(String sFTP_LOGIN_NAME) {
		Const.SFTP_LOGIN_NAME = sFTP_LOGIN_NAME;
	}

	public String getSFTP_IP() {
		return SFTP_IP;
	}

	@Value("${sftp.ip}")
	public void setSFTP_IP(String sFTP_IP) {
		Const.SFTP_IP = sFTP_IP;
	}

	public int getSFTP_PORT() {
		return SFTP_PORT;
	}

	@Value("${sftp.port}")
	public void setSFTP_PORT(int sFTP_PORT) {
		Const.SFTP_PORT = sFTP_PORT;
	}

	public String getSFTP_PASSWORD() {
		return SFTP_PASSWORD;
	}

	@Value("${sftp.password}")
	public void setSFTP_PASSWORD(String sFTP_PASSWORD) {
		Const.SFTP_PASSWORD = sFTP_PASSWORD;
	}

	public String getSFTP_SERVER_FILE() {
		return SFTP_SERVER_FILE;
	}

	@Value("${sftp.serverFile}")
	public void setSFTP_SERVER_FILE(String sFTP_SERVER_FILE) {
		SFTP_SERVER_FILE = sFTP_SERVER_FILE;
	}

	public String getSFTP_LOCAL_FILE() {
		return SFTP_LOCAL_FILE;
	}

	@Value("${sftp.localFile}")
	public void setSFTP_LOCAL_FILE(String sFTP_LOCAL_FILE) {
		SFTP_LOCAL_FILE = sFTP_LOCAL_FILE;
	}
	
	public String getSFTP_LOCAL_SUPPLIERMASTER_FILE() {
		return SFTP_LOCAL_SUPPLIERMASTER_FILE;
	}

	@Value("${sftp.localSuppliermasterFile}")
	public void setSFTP_LOCAL_SUPPLIERMASTER_FILE(String sFTP_LOCAL_SUPPLIERMASTER_FILE) {
		SFTP_LOCAL_SUPPLIERMASTER_FILE = sFTP_LOCAL_SUPPLIERMASTER_FILE;
	}
	
	public String getSFTP_LOCAL_CSV_FILE() {
		return SFTP_LOCAL_CSV_FILE;
	}

	@Value("${sftp.localCsvFile}")
	public void setSFTP_LOCAL_CSV_FILE(String sSFTP_LOCAL_CSV_FILE) {
		SFTP_LOCAL_CSV_FILE = sSFTP_LOCAL_CSV_FILE;
	}

	public String getSPRING_MAIL_SENDER() {
		return SPRING_MAIL_SENDER;
	}

	@Value("${spring.mail.sender}")
	public void setSPRING_MAIL_SENDER(String sPRING_MAIL_SENDER) {
		SPRING_MAIL_SENDER = sPRING_MAIL_SENDER;
	}
	
	public String getERRORMSG_MAIL_ADDR() {
		return ERRORMSG_MAIL_ADDR;
	}

	@Value("${errorMsg.mail.addr}")
	public void setERRORMSG_MAIL_ADDR(String eRRORMSG_MAIL_ADDR) {
		ERRORMSG_MAIL_ADDR = eRRORMSG_MAIL_ADDR;
	}
	
}
