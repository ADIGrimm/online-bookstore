package online.bookstore.mapper;

import online.bookstore.config.MapperConfig;
import online.bookstore.dto.cart.AddCartItemDto;
import online.bookstore.model.CartItem;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface CartItemMapper {
    CartItem toEntity(AddCartItemDto addCartItemDto);
}
