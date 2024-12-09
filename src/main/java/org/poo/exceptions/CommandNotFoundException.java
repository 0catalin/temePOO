package org.poo.exceptions;

public class CommandNotFoundException extends RuntimeException {
    public CommandNotFoundException(final String message) {
        super(message);
    }
}
