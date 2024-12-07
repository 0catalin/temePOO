package org.poo.cards;

import org.poo.visitors.PayOnlineVisitor;
import org.poo.utils.Utils;
/**
 * class designed to extend Card class, representing a card with one time pay entity
 */
public final class OneTimeCard extends Card {
    public OneTimeCard() {
        setStatus("active");
        setCardNumber(Utils.generateCardNumber());
    }
    /**
     * accepts a visitor that does different operations on different types of cards
     * @param visitor a visitor which does a payment based on the type of card
     */
    public void accept(final PayOnlineVisitor visitor) {
        visitor.visit(this);
    }
}
