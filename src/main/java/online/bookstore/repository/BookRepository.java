package online.bookstore.repository;

import online.bookstore.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface BookRepository {
    Book save(Book book);

    List<Book> findAll();
}
