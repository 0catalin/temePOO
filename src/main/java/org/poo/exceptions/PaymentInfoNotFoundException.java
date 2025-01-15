package org.poo.exceptions;

public class PaymentInfoNotFoundException extends RuntimeException {
    public PaymentInfoNotFoundException(final String message) {
        super(message);
    }
}
