package org.example.reactiveorderplatform.service;

import org.example.reactiveorderplatform.model.Order;
import org.example.reactiveorderplatform.model.PaymentResult;
import org.example.reactiveorderplatform.model.PaymentStatus;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class PaymentServiceImpl implements PaymentService {

    @Override
    public CompletableFuture<PaymentResult> charge(Order order) {
        return CompletableFuture.supplyAsync(() -> {
            final var paymentResult = PaymentResult.builder()
                    .orderId(order.getId());

            try {
                Thread.sleep(500);

                paymentResult
                        .transactionId(UUID.randomUUID())//TODO this will be received from actual payment provider
                        .status(PaymentStatus.SUCCESS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();

                paymentResult
                        .status(PaymentStatus.TIMEOUT)
                        .failureReason(e.getMessage());
            } catch (Exception e) {
                paymentResult
                        .status(PaymentStatus.PROCESSOR_ERROR)
                        .failureReason(e.getMessage());
            }

            return paymentResult.processedAt(LocalDateTime.now()).build();
        });
    }
}
