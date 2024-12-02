package org.poo.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bankGraph.Bank;
import org.poo.PayOnlineVisitor;
import org.poo.accounts.Account;
import org.poo.baseinput.User;
import org.poo.cards.Card;
import org.poo.fileio.CommandInput;

public class PayOnline implements Command{
    private String cardNumber;
    private double amount;
    private int timestamp;
    private String currency;
    private String description;
    private String commerciant;
    private String email;

    public PayOnline(CommandInput commandInput) {
        timestamp = commandInput.getTimestamp();
        email = commandInput.getEmail();
        currency = commandInput.getCurrency();
        amount = commandInput.getAmount();
        commerciant = commandInput.getCommerciant();
        description = commandInput.getDescription();
        cardNumber = commandInput.getCardNumber();
    }

    public void execute(Bank bank, ArrayNode output, ObjectMapper mapper) {
        Card card = bank.getCardByCardNumber(cardNumber);
        User user = bank.getUserByEmail(email);
        Account account = bank.getAccountByCardNumber(cardNumber);
        if(card == null) {
            cardNotFound(output, mapper);
        } else if (user == null) {
            System.out.println("User not found"); // de sters, probabil
        } else if (!user.getAccounts().contains(account)) {
            // System.out.println("Card does not belong to user");
            cardNotFound(output, mapper);
        } else if (account.getBalance() < amount * bank.findExchangeRate(currency, account.getCurrency())) {
            user.getTranzactions().add(insufficientFunds(output, mapper));
        }
        else {
            amount = amount * bank.findExchangeRate(currency, account.getCurrency()); // converts to specific amount
            user.getTranzactions().add(successfulPayment(output, mapper));
            PayOnlineVisitor visitor = new PayOnlineVisitor(amount, timestamp, description,
                    commerciant, mapper, output, account);
            card.accept(visitor);
        }

    }

    private void cardNotFound(ArrayNode output, ObjectMapper mapper) {
        ObjectNode finalNode = mapper.createObjectNode();
        finalNode.put("command", "payOnline");
        ObjectNode outputNode = mapper.createObjectNode();
        outputNode.put("description", "Card not found");
        outputNode.put("timestamp", timestamp);
        finalNode.set("output", outputNode);
        finalNode.put("timestamp", timestamp);
        output.add(finalNode);
    }

    private ObjectNode insufficientFunds(ArrayNode output, ObjectMapper mapper) {
        ObjectNode finalNode = mapper.createObjectNode();
        finalNode.put("timestamp", timestamp);
        finalNode.put("description", "Insufficient funds");
        return finalNode;
    }

    private ObjectNode successfulPayment(ArrayNode output, ObjectMapper mapper) {
        ObjectNode finalNode = mapper.createObjectNode();
        finalNode.put("timestamp", timestamp);
        finalNode.put("description", "Card payment");
        finalNode.put("amount", amount);
        finalNode.put("commerciant", commerciant);
        return finalNode;
    }
}
