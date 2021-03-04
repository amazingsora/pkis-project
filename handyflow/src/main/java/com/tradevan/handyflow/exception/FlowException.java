package com.tradevan.handyflow.exception;

/**
 * Title: FlowException<br>
 * Description: <br>
 * Company: Tradevan Co.<br>
 * 
 * @author Neil Chen
 * @version 1.0
 */
public class FlowException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public FlowException() {
		super();
	}

	public FlowException(String message, Throwable cause) {
		super(message, cause);
	}

	public FlowException(String message) {
		super(message);
	}

	public FlowException(Throwable cause) {
		super(cause);
	}
}
