package br.com.fiap.postech.orders.infrastructure.mapper;

import br.com.fiap.postech.orders.domain.entities.OrderItem;
import br.com.fiap.postech.orders.infrastructure.persistence.OrderItemEntity;
import org.springframework.stereotype.Component;

@Component
public class OrderItemMapper {

    public OrderItemEntity toEntity (OrderItem orderItem) {
        return new OrderItemEntity(
                orderItem.getId(),
                orderItem.getProductId(),
                orderItem.getQuantity(),
                orderItem.getUnitPrice(),
                orderItem.getTotalPrice()
        );
    }

    public OrderItem toModel (OrderItemEntity entity) {
        return new OrderItem(
                entity.getId(),
                entity.getProductId(),
                entity.getQuantity(),
                entity.getUnitPrice()
        );
    }
}
