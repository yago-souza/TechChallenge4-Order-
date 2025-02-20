package br.com.fiap.postech.orders.application.controller;

import br.com.fiap.postech.orders.infrastructure.messaging.KafkaProducerService;
import br.com.fiap.postech.orders.infrastructure.messaging.OrderCreatedEvent;
import br.com.fiap.postech.orders.infrastructure.messaging.OrderDeliveredEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/pedidos")
public class PedidoController {

    private final KafkaProducerService kafkaProducerService;

    public PedidoController(KafkaProducerService kafkaProducerService) {
        this.kafkaProducerService = kafkaProducerService;
    }

    @PostMapping
    public ResponseEntity<String> createOrder(@RequestBody OrderCreatedEvent order) {
        kafkaProducerService.sendOrderCreatedEvent(order);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/delivered")
    public ResponseEntity<String> deliveredOrder(@RequestBody OrderDeliveredEvent order) {
        kafkaProducerService.sendOrderDeliveredEvent(order);
        return ResponseEntity.ok().build();
    }
}
