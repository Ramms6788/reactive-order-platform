package org.example.reactiveorderplatform.service;

import org.example.reactiveorderplatform.model.Order;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class FraudNotificationServiceImpl implements FraudNotificationService {

    @Override
    public Mono<Void> notify(Order order) {
        return Mono.fromRunnable(() -> {
                    // Notify fraud detection system about the order
                    Utils.simulateLatency(40);
                    System.out.println("Send order id = " + order.getId() + " to fraud dealing system");
                })
                .then();
    }
}
