package online.bookstore.service;

import online.bookstore.dto.order.OrderDto;
import online.bookstore.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderService {
    Page<OrderDto> getAll(Long userId, Pageable pageable);

    OrderDto create(Long userId, String address);

    OrderDto updateStatus(Long orderId, Order.Status status);
}
