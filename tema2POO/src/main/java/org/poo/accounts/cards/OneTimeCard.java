package org.poo.accounts.cards;

import org.poo.visitors.cardVisitors.CardVisitor;


/**
 * class designed to extend Card class, representing a card with one time pay entity
 */
public final class OneTimeCard extends Card {

    public OneTimeCard() {
        super();
    }


    /**
     * accepts a visitor that does different operations on different types of cards
     * @param visitor a visitor which does a payment based on the type of card
     */
    public boolean accept(final CardVisitor visitor) {
        return visitor.visit(this);
    }
}
