package br.com.fiap.postech.orders.infrastructure.mensageria;

import br.com.fiap.postech.orders.domain.entities.Address;

import java.util.UUID;

public class OrderEvent {
    private UUID orderId;
    private UUID customerId;
    private Address address;
    //updateTime

    public OrderEvent() {
    }

    public OrderEvent(UUID orderId, UUID customerId, Address address) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.address = address;
    }

    public UUID getOrderId() {
        return orderId;
    }

    public void setOrderId(UUID orderId) {
        this.orderId = orderId;
    }

    public UUID getCustomerId() {
        return customerId;
    }

    public void setCustomerId(UUID customerId) {
        this.customerId = customerId;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
}
