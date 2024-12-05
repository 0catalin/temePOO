package org.poo.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bankGraph.Bank;
import org.poo.accounts.Account;
import org.poo.fileio.CommandInput;

import java.util.ArrayList;

public class DeleteCard implements Command{
    private String cardNumber;
    private int timestamp;

    public DeleteCard(CommandInput commandInput) {
        timestamp = commandInput.getTimestamp();
        cardNumber = commandInput.getCardNumber();
    }

    public void execute(Bank bank, ArrayNode output, ObjectMapper mapper) {
        Account account = bank.getAccountByCardNumber(cardNumber);
        if(account == null) {
            System.out.println("Card not found");
        } else {
            String IBAN = account.getIBAN();
            String email = bank.getUserByIBAN(IBAN).getEmail();
            bank.getUserByIBAN(IBAN).getTranzactions().computeIfAbsent(successfulDeletion(output, mapper, IBAN, email), k -> new ArrayList<>()).add(IBAN);
            account.getCards().remove(account.getCardByCardNumber(cardNumber));
        }

    }

    private ObjectNode successfulDeletion(ArrayNode output, ObjectMapper mapper, String IBAN, String email) {
        ObjectNode finalNode = mapper.createObjectNode();
        finalNode.put("timestamp", timestamp);
        finalNode.put("description", "The card has been destroyed");
        finalNode.put("card", cardNumber);
        finalNode.put("cardHolder", email);
        finalNode.put("account", IBAN);
        return finalNode;
    }
}