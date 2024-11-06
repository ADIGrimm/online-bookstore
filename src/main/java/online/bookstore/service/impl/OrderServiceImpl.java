package online.bookstore.service.impl;

import jakarta.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import online.bookstore.dto.order.OrderDto;
import online.bookstore.mapper.OrderItemMapper;
import online.bookstore.mapper.OrderMapper;
import online.bookstore.model.Order;
import online.bookstore.model.OrderItem;
import online.bookstore.repository.cart.ShoppingCartRepository;
import online.bookstore.repository.order.OrderRepository;
import online.bookstore.repository.user.UserRepository;
import online.bookstore.service.OrderService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;
    private final UserRepository userRepository;
    private final ShoppingCartRepository shoppingCartRepository;

    @Override
    public Page<OrderDto> getAll(Long userId, Pageable pageable) {
        return orderRepository.findAllByUserId(userId, pageable).map(orderMapper::toDto);
    }

    @Override
    public OrderDto create(Long userId, String address) {
        Order order = new Order();
        order.setUser(userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Can't find user by id" + userId)));
        order.setStatus(Order.Status.Pending);
        order.setOrderItems(shoppingCartRepository.findById(userId).get().getCartItems().stream()
                .map(orderItemMapper::fromCartToOrderItem).collect(Collectors.toSet()));
        BigDecimal totalPrice = BigDecimal.ZERO;
        for (OrderItem item : order.getOrderItems()) {
            totalPrice = totalPrice.add(item.getPrice());
        }
        order.setTotal(totalPrice);
        order.setOrderDate(LocalDateTime.now());
        order.setShippingAddress(address);
        return orderMapper.toDto(orderRepository.save(order));
    }

    @Override
    public OrderDto updateStatus(Long orderId, Order.Status status) {
        Order order = orderRepository.findById(orderId).orElseThrow(()
                -> new EntityNotFoundException("Can't find order by id " + orderId));
        order.setStatus(status);
        return orderMapper.toDto(orderRepository.save(order));
    }
}
