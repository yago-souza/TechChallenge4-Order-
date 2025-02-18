package br.com.fiap.postech.orders.infrastructure.mensageria;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

@Component
public class OrderConsumer {

    private static final Logger logger = LoggerFactory.getLogger(OrderConsumer.class);

    @Bean
    public Consumer<OrderCreatedEvent> orderCreate() {
        return event -> logger.info("📦 Order received: {}", event);
    }
}
