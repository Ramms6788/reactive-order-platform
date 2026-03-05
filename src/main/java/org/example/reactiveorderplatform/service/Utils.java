package org.example.reactiveorderplatform.service;

import lombok.experimental.UtilityClass;
import org.example.reactiveorderplatform.model.Order;
import org.example.reactiveorderplatform.model.OrderItem;
import org.example.reactiveorderplatform.model.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;

@UtilityClass
public class Utils {

    public void simulateLatency(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }
    }

    public static List<Order> generateOrders(int n) {
        return IntStream.range(0, n)
                .mapToObj(i -> Order.builder()
                        .id(UUID.randomUUID())
                        .customerId(UUID.randomUUID())
                        .items(List.of(
                                OrderItem.builder().productId(UUID.randomUUID()).build()
                        ))
                        .totalAmount(BigDecimal.valueOf(Math.random() * 1000))
                        .status(OrderStatus.CREATED)
                        .createdAt(LocalDateTime.now())
                        .build())
                .toList();
    }
}
