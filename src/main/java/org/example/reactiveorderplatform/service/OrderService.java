package org.example.reactiveorderplatform.service;

import org.example.reactiveorderplatform.model.Order;
import org.example.reactiveorderplatform.model.OrderConfirmation;
import reactor.core.publisher.Mono;

public interface OrderService {

    Mono<OrderConfirmation> processOrder(Order order);
}
