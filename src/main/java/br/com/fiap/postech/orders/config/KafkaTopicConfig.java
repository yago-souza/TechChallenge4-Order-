package br.com.fiap.postech.orders.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    @Bean
    public NewTopic ordersCreatedTopic() {
        return TopicBuilder.name("orders.created").build();
    }

    @Bean
    public NewTopic ordersCompletedTopic() {
        return TopicBuilder.name("orders.delivered").build();
    }
}