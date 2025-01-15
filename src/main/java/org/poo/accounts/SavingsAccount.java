package org.poo.accounts;

import lombok.Getter;
import lombok.Setter;
import org.poo.accounts.cards.Card;
import org.poo.exceptions.CardNotFoundException;
import org.poo.visitors.reportVisitors.Visitor;




@Getter
@Setter
/**
 * class designed to extend Account class, representing a savings account entity
 */
public final class SavingsAccount extends Account {

    private double interestRate;

    public SavingsAccount(final String currency, final double interestRate) {
        super(currency);
        this.interestRate = interestRate;
        setType("savings");

    }

    public double getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(final double interestRate) {
        this.interestRate = interestRate;
    }

    /**
     * method of accepting the visitor
     * @param visitor the interface of the possible visitor classes
     */
    public void accept(final Visitor visitor) {
        visitor.visit(this);
    }


    /**
     * iterates through the account cards and returns the corresponding card
     * @param cardNumber the card id
     * @return the Card object corresponding to ID
     */
    public Card getCardByCardNumber(final String cardNumber) {
        for (Card card : getCards()) {
            if (card.getCardNumber().equals(cardNumber)) {
                return card;
            }
        }
        throw new CardNotFoundException("");
    }
}
