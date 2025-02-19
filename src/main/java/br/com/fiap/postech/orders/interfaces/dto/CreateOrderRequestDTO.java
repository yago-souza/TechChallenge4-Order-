package br.com.fiap.postech.orders.interfaces.dto;

import br.com.fiap.postech.orders.domain.entities.Address;
import br.com.fiap.postech.orders.domain.entities.Order;
import br.com.fiap.postech.orders.domain.entities.OrderItem;
import br.com.fiap.postech.orders.domain.enums.OrderStatus;
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
        Order order = new Order(
                OrderStatus.OPEN,
                customerId,
                null, // Lista de itens será preenchida depois
                deliveryAddress,
                paymentMethod,
                null, // estimatedDeliveryDate
                null  // createdAt
        );

        // Associa os itens ao pedido
        List<OrderItem> domainItems = items.stream()
                .map(item -> item.toDomain(order))
                .toList();

        order.setItems(domainItems);
        return order;
    }
}