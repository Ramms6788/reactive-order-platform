package org.example.reactiveorderplatform.service;

import org.example.reactiveorderplatform.model.Order;
import org.example.reactiveorderplatform.model.PaymentResult;

import java.util.concurrent.CompletableFuture;

public interface PaymentService {

    CompletableFuture<PaymentResult> charge(Order order);
}
