package br.com.fiap.postech.orders.interfaces.dto;

import java.math.BigDecimal;
import java.util.UUID;

record OrderItemResponseDTO(
        UUID productId,
        int quantity,
        BigDecimal unitPrice,
        BigDecimal totalPrice
) {
    public static OrderItemResponseDTO fromDomain(br.com.fiap.postech.orders.domain.entities.OrderItem item) {
        return new OrderItemResponseDTO(
                item.getProductId(),
                item.getQuantity(),
                item.getUnitPrice(),
                item.getTotalPrice()
        );
    }
}