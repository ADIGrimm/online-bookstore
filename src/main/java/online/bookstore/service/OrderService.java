package online.bookstore.service;

import jakarta.validation.Valid;
import online.bookstore.dto.order.CreateOrderRequestDto;
import online.bookstore.dto.order.OrderDto;
import online.bookstore.dto.order.OrderItemDto;
import online.bookstore.dto.order.UpdateOrderStatusDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderService {
    Page<OrderDto> getAll(Long userId, Pageable pageable);

    OrderDto create(Long userId, @Valid CreateOrderRequestDto address);

    OrderDto updateStatus(Long orderId, @Valid UpdateOrderStatusDto status);

    Page<OrderItemDto> getAllItems(Long userId, Long orderId, Pageable pageable);

    OrderItemDto getItemById(Long userId, Long orderId, Long itemId);
}
