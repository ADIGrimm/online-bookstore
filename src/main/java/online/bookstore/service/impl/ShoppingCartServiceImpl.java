package online.bookstore.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import online.bookstore.dto.cart.AddCartItemDto;
import online.bookstore.dto.cart.ShoppingCartDto;
import online.bookstore.dto.cart.UpdateCartItemQuantityDto;
import online.bookstore.mapper.CartItemMapper;
import online.bookstore.mapper.ShoppingCartMapper;
import online.bookstore.model.CartItem;
import online.bookstore.model.ShoppingCart;
import online.bookstore.model.User;
import online.bookstore.repository.cart.CartItemRepository;
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
    private final CartItemRepository itemRepository;

    @Override
    @Transactional
    public ShoppingCartDto addBook(Long userId, AddCartItemDto addCartItemDto) {
        ShoppingCart shoppingCart = shoppingCartRepository.findById(userId).orElseThrow(()
                -> new EntityNotFoundException("Can't find shopping cart by id " + userId));
        CartItem cartItem = cartItemMapper.toEntity(addCartItemDto);
        for (CartItem item : shoppingCart.getCartItems()) {
            if (item.getBook().equals(cartItem.getBook())) {
                item.setQuantity(item.getQuantity() + addCartItemDto.getQuantity());
                return shoppingCartMapper.toDto(shoppingCart);
            }
        }
        cartItem.setShoppingCart(shoppingCart);
        shoppingCart.getCartItems().add(cartItem);
        shoppingCartRepository.save(shoppingCart);
        return shoppingCartMapper.toDto(shoppingCart);
    }

    @Override
    public ShoppingCartDto getInfo(Long userId) {
        return shoppingCartMapper.toDto(shoppingCartRepository.findById(userId).orElseThrow(()
                -> new EntityNotFoundException("Can't find shopping cart by id " + userId)));
    }

    @Override
    @Transactional
    public ShoppingCartDto updateQuantity(
            Long cartItemId,
            Long userId,
            UpdateCartItemQuantityDto updateQuantityDto
    ) {
        ShoppingCart shoppingCart = shoppingCartRepository.findById(userId).orElseThrow(()
                -> new EntityNotFoundException("Can't find shopping cart by id " + userId));
        CartItem cartItem =
                itemRepository.findByIdAndShoppingCartId(cartItemId, shoppingCart.getId())
                .orElseThrow(() -> new EntityNotFoundException("Can't find cart item by id "
                        + cartItemId + " and cart id " + shoppingCart.getId()));
        cartItem.setQuantity(updateQuantityDto.getQuantity());
        shoppingCartRepository.save(shoppingCart);
        return shoppingCartMapper.toDto(shoppingCart);
    }

    @Override
    @Transactional
    public void deleteBook(Long cartItemId, Long userId) {
        ShoppingCart shoppingCart = shoppingCartRepository.findById(userId).orElseThrow(()
                -> new EntityNotFoundException("Can't find shopping cart by id " + userId));
        CartItem cartItem =
                itemRepository.findByIdAndShoppingCartId(cartItemId, shoppingCart.getId())
                        .orElseThrow(()
                                -> new EntityNotFoundException("Can't find cart item by id "
                                + cartItemId + " and cart id " + shoppingCart.getId()));
        itemRepository.delete(cartItem);
        shoppingCartRepository.save(shoppingCart);
    }

    @Override
    @Transactional
    public void createShoppingCart(User user) {
        ShoppingCart cart = new ShoppingCart();
        cart.setUser(user);
        shoppingCartRepository.save(cart);
    }
}
