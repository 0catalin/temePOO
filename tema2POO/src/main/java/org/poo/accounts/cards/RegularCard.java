package org.poo.accounts.cards;

import org.poo.visitors.cardVisitors.CardVisitor;


/**
 * class designed to extend Card class, representing a regular card entity
 */
public final class RegularCard extends Card {

    public RegularCard() {
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
