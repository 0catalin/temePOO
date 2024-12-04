package org.poo.cards;
import lombok.Getter;
import lombok.Setter;
import org.poo.PayOnlineVisitor;

@Getter
@Setter

public abstract class Card {
    private String cardNumber;
    private String status;

    public abstract boolean accept(PayOnlineVisitor visitor);
}
