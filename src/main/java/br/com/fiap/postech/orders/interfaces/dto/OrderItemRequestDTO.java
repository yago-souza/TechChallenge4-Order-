package br.com.fiap.postech.orders.interfaces.dto;

import br.com.fiap.postech.orders.domain.entities.Order;
import br.com.fiap.postech.orders.domain.entities.OrderItem;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.util.UUID;

public record OrderItemRequestDTO(
        @Validated UUID id,
        @Validated UUID productId,
        @Validated int quantity,
        @Validated BigDecimal unitPrice
) {
    public OrderItem toDomain(Order order) {
        return new OrderItem(id, productId, quantity, unitPrice, order);
    }
}
