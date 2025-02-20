package br.com.fiap.postech.orders.application.controller;

import br.com.fiap.postech.orders.application.usecases.*;
import br.com.fiap.postech.orders.domain.entities.Address;
import br.com.fiap.postech.orders.domain.entities.Order;
import br.com.fiap.postech.orders.domain.entities.OrderItem;
import br.com.fiap.postech.orders.domain.enums.OrderStatus;
import br.com.fiap.postech.orders.domain.enums.PaymentMethod;
import br.com.fiap.postech.orders.infrastructure.messaging.KafkaProducerService;
import br.com.fiap.postech.orders.interfaces.dto.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class OrderControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper = new ObjectMapper();

    @InjectMocks
    private OrderController orderController;

    @Mock
    private CreateOrderUsecase createOrderUseCase;

    @Mock
    private GetOrderUseCase getOrderUseCase;

    @Mock
    private AddItemToOrderUseCase addItemToOrderUseCase;

    @Mock
    private RemoveItemFromOrderUseCase removeItemFromOrderUseCase;

    @Mock
    private UpdateOrderStatusUseCase updateOrderStatusUseCase;

    @Mock
    private ListOrdersUseCase listOrdersUseCase;

    @Mock
    private KafkaProducerService kafkaProducerService;


    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(orderController).build();
    }

    @Test
    void shouldCreateOrderSuccessfully() throws Exception {
        // Arrange
        UUID orderId = UUID.randomUUID();
        UUID customerId = UUID.randomUUID();
        UUID itemId = UUID.randomUUID();
        UUID productId = UUID.randomUUID();

        OrderItemRequestDTO orderItemRequestDTO = new OrderItemRequestDTO(
                itemId,
                productId,
                2,
                BigDecimal.valueOf(29.10)
        );

        CreateOrderRequestDTO requestDTO = new CreateOrderRequestDTO(
                customerId,
                List.of(orderItemRequestDTO),
                new Address("Rua teste",
                        "123",
                        "Teste",
                        "Bairro teste",
                        "Cidade Teste",
                        "Estado Teste",
                        "Pais Teste",
                        "12345-678"),
                PaymentMethod.CREDIT_CARD
        );

        Order mockOrder = requestDTO.toDomain(); // Usa o método para criar a entidade
        mockOrder.setId(orderId); // Define o ID da ordem manualmente para o teste

        when(createOrderUseCase.execute(Mockito.any())).thenReturn(mockOrder);

        // Act & Assert
        mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(orderId.toString()))
                .andExpect(jsonPath("$.status").value("OPEN"))
                .andExpect(jsonPath("$.customerId").value(customerId.toString()))
                .andExpect(jsonPath("$.items[0].productId").value(productId.toString()));
    }


    @Test
    void shouldReturnBadRequestWhenInvalidInput() throws Exception {
        // Arrange
        CreateOrderRequestDTO invalidRequest = new CreateOrderRequestDTO(
                null, // customerId inválido
                Collections.emptyList(), // items vazio
                null, // endereço vazio
                null // método de pagamento inválido
        );

        // Act & Assert
        mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testAddItemToOrder_ShouldReturnOrderResponseDTO() {
        // Arrange
        UUID orderId = UUID.randomUUID();
        Order order = new Order();
        OrderItem newItem = new OrderItem(UUID.randomUUID(), UUID.randomUUID(), 2, BigDecimal.valueOf(100.0), order);
        order.setStatus(OrderStatus.OPEN);
        order.addItem(newItem);
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

        AddItemToOrderRequestDTO request = new AddItemToOrderRequestDTO(UUID.randomUUID(), 2, BigDecimal.valueOf(100.0));
        when(addItemToOrderUseCase.execute(any(UUID.class), any(OrderItem.class))).thenReturn(order);

        // Act
        ResponseEntity<OrderResponseDTO> response = orderController.addItemToOrder(orderId, request);

        // Assert
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
    }

    @Test
    void testRemoveItemFromOrder_ShouldReturnOrderResponseDTO() {
        // Arrange
        UUID orderId = UUID.randomUUID();
        UUID productId = UUID.randomUUID();
        Order order = new Order();
        OrderItem newItem = new OrderItem(UUID.randomUUID(), UUID.randomUUID(), 2, BigDecimal.valueOf(100.0), order);
        order.setStatus(OrderStatus.OPEN);
        order.addItem(newItem);
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
        when(removeItemFromOrderUseCase.execute(orderId, productId)).thenReturn(order);

        // Act
        ResponseEntity<OrderResponseDTO> response = orderController.removeItemFromOrder(orderId, productId);

        // Assert
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        verify(removeItemFromOrderUseCase, times(1)).execute(orderId, productId);
    }

    @Test
    void testUpdateOrderStatus_ShouldReturnOrderResponseDTO() {
        // Arrange
        UUID orderId = UUID.randomUUID();
        Order order = new Order(
                OrderStatus.SHIPPED,
                orderId,
                List.of(),
                new Address("Rua teste",
                        "123",
                        "Teste",
                        "Bairro teste",
                        "Cidade Teste",
                        "Estado Teste",
                        "Pais Teste",
                        "12345-678"),
                null,
                null,
                null);
        when(updateOrderStatusUseCase.execute(orderId, OrderStatus.SHIPPED)).thenReturn(order);

        // Act
        ResponseEntity<OrderResponseDTO> response = orderController.updateOrderStatus(orderId, order.getStatus());

        // Assert
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        verify(updateOrderStatusUseCase, times(1)).execute(orderId, OrderStatus.SHIPPED);
    }

    @Test
    void testListOrders_ShouldReturnOrderList() {
        // Arrange
        UUID customerId = UUID.randomUUID();
        Order order = new Order(
                OrderStatus.OPEN,
                customerId,
                List.of(),
                new Address("Rua teste",
                        "123",
                        "Teste",
                        "Bairro teste",
                        "Cidade Teste",
                        "Estado Teste",
                        "Pais Teste",
                        "12345-678"),
                null,
                null,
                null);
        when(listOrdersUseCase.execute(customerId, OrderStatus.OPEN)).thenReturn(List.of(order));

        // Act
        ResponseEntity<List<OrderResponseDTO>> response = orderController.listOrders(customerId, OrderStatus.OPEN);

        // Assert
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isEmpty());
        verify(listOrdersUseCase, times(1)).execute(customerId, OrderStatus.OPEN);
    }

    @Test
    void testGetById_ShouldReturnOrderResponseDTO() throws Exception {
        // Arrange
        UUID orderId = UUID.fromString("71a374d1-3607-4c26-a4f8-158311d76c44");
        Order mockOrder = new Order(
                OrderStatus.OPEN,
                orderId,
                List.of(),
                new Address("Rua teste",
                        "123",
                        "Teste",
                        "Bairro teste",
                        "Cidade Teste",
                        "Estado Teste",
                        "Pais Teste",
                        "12345-678"),
                null,
                null,
                null);

        mockOrder.setId(orderId);

        when(getOrderUseCase.execute(orderId)).thenReturn(mockOrder);

        // Act & Assert
        mockMvc.perform(get("/orders/{id}", orderId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(orderId.toString()))
                .andExpect(jsonPath("$.status").value("OPEN"));
    }

}