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
		return new CommandLineRunner() {
			@Override
			public void run(String... args) throws Exception {
				Book book = new Book();
				book.setPrice(BigDecimal.valueOf(500));
				book.setTitle("1984");
				book.setAuthor("George Orwell");
				book.setDescription("1984 is a dystopian novel that was written by George Orwell "
						+ "and published in 1949. It tells the story of Winston Smith, "
						+ "a citizen of the miserable society of Oceania,"
						+ "who is trying to rebel against "
						+ "the Party and its omnipresent symbol, Big Brother.");
				book.setCoverImage("https://m.media-amazon.com/images/I/"
						+ "61ZewDE3beL._AC_UF894,1000_QL80_.jpg");
				book.setIsbn("9781443434973");
				bookService.save(book);
			}
		};
	}
}

