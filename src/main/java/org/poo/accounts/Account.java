package org.poo.accounts;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;
import org.poo.utils.Utils;
import org.poo.visitors.reportVisitors.Visitor;
import org.poo.accounts.cards.Card;

import java.util.ArrayList;

@Getter
@Setter
/**
 * Class designed to be extended by the Classic and Savings account classes
 * represents an account
 */
public abstract class Account {

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


    public Account(final String currency) {
        setCurrency(currency);
        setBalance(0);
        setCards(new ArrayList<Card>());
        setAlias("");
        setIban(Utils.generateIBAN());
        setSpendingReports(new ArrayList<ObjectNode>());
        setReportsSavings(new ArrayList<ObjectNode>());
        setReportsClassic(new ArrayList<ObjectNode>());
    }

    /**
     * iterates through the account cards and returns the corresponding card
     * @param cardNumber the card id
     * @return the Card object corresponding to ID
     */
    public Card getCardByCardNumber(final String cardNumber) {
        for (Card card : cards) {
            if (card.getCardNumber().equals(cardNumber)) {
                return card;
            }
        }
        return null;
    }

    /**
     * checks if an account balance is 0
     * @return tru if it is 0 false if it is not
     */
    public boolean isEmpty() {
        return balance == 0;
    }

    /**
     * checks if the balance is in warning range
     * @return true if it is and false if it is not
     */
    public boolean isInWarningRange() {
        return balance - minBalance <= WARNING_LIMIT;
    }

    /**
     * accepts visitors that do different operations on different types of accounts
     * @param visitor the interface of the possible visitor classes
     */
    public abstract void accept(Visitor visitor);

}
