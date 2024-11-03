package online.bookstore.service;

import online.bookstore.dto.cart.AddCartItemDto;
import online.bookstore.dto.cart.ShoppingCartDto;
import online.bookstore.dto.cart.UpdateCartItemQuantityDto;
import online.bookstore.model.User;

public interface ShoppingCartService {
    ShoppingCartDto addBook(Long id, AddCartItemDto addCartItemDto);

    ShoppingCartDto getInfo(Long id);

    ShoppingCartDto updateQuantity(
            Long id,
            Long userId,
            UpdateCartItemQuantityDto updateQuantityDto
    );

    void deleteBook(Long cartItemId, Long userId);

    void createShoppingCart(User user);
}
