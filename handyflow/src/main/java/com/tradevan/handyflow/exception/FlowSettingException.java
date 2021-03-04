package com.tradevan.handyflow.exception;

/**
 * Title: FlowSettingException<br>
 * Description: <br>
 * Company: Tradevan Co.<br>
 * 
 * @author Neil Chen
 * @version 1.0
 */
public class FlowSettingException extends FlowException {
	private static final long serialVersionUID = 1L;

	public FlowSettingException() {
		super();
	}

	public FlowSettingException(String message, Throwable cause) {
		super(message, cause);
	}

	public FlowSettingException(String message) {
		super(message);
	}

	public FlowSettingException(Throwable cause) {
		super(cause);
	}
}
