package org.example.reactiveorderplatform.service;

import org.example.reactiveorderplatform.model.Order;
import reactor.core.publisher.Mono;

public interface FraudNotificationService {

    Mono<Void> notify(Order order);
}
