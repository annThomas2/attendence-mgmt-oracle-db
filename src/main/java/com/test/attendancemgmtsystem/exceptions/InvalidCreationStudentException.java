package com.test.attendancemgmtsystem.exceptions;

public class InvalidCreationStudentException extends RuntimeException{
	
	private static final long serialVersionUID = 1L;

	public InvalidCreationStudentException(String msg) {
		super(msg);
	}

}
