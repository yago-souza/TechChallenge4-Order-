package br.com.fiap.postech.orders.infrastructure.mapper;

import br.com.fiap.postech.orders.domain.entities.Order;
import br.com.fiap.postech.orders.domain.entities.OrderItem;
import br.com.fiap.postech.orders.infrastructure.persistence.OrderEntity;
import br.com.fiap.postech.orders.infrastructure.persistence.OrderItemEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

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
                .collect(Collectors.toList()); // Coleta os resultados em uma lista


        return new OrderEntity(
                order.getId(),
                order.getStatus(),
                order.getCustomerId(),
                orderItems,
                order.getDeliveryAddress(),
                order.getTotalAmount(),
                order.getPaymentMethod(),
                order.getEstimatedDeliveryDate(),
                order.getCreatedAt(),
                order.getUpdatedAt()
        );
    }

    public Order toModel(OrderEntity entity) {
        List<OrderItem> orderItems = entity.getItems().stream()
                .map(orderItemMapper::toModel)
                .collect(Collectors.toList());

        return new Order(
                entity.getId(),
                entity.getStatus(),
                entity.getCustomerId(),
                orderItems,
                entity.getDeliveryAddress(),
                entity.getTotalAmount(),
                entity.getPaymentMethod(),
                entity.getEstimatedDeliveryDate(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}
