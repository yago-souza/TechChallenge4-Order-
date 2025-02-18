package br.com.fiap.postech.orders.interfaces.dto;

import br.com.fiap.postech.orders.domain.entities.Address;
import br.com.fiap.postech.orders.domain.entities.Order;
import br.com.fiap.postech.orders.domain.enums.PaymentMethod;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.UUID;

public record CreateOrderRequestDTO(
        @NotNull(message = "O ID do cliente é obrigatório")
        @Validated UUID customerId,
        @NotEmpty(message = "A lista de itens não pode estar vazia")
        @Validated List<OrderItemRequestDTO> items,
        @NotNull(message = "O endereço não pode estar vazio")
        @Validated Address deliveryAddress,
        @NotNull(message = "O método de pagamento é obrigatório")
        @Validated PaymentMethod paymentMethod
) {
    // Método para converter DTO -> Domínio
    public Order toDomain() {
        return new Order(
                null, // Status inicial (ex: "OPEN")
                customerId,
                items.stream().map(OrderItemRequestDTO::toDomain).toList(),
                deliveryAddress,
                paymentMethod,
                null, // estimatedDeliveryDate
                null // createdAt
        );
    }
}