package com.tradevan.apcommon.bean;

/**
 * Title: CommonMsgCode<br>
 * Description: <br>
 * Company: Tradevan Co.<br>
 * 
 * @author Neil Chen
 * @version 1.0.1
 */
public interface CommonMsgCode {
	// Fatal
	String CODE_F_DB_ERR = "TV00_F0000";               String MSG_F_DB_ERR = "global.msg.dbErr";
	String CODE_F_SYS_ERR = "TV00_F0001";              String MSG_F_SYS_ERR = "global.msg.sysErr";
	
	// Error
	String CODE_E_SAVE_ERR = "TV00_E0000";             String MSG_E_DATA_ADD_ERR = "global.msg.saveErr";
	String CODE_E_DATA_ALREADY_EXIST = "TV00_E0001";   String MSG_E_DATA_ALREADY_EXIST = "global.msg.dataAlreadyExist";
	String CODE_E_DELETE_ERR = "TV00_E0002";           String MSG_E_DATA_DELETE_ERR = "global.msg.deleteErr";
	String CODE_E_UPDATE_ERR = "TV00_E0003";           String MSG_E_DATA_UPDATE_ERR = "global.msg.updateErr";
	String CODE_E_QUERY_ERR = "TV00_E0004";            String MSG_E_DATA_QUERY_ERR = "global.msg.queryErr";
	String CODE_E_DATA_NO_CHANGED = "TV00_E0005";      String MSG_E_DATA_NO_CHANGED = "global.msg.dataNoChanged";
	String CODE_E_LOGIN_ERR = "TV00_E0006";            String MSG_E_LOGIN_ERR = "global.msg.loginErr";
	String CODE_E_OPERATION_NOT_EXIST = "TV00_E0007";  String MSG_E_OPERATION_NOT_EXIST = "global.msg.operationNotExist";
	String CODE_E_UPLOAD_ERR = "TV00_E0008";           String MSG_E_UPLOAD_ERR = "global.msg.uploadErr";
	String CODE_E_DOWNLOAD_ERR = "TV00_E0009";         String MSG_E_DOWNLOAD_ERR = "global.msg.downloadErr";
	String CODE_E_FILE_TRANS_ERR = "TV00_E0010";       String MSG_E_FILE_TRANS_ERR = "global.msg.fileTransErr";
	String CODE_E_FILE_EXPORT_ERR = "TV00_E0011";      String MSG_E_FILE_EXPORT_ERR = "global.msg.fileExportErr";
	String CODE_E_FILE_IMPORT_ERR = "TV00_E0012";      String MSG_E_FILE_IMPORT_ERR = "global.msg.fileImportErr";
	String CODE_E_CANNOT_BE_DELETED = "TV00_E0013";    String MSG_E_CANNOT_BE_DELETED = "global.msg.cannotBeDeleted";
	String CODE_E_ILLEGAL_STATE = "TV00_E0014";        String MSG_E_ILLEGAL_STATE = "global.msg.illegalState";
	
	// Warning
	String CODE_W_DATE_FORMAT_ERR = "TV00_W0000";      String MSG_W_DATE_FORMAT_ERR = "global.msg.dateFormatErr";
	String CODE_W_EMAIL_FORMAT_ERR = "TV00_W0001";     String MSG_W_EMAIL_FORMAT_ERR = "global.msg.emailFormatErr";
	String CODE_W_MUST_BE_NUMBER = "TV00_W0002";       String MSG_W_MUST_BE_NUMBER = "global.msg.mustBeNumber";
	String CODE_W_MUST_BE_TEXT = "TV00_W0003";         String MSG_W_MUST_BE_TEXT = "global.msg.mustBeText";
	String CODE_W_EXCEED_MAX_LENGTH = "TV00_W0004";    String MSG_W_EXCEED_MAX_LENGTH = "global.msg.exceedMaxLength";
	String CODE_W_LESS_THAN_MIN_LENGTH = "TV00_W0005"; String MSG_W_LESS_THAN_MIN_LENGTH = "global.msg.lessThanMinLength";
	String CODE_W_LENGTH_MUST_BETWEEN = "TV00_W0006";  String MSG_W_LENGTH_MUST_BETWEEN = "global.msg.lengthMustBetween";
	String CODE_W_CANNOT_BE_BLANK = "TV00_W0007";      String MSG_W_CANNOT_BE_BLANK = "global.msg.cannotBeBlank";
	String CODE_W_CANNOT_BE_MODIFY = "TV00_W0008";     String MSG_W_CANNOT_BE_MODIFY = "global.msg.cannotBeModify";
	String CODE_W_DATA_VERIFY_FAIL = "TV00_W0009";     String MSG_W_DATA_VERIFY_ERR = "global.msg.dataVerifyFail";
	String CODE_W_DELETE_CONFIRM = "TV00_W0010";       String MSG_W_DELETE_CONFIRM = "global.msg.deleteConfirm";
	String CODE_W_DATE_RANGE_ERR = "TV00_W0011";       String MSG_W_DATE_RANGE_ERR = "global.msg.dateRangeErr";
	String CODE_W_MUST_BE_POS_INTEGER = "TV00_W0012";  String MSG_W_MUST_BE_POS_INTEGER = "global.msg.mustBePosInteger";
	
	// Info
	String CODE_I_OK = "TV00_I0000";                   String MSG_I_OK = "global.msg.ok";
	String CODE_I_SAVE_OK = "TV00_I0001";              String MSG_I_SAVE_OK = "global.msg.saveOK";
	String CODE_I_DELETE_OK = "TV00_I0002";            String MSG_I_DELETE_OK = "global.msg.deleteOK";
	String CODE_I_UPDATE_OK = "TV00_I0003";            String MSG_I_UPDATE_OK = "global.msg.updateOK";
	String CODE_I_NO_DATA_FOUND = "TV00_I0004";        String MSG_I_NO_DATA_FOUND = "global.msg.noDataFound";
	String CODE_I_NO_DATA_MATCHED = "TV00_I0005";      String MSG_I_NO_DATA_MACHED = "global.msg.noDataMatched";
	String CODE_I_QUERY_MATCH_ROWS = "TV00_I0006";     String MSG_I_QUERY_MATCH_ROWS = "global.msg.queryMatchedRows";
	String CODE_I_UPLOAD_OK = "TV00_I0007";            String MSG_I_UPLOAD_OK = "global.msg.uploadOK";
	String CODE_I_DOWNLOAD_OK = "TV00_I0008";          String MSG_I_DOWNLOAD_OK = "global.msg.downloadOK";
	String CODE_I_EXPORT_OK = "TV00_I0009";            String MSG_I_EXPORT_OK = "global.msg.exportOK";
	String CODE_I_IMPORT_OK = "TV00_I0010";            String MSG_I_IMPORT_OK = "global.msg.importOK";
	String CODE_I_PROCESSING = "TV00_I0011";           String MSG_I_PROCESSING = "global.msg.processing";
}
