package org.poo.parsers.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.accounts.Account;
import org.poo.bankGraph.Bank;
import org.poo.baseinput.User;
import org.poo.exceptions.AccountNotFoundException;
import org.poo.parsers.fileio.CommandInput;

/**
 * class implementing the check card status command
 */
public final class CheckCardStatus implements Command {
    private String cardNumber;
    private int timestamp;


    public CheckCardStatus(final CommandInput commandInput) {
        timestamp = commandInput.getTimestamp();
        cardNumber = commandInput.getCardNumber();
    }



    /**
     * checks the card status if the account is found
     */
    @Override
    public void execute() {
        try {
            Account account = Bank.getInstance().getAccountByCardNumber(cardNumber);
            User user = Bank.getInstance().getUserByIBAN(account.getIban());
            if (account.getBalance() < account.getMinBalance()) {
                user.getTranzactions().add(cardFrozenError());
            } else if (account.isInWarningRange()) {
                user.getTranzactions().add(cardStatusWarning());
            }
        } catch (AccountNotFoundException e) {
            cardNotFoundError();
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
