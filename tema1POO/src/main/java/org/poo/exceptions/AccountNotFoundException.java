package org.poo.exceptions;

public class AccountNotFoundException extends RuntimeException {

    public AccountNotFoundException(final String message) {
        super(message);
    }
}
