package com.tradevan.apcommon.exception;

/**
 * Title: ApRuntimeException<br>
 * Description: Application's unchecked exception should extends ApRuntimeException<br>
 * Company: Tradevan Co.<br>
 * 
 * @author Neil Chen
 * @version 1.2
 */
public class ApRuntimeException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	private String errCode;

	public ApRuntimeException(String message) {
		super(message);
	}
	
	public ApRuntimeException(String errCode, String message) {
		super(message);
		this.errCode = errCode;
	}

	public ApRuntimeException(String errCode, String message, Throwable cause) {
		super(message, cause);
		this.errCode = errCode;
	}

	public ApRuntimeException(Throwable cause) {
		super(cause);
	}

	public String getErrCode() {
		return errCode;
	}

}
