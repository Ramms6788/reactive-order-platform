package org.example.reactiveorderplatform.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.reactiveorderplatform.exception.PaymentDeclinedException;
import org.example.reactiveorderplatform.exception.PaymentTimeoutException;
import org.example.reactiveorderplatform.model.Order;
import org.example.reactiveorderplatform.model.PaymentResult;
import org.example.reactiveorderplatform.model.PaymentStatus;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Primary
@Service("paymentService")
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentService secondaryPaymentService;

    @Override
    public Mono<PaymentResult> charge(Order order) {
        return Mono.fromCallable(() -> makePayment(order))
                .retryWhen(Retry.backoff(3, Duration.ofMillis(500))
                        .filter(e -> e instanceof PaymentTimeoutException)
                        .jitter(0.5)
                        .multiplier(1.5)
                        .onRetryExhaustedThrow((spec, signal) -> signal.failure())
                )
                .doOnError(e -> log.info("Payment error for order {}. Caused by: {}", order.getId(), e.getMessage()))
                .onErrorResume(PaymentTimeoutException.class, e -> secondaryPaymentService.charge(order))
                .onErrorMap(e -> new PaymentDeclinedException("Payment failed for order: " + order.getId(), e));
    }

    private PaymentResult makePayment(Order order) {
        Utils.simulateLatency(500);

        final double errorRate = Math.random();

        if (errorRate < 0.14)
            throw new PaymentTimeoutException("Payment provider timed out");

        if (errorRate < 0.3)
            throw new PaymentDeclinedException("Card declined");


        return PaymentResult.builder()
                .orderId(order.getId())
                .transactionId(UUID.randomUUID())//TODO this will be received from actual payment provider
                .status(PaymentStatus.SUCCESS)
                .processedAt(LocalDateTime.now())
                .build();
    }
}
