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

@Service
public class PaymentServiceImpl implements PaymentService {

    /**
     * Reactor vs CompletableFuture comparison:
     * 1. Mono.zip() preserves types — no more allOf() + join() awkwardness
     * 2. Errors propagate as signals — no silent swallowing like allOf()
     * 3. Backpressure built in — flatMap's prefetch(256) controls flow automatically
     * 4. Lazy evaluation — nothing runs until subscribe, unlike CF which starts immediately
     * 5. log() operator gives full visibility into the reactive signals
     */
    @Override
    public Mono<PaymentResult> charge(Order order) {
        return Mono.fromCallable(() -> {
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
