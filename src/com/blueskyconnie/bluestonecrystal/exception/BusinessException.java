package com.blueskyconnie.bluestonecrystal.exception;

public class BusinessException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6885125575283878997L;

	public BusinessException() {
		super();
	}

	public BusinessException(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
	}

	public BusinessException(String detailMessage) {
		super(detailMessage);
	}

	public BusinessException(Throwable throwable) {
		super(throwable);
	}

}
