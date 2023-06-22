package com.test.attendancemgmtsystem.exceptions;

public class NonExistentFacultyIdException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public NonExistentFacultyIdException(String msg) {
		super(msg);
	}
}
