package br.com.fiap.postech.orders.interfaces.dto;

import br.com.fiap.postech.orders.domain.entities.OrderItem;
import org.antlr.v4.runtime.misc.NotNull;

import java.util.UUID;

public record OrderItemRequestDTO(
        @NotNull UUID id,
        @NotNull UUID productId,
        @NotNull int quantity,
        @NotNull double unitPrice
) {
    public OrderItem toDomain() {
        return new OrderItem(id, productId, quantity, unitPrice, 0.0);
    }
}