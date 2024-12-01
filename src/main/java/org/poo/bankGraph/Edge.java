package org.poo.bankGraph;
import lombok.Setter;
import lombok.Getter;

@Setter
@Getter

public class Edge {
    String to;
    double rate;

    public Edge(String to, double rate) {
        this.to = to;
        this.rate = rate;
    }
}
