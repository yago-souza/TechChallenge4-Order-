package br.com.fiap.postech.orders.interfaces.dto;

import br.com.fiap.postech.orders.domain.entities.OrderItem;
import org.springframework.validation.annotation.Validated;

import java.util.UUID;

public record OrderItemRequestDTO(
        @Validated UUID id,
        @Validated UUID productId,
        @Validated int quantity,
        @Validated double unitPrice
) {
    public OrderItem toDomain() {
        return new OrderItem(id, productId, quantity, unitPrice);
    }
}