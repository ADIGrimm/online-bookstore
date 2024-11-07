package online.bookstore.dto.order;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import online.bookstore.model.Order;

@Data
public class UpdateOrderStatusDto {
    @NotNull
    private Order.Status status;
}
