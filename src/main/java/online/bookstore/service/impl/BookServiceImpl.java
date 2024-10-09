package online.bookstore.service.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import online.bookstore.dto.BookDto;
import online.bookstore.dto.CreateBookRequestDto;
import online.bookstore.exception.EntityNotFoundException;
import online.bookstore.mapper.BookMapper;
import online.bookstore.model.Book;
import online.bookstore.repository.BookRepository;
import online.bookstore.service.BookService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    @Override
    public BookDto save(CreateBookRequestDto bookRequestDto) {
        Book book = bookMapper.toModel(bookRequestDto);
        return bookMapper.toDto(bookRepository.save(book));
    }

    @Override
    public List<BookDto> findAll() {
        return bookRepository.findAll().stream()
                .map(bookMapper::toDto)
                .toList();
    }

    @Override
    public BookDto findById(Long id) {
        Book book = bookRepository.findById(id).orElseThrow(
                        () -> new EntityNotFoundException("Can't find book by id " + id)
                );
        return bookMapper.toDto(book);
    }
}
