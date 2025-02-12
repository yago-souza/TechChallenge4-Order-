package br.com.fiap.postech.orders.infrastructure.persistence;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "TB_ORDER_ITEMS")
public class OrderItemEntity {
    private UUID id;
    private UUID productId;
    private int quantity;
    private double unitPrice;
    private double totalPrice;

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
        this.quantity = quantity;
        calculateTotalPrice(); // Recalcula o totalPrice quando o preço unitário é alterado
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
        calculateTotalPrice(); // Recalcula o totalPrice quando o preço unitário é alterado
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    private void calculateTotalPrice() {
        this.totalPrice = this.quantity * this.unitPrice;
    }
}
