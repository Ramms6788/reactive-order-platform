package org.example.reactiveorderplatform.model;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
public class Order {
    private UUID            id;
    private UUID            customerId;
    private List<OrderItem> items;
    private BigDecimal      totalAmount;
    private OrderStatus     status;
    private LocalDateTime   createdAt;
}