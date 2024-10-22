package online.bookstore.service;

import java.util.List;
import online.bookstore.dto.book.BookDto;
import online.bookstore.dto.book.BookSearchParameters;
import online.bookstore.dto.book.CreateBookRequestDto;
import org.springframework.data.domain.Pageable;

public interface BookService {
    BookDto save(CreateBookRequestDto bookRequestDto);

    List<BookDto> findAll(Pageable pageable);

    BookDto findById(Long id);

    void deleteById(Long id);

    BookDto update(Long id, CreateBookRequestDto bookRequestDto);

    List<BookDto> search(BookSearchParameters params);
}
