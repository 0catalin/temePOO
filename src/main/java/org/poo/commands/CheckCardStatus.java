package org.poo.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.accounts.Account;
import org.poo.bankGraph.Bank;
import org.poo.baseinput.User;
import org.poo.cards.Card;
import org.poo.fileio.CommandInput;


public final class CheckCardStatus implements Command {
    private String cardNumber;
    private int timestamp;

    public CheckCardStatus(final CommandInput commandInput) {
        timestamp = commandInput.getTimestamp();
        cardNumber = commandInput.getCardNumber();
    }

    @Override
    public void execute() {
        Card card = Bank.getInstance().getCardByCardNumber(cardNumber);
        Account account = Bank.getInstance().getAccountByCardNumber(cardNumber);
        if (card == null) {
            cardNotFoundError();
        } else {
            User user = Bank.getInstance().getUserByIBAN(account.getIban());
            if (account.getBalance() < account.getMinBalance()) {
                user.getTranzactions().add(cardFrozenError());
            } else if (account.isInWarningRange()) {
                user.getTranzactions().add(cardStatusWarning());
            }
        }
    }

    private void cardNotFoundError() {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode error = mapper.createObjectNode();
        error.put("command", "checkCardStatus");
        ObjectNode outputNode = mapper.createObjectNode();
        outputNode.put("timestamp", timestamp);
        outputNode.put("description", "Card not found");
        error.set("output", outputNode);
        error.put("timestamp", timestamp);
        Bank.getInstance().getOutput().add(error);
    }

    private ObjectNode cardFrozenError() {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        node.put("timestamp", timestamp);
        node.put("description", "The card is frozen");
        return node;
    }

    private ObjectNode cardStatusWarning() {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        node.put("timestamp", timestamp);
        node.put("description",
                "You have reached the minimum amount of funds, the card will be frozen");
        return node;
    }
}
