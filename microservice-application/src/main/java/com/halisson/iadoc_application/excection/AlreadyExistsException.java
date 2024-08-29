package com.halisson.iadoc_application.excection;

public class AlreadyExistsException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8110181142526395311L;

	public AlreadyExistsException(String message, Throwable cause) {
		super(message, cause);
	}

	public AlreadyExistsException(String message) {
		super(message);
	}

	public AlreadyExistsException(Throwable cause) {
		super(cause);
	}

}
