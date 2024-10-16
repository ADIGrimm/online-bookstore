package online.bookstore.service;

import java.util.List;
import online.bookstore.dto.BookDto;
import online.bookstore.dto.BookSearchParameters;
import online.bookstore.dto.CreateBookRequestDto;
import org.springframework.data.domain.Pageable;

public interface BookService {
    BookDto save(CreateBookRequestDto bookRequestDto);

    List<BookDto> findAll(Pageable pageable);

    BookDto findById(Long id);

    void deleteById(Long id);

    BookDto update(Long id, CreateBookRequestDto bookRequestDto);

    List<BookDto> search(BookSearchParameters params);
}
