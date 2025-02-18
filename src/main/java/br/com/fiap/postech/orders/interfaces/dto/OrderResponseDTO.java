package br.com.fiap.postech.orders.interfaces.dto;

import br.com.fiap.postech.orders.domain.entities.Address;
import br.com.fiap.postech.orders.domain.entities.Order;
import br.com.fiap.postech.orders.domain.entities.OrderItem;
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
        Address deliveryAddress,
        double totalAmount,
        PaymentMethod paymentMethod,
        LocalDateTime estimatedDeliveryDate,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static OrderResponseDTO fromDomain(Order order) {
        return new OrderResponseDTO(
                order.getId(),
                order.getStatus(),
                order.getCustomerId(),
                convertItemsToDTO(order.getItems()),
                order.getDeliveryAddress(),
                order.getTotalAmount(),
                order.getPaymentMethod(),
                order.getEstimatedDeliveryDate(),
                order.getCreatedAt(),
                order.getUpdatedAt()
        );
    }

    private static List<OrderItemResponseDTO> convertItemsToDTO(List<OrderItem> items) {
        return items.stream()
                .map(OrderItemResponseDTO::fromDomain)
                .toList();
    }
}
