package online.bookstore.service.impl;

import jakarta.persistence.EntityNotFoundException;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import online.bookstore.dto.cart.AddCartItemDto;
import online.bookstore.dto.cart.ShoppingCartDto;
import online.bookstore.dto.cart.UpdateCartItemQuantityDto;
import online.bookstore.mapper.CartItemMapper;
import online.bookstore.mapper.ShoppingCartMapper;
import online.bookstore.model.CartItem;
import online.bookstore.model.ShoppingCart;
import online.bookstore.repository.cart.ShoppingCartRepository;
import online.bookstore.service.ShoppingCartService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartMapper shoppingCartMapper;
    private final ShoppingCartRepository shoppingCartRepository;
    private final CartItemMapper cartItemMapper;

    @Override
    @Transactional
    public ShoppingCartDto addBook(Long id, AddCartItemDto addCartItemDto) {
        ShoppingCart shoppingCart = shoppingCartRepository.findById(id).orElseThrow(()
                -> new EntityNotFoundException("Can't find shopping cart by id " + id));
        CartItem cartItem = cartItemMapper.toEntity(addCartItemDto);
        cartItem.setShoppingCart(shoppingCart);
        shoppingCart.getCartItems().add(cartItem);
        shoppingCartRepository.save(shoppingCart);
        return shoppingCartMapper.toDto(shoppingCart);
    }

    @Override
    public ShoppingCartDto getInfo(Long id) {
        return shoppingCartMapper.toDto(shoppingCartRepository.findById(id).orElseThrow(()
                -> new EntityNotFoundException("Can't find shopping cart by id " + id)));
    }

    @Override
    public ShoppingCartDto updateQuantity(
            Long cartItemId,
            Long userId,
            UpdateCartItemQuantityDto updateQuantityDto
    ) {
        ShoppingCart shoppingCart = shoppingCartRepository.findById(userId).orElseThrow(()
                -> new EntityNotFoundException("Can't find shopping cart by id " + userId));
        Set<CartItem> cartItems = shoppingCart.getCartItems();
        for (CartItem cartItem : cartItems) {
            if (cartItem.getId() == cartItemId) {
                cartItem.setQuantity(updateQuantityDto.getQuantity());
                break;
            }
        }
        shoppingCart.setCartItems(cartItems);
        shoppingCartRepository.save(shoppingCart);
        return shoppingCartMapper.toDto(shoppingCart);
    }

    @Override
    public ShoppingCartDto deleteBook(Long cartItemId, Long userId) {
        ShoppingCart shoppingCart = shoppingCartRepository.findById(userId).orElseThrow(()
                -> new EntityNotFoundException("Can't find shopping cart by id " + userId));
        Set<CartItem> cartItems = shoppingCart.getCartItems();
        for (CartItem cartItem : cartItems) {
            if (cartItem.getId() == cartItemId) {
                cartItems.remove(cartItem);
                break;
            }
        }
        shoppingCart.setCartItems(cartItems);
        shoppingCartRepository.save(shoppingCart);
        return shoppingCartMapper.toDto(shoppingCart);
    }
}
