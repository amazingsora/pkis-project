package com.tradevan.handyflow.exception;

/**
 * Title: FlowParseException<br>
 * Description: <br>
 * Company: Tradevan Co.<br>
 * 
 * @author Neil Chen
 * @version 1.0
 */
public class FlowParseException extends FlowException {
	private static final long serialVersionUID = 1L;

	public FlowParseException() {
		super();
	}

	public FlowParseException(String message, Throwable cause) {
		super(message, cause);
	}

	public FlowParseException(String message) {
		super(message);
	}

	public FlowParseException(Throwable cause) {
		super(cause);
	}
}
