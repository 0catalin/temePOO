package org.poo.accounts;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;
import org.poo.visitors.reportVisitors.Visitor;
import org.poo.cards.Card;

import java.util.ArrayList;

@Getter
@Setter

public abstract class Account {
    // poate clasa abstracta
    private String currency;
    private double minBalance = 0;
    private String alias;
    private String iban;
    private ArrayList<Card> cards;
    private double balance;
    private String type;
    private ArrayList<ObjectNode> spendingReports;
    private ArrayList<ObjectNode> reportsSavings;
    private ArrayList<ObjectNode> reportsClassic;

    private static final int WARNING_LIMIT = 30;


    public Card getCardByCardNumber(final String cardNumber) {
        for (Card card : cards) {
            if (card.getCardNumber().equals(cardNumber)) {
                return card;
            }
        }
        return null;
    }

    public boolean isEmpty() {
        return balance == 0;
    }
    public boolean isInWarningRange() {
        return balance - minBalance <= WARNING_LIMIT;
    }
    public abstract void accept(Visitor visitor);

}
