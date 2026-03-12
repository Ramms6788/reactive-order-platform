package org.example.reactiveorderplatform.service;

import org.example.reactiveorderplatform.model.Order;

public interface OrderValidationService {

    void validate(Order order);
}
