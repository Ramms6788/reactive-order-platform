package org.example.reactiveorderplatform.exception;

public class PaymentDeclinedException extends RuntimeException {
    public PaymentDeclinedException(String cardDeclined) {
        super(cardDeclined);
    }
}
