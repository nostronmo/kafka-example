package com.tom.kafka.consumer;

import org.springframework.http.HttpStatus;

@SuppressWarnings("serial")
public abstract class GlobalRuntimeException extends RuntimeException {

	private final HttpStatus status;

	public GlobalRuntimeException(String message, HttpStatus status) {
		super(message);
		this.status = status;
	}

	public GlobalRuntimeException(String message, Throwable cause, HttpStatus status) {
		super(message, cause);
		this.status = status;
	}

	public HttpStatus getStatus() {
		return status;
	}

}
