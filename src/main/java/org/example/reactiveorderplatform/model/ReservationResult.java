package org.example.reactiveorderplatform.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
public class ReservationResult {
    private UUID              reservationId;
    private UUID              orderId;
    private ReservationStatus status;
    private List<UUID>        unavailableProducts;  // empty if all available
    private LocalDateTime     processedAt;
}