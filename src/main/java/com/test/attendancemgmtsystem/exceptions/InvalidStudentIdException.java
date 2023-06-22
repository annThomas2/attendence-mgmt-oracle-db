package com.test.attendancemgmtsystem.exceptions;

public class InvalidStudentIdException extends RuntimeException{
	
	private static final long serialVersionUID = 1L;

	public InvalidStudentIdException(String msg) {
		super(msg);
	}

}
