package org.example.reactiveorderplatform.service;

import org.example.reactiveorderplatform.model.Order;

public class OrderValidationServiceImpl implements OrderValidationService {

    @Override
    public void validate(Order order) {
        if (order == null) {
            throw new IllegalArgumentException("Order cannot be null");
        }
    }
}