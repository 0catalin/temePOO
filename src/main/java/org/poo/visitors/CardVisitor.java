package org.poo.visitors;

import org.poo.accounts.cards.OneTimeCard;
import org.poo.accounts.cards.RegularCard;

public interface CardVisitor {

    /**
     * visit method which visits a card
     * @param card the visited card
     * @return true or false depending on which Visitor it is
     */
    boolean visit(OneTimeCard card);




    /**
     * visit method which visits a card
     * @param card the visited card
     * @return true or false depending on which Visitor it is
     */
    boolean visit(RegularCard card);
}
