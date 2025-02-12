package br.com.fiap.postech.orders.usecases;

import br.com.fiap.postech.orders.domain.entities.Order;
import br.com.fiap.postech.orders.domain.entities.OrderItem;
import br.com.fiap.postech.orders.domain.enums.OrderStatus;
import br.com.fiap.postech.orders.domain.gateways.CustomerGateway;
import br.com.fiap.postech.orders.domain.gateways.ProductGateway;
import br.com.fiap.postech.orders.domain.models.Customer;
import br.com.fiap.postech.orders.domain.models.Product;
import br.com.fiap.postech.orders.infrastructure.exception.*;
import br.com.fiap.postech.orders.infrastructure.persistence.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class CreateOrderUsecaseTest {

    @InjectMocks
    private CreateOrderUsecase createOrderUsecase;

    @Mock
    private CustomerGateway customerGateway;

    @Mock
    private OrderRepository orderRepository;

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

        // Criação de um item de pedido real
        OrderItem item = new OrderItem(UUID.randomUUID(), productId, 2, 50.0, 100.0);

        // Criação do pedido real
        Order order = new Order(
                UUID.randomUUID(),
                OrderStatus.OPEN,
                customerId,
                List.of(item),
                "Rua Exemplo, 123",
                100.0,
                "Cartão de Crédito",
                LocalDateTime.now(),
                "ABC123",
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        // Simulação de um cliente válido retornado pelo ClientGateway
        Customer customer = new Customer(customerId, "Test Client", "test@test.com", "teste, 123, teste");
        when(customerGateway.getCustomerById(customerId)).thenReturn(customer);

        // Simulação de um produto válido retornado pelo ProductGateway
        Product product = new Product(productId, "Produto Teste", "Descrição", 10.0, 100);
        when(productGateway.getProductById(productId)).thenReturn(product);

        // Simulação de estoque suficiente para o produto
        when(productGateway.isInStock(productId, 2)).thenReturn(true);

        // Simulação do salvamento do pedido no repositório
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        // Execução do caso de uso e validação de que nenhuma exceção é lançada
        assertDoesNotThrow(() -> createOrderUsecase.execute(order));

        // Verifica se o pedido foi realmente salvo no repositório
        verify(orderRepository, times(1)).save(any(Order.class));
    }



    @Test
    public void testCreateOrder_ClientNotFound() {
        UUID clientId = UUID.randomUUID();
        UUID productId = UUID.randomUUID();

        Order order = new Order(
                UUID.randomUUID(),
                OrderStatus.OPEN,
                clientId,
                List.of(new OrderItem(UUID.randomUUID(), productId, 2, 50.0, 100.0)),
                "Rua Exemplo, 123",
                100.0,
                "Cartão de Crédito",
                LocalDateTime.now(),
                "ABC123",
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        // Simula a ausência do cliente no sistema
        when(customerGateway.getCustomerById(clientId)).thenReturn(null);

        // Executa e verifica que uma exceção é lançada
        assertThrows(CustomerNotFoundException.class, () -> createOrderUsecase.execute(order));

        // Garante que o pedido não foi salvo
        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    public void testCreateOrder_ProductNotFound() {
        UUID clientId = UUID.randomUUID();
        UUID productId = UUID.randomUUID();

        Order order = new Order(
                UUID.randomUUID(),
                OrderStatus.OPEN,
                clientId,
                List.of(new OrderItem(UUID.randomUUID(), productId, 2, 50.0, 100.0)),
                "Rua Exemplo, 123",
                100.0,
                "Cartão de Crédito",
                LocalDateTime.now(),
                "ABC123",
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        // Simulação de um cliente válido retornado pelo ClientGateway
        Customer customer = new Customer(clientId, "Test Client", "test@test.com", "teste, 123, teste");
        when(customerGateway.getCustomerById(clientId)).thenReturn(customer);

        // Simula a ausência do produto no sistema
        when(productGateway.getProductById(productId)).thenReturn(null);

        // Executa e verifica que uma exceção é lançada
        assertThrows(ProductNotFoundException.class, () -> createOrderUsecase.execute(order));

        // Garante que o pedido não foi salvo
        verify(orderRepository, never()).save(any(Order.class));

        // Garante que a verificação de estoque nunca foi chamada, já que o produto nem existe
        verify(productGateway, never()).isInStock(any(), any(Integer.class));
    }

    @Test
    public void testCreateOrder_InsufficientStock() {
        UUID clientId = UUID.randomUUID();
        UUID productId = UUID.randomUUID();

        // Simulação de um item do pedido com quantidade maior do que o estoque disponível
        OrderItem item = new OrderItem(UUID.randomUUID(), productId, 2, 50.0, 100.0);
        Order order = new Order(
                UUID.randomUUID(),
                OrderStatus.OPEN,
                clientId,
                List.of(item),
                "Rua Exemplo, 123",
                100.0,
                "Cartão de Crédito",
                LocalDateTime.now(),
                "ABC123",
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        // Simulação um cliente válido
        Customer customer = new Customer(clientId, "Test Client", "test@test.com", "teste, 123, teste");
        when(customerGateway.getCustomerById(clientId)).thenReturn(customer);

        // Simulação de um produto válido retornado pelo ProductGateway
        Product product = new Product(productId, "Produto Teste", "Descrição", 10.0, 10);
        when(productGateway.getProductById(productId)).thenReturn(product);

        // Simulação de estoque suficiente para o produto
        when(productGateway.isInStock(productId, 2)).thenReturn(false);

        // Executa e verifica que uma exceção é lançada
        assertThrows(InsufficientStockException.class, () -> createOrderUsecase.execute(order));

        // Garante que a consulta de estoque foi feita corretamente
        verify(productGateway).isInStock(productId, 2);

        // Garante que o pedido não foi salvo
        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    public void testCreateOrder_NoItems_ShouldThrowException() {
        UUID clientId = UUID.randomUUID();
        Order order = new Order(
                UUID.randomUUID(),
                OrderStatus.OPEN,
                clientId,
                List.of(), // Sem itens
                "Rua Exemplo, 123",
                100.0,
                "Cartão de Crédito",
                LocalDateTime.now(),
                "ABC123",
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        // Simulação um cliente válido
        Customer customer = new Customer(clientId, "Test Client", "test@test.com", "teste, 123, teste");
        when(customerGateway.getCustomerById(clientId)).thenReturn(customer);

        assertThrows(NoItemException.class, () -> createOrderUsecase.execute(order));
        // Garante que a busca por item foi chamada, já que o pedito não possui itens
        verify(productGateway, never()).getProductById(any());
        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    public void testCreateOrder_NoCustomer_ShouldThrowException() {
        UUID productId = UUID.randomUUID();
        OrderItem item = new OrderItem(UUID.randomUUID(), productId, 2, 50.0, 100.0);
        Order order = new Order(
                UUID.randomUUID(),
                OrderStatus.OPEN,
                null, // Sem cliente
                List.of(item),
                "Rua Exemplo, 123",
                100.0,
                "Cartão de Crédito",
                LocalDateTime.now(),
                "ABC123",
                LocalDateTime.now(),
                LocalDateTime.now()
        );


        assertThrows(NoCustomerException.class, () -> createOrderUsecase.execute(order));
        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    public void testCreateOrder_NegativeQuantity_ShouldThrowException() {
        UUID clientId = UUID.randomUUID();
        UUID productId = UUID.randomUUID();

        OrderItem item = new OrderItem(UUID.randomUUID(), productId, -2, 50.0, -100.0);
        Order order = new Order(
                UUID.randomUUID(),
                OrderStatus.OPEN,
                clientId,
                List.of(item),
                "Rua Exemplo, 123",
                -100.0, // Valor inválido
                "Cartão de Crédito",
                LocalDateTime.now(),
                "ABC123",
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        // Simulação de um cliente válido retornado pelo ClientGateway
        Customer customer = new Customer(clientId, "Test Client", "test@test.com", "teste, 123, teste");
        when(customerGateway.getCustomerById(clientId)).thenReturn(customer);

        // Simulação de um produto válido retornado pelo ProductGateway
        Product product = new Product(productId, "Produto Teste", "Descrição", 10.0, 100);
        when(productGateway.getProductById(productId)).thenReturn(product);

        // Simula estoque suficiente para o produto (não necessário para um item inválido)
        when(productGateway.isInStock(productId, -2)).thenReturn(false);

        assertThrows(InvalidQuantityException.class, () -> createOrderUsecase.execute(order));
        verify(orderRepository, never()).save(any(Order.class));
    }

}