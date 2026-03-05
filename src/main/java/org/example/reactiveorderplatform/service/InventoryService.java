package org.example.reactiveorderplatform.service;

import org.example.reactiveorderplatform.model.Order;
import org.example.reactiveorderplatform.model.ReservationResult;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletableFuture;

public interface InventoryService {

    Mono<ReservationResult> reserve(Order order);
}
