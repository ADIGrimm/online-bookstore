package online.bookstore.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import online.bookstore.dto.order.OrderItemDto;
import online.bookstore.service.OrderItemService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/orders/{orderId}/items")
@RequiredArgsConstructor
@RestController
public class OrderItemController implements UserContextHelper {
    private final OrderItemService orderItemService;

    @Operation(summary = "", description = "")
    @GetMapping
    public Page<OrderItemDto> getAllOrderItems(
            Authentication authentication,
            @PathVariable Long orderId,
            Pageable pageable
    ) {
        return orderItemService.getAllItems(getUserId(authentication), orderId, pageable);
    }

    @Operation(summary = "", description = "")
    @GetMapping("/{itemId}")
    public OrderItemDto getOrderItemById(
            Authentication authentication,
            @PathVariable Long orderId,
            @PathVariable Long itemId
    ) {
        return orderItemService.getItemById(getUserId(authentication), orderId, itemId);
    }
}
