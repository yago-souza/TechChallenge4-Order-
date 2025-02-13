package br.com.fiap.postech.orders.application.controllers;

import br.com.fiap.postech.orders.application.usecases.CreateOrderUsecase;
import br.com.fiap.postech.orders.domain.entities.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final CreateOrderUsecase createOrderUseCase;

    public OrderController(CreateOrderUsecase createOrderUseCase) {
        this.createOrderUseCase = createOrderUseCase;
    }

    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody Order order) {
        Order createdOrder = createOrderUseCase.execute(order);
        return ResponseEntity.ok(createdOrder);
    }

//    @GetMapping("/{id}")
//    public ResponseEntity<Order> getOrderById(@PathVariable UUID id) {
//        Order order = orderService.getOrderById(id);
//        return ResponseEntity.ok(order);
//    }
}
