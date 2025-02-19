package br.com.fiap.postech.orders.infrastructure.messaging;

import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducerService {
    private StreamBridge streamBridge;

    public KafkaProducerService(StreamBridge streamBridge) {
        this.streamBridge = streamBridge;
    }

    public void sendOrderCreatedEvent (OrderCreatedEvent orderCreatedEvent) {
        streamBridge.send("orders.created", orderCreatedEvent);
    }

    public void sendOrderDeliveredEvent (OrderDeliveredEvent orderDeliveredEvent) {
        streamBridge.send("orders.delivered", orderDeliveredEvent);
    }
}
