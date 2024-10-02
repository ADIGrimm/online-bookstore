package online.bookstore.service.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import online.bookstore.model.Book;
import online.bookstore.repository.BookRepository;
import online.bookstore.service.BookService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;

    @Override
    public Book save(Book book) {
        return bookRepository.save(book);
    }

    @Override
    public List<Book> findAll() {
        return bookRepository.findAll();
    }
}
