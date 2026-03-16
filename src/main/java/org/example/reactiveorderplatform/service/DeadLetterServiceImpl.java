package org.example.reactiveorderplatform.service;

import org.example.reactiveorderplatform.model.FailedOrder;
import org.example.reactiveorderplatform.model.Order;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class DeadLetterServiceImpl implements DeadLetterService {

    private final List<FailedOrder> failedOrders = new CopyOnWriteArrayList<>();

    @Override
    public void save(Order order, Throwable ex) {
        failedOrders.add(FailedOrder.builder()
                .order(order)
                .ex(ex)
                .timestamp(LocalDateTime.now())
                .build());
    }

    @Override
    public List<FailedOrder> getAll() {
        return List.copyOf(failedOrders);
    }
}
