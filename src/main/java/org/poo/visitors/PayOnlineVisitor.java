package org.poo.visitors;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.accounts.Account;
import org.poo.bankPair.Bank;
import org.poo.baseinput.User;
import org.poo.accounts.cards.Card;
import org.poo.accounts.cards.OneTimeCard;
import org.poo.accounts.cards.RegularCard;

/**
 * Visitor which simulates an online card payment based on the type of card
 */
public final class PayOnlineVisitor {

    private final double amount;
    private final int timestamp;
    private final String commerciant;
    private final Account account;
    private final double prevAmount;



    public PayOnlineVisitor(final double amount, final int timestamp,
                            final String commerciant, final Account account, double prevAmount) {

        this.amount = amount;
        this.timestamp = timestamp;
        this.commerciant = commerciant;
        this.account = account;
        this.prevAmount = prevAmount;
    }


    /**
     * simulates an online payment with a one time card and takes care of all the error cases
     * @param card the OneTimeCard which is used for the payment
     */
    public void visit(final OneTimeCard card) {
        User user = Bank.getInstance().getUserByIBAN(account.getIban());
        if (account.getBalance() - amount < account.getMinBalance()) {
            user.getTranzactions().add(insufficientFunds());
            account.getReportsClassic().add(insufficientFunds());
            card.setStatus("frozen");
        } else {
            account.setBalance(account.getBalance() - amount);

            user.getTranzactions().add(successfulPayment());
            account.getReportsClassic().add(successfulPayment());
            account.getSpendingReports().add(successfulPayment());

            user.getTranzactions().add(oneTimeCardCreatedOrDestroyed(card,
                    "The card has been destroyed"));
            account.getReportsClassic().add(oneTimeCardCreatedOrDestroyed(card,
                    "The card has been destroyed"));

            card.updateCardNumber();

            user.getTranzactions().add(oneTimeCardCreatedOrDestroyed(card, "New card created"));
            account.getReportsClassic().add(oneTimeCardCreatedOrDestroyed(card,
                    "New card created"));
        }
    }



    /**
     * simulates an online payment with a regular card and takes care of all the error cases
     * @param card the regular card which is used for the payment
     */
    public void visit(final RegularCard card) {
        User user = Bank.getInstance().getUserByIBAN(account.getIban());
        if (card.getStatus().equals("frozen")) {
            user.getTranzactions().add(frozenError());
            account.getReportsClassic().add(frozenError());
        } else if (account.getBalance() - amount < account.getMinBalance()) {
            user.getTranzactions().add(frozenError());
            account.getReportsClassic().add(frozenError());
            card.setStatus("frozen");
        } else {
            account.setBalance(account.getBalance() - amount);

            user.getTranzactions().add(successfulPayment());
            account.getReportsClassic().add(successfulPayment());
            account.getSpendingReports().add(successfulPayment());
        }
    }



    private ObjectNode frozenError() {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode errorNode = mapper.createObjectNode();
        errorNode.put("timestamp", timestamp);
        errorNode.put("description", "The card is frozen");
        return errorNode;
    }



    private ObjectNode insufficientFunds() {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode finalNode = mapper.createObjectNode();
        finalNode.put("timestamp", timestamp);
        finalNode.put("description", "Insufficient funds");
        return finalNode;
    }



    private ObjectNode oneTimeCardCreatedOrDestroyed(final Card card, final String message) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode finalNode = mapper.createObjectNode();
        finalNode.put("account", account.getIban());
        finalNode.put("card", card.getCardNumber());
        finalNode.put("cardHolder", Bank.getInstance().getUserByIBAN(account.getIban()).getEmail());
        finalNode.put("description", message);
        finalNode.put("timestamp", timestamp);
        return finalNode;
    }



    private ObjectNode successfulPayment() {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode finalNode = mapper.createObjectNode();
        finalNode.put("timestamp", timestamp);
        finalNode.put("description", "Card payment");
        finalNode.put("amount", prevAmount);
        finalNode.put("commerciant", commerciant);
        return finalNode;
    }
}
