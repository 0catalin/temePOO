package org.poo.bankPair;

import java.util.Objects;

/**
 * a pair class for finding the cost
 */
public final class Pair {

    private final String from;
    private final String to;


    public Pair(final String from, final String to) {
        this.from = from;
        this.to = to;
    }


    public String getFrom() {
        return from;
    }


    public String getTo() {
        return to;
    }


    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Pair pair = (Pair) o;
        return from.equals(pair.from) && to.equals(pair.to);
    }


    @Override
    public int hashCode() {
        return Objects.hash(from, to);
    }

}
