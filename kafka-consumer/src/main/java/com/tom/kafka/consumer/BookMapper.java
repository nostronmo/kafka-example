package com.tom.kafka.consumer;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.springframework.data.domain.Page;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BookMapper {

	BookResponse toResponse(Book book);
	
	@Mapping(target = "id", ignore = true)
	Book build(BookData book);
	
	List<BookResponse> toResponseList(List<Book> books);
	default BookPageResponse toBookPageResponse(Page<Book> page) {
		if(page == null) {
			return null;
		}
		List<BookResponse> content = toResponseList(page.getContent());
		return new BookPageResponse(content,
				page.getNumber(),
				page.getSize(),
				page.getTotalPages(),
				page.getTotalElements()
			);
	}
	
}
