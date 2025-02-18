package br.com.fiap.postech.orders.infrastructure.api.models;

import br.com.fiap.postech.orders.domain.entities.Address;

import java.util.UUID;

public class Customer {
    private UUID id;
    private String name;
    private String email;
    private Address address;

    public Customer() {
    }

    public Customer(UUID id, String name, String email, Address address) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.address = address;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
}
