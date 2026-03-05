package org.example.reactiveorderplatform.service;

import org.example.reactiveorderplatform.exception.InsufficientStockException;
import org.example.reactiveorderplatform.model.Order;
import org.example.reactiveorderplatform.model.ReservationResult;
import org.example.reactiveorderplatform.model.ReservationStatus;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class InventoryServiceImpl implements InventoryService {

    @Override
    public Mono<ReservationResult> reserve(Order order) {
        return Mono.fromCallable(() -> {
            Utils.simulateLatency(300);

            if (Math.random() < 0.2)
                throw new InsufficientStockException("Product out of stock");

            return ReservationResult.builder()
                    .orderId(order.getId())
                    .reservationId(UUID.randomUUID())
                    .status(ReservationStatus.RESERVED)
                    .processedAt(LocalDateTime.now())
                    .build();
        });
    }
}
