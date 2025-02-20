package br.com.fiap.postech.orders.application.usecases;

import br.com.fiap.postech.orders.domain.entities.Address;
import br.com.fiap.postech.orders.domain.entities.Order;
import br.com.fiap.postech.orders.domain.entities.OrderItem;
import br.com.fiap.postech.orders.domain.enums.OrderStatus;
import br.com.fiap.postech.orders.domain.enums.PaymentMethod;
import br.com.fiap.postech.orders.infrastructure.api.CustomerGateway;
import br.com.fiap.postech.orders.infrastructure.api.ProductGateway;
import br.com.fiap.postech.orders.infrastructure.api.models.Customer;
import br.com.fiap.postech.orders.infrastructure.api.models.Product;
import br.com.fiap.postech.orders.infrastructure.exception.*;
import br.com.fiap.postech.orders.infrastructure.gateway.impl.OrderRepositoryGatewayImpl;
import br.com.fiap.postech.orders.infrastructure.mapper.OrderMapper;
import br.com.fiap.postech.orders.infrastructure.messaging.KafkaProducerService;
import br.com.fiap.postech.orders.infrastructure.messaging.OrderCreatedEvent;
import br.com.fiap.postech.orders.infrastructure.messaging.OrderEventPublisher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CreateOrderUsecaseTest {

    @InjectMocks
    private CreateOrderUsecase createOrderUsecase;

    @Mock
    private OrderEventPublisher eventPublisher;

    @Mock
    private OrderCreatedEvent orderCreatedEvent;

    @Mock
    private OrderEventPublisher orderEventPublisher;

    @Mock
    private OrderMapper orderMapper;

    @Mock
    private KafkaProducerService kafkaProducerService;

    @Mock
    private CustomerGateway customerGateway;

    @Mock
    private OrderRepositoryGatewayImpl orderRepositoryGateway;

    @Mock
    private ProductGateway productGateway;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldCreateOrderSuccessfully() {
        // Criação de IDs aleatórios para simular um cliente e um produto
        UUID customerId = UUID.randomUUID();
        UUID productId = UUID.randomUUID();


        // Criação do pedido real
        Order order = new Order();
        order.setStatus(OrderStatus.OPEN);
        order.setCustomerId(customerId);
        // Criação de um item de pedido real
        OrderItem item = new OrderItem(UUID.randomUUID(), productId, 2, BigDecimal.valueOf(50.0), order);
        order.addItem(item);
        order.setDeliveryAddress(
                new Address("Rua teste",
                        "123",
                        "Teste",
                        "Bairro teste",
                        "Cidade Teste",
                        "Estado Teste",
                        "Pais Teste",
                        "12345-678")
        );
        order.setPaymentMethod(PaymentMethod.CREDIT_CARD);
        order.setCreatedAt(LocalDateTime.now());
        order.setUpdatedAt(LocalDateTime.now());

        // Simulação de um cliente válido retornado pelo ClientGateway
        Customer customer = new Customer(
                customerId,
                "Test Client",
                "test@test.com",
                new Address("Rua teste",
                        "123",
                        "Teste",
                        "Bairro teste",
                        "Cidade Teste",
                        "Estado Teste",
                        "Pais Teste",
                        "12345-678")
                );
        when(customerGateway.getCustomerById(customerId)).thenReturn(customer);

        // Simulação de um produto válido retornado pelo ProductGateway
        Product product = new Product(productId, "Produto Teste", "Descrição", BigDecimal.valueOf(10.0), 100);
        when(productGateway.getProductById(productId)).thenReturn(product);

        // Simulação de estoque suficiente para o produto
        when(productGateway.isInStock(productId, 2)).thenReturn(true);

        // Simulação do salvamento do pedido no repositório
        when(orderRepositoryGateway.save(order)).thenReturn(order);

        // Execução do caso de uso e validação de que nenhuma exceção é lançada
        assertDoesNotThrow(() -> createOrderUsecase.execute(order));

        // Verifica se o pedido foi realmente salvo no repositório
        verify(orderRepositoryGateway, times(1)).save(order);
    }



    @Test
    void testCreateOrder_ClientNotFound_ShouldThrowException() {
        UUID customerId = UUID.randomUUID();
        UUID productId = UUID.randomUUID();

        // Criação do pedido real
        Order order = new Order();
        order.setStatus(OrderStatus.OPEN);
        order.setCustomerId(customerId);
        // Criação de um item de pedido real
        OrderItem item = new OrderItem(UUID.randomUUID(), productId, 2, BigDecimal.valueOf(50.0), order);
        order.addItem(item);
        order.setDeliveryAddress(
                new Address("Rua teste",
                        "123",
                        "Teste",
                        "Bairro teste",
                        "Cidade Teste",
                        "Estado Teste",
                        "Pais Teste",
                        "12345-678")
        );
        order.setPaymentMethod(PaymentMethod.CREDIT_CARD);
        order.setCreatedAt(LocalDateTime.now());
        order.setUpdatedAt(LocalDateTime.now());

        // Simula a ausência do cliente no sistema
        when(customerGateway.getCustomerById(customerId)).thenReturn(null);

        // Executa e verifica que uma exceção é lançada
        assertThrows(CustomerNotFoundException.class, () -> createOrderUsecase.execute(order));

        // Garante que o pedido não foi salvo
        verify(orderRepositoryGateway, never()).save(order);
    }

    @Test
    void testCreateOrder_ProductNotFound_ShouldThrowException() {
        UUID customerId = UUID.randomUUID();
        UUID productId = UUID.randomUUID();

        // Criação do pedido real
        Order order = new Order();
        order.setStatus(OrderStatus.OPEN);
        order.setCustomerId(customerId);
        // Criação de um item de pedido real
        OrderItem item = new OrderItem(UUID.randomUUID(), productId, 2, BigDecimal.valueOf(50.0), order);
        order.addItem(item);
        order.setDeliveryAddress(
                new Address("Rua teste",
                        "123",
                        "Teste",
                        "Bairro teste",
                        "Cidade Teste",
                        "Estado Teste",
                        "Pais Teste",
                        "12345-678")
        );
        order.setPaymentMethod(PaymentMethod.CREDIT_CARD);
        order.setCreatedAt(LocalDateTime.now());
        order.setUpdatedAt(LocalDateTime.now());

        // Simulação de um cliente válido retornado pelo ClientGateway
        Customer customer = new Customer(customerId, "Test Client", "test@test.com", new Address(
                "Rua teste",
                        "123",
                        "Teste",
                        "Bairro teste",
                        "Cidade Teste",
                        "Estado Teste",
                        "Pais Teste",
                        "12345-678")
        );
        when(customerGateway.getCustomerById(customerId)).thenReturn(customer);

        // Simula a ausência do produto no sistema
        when(productGateway.getProductById(productId)).thenReturn(null);

        // Executa e verifica que uma exceção é lançada
        assertThrows(ProductNotFoundException.class, () -> createOrderUsecase.execute(order));

        // Garante que o pedido não foi salvo
        verify(orderRepositoryGateway, never()).save(order);

        // Garante que a verificação de estoque nunca foi chamada, já que o produto nem existe
        verify(productGateway, never()).isInStock(any(), any(Integer.class));
    }

    @Test
    void testCreateOrder_InsufficientStock_ShouldThrowException() {
        UUID customerId = UUID.randomUUID();
        UUID productId = UUID.randomUUID();

        // Simulação de um item do pedido com quantidade maior do que o estoque disponível
        Order order = new Order();
        order.setStatus(OrderStatus.OPEN);
        order.setCustomerId(customerId);
        // Criação de um item de pedido real
        OrderItem item = new OrderItem(UUID.randomUUID(), productId, 2, BigDecimal.valueOf(50.0), order);
        order.addItem(item);
        order.setDeliveryAddress(
                new Address("Rua teste",
                        "123",
                        "Teste",
                        "Bairro teste",
                        "Cidade Teste",
                        "Estado Teste",
                        "Pais Teste",
                        "12345-678")
        );
        order.setPaymentMethod(PaymentMethod.CREDIT_CARD);
        order.setCreatedAt(LocalDateTime.now());
        order.setUpdatedAt(LocalDateTime.now());

        // Simulação um cliente válido
        Customer customer = new Customer(customerId, "Test Client", "test@test.com", new Address("Rua teste",
                        "123",
                        "Teste",
                        "Bairro teste",
                        "Cidade Teste",
                        "Estado Teste",
                        "Pais Teste",
                        "12345-678")
        );
        when(customerGateway.getCustomerById(customerId)).thenReturn(customer);

        // Simulação de um produto válido retornado pelo ProductGateway
        Product product = new Product(productId, "Produto Teste", "Descrição", BigDecimal.valueOf(10.0), 10);
        when(productGateway.getProductById(productId)).thenReturn(product);

        // Simulação de estoque suficiente para o produto
        when(productGateway.isInStock(productId, 2)).thenReturn(false);

        // Executa e verifica que uma exceção é lançada
        assertThrows(InsufficientStockException.class, () -> createOrderUsecase.execute(order));

        // Garante que a consulta de estoque foi feita corretamente
        verify(productGateway).isInStock(productId, 2);

        // Garante que o pedido não foi salvo
        verify(orderRepositoryGateway, never()).save(order);
    }

    @Test
    void testCreateOrder_NoItems_ShouldThrowException() {
        UUID customerId = UUID.randomUUID();
        Order order = new Order(
                OrderStatus.OPEN,
                customerId,
                List.of(), // Sem itens
                new Address("Rua teste",
                        "123",
                        "Teste",
                        "Bairro teste",
                        "Cidade Teste",
                        "Estado Teste",
                        "Pais Teste",
                        "12345-678"),
                PaymentMethod.CREDIT_CARD,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        // Simulação um cliente válido
        Customer customer = new Customer(customerId, "Test Client", "test@test.com", new Address("Rua teste",
                        "123",
                        "Teste",
                        "Bairro teste",
                        "Cidade Teste",
                        "Estado Teste",
                        "Pais Teste",
                        "12345-678")
        );
        when(customerGateway.getCustomerById(customerId)).thenReturn(customer);

        assertThrows(NoItemException.class, () -> createOrderUsecase.execute(order));
        // Garante que a busca por item foi chamada, já que o pedito não possui itens
        verify(productGateway, never()).getProductById(any());
        verify(orderRepositoryGateway, never()).save(order);
    }

    @Test
    void testCreateOrder_NoCustomer_ShouldThrowException() {
        UUID productId = UUID.randomUUID();

        // Criação do pedido real
        Order order = new Order();
        order.setStatus(OrderStatus.OPEN);
        // Criação de um item de pedido real
        OrderItem item = new OrderItem(UUID.randomUUID(), productId, 2, BigDecimal.valueOf(50.0), order);
        order.addItem(item);
        order.setDeliveryAddress(
                new Address("Rua teste",
                        "123",
                        "Teste",
                        "Bairro teste",
                        "Cidade Teste",
                        "Estado Teste",
                        "Pais Teste",
                        "12345-678")
        );
        order.setPaymentMethod(PaymentMethod.CREDIT_CARD);
        order.setCreatedAt(LocalDateTime.now());
        order.setUpdatedAt(LocalDateTime.now());

        assertThrows(NoCustomerException.class, () -> createOrderUsecase.execute(order));
        verify(orderRepositoryGateway, never()).save(order);
    }

    @Test
    void testCreateOrder_NegativeQuantity_ShouldThrowException() {
        UUID customerId = UUID.randomUUID();
        UUID productId = UUID.randomUUID();

        // Criação do pedido real
        Order order = new Order();
        order.setStatus(OrderStatus.OPEN);
        order.setCustomerId(customerId);
        // Criação de um item de pedido real
        OrderItem item = new OrderItem(UUID.randomUUID(), productId, -2, BigDecimal.valueOf(50.0), order);
        order.addItem(item);
        order.setDeliveryAddress(
                new Address("Rua teste",
                        "123",
                        "Teste",
                        "Bairro teste",
                        "Cidade Teste",
                        "Estado Teste",
                        "Pais Teste",
                        "12345-678")
        );
        order.setPaymentMethod(PaymentMethod.CREDIT_CARD);
        order.setCreatedAt(LocalDateTime.now());
        order.setUpdatedAt(LocalDateTime.now());

        // Simulação de um cliente válido retornado pelo ClientGateway
        Customer customer = new Customer(customerId, "Test Client", "test@test.com", new Address("Rua teste",
                        "123",
                        "Teste",
                        "Bairro teste",
                        "Cidade Teste",
                        "Estado Teste",
                        "Pais Teste",
                        "12345-678")
        );
        when(customerGateway.getCustomerById(customerId)).thenReturn(customer);

        // Simulação de um produto válido retornado pelo ProductGateway
        Product product = new Product(productId, "Produto Teste", "Descrição", BigDecimal.valueOf(10.0), 100);
        when(productGateway.getProductById(productId)).thenReturn(product);

        when(productGateway.isInStock(productId, -2)).thenReturn(false);

        assertThrows(InvalidQuantityException.class, () -> createOrderUsecase.execute(order));
        verify(orderRepositoryGateway, never()).save(order);
    }

    @Test
    void testCreateOrder_WhenAddressIsNull_ShouldThrowException() {
        UUID customerId = UUID.randomUUID();
        UUID productId = UUID.randomUUID();

        // Criação do pedido real
        Order order = new Order();
        order.setStatus(OrderStatus.OPEN);
        order.setCustomerId(customerId);
        // Criação de um item de pedido real
        OrderItem item = new OrderItem(UUID.randomUUID(), productId, 2, BigDecimal.valueOf(50.0), order);
        order.addItem(item);
        // Sem endereço
        order.setPaymentMethod(PaymentMethod.CREDIT_CARD);
        order.setCreatedAt(LocalDateTime.now());
        order.setUpdatedAt(LocalDateTime.now());

        // Simulação de um cliente válido retornado pelo ClientGateway
        Customer customer = new Customer(customerId, "Test Client", "test@test.com", new Address("Rua teste",
                        "123",
                        "Teste",
                        "Bairro teste",
                        "Cidade Teste",
                        "Estado Teste",
                        "Pais Teste",
                        "12345-678")
        );
        when(customerGateway.getCustomerById(customerId)).thenReturn(customer);

        assertThrows(InvalidAddressException.class, () -> createOrderUsecase.execute(order));
    }

    @Test
    void testCreateOrder_WhenAddressIsEmpty_ShouldThrowException() {
        UUID customerId = UUID.randomUUID();
        UUID productId = UUID.randomUUID();

        // Criação do pedido real
        Order order = new Order();
        order.setStatus(OrderStatus.OPEN);
        order.setCustomerId(customerId);
        // Criação de um item de pedido real
        OrderItem item = new OrderItem(UUID.randomUUID(), productId, 2, BigDecimal.valueOf(50.0), order);
        order.addItem(item);
        order.setDeliveryAddress(
                new Address("", "", "", "", "", "", "", "")
        );

        // Simulação de um cliente válido retornado pelo ClientGateway
        Customer customer = new Customer(
                customerId,
                "Test Client",
                "test@test.com",
                new Address(
                        "",
                        "",
                        "",
                        "",
                        "",
                        "",
                        "",
                        ""
                )
        );
        when(customerGateway.getCustomerById(customerId)).thenReturn(customer);

        // Simulação de um produto válido retornado pelo ProductGateway
        Product product = new Product(productId, "Produto Teste", "Descrição", BigDecimal.valueOf(10.0), 100);
        when(productGateway.getProductById(productId)).thenReturn(product);

        // Simulação de estoque suficiente para o produto
        when(productGateway.isInStock(productId, 2)).thenReturn(true);

        // Simulação do salvamento do pedido no repositório
        when(orderRepositoryGateway.save(order)).thenReturn(order);

        assertThrows(InvalidAddressException.class, () -> createOrderUsecase.execute(order));
    }

    @Test
    void shouldPublishEventWhenOrderIsCreated() {
        UUID customerId = UUID.randomUUID();
        UUID productId = UUID.randomUUID();

        // Criação do pedido real
        Order order = new Order();
        order.setStatus(OrderStatus.OPEN);
        order.setCustomerId(customerId);
        // Criação de um item de pedido real
        OrderItem item = new OrderItem(UUID.randomUUID(), productId, 2, BigDecimal.valueOf(50.0), order);
        order.addItem(item);
        order.setDeliveryAddress(
                new Address("Rua teste",
                        "123",
                        "Teste",
                        "Bairro teste",
                        "Cidade Teste",
                        "Estado Teste",
                        "Pais Teste",
                        "12345-678")
        );
        order.setPaymentMethod(PaymentMethod.CREDIT_CARD);
        order.setCreatedAt(LocalDateTime.now());
        order.setUpdatedAt(LocalDateTime.now());

        // Simulação de um cliente válido retornado pelo ClientGateway
        Customer customer = new Customer(customerId, "Test Client", "test@test.com", new Address("Rua teste",
                        "123",
                        "Teste",
                        "Bairro teste",
                        "Cidade Teste",
                        "Estado Teste",
                        "Pais Teste",
                        "12345-678")
        );
        when(customerGateway.getCustomerById(customerId)).thenReturn(customer);

        // Simulação de um produto válido retornado pelo ProductGateway
        Product product = new Product(productId, "Produto Teste", "Descrição", BigDecimal.valueOf(10.0), 100);
        when(productGateway.getProductById(productId)).thenReturn(product);

        // Simulação de estoque suficiente para o produto
        when(productGateway.isInStock(productId, 2)).thenReturn(true);

        when(orderRepositoryGateway.save(any())).thenReturn(order);

        createOrderUsecase.execute(order);

        verify(kafkaProducerService).sendOrderCreatedEvent(any(OrderCreatedEvent.class));
    }
}
