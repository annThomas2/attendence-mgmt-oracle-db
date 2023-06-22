package com.test.attendancemgmtsystem.exceptions;

public class NonExistentStudentIdException extends RuntimeException{
	private static final long serialVersionUID = 1L;

	public NonExistentStudentIdException (String msg) {
		super(msg);
	}

}
