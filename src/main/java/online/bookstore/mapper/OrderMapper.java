package online.bookstore.mapper;

import online.bookstore.config.MapperConfig;
import online.bookstore.dto.order.OrderDto;
import online.bookstore.model.Order;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface OrderMapper {
    OrderDto toDto(Order order);
}
