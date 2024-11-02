package online.bookstore.service;

import online.bookstore.dto.cart.AddCartItemDto;
import online.bookstore.dto.cart.ShoppingCartDto;
import online.bookstore.dto.cart.UpdateCartItemQuantityDto;

public interface ShoppingCartService {
    ShoppingCartDto addBook(Long id, AddCartItemDto addCartItemDto);

    ShoppingCartDto getInfo(Long id);

    ShoppingCartDto updateQuantity(
            Long id,
            Long userId,
            UpdateCartItemQuantityDto updateQuantityDto
    );

    ShoppingCartDto deleteBook(Long cartItemId, Long userId);
}
