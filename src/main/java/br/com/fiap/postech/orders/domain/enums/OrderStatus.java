package br.com.fiap.postech.orders.domain.enums;

public enum OrderStatus {
    OPEN,
    PAID,
    PROCESSING, // Pedido em preparação
    SHIPPED,
    DELIVERED,
    CANCELED,
    RETURNED //Para casos de devolução
}
