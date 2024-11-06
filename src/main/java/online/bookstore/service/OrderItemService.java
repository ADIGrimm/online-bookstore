package online.bookstore.service;

import online.bookstore.dto.order.OrderItemDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderItemService {
    Page<OrderItemDto> getAllItems(Long userId, Long orderId, Pageable pageable);

    OrderItemDto getItemById(Long userId, Long orderId, Long itemId);
}
