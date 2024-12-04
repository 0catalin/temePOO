package org.poo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.accounts.Account;
import org.poo.accounts.ClassicAccount;
import org.poo.bankGraph.Bank;
import org.poo.cards.OneTimeCard;
import org.poo.cards.RegularCard;

public class PayOnlineVisitor {
    private double amount;
    private int timestamp;
    private String description;
    private String commerciant;
    private ObjectMapper mapper;
    private ArrayNode output;
    private Account account;
    private Bank bank;

    public PayOnlineVisitor(double amount, int timestamp, String description,
                            String commerciant, ObjectMapper mapper, ArrayNode output, Account account, Bank bank) {

        this.amount = amount;
        this.timestamp = timestamp;
        this.description = description;
        this.commerciant = commerciant;
        this.mapper = mapper;
        this.output = output;
        this.account = account;
        this.bank = bank;
    }

    public boolean visit(OneTimeCard card) {
        if (account.getBalance() < amount) {
            bank.getUserByIBAN(account.getIBAN()).getTranzactions().add(insufficientFunds());
            return false;
        } else if (card.getStatus().equals("blocked")) {
            bank.getUserByIBAN(account.getIBAN()).getTranzactions().add(blockedOrFrozenError("blocked"));
            return false;
        } else if (card.getStatus().equals("frozen")) {
            bank.getUserByIBAN(account.getIBAN()).getTranzactions().add(blockedOrFrozenError("frozen"));
            return false;
        } else if (account.getBalance() - amount < account.getMinBalance()) {
            bank.getUserByIBAN(account.getIBAN()).getTranzactions().add(insufficientFunds());
            card.setStatus("frozen");
            return false;
        } else {
            account.setBalance(account.getBalance() - amount);
            card.setStatus("blocked");
            return true;
        }
    }

    public boolean visit(RegularCard card) {
        if (account.getBalance() < amount) {
            bank.getUserByIBAN(account.getIBAN()).getTranzactions().add(insufficientFunds());
            return false;
        } else if (card.getStatus().equals("frozen")) {
            bank.getUserByIBAN(account.getIBAN()).getTranzactions().add(blockedOrFrozenError("frozen"));
            return false;
        } else if (account.getBalance() - amount < account.getMinBalance()) {
            bank.getUserByIBAN(account.getIBAN()).getTranzactions().add(blockedOrFrozenError("frozen"));
            card.setStatus("frozen");
            return false;
        } else {
            account.setBalance(account.getBalance() - amount);
            return true;
        }
    }

    public ObjectNode blockedOrFrozenError(String error) {
        ObjectNode errorNode = mapper.createObjectNode();
        errorNode.put("timestamp", timestamp);
        errorNode.put("description", "The card is " + error);
        return errorNode;
    }
    private ObjectNode insufficientFunds() {
        ObjectNode finalNode = mapper.createObjectNode();
        finalNode.put("timestamp", timestamp);
        finalNode.put("description", "Insufficient funds");
        return finalNode;
    }
}
