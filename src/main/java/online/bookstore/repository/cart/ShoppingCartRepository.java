package online.bookstore.repository.cart;

import online.bookstore.model.ShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ShoppingCartRepository extends
        JpaRepository<ShoppingCart, Long>, JpaSpecificationExecutor<ShoppingCart> {
}
