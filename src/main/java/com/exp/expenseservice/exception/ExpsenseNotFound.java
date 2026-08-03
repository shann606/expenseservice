package com.exp.expenseservice.exception;

public class ExpsenseNotFound extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ExpsenseNotFound(String reason) {

		super(reason);

	}

}
