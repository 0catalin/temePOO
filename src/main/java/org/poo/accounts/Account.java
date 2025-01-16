package org.poo.accounts;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;
import org.poo.baseinput.Commerciant;
import org.poo.utils.Utils;
import org.poo.visitors.accountVisitors.Visitor;
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
    private double balance;
    private String type;
    private ArrayList<ObjectNode> spendingReports;
    private ArrayList<ObjectNode> reportsSavings;
    private ArrayList<ObjectNode> reportsClassic;
    private DiscountInfo discountInfo;
    private ArrayList<Card> cards;

    private static final int WARNING_LIMIT = 30;

    private static final double FOOD_CASHBACK = 0.02;
    private static final double CLOTHES_CASHBACK = 0.05;
    private static final double TECH_CASHBACK = 0.1;

    private static final double SPENDING_THRESHOLD1 = 100;
    private static final double SPENDING_THRESHOLD2 = 300;
    private static final double SPENDING_THRESHOLD3 = 500;

    private static final double CASHBACK_REGULAR1 = 0.001;
    private static final double CASHBACK_REGULAR2 = 0.002;
    private static final double CASHBACK_REGULAR3 = 0.0025;

    private static final double CASHBACK_SILVER1 = 0.003;
    private static final double CASHBACK_SILVER2 = 0.004;
    private static final double CASHBACK_SILVER3 = 0.005;

    private static final double CASHBACK_GOLD1 = 0.005;
    private static final double CASHBACK_GOLD2 = 0.0055;
    private static final double CASHBACK_GOLD3 = 0.007;



    public Account(final String currency) {
        setCurrency(currency);
        setBalance(0);
        setAlias("");
        setIban(Utils.generateIBAN());
        setSpendingReports(new ArrayList<ObjectNode>());
        setReportsSavings(new ArrayList<ObjectNode>());
        setReportsClassic(new ArrayList<ObjectNode>());
        setDiscountInfo(new DiscountInfo());
        setCards(new ArrayList<Card>());
    }



    /**
     * iterates through the account cards and returns the corresponding card
     * @param cardNumber the card id
     * @return the Card object corresponding to ID
     */
    public abstract Card getCardByCardNumber(String cardNumber);




    /**
     * depending on the commerciant and the user's service plan it returns the fraction
     * of the cashback that will be sent back to the user
     * @param commerciant the commerciant which the payment is made to
     * @param servicePlan the user's service plan
     * @return the fraction of the payment amount that will be sent back
     */
    public double getSpendingCashBack(final Commerciant commerciant, final String servicePlan) {
        // if the type is not spending Threshold there will not be cashback
        if (!commerciant.getCashbackStrategy().equals("spendingThreshold")) {
            return 0;
        }
        // depending on the sum spent so far and the user plan
        // we are getting cashback percentage
        if (discountInfo.getSpendingThreshold() >= SPENDING_THRESHOLD1
                && discountInfo.getSpendingThreshold() < SPENDING_THRESHOLD2) {
            if (servicePlan.equals("standard") || servicePlan.equals("student")) {
                return CASHBACK_REGULAR1;
            } else if (servicePlan.equals("silver")) {
                return CASHBACK_SILVER1;
            } else if (servicePlan.equals("gold")) {
                return CASHBACK_GOLD1;
            }

        } else if (discountInfo.getSpendingThreshold() >= SPENDING_THRESHOLD2
                && discountInfo.getSpendingThreshold() < SPENDING_THRESHOLD3) {
            if (servicePlan.equals("standard") || servicePlan.equals("student")) {
                return CASHBACK_REGULAR2;
            } else if (servicePlan.equals("silver")) {
                return CASHBACK_SILVER2;
            } else if (servicePlan.equals("gold")) {
                return CASHBACK_GOLD2;
            }

        } else if (discountInfo.getSpendingThreshold() >= SPENDING_THRESHOLD3) {
            if (servicePlan.equals("standard") || servicePlan.equals("student")) {
                return CASHBACK_REGULAR3;
            } else if (servicePlan.equals("silver")) {
                return CASHBACK_SILVER3;
            } else if (servicePlan.equals("gold")) {
                return CASHBACK_GOLD3;
            }
        }
        return 0;

    }


    /**
     * returns the cashback if the user has a coupon depending on the type of commerciant
     * @param commerciant the commerciant the payment is made to
     * @return the fraction of the payback to the user
     */
    public double getTransactionCashback(final Commerciant commerciant) {
        // if the cashback exists it gets nullified and the fraction is returned
        if (commerciant.getType().equals("Food") && discountInfo.isFoodCashback()) {
            discountInfo.setFoodCashback(false);
            return FOOD_CASHBACK;
        }
        if (commerciant.getType().equals("Clothes") && discountInfo.isClothesCashback()) {
            discountInfo.setClothesCashback(false);
            return CLOTHES_CASHBACK;
        }
        if (commerciant.getType().equals("Tech") && discountInfo.isTechCashback()) {
            discountInfo.setTechCashback(false);
            return TECH_CASHBACK;
        }
        return 0;
    }


    /**
     * checks if an account balance is 0
     * @return true if it is 0 false if it is not
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
