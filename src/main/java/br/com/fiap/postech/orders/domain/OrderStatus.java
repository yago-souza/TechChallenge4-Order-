package br.com.fiap.postech.orders.domain;

public enum OrderStatus {
    OPEN,
    PAID,
    IN_PROGRESS,
    SHIPPED,
    DELIVERED,
    CANCELED,
    RETURNED //Para casos de devolução
}
