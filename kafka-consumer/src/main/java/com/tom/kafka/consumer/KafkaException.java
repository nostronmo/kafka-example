package com.tom.kafka.consumer;

import org.springframework.http.HttpStatus;

import lombok.EqualsAndHashCode;

@SuppressWarnings("serial")
@EqualsAndHashCode(callSuper = true)
public class KafkaException extends GlobalRuntimeException {

	public KafkaException(String message) {
		super(message, HttpStatus.CONFLICT);
	}

	public KafkaException(String message, Throwable cause) {
		super(message, cause, HttpStatus.CONFLICT);
	}

}
