package br.com.fiap.postech.orders.infrastructure.persistence;

import java.util.UUID;

public class OrderItem {
    private UUID id;
    private UUID productId;
    private int quantity;
    private double unitPrice;
    private double totalPrice;

    public OrderItem() {
    }

    public OrderItem(UUID id, UUID productId, int quantity, double unitPrice, double totalPrice) {
        this.id = id;
        this.productId = productId;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.totalPrice = totalPrice;
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
