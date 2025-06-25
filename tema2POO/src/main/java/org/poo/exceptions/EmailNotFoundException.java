package org.poo.exceptions;

public class EmailNotFoundException extends RuntimeException {

    public EmailNotFoundException(final String message) {
        super(message);
    }
}
