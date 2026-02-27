package org.example.reactiveorderplatform.model;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
public class OrderItem {
    private UUID       productId;
    private String     productName;
    private int        quantity;
    private BigDecimal price;
}