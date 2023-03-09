package com.example.jwt.exception;

public class MappingException extends Exception {
	private static final long serialVersionUID = 1L;

	public MappingException(String message) {
		super(message);
	}

	public MappingException(Exception exception) {
		super(exception);
	}
}
