package org.example.reactiveorderplatform.service;

import lombok.Builder;
import org.example.reactiveorderplatform.model.Order;
import org.example.reactiveorderplatform.model.OrderConfirmation;

import java.util.concurrent.CompletableFuture;

@Builder
public class OrderServiceImpl implements OrderService {

    private final OrderValidationService orderValidationService;
    private final PaymentService         paymentService;
    private final InventoryService       inventoryService;

    @Override
    public void processOrder(Order order) {
        orderValidationService.validate(order);

        final var paymentResult     = paymentService.charge(order);
        final var reservationResult = inventoryService.reserve(order);

        CompletableFuture.allOf(paymentResult, reservationResult)
                .thenAccept(v -> {
                    OrderConfirmation.success(order, paymentResult.join(), reservationResult.join());
                });
    }
}
