package org.example.reactiveorderplatform.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class PaymentResult {
    private UUID          transactionId;
    private UUID          orderId;
    private PaymentStatus status;
    private String        failureReason;  // null if successful
    private LocalDateTime processedAt;
}