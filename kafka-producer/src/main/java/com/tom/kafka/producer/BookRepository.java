package com.tom.kafka.producer;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface BookRepository extends JpaRepository<Book, Long>, JpaSpecificationExecutor<Book> {

	Book findByIsbn(String isbn);

	Optional<Book> findByTitle(String title);
	
	boolean existsByIsbn(String isbn);
	
	boolean existsByTitle(String title);
	
}
