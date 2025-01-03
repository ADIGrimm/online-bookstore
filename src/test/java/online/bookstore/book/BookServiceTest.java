package online.bookstore.book;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import online.bookstore.dto.book.BookDto;
import online.bookstore.dto.book.BookSearchParameters;
import online.bookstore.dto.book.CreateBookRequestDto;
import online.bookstore.exception.EntityNotFoundException;
import online.bookstore.mapper.BookMapper;
import online.bookstore.model.Book;
import online.bookstore.repository.book.BookRepository;
import online.bookstore.repository.book.BookSpecificationBuilder;
import online.bookstore.service.impl.BookServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {
    @Mock
    private BookRepository bookRepository;
    @Mock
    private BookMapper bookMapper;
    @Mock
    private BookSpecificationBuilder bookSpecificationBuilder;
    @InjectMocks
    private BookServiceImpl bookService;

    @Test
    public void getAllBooks_WithPageable_ShouldReturnAllBooks() {
        Pageable pageable = PageRequest.of(0, 10);
        List<Book> books = List.of(new Book(), new Book());
        Page<Book> bookPage = new PageImpl<>(books);
        Mockito.when(bookRepository.findAll(pageable)).thenReturn(bookPage);

        List<BookDto> bookDtos = List.of(new BookDto(), new BookDto());
        Mockito.when(bookMapper.toDto(Mockito.any(Book.class)))
                .thenAnswer(invocation -> {
                    Book book = invocation.getArgument(0);
                    int index = books.indexOf(book);
                    return bookDtos.get(index);
                });

        Page<BookDto> result = bookService.getAll(pageable);

        Mockito.verify(bookRepository).findAll(pageable);
        Mockito.verify(bookMapper, Mockito.times(books.size())).toDto(Mockito.any(Book.class));

        Assertions.assertEquals(bookDtos, result.getContent());
    }

    @Test
    public void getBookById_WithValidBookId_ShouldReturnValidBook() {
        Long bookId = 1L;
        Book book = new Book();
        book.setId(bookId);
        book.setAuthor("Bob");
        book.setPrice(BigDecimal.TEN);
        book.setDescription("Test book");
        book.setTitle("Bob's book");
        book.setIsbn("5-02-013850-9");
        book.setCoverImage("");
        book.setCategories(Set.of());
        book.setDeleted(false);

        BookDto bookDto = new BookDto();
        bookDto.setId(book.getId());
        bookDto.setAuthor(book.getAuthor());
        bookDto.setPrice(book.getPrice());
        bookDto.setDescription(book.getDescription());
        bookDto.setTitle(book.getTitle());
        bookDto.setIsbn(book.getIsbn());
        bookDto.setCoverImage(book.getCoverImage());
        bookDto.setCategories(book.getCategories());

        Mockito.when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        Mockito.when(bookMapper.toDto(book)).thenReturn(bookDto);

        BookDto actual = bookService.getById(bookId);

        Assertions.assertNotNull(actual);
        Assertions.assertEquals(bookDto.getAuthor(), actual.getAuthor());
        Mockito.verify(bookRepository).findById(bookId);
        Mockito.verify(bookMapper).toDto(book);
    }

    @Test
    public void getBookById_WithInvalidBookId_ShouldReturnException() {
        Long bookId = 10L;

        Mockito.when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        Exception exception = Assertions.assertThrows(
                EntityNotFoundException.class,
                () -> bookService.getById(bookId)
        );
        String expected = "Can't find book by id " + bookId;
        String actual = exception.getMessage();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void searchBookByParam_WithValidParams_ShouldReturnValidBook() {
        Book book = new Book();
        book.setId(1L);
        book.setTitle("Harry Potter");
        book.setAuthor("J.K. Rowling");
        book.setIsbn("123456789");
        book.setPrice(BigDecimal.valueOf(19.99));
        book.setDescription("A fantasy book");
        book.setCoverImage("cover.jpg");
        book.setCategories(Set.of());
        book.setDeleted(false);
        BookDto bookDto = new BookDto();
        bookDto.setId(book.getId());
        bookDto.setTitle(book.getTitle());
        bookDto.setAuthor(book.getAuthor());
        bookDto.setIsbn(book.getIsbn());
        bookDto.setPrice(book.getPrice());
        bookDto.setDescription(book.getDescription());
        bookDto.setCoverImage(book.getCoverImage());
        bookDto.setCategories(book.getCategories());
        BookSearchParameters params = new BookSearchParameters("J.K. Rowling", "Harry");
        Specification<Book> specification = Specification.where(null);
        Mockito.when(bookSpecificationBuilder.build(params)).thenReturn(specification);
        List<Book> books = List.of(book);
        Mockito.when(bookRepository.findAll(specification)).thenReturn(books);
        Mockito.when(bookMapper.toDto(book)).thenReturn(bookDto);
        List<BookDto> result = bookService.search(params);
        List<BookDto> bookDtos = List.of(bookDto);
        Assertions.assertEquals(bookDtos, result);
        Mockito.verify(bookSpecificationBuilder).build(params);
        Mockito.verify(bookRepository).findAll(specification);
        Mockito.verify(bookMapper, Mockito.times(books.size())).toDto(Mockito.any(Book.class));
    }

    @Test
    public void save_WithValidRequest_ShouldReturnSavedBook() {
        CreateBookRequestDto requestDto = new CreateBookRequestDto();
        requestDto.setTitle("New Book");
        requestDto.setAuthor("Author");
        requestDto.setPrice(BigDecimal.TEN);
        requestDto.setIsbn("123-4567890123");
        requestDto.setDescription("Description");
        requestDto.setCoverImage("");

        Book book = new Book();
        book.setTitle(requestDto.getTitle());
        book.setAuthor(requestDto.getAuthor());
        book.setPrice(requestDto.getPrice());
        book.setIsbn(requestDto.getIsbn());
        book.setDescription(requestDto.getDescription());
        book.setCoverImage("");

        Book savedBook = new Book();
        savedBook.setId(1L);
        savedBook.setTitle(book.getTitle());
        savedBook.setAuthor(book.getAuthor());
        savedBook.setPrice(book.getPrice());
        savedBook.setIsbn(book.getIsbn());
        savedBook.setDescription(book.getDescription());
        savedBook.setCoverImage("");

        BookDto savedBookDto = new BookDto();
        savedBookDto.setId(savedBook.getId());
        savedBookDto.setTitle(savedBook.getTitle());
        savedBookDto.setAuthor(savedBook.getAuthor());
        savedBookDto.setPrice(savedBook.getPrice());
        savedBookDto.setIsbn(savedBook.getIsbn());
        savedBookDto.setDescription(savedBook.getDescription());
        savedBookDto.setCoverImage(savedBook.getCoverImage());

        Mockito.when(bookMapper.toModel(requestDto)).thenReturn(book);
        Mockito.when(bookRepository.save(book)).thenReturn(savedBook);
        Mockito.when(bookMapper.toDto(savedBook)).thenReturn(savedBookDto);

        BookDto actual = bookService.save(requestDto);

        Assertions.assertNotNull(actual);
        Assertions.assertEquals(savedBookDto, actual);
        Mockito.verify(bookMapper).toModel(requestDto);
        Mockito.verify(bookRepository).save(book);
        Mockito.verify(bookMapper).toDto(savedBook);
    }

    @Test
    public void update_WithValidIdAndRequest_ShouldUpdateAndReturnBook() {
        CreateBookRequestDto requestDto = new CreateBookRequestDto();
        requestDto.setTitle("Updated Book");
        requestDto.setAuthor("Updated Author");
        requestDto.setPrice(BigDecimal.valueOf(15));
        requestDto.setIsbn("987-6543210987");
        requestDto.setDescription("Updated Description");
        requestDto.setCoverImage("");
        Long bookId = 1L;
        Book existingBook = new Book();
        existingBook.setId(bookId);
        existingBook.setTitle("Old Book");
        existingBook.setAuthor("Old Author");
        existingBook.setPrice(BigDecimal.TEN);
        existingBook.setIsbn("123-4567890123");
        existingBook.setDescription("Old Description");
        existingBook.setCoverImage("");
        Mockito.when(bookRepository.findById(bookId)).thenReturn(Optional.of(existingBook));
        bookService.update(bookId, requestDto);
        Mockito.verify(bookMapper).updateBookFromDto(requestDto, existingBook);
        Mockito.verify(bookRepository).save(existingBook);
        Mockito.verify(bookMapper).toDto(existingBook);
    }

    @Test
    public void deleteById_WithValidId_ShouldDeleteBook() {
        Long bookId = 1L;
        bookService.deleteById(bookId);
        Mockito.verify(bookRepository).deleteById(bookId);
    }
}
