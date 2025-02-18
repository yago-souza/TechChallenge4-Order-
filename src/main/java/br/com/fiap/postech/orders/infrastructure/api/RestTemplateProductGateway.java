package br.com.fiap.postech.orders.infrastructure.api;

import br.com.fiap.postech.orders.infrastructure.api.models.Product;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

@Component
public class RestTemplateProductGateway implements ProductGateway {

    private final RestTemplate restTemplate;
    private static final String PRODUCT_SERVICE_URL = "http://localhost:8082/products"; // URL do microsserviço de produtos

    public RestTemplateProductGateway(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public Product getProductById(UUID productId) {
        String url = PRODUCT_SERVICE_URL + "/" + productId;
        return restTemplate.getForObject(url, Product.class); // Faz uma requisição GET
    }

    @Override
    public boolean isInStock(UUID productId, int quantity) {
        String url = PRODUCT_SERVICE_URL + "/" + productId + "/stock?quantity=" + quantity;
        Boolean response = restTemplate.getForObject(url, Boolean.class);
        return Boolean.TRUE.equals(response); // Retorna false se for null
    }
}