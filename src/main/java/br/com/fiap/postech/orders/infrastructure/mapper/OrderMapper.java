package br.com.fiap.postech.orders.infrastructure.mapper;

import br.com.fiap.postech.orders.domain.entities.Order;
import br.com.fiap.postech.orders.domain.entities.OrderItem;
import br.com.fiap.postech.orders.infrastructure.messaging.OrderCreatedEvent;
import br.com.fiap.postech.orders.infrastructure.persistence.OrderEntity;
import br.com.fiap.postech.orders.infrastructure.persistence.OrderItemEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderMapper {
    private OrderItemMapper orderItemMapper;

    public OrderMapper(OrderItemMapper orderItemMapper) {
        this.orderItemMapper = orderItemMapper;
    }

    public OrderEntity toEntity(Order order) {
        // Usando stream().map() para transformar a lista de OrderItem em OrderItemEntity
        List<OrderItemEntity> orderItems = order.getItems().stream()
                .map(orderItemMapper::toEntity) // Mapeia cada OrderItem para OrderItemEntity
                .toList(); // Coleta os resultados em uma lista


        return new OrderEntity(
                order.getId(),
                order.getUpdatedAt(),
                order.getCreatedAt(),
                order.getEstimatedDeliveryDate(),
                order.getPaymentMethod(),
                order.getDeliveryAddress(),
                orderItems,
                order.getCustomerId(),
                order.getStatus()
        );
    }

    public Order toModel(OrderEntity entity) {
        List<OrderItem> orderItems = entity.getItems().stream()
                .map(orderItemMapper::toModel)
                .toList();

        return new Order(
                entity.getStatus(),
                entity.getCustomerId(),
                orderItems,
                entity.getDeliveryAddress(),
                entity.getPaymentMethod(),
                entity.getEstimatedDeliveryDate(),
                entity.getCreatedAt()
        );
    }
}
