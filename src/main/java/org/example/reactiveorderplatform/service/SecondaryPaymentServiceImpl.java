package org.example.reactiveorderplatform.service;

import org.example.reactiveorderplatform.exception.PaymentDeclinedException;
import org.example.reactiveorderplatform.exception.PaymentTimeoutException;
import org.example.reactiveorderplatform.model.Order;
import org.example.reactiveorderplatform.model.PaymentResult;
import org.example.reactiveorderplatform.model.PaymentStatus;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.UUID;

@Service("secondaryPaymentService")
public class SecondaryPaymentServiceImpl implements PaymentService {

    @Override
    public Mono<PaymentResult> charge(Order order) {
        return Mono.fromCallable(() -> makePayment(order));
    }

    private PaymentResult makePayment(Order order) {
        Utils.simulateLatency(1000);

        final double errorRate = Math.random();

        if (errorRate < 0.01)
            throw new PaymentTimeoutException("Payment provider timed out");

        if (errorRate < 0.03)
            throw new PaymentDeclinedException("Card declined");


        return PaymentResult.builder()
                .orderId(order.getId())
                .transactionId(UUID.randomUUID())//TODO this will be received from actual payment provider
                .status(PaymentStatus.SUCCESS)
                .processedAt(LocalDateTime.now())
                .build();
    }
}
