package online.bookstore.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import online.bookstore.dto.order.CreateOrderRequestDto;
import online.bookstore.dto.order.OrderDto;
import online.bookstore.dto.order.OrderItemDto;
import online.bookstore.dto.order.UpdateOrderStatusDto;
import online.bookstore.service.OrderService;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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
    public Page<OrderDto> getAll(
            Authentication authentication,
            @ParameterObject @PageableDefault Pageable pageable
    ) {
        return orderService.getAll(getUserId(authentication), pageable);
    }

    @Operation(summary = "Create order",
            description = "Create order")
    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_USER')")
    public OrderDto createOrder(
            Authentication authentication,
            @RequestBody @Valid CreateOrderRequestDto requestDto
    ) {
        return orderService.create(getUserId(authentication), requestDto);
    }

    @Operation(summary = "Update order status", description =
            "Updates the order status to one of these: delivered, cancelled, pending")
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public OrderDto updateStatus(
            @PathVariable Long orderId,
            @RequestBody @Valid UpdateOrderStatusDto updateDto
    ) {
        return orderService.updateStatus(orderId, updateDto);
    }

    @Operation(summary = "", description = "")
    @GetMapping("/{orderId}/items")
    public Page<OrderItemDto> getAllOrderItems(
            Authentication authentication,
            @PathVariable Long orderId,
            @ParameterObject @PageableDefault Pageable pageable
    ) {
        return orderService.getAllItems(getUserId(authentication), orderId, pageable);
    }

    @Operation(summary = "", description = "")
    @GetMapping("/{orderId}/items/{itemId}")
    public OrderItemDto getOrderItemById(
            Authentication authentication,
            @PathVariable Long orderId,
            @PathVariable Long itemId
    ) {
        return orderService.getItemById(getUserId(authentication), orderId, itemId);
    }
}
