package br.com.fiap.postech.orders.infrastructure.messaging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.function.Consumer;

@Service
public class OrderConsumer {

    private static final Logger logger = LoggerFactory.getLogger(OrderConsumer.class);

    @Bean
    public Consumer<OrderCreatedEvent> orderCreate() {
        return event -> logger.info("📦 Order created: {}", event);
    }
}
