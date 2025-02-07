package br.com.fiap.postech.orders.config;

import br.com.fiap.postech.orders.infrastructure.exception.CustomerResponseErrorHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setErrorHandler(new CustomerResponseErrorHandler());
        return restTemplate;
    }
}
