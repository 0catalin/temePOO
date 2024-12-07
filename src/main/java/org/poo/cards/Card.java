package org.poo.cards;
import lombok.Getter;
import lombok.Setter;
import org.poo.visitors.PayOnlineVisitor;
import org.poo.utils.Utils;

@Getter
@Setter

public abstract class Card {
    private String cardNumber;
    private String status;

    public abstract void accept(PayOnlineVisitor visitor);

    /**
     * updates card id by generating it with Utils class
     */
    public void updateCardNumber() {
        cardNumber = Utils.generateCardNumber();
    }
}
