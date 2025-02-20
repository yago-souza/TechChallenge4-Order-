package br.com.fiap.postech.orders.application.controller;

import br.com.fiap.postech.orders.application.usecases.*;
import br.com.fiap.postech.orders.domain.entities.Order;
import br.com.fiap.postech.orders.domain.entities.OrderItem;
import br.com.fiap.postech.orders.domain.enums.OrderStatus;
import br.com.fiap.postech.orders.infrastructure.messaging.KafkaProducerService;
import br.com.fiap.postech.orders.infrastructure.messaging.OrderCreatedEvent;
import br.com.fiap.postech.orders.infrastructure.messaging.OrderDeliveredEvent;
import br.com.fiap.postech.orders.interfaces.dto.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/orders")
public class OrderController {

    private final CreateOrderUsecase createOrderUseCase;
    private final GetOrderUseCase getOrderUseCase;
    private final AddItemToOrderUseCase addItemToOrderUseCase;
    private final RemoveItemFromOrderUseCase removeItemFromOrderUseCase;
    private final UpdateOrderStatusUseCase updateOrderStatusUseCase;
    private final ListOrdersUseCase listOrdersUseCase;
    private final KafkaProducerService kafkaProducerService;

    public OrderController(CreateOrderUsecase createOrderUseCase, GetOrderUseCase getOrderUseCase,
                           AddItemToOrderUseCase addItemToOrderUseCase, RemoveItemFromOrderUseCase removeItemFromOrderUseCase,
                           UpdateOrderStatusUseCase updateOrderStatusUseCase, ListOrdersUseCase listOrdersUseCase,
                           KafkaProducerService kafkaProducerService) {
        this.createOrderUseCase = createOrderUseCase;
        this.getOrderUseCase = getOrderUseCase;
        this.addItemToOrderUseCase = addItemToOrderUseCase;
        this.removeItemFromOrderUseCase = removeItemFromOrderUseCase;
        this.updateOrderStatusUseCase = updateOrderStatusUseCase;
        this.listOrdersUseCase = listOrdersUseCase;
        this.kafkaProducerService = kafkaProducerService;
    }

    @Operation(summary = "Cria um novo pedido")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pedido criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Requisição inválida")
    })
    @PostMapping
    public ResponseEntity<OrderResponseDTO> createOrder(@Valid @RequestBody CreateOrderRequestDTO request) {
        Order order = createOrderUseCase.execute(request.toDomain());
        kafkaProducerService.sendOrderCreatedEvent(new OrderCreatedEvent(order.getId(), order.getCustomerId(), order.getDeliveryAddress()));
        return ResponseEntity.ok(OrderResponseDTO.fromDomain(order));
    }

    @Operation(summary = "Obtém um pedido pelo ID")
    @GetMapping("/{id}")
    public ResponseEntity<OrderResponseDTO> getById(@PathVariable UUID id) {
        Order order = getOrderUseCase.execute(id);
        return ResponseEntity.ok(OrderResponseDTO.fromDomain(order));
    }

    @Operation(summary = "Adiciona um item ao pedido")
    @PostMapping("/{orderId}/items")
    public ResponseEntity<OrderResponseDTO> addItemToOrder(@PathVariable UUID orderId,
                                                           @Valid @RequestBody AddItemToOrderRequestDTO request) {
        OrderItem newItem = request.toDomain();
        Order updatedOrder = addItemToOrderUseCase.execute(orderId, newItem);
        return ResponseEntity.ok(OrderResponseDTO.fromDomain(updatedOrder));
    }

    @Operation(summary = "Remove um item do pedido")
    @DeleteMapping("/{orderId}/items/{productId}")
    public ResponseEntity<OrderResponseDTO> removeItemFromOrder(@PathVariable UUID orderId,
                                                                @PathVariable UUID productId) {
        Order updatedOrder = removeItemFromOrderUseCase.execute(orderId, productId);
        return ResponseEntity.ok(OrderResponseDTO.fromDomain(updatedOrder));
    }

    @Operation(summary = "Atualiza o status de um pedido")
    @PutMapping("/{orderId}/status")
    public ResponseEntity<OrderResponseDTO> updateOrderStatus(@PathVariable UUID orderId,
                                                              @RequestBody OrderStatus newStatus) {
        Order updatedOrder = updateOrderStatusUseCase.execute(orderId, newStatus);
        if (newStatus == OrderStatus.DELIVERED) {
            kafkaProducerService.sendOrderDeliveredEvent(new OrderDeliveredEvent(orderId, newStatus));
        }
        return ResponseEntity.ok(OrderResponseDTO.fromDomain(updatedOrder));
    }

    @Operation(summary = "Lista todos os pedidos")
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
