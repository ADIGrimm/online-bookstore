package online.bookstore.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import online.bookstore.dto.order.OrderDto;
import online.bookstore.model.Order;
import online.bookstore.service.OrderService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/orders")
@RequiredArgsConstructor
@RestController
public class OrderController implements UserContextHelper {
    private final OrderService orderService;

    @Operation(summary = "Get user order history",
            description = "Return list of all orders of user")
    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_USER')")
    public Page<OrderDto> getAll(Authentication authentication, Pageable pageable) {
        return orderService.getAll(getUserId(authentication), pageable);
    }

    @Operation(summary = "Create order",
            description = "Create order")
    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_USER')")
    public OrderDto createOrder(Authentication authentication, @RequestBody String address) {
        return orderService.create(getUserId(authentication), address);
    }

    @Operation(summary = "Update order status", description =
            "Updates the order status to one of these: delivered, cancelled, pending")
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public OrderDto updateStatus(@PathVariable Long orderId, @RequestBody Order.Status status) {
        return orderService.updateStatus(orderId, status);
    }
}
