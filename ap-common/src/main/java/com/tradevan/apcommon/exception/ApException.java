package com.tradevan.apcommon.exception;

/**
 * Title: ApplicationException<br>
 * Description: Application's checked exception should extends ApplicationException<br>
 * Company: Tradevan Co.<br>
 * 
 * @author Neil Chen
 * @version 1.0
 */
public class ApException extends Exception {
	private static final long serialVersionUID = 1L;
	
	private String errCode;

	public ApException(String errCode, String message) {
		super(message);
		this.errCode = errCode;
	}

	public ApException(String errCode, String message, Throwable cause) {
		super(message, cause);
		this.errCode = errCode;
	}

	public String getErrCode() {
		return errCode;
	}

}
