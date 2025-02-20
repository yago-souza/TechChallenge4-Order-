package br.com.fiap.postech.orders.infrastructure.messaging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducerService {
    private StreamBridge streamBridge;
    private static final Logger logger = LoggerFactory.getLogger(KafkaProducerService.class);

    public KafkaProducerService(StreamBridge streamBridge) {
        this.streamBridge = streamBridge;
    }

    public void sendOrderCreatedEvent (OrderCreatedEvent orderCreatedEvent) {
        logger.info("📤 Enviando evento OrderCreatedEvent: {}", orderCreatedEvent);
        streamBridge.send("orders.created", orderCreatedEvent);
    }

    public void sendOrderDeliveredEvent (OrderDeliveredEvent orderDeliveredEvent) {
        streamBridge.send("orders.delivered", orderDeliveredEvent);
    }
}
