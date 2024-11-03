package online.bookstore.repository.cart;

import java.util.Optional;
import online.bookstore.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CartItemRepository extends
        JpaRepository<CartItem, Long>, JpaSpecificationExecutor<CartItem> {
    Optional<CartItem> findByIdAndShoppingCartId(Long id, Long cartId);
}
