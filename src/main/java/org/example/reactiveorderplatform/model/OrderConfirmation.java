package org.example.reactiveorderplatform.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class OrderConfirmation {
    private UUID              orderId;
    private OrderStatus       status;
    private PaymentResult     paymentResult;
    private ReservationResult reservationResult;
    private String            message;
    private LocalDateTime     confirmedAt;

    public static OrderConfirmation success(Order order,
                                            PaymentResult payment,
                                            ReservationResult reservation) {
        return OrderConfirmation.builder()
                .orderId(order.getId())
                .status(OrderStatus.CONFIRMED)
                .paymentResult(payment)
                .reservationResult(reservation)
                .message("Order confirmed successfully")
                .confirmedAt(LocalDateTime.now())
                .build();
    }

    public static OrderConfirmation failed(Order order, Throwable ex) {
        return OrderConfirmation.builder()
                .orderId(order.getId())
                .status(OrderStatus.PAYMENT_FAILED)
                .message("Order failed: " + ex.getMessage())
                .confirmedAt(LocalDateTime.now())
                .build();
    }
}