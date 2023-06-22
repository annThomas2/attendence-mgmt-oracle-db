package com.test.attendancemgmtsystem.exceptions;

public class NullStudentException extends RuntimeException{
	
	private static final long serialVersionUID = 1L;

	public NullStudentException(String msg) {
		super(msg);
	}

}
