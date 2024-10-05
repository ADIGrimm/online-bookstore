package online.bookstore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan(basePackages = "online.bookstore")
public class OnlineBookstoreApplication {
    public static void main(String[] args) {
        SpringApplication.run(OnlineBookstoreApplication.class, args);
    }
}

