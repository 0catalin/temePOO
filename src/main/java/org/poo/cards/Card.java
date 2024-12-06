package org.poo.cards;
import lombok.Getter;
import lombok.Setter;
import org.poo.PayOnlineVisitor;
import org.poo.utils.Utils;

@Getter
@Setter

public abstract class Card {
    private String cardNumber;
    private String status;

    public abstract void accept(PayOnlineVisitor visitor);

    public void updateCardNumber() {
        cardNumber = Utils.generateCardNumber();
    }
}
