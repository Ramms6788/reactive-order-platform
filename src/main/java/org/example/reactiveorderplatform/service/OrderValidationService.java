package org.example.reactiveorderplatform.service;

import org.example.reactiveorderplatform.model.Order;
import org.springframework.stereotype.Service;

public interface OrderValidationService {

    void validate(Order order);
}
