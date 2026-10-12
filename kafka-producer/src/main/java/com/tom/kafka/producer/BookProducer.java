package com.tom.kafka.producer;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@KafkaStatusInterface
@RequiredArgsConstructor
public class BookProducer {

	@Value("${kafka.topics.stream.book}")
	private String topicName;

	private final KafkaTemplate<String, BookData> kafkaTemplate;
	private final BookMapper mapper;

	public void sendBook(Book book) {
		var bookEntity = mapper.toKafka(book);
		kafkaTemplate.send(topicName, bookEntity);
	}

}
