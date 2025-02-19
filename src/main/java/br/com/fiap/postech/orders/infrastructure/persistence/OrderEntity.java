package br.com.fiap.postech.orders.infrastructure.persistence;

import br.com.fiap.postech.orders.domain.entities.Address;
import br.com.fiap.postech.orders.domain.enums.OrderStatus;
import br.com.fiap.postech.orders.domain.enums.PaymentMethod;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "TB_ORDERS")
public class OrderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @Column(nullable = false)
    private UUID customerId;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JsonManagedReference
    private List<OrderItemEntity> items = new ArrayList<>();

    @Embedded
    private Address deliveryAddress;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal totalAmount = BigDecimal.ZERO;

    @Column(nullable = false)
    private PaymentMethod paymentMethod;

    private LocalDateTime estimatedDeliveryDate;
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

    public OrderEntity(
            UUID id,
            LocalDateTime updatedAt,
            LocalDateTime createdAt,
            LocalDateTime estimatedDeliveryDate,
            PaymentMethod paymentMethod,
            Address deliveryAddress,
            List<OrderItemEntity> items,
            UUID customerId,
            OrderStatus status
    ) {
        this.id = id;
        this.updatedAt = updatedAt;
        this.createdAt = createdAt;
        this.estimatedDeliveryDate = estimatedDeliveryDate;
        this.paymentMethod = paymentMethod;
        this.deliveryAddress = deliveryAddress;
        this.items = items;
        this.customerId = customerId;
        this.status = status;

        calculateTotalAmount();
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public void setCustomerId(UUID customerId) {
        this.customerId = customerId;
    }

    public void setItems(List<OrderItemEntity> items) {
        this.items = items;
    }

    public void setDeliveryAddress(Address deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public void setEstimatedDeliveryDate(LocalDateTime estimatedDeliveryDate) {
        this.estimatedDeliveryDate = estimatedDeliveryDate;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void addItem(OrderItemEntity item) {
        this.items.add(item);
        calculateTotalAmount(); // Recalcula o totalAmount
    }

    public void removeItem(OrderItemEntity item) {
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
        this.totalAmount = BigDecimal.ZERO;
    }

    private void calculateTotalAmount() {
        this.totalAmount = items.stream()
                .map(item -> item.getTotalPrice())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
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