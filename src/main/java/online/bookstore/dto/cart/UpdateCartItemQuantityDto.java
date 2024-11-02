package online.bookstore.dto.cart;

import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class UpdateCartItemQuantityDto {
    @Min(1)
    private int quantity;
}
