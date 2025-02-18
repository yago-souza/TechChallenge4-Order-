package br.com.fiap.postech.orders.application.controller;

import br.com.fiap.postech.orders.application.usecases.*;
import br.com.fiap.postech.orders.domain.entities.Order;
import br.com.fiap.postech.orders.domain.entities.OrderItem;
import br.com.fiap.postech.orders.domain.enums.OrderStatus;
import br.com.fiap.postech.orders.interfaces.dto.AddItemToOrderRequestDTO;
import br.com.fiap.postech.orders.interfaces.dto.CreateOrderRequestDTO;
import br.com.fiap.postech.orders.interfaces.dto.OrderResponseDTO;
import br.com.fiap.postech.orders.interfaces.dto.UpdateOrderStatusRequestDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final CreateOrderUsecase createOrderUseCase;
    private final GetOrderUseCase getOrderUseCase;
    private final AddItemToOrderUseCase addItemToOrderUseCase;
    private final RemoveItemFromOrderUseCase removeItemFromOrderUseCase;
    private final UpdateOrderStatusUseCase updateOrderStatusUseCase;
    private final ListOrdersUseCase listOrdersUseCase;

    public OrderController(CreateOrderUsecase createOrderUseCase, GetOrderUseCase getOrderUseCase,
                           AddItemToOrderUseCase addItemToOrderUseCase, RemoveItemFromOrderUseCase removeItemFromOrderUseCase,
                           UpdateOrderStatusUseCase updateOrderStatusUseCase, ListOrdersUseCase listOrdersUseCase) {
        this.createOrderUseCase = createOrderUseCase;
        this.getOrderUseCase = getOrderUseCase;
        this.addItemToOrderUseCase = addItemToOrderUseCase;
        this.removeItemFromOrderUseCase = removeItemFromOrderUseCase;
        this.updateOrderStatusUseCase = updateOrderStatusUseCase;
        this.listOrdersUseCase = listOrdersUseCase;
    }

    @PostMapping
    public ResponseEntity<OrderResponseDTO> createOrder(@Valid @RequestBody CreateOrderRequestDTO request) {
        Order order = createOrderUseCase.execute(request.toDomain());
        return ResponseEntity.ok(OrderResponseDTO.fromDomain(order));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponseDTO> getById(@PathVariable UUID id) {
        Order order = getOrderUseCase.execute(id);
        return ResponseEntity.ok(OrderResponseDTO.fromDomain(order));
    }

    @PostMapping("/{orderId}/items")
    public ResponseEntity<OrderResponseDTO> addItemToOrder(@PathVariable UUID orderId,
                                                           @Valid @RequestBody AddItemToOrderRequestDTO request) {
        OrderItem newItem = request.toDomain();
        Order updatedOrder = addItemToOrderUseCase.execute(orderId, newItem);
        return ResponseEntity.ok(OrderResponseDTO.fromDomain(updatedOrder));
    }

    @DeleteMapping("/{orderId}/items/{productId}")
    public ResponseEntity<OrderResponseDTO> removeItemFromOrder(@PathVariable UUID orderId,
                                                                @PathVariable UUID productId) {
        Order updatedOrder = removeItemFromOrderUseCase.execute(orderId, productId);
        return ResponseEntity.ok(OrderResponseDTO.fromDomain(updatedOrder));
    }

    @PutMapping("/{orderId}/status")
    public ResponseEntity<OrderResponseDTO> updateOrderStatus(@PathVariable UUID orderId,
                                                              @RequestBody UpdateOrderStatusRequestDTO request) {
        Order updatedOrder = updateOrderStatusUseCase.execute(orderId, request.newStatus());
        return ResponseEntity.ok(OrderResponseDTO.fromDomain(updatedOrder));
    }

    @GetMapping
    public ResponseEntity<List<OrderResponseDTO>> listOrders(@RequestParam(required = false) UUID customerId,
                                                             @RequestParam(required = false) OrderStatus status) {
        List<Order> orders = listOrdersUseCase.execute(customerId, status);
        List<OrderResponseDTO> response = orders.stream()
                .map(OrderResponseDTO::fromDomain)
                .toList();
        return ResponseEntity.ok(response);
    }
}
