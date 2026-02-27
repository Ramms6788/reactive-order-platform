package org.example.reactiveorderplatform.service;

import org.example.reactiveorderplatform.model.Order;
import org.example.reactiveorderplatform.model.ReservationResult;

import java.util.concurrent.CompletableFuture;

public interface InventoryService {

    CompletableFuture<ReservationResult> reserve(Order order);
}
