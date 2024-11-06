package online.bookstore.service.impl;

import lombok.RequiredArgsConstructor;
import online.bookstore.dto.order.OrderItemDto;
import online.bookstore.mapper.OrderItemMapper;
import online.bookstore.repository.order.OrderItemRepository;
import online.bookstore.service.OrderItemService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderItemServiceImpl implements OrderItemService {
    private final OrderItemRepository orderItemRepository;
    private final OrderItemMapper orderItemMapper;

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
