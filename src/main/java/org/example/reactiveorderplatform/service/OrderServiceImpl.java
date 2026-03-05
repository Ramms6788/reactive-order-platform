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
        return Mono.fromRunnable(() -> orderValidationService.validate(order))
                .then(Mono.zip(
                        paymentService.charge(order),
                        inventoryService.reserve(order)
                ))
                .map(tuple -> OrderConfirmation.success(order, tuple.getT1(), tuple.getT2()));

    }
}
