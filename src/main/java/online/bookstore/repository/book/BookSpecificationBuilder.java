package online.bookstore.repository.book;

import lombok.RequiredArgsConstructor;
import online.bookstore.dto.book.BookSearchParameters;
import online.bookstore.model.Book;
import online.bookstore.repository.SpecificationBuilder;
import online.bookstore.repository.SpecificationProviderManager;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class BookSpecificationBuilder implements SpecificationBuilder<Book> {
    private SpecificationProviderManager<Book> bookSpecificationProviderManager;

    @Override
    public Specification<Book> build(BookSearchParameters searchParameters) {
        Specification<Book> spec = Specification.where(null);
        if (searchParameters.author() != null) {
            spec.and(bookSpecificationProviderManager.getSpecificationProvider("author")
                    .getSpecification(searchParameters.author()));
        }
        if (searchParameters.titlePart() != null) {
            spec.and(bookSpecificationProviderManager.getSpecificationProvider("title")
                    .getSpecification(searchParameters.titlePart()));
        }
        return spec;
    }
}
