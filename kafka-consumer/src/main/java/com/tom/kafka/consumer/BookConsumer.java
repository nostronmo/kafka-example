package com.tom.kafka.consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@KafkaStatusInterface
@RequiredArgsConstructor
public class BookConsumer {

	private final BookRepository bookRepository;
	private final BookMapper mapper;

	@KafkaListener(topics = "${kafka.topics.stream.book}", containerFactory = "kafkaListenerContainerFactory")
	public void recieveBookData(BookData bookDto) {
		var bookEntity = mapper.build(bookDto);
		log.info("Recieved Book: {}", bookDto.title());
		bookRepository.save(bookEntity);
	}

}
