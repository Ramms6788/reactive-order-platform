package org.example.reactiveorderplatform.service;

import lombok.RequiredArgsConstructor;
import org.example.reactiveorderplatform.model.Order;
import org.example.reactiveorderplatform.model.OrderConfirmation;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderValidationService orderValidationService;
    private final PaymentService         paymentService;
    private final InventoryService       inventoryService;

    @Override
    public Mono<OrderConfirmation> processOrder(Order order) {
        orderValidationService.validate(order);

        final var paymentResultMono    = paymentService.charge(order);
        final var reservationResult    = inventoryService.reserve(order);

        return Mono.zip(
                        paymentResultMono,
                        reservationResult
                )
//                .log()
                .map(tuple -> OrderConfirmation.success(order, tuple.getT1(), tuple.getT2()));

    }
}
