package online.bookstore.dto.cart;

import java.util.Set;
import lombok.Data;
import online.bookstore.model.CartItem;

@Data
public class ShoppingCartDto {
    private Long id;
    private Long userId;
    private Set<CartItem> cartItems;
}
