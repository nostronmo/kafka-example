package com.tom.kafka.producer;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.IntStream;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class GenerateData {

	private final BookRepository bookRepository;
	private final BookMapper mapper;
	private final DataGeneratorUtil dataUtil;
    
	@Async
	public void processGenerateBooks(int quantity) {
	    List<Book> books = IntStream.range(0, quantity)
	            .parallel()
	            .mapToObj(i -> genBook())
	            .toList();

	    saveInBatches(books, 500);
	}

	@Transactional
	void saveInBatches(List<Book> books, int batchSize) {
	    for (int i = 0; i < books.size(); i += batchSize) {
	        List<Book> batch = books.subList(i, Math.min(i + batchSize, books.size()));
	        bookRepository.saveAll(batch);
	        bookRepository.flush();
	    }
	}
    
    @Transactional
    public Book processGenerateAnBook() {
    	var gen = genBook();
    	var bookSaved = bookRepository.save(gen);
		return bookSaved;
    }
    
	private Book genBook() {
		Book book = new Book();
		
		String isbn = dataUtil.generateIsbn();
		String title = dataUtil.generateBookTitle();
		String author = dataUtil.generateAuthorNames();
		BigDecimal price = dataUtil.getRandonBigDecimal(4, 250);
        LocalDate startDate = LocalDate.of(1899, 1, 1);
        LocalDate endDate = LocalDate.of(2020, 1, 1);
		LocalDate publishedDate = dataUtil.getRandomDate(startDate, endDate);
		Integer stockQuantity = dataUtil.getRandomInteger(1, 1000);
		
		var bookUpdated = mapper.updateBookFromData(book, isbn, title, author, price, publishedDate, stockQuantity);
		return bookUpdated;
	}
	
}
