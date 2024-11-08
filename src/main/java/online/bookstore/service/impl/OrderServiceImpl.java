package online.bookstore.service.impl;

import jakarta.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import online.bookstore.dto.order.CreateOrderRequestDto;
import online.bookstore.dto.order.OrderDto;
import online.bookstore.dto.order.OrderItemDto;
import online.bookstore.dto.order.UpdateOrderStatusDto;
import online.bookstore.exception.OrderProcessingException;
import online.bookstore.mapper.OrderItemMapper;
import online.bookstore.mapper.OrderMapper;
import online.bookstore.model.CartItem;
import online.bookstore.model.Order;
import online.bookstore.model.OrderItem;
import online.bookstore.repository.cart.ShoppingCartRepository;
import online.bookstore.repository.order.OrderItemRepository;
import online.bookstore.repository.order.OrderRepository;
import online.bookstore.repository.user.UserRepository;
import online.bookstore.service.OrderService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;
    private final UserRepository userRepository;
    private final ShoppingCartRepository shoppingCartRepository;
    private final OrderItemRepository orderItemRepository;

    @Override
    public Page<OrderDto> getAll(Long userId, Pageable pageable) {
        return orderRepository.findAllByUserId(userId, pageable).map(orderMapper::toDto);
    }

    @Override
    @Transactional
    public OrderDto create(Long userId, CreateOrderRequestDto address) {
        Order order = orderMapper.toModel(address);
        order.setUser(userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Can't find user by id" + userId)));
        order.setStatus(Order.Status.PENDING);
        Set<CartItem> cartItems = shoppingCartRepository.findById(userId).get().getCartItems();
        if (cartItems.isEmpty()) {
            throw new OrderProcessingException("Shopping cart is empty for user " + userId);
        }
        Set<OrderItem> orderItems = cartItems.stream()
                .map(orderItemMapper::fromCartToOrderItem)
                .collect(Collectors.toSet());
        order.setOrderItems(orderItems);
        BigDecimal totalPrice = orderItems.stream()
                .map(OrderItem::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        order.setTotal(totalPrice);
        order.setOrderDate(LocalDateTime.now());
        return orderMapper.toDto(orderRepository.save(order));
    }

    @Override
    public OrderDto updateStatus(Long orderId, UpdateOrderStatusDto updateDto) {
        Order order = orderRepository.findById(orderId).orElseThrow(()
                -> new EntityNotFoundException("Can't find order by id " + orderId));
        order.setStatus(updateDto.getStatus());
        return orderMapper.toDto(orderRepository.save(order));
    }

    @Override
    public Page<OrderItemDto> getAllItems(Long userId, Long orderId, Pageable pageable) {
        return orderItemRepository.findAllByOrderUserIdAndOrderId(userId, orderId, pageable)
                .map(orderItemMapper::toDto);
    }

    @Override
    public OrderItemDto getItemById(Long userId, Long orderId, Long itemId) {
        return orderItemMapper.toDto(orderItemRepository
                .findByOrderUserIdAndOrderIdAndId(userId, orderId, itemId));
    }
}
