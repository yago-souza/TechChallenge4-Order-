package br.com.fiap.postech.orders.infrastructure.mensageria;

import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Service;

@Service
public class OrderPublisher {
    private final StreamBridge streamBridge;

    public OrderPublisher(StreamBridge streamBridge) {
        this.streamBridge = streamBridge;
    }

    public void sendOrderCreatedEvent(OrderEvent event) {
        streamBridge.send("orderCreate-out-0", event);
    }
}