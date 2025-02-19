package br.com.fiap.postech.orders.infrastructure.messaging;

import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

@Component
public class OrderEventPublisher {

    private static final String ORDER_CREATED_BINDING = "orderCreated-out-0";

    private final StreamBridge streamBridge;

    public OrderEventPublisher(StreamBridge streamBridge) {
        this.streamBridge = streamBridge;
    }

    public void publishOrderCreatedEvent(OrderCreatedEvent event) {
        Message<OrderCreatedEvent> message = MessageBuilder
                .withPayload(event)
                .setHeader("eventType", "ORDER_CREATED") // Headers opcionais
                .build();

        streamBridge.send(ORDER_CREATED_BINDING, message);
    }
}