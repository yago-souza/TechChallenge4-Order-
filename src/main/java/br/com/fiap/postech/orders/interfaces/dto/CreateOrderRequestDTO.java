package br.com.fiap.postech.orders.interfaces.dto;

import br.com.fiap.postech.orders.domain.entities.Order;
import br.com.fiap.postech.orders.domain.enums.PaymentMethod;
import org.antlr.v4.runtime.misc.NotNull;

import java.util.List;
import java.util.UUID;

public record CreateOrderRequestDTO(
        @NotNull UUID customerId,
        @NotNull List<OrderItemRequestDTO> items,
        @NotNull String deliveryAddress,
        @NotNull PaymentMethod paymentMethod
) {
    // Método para converter DTO -> Domínio
    public Order toDomain() {
        return new Order(
                null, // ID é gerado no banco
                null, // Status inicial (ex: "OPEN")
                customerId,
                items.stream().map(OrderItemRequestDTO::toDomain).toList(),
                deliveryAddress,
                0.0, // Total será calculado no domínio
                paymentMethod,
                null, // estimatedDeliveryDate
                null, // trackingCode
                null, // createdAt
                null  // updatedAt
        );
    }
}