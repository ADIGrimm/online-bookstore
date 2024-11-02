package online.bookstore.dto.cart;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AddCartItemDto {
    @NotBlank
    private Long bookId;
    @Min(1)
    private int quantity;
}
