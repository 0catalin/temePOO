package org.poo.exceptions;

public class PaymentInfoNotFoundException extends RuntimeException {
    public PaymentInfoNotFoundException(String message) {
        super(message);
    }
}
