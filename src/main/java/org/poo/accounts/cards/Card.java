package org.poo.accounts.cards;
import lombok.Getter;
import lombok.Setter;
import org.poo.visitors.CardVisitor;
import org.poo.utils.Utils;

@Getter
@Setter
/**
 * Class designed to be extended by the OneTimeCard and RegularCard classes
 * represents a card
 */
public abstract class Card {

    private String cardNumber;
    private String status;

    public Card() {
        status = "active";
        cardNumber = Utils.generateCardNumber();
    }


    /**
     * accepts a visitor that does different operations on different types of cards
     * @param visitor a visitor which does a payment based on the type of card
     */


    public abstract boolean accept(CardVisitor visitor);


    /**
     * updates card id by generating it with Utils class
     */
    public void updateCardNumber() {
        cardNumber = Utils.generateCardNumber();

    }
}
