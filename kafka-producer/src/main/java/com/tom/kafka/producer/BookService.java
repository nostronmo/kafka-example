package com.tom.kafka.producer;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookService {

	private final BookMapper mapper;
	private final BookProducer bookProducer;
	private final GenerateData dataGeneration;

	@Transactional
	public BookResponse sendBookRandomToKafka() {
		var book = dataGeneration.processGenerateAnBook();
		bookProducer.sendBook(book);
		log.info("Sending Book: Book ID: {}, Book Title: {}", book.getId(), book.getTitle());
		return mapper.toResponse(book);
	}

	@Async
	public void generateBooks(int quantity) {
		log.info("Starting async generation of {} books...", quantity);
		dataGeneration.processGenerateBooks(quantity);
		log.info("Products finished generation");
	}

}
