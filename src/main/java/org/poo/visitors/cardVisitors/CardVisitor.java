package org.poo.visitors.cardVisitors;

import org.poo.accounts.cards.OneTimeCard;
import org.poo.accounts.cards.RegularCard;



/**
 * interface used for the Visitors who do operations on cards
 */
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
