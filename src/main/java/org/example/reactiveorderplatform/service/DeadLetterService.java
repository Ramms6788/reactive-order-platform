package org.example.reactiveorderplatform.service;

import org.example.reactiveorderplatform.model.FailedOrder;
import org.example.reactiveorderplatform.model.Order;

import java.util.List;

public interface DeadLetterService {

    void save(Order order, Throwable ex);

    List<FailedOrder> getAll();

}
