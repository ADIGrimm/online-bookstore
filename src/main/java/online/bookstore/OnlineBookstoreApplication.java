package online.bookstore;

import java.math.BigDecimal;
import online.bookstore.model.Book;
import online.bookstore.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EntityScan(basePackages = "online.bookstore.model")
public class OnlineBookstoreApplication {
    @Autowired
    private BookService bookService;

    public static void main(String[] args) {
        SpringApplication.run(OnlineBookstoreApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner() {
        return args -> {
            Book book = new Book();
            book.setPrice(BigDecimal.valueOf(750));
            book.setTitle("Brave New World");
            book.setAuthor("Aldous Huxley");
            book.setDescription("Brave New World is a dystopian social "
                    + "science fiction novel written by Aldous Huxley. "
                    + "It presents a future world where society is "
                    + "controlled by technology and the government.");
            book.setCoverImage("https://m.media-amazon.com/images/I/81gTwYAhU7L.jpg");
            book.setIsbn("9780060850524");
            bookService.save(book);
        };
    }
}
