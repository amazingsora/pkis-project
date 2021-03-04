package com.tradevan.handyform.exception;

/**
 * Title: FormMarshalException<br>
 * Description: <br>
 * Company: Tradevan Co.<br>
 * 
 * @author Neil Chen
 * @version 1.0
 */
public class FormMarshallerException extends FormException {
	private static final long serialVersionUID = 1L;

	public FormMarshallerException() {
		super();
	}

	public FormMarshallerException(String message, Throwable cause) {
		super(message, cause);
	}

	public FormMarshallerException(String message) {
		super(message);
	}

	public FormMarshallerException(Throwable cause) {
		super(cause);
	}
}
