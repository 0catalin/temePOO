package org.poo.accounts;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;
import org.poo.DiscountInfo;
import org.poo.baseinput.Commerciant;
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
    private DiscountInfo discountInfo;
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
        setDiscountInfo(new DiscountInfo());
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

    public double getSpendingCashBack (Commerciant commerciant, String servicePlan) {
        if (!commerciant.getCashbackStrategy().equals("spendingThreshold")) {
            return 0;
        }
        if (discountInfo.getSpendingThreshold() >= 100 && discountInfo.getSpendingThreshold() < 300) {
            if (servicePlan.equals("standard") || servicePlan.equals("student")) {
                return 0.001;
            } else if (servicePlan.equals("silver")) {
                return 0.003;
            } else if (servicePlan.equals("gold")) {
                return 0.005;
            }
        } else if (discountInfo.getSpendingThreshold() >= 300 && discountInfo.getSpendingThreshold() < 500) {
            if (servicePlan.equals("standard") || servicePlan.equals("student")) {
                return 0.002;
            } else if (servicePlan.equals("silver")) {
                return 0.004;
            } else if (servicePlan.equals("gold")) {
                return 0.0055;
            }
        } else if (discountInfo.getSpendingThreshold() >= 500) {
            if (servicePlan.equals("standard") || servicePlan.equals("student")) {
                return 0.0025;
            } else if (servicePlan.equals("silver")) {
                return 0.005;
            } else if (servicePlan.equals("gold")) {
                return 0.007;
            }
        }
        return 0;

    }
    public double getTransactionCashback (Commerciant commerciant) {
        if (commerciant.getType().equals("Food") && discountInfo.isFoodCashback()) {
            discountInfo.setFoodCashback(false);
            return 0.02;
        }
        if (commerciant.getType().equals("Clothes") && discountInfo.isClothesCashback()) {
            discountInfo.setClothesCashback(false);
            return 0.05;
        }
        if (commerciant.getType().equals("Tech") && discountInfo.isTechCashback()) {
            discountInfo.setTechCashback(false);
            return 0.1;
        }
        return 0;
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
