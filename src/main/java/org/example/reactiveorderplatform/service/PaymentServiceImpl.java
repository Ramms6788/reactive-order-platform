package org.example.reactiveorderplatform.service;

import org.example.reactiveorderplatform.exception.PaymentDeclinedException;
import org.example.reactiveorderplatform.exception.PaymentTimeoutException;
import org.example.reactiveorderplatform.model.Order;
import org.example.reactiveorderplatform.model.PaymentResult;
import org.example.reactiveorderplatform.model.PaymentStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Override
    public CompletableFuture<PaymentResult> charge(Order order) {
        return CompletableFuture.supplyAsync(() -> {
                    Utils.simulateLatency(500);

                    if (Math.random() < 0.3)
                        throw new PaymentDeclinedException("Card declined");

                    if (Math.random() < 0.2)
                        throw new PaymentTimeoutException("Payment provider timed out");

                    return PaymentResult.builder()
                            .orderId(order.getId())
                            .transactionId(UUID.randomUUID())//TODO this will be received from actual payment provider
                            .status(PaymentStatus.SUCCESS)
                            .processedAt(LocalDateTime.now())
                            .build();
                }
        );
    }
}
