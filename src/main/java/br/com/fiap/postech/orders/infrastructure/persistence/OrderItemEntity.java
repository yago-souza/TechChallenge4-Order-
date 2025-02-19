package br.com.fiap.postech.orders.infrastructure.persistence;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "TB_ORDER_ITEMS")
public class OrderItemEntity {
    @Id
    private UUID id;
    @Column(nullable = false)
    private UUID productId;
    @Column(nullable = false)
    private int quantity;
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal totalPrice;
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal unitPrice;
    @ManyToOne
    @JoinColumn(name = "order_id")
    @JsonBackReference
    private OrderEntity order;

    public OrderItemEntity(UUID id, UUID productId, int quantity, BigDecimal unitPrice, OrderEntity order) {
        this.id = id;
        this.productId = productId;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.order = order;
        calculateTotalPrice(); // Calcula o preço total ao criar o item
    }


    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getProductId() {
        return productId;
    }

    public void setProductId(UUID productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("Quantidade não pode ser negativa.");
        }
        this.quantity = quantity;
        calculateTotalPrice();
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        if (unitPrice == null || unitPrice.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("O preço unitário não pode ser nulo ou negativo.");
        }
        this.unitPrice = unitPrice.setScale(2, RoundingMode.HALF_UP);
        calculateTotalPrice();
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    private void calculateTotalPrice() {
        if (this.unitPrice != null && this.quantity >= 0) {
            this.totalPrice = this.unitPrice.multiply(BigDecimal.valueOf(this.quantity))
                    .setScale(2, RoundingMode.HALF_UP);
        } else {
            this.totalPrice = BigDecimal.ZERO;
        }
    }
}
