package org.poo.visitors;

import org.poo.accounts.cards.Card;
import org.poo.accounts.cards.OneTimeCard;
import org.poo.accounts.cards.RegularCard;

public class ChangeCardVisitor implements CardVisitor {
    public boolean visit(OneTimeCard card) {
        card.updateCardNumber();
        return false;
    }

    public boolean visit(RegularCard card) {
        return true;
    }
}
