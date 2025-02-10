package br.com.fiap.postech.orders.infrastructure.controllers;

import br.com.fiap.postech.orders.domain.entities.Order;
import br.com.fiap.postech.orders.usecases.CreateOrderUseCaseImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final CreateOrderUseCaseImpl createOrderUseCaseImpl;

    public OrderController(CreateOrderUseCaseImpl createOrderUseCaseImpl) {
        this.createOrderUseCaseImpl = createOrderUseCaseImpl;
    }

    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody Order order) {
        Order createdOrder = createOrderUseCaseImpl.createOrder(order);
        return new ResponseEntity<>(createdOrder, HttpStatus.CREATED);
    }
}
