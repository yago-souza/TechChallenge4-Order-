package br.com.fiap.postech.orders.application.controllers;

import br.com.fiap.postech.orders.application.usecases.CreateOrderUsecase;
import br.com.fiap.postech.orders.domain.entities.Order;
import br.com.fiap.postech.orders.domain.entities.OrderItem;
import br.com.fiap.postech.orders.domain.enums.OrderStatus;
import br.com.fiap.postech.orders.domain.enums.PaymentMethod;
import br.com.fiap.postech.orders.interfaces.dto.CreateOrderRequestDTO;
import br.com.fiap.postech.orders.interfaces.dto.OrderItemRequestDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class OrderControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private CreateOrderUsecase createOrderUseCase;

    @InjectMocks
    private OrderController orderController;

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

        CreateOrderRequestDTO requestDTO = new CreateOrderRequestDTO(
                customerId,
                Collections.singletonList(

                        new OrderItemRequestDTO(
                                itemId,
                                productId,
                                2,
                                29.0
                        )
                ),
                "Rua das Flores, 123",
                PaymentMethod.CREDIT_CARD
        );

        Order mockOrder = new Order(
                orderId,
                OrderStatus.OPEN,
                customerId,
                Collections.singletonList(
                        new OrderItem(
                                itemId,
                                productId,
                                2,
                                29.0,
                                29.90
                        )
                ),
                "Rua das Flores, 123",
                59.80,
                PaymentMethod.CREDIT_CARD,
                null,
                null,
                null,
                null
        );

        when(createOrderUseCase.execute(any(Order.class))).thenReturn(mockOrder);

        // Act & Assert
        mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(orderId.toString()))
                .andExpect(jsonPath("$.status").value("OPEN"))
                .andExpect(jsonPath("$.customerId").value(customerId.toString()))
                .andExpect(jsonPath("$.items[0].productId").value(productId.toString()))
                .andExpect(jsonPath("$.totalAmount").value(59.80));
    }

    @Test
    void shouldReturnBadRequestWhenInvalidInput() throws Exception {
        // Arrange
        CreateOrderRequestDTO invalidRequest = new CreateOrderRequestDTO(
                null, // customerId inválido
                Collections.emptyList(), // items vazio
                "", // endereço vazio
                null // método de pagamento inválido
        );

        // Act & Assert
        mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }
}