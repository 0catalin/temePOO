package org.poo.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bankGraph.Bank;
import org.poo.accounts.Account;
import org.poo.fileio.CommandInput;


public final class DeleteCard implements Command {
    private String cardNumber;
    private int timestamp;

    public DeleteCard(final CommandInput commandInput) {
        timestamp = commandInput.getTimestamp();
        cardNumber = commandInput.getCardNumber();
    }

    @Override
    public void execute() {
        Account account = Bank.getInstance().getAccountByCardNumber(cardNumber);
        if (account == null) {

        } else {
            String iban = account.getIban();
            String email = Bank.getInstance().getUserByIBAN(iban).getEmail();
            Bank.getInstance().getUserByIBAN(iban)
                    .getTranzactions().add(successfulDeletion(iban, email));
            account.getReportsClassic().add(successfulDeletion(iban, email));
            account.getCards().remove(account.getCardByCardNumber(cardNumber));
        }

    }

    private ObjectNode successfulDeletion(final String iban, final String email) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode finalNode = mapper.createObjectNode();
        finalNode.put("timestamp", timestamp);
        finalNode.put("description", "The card has been destroyed");
        finalNode.put("card", cardNumber);
        finalNode.put("cardHolder", email);
        finalNode.put("account", iban);
        return finalNode;
    }
}
