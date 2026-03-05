package org.example.reactiveorderplatform;

import org.example.reactiveorderplatform.model.Order;
import org.example.reactiveorderplatform.model.OrderStatus;
import org.example.reactiveorderplatform.pipeline.OrderProcessor;
import org.example.reactiveorderplatform.pipeline.OrderPublisher;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;

@SpringBootApplication
public class ReactiveOrderPlatformApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReactiveOrderPlatformApplication.class, args);

        final OrderPublisher publisher = new OrderPublisher(generateOrders(50));

        // in this case we have `onComplete()` called only once, even though we have recursive calls of `subscription.request`
        publisher.subscribe(new OrderProcessor(10));

        System.out.println("\n\n New run \n\n");

        // this run goes into one big batch
        publisher.subscribe(new OrderProcessor(Long.MAX_VALUE));
    }

    public static List<Order> generateOrders(int n) {
        return IntStream.range(0, n)
                .mapToObj(i -> Order.builder()
                        .id(UUID.randomUUID())
                        .customerId(UUID.randomUUID())
                        .items(List.of())
                        .status(OrderStatus.CREATED)
                        .createdAt(LocalDateTime.now())
                        .build())
                .toList();
    }
}
