package br.com.fiap.postech.orders.usecases;

import br.com.fiap.postech.orders.domain.entities.Order;

import java.util.UUID;

public interface TrackOrderUseCase {
    Order trackOrder(UUID orderId);
}
