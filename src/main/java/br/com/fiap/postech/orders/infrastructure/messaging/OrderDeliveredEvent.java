package br.com.fiap.postech.orders.infrastructure.messaging;

import br.com.fiap.postech.orders.domain.enums.OrderStatus;

import java.util.UUID;

public class OrderDeliveredEvent {
    private UUID orderId;
    private OrderStatus orderStatus;

    public OrderDeliveredEvent() {
    }

    public OrderDeliveredEvent(UUID orderId, OrderStatus orderStatus) {
        this.orderId = orderId;
        this.orderStatus = orderStatus;
    }

    public UUID getOrderId() {
        return orderId;
    }

    public void setOrderId(UUID orderId) {
        this.orderId = orderId;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }
}
