package com.tom.kafka.consumer;

import org.springframework.http.HttpStatus;

import lombok.EqualsAndHashCode;

@SuppressWarnings("serial")
@EqualsAndHashCode(callSuper = true)
public class KafkaUnavailableException extends GlobalRuntimeException {

	public KafkaUnavailableException(String message) {
		super(message, HttpStatus.SERVICE_UNAVAILABLE);
	}

	public KafkaUnavailableException(String message, Throwable cause) {
		super(message, cause, HttpStatus.SERVICE_UNAVAILABLE);
	}

}
