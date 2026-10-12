package com.tom.kafka.consumer;

import java.math.BigDecimal;
import java.time.LocalDate;

public record BookData(
		
	    String isbn,
	    String title,
	    String author,
	    BigDecimal price,
	    LocalDate publishedDate,
	    Integer stockQuantity
		
		) {

}
