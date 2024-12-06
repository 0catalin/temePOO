package org.poo.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bankGraph.Bank;
import org.poo.visitors.PayOnlineVisitor;
import org.poo.accounts.Account;
import org.poo.baseinput.User;
import org.poo.cards.Card;
import org.poo.fileio.CommandInput;

public final class PayOnline implements Command {
    private String cardNumber;
    private double amount;
    private int timestamp;
    private String currency;
    private String commerciant;
    private String email;

    public PayOnline(final CommandInput commandInput) {
        timestamp = commandInput.getTimestamp();
        email = commandInput.getEmail();
        currency = commandInput.getCurrency();
        amount = commandInput.getAmount();
        commerciant = commandInput.getCommerciant();
        cardNumber = commandInput.getCardNumber();
    }

    @Override
    public void execute() {
        Card card = Bank.getInstance().getCardByCardNumber(cardNumber);
        User user = Bank.getInstance().getUserByEmail(email);
        Account account = Bank.getInstance().getAccountByCardNumber(cardNumber);
        if (card == null) {
            cardNotFound();
        } else if (user == null) {
            // no need here
        } else if (!user.getAccounts().contains(account)) {
            cardNotFound();
        } else if (account.getBalance() != 0) {
                amount = amount * Bank.getInstance()
                        .findExchangeRate(currency, account.getCurrency());
                PayOnlineVisitor visitor = new PayOnlineVisitor(amount, timestamp,
                        commerciant, account);
                card.accept(visitor);
        }

    }

    private void cardNotFound() {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode finalNode = mapper.createObjectNode();
        finalNode.put("command", "payOnline");
        ObjectNode outputNode = mapper.createObjectNode();
        outputNode.put("description", "Card not found");
        outputNode.put("timestamp", timestamp);
        finalNode.set("output", outputNode);
        finalNode.put("timestamp", timestamp);
        Bank.getInstance().getOutput().add(finalNode);
    }






}
