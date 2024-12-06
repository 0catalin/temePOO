package org.poo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.accounts.Account;
import org.poo.accounts.ClassicAccount;
import org.poo.bankGraph.Bank;
import org.poo.baseinput.User;
import org.poo.cards.Card;
import org.poo.cards.OneTimeCard;
import org.poo.cards.RegularCard;
import org.poo.utils.Utils;

import java.util.ArrayList;

public class PayOnlineVisitor {
    private double amount;
    private int timestamp;
    private String commerciant;
    private Account account;
    private final static String CARD_DESTROYED = "The card has been destroyed";
    private final static String CARD_CREATED = "New card created";


    public PayOnlineVisitor(double amount, int timestamp,
                            String commerciant, Account account) {

        this.amount = amount;
        this.timestamp = timestamp;
        this.commerciant = commerciant;
        this.account = account;
    }

    public void visit(OneTimeCard card) {
        User user = Bank.getInstance().getUserByIBAN(account.getIBAN());
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

            user.getTranzactions().add(oneTimeCardCreatedOrDestroyed(card, CARD_DESTROYED));
            account.getReportsClassic().add(oneTimeCardCreatedOrDestroyed(card, CARD_DESTROYED));

            card.updateCardNumber();

            user.getTranzactions().add(oneTimeCardCreatedOrDestroyed(card, CARD_CREATED));
            account.getReportsClassic().add(oneTimeCardCreatedOrDestroyed(card, CARD_CREATED));
        }
    }

    public void visit(RegularCard card) {
        User user = Bank.getInstance().getUserByIBAN(account.getIBAN());
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

    public ObjectNode blockedOrFrozenError(String error) {
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

    private ObjectNode oneTimeCardCreatedOrDestroyed(Card card, String message) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode finalNode = mapper.createObjectNode();
        finalNode.put("account", account.getIBAN());
        finalNode.put("card", card.getCardNumber());
        finalNode.put("cardHolder", Bank.getInstance().getUserByIBAN(account.getIBAN()).getEmail());
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
