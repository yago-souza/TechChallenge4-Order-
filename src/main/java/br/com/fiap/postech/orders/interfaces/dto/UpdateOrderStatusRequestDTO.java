package br.com.fiap.postech.orders.interfaces.dto;

import br.com.fiap.postech.orders.domain.enums.OrderStatus;
import jakarta.validation.constraints.NotNull;

public record UpdateOrderStatusRequestDTO(
        @NotNull(message = "O novo status do pedido não pode ser nulo.") OrderStatus newStatus
) {
}