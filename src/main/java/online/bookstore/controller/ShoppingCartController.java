package online.bookstore.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import online.bookstore.dto.cart.AddCartItemDto;
import online.bookstore.dto.cart.ShoppingCartDto;
import online.bookstore.dto.cart.UpdateCartItemQuantityDto;
import online.bookstore.model.User;
import online.bookstore.service.ShoppingCartService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/cart")
@RequiredArgsConstructor
@RestController
public class ShoppingCartController {
    private final ShoppingCartService shoppingCartService;

    @Operation(summary = "Add book to cart",
            description = "Add book to cart and return cart info")
    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_USER')")
    public ShoppingCartDto addBook(
            @Valid @RequestBody AddCartItemDto addCartItemDto,
            Authentication authentication
    ) {
        User user = (User) authentication.getPrincipal();
        return shoppingCartService.addBook(user.getId(), addCartItemDto);
    }

    @Operation(summary = "Get info about cart",
            description = "Return cart info")
    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_USER')")
    public ShoppingCartDto getInfo(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return shoppingCartService.getInfo(user.getId());
    }

    @Operation(summary = "Update quantity of some book",
            description = "Update quantity of some book and return cart info")
    @PutMapping("/items/{cartItemId}")
    @PreAuthorize("hasAnyRole('ROLE_USER')")
    public ShoppingCartDto updateQuantity(
            @PathVariable Long cartItemId,
            @Valid @RequestBody UpdateCartItemQuantityDto updateQuantityDto,
            Authentication authentication
    ) {
        User user = (User) authentication.getPrincipal();
        return shoppingCartService.updateQuantity(cartItemId, user.getId(), updateQuantityDto);
    }

    @Operation(summary = "Delete book from cart",
            description = "Delete book from cart and return cart info")
    @DeleteMapping("/items/{cartItemId}")
    @PreAuthorize("hasAnyRole('ROLE_USER')")
    public ShoppingCartDto deleteBook(
            @PathVariable Long cartItemId,
            Authentication authentication
    ) {
        User user = (User) authentication.getPrincipal();
        return shoppingCartService.deleteBook(cartItemId, user.getId());
    }
}
