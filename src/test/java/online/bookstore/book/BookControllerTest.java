package online.bookstore.book;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import javax.sql.DataSource;
import lombok.SneakyThrows;
import online.bookstore.dto.book.BookDto;
import online.bookstore.dto.book.CreateBookRequestDto;
import online.bookstore.model.Book;
import online.bookstore.model.Category;
import online.bookstore.repository.book.BookRepository;
import online.bookstore.security.JwtAuthenticationFilter;
import online.bookstore.security.JwtUtil;
import online.bookstore.service.impl.BookServiceImpl;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BookControllerTest {
    protected static MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private BookRepository bookRepository;
    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;
    @MockBean
    private JwtUtil jwtUtil;
    @Mock
    private BookServiceImpl bookService;

    @BeforeAll
    static void beforeAll(
            @Autowired WebApplicationContext applicationContext,
            @Autowired DataSource dataSource
    ) throws SQLException {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .apply(springSecurity())
                .build();
        teardown(dataSource);
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("database/books/create-some-books.sql")
            );
        }
    }

    @AfterAll
    static void afterAll(
            @Autowired DataSource dataSource
    ) {
        teardown(dataSource);
    }

    @SneakyThrows
    private static void teardown(DataSource dataSource) {
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("database/books/delete-all-books.sql")
            );
        }
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void getAllBooks_ShouldReturnPaginatedBooks_IgnoringCategories() throws Exception {
        List<BookDto> expectedBooks = createThreeBookDtoList();
        Pageable pageable = PageRequest.of(0, 3);
        Page<BookDto> bookPage = new PageImpl<>(expectedBooks, pageable, expectedBooks.size());
        when(bookService.getAll(pageable)).thenReturn(bookPage);

        MvcResult result = mockMvc.perform(get("/books")
                        .param("page", "0")
                        .param("size", "3")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        List<BookDto> actualBooks = objectMapper.readValue(
                objectMapper.readTree(jsonResponse).get("content").toString(),
                new TypeReference<List<BookDto>>() {}
        );
        Assertions.assertEquals(expectedBooks.size(), actualBooks.size());
        for (int i = 0; i < expectedBooks.size(); i++) {
            BookDto expectedBook = expectedBooks.get(i);
            BookDto actualBook = actualBooks.get(i);
            EqualsBuilder.reflectionEquals(expectedBook, actualBook, "categories");
        }
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void getById_WithValidId_ShouldReturnBookDto() throws Exception {
        Long bookId = 1L;
        BookDto expected = createThreeBookDtoList().getFirst();
        when(bookService.getById(bookId)).thenReturn(expected);
        MvcResult result = mockMvc.perform(get("/books/{id}", bookId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        String jsonResponse = result.getResponse().getContentAsString();
        BookDto actual = objectMapper.readValue(jsonResponse, BookDto.class);
        EqualsBuilder.reflectionEquals(expected, actual, "categories");
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Sql(
            scripts = "classpath:database/books/create-horror-category.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = "classpath:database/books/delete-bind.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    @Sql(
            scripts = "classpath:database/books/delete-new-book.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    @Sql(
            scripts = "classpath:database/books/delete-horror-category.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    void createBook() throws Exception {
        CreateBookRequestDto requestBook = createBookRequestDtoWithCategory();
        String jsonRequest = objectMapper.writeValueAsString(requestBook);
        MvcResult result = mockMvc.perform(post("/books")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();
        BookDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                BookDto.class
        );
        Assertions.assertNotNull(actual.getId());
        Optional<Book> savedBook = bookRepository.findById(actual.getId());
        Assertions.assertTrue(savedBook.isPresent());
        Assertions.assertEquals(requestBook.getTitle(), savedBook.get().getTitle());
        Assertions.assertEquals(requestBook.getAuthor(), savedBook.get().getAuthor());
    }

    private List<BookDto> createThreeBookDtoList() {
        List<BookDto> books = new ArrayList<>();
        books.add(new BookDto()
                .setId(1L)
                .setTitle("Book One")
                .setAuthor("Author One")
                .setPrice(BigDecimal.valueOf(19.99))
                .setIsbn("978-3-16-148410-0")
                .setDescription("Description for book one")
                .setCoverImage("http://example.com/book1.jpg")
                .setCategories(Set.of()));
        books.add(new BookDto()
                .setId(2L)
                .setTitle("Book Two")
                .setAuthor("Author Two")
                .setPrice(BigDecimal.valueOf(29.99))
                .setIsbn("978-3-16-148411-7")
                .setDescription("Description for book two")
                .setCoverImage("http://example.com/book2.jpg")
                .setCategories(Set.of()));
        books.add(new BookDto()
                .setId(3L)
                .setTitle("Book Three")
                .setAuthor("Author Three")
                .setPrice(BigDecimal.valueOf(39.99))
                .setIsbn("978-3-16-148412-4")
                .setDescription("Description for book three")
                .setCoverImage("http://example.com/book3.jpg")
                .setCategories(Set.of()));
        return books;
    }

    private CreateBookRequestDto createBookRequestDtoWithCategory() {
        CreateBookRequestDto requestBook = new CreateBookRequestDto();
        requestBook.setTitle("New Book");
        requestBook.setAuthor("Author");
        requestBook.setPrice(BigDecimal.TEN);
        requestBook.setIsbn("978-3-16-148410-0");
        requestBook.setDescription("Description");
        requestBook.setCoverImage("http://example.com/cover3.jpg");
        Category category = new Category();
        category.setId(1L);
        category.setName("Horror");
        category.setDescription("Book contain horror moments");
        requestBook.setCategories(Set.of(category));
        return requestBook;
    }
}
