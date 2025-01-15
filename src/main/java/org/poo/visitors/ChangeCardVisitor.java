package org.poo.visitors;


import org.poo.accounts.cards.OneTimeCard;
import org.poo.accounts.cards.RegularCard;

public final class ChangeCardVisitor implements CardVisitor {

    public boolean visit(final OneTimeCard card) {
        card.updateCardNumber();
        return false;
    }

    public boolean visit(final RegularCard card) {
        return true;
    }
}
