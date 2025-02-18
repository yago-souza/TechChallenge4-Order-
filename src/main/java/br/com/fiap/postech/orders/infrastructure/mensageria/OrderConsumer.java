package br.com.fiap.postech.orders.infrastructure.mensageria;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

@Component
public class OrderConsumer {

    @Bean
    public Consumer<OrderEvent> orderCreate() {
        return event -> {
            System.out.println("📦 Order received: " + event);
            // Aqui você pode salvar no banco, chamar outro serviço, etc.
        };
    }
}
