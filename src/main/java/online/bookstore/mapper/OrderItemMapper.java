package online.bookstore.mapper;

import java.math.BigDecimal;
import online.bookstore.config.MapperConfig;
import online.bookstore.dto.order.OrderItemDto;
import online.bookstore.model.CartItem;
import online.bookstore.model.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class, imports = BigDecimal.class)
public interface OrderItemMapper {
    OrderItemDto toDto(OrderItem orderItem);

    @Mapping(target = "order", ignore = true)
    @Mapping(target = "price", expression = "java(cartItem.getBook().getPrice() "
            + " .multiply(BigDecimal.valueOf(cartItem.getQuantity())))")
    OrderItem fromCartToOrderItem(CartItem cartItem);
}
