package br.com.fiap.postech.orders.infrastructure.messaging;

import br.com.fiap.postech.orders.application.usecases.UpdateOrderStatusUseCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.function.Consumer;

@Service
public class OrderDeliveredConsumer {


    private final UpdateOrderStatusUseCase updateOrderStatusUseCase;
    private static final Logger logger = LoggerFactory.getLogger(OrderDeliveredConsumer.class);

    public OrderDeliveredConsumer(UpdateOrderStatusUseCase updateOrderStatusUseCase) {
        this.updateOrderStatusUseCase = updateOrderStatusUseCase;
    }

    @Bean
    public Consumer<OrderDeliveredEvent> orderDelivered() {
        return event -> {
            logger.info("✅ Pedido entregue: {}", event);
            // Aqui você pode chamar um serviço para atualizar o status no banco de dados

            //descomentar após integração
            //updateOrderStatusUseCase.execute(event.getOrderId(), event.getOrderStatus());
        };
    }
}