package org.poo.visitors;

import org.poo.accounts.cards.OneTimeCard;
import org.poo.accounts.cards.RegularCard;

public interface CardVisitor {
    boolean visit(OneTimeCard card);

    boolean visit(RegularCard card);
}
