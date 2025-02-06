package br.com.fiap.postech.orders.domain.entities;

import br.com.fiap.postech.orders.domain.enums.OrderStatus;
import br.com.fiap.postech.orders.domain.enums.PaymentMethod;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID orderId;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @Column(nullable = false)
    private UUID clientId;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<OrderItem> items = new ArrayList<>();

    @Column(nullable = false)
    private String deliveryAddress;

    @Column(nullable = false)
    private double totalAmount;

    @Column(nullable = false)
    private PaymentMethod paymentMethod;

    private LocalDateTime estimatedDeliveryDate;
    private String trackingCode;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public Order() {
    }

    public Order(
            UUID orderId,
            OrderStatus status,
            UUID clientId,
            List<OrderItem> items,
            String deliveryAddress,
            PaymentMethod paymentMethod,
            LocalDateTime estimatedDeliveryDate,
            String trackingCode,
            LocalDateTime createdAt
            ) {
        this.orderId = orderId;
        this.status = status;
        this.clientId = clientId;
        this.items = items;
        this.deliveryAddress = deliveryAddress;
        this.paymentMethod = paymentMethod;
        this.estimatedDeliveryDate = estimatedDeliveryDate;
        this.trackingCode = trackingCode;
        this.createdAt = createdAt;
    }

    public UUID getOrderId() {
        return orderId;
    }

    public void setOrderId(UUID orderId) {
        this.orderId = orderId;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public UUID getClientId() {
        return clientId;
    }

    public void setClientId(UUID clientId) {
        this.clientId = clientId;
    }

    public List<OrderItem> getItems() {
        return Collections.unmodifiableList(items);
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
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

    public String getTrackingCode() {
        return trackingCode;
    }

    public void setTrackingCode(String trackingCode) {
        this.trackingCode = trackingCode;
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
        item.setOrder(this);
        this.items.add(item);
        calculateTotalAmount();
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
                .mapToDouble(OrderItem::getTotalPrice)
                .sum();
    }

    public void updateStatus(OrderStatus newStatus) {
        // Valide a transição de status aqui
        if (this.status == OrderStatus.DELIVERED && newStatus == OrderStatus.OPEN) {
            throw new IllegalStateException("Pedidos entregues não podem voltar para 'Em Aberto'.");
        }
        if (this.status != OrderStatus.DELIVERED && newStatus == OrderStatus.RETURNED) {
            throw new IllegalStateException("Apenas pedidos entregues podem ir para 'Devolução'.");
        }
        this.status = newStatus;
        this.updatedAt = LocalDateTime.now();
    }
}