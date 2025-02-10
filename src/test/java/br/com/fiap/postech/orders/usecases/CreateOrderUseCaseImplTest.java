package br.com.fiap.postech.orders.usecases;

import br.com.fiap.postech.orders.domain.entities.Order;
import br.com.fiap.postech.orders.domain.entities.OrderItem;
import br.com.fiap.postech.orders.domain.enums.OrderStatus;
import br.com.fiap.postech.orders.infrastructure.exception.CustomerNotFoundException;
import br.com.fiap.postech.orders.infrastructure.exception.InsufficientStockException;
import br.com.fiap.postech.orders.infrastructure.exception.ProductNotFoundException;
import br.com.fiap.postech.orders.infrastructure.http.RestTemplateCustomerGateway;
import br.com.fiap.postech.orders.infrastructure.http.RestTemplateProductGateway;
import br.com.fiap.postech.orders.infrastructure.persistence.repositories.OrderRepository;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class CreateOrderUseCaseImplTest {

    private MockWebServer mockWebServer;

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private RestTemplateCustomerGateway customerGateway;

    @InjectMocks
    private RestTemplateProductGateway productGateway;

    @InjectMocks
    private CreateOrderUseCaseImpl createOrderUseCaseImpl;

    @BeforeEach
    void setUp() throws IOException {
        MockitoAnnotations.openMocks(this);
        mockWebServer = new MockWebServer();
        mockWebServer.start();

        String baseUrl = mockWebServer.url("/").toString();
        customerGateway = new RestTemplateCustomerGateway(new RestTemplate());
        productGateway = new RestTemplateProductGateway(new RestTemplate());
        createOrderUseCaseImpl = new CreateOrderUseCaseImpl(orderRepository, customerGateway, productGateway);
    }

    @AfterEach
    void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    @Test
    public void execute_SuccessfullyCreatesOrder_WhenAllValidationsPass() throws Exception {
        Order order = new Order();
        order.setCustomerId(UUID.randomUUID());
        order.addItem(new OrderItem(UUID.randomUUID(), order, UUID.randomUUID(), 2, 50.0, 100.0, LocalDateTime.now(), LocalDateTime.now()));

        mockWebServer.enqueue(new MockResponse().setBody("{\"id\":\"" + order.getCustomerId() + "\",\"name\":\"John Doe\"}").setResponseCode(200));
        mockWebServer.enqueue(new MockResponse().setBody("{\"id\":\"" + order.getItems().get(0).getProductId() + "\",\"name\":\"Product A\"}").setResponseCode(200));
        mockWebServer.enqueue(new MockResponse().setBody("true").setResponseCode(200));

        when(orderRepository.save(order)).thenReturn(order);

        Order createdOrder = createOrderUseCaseImpl.createOrder(order);

        assertNotNull(createdOrder);
        assertEquals(OrderStatus.OPEN, createdOrder.getStatus());
        assertNotNull(createdOrder.getCreatedAt());
        assertNotNull(createdOrder.getUpdatedAt());
    }

    @Test
    public void execute_ThrowsCustomerNotFoundException_WhenCustomerDoesNotExist() throws Exception {
        Order order = new Order();
        order.setCustomerId(UUID.randomUUID());

        mockWebServer.enqueue(new MockResponse().setResponseCode(404));

        assertThrows(CustomerNotFoundException.class, () -> createOrderUseCaseImpl.createOrder(order));
    }

    @Test
    public void execute_ThrowsProductNotFoundException_WhenProductDoesNotExist() throws Exception {
        Order order = new Order();
        order.setCustomerId(UUID.randomUUID());
        order.addItem(new OrderItem(UUID.randomUUID(), order, UUID.randomUUID(), 2, 50.0, 100.0, LocalDateTime.now(), LocalDateTime.now()));

        mockWebServer.enqueue(new MockResponse().setBody("{\"id\":\"" + order.getCustomerId() + "\",\"name\":\"John Doe\"}").setResponseCode(200));
        mockWebServer.enqueue(new MockResponse().setResponseCode(404));

        assertThrows(ProductNotFoundException.class, () -> createOrderUseCaseImpl.createOrder(order));
    }

    @Test
    public void execute_ThrowsInsufficientStockException_WhenProductStockIsInsufficient() throws Exception {
        Order order = new Order();
        order.setCustomerId(UUID.randomUUID());
        order.addItem(new OrderItem(UUID.randomUUID(), order, UUID.randomUUID(), 2, 50.0, 100.0, LocalDateTime.now(), LocalDateTime.now()));

        mockWebServer.enqueue(new MockResponse().setBody("{\"id\":\"" + order.getCustomerId() + "\",\"name\":\"John Doe\"}").setResponseCode(200));
        mockWebServer.enqueue(new MockResponse().setBody("{\"id\":\"" + order.getItems().get(0).getProductId() + "\",\"name\":\"Product A\"}").setResponseCode(200));
        mockWebServer.enqueue(new MockResponse().setBody("false").setResponseCode(200));

        assertThrows(InsufficientStockException.class, () -> createOrderUseCaseImpl.createOrder(order));
    }
}