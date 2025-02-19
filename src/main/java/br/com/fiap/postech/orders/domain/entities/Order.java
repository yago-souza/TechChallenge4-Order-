package br.com.fiap.postech.orders.domain.entities;

import br.com.fiap.postech.orders.domain.enums.OrderStatus;
import br.com.fiap.postech.orders.domain.enums.PaymentMethod;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

public class Order {

    private UUID id;

    private OrderStatus status;

    private UUID customerId;

    private List<OrderItem> items = new ArrayList<>();

    private Address deliveryAddress;

    private double totalAmount;

    private PaymentMethod paymentMethod;

    private LocalDateTime estimatedDeliveryDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public void prePersist() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public Order() {
    }

    public Order(
            OrderStatus status,
            UUID customerId,
            List<OrderItem> items,
            Address deliveryAddress,
            PaymentMethod paymentMethod,
            LocalDateTime estimatedDeliveryDate,
            LocalDateTime createdAt
            ) {
        this.status = status;
        this.customerId = customerId;
        this.items = items;
        this.deliveryAddress = deliveryAddress;
        this.paymentMethod = paymentMethod;
        this.estimatedDeliveryDate = estimatedDeliveryDate;
        this.createdAt = createdAt;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public UUID getCustomerId() {
        return customerId;
    }

    public void setCustomerId(UUID customerId) {
        this.customerId = customerId;
    }

    public List<OrderItem> getItems() {
        return Collections.unmodifiableList(items);
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
    }

    public Address getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(Address deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public LocalDateTime getEstimatedDeliveryDate() {
        return estimatedDeliveryDate;
    }

    public void setEstimatedDeliveryDate(LocalDateTime estimatedDeliveryDate) {
        this.estimatedDeliveryDate = estimatedDeliveryDate;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void addItem(OrderItem item) {
        this.items.add(item);
        calculateTotalAmount(); // Recalcula o totalAmount
    }

    public void removeItem(OrderItem item) {
        if (!this.items.remove(item)) {
            throw new IllegalArgumentException("Item não encontrado no pedido.");
        }
        calculateTotalAmount();
    }

    public void clearItems() {
        if (status != OrderStatus.OPEN) {
            throw new IllegalStateException("Não é possível remover itens de um pedido processado.");
        }
        this.items.clear();
        this.totalAmount = 0.0;
    }

    private void calculateTotalAmount() {
        this.totalAmount = items.stream()
                .map(OrderItem::getTotalPrice) // Obtém o BigDecimal
                .filter(Objects::nonNull) // Evita NullPointerException
                .mapToDouble(BigDecimal::doubleValue) // Converte para double
                .sum();
    }

}