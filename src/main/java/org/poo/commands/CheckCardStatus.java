package org.poo.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.accounts.Account;
import org.poo.bankGraph.Bank;
import org.poo.baseinput.User;
import org.poo.cards.Card;
import org.poo.fileio.CommandInput;

import java.util.ArrayList;

public class CheckCardStatus implements Command{
    private String cardNumber;
    private int timestamp;

    public CheckCardStatus(CommandInput commandInput) {
        timestamp = commandInput.getTimestamp();
        cardNumber = commandInput.getCardNumber();
    }

    public void execute(Bank bank, ArrayNode output, ObjectMapper mapper) {
        Card card = bank.getCardByCardNumber(cardNumber);
        Account account = bank.getAccountByCardNumber(cardNumber);
        if (card == null) {
            cardNotFoundError(output, mapper);
        } else {
            User user = bank.getUserByIBAN(account.getIBAN());
            if (account.getBalance() < account.getMinBalance()) {
                //user.getTranzactions().computeIfAbsent(cardStatusError("frozen", mapper), k -> new ArrayList<>()).add("");
                user.getTranzactions().add(cardStatusError("frozen", mapper));
            } else if (card.getStatus().equals("blocked")) {
                //user.getTranzactions().computeIfAbsent(cardStatusError("blocked", mapper), k -> new ArrayList<>()).add("");
                user.getTranzactions().add(cardStatusError("blocked", mapper));
            } else if (account.getBalance() - account.getMinBalance() <= 30 &&
                    account.getBalance() - account.getMinBalance() >= 0) {
                //user.getTranzactions().computeIfAbsent(cardStatusWarning(mapper), k -> new ArrayList<>()).add("");
                user.getTranzactions().add(cardStatusWarning(mapper));
            }
        }
    }

    private void cardNotFoundError(ArrayNode output, ObjectMapper mapper) {
        ObjectNode error = mapper.createObjectNode();
        error.put("command", "checkCardStatus");
        ObjectNode outputNode = mapper.createObjectNode();
        outputNode.put("timestamp", timestamp);
        outputNode.put("description", "Card not found");
        error.set("output", outputNode);
        error.put("timestamp", timestamp);
        output.add(error);
    }

    private ObjectNode cardStatusError(String status, ObjectMapper mapper) {
        ObjectNode node = mapper.createObjectNode();
        node.put("timestamp", timestamp);
        node.put("description", "The card is " + status);
        return node;
    }

    private ObjectNode cardStatusWarning(ObjectMapper mapper) {
        ObjectNode node = mapper.createObjectNode();
        node.put("timestamp", timestamp);
        node.put("description", "You have reached the minimum amount of funds, the card will be frozen");
        return node;
    }
}
