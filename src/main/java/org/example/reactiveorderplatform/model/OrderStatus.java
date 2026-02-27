package org.example.reactiveorderplatform.model;

public enum OrderStatus {
    CREATED,
    PAYMENT_PROCESSING,
    PAYMENT_FAILED,
    CONFIRMED,
    BACKORDERED,
    CANCELLED,
    SHIPPED
}
