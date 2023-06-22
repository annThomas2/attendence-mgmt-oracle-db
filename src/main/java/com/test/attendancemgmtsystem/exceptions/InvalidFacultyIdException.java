package com.test.attendancemgmtsystem.exceptions;

public class InvalidFacultyIdException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public InvalidFacultyIdException(String msg) {
		super(msg);
	}
}
