package online.bookstore.dto.book;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Set;
import lombok.Data;
import online.bookstore.model.Category;
import org.hibernate.validator.constraints.ISBN;

@Data
public class CreateBookRequestDto {
    @NotBlank
    private String title;
    @NotBlank
    private String author;
    @ISBN
    @NotBlank
    private String isbn;
    @Min(0)
    @NotNull
    private BigDecimal price;
    private String description;
    private String coverImage;
    @NotEmpty
    private Set<Category> categories;
}
