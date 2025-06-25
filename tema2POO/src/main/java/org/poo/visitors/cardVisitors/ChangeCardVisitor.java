package org.poo.visitors.cardVisitors;


import org.poo.accounts.cards.OneTimeCard;
import org.poo.accounts.cards.RegularCard;


/**
 * Visitor which simulates updating a card only if it is oneTime
 */
public final class ChangeCardVisitor implements CardVisitor {


    /**
     * updates the card since it is oneTime
     * @param card the given card
     * @return false because the card has been changed
     */
    public boolean visit(final OneTimeCard card) {
        card.updateCardNumber();
        return false;
    }


    /**
     * doesn't update the card
     * @param card the given card
     * @return true because it didn't update the card
     */
    public boolean visit(final RegularCard card) {
        return true;
    }
}
