package org.example.reactiveorderplatform.service;

import org.example.reactiveorderplatform.exception.FraudDetectedException;
import org.example.reactiveorderplatform.model.Order;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class FraudCheckServiceImpl implements FraudCheckService {

    @Override
    public Mono<Boolean> check(Order order) {
        return Mono.fromCallable(() -> {
                    Utils.simulateLatency(150);
                    return Math.random() < 0.15;//true means this order is fraudulent
                })
                .onErrorMap(e -> new FraudDetectedException("Fraud check failed for order: " + order.getId()));
    }
}
