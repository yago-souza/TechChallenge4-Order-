package br.com.fiap.postech.orders.interfaces.dto;

import br.com.fiap.postech.orders.domain.enums.OrderStatus;
import br.com.fiap.postech.orders.domain.enums.PaymentMethod;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record OrderResponseDTO(
        UUID id,
        OrderStatus status,
        UUID customerId,
        List<OrderItemResponseDTO> items,
        String deliveryAddress,
        double totalAmount,
        PaymentMethod paymentMethod,
        LocalDateTime estimatedDeliveryDate,
        String trackingCode,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static OrderResponseDTO fromDomain(br.com.fiap.postech.orders.domain.entities.Order order) {
        return new OrderResponseDTO(
                order.getId(),
                order.getStatus(),
                order.getCustomerId(),
                convertItemsToDTO(order.getItems()),
                order.getDeliveryAddress(),
                order.getTotalAmount(),
                order.getPaymentMethod(),
                order.getEstimatedDeliveryDate(),
                order.getTrackingCode(),
                order.getCreatedAt(),
                order.getUpdatedAt()
        );
    }

    private static List<OrderItemResponseDTO> convertItemsToDTO(List<br.com.fiap.postech.orders.domain.entities.OrderItem> items) {
        return items.stream()
                .map(OrderItemResponseDTO::fromDomain)
                .toList();
    }
}
