package org.example.reactiveorderplatform.service;

import lombok.RequiredArgsConstructor;
import org.example.reactiveorderplatform.model.Order;
import org.example.reactiveorderplatform.model.OrderConfirmation;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderValidationService orderValidationService;
    private final PaymentService         paymentService;
    private final InventoryService       inventoryService;

    @Override
    public CompletableFuture<OrderConfirmation> processOrder(Order order) {
        orderValidationService.validate(order);

        final var paymentResult     = paymentService.charge(order);
        final var reservationResult = inventoryService.reserve(order);

        return CompletableFuture.allOf(paymentResult, reservationResult)
                .thenApply(_ -> OrderConfirmation.success(order, paymentResult.join(), reservationResult.join()))
                .exceptionally(ex -> OrderConfirmation.failed(order, ex.getCause()));
    }
}
