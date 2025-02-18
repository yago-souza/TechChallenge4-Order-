package br.com.fiap.postech.orders.application.controller;

import br.com.fiap.postech.orders.application.usecases.CreateOrderUsecase;
import br.com.fiap.postech.orders.domain.entities.Order;
import br.com.fiap.postech.orders.interfaces.dto.CreateOrderRequestDTO;
import br.com.fiap.postech.orders.interfaces.dto.OrderResponseDTO;
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
    public ResponseEntity<OrderResponseDTO> createOrder(@RequestBody CreateOrderRequestDTO request) {
        Order order = createOrderUseCase.execute(request.toDomain());
        return ResponseEntity.ok(OrderResponseDTO.fromDomain(order));
    }
}
