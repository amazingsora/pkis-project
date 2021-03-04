package com.tradevan.handyform.exception;

/**
 * Title: FormException<br>
 * Description: <br>
 * Company: Tradevan Co.<br>
 * 
 * @author Neil Chen
 * @version 1.0
 */
public class FormException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public FormException() {
		super();
	}

	public FormException(String message, Throwable cause) {
		super(message, cause);
	}

	public FormException(String message) {
		super(message);
	}

	public FormException(Throwable cause) {
		super(cause);
	}
}
