package org.example.reactiveorderplatform.service;

import org.example.reactiveorderplatform.model.Order;
import org.example.reactiveorderplatform.model.PaymentResult;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletableFuture;

public interface PaymentService {

    Mono<PaymentResult> charge(Order order);
}
