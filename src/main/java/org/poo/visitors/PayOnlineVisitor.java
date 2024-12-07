package org.poo.visitors;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.accounts.Account;
import org.poo.bankGraph.Bank;
import org.poo.baseinput.User;
import org.poo.cards.Card;
import org.poo.cards.OneTimeCard;
import org.poo.cards.RegularCard;

/**
 * Visitor which simulates an online card payment based on the type of card
 */
public final class PayOnlineVisitor {
    private double amount;
    private int timestamp;
    private String commerciant;
    private Account account;
    private final String cardDestroyed = "The card has been destroyed";
    private final String cardCreated = "New card created";


    public PayOnlineVisitor(final double amount, final int timestamp,
                            final String commerciant, final Account account) {

        this.amount = amount;
        this.timestamp = timestamp;
        this.commerciant = commerciant;
        this.account = account;
    }

    /**
     * simulates an online payment with a one time card and takes care of all the error cases
     * @param card the OneTimeCard which is used for the payment
     */
    public void visit(final OneTimeCard card) {
        User user = Bank.getInstance().getUserByIBAN(account.getIban());
        if (account.getBalance() < amount) {
            user.getTranzactions().add(insufficientFunds());
            account.getReportsClassic().add(insufficientFunds());
        } else if (account.getBalance() - amount < account.getMinBalance()) {
            user.getTranzactions().add(insufficientFunds());
            account.getReportsClassic().add(insufficientFunds());
            card.setStatus("frozen");
        } else {
            account.setBalance(account.getBalance() - amount);

            user.getTranzactions().add(successfulPayment());
            account.getReportsClassic().add(successfulPayment());
            account.getSpendingReports().add(successfulPayment());

            user.getTranzactions().add(oneTimeCardCreatedOrDestroyed(card, cardDestroyed));
            account.getReportsClassic().add(oneTimeCardCreatedOrDestroyed(card, cardDestroyed));

            card.updateCardNumber();

            user.getTranzactions().add(oneTimeCardCreatedOrDestroyed(card, cardCreated));
            account.getReportsClassic().add(oneTimeCardCreatedOrDestroyed(card, cardCreated));
        }
    }

    /**
     * simulates an online payment with a regular card and takes care of all the error cases
     * @param card the regular card which is used for the payment
     */
    public void visit(final RegularCard card) {
        User user = Bank.getInstance().getUserByIBAN(account.getIban());
        if (account.getBalance() < amount) {
            user.getTranzactions().add(insufficientFunds());
            account.getReportsClassic().add(insufficientFunds());
        } else if (card.getStatus().equals("frozen")) {
            user.getTranzactions().add(blockedOrFrozenError("frozen"));
            account.getReportsClassic().add(blockedOrFrozenError("frozen"));
        } else if (account.getBalance() - amount < account.getMinBalance()) {
            user.getTranzactions().add(blockedOrFrozenError("frozen"));
            account.getReportsClassic().add(blockedOrFrozenError("frozen"));
            card.setStatus("frozen");
        } else {
            account.setBalance(account.getBalance() - amount);

            user.getTranzactions().add(successfulPayment());
            account.getReportsClassic().add(successfulPayment());
            account.getSpendingReports().add(successfulPayment());
        }
    }

    private ObjectNode blockedOrFrozenError(final String error) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode errorNode = mapper.createObjectNode();
        errorNode.put("timestamp", timestamp);
        errorNode.put("description", "The card is " + error);
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
        finalNode.put("amount", amount);
        finalNode.put("commerciant", commerciant);
        return finalNode;
    }
}
