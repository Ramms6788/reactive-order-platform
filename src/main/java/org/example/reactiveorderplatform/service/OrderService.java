package org.example.reactiveorderplatform.service;

import org.example.reactiveorderplatform.model.Order;
import org.example.reactiveorderplatform.model.OrderConfirmation;

import java.util.concurrent.CompletableFuture;

public interface OrderService {

    CompletableFuture<OrderConfirmation> processOrder(Order order);
}
