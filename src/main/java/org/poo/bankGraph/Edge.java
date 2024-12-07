package org.poo.bankGraph;
import lombok.Setter;
import lombok.Getter;

@Setter
@Getter
/**
 * a currency graph edge representation
 */
public final class Edge {
    private String to;
    private double rate;

    public Edge(final String to, final double rate) {
        this.to = to;
        this.rate = rate;
    }
}
