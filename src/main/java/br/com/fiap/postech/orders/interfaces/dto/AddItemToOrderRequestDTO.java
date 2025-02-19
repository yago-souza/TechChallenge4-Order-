package br.com.fiap.postech.orders.interfaces.dto;

import br.com.fiap.postech.orders.domain.entities.OrderItem;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

public record AddItemToOrderRequestDTO(
        @NotNull(message = "O ID do produto não pode ser nulo.") UUID productId,
        @NotNull(message = "A quantidade não pode ser nula.")
        @Min(value = 1, message = "A quantidade deve ser maior que zero.") Integer quantity,
        @NotNull(message = "O valor total do item não pode ser nulo.") BigDecimal unitPrice
) {
    // Método para converter o DTO em um objeto OrderItem
    public OrderItem toDomain() {
        OrderItem item = new OrderItem();
        item.setProductId(this.productId);
        item.setUnitPrice(this.unitPrice);
        item.setQuantity(this.quantity);
        return item;
    }
}
