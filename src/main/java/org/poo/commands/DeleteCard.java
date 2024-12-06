package org.poo.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bankGraph.Bank;
import org.poo.accounts.Account;
import org.poo.fileio.CommandInput;


public class DeleteCard implements Command{
    private String cardNumber;
    private int timestamp;

    public DeleteCard(CommandInput commandInput) {
        timestamp = commandInput.getTimestamp();
        cardNumber = commandInput.getCardNumber();
    }

    public void execute() {
        Account account = Bank.getInstance().getAccountByCardNumber(cardNumber);
        if (account == null) {

        } else {
            String IBAN = account.getIBAN();
            String email = Bank.getInstance().getUserByIBAN(IBAN).getEmail();
            Bank.getInstance().getUserByIBAN(IBAN).getTranzactions().add(successfulDeletion(IBAN, email));
            account.getReportsClassic().add(successfulDeletion(IBAN, email));
            account.getCards().remove(account.getCardByCardNumber(cardNumber));
        }

    }

    private ObjectNode successfulDeletion(String IBAN, String email) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode finalNode = mapper.createObjectNode();
        finalNode.put("timestamp", timestamp);
        finalNode.put("description", "The card has been destroyed");
        finalNode.put("card", cardNumber);
        finalNode.put("cardHolder", email);
        finalNode.put("account", IBAN);
        return finalNode;
    }
}