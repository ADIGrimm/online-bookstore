package online.bookstore.repository.book.specifications;

import online.bookstore.model.Book;
import online.bookstore.repository.SpecificationProvider;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class AuthorSpecificationProvider implements SpecificationProvider<Book> {
    @Override
    public String getKey() {
        return "author";
    }

    public Specification<Book> getSpecification(String param) {
        return (root, query, criteriaBuilder) -> root.get("author").in(param);
    }
}
