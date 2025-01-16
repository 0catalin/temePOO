package org.poo.exceptions;

public class StrategyNotFoundException extends RuntimeException {

    public StrategyNotFoundException(final String message) {
        super(message);
    }
}
