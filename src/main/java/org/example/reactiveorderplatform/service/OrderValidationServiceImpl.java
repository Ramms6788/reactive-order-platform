package org.example.reactiveorderplatform.service;

import org.example.reactiveorderplatform.model.Order;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class OrderValidationServiceImpl implements OrderValidationService {

    @Override
    public void validate(Order order) {
        if (order == null) throw new IllegalArgumentException("Order cannot be null");

        if (order.getItems() == null || order.getItems().isEmpty())
            throw new IllegalArgumentException("Order must have at least one item");

        if (order.getTotalAmount().compareTo(BigDecimal.ZERO) <= 0)
            throw new IllegalArgumentException("Order total must be positive");
    }
}