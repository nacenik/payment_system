package net.oleksin.paymentsystem.exception;

public class PaymentNotFoundException extends RuntimeException {
    public PaymentNotFoundException() {
    }

    public PaymentNotFoundException(String message) {
        super(message);
    }
}
